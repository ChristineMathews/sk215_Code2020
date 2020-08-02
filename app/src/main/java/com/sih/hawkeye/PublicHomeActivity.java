package com.sih.hawkeye;

import android.content.Intent;
import android.net.Uri;
import androidx.annotation.NonNull;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.google.android.material.navigation.NavigationView;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.appcompat.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.sih.hawkeye.views.CyberAwarenessActivity;
import com.sih.hawkeye.views.NocActivity;
import com.sih.hawkeye.views.OtherServicesActivity;
import com.sih.hawkeye.views.PassportVerificationActivity;
import com.sih.hawkeye.views.ReportCyberFraudActivity;

import static com.sih.hawkeye.PublicLoginActivity.mGoogleSignInClient;

public class PublicHomeActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    String PERSON_NAME = "PERSON_NAME";
    String PERSON_EMAIL = "PERSON_EMAIL";
    String PROFILE_PIC = "PROFILE_PIC";

    String personName;
    String personEmail;
    String profilePicUri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_public_home);


        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        personName = getIntent().getExtras().get("PERSON_NAME").toString();
        personEmail = getIntent().getExtras().get("PERSON_EMAIL").toString();
        profilePicUri = getIntent().getExtras().get("PROFILE_PIC").toString();

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        View header = navigationView.getHeaderView(0);
        TextView personNameTV = header.findViewById(R.id.person_name);
        TextView personEmailTV = header.findViewById(R.id.person_email);
        ImageView profilePic = header.findViewById(R.id.profile_pic);

        Glide.with(this)
                .load(profilePicUri)
                .apply(new RequestOptions()
                        .circleCrop())
                .into(profilePic);

        personNameTV.setText(personName);
        personEmailTV.setText(personEmail);
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_logout) {
            signOut();
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    private void signOut() {
        FirebaseAuth.getInstance().signOut();
        mGoogleSignInClient.signOut().addOnCompleteListener(this,
                new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        finish();
                        Intent intent = new Intent(getApplicationContext(), PublicLoginActivity.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(intent);
                    }
                });
        Toast.makeText(this,"Logged out ",Toast.LENGTH_LONG).show();
    }

    public void regCrimesBtn(View view){
        Intent intent = new Intent(getApplicationContext(), RegisterCrimeStatusActivity.class);
        intent.putExtra(PERSON_NAME,personName);
        intent.putExtra(PERSON_EMAIL,personEmail);
        intent.putExtra(PROFILE_PIC,profilePicUri);
        startActivity(intent);
    }

    public void cyberAwarenessBtn(View view){
        Intent intent = new Intent(getApplicationContext(), CyberAwarenessActivity.class);
        startActivity(intent);
    }

    public void passportVerificationBtn(View view){
        Intent intent = new Intent(getApplicationContext(), PassportVerificationActivity.class);
        startActivity(intent);
    }

    public void cyberFraudBtn(View view){
        Intent intent = new Intent(getApplicationContext(), ReportCyberFraudActivity.class);
        startActivity(intent);
    }

    public void otherServicesBtn(View view){
        Intent intent = new Intent(getApplicationContext(), OtherServicesActivity.class);
        startActivity(intent);
    }

    public void nocBtn(View view){
        Intent intent = new Intent(getApplicationContext(), NocActivity.class);
        startActivity(intent);
    }

    public void locatePolice(View view){
        Uri gmmIntentUri = Uri.parse("geo:37.7749,-122.4194");
        Intent mapIntent = new Intent(Intent.ACTION_VIEW, gmmIntentUri);
        mapIntent.setPackage("com.google.android.apps.maps");
        if (mapIntent.resolveActivity(getPackageManager()) != null) {
            startActivity(mapIntent);
        }
    }
}
