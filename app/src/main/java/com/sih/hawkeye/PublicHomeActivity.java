package com.sih.hawkeye;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;

import static com.sih.hawkeye.InmatesLoginActivity.mGoogleSignInClient;

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

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();


        personName = getIntent().getExtras().get("PERSON_NAME").toString();
        personEmail = getIntent().getExtras().get("PERSON_EMAIL").toString();
        profilePicUri = getIntent().getExtras().get("PROFILE_PIC").toString();
    }

    public void regCrimesBtn(View view){
        Intent intent = new Intent(getApplicationContext(),IssueStatusActivity.class);
        intent.putExtra(PERSON_NAME,personName);
        intent.putExtra(PERSON_EMAIL,personEmail);
        intent.putExtra(PROFILE_PIC,profilePicUri);
        startActivity(intent);
    }

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
                        Intent intent = new Intent(getApplicationContext(),InmatesLoginActivity.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(intent);
                    }
                });
        Toast.makeText(this,"Logged out ",Toast.LENGTH_LONG).show();
    }
}
