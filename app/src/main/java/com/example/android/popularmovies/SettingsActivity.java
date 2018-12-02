package com.example.android.popularmovies;

import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class SettingsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ActionBar actionBar = this.getSupportActionBar();

        // Set the action bar to act as back button
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }


        // Display the settings fragment
        getFragmentManager().beginTransaction()
                .replace(android.R.id.content, new SettingsFragment()).commit();

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }
}
