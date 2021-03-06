package com.sih.hawkeye.ocr;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.FileProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.text.format.DateFormat;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.mlkit.vision.common.InputImage;
import com.google.mlkit.vision.text.Text;
import com.google.mlkit.vision.text.TextRecognition;
import com.google.mlkit.vision.text.TextRecognizer;
import com.sih.hawkeye.Issue;
import com.sih.hawkeye.R;
import com.sih.hawkeye.RecyclerViewAdapter;
import com.soundcloud.android.crop.Crop;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;

public class OcrActivity extends AppCompatActivity {

    private static final int REQUEST_CODE_ASK_MULTIPLE_PERMISSIONS = 101;
    String CAMERA_PERMISSION = android.Manifest.permission.CAMERA;

    String READ_EXTERNAL_STORAGE_PERMISSION = android.Manifest.permission.READ_EXTERNAL_STORAGE;
    String WRITE_EXTERNAL_STORAGE_PERMISSION = android.Manifest.permission.WRITE_EXTERNAL_STORAGE;

    private static final int CAMERA_REQUEST = 1;

    Bitmap ocrPhoto;
    Uri imageFileUri;
    Uri croppedImageFileUri;

    FloatingActionButton fab;

    DatabaseReference databaseVehicle;
    VehicleAdapter adapter;

    public static List<Vehicle> vehicleList = new ArrayList<>();

    ProgressBar pbLoading;

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ocr);

        fab = findViewById(R.id.ocrFab);
        pbLoading = findViewById(R.id.pbLoading);
        pbLoading.setVisibility(View.VISIBLE);

        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        setTitle(getString(R.string.ocr_activity_title));

        databaseVehicle = FirebaseDatabase.getInstance().getReference("vehicles");
        databaseVehicle.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                vehicleList.clear();
                for(DataSnapshot issueSnapshot : dataSnapshot.getChildren()){
                    Vehicle vehicle = issueSnapshot.getValue(Vehicle.class);
                    vehicleList.add(vehicle);
                }

                RecyclerView recyclerView = (RecyclerView) findViewById(R.id.rvReportedVehicles);
                recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
                adapter = new VehicleAdapter(getApplicationContext(), vehicleList);
                recyclerView.setAdapter(adapter);
                pbLoading.setVisibility(View.GONE);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    checkMultiplePermissions(REQUEST_CODE_ASK_MULTIPLE_PERMISSIONS, OcrActivity.this);
                } else {
                    // Open your camera here.
                    takePhoto();
                }
            }
        });

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
        }
        return true;
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == CAMERA_REQUEST && resultCode == Activity.RESULT_OK) {

            Crop.of(imageFileUri, croppedImageFileUri).start(this);

        }

        if (requestCode == Crop.REQUEST_CROP && resultCode == RESULT_OK) {
            try {
                Bitmap ocrBitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), croppedImageFileUri);
                runTextRecognition(ocrBitmap);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    //check for camera and storage access permissions
    @TargetApi(Build.VERSION_CODES.M)
    private void checkMultiplePermissions(int permissionCode, Context context) {

        String[] PERMISSIONS = {CAMERA_PERMISSION, READ_EXTERNAL_STORAGE_PERMISSION, WRITE_EXTERNAL_STORAGE_PERMISSION};
        if (!hasPermissions(context, PERMISSIONS)) {
            ActivityCompat.requestPermissions((Activity) context, PERMISSIONS, permissionCode);
        } else {
            // Open your camera here.
            takePhoto();
        }
    }

    private boolean hasPermissions(Context context, String... permissions) {
        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && context != null && permissions != null) {
            for (String permission : permissions) {
                if (ActivityCompat.checkSelfPermission(context, permission) != PackageManager.PERMISSION_GRANTED) {
                    return false;
                }
            }
        }
        return true;
    }

    @TargetApi(Build.VERSION_CODES.M)
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case REQUEST_CODE_ASK_MULTIPLE_PERMISSIONS:
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED && grantResults[1] == PackageManager.PERMISSION_GRANTED && grantResults[2] == PackageManager.PERMISSION_GRANTED) {
                    // Call your camera here.
                    takePhoto();
                }
                break;
            default:
                super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }

    public void takePhoto() {
        final String dir =  Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES)+ "/Garuda/";
        File garudaDir = new File(dir);
        garudaDir.mkdirs();

        String rawFile = dir + "ocr_" + DateFormat.format("yyyy-MM-dd_hhmmss", new Date()).toString()+".jpg";
        String cropFile = dir + "ocr_crop_" + DateFormat.format("yyyy-MM-dd_hhmmss", new Date()).toString()+".jpg";

        File mRawFile = new File(rawFile);
        File mCropFile = new File(cropFile);
        try {
            mRawFile.createNewFile();
            mCropFile.createNewFile();
        } catch (IOException e) {}

        imageFileUri = FileProvider.getUriForFile(this, this.getApplicationContext().getPackageName() + ".provider", mRawFile);
        croppedImageFileUri = FileProvider.getUriForFile(this, this.getApplicationContext().getPackageName() + ".provider", mCropFile);

        Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        cameraIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, imageFileUri);
        startActivityForResult(cameraIntent, CAMERA_REQUEST);
    }

    // OCR
    private void runTextRecognition(Bitmap bitmap) {
        pbLoading.setVisibility(View.VISIBLE);
        InputImage image = InputImage.fromBitmap(bitmap, 0);
        TextRecognizer recognizer = TextRecognition.getClient();
//        mTextButton.setEnabled(false);
        recognizer.process(image)
                .addOnSuccessListener(new OnSuccessListener<Text>() {
                    @Override
                    public void onSuccess(Text text) {
                        Toast.makeText(OcrActivity.this, "REG_NUMBER: " + text.getText(), Toast.LENGTH_SHORT).show();
                        goToNumberPlateActivity(text.getText(), imageFileUri.toString());
                    }
                })
                .addOnFailureListener(
                        new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {

                                Toast.makeText(OcrActivity.this, "Network issue", Toast.LENGTH_SHORT).show();
                                // Task failed with an exception
//                                mTextButton.setEnabled(true);
                                e.printStackTrace();
                            }
                        });
    }
    public void goToNumberPlateActivity(String vehicleRegNo, String imgUri){
        pbLoading.setVisibility(View.GONE);
        Intent intent = new Intent(getApplicationContext(), NumberPlateActivity.class);
        intent.putExtra("KEY_VEHICLE_REG_NO", vehicleRegNo);
        intent.putExtra("KEY_VEHICLE_IMAGE_URL", imgUri);
        startActivity(intent);
    }
}