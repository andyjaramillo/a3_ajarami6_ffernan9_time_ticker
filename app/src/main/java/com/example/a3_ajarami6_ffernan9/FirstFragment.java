package com.example.a3_ajarami6_ffernan9;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.DecelerateInterpolator;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
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

import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class FirstFragment extends Fragment {

    private FragmentFirstBinding binding;
    private SharedPreferences sharedPrefs;
    private String currentTimeZone;
    private String originalTime;
    private String homeTimeZone;
    private String convertedTime;
    private boolean format24Hr;

    private FloatingActionButton ctaButton;
    private Spinner timeZoneSpinner;
    private View originalTimeView;
    private Button convertButton;

    private TextView currOffsetTxt;
    private TextView homeZoneText;
    private TextView homeOffsetTxt;

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

        timeZoneSpinner = view.findViewById(R.id.time_zone_spinner);
        originalTimeView = view.findViewById(R.id.original_time_card);
        convertButton = view.findViewById(R.id.convert_button);
        ctaButton = requireActivity().findViewById(R.id.settings_cta);

        currOffsetTxt = view.findViewById(R.id.curr_gmt_offset);
        homeZoneText = view.findViewById(R.id.home_zone_name);
        homeOffsetTxt = view.findViewById(R.id.home_gmt_offset);

        sharedPrefs = context.getSharedPreferences(
            getString(R.string.shared_prefs),
            Context.MODE_PRIVATE
        );

        loadStateFromPrefs();

        ArrayAdapter<CharSequence> spinnerAdapter = ArrayAdapter.createFromResource(
            context,
            R.array.time_zone_array,
            R.layout.custom_spinner
        );

        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        timeZoneSpinner.setDropDownVerticalOffset(250);
        timeZoneSpinner.setAdapter(spinnerAdapter);
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

        originalTimeView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Calendar cal = Calendar.getInstance();
                MaterialTimePicker timePicker = new MaterialTimePicker.Builder()
                    .setTheme(R.style.time_picker_theme)
                    .setInputMode(MaterialTimePicker.INPUT_MODE_CLOCK)
                    .setTimeFormat(format24Hr ? TimeFormat.CLOCK_24H : TimeFormat.CLOCK_12H)
                    .setHour(cal.get(Calendar.HOUR_OF_DAY))
                    .setMinute(cal.get(Calendar.MINUTE))
                    .build();

                timePicker.addOnPositiveButtonClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        int hour = timePicker.getHour();
                        int minute = timePicker.getMinute();
                        Log.d("hour", hour + ":" + minute);
                    }
                });

                FragmentManager manager = requireActivity().getSupportFragmentManager();
                timePicker.show(manager, "fragment_first");
            }
        });

        convertButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                updateConvertedTime();
            }
        });

        ctaButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                NavController navController = Navigation.findNavController(
                    requireActivity(),
                    R.id.nav_host_fragment_content_main
                );
                // Navigate to the desired destination
                navController.navigate(R.id.action_FirstFragment_to_SecondFragment);
            }
        });
    }

    @Override
    public void onViewStateRestored(@Nullable Bundle savedInstanceState) {
        super.onViewStateRestored(savedInstanceState);
        loadStateFromPrefs();
        showCTAButton();
    }

    private void loadStateFromPrefs() {
        String[] zonesArray = getResources().getStringArray(R.array.time_zone_array);
        List<String> timeZones = Arrays.asList(zonesArray);
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
        currOffsetTxt.setText(getGMTOffset(currentTimeZone));
        homeZoneText.setText(homeTimeZone);
        homeOffsetTxt.setText(getGMTOffset(homeTimeZone));

        // init curr time
        String timeFormat = format24Hr ? "H:mm" : "h:mm a";
        String currTime = new SimpleDateFormat(timeFormat, Locale.US).format(new Date());
        TextView originalTimeTxt = originalTimeView.findViewById(R.id.original_time_val);
        originalTimeTxt.setText(currTime);
    }

    private void updateCurrTimeZone(String timeZone) {
        SharedPreferences.Editor prefEditor = sharedPrefs.edit();
        prefEditor.putString(
            getString(R.string.curr_zone_pref),
            timeZone
        );
        currOffsetTxt.setText(getGMTOffset(timeZone));
        prefEditor.apply();
    }

    private void updateConvertedTime() {
        TextView originalTimeTxt = binding.content.findViewById(R.id.original_time_val);
        String originalTime = originalTimeTxt.getText().toString();

        String[] originalTimeParts = originalTime.split(" ");
        String[] hourMinArray = originalTimeParts[0].split(":");
        int hourInt = Integer.parseInt(hourMinArray[0]);
        int minuteInt = Integer.parseInt(hourMinArray[1]);
        boolean invalidHr = hourInt < 1 || (hourInt > 12);
        if(hourInt > 12 || hourInt < 1 || minuteInt > 59 || minuteInt < 0) {
            Toast.makeText(getActivity(), "Invalid Time", Toast.LENGTH_SHORT).show();
            return;
        }

        TextView homeOffsetTxt = binding.content.findViewById(R.id.home_gmt_offset);
        TextView currOffsetTxt= binding.content.findViewById(R.id.curr_gmt_offset);
        String finalTime = "";
        int homeOffset = getOffset(homeOffsetTxt.getText().toString());
        int currentOffset = getOffset(currOffsetTxt.getText().toString());
        int offset = homeOffset - currentOffset;
        String[] time = originalTime.split(":");
        int hour = Integer.parseInt(time[0]);
        int minute = Integer.parseInt(time[1].split(" ")[0]);
        String amPm = time[1].split(" ")[1];

        if (amPm.equals("PM")) {
            hour += 12;
        }

        hour += offset;
        if (hour < 0) {
            hour += 24;
        }
        if (hour >= 24) {
            hour -= 24;
        }

// Format the minute part to ensure it has two digits
        String formattedMinute = String.format(Locale.US, "%02d", minute);
        finalTime = String.format(Locale.US, "%d:%s", hour, formattedMinute);
        TextView timeText = binding.content.findViewById(R.id.converted_time_val);
        timeText.setText(finalTime);
    }


    public void updateGMTCurrText(String timeZone) {
        TextView offsetView = binding.content.findViewById(R.id.curr_gmt_offset);
        offsetView.setText(getGMTOffset(timeZone));
    }

    public void updateGMTHomeText(String timeZone) {
        TextView offsetView =binding.content.findViewById(R.id.home_gmt_offset);
        offsetView.setText(getGMTOffset(timeZone));
    }

    private boolean isValidHour(String text) {
        try {
            int hour = Integer.parseInt(text);
            return hour >= 1 && hour <= 12;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    private int getOffset(String gmtString) {
        String[] parts = gmtString.split("\\s+");
        // Extract the offset part (e.g., "-05:00")
        String offsetString = parts[1];
        // Remove the ":" from the offset string
        offsetString = offsetString.split(":")[0];
        // Parse the offset string to an integer
        return Integer.parseInt(offsetString);
    }

    private String getGMTOffset(String item) {
        return TimeZoneConverter.convertToGMT(item);
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