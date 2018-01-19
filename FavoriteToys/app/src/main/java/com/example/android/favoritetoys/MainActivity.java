package com.example.android.favoritetoys;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    TextView mToysListTextView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        this.mToysListTextView = (TextView) findViewById(R.id.tv_toy_names);

        String[] toyNames = ToyBox.getToyNames();

        for(String toyName : toyNames){
            this.mToysListTextView.append(toyName + "\n\n\n");
        }
    }
}
