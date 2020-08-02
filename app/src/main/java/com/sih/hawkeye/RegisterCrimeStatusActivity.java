package com.sih.hawkeye;

import android.appwidget.AppWidgetManager;
import android.content.ComponentName;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import androidx.annotation.NonNull;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import androidx.annotation.RequiresApi;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.View;
import com.google.android.material.navigation.NavigationView;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RemoteViews;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import static com.sih.hawkeye.PublicLoginActivity.mGoogleSignInClient;

public class RegisterCrimeStatusActivity extends AppCompatActivity {

    RecyclerViewAdapter adapter;

    public static List<Issue> issueList = new ArrayList<>();

    DatabaseReference databaseIssue;

    ProgressBar progressBarLodingIssuesForInmates;

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_issue_status);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);

        progressBarLodingIssuesForInmates = findViewById(R.id.loading_issues_for_inmates);
        progressBarLodingIssuesForInmates.setVisibility(View.VISIBLE);

        String personName = getIntent().getExtras().get("PERSON_NAME").toString();
        final String personEmail = getIntent().getExtras().get("PERSON_EMAIL").toString();
        String profilePicUri = getIntent().getExtras().get("PROFILE_PIC").toString();

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(),ReportAnIssueActivity.class);
                intent.putExtra("PERSON_EMAIL",personEmail);
                startActivity(intent);
            }
        });

        databaseIssue = FirebaseDatabase.getInstance().getReference("issues");
        databaseIssue.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                issueList.clear();
                for(DataSnapshot issueSnapshot : dataSnapshot.getChildren()){
                    Issue issue = issueSnapshot.getValue(Issue.class);
                    if(issue.issueReportedBy.equals(personEmail)) {
                        issueList.add(issue);
                    }
                }

                if(issueList.size() != 0)
                updateWidget(issueList.get(issueList.size()-1).issueStatus);

                RecyclerView recyclerView = (RecyclerView) findViewById(R.id.rv_issue_status);
                recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
                adapter = new RecyclerViewAdapter(getApplicationContext(), issueList);
                recyclerView.setAdapter(adapter);
                progressBarLodingIssuesForInmates.setVisibility(View.GONE);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

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

    void updateWidget(String isFixed){
        AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(this);
        RemoteViews remoteViews = new RemoteViews(this.getPackageName(), R.layout.widget_for_inmates);
        ComponentName thisWidget = new ComponentName(this, WidgetForInmates.class);
        String widgetText = issueList.get(issueList.size()-1).issueTitle;
        remoteViews.setTextViewText(R.id.appwidget_text, widgetText);
        if(isFixed.equals("Fixed")) {
            remoteViews.setImageViewResource(R.id.img_widget, R.drawable.hostel_green);
        }else {
            remoteViews.setImageViewResource(R.id.img_widget, R.drawable.hostel_red);
        }
        appWidgetManager.updateAppWidget(thisWidget, remoteViews);

    }
}
