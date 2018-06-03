package com.escapevelocityfromearth2.shohosan;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.EditText;

import static com.escapevelocityfromearth2.shohosan.CameraActivity.KEY_INTENT_OCR_RESULT;

public class EditActivity extends AppCompatActivity {

    EditText mOthersEditText;
    String mOthersText;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit);
        mOthersEditText = (EditText) findViewById(R.id.others_edit_text);
        mOthersText = getIntent().getStringExtra(KEY_INTENT_OCR_RESULT);
        if(mOthersText == null){
            mOthersEditText.setText(R.string.others);
        } else {
            mOthersEditText.setText(mOthersText);
        }

    }
}
