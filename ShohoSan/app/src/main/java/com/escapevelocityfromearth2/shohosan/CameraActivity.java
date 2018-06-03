package com.escapevelocityfromearth2.shohosan;

import android.Manifest;
import android.annotation.TargetApi;
import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.ImageReader;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.PermissionChecker;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.api.client.extensions.android.http.AndroidHttp;
import com.google.api.client.googleapis.json.GoogleJsonResponseException;
import com.google.api.client.http.HttpTransport;

import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.gson.GsonFactory;
import com.google.api.services.vision.v1.Vision;
import com.google.api.services.vision.v1.VisionRequestInitializer;
import com.google.api.services.vision.v1.model.AnnotateImageRequest;
import com.google.api.services.vision.v1.model.BatchAnnotateImagesRequest;
import com.google.api.services.vision.v1.model.BatchAnnotateImagesResponse;
import com.google.api.services.vision.v1.model.EntityAnnotation;
import com.google.api.services.vision.v1.model.Feature;
import com.google.api.services.vision.v1.model.Image;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class CameraActivity extends AppCompatActivity implements View.OnClickListener{

    private androidCamera2Sample mTextureView;
    private ImageView mImageView;
    private Camera2 mCamera2;
    private int REQUEST_CODE_CAMERA_PERMISSION = 1;
    private Button shtBtn;
    private Button okButton;
    private Button cancelButton;

    private int REQUEST_GALLERY = 0;
    private Context mContext = this ;
    private int REQUEST_CODE_STORAGE_PERMISSION = 1;
    static public String KEY_INTENT_OCR_RESULT = "key_intent_ocr_result";

    private Bitmap mBitmap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_camera);
        shtBtn = (Button)findViewById(R.id.shutter_btn);
        shtBtn.setOnClickListener(this);
        okButton = (Button)findViewById(R.id.ok_button);
        okButton.setOnClickListener(this);
        cancelButton = (Button)findViewById(R.id.cancel_button);
        cancelButton.setOnClickListener(this);

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            requestCameraPermissions();
        }
        if(PermissionChecker.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED){
            requestStoragePermissions();
        }
        mTextureView = (androidCamera2Sample) findViewById(R.id.textureView);
        mImageView = (ImageView) findViewById(R.id.imageView2);
        okButton.setVisibility(View.INVISIBLE);
        cancelButton.setVisibility(View.INVISIBLE);
        mCamera2 = new Camera2();
    }

    public void onRequestPermissionsResult(int requestCode , String[] permissions, int[] grantResults) {
        if (REQUEST_CODE_CAMERA_PERMISSION == requestCode) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {

            } else {
                // 拒否された
                Toast.makeText(this, "拒否された", Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        mCamera2.open(this, mTextureView);
    }
    @Override
    protected void onPause() {
        super.onPause();
        mCamera2.close();
    }
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && mImageView.getVisibility() == View.VISIBLE) {
            mTextureView.setVisibility(View.VISIBLE);
            mImageView.setVisibility(View.INVISIBLE);
            okButton.setVisibility(View.INVISIBLE);
            cancelButton.setVisibility(View.INVISIBLE);
            return false;
        }
        return super.onKeyDown(keyCode, event);
    }

    @TargetApi(Build.VERSION_CODES.KITKAT)
    @Override
    public void onClick(View view) {
        switch(view.getId()) {
            case R.id.shutter_btn:
                mCamera2.takePicture(new ImageReader.OnImageAvailableListener() {
                    @Override
                    public void onImageAvailable(ImageReader reader) {
                        // 撮れた画像をImageViewに貼り付けて表示。
                        final android.media.Image image = reader.acquireLatestImage();
                        ByteBuffer buffer = image.getPlanes()[0].getBuffer();
                        byte[] bytes = new byte[buffer.remaining()];
                        buffer.get(bytes);
                        Bitmap bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
                        image.close();
                        mBitmap = bitmap;
                        saveImage(bitmap);
                        mImageView.setImageBitmap(bitmap);
                        mImageView.setVisibility(View.VISIBLE);
                        mTextureView.setVisibility(View.INVISIBLE);
                        okButton.setVisibility(View.VISIBLE);
                        cancelButton.setVisibility(View.VISIBLE);
                        shtBtn.setVisibility(View.INVISIBLE);
                    }
                });
                break;
            case R.id.ok_button:
                try {
                    callCloudVision(mBitmap, this);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                break;
            case R.id.cancel_button:
                mTextureView.setVisibility(View.VISIBLE);
                mImageView.setVisibility(View.INVISIBLE);
                okButton.setVisibility(View.INVISIBLE);
                shtBtn.setVisibility(View.VISIBLE);
                cancelButton.setVisibility(View.INVISIBLE);
                break;

            default:
                break;
        }

    }

    static private void callCloudVision(final Bitmap bitmap, final Context context) throws IOException {

        // Do the real work in an async task, because we need to use the network anyway
        new AsyncTask<Object, Void, String>() {

            ProgressDialog progressDialog;

            @Override
            protected void onPreExecute(){
                progressDialog = new ProgressDialog(context);
                progressDialog.setMessage("画像を分析しています ...");
                progressDialog.show();
            }

            @Override
            protected String doInBackground(Object... params) {
                try {

                    HttpTransport httpTransport = AndroidHttp.newCompatibleTransport();
                    JsonFactory jsonFactory = GsonFactory.getDefaultInstance();

                    Vision.Builder builder = new Vision.Builder(httpTransport, jsonFactory, null);
                    builder.setVisionRequestInitializer(new VisionRequestInitializer("AIzaSyBgYO94PqGDESiNwiLLQQWPJHfkDFAsPWk"));
                    builder.setApplicationName("shohosan");
                    Vision vision = builder.build();

                    BatchAnnotateImagesRequest batchAnnotateImagesRequest =
                            new BatchAnnotateImagesRequest();
                    batchAnnotateImagesRequest.setRequests(new ArrayList<AnnotateImageRequest>() {{
                        AnnotateImageRequest annotateImageRequest = new AnnotateImageRequest();
                        // Add the image
                        Image base64EncodedImage = new Image();
                        // Convert the bitmap to a JPEG
                        // Just in case it's a format that Android understands but Cloud Vision
                        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
                        bitmap.compress(Bitmap.CompressFormat.JPEG, 90, byteArrayOutputStream);
                        byte[] imageBytes = byteArrayOutputStream.toByteArray();

                        // Base64 encode the JPEG
                        base64EncodedImage.encodeContent(imageBytes);
                        annotateImageRequest.setImage(base64EncodedImage);

                        // add the features we want
                        annotateImageRequest.setFeatures(new ArrayList<Feature>() {{
                            Feature labelDetection = new Feature();
                            labelDetection.setType("TEXT_DETECTION");
                            labelDetection.setMaxResults(10);
                            add(labelDetection);
                        }});

                        // Add the list of one thing to the request
                        add(annotateImageRequest);
                    }});

                    Vision.Images.Annotate annotateRequest =
                            vision.images().annotate(batchAnnotateImagesRequest);
                    // Due to a bug: requests to Vision API containing large images fail when GZipped.
                    annotateRequest.setDisableGZipContent(true);
                    Log.d("CameraActivity", "created Cloud Vision request object, sending request");

                    BatchAnnotateImagesResponse response = annotateRequest.execute();
                    System.out.println(convertResponseToString(response));
                    Log.d("CameraActivity", "reqest recieved " + response);
                    return convertResponseToString(response);

                } catch (GoogleJsonResponseException e) {
                    Log.d("CameraActivity", "failed to make API request because " + e.getContent());
                } catch (IOException e) {
                    Log.d("CameraActivity", "failed to make API request because of other IOException " +
                            e.getMessage());
                } finally {
                    progressDialog.dismiss();
                }
                return "Cloud Vision API request failed. Check logs for details.";
            }

            protected void onPostExecute(String result) {
                Log.d("CameraActivity", "Congrats! Request Recieved!!!!!");
                progressDialog.dismiss();
                Intent intent = new Intent(context, EditActivity.class);
                intent.putExtra(KEY_INTENT_OCR_RESULT, result);
                context.startActivity(intent);
                //mImageDetails.setText(result);

            }
        }.execute();
    }

    private static String convertResponseToString(BatchAnnotateImagesResponse response) {
        String message = null;
        List<EntityAnnotation> labels = response.getResponses().get(0).getTextAnnotations();
        if (labels != null) {
            for (EntityAnnotation label : labels) {
                message += String.format("%.3f: %s", label.getScore(), label.getDescription());
                message += "\n";

            }
        } else {
            message += "nothing";
        }

        return message;
    }


    private void saveImage(Bitmap image){
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    ActivityCompat.requestPermissions(CameraActivity.this,
                            new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                            REQUEST_CODE_CAMERA_PERMISSION);
                }
            };
        }
        String path = Environment.getExternalStorageDirectory() + "/DCIM/";
        Date mDate = new Date();
        SimpleDateFormat fileNameDate = new SimpleDateFormat("yyyyMMdd_HHmmss");
        String fileName = fileNameDate.format(mDate) + ".jpg";
        File file = new File(path,fileName);
        String AttachName = file.getAbsolutePath();
        Log.d("CameraActivity", "AttachName"+AttachName);
        if(file.exists()){
            file.delete();
        }
        try{
            FileOutputStream out = new FileOutputStream(AttachName);
            image.compress(Bitmap.CompressFormat.PNG, 100, out);
            out.flush();
            out.close();
        }catch(Exception e){
            e.printStackTrace();
        }
        try{
            showFolder(file);

        }catch (Exception e){
            e.printStackTrace();
        }

    }


    private void requestCameraPermissions() {
        if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.CAMERA)) {
            new AlertDialog.Builder(this)
                    .setTitle("パーミッションの説明")
                    .setMessage("このアプリで保存を行うにはパーミッションが必要です。")
                    .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            ActivityCompat.requestPermissions(CameraActivity.this, new String[]{Manifest.permission.CAMERA}, REQUEST_CODE_CAMERA_PERMISSION);
                        }
                    })
                    .create()
                    .show();
            return;
        }
        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA}, REQUEST_CODE_CAMERA_PERMISSION);
        return;
    }

    private void requestStoragePermissions() {
        if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
            new AlertDialog.Builder(this)
                    .setTitle("パーミッションの説明")
                    .setMessage("このアプリで保存を行うにはパーミッションが必要です。")
                    .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            ActivityCompat.requestPermissions(CameraActivity.this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, REQUEST_CODE_STORAGE_PERMISSION);
                        }
                    })
                    .create()
                    .show();
            return;
        }
        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, REQUEST_CODE_STORAGE_PERMISSION);
        return;
    }


    private void showFolder(File path) throws Exception {
        try {
            ContentValues values = new ContentValues();
            ContentResolver contentResolver = mContext.getContentResolver();
            values.put(MediaStore.Images.Media.MIME_TYPE, "image/jpeg");
            values.put(MediaStore.Images.Media.DATE_MODIFIED, System.currentTimeMillis() / 1000);
            values.put(MediaStore.Images.Media.SIZE, path.length());
            values.put(MediaStore.Images.Media.TITLE, path.getName());
            values.put(MediaStore.Images.Media.DATA, path.getPath());
            contentResolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);
        } catch (Exception e) {
            throw e;
        }
    }



    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_GALLERY && resultCode == RESULT_OK) {
            try {
                InputStream in = getContentResolver().openInputStream(
                        data.getData());
                Bitmap img = BitmapFactory.decodeStream(in);
                // 新しいフォルダにギャラリーから選ん画像を保存
                saveImage(img);
                in.close();
                // サイズ変更せずにエリアの中央に表示
                mImageView.setScaleType(ImageView.ScaleType.CENTER);
                // ギャラリーで選んだ画像を表示
                mImageView.setImageBitmap(img);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
