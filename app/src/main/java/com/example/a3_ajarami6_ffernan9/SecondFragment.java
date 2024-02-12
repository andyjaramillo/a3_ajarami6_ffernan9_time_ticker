package com.example.a3_ajarami6_ffernan9;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.fragment.NavHostFragment;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.example.a3_ajarami6_ffernan9.databinding.FragmentSecondBinding;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class SecondFragment extends Fragment {

    private FragmentSecondBinding binding;

    private String[] listOfPersonalTimezones;

    @Override
    public View onCreateView(
            LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState
    ) {

        binding = FragmentSecondBinding.inflate(inflater, container, false);
        return binding.getRoot();

    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Context context = getActivity();
        assert context != null;
        SharedPreferences sharedPref = context.getSharedPreferences("my_preferences", Context.MODE_PRIVATE);
        String timeZonesString = sharedPref.getString("personal_timezone", "");
        String[] timeZonesArray = timeZonesString.split(",");
        List<String> timeZones = new ArrayList<>(Arrays.asList(timeZonesArray));
        // Retrieve the string array from the strings.xml resource file
        String[] stringArray = getResources().getStringArray(R.array.full_time_zone_array);
        List<String> fullTimeZones = new ArrayList<>(Arrays.asList(stringArray));
        fullTimeZones = fullTimeZones.stream()
                .filter(timeZone -> !timeZones.contains(timeZone))
                .collect(Collectors.toList());

        Spinner spinner = binding.totalList;
        ArrayAdapter<String> adapter = new ArrayAdapter<>(
                getActivity(),
                android.R.layout.simple_spinner_item,
                fullTimeZones
        );
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
// Apply the adapter to the spinner.
        spinner.setAdapter(adapter);
        adapter.setNotifyOnChange(true);

        Spinner personalSpinner = binding.personalList;
        ArrayAdapter<String> personalAdapter = new ArrayAdapter<>(
                getActivity(),
                android.R.layout.simple_spinner_item,
                timeZones
        );
        personalAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
// Apply the adapter to the spinner.
        personalSpinner.setAdapter(personalAdapter);
        personalAdapter.setNotifyOnChange(true);
        binding.totalList.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long id) {
                binding.displayNewTimezoneText.setText(adapterView.getItemAtPosition(position).toString());
            }
            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                // do nothing
            }
        });

        binding.submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String selectedPersonalTimezone = binding.displayNewTimezoneText.getText().toString();
                Log.d("SecondFragment", "Selected personal timezone " + selectedPersonalTimezone);
                SharedPreferences sharedPref = context.getSharedPreferences("my_preferences", Context.MODE_PRIVATE);
                String timeZonesString = sharedPref.getString("personal_timezone", "");
                String[] timeZonesArray = timeZonesString.split(",");
                List<String> timeZonesList = new ArrayList<>(Arrays.asList(timeZonesArray));
                timeZonesList.add(selectedPersonalTimezone);
                String jsonArray = TextUtils.join(",", timeZonesList);
                SharedPreferences.Editor editor = sharedPref.edit();
                editor.putString("personal_timezone", jsonArray);
                editor.apply();
                personalAdapter.clear();
                personalAdapter.addAll(timeZonesList);
                String[] stringArray = getResources().getStringArray(R.array.full_time_zone_array);
                List<String> fullTimeZones = new ArrayList<>(Arrays.asList(stringArray));
                fullTimeZones = fullTimeZones.stream()
                        .filter(timeZone -> !timeZonesList.contains(timeZone))
                        .collect(Collectors.toList());
                adapter.clear();
                adapter.addAll(fullTimeZones);

//                String[] personalTimeZoneArray = getResources().getStringArray(R.array.personal_time_zone_array);
//                List<String> modifiedTimeZonesList = new ArrayList<>(Arrays.asList(personalTimeZoneArray));
//                modifiedTimeZonesList.add(selectedPersonalTimezone);

            }
        });

        binding.removeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String selectedPersonalTimezone = binding.personalList.getSelectedItem().toString();
                Log.d("SecondFragment", "Selected personal timezone " + selectedPersonalTimezone);

                SharedPreferences sharedPref = context.getSharedPreferences("my_preferences", Context.MODE_PRIVATE);
                String timeZonesString = sharedPref.getString("personal_timezone", "");
                String[] timeZonesArray = timeZonesString.split(",");
                List<String> timeZonesList = new ArrayList<>(Arrays.asList(timeZonesArray));

// Remove the selected item from the list
                timeZonesList.remove(selectedPersonalTimezone);

// Convert the updated list back to a comma-separated string
                String jsonArray = TextUtils.join(",", timeZonesList);

// Update SharedPreferences with the modified string
                SharedPreferences.Editor editor = sharedPref.edit();
                editor.putString("personal_timezone", jsonArray);
                editor.apply();

// Update the adapter to reflect the changes
                personalAdapter.clear();
                personalAdapter.addAll(timeZonesList);
                String[] stringArray = getResources().getStringArray(R.array.full_time_zone_array);
                List<String> fullTimeZones = new ArrayList<>(Arrays.asList(stringArray));
                fullTimeZones = fullTimeZones.stream()
                        .filter(timeZone -> !timeZonesList.contains(timeZone))
                        .collect(Collectors.toList());
                adapter.clear();
                adapter.addAll(fullTimeZones);

            }
        });




//        binding.buttonSecond.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                NavController navController = Navigation.findNavController(requireActivity(), R.id.action_SecondFragment_to_FirstFragment);
//                navController.navigate(R.id.action_SecondFragment_to_FirstFragment);
//
//            }
//        });
        /*
        * binding.
        *
        * */
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

}