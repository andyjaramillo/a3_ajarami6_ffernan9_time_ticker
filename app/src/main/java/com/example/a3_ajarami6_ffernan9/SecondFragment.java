package com.example.a3_ajarami6_ffernan9;

import static android.content.Context.INPUT_METHOD_SERVICE;
import static com.example.a3_ajarami6_ffernan9.MainActivity.attachAdapterToZoneSpinner;
import static com.example.a3_ajarami6_ffernan9.TimeZoneConverter.getGMTOffsetString;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateInterpolator;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import com.example.a3_ajarami6_ffernan9.databinding.FragmentSecondBinding;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.materialswitch.MaterialSwitch;
import com.google.android.material.textfield.TextInputEditText;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;

public class SecondFragment extends Fragment {

    private FragmentSecondBinding binding;
    private SharedPreferences sharedPrefs;
    private String homeTimeZone;
    private String homeCity;
    private boolean format24Hr;

    private Spinner timeZoneSpinner;
    private TextInputEditText homeCityInput;
    private MaterialSwitch formatToggle;

    private TextView homeZoneOffset;

    @Override
    public View onCreateView(
        @NonNull LayoutInflater inflater, ViewGroup container,
        Bundle savedInstanceState
    ) {
        binding = FragmentSecondBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Context context = requireActivity();

        sharedPrefs = context.getSharedPreferences(
            getString(R.string.shared_prefs),
            Context.MODE_PRIVATE
        );

        // initialize relevant views
        timeZoneSpinner = view.findViewById(R.id.home_time_zone_spinner);
        homeCityInput = view.findViewById(R.id.home_city_edit_field);
        homeZoneOffset = view.findViewById(R.id.home_gmt_offset);
        formatToggle = view.findViewById(R.id.toggle_24h);

        loadStateFromPrefs(savedInstanceState);

        attachAdapterToZoneSpinner(context, timeZoneSpinner);
        timeZoneSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                homeTimeZone = parent.getSelectedItem().toString();
                homeZoneOffset.setText(getGMTOffsetString(homeTimeZone));
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // do nothing
            }
        });

        homeCityInput.setOnFocusChangeListener((v, hasFocus) -> {
            if (!hasFocus) {
                InputMethodManager imm = (InputMethodManager) context.getSystemService(INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
            }
        });

        formatToggle.setOnCheckedChangeListener((buttonView, isChecked) -> format24Hr = isChecked);

        Button saveButton = view.findViewById(R.id.save_button);
        saveButton.setOnClickListener((view1) -> {
            homeCity = Objects.requireNonNull(homeCityInput.getText()).toString();
            saveSettings();
        });

        hideCTAButton();
    }

    @Override
    public void onViewStateRestored(@Nullable Bundle savedInstanceState) {
        super.onViewStateRestored(savedInstanceState);
        hideCTAButton();
        loadStateFromPrefs(savedInstanceState);
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString(getString(R.string.home_zone_pref), homeTimeZone);
        outState.putString(getString(R.string.home_city_pref), homeCity);
        outState.putBoolean(getString(R.string.format_24h_pref), format24Hr);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    private void loadStateFromPrefs(@Nullable Bundle savedInstanceState) {
        String[] zonesArray = getResources().getStringArray(R.array.time_zone_array);
        List<String> timeZones = Arrays.asList(zonesArray);

        if (savedInstanceState == null) {
            homeTimeZone = sharedPrefs.getString(
                getString(R.string.home_zone_pref),
                timeZones.get(0)
            );
            homeCity = sharedPrefs.getString(
                getString(R.string.home_city_pref),
                "Baltimore"
            );
            format24Hr = sharedPrefs.getBoolean(
                getString(R.string.format_24h_pref),
                false
            );
        } else {
            homeTimeZone = savedInstanceState.getString(
                getString(R.string.home_zone_pref),
                timeZones.get(0)
            );
            homeCity = savedInstanceState.getString(
                getString(R.string.home_city_pref),
                "Baltimore"
            );
            format24Hr = savedInstanceState.getBoolean(
                getString(R.string.format_24h_pref),
                false
            );
        }
        timeZoneSpinner.setSelection(timeZones.indexOf(homeTimeZone));
        homeZoneOffset.setText(getGMTOffsetString(homeTimeZone));
        homeCityInput.setText(homeCity);
        formatToggle.setChecked(format24Hr);
    }

    private void saveSettings() {
        SharedPreferences sharedPref = requireContext().getSharedPreferences(
            getString(R.string.shared_prefs),
            Context.MODE_PRIVATE
        );
        SharedPreferences.Editor prefEditor = sharedPref.edit();
        prefEditor.putBoolean(getString(R.string.format_24h_pref), format24Hr);
        prefEditor.putString(getString(R.string.home_zone_pref), homeTimeZone);
        prefEditor.putString(getString(R.string.home_city_pref), homeCity);
        prefEditor.apply();

        NavController navController = Navigation.findNavController(
            requireActivity(),
            R.id.nav_host_fragment_content_main
        );
        navController.navigate(R.id.action_SecondFragment_to_FirstFragment);
    }

    private void hideCTAButton() {
        FloatingActionButton ctaButton = requireActivity().findViewById(R.id.settings_cta);
        ctaButton.animate()
            .translationY(ctaButton.getCustomSize() * 1.8f)
            .setInterpolator(new AccelerateInterpolator(1.5f))
            .start();
    }
}