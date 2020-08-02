package com.sih.hawkeye.ocr;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.net.Uri;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.sih.hawkeye.R;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class NumberPlateActivity extends AppCompatActivity {

    String vehicleRegNo;
    String vehicleImageUrl;
    String cloudUrl = "error";

    EditText editTextVehicleRegNo;
    ImageView imageViewVehicleImage;
    Button buttonReport;
    ProgressBar pbLoading;

    DatabaseReference databaseVehicle;

    FirebaseStorage storage = FirebaseStorage.getInstance();
    StorageReference storageRef = storage.getReference();

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
        pbLoading = findViewById(R.id.pbLoading);

        editTextVehicleRegNo.setText(vehicleRegNo);
        imageViewVehicleImage.setImageURI(Uri.parse(vehicleImageUrl));

        buttonReport.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                vehicleRegNo = editTextVehicleRegNo.getText().toString();
                uploadVehicleImageToCloudStorage(Uri.parse(vehicleImageUrl));
            }
        });
    }

    private void uploadVehicleImageToCloudStorage(Uri localUri){
        pbLoading.setVisibility(View.VISIBLE);
        buttonReport.setClickable(false);

        final StorageReference riversRef = storageRef.child("vehicle_images/" + "vehicle_" + getTimeStamp());
        UploadTask uploadTask =  riversRef.putFile(localUri);

// Register observers to listen for when the download is done or if it fails
/*        uploadTask.addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                // Handle unsuccessful uploads
            }
        }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                // taskSnapshot.getMetadata() contains file metadata such as size, content-type, etc.
                // ...
            }
        });*/

        Task<Uri> urlTask = uploadTask.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
            @Override
            public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                if (!task.isSuccessful()) {
                    throw task.getException();
                }

                // Continue with the task to get the download URL
                return riversRef.getDownloadUrl();
            }
        }).addOnCompleteListener(new OnCompleteListener<Uri>() {
            @Override
            public void onComplete(@NonNull Task<Uri> task) {
                if (task.isSuccessful()) {
                    Uri downloadUri = task.getResult();
                    cloudUrl = downloadUri.toString();
                    reportVehicle();
                } else {
                    // Handle failures
                    // ...
                }
            }
        });
    }

    public void reportVehicle(){

        String id = databaseVehicle.push().getKey();
        String date = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault()).format(new Date());

        Vehicle vehicle = new Vehicle(id, vehicleRegNo, cloudUrl, date);

        databaseVehicle.child(id).setValue(vehicle);

        pbLoading.setVisibility(View.GONE);
        buttonReport.setClickable(true);
        Toast.makeText(this,"Vehicle reported",Toast.LENGTH_LONG).show();
        finish();
    }

    private String getTimeStamp(){
        return DateFormat.format("yyyyMMddhhmmss", new Date()).toString();
    }
}