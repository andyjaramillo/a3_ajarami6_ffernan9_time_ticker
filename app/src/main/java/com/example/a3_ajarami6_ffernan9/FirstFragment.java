package com.example.a3_ajarami6_ffernan9;

import android.app.TimePickerDialog;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.example.a3_ajarami6_ffernan9.databinding.FragmentFirstBinding;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class FirstFragment extends Fragment {

    private FragmentFirstBinding binding;
    private String originalTime = "";
    private TimePickerDialog originalTimePicker;
    private boolean format24Hr;

    @Override
    public View onCreateView(
            LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState
    ) {

        binding = FragmentFirstBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }


    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Context context = getActivity();
        assert context != null;

        format24Hr = false; // TODO: get format setting
        updateOriginalTimeText();

        Spinner timeZoneSpinner = view.findViewById(R.id.time_zone_spinner);

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
                updateGMTCurrText(adapter.getSelectedItem().toString());
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                // do nothing
            }
        });

        view.findViewById(R.id.original_time_picker).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                TimePickerDialog.OnTimeSetListener onTimeSetListener = new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker timePicker, int hour, int minute) {
                        // TODO: update curr time
                        Log.d("hour", hour + ":" + minute);
                    }
                };

                Calendar cal = Calendar.getInstance();
                int hour = cal.get(Calendar.HOUR_OF_DAY);
                int minute = cal.get(Calendar.MINUTE);
                originalTimePicker = new TimePickerDialog(
                    context,
                    onTimeSetListener,
                    hour,
                    minute,
                    format24Hr
                );

                originalTimePicker.show();
            }
        });



        view.findViewById(R.id.convert_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                updateFinalTimeText();
            }
        });
    }

    public void onCloseButtonClicked(String buttonText) {
        // Handle the button close event here
        originalTime = buttonText;
    }

    private void updateOriginalTimeText() {
        String timeFormat = format24Hr ? "H:mm" : "h:mm a";
        String currTime = new SimpleDateFormat(timeFormat, Locale.US).format(new Date());
        TextView orig_time_val = binding.content.findViewById(R.id.original_time_val);
        orig_time_val.setText(currTime);
    }

    private void updateFinalTimeText() {
        String originalTime = binding.originalTimeVal.getText().toString();

        String[] originalTimeParts = originalTime.split(" ");
        String[] hourMinArray = originalTimeParts[0].split(":");
        int hourInt = Integer.parseInt(hourMinArray[0]);
        int minuteInt = Integer.parseInt(hourMinArray[1]);
        boolean invalidHr = hourInt < 1 || (hourInt > 12);
        if(hourInt > 12 || hourInt < 1 || minuteInt > 59 || minuteInt < 0) {
            Toast.makeText(getActivity(), "Invalid Time", Toast.LENGTH_SHORT).show();
            return;
        }

        TextView homeOffsetView = binding.content.findViewById(R.id.home_gmt_offset);
        TextView currOffsetView = binding.content.findViewById(R.id.curr_gmt_offset);
        String finalTime = "";
        int homeOffset = getOffset(homeOffsetView.getText().toString());
        int currentOffset = getOffset(currOffsetView.getText().toString());
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
        offsetView.setText(getGMTTime(timeZone));
    }

    public void updateGMTHomeText(String timeZone) {
        TextView offsetView =binding.content.findViewById(R.id.home_gmt_offset);
        offsetView.setText(getGMTTime(timeZone));
    }

    private boolean isValidHour(String text) {
        try {
            int hour = Integer.parseInt(text);
            return hour >= 1 && hour <= 12;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    private int getOffset(String item) {
        String[] parts = item.split("\\s+");
        // Extract the offset part (e.g., "-05:00")
        String offsetString = parts[1];
        // Remove the ":" from the offset string
        offsetString = offsetString.split(":")[0];
        // Parse the offset string to an integer
        return Integer.parseInt(offsetString);
    }

    private String getGMTTime(String item) {
        return TimeZoneConverter.convertToGMT(item);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

}