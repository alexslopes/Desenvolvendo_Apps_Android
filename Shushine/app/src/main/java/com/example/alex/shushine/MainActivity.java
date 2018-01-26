package com.example.alex.shushine;

import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import com.example.alex.shushine.utilities.NetworkUtils;
import com.example.alex.shushine.utilities.OpenWeatherJsonUtils;
import com.example.alex.shushine.utilities.SunshinePreferences;

import java.net.URL;

import static com.example.alex.shushine.utilities.OpenWeatherJsonUtils.getSimpleWeatherStringsFromJson;

public class MainActivity extends AppCompatActivity {

    private TextView mWeatherTextView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        this.mWeatherTextView = (TextView) findViewById(R.id.weather_item);

        loadWeatherData();

    }

    private void loadWeatherData() {
        String location = SunshinePreferences.getPreferredWeatherLocation(this);
        new FetchWeatherTask().execute(location);
    }

    public class FetchWeatherTask extends AsyncTask<String, Void, String[]> {
        @Override
        protected String[] doInBackground(String... params) {
            /* If there's no zip code, there's nothing to look up. */
            if (params.length == 0) {
                return null;
            }

            String location = params[0];
            URL weatherRequestUrl = NetworkUtils.buildUrl(location);

            try {
                String jsonWeatherResponse = NetworkUtils
                        .getResponseFromHttpUrl(weatherRequestUrl);

                String[] simpleJsonWeatherData = OpenWeatherJsonUtils
                        .getSimpleWeatherStringsFromJson(MainActivity.this, jsonWeatherResponse);

                return simpleJsonWeatherData;

            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }
        }

        @Override
        protected void onPostExecute(String[] weatherData){
            if(weatherData != null){
                for(String weatherString : weatherData){
                    mWeatherTextView.append((weatherString) + "\n\n\n");
                }

            }

        }
    }
}
