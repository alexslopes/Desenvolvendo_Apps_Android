package com.example.alex.githubreposearch;

import android.content.Context;
import android.net.Network;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.alex.githubreposearch.utilities.NetworkUtils;

import java.io.IOException;
import java.net.URL;

public class MainActivity extends AppCompatActivity {

    EditText mSearchBoxEditText;

    TextView mUrlDisplayTextView;

    TextView mSearchResultsTextView;

    TextView mErrorMessagetextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mSearchBoxEditText = (EditText) findViewById(R.id.et_search_box);

        mUrlDisplayTextView = (TextView) findViewById(R.id.tv_url_display);

        mSearchResultsTextView = (TextView) findViewById(R.id.tv_github_search_results_json);

        mErrorMessagetextView = (TextView) findViewById(R.id.tv_error_message_display);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    public void makeGithubSearchQuery(){
        String githubQuery = mSearchBoxEditText.getText().toString();
        URL githubSearchUrl = NetworkUtils.buildUrl(githubQuery);
        mUrlDisplayTextView.setText(githubSearchUrl.toString());
        String githubSearchResults = null;
        new GithubQueryTask().execute(githubSearchUrl);

    }

    private void showJsondataView(){
        mErrorMessagetextView.setVisibility(View.INVISIBLE);
        mSearchResultsTextView.setVisibility(View.VISIBLE);
    }

    private void showErrorMessage(){
        mSearchResultsTextView.setVisibility(View.INVISIBLE);
        mErrorMessagetextView.setVisibility(View.VISIBLE);
    }

    public class GithubQueryTask extends AsyncTask<URL, Void, String> {
        @Override
        protected String doInBackground(URL... urls){
            URL searchUrl = urls[0];
            String githubSearchResults = null;
            try{
                githubSearchResults = NetworkUtils.getResponseFromHttpUrl(searchUrl);
            }catch (IOException e){
                e.printStackTrace();
            }
            return githubSearchResults;
        }

        @Override
        protected void onPostExecute(String s){
            if(s !=null && !s.equals("")){
                mSearchResultsTextView.setText(s);
            } else {
                showErrorMessage();
            }
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int itemThatWasPressed = item.getItemId();
        if(itemThatWasPressed == R.id.action_search){
//            Context context = MainActivity.this;
//            String message = "Search clicked!";
//            Toast.makeText(context, message, Toast.LENGTH_LONG).show();
            makeGithubSearchQuery();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
