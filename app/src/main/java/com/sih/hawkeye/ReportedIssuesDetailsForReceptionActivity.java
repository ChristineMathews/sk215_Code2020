package com.sih.hawkeye;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Base64;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class ReportedIssuesDetailsForReceptionActivity extends AppCompatActivity {

    TextView blockTextView;
    TextView roomTextView;
    TextView descriptionTextView;
    TextView reportedByTextView;
    Button mButton;
    ImageView mImageView;
    ImageView tickButton;

    int position;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reported_issues_details_for_reception);

        position = getIntent().getIntExtra("POSITION_ID",0);

        blockTextView = findViewById(R.id.tv_block);
        roomTextView = findViewById(R.id.tv_room);
        descriptionTextView = findViewById(R.id.tv_description);
        reportedByTextView = findViewById(R.id.tv_reported_by);
        mButton = findViewById(R.id.mark_as_fixed);
        mImageView = findViewById(R.id.img_issue);
        tickButton = findViewById(R.id.tick_mark);

        setTitle(ReportedCrimesActivity.issueList.get(position).issueTitle);
        blockTextView.setText(ReportedCrimesActivity.issueList.get(position).issueBlock);
        roomTextView.setText(ReportedCrimesActivity.issueList.get(position).issueRoom);
        descriptionTextView.setText(ReportedCrimesActivity.issueList.get(position).issueDescription);
        reportedByTextView.setText(ReportedCrimesActivity.issueList.get(position).issueReportedBy);

        try {
            Bitmap imageBitmap = decodeFromFirebaseBase64(ReportedCrimesActivity.issueList.get(position).imageEncoded);
            mImageView.setImageBitmap(imageBitmap);
        }catch (Exception e){
            e.printStackTrace();
        }

        if(ReportedCrimesActivity.issueList.get(position).issueStatus.equals("Fixed")){
            mButton.setVisibility(View.INVISIBLE);
            tickButton.setVisibility(View.VISIBLE);
        }else {
            mButton.setVisibility(View.VISIBLE);
            tickButton.setVisibility(View.INVISIBLE);
        }
    }

    public void updateIssueStatus(View view){
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("issues")
                .child(ReportedCrimesActivity
                        .issueList.get(position).issueId);

        String status = "Fixed";

        String id = ReportedCrimesActivity.issueList.get(position).issueId;
        String title = ReportedCrimesActivity.issueList.get(position).issueTitle;
        String block = ReportedCrimesActivity.issueList.get(position).issueBlock;
        String room = ReportedCrimesActivity.issueList.get(position).issueRoom;
        String description = ReportedCrimesActivity.issueList.get(position).issueDescription;
        String reportedBy = ReportedCrimesActivity.issueList.get(position).issueReportedBy;
        String date = ReportedCrimesActivity.issueList.get(position).issueDate;
        String imageEncoded = ReportedCrimesActivity.issueList.get(position).imageEncoded;

        Issue issue = new Issue(id, title, block, room, description, reportedBy, date, status, imageEncoded);

        databaseReference.setValue(issue);

        mButton.setVisibility(View.INVISIBLE);
        tickButton.setVisibility(View.VISIBLE);
    }

    public static Bitmap decodeFromFirebaseBase64(String image) {
        byte[] decodedByteArray = android.util.Base64.decode(image, Base64.DEFAULT);
        return BitmapFactory.decodeByteArray(decodedByteArray, 0, decodedByteArray.length);
    }
}
