package com.sih.hawkeye.views;

import android.app.ProgressDialog;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Build;
import android.os.Bundle;
import android.view.MenuItem;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.sih.hawkeye.R;

import java.util.Objects;

public class CyberAwarenessActivity extends AppCompatActivity {

    ProgressDialog progressDialog;

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

/*        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();*/
        setContentView(R.layout.activity_cyber_awareness);

        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);

        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Loading...");
        progressDialog.setCancelable(false);
        progressDialog.show();

        final WebView browser = (WebView) findViewById(R.id.connect_volunteers_online);

        browser.getSettings().setDomStorageEnabled(true);
        browser.getSettings().setJavaScriptEnabled(true);
        browser.clearCache(true);
        browser.reload();
        browser.getSettings().setGeolocationEnabled(true);
        browser.getSettings().setAppCacheEnabled(true);
        browser.getSettings().setDatabaseEnabled(true);
        browser.getSettings().setDomStorageEnabled(true);
        browser.getSettings().setGeolocationDatabasePath(getFilesDir().getPath());

        browser.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                view.loadUrl(url);
                return true;
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                if (progressDialog.isShowing()) {
                    progressDialog.dismiss();
                }
            }

            @Override
            public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
                //Toast.makeText(get, "Error:" + description, Toast.LENGTH_SHORT).show();

            }
        });

        browser.loadUrl("https://sihawareness.firebaseapp.com/");
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
        }
        return true;
    }
}