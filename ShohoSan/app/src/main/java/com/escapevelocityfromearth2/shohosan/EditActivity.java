package com.escapevelocityfromearth2.shohosan;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.escapevelocityfromearth2.shohosan.database.DbManager;
import com.escapevelocityfromearth2.shohosan.database.DrugDbHelper;

import java.util.HashMap;

import static com.escapevelocityfromearth2.shohosan.CameraActivity.DATE;
import static com.escapevelocityfromearth2.shohosan.CameraActivity.DRUG_NAME;
import static com.escapevelocityfromearth2.shohosan.CameraActivity.KEY_INTENT_OCR_RESULT;
import static com.escapevelocityfromearth2.shohosan.CameraActivity.NUMBER;
import static com.escapevelocityfromearth2.shohosan.CameraActivity.WHEN_TO_HAVE;

public class EditActivity extends AppCompatActivity {

    EditText mDrugNameEditText;
    EditText mTimingEditText;
    EditText mFrequencyEditText;
    EditText mOthersEditText;

    String mDrugNameText;
    String mTimingText;
    String mFrequencyText;
    String mOthersText;

    Button mSubmitButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit);
        mOthersEditText = (EditText) findViewById(R.id.others_edit_text);
        mDrugNameEditText = (EditText) findViewById(R.id.drug_name_edit_text);
        mTimingEditText = (EditText) findViewById(R.id.timing_edit_text);
        mFrequencyEditText = (EditText) findViewById(R.id.frequency_edit_text);
        mSubmitButton = (Button) findViewById(R.id.submit_button);
        mSubmitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DbManager.register(getApplicationContext(), new DrugData(null, null, -3, mDrugNameText, DrugData.MEDICAL_TIME.AFTER_MEAL, 5, 1, System.currentTimeMillis()));
                EditActivity.this.finish();
                startActivity(new Intent(getApplicationContext(), MainActivity.class));
            }
        });

        HashMap<String, String> requestResult = (HashMap<String, String>) getIntent().getSerializableExtra(KEY_INTENT_OCR_RESULT);
        if(requestResult != null) {
            mDrugNameText = requestResult.get(DRUG_NAME);
            mTimingText = requestResult.get(DATE);
            mFrequencyText = requestResult.get(WHEN_TO_HAVE);
            mOthersText = requestResult.get(NUMBER);

            mDrugNameEditText.setText(mDrugNameText);
            mTimingEditText.setText(mTimingText);
            mFrequencyEditText.setText(mFrequencyText);
            mOthersEditText.setText(mOthersText);
        }


    }
}
