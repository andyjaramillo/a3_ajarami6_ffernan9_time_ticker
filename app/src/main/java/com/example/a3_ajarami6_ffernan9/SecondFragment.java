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
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.example.a3_ajarami6_ffernan9.databinding.FragmentSecondBinding;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class SecondFragment extends Fragment {

    private FragmentSecondBinding binding;
    private String homeTimeZone;
    private String homeCity;

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
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                homeTimeZone = parent.getSelectedItem().toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // do nothing
            }
        });
        spinnerAdapter.setNotifyOnChange(true);

        binding.content.findViewById(R.id.save_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // TODO: save home time zone
                // TODO: save home city from input
                Log.d("SecondFragment", "Selected personal timezone " + homeTimeZone);
                SharedPreferences sharedPref = context.getSharedPreferences("my_preferences", Context.MODE_PRIVATE);
                String timeZonesString = sharedPref.getString("personal_timezone", "");
                String[] timeZonesArray = timeZonesString.split(",");
                List<String> timeZonesList = new ArrayList<>(Arrays.asList(timeZonesArray));
                String jsonArray = TextUtils.join(",", timeZonesList);
                SharedPreferences.Editor prefEditor = sharedPref.edit();
                prefEditor.putString("personal_timezone", jsonArray);
                prefEditor.apply();
            }
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

}