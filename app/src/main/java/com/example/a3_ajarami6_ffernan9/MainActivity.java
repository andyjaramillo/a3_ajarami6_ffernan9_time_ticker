package com.example.a3_ajarami6_ffernan9;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import androidx.appcompat.app.AppCompatActivity;

import android.text.TextUtils;
import android.util.Log;
import android.view.View;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.example.a3_ajarami6_ffernan9.databinding.ActivityMainBinding;

import android.view.Menu;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.DecelerateInterpolator;

public class MainActivity extends AppCompatActivity {

    private AppBarConfiguration appBarConfiguration;
    private NavController navController;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d("app_events", "onCreate");

        SharedPreferences sharedPref = getSharedPreferences("my_preferences", Context.MODE_PRIVATE);
        // Initialize with default values
        String[] defaultArray = {
            "America/New_York",
            "America/Los_Angeles",
            "Europe/Berlin",
            "Europe/Istanbul",
            "Asia/Singapore",
            "Asia/Tokyo",
            "Australia/Canberra"
        };
        String jsonArray = TextUtils.join(",", defaultArray);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putString("personal_timezone", jsonArray);
        editor.apply();

        ActivityMainBinding binding = ActivityMainBinding.inflate(getLayoutInflater());
        int mainColor = getResources().getColor(R.color.main_bg_color, null);
        getWindow().setStatusBarColor(mainColor);
        setContentView(binding.getRoot());

        setSupportActionBar(findViewById(R.id.main_toolbar));
    }

    @Override
    protected void onStart() {
        super.onStart();
        Log.d("app_events", "onStart");

        navController = Navigation.findNavController(
            this,
            R.id.nav_host_fragment_content_main
        );
        // Ensure that appBarConfiguration is properly initialized
        appBarConfiguration = new AppBarConfiguration.Builder(
            navController.getGraph()
        ).build();
        // Setup the ActionBar with the Navigation Controller and AppBarConfiguration
        NavigationUI.setupActionBarWithNavController(
            this,
            navController,
            appBarConfiguration
        );
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        Log.d("app_events", "onRestart");
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.d("app_events", "onResume");
    }

    @Override
    protected void onPause() {
        Log.d("app_events", "onPause");
        super.onPause();
    }

    @Override
    protected void onStop() {
        Log.d("app_events", "onStop");
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        Log.d("app_events", "onDestroy");
        super.onDestroy();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // No options menu
        return false;
    }

    @Override
    public boolean onSupportNavigateUp() {
        navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_main);
        FloatingActionButton ctaButton = findViewById(R.id.settings_cta);
        ctaButton.animate().translationY(0).setInterpolator(new DecelerateInterpolator(1f)).start();
        return NavigationUI.navigateUp(navController, appBarConfiguration)
            || super.onSupportNavigateUp();
    }
}