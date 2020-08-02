package com.sih.hawkeye.ocr;

import androidx.appcompat.app.AppCompatActivity;

import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.sih.hawkeye.Issue;
import com.sih.hawkeye.R;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class NumberPlateActivity extends AppCompatActivity {

    String vehicleRegNo;
    String vehicleImageUrl;

    EditText editTextVehicleRegNo;
    ImageView imageViewVehicleImage;
    Button buttonReport;

    DatabaseReference databaseVehicle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_number_plate);

        databaseVehicle = FirebaseDatabase.getInstance().getReference("vehicles");

        vehicleRegNo = getIntent().getStringExtra("KEY_VEHICLE_REG_NO");
        vehicleImageUrl = getIntent().getStringExtra("KEY_VEHICLE_IMAGE_URL");

        editTextVehicleRegNo = findViewById(R.id.edVehicleRegNo);
        imageViewVehicleImage = findViewById(R.id.ivVehicleImg);
        buttonReport = findViewById(R.id.btnReportVehicle);

        editTextVehicleRegNo.setText(vehicleRegNo);
        imageViewVehicleImage.setImageURI(Uri.parse(vehicleImageUrl));

        buttonReport.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                vehicleRegNo = editTextVehicleRegNo.getText().toString();
                reportVehicle();
            }
        });
    }

    public void reportVehicle(){

        String id = databaseVehicle.push().getKey();
        String date = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault()).format(new Date());

        Vehicle vehicle = new Vehicle(id, vehicleRegNo, "dummyImgUrl", date);

        databaseVehicle.child(id).setValue(vehicle);

        Toast.makeText(this,"Vehicle reported",Toast.LENGTH_LONG).show();
        finish();
    }
}