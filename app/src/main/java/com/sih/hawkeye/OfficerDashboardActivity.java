package com.sih.hawkeye;

import android.content.Intent;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;

import com.sih.hawkeye.ocr.OcrActivity;
import com.sih.hawkeye.views.BeatsAllocationActivity;
import com.sih.hawkeye.views.FaceRecognitionActivity;

public class OfficerDashboardActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_officer_dashboard);
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.actionbar_menu, menu);
        return true;
    }

    public void reportedCrimesBtn(View view){
        Intent intent = new Intent(getApplicationContext(), ReportedCrimesActivity.class);
        startActivity(intent);
    }
    public void ocrBtn(View view){
        Intent intent = new Intent(getApplicationContext(), OcrActivity.class);
        startActivity(intent);
    }
    public void faceRecognitionBtn(View view){
        Intent intent = new Intent(getApplicationContext(), FaceRecognitionActivity.class);
        startActivity(intent);
    }
    public void beatsAllocationBtn(View view){
        Intent intent = new Intent(getApplicationContext(), BeatsAllocationActivity.class);
        startActivity(intent);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        switch (item.getItemId()) {
            case R.id.control_panel:
                Intent intent = new Intent(this, ControlPanelActivity.class);
                startActivity(intent);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}