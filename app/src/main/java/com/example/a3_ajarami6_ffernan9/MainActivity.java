package com.example.a3_ajarami6_ffernan9;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.util.TypedValue;
import android.view.Menu;
import android.view.animation.DecelerateInterpolator;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.example.a3_ajarami6_ffernan9.databinding.ActivityMainBinding;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class MainActivity extends AppCompatActivity {

    private AppBarConfiguration appBarConfiguration;
    private NavController navController;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ActivityMainBinding binding = ActivityMainBinding.inflate(getLayoutInflater());
        int mainColor = getResources().getColor(R.color.main_bg_color, null);
        getWindow().setStatusBarColor(mainColor);
        setContentView(binding.getRoot());

        setSupportActionBar(findViewById(R.id.main_toolbar));
    }

    @Override
    protected void onStart() {
        super.onStart();

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

    public static void attachAdapterToZoneSpinner(Context context, Spinner timeZoneSpinner) {
        ArrayAdapter<CharSequence> spinnerAdapter = ArrayAdapter.createFromResource(
            context,
            R.array.time_zone_array,
            R.layout.custom_spinner
        );
        spinnerAdapter.setDropDownViewResource(R.layout.custom_spinner_list_item);
        timeZoneSpinner.setDropDownVerticalOffset((int) TypedValue.applyDimension(
            TypedValue.COMPLEX_UNIT_DIP,
            5f,
            context.getResources().getDisplayMetrics()
        ));
        timeZoneSpinner.setAdapter(spinnerAdapter);
    }
}