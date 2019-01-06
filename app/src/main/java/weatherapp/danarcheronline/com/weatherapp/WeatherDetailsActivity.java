package weatherapp.danarcheronline.com.weatherapp;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

public class WeatherDetailsActivity extends AppCompatActivity {

//    view variable instantiations
    TextView tv_weather_details;

    String weatherDetailsString;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_weather_details);

//        set up views
        initViews();

//        get weather data from intent and load the data into the corresponding views
        loadWeatherDetails();

    }

    /**
     * makes sure the intent exists and has the necessary data and then gets the weather data
     * from intent and loads the data into the corresponding views.
     */
    private void loadWeatherDetails() {
        Intent weatherDetailsIntent = getIntent();
        if(weatherDetailsIntent != null) {
            if(weatherDetailsIntent.hasExtra(Intent.EXTRA_TEXT)) {
                weatherDetailsString = weatherDetailsIntent.getStringExtra(Intent.EXTRA_TEXT);
                tv_weather_details.setText(weatherDetailsString);
            }
        }
    }

    /**
     * Assign the views in activity_weather_details.xml to matching variables
     */
    private void initViews() {
        tv_weather_details = findViewById(R.id.tv_weather_details);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        return super.onOptionsItemSelected(item);
    }
}
