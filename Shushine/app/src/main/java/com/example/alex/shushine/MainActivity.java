package com.example.alex.shushine;

import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.alex.shushine.utilities.NetworkUtils;
import com.example.alex.shushine.utilities.OpenWeatherJsonUtils;
import com.example.alex.shushine.utilities.SunshinePreferences;

import java.net.URL;

public class MainActivity extends AppCompatActivity {

    private TextView mErrorMessagetextView;
    private TextView mWeatherTextView;
    private ProgressBar mProgressarBar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        this.mWeatherTextView = (TextView) findViewById(R.id.weather_item);
        this.mErrorMessagetextView = (TextView) findViewById(R.id.error_message);
        this.mProgressarBar = (ProgressBar) findViewById(R.id.pb_loading_indicator);
        loadWeatherData();

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        int itemThatWasPressed = item.getItemId();
        if(itemThatWasPressed == R.id.action_refresh) {
            mWeatherTextView.setText("");
            loadWeatherData();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void loadWeatherData() {
        String location = SunshinePreferences.getPreferredWeatherLocation(this);
        new FetchWeatherTask().execute(location);
    }

    private void showErroMessage(){
        mWeatherTextView.setVisibility(View.INVISIBLE);
        mErrorMessagetextView.setVisibility(View.VISIBLE);
    }

    private void showWeatherDataView(){
        mErrorMessagetextView.setVisibility(View.INVISIBLE);
        mWeatherTextView.setVisibility(View.VISIBLE);
    }

    public class FetchWeatherTask extends AsyncTask<String, Void, String[]> {
        @Override
        protected void onPreExecute(){
            super.onPreExecute();
            mProgressarBar.setVisibility(View.VISIBLE);
        }

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
            mProgressarBar.setVisibility(View.INVISIBLE);
            if(weatherData != null){
                showWeatherDataView();
                for(String weatherString : weatherData){
                    mWeatherTextView.append((weatherString) + "\n\n\n");
                }
            }else{
                showErroMessage();
            }

        }
    }
}
