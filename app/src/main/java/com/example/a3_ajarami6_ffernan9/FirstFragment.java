package com.example.a3_ajarami6_ffernan9;

import static com.example.a3_ajarami6_ffernan9.MainActivity.attachAdapterToZoneSpinner;
import static com.example.a3_ajarami6_ffernan9.TimeZoneConverter.getGMTOffset;
import static com.example.a3_ajarami6_ffernan9.TimeZoneConverter.getGMTOffsetString;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.DecelerateInterpolator;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import com.example.a3_ajarami6_ffernan9.databinding.FragmentFirstBinding;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.timepicker.MaterialTimePicker;
import com.google.android.material.timepicker.TimeFormat;

import java.util.Arrays;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

public class FirstFragment extends Fragment {

    private FragmentFirstBinding binding;
    private SharedPreferences sharedPrefs;
    private String currentTimeZone;
    private int[] originalTime;
    private String homeTimeZone;
    private boolean format24Hr;

    private FloatingActionButton ctaButton;
    private Spinner timeZoneSpinner;
    private ImageView dndAlert;

    private TextView currOffsetTxt;
    private TextView originalTimeTxt;
    private TextView homeZoneText;
    private TextView homeOffsetTxt;
    private TextView convertedTimeTxt;

    @Override
    public View onCreateView(
        @NonNull LayoutInflater inflater, ViewGroup container,
        Bundle savedInstanceState
    ) {
        binding = FragmentFirstBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Context context = requireActivity();

        timeZoneSpinner = view.findViewById(R.id.curr_time_zone_spinner);
        ctaButton = requireActivity().findViewById(R.id.settings_cta);
        dndAlert = view.findViewById(R.id.dnd_alert);

        originalTimeTxt = view.findViewById(R.id.original_time_val);
        currOffsetTxt = view.findViewById(R.id.curr_gmt_offset);
        homeZoneText = view.findViewById(R.id.home_zone_name);
        homeOffsetTxt = view.findViewById(R.id.home_gmt_offset);
        convertedTimeTxt = view.findViewById(R.id.converted_time_val);

        sharedPrefs = context.getSharedPreferences(
            getString(R.string.shared_prefs),
            Context.MODE_PRIVATE
        );

        loadStateFromPrefs(savedInstanceState);

        attachAdapterToZoneSpinner(context, timeZoneSpinner);
        timeZoneSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapter, View view, int position, long id) {
                updateCurrTimeZone(adapter.getSelectedItem().toString());
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                // do nothing
            }
        });

        View originalTimeView = view.findViewById(R.id.original_time_card);
        originalTimeView.setOnClickListener((v) -> openTimePicker());

        Button convertButton = view.findViewById(R.id.convert_button);
        convertButton.setOnClickListener((v) -> updateConvertedTime());

        ctaButton.setOnClickListener((v) -> {
            NavController navController = Navigation.findNavController(
                requireActivity(),
                R.id.nav_host_fragment_content_main
            );
            navController.navigate(R.id.action_FirstFragment_to_SecondFragment);
        });
    }

    @Override
    public void onViewStateRestored(@Nullable Bundle savedInstanceState) {
        super.onViewStateRestored(savedInstanceState);
        loadStateFromPrefs(savedInstanceState);
        showCTAButton();
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putIntArray("original_time", originalTime);
    }

    private void loadStateFromPrefs(Bundle savedInstanceState) {
        String[] zonesArray = getResources().getStringArray(R.array.time_zone_array);
        List<String> timeZones = Arrays.asList(zonesArray);

        if (savedInstanceState != null) {
            originalTime = savedInstanceState.getIntArray("original_time");
        } else {
            Calendar cal = Calendar.getInstance();
            int hour = cal.get(Calendar.HOUR_OF_DAY);
            int minute = cal.get(Calendar.MINUTE);
            originalTime = new int[]{hour, minute};
        }
        format24Hr = sharedPrefs.getBoolean(
            getString(R.string.format_24h_pref),
            false
        );
        currentTimeZone = sharedPrefs.getString(
            getString(R.string.curr_zone_pref),
            timeZones.get(0)
        );
        homeTimeZone = sharedPrefs.getString(
            getString(R.string.home_zone_pref),
            timeZones.get(1)
        );

        timeZoneSpinner.setSelection(timeZones.indexOf(currentTimeZone));
        currOffsetTxt.setText(getGMTOffsetString(currentTimeZone));
        homeZoneText.setText(homeTimeZone);
        homeOffsetTxt.setText(getGMTOffsetString(homeTimeZone));
        originalTimeTxt.setText(formatTime(originalTime));
        updateConvertedTime();
    }

    private void updateCurrTimeZone(String timeZone) {
        currentTimeZone = timeZone;
        SharedPreferences.Editor prefEditor = sharedPrefs.edit();
        prefEditor.putString(getString(R.string.curr_zone_pref), timeZone);
        currOffsetTxt.setText(getGMTOffsetString(timeZone));
        prefEditor.apply();
    }

    private void openTimePicker() {
        MaterialTimePicker timePicker = new MaterialTimePicker.Builder()
            .setTheme(R.style.time_picker_theme)
            .setInputMode(MaterialTimePicker.INPUT_MODE_CLOCK)
            .setTimeFormat(format24Hr ? TimeFormat.CLOCK_24H : TimeFormat.CLOCK_12H)
            .setHour(originalTime[0])
            .setMinute(originalTime[1])
            .build();

        timePicker.addOnPositiveButtonClickListener((v) -> {
            int hour = timePicker.getHour();
            int minute = timePicker.getMinute();
            originalTime = new int[]{hour, minute};
            originalTimeTxt.setText(formatTime(originalTime));
        });

        FragmentManager manager = requireActivity().getSupportFragmentManager();
        timePicker.show(manager, "fragment_first");
    }

    private String formatTime(int[] time) {
        int hour = time[0];
        int minute = time[1];
        boolean validHour = hour >= 0 && hour < 24;
        boolean validMinute = minute >= 0 && minute < 60;
        if (!validHour || !validMinute) {
            Toast.makeText(
                getActivity(),
                "Invalid Time",
                Toast.LENGTH_SHORT
            ).show();
        }

        String markAMPM = "";
        if (!format24Hr) {
            markAMPM = hour < 12 ? " AM" : " PM";
            if (hour == 0) hour = 12;
            else if (hour > 12) hour %= 12;
        }
        return String.format(Locale.US, "%d:%02d%s", hour, minute, markAMPM);
    }

    private int getZoneOffsetDifference() {
        int currGMTOffset = getGMTOffset(currentTimeZone);
        int homeGMTOffset = getGMTOffset(homeTimeZone);
        return homeGMTOffset - currGMTOffset;
    }

    private void updateConvertedTime() {
        int offsetDiff = getZoneOffsetDifference();

        if (offsetDiff == 0) {
            // same time zones
            Toast.makeText(
                getActivity(),
                "Current and home time-zones cannot be the same",
                Toast.LENGTH_SHORT
            ).show();
            return;
        }

        int hour = originalTime[0];
        int minute = originalTime[1];
        int dayDiff = 0;

        hour += offsetDiff;
        if (hour < 0) {
            dayDiff = -1;
            hour += 24;
        } else if (hour >= 24) {
            dayDiff = 1;
            hour %= 24;
        }

        int visibility = View.INVISIBLE;
        boolean dndEarly = (60 * hour) + minute <= (60 * 7);
        boolean dndLate = (60 * hour) + minute >= (60 * 23);
        if (dndEarly || dndLate) {
            visibility = View.VISIBLE;
        }

        String convertedTime = formatTime(new int[]{hour, minute});
        String dayOffsetStr = "";
        if (dayDiff != 0) {
            int stringId = dayDiff > 0 ? R.string.next_day_string : R.string.prev_day_string;
            dayOffsetStr = getString(stringId);
        }
        TextView dayOffset = binding.content.findViewById(R.id.day_offset_indicator);
        dayOffset.setText(dayOffsetStr);
        dndAlert.setVisibility(visibility);
        convertedTimeTxt.setText(convertedTime);
    }

    private void showCTAButton() {
        ctaButton.animate()
            .translationY(0)
            .setInterpolator(new DecelerateInterpolator(1f))
            .start();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

}