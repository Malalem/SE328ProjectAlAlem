package com.example.se328projectalalem;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Date;

import es.dmoral.toasty.Toasty;
import io.github.muddz.styleabletoast.StyleableToast;

public class WeatherActivity extends AppCompatActivity {
    JSONObject jsonObj;

    ImageView img;
    TextView temp,hum;
    EditText city;
    Button setCity, back;
    String mainCity = "Berlin";
    String url ="https://api.openweathermap.org/data/2.5/weather?q="+mainCity+"&appid=a21f3cf1db6a937fea438e2feccb64a6&units=metric";
    //SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(this);
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_weather);

        img = (ImageView) findViewById(R.id.weatherImg);
        temp = (TextView) findViewById(R.id.temp);
        hum = (TextView) findViewById(R.id.humd);
        city = (EditText) findViewById(R.id.cityName);
        setCity = (Button) findViewById(R.id.changeCitybttn);
        back = (Button) findViewById(R.id.fromWeatherToMain);

        SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(this);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(WeatherActivity.this,MainActivity.class));
            }
        });
        setCity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SharedPreferences.Editor editor = pref.edit();
                String newCity;
                newCity = city.getText().toString();


                editor.putString("city",newCity);
                editor.commit();
                Log.d("Mohammed",pref.getString("city",null).toString());

                city.setText(pref.getString("city",null));
                newCity = city.getText().toString();
                url ="https://api.openweathermap.org/data/2.5/weather?q="+newCity+"&appid=a21f3cf1db6a937fea438e2feccb64a6&units=metric";
                weather(url);
                //Toast.makeText(WeatherActivity.this,"City is set to "+newCity,Toast.LENGTH_LONG).show();
                Toasty.success(WeatherActivity.this, "City is set to "+newCity, Toast.LENGTH_SHORT, true).show();
            }
        });

        img = (ImageView) findViewById(R.id.weatherImg);
        weather(url);

    }

    public void weather(String url) {
        SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(this);
        SharedPreferences.Editor editor = pref.edit();
        JsonObjectRequest jsonObj = new JsonObjectRequest(Request.Method.GET,
                url,
                null,
                new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {

                        Log.d("Mohammed", "Response received");
                        Log.d("Mohammed", response.toString());
                        try {
                            JSONObject jsonMain = response.getJSONObject("main");
                            JSONObject jsonSys = response.getJSONObject("sys");

                            double temper = jsonMain.getDouble("temp");
                            Log.d("Mohammed", "temp=" + String.valueOf(temper)+"º");
                            temp.setText(String.valueOf(temper)+"º C");

                            double humid = jsonMain.getDouble("humidity");
                            Log.d("Mohammed", "humidity=" + String.valueOf(humid));
                            hum.setText("humidity: " +String.valueOf(humid));
                            JSONArray jasonArray = response.getJSONArray("weather");
                            for (int i = 0; i < jasonArray.length(); i++) {
                                JSONObject oneObject = jasonArray.getJSONObject(i);
                                String weather = oneObject.getString("main");
                                Log.d("Mohammed-w", weather);

                                //SharedPreferences.Editor editor = pref.edit();
                                if (weather.equals("Clear")) {
                                    Glide.with(WeatherActivity.this).load("https://i.picsum.photos/id/1067/5760/3840.jpg?hmac=gO_V7rUFdM8YddyLysCQet4CS0CzSvUcfAtHI1ismLM").into(img);
                                    String imgUrl="https://i.picsum.photos/id/1067/5760/3840.jpg?hmac=gO_V7rUFdM8YddyLysCQet4CS0CzSvUcfAtHI1ismLM";
                                    editor.putString("url",imgUrl);
                                    editor.commit();
                                    //editor.putString("ImageLink",);
                                } else if (weather.equals("Clouds")) {
                                    Glide.with(WeatherActivity.this).load("https://i.picsum.photos/id/1056/3988/2720.jpg?hmac=qX6hO_75zxeYI7C-1TOspJ0_bRDbYInBwYeoy_z_h08").into(img);
                                    String imgUrl="https://i.picsum.photos/id/1056/3988/2720.jpg?hmac=qX6hO_75zxeYI7C-1TOspJ0_bRDbYInBwYeoy_z_h08";
                                    editor.putString("url",imgUrl);
                                    editor.commit();
                                } else if (weather.equals("Rain")) {
                                    Glide.with(WeatherActivity.this).load("https://i.picsum.photos/id/178/2592/1936.jpg?hmac=lW1JtHlmevwr41pQSILng2JYKGlX7fLMmptsz45JKZ0").into(img);
                                    String imgUrl="https://i.picsum.photos/id/178/2592/1936.jpg?hmac=lW1JtHlmevwr41pQSILng2JYKGlX7fLMmptsz45JKZ0";
                                    editor.putString("url",imgUrl);
                                    editor.commit();
                                }
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                            Log.e("JSON Error", e.toString());
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("Mohammed", error.getMessage());

            }
        }
        );
        RequestQueue queue = Volley.newRequestQueue(this);
        queue.add(jsonObj);
    }
    public String weatherAll(String imgLink){
        return imgLink=imgLink;
    }
}