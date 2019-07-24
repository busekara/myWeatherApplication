package com.example.myweatherapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;

public class MainActivity extends AppCompatActivity {
    private TextView cityTxt, tempTxt, weatherTxt, descTxt;
    private Button button;
    private EditText editText;
    private ImageView image;
    String sehir;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initActivity();

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                JsonParse jsonParse = new JsonParse();
                sehir = String.valueOf(editText.getText());
                new JsonParse().execute();

            }
        });

    }

    private void initActivity() {
        cityTxt = (TextView) findViewById(R.id.cityTxt);
        tempTxt = (TextView) findViewById(R.id.tempTxt);
        weatherTxt = (TextView) findViewById(R.id.weatherTxt);
        descTxt = (TextView) findViewById(R.id.descTxt);
        button = (Button) findViewById(R.id.button);
        editText = (EditText) findViewById(R.id.editText);
        image = (ImageView) findViewById(R.id.imageView);
    }

    protected class JsonParse extends AsyncTask<Void, Void, Void> {

        String result_main = "";
        String result_description = "";
        String result_icon = "";
        int result_temp;
        String result_city;
        Bitmap bitImage;

        @Override
        protected Void doInBackground(Void... params) {
            String result = "";
            try {
                URL weather_url = new URL("http://api.openweathermap.org/data/2.5/weather?q=" + sehir + "&APPID=e4c120256cfeddfbc30fb034d1484375");

                //HttpURLConnection weather_url_con = (HttpURLConnection) weather_url.openConnection();
                // InputStreamReader inputStreamReader = new InputStreamReader(weather_url_con.getInputStream());
                BufferedReader bufferedReader = null;
                bufferedReader = new BufferedReader(new InputStreamReader(weather_url.openStream()));
                String line = null;
                while ((line = bufferedReader.readLine()) != null) {
                    result += line;
                }
                bufferedReader.close();

                JSONObject jsonObject = new JSONObject(result);
                JSONArray jsonArray = jsonObject.getJSONArray("weather");
                JSONObject jsonObject_weather = jsonArray.getJSONObject(0);
                result_main = jsonObject_weather.getString("main");
                result_description = jsonObject_weather.getString("description");
                result_icon = jsonObject_weather.getString("icon");

                JSONObject jsonObject_main = jsonObject.getJSONObject("main");
                Double temp = jsonObject_main.getDouble("temp");

                result_city = jsonObject.getString("name");

                result_temp = (int) (temp - 273);

                URL icon_url = new URL("http://openweathermap.org/img/w/" + result_icon + ".png");
                bitImage = BitmapFactory.decodeStream(icon_url.openConnection().getInputStream());

            } catch (IOException e) {
                e.printStackTrace();
            } catch (JSONException e) {
                e.printStackTrace();
            }

            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            tempTxt.setText(String.valueOf(result_temp));
            weatherTxt.setText(result_main);
            cityTxt.setText(result_city);
            descTxt.setText(result_description);
            image.setImageBitmap(bitImage);
            super.onPostExecute(aVoid);
        }
    }
}

