package com.sih.hawkeye.ocr;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.sih.hawkeye.R;

public class OcrActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ocr);

        setTitle(getString(R.string.ocr_activity_title));
    }
}