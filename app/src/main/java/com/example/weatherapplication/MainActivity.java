package com.example.weatherapplication;

import android.os.Bundle;
import android.view.View;

import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    private RelativeLayout mainContainer;
    private ProgressBar loader;
    private TextView errorText, address, updatedAt, status, temp, tempMin, tempMax, sunrise, sunset, wind, pressure, humidity, about;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main); // Update if the XML file has a different name

        // Initialize views
        mainContainer = findViewById(R.id.mainContainer);
        loader = findViewById(R.id.loader);
        errorText = findViewById(R.id.errorText);
        address = findViewById(R.id.address);
        updatedAt = findViewById(R.id.updated_at);
        status = findViewById(R.id.status);
        temp = findViewById(R.id.temp);
        tempMin = findViewById(R.id.temp_min);
        tempMax = findViewById(R.id.temp_max);
        sunrise = findViewById(R.id.sunrise);
        sunset = findViewById(R.id.sunset);
        wind = findViewById(R.id.wind);
        pressure = findViewById(R.id.pressure);
        humidity = findViewById(R.id.humidity);
        about = findViewById(R.id.about);

        // Load data (example simulation)
        loadData();
    }

    private void loadData() {
        // Show loader and hide content
        loader.setVisibility(View.VISIBLE);
        mainContainer.setVisibility(View.GONE);
        errorText.setVisibility(View.GONE);

        // Simulate data loading
        // Replace with your data-fetching logic (e.g., API call)
        new android.os.Handler().postDelayed(() -> {
            // Simulated data loading success
            loader.setVisibility(View.GONE);
            mainContainer.setVisibility(View.VISIBLE);

            // Update the UI with simulated data
            address.setText("Colombo, Sri Lanka");
            updatedAt.setText("15 Jan 2025, 20:08 PM");
            status.setText("Clear Sky");
            temp.setText("30°C");
            tempMin.setText("Min Temp: 25°C");
            tempMax.setText("Max Temp: 35°C");
            sunrise.setText("06:42 AM");
            sunset.setText("06:41 PM");
            wind.setText("15 km/h");
            pressure.setText("1015 hPa");
            humidity.setText("75%");
            about.setText("Created by Evan");
        }, 2000); // Simulates a 2-second delay
    }
}
