package com.example.se328projectalalem;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.bumptech.glide.Glide;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Button firebase = findViewById(R.id.fireBaseBttn);
        Button sql = findViewById(R.id.sqliteBttn);
        Button weather = findViewById(R.id.weatherActivityBttn);
        ImageView weatherimgMain = findViewById(R.id.weatherMain);
        final SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(this);

        String img = pref.getString("url",null);
        Log.d("Mohammed", img.toString());
        Glide.with(MainActivity.this).load(img).into(weatherimgMain);

        firebase.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this,FirebaseActivity.class));
            }
        });
        sql.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainActivity.this,SQLiteActivity.class));
            }
        });
        weather.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainActivity.this,WeatherActivity.class));
            }
        });
    }
}