package com.sih.hawkeye.ocr;

import androidx.appcompat.app.AppCompatActivity;

import android.net.Uri;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.ImageView;

import com.sih.hawkeye.R;

public class NumberPlateActivity extends AppCompatActivity {

    String vehicleRegNo;
    String vehicleImageUrl;

    EditText editTextVehicleRegNo;
    ImageView imageViewVehicleImage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_number_plate);

        vehicleRegNo = getIntent().getStringExtra("KEY_VEHICLE_REG_NO");
        vehicleImageUrl = getIntent().getStringExtra("KEY_VEHICLE_IMAGE_URL");

        editTextVehicleRegNo = findViewById(R.id.edVehicleRegNo);
        imageViewVehicleImage = findViewById(R.id.ivVehicleImg);

        editTextVehicleRegNo.setText(vehicleRegNo);
        imageViewVehicleImage.setImageURI(Uri.parse(vehicleImageUrl));
    }
}