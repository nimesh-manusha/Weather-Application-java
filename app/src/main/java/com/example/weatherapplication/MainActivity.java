package com.example.weatherapplication;

import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import org.json.JSONObject;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class MainActivity extends AppCompatActivity {

    private SearchView searchView;
    private RelativeLayout mainContainer;
    private ProgressBar loader;
    private TextView errorText, address, updatedAt, status, temp, tempMin, tempMax, sunrise, sunset, wind, pressure, humidity, about;

    private static final String API_KEY = "7c5854ea1f5e3beb98162f19e0da75a6"; // Replace with your API key

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Initialize UI Components
        searchView = findViewById(R.id.searchView);
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

        // Set up SearchView listener
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                fetchWeatherData(query);
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });

        // Load default weather data for Colombo
        fetchWeatherData("Colombo");
    }

    private void fetchWeatherData(String location) {
        String apiUrl = "https://api.openweathermap.org/data/2.5/weather?q=" + location + "&units=metric&appid=" + API_KEY;
        new FetchWeatherTask().execute(apiUrl);
    }

    private class FetchWeatherTask extends AsyncTask<String, Void, String> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            loader.setVisibility(View.VISIBLE);
            mainContainer.setVisibility(View.GONE);
            errorText.setVisibility(View.GONE);
        }

        @Override
        protected String doInBackground(String... urls) {
            StringBuilder result = new StringBuilder();
            try {
                URL url = new URL(urls[0]);
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setRequestMethod("GET");
                InputStream inputStream = conn.getInputStream();
                BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
                String line;
                while ((line = reader.readLine()) != null) {
                    result.append(line);
                }
            } catch (Exception e) {
                Log.e("WeatherError", "Error fetching weather data", e);
                return null;
            }
            return result.toString();
        }

        @Override
        protected void onPostExecute(String result) {
            loader.setVisibility(View.GONE);

            if (result == null) {
                errorText.setVisibility(View.VISIBLE);
                return;
            }

            try {
                JSONObject jsonObject = new JSONObject(result);

                // Extract data from JSON
                String cityName = jsonObject.getString("name");
                JSONObject main = jsonObject.getJSONObject("main");
                double temperature = main.getDouble("temp");
                double tempMinValue = main.getDouble("temp_min");
                double tempMaxValue = main.getDouble("temp_max");
                int pressureValue = main.getInt("pressure");
                int humidityValue = main.getInt("humidity");

                JSONObject windObj = jsonObject.getJSONObject("wind");
                double windSpeed = windObj.getDouble("speed");

                JSONObject weather = jsonObject.getJSONArray("weather").getJSONObject(0);
                String weatherDescription = weather.getString("description");

                JSONObject sys = jsonObject.getJSONObject("sys");
                long sunriseTime = sys.getLong("sunrise") * 1000;
                long sunsetTime = sys.getLong("sunset") * 1000;

                // Update UI
                address.setText(cityName);
                updatedAt.setText("Updated just now");
                status.setText(weatherDescription);
                temp.setText(String.format("%.1f°C", temperature));
                tempMin.setText("Min: " + String.format("%.1f°C", tempMinValue));
                tempMax.setText("Max: " + String.format("%.1f°C", tempMaxValue));
                wind.setText(windSpeed + " km/h");
                pressure.setText(pressureValue + " hPa");
                humidity.setText(humidityValue + "%");
                sunrise.setText(new java.text.SimpleDateFormat("hh:mm a").format(new java.util.Date(sunriseTime)));
                sunset.setText(new java.text.SimpleDateFormat("hh:mm a").format(new java.util.Date(sunsetTime)));
                about.setText("More details about " + cityName);

                mainContainer.setVisibility(View.VISIBLE);

            } catch (Exception e) {
                errorText.setVisibility(View.VISIBLE);
                Log.e("WeatherError", "Error parsing JSON", e);
            }
        }
    }
}
