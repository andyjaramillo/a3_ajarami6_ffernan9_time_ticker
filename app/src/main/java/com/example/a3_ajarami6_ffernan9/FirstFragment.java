package com.example.a3_ajarami6_ffernan9;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;

import com.example.a3_ajarami6_ffernan9.databinding.FragmentFirstBinding;
import com.example.a3_ajarami6_ffernan9.databinding.ModalViewBinding;

public class FirstFragment extends Fragment {

    private FragmentFirstBinding binding;
    private String originalTime = "";


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
        Spinner spinner = binding.spinner;
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(
                getActivity(),
                R.array.time_zone_array,
                android.R.layout.simple_spinner_item
        );
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
// Apply the adapter to the spinner.
        spinner.setAdapter(adapter);

        Spinner spinner2 = binding.spinner2;
        ArrayAdapter<CharSequence> adapter2 = ArrayAdapter.createFromResource(
                getActivity(),
                R.array.time_zone_array,
                android.R.layout.simple_spinner_item
        );
        adapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
// Apply the adapter to the spinner.
        spinner2.setAdapter(adapter2);


        binding.spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long id) {
                updateGMTText(adapterView.getItemAtPosition(position).toString());
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                // do nothing
            }
        });

        binding.spinner2.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long id) {
                updateGMTHomeText(adapterView.getItemAtPosition(position).toString());
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                // do nothing
            }
        });

        binding.originalTimeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Dialog dialog = onCreateDialog(savedInstanceState);
                dialog.show();

            }
        });



        binding.convertButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                updateFinalTimeText();
            }
        });




//        binding.buttonFirst.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                NavHostFragment.findNavController(FirstFragment.this)
//                        .navigate(R.id.action_FirstFragment_to_SecondFragment);
//            }
//        });
    }

    public void onCloseButtonClicked(String buttonText) {
        // Handle the button close event here
        originalTime = buttonText;
    }

    private void updateOriginalTimeText(String time) {
        originalTime = time;
    }

    private void updateFinalTimeText() {

        String originalTime = binding.originalTimeButton.getText().toString();
        String gmtCurrent = binding.gmtCurrentText.getText().toString();
        String gmtHome = binding.gmtHomeText.getText().toString();

        String hourString = originalTime.split(" ")[0].split(":")[0];
        String minuteString = originalTime.split(" ")[0].split(":")[1];
        int hourInt = Integer.parseInt(hourString);
        int minuteInt = Integer.parseInt(minuteString);
        if(hourInt > 12 || hourInt < 1 || minuteInt > 59 || minuteInt < 0) {
            Toast.makeText(getActivity(), "Invalid Time", Toast.LENGTH_SHORT).show();
            return;
        }


        String finalTime = "";
        int homeOffset = getOffset(binding.spinner2.getSelectedItem().toString());
        int currentOffset = getOffset(binding.spinner.getSelectedItem().toString());
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
        String formattedMinute = String.format("%02d", minute);

        finalTime = String.format("%d:%s", hour, formattedMinute);

        binding.finalTimeText.setText(finalTime);
    }

    public Dialog onCreateDialog(Bundle savedInstanceState) {
        // Use the Builder class for convenient dialog construction.
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setMessage(R.string.select_your_timezone);
        LayoutInflater inflater = requireActivity().getLayoutInflater();
      //  binding2.twelveText.addTextChangedListener(textWatcher);
      //  binding2.zeroText.addTextChangedListener(textWatcher);
        // Inflate and set the layout for the dialog.
        // Pass null as the parent view because it's going in the dialog layout.

        builder.setView(inflater.inflate(R.layout.modal_view, null))
                .setPositiveButton(R.string.start, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // START THE GAME!
                        TextView twelveTextView = ((AlertDialog) dialog).findViewById(R.id.twelve_text);
                        TextView zeroTextView = ((AlertDialog) dialog).findViewById(R.id.zero_text);
                        Switch amPmSwitch = ((AlertDialog) dialog).findViewById(R.id.amSwitch);
                        // Get the text from the TextViews
                        String hour = twelveTextView.getText().toString();
                        String minute = zeroTextView.getText().toString();
                        binding.originalTimeButton.setText(twelveTextView.getText() + ":" + zeroTextView.getText() + " " + (amPmSwitch.isChecked() ? "PM" : "AM"));

                    }
                })
                .setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // User cancels the dialog.
                    }
                });
        // Get the layout for the dialog


        // Create the AlertDialog object and return it.
        return builder.create();
    }


    public void updateGMTText(String item) {
        binding.gmtCurrentText.setText(getGMTTime(item));
    }

    public void updateGMTHomeText(String item) {
        binding.gmtHomeText.setText(getGMTTime(item));
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
        int offset = 0;
        switch (item) {
            case "America/New_York":
                offset = -5;
                break;
            case "America/Los_Angeles":
                offset = -8;
                break;
            case "Europe/Berlin":
                offset = 1;
                break;
            case "Europe/Istanbul":
                offset = 2;
                break;
            case "Asia/Singapore":
                offset = 8;
                break;
            case "Asia/Tokyo":
                offset = 9;
                break;
            case "Australia/Canberra":
                offset = 10;
                break;
        }
        return offset;
    }

    private String getGMTTime(String item) {
        String gmtTime = "";
        switch (item) {
            case "America/New_York":
                gmtTime = "GMT -05:00";
                break;
            case "America/Los_Angeles":
                gmtTime = "GMT -08:00";
                break;
            case "Europe/Berlin":
                gmtTime = "GMT +01:00";
                break;
            case "Europe/Istanbul":
                gmtTime = "GMT +02:00";
                break;
            case "Asia/Singapore":
                gmtTime = "GMT +08:00";
                break;
            case "Asia/Tokyo":
                gmtTime = "GMT +09:00";
                break;
            case "Australia/Canberra":
                gmtTime = "GMT +10:00";
                break;
        }
        return gmtTime;
    }

    TextWatcher textWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            // Not needed
        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            // Not needed
            if(s.length() == 2) {
                if (!isValidHour(s.toString())) {
                    Toast.makeText(getActivity(), "Invalid hour", Toast.LENGTH_SHORT).show();
                }
            }

        }

        @Override
        public void afterTextChanged(Editable s) {
            // Check if the input has exactly two characters
        }
    };

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

}