package weatherapp.danarcheronline.com.weatherapp;

import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import java.net.URL;
import weatherapp.danarcheronline.com.weatherapp.Utils.NetworkUtils;
import weatherapp.danarcheronline.com.weatherapp.Utils.WeatherJsonUtils;

public class ForecastActivity extends AppCompatActivity {

//    tag for debugging purposes
    private static final String TAG = ForecastActivity.class.getSimpleName();

//    View variable instantiations
    TextView tv_weather_data;
    TextView tv_error_message_display;
    ProgressBar pb_loading_indicator;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forecast);

        //set up views
        initViews();

        //show weather data view and get weather data
        loadWeatherData();
    }


    /**
     *  Turns the view for the weather data on, starts the async task to get and display
     *  the weather data. If it's not successful, an error message is displayed.
     */
    private void loadWeatherData() {
        showWeatherDataView();
//        start the async task with the location string parameter to get the weather data
        new GetWeatherDataAsyncTask().execute("Tokyo");
    }


    /**
     * Assign the views in activity_forecast to matching variables
     */
    private void initViews() {
        tv_weather_data = findViewById(R.id.tv_weather_data);
        tv_error_message_display = findViewById(R.id.tv_error_message_display);
        pb_loading_indicator = findViewById(R.id.pb_loading_indicator);
    }


    /**
     * Makes the view that holds all the weather data visible and the error message view invisible
     */
    private void showWeatherDataView() {
        tv_error_message_display.setVisibility(View.INVISIBLE);
        tv_weather_data.setVisibility(View.VISIBLE);
    }


    /**
     *
     */
    private void showErrorMessageView() {
        tv_error_message_display.setVisibility(View.VISIBLE);
        tv_weather_data.setVisibility(View.INVISIBLE);
    }


    /**
     *  an async task to fetch the json weather data from the open weather map api,
     *  and displays it if the http request and json extraction was successful.
     *  While retrieving the data from the internet a progress bar is shown and then made invisible
     *  once the result of the http request comes back.
     *  If not successful, it displays an error message to the user saying something went wrong.
     *
     *  Takes a string location and produces a string array of extracted weather data.
     */
    public class GetWeatherDataAsyncTask extends AsyncTask<String, Void, String[]> {

        private final String TAG = GetWeatherDataAsyncTask.class.getSimpleName();


        /**
         * Before the async task starts:
         * show the loading progress bar
         */
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pb_loading_indicator.setVisibility(View.VISIBLE);

        }


        /**
         * The Async task on the background thread:
         * Tries to get save some json weather data using the passed in location string
         * from open weather maps api and then extract and put the data into
         * a string array variable called 'simpleJsonWeatherData'.
         * If either the http request or the json data extraction is unsuccessful, the error is
         * caught and null is returned.
         * Null is also returned if there were no location strings in the string array parameter.
         *
         * @param strings
         * @return simpleJsonWeatherData
         * @return null
         */
        @Override
        protected String[] doInBackground(String... strings) {

//            return null if there are no location strings in the locations string array
            if(strings.length == 0) {
                return null;
            }

//            get the first location string from the locations string array
//            there should only be one location string in the string array anyway
            String location = strings[0];

//            convert a string url with the 'location' parameter as a query in the url to a URL object
            URL weatherRequestURL = NetworkUtils.buildUri(location);

            try {
//                get the json data using the URL object
                String jsonWeatherResponse = NetworkUtils.getResponseFromHttpUrl(weatherRequestURL);

//                get the weather data from the retrieved json and store it in a string array
                String[] simpleJsonWeatherData = WeatherJsonUtils.getStringsFromJson(jsonWeatherResponse);

                return simpleJsonWeatherData;
            }
            catch(Exception e) {
                return null;
            }
        }


        /**
         *After the background task has been completed:
         * hides the loading loading progress bar and inserts the data into the weather data view.
         * If the weather data could not be retrieved, the error message display is shown instead.
         *
         * @param weatherData
         */
        @Override
        protected void onPostExecute(String[] weatherData) {

//            make the loading progress bar invisible
            pb_loading_indicator.setVisibility(View.INVISIBLE);

//            if there is weather data
            if(weatherData != null) {
//                show the weather data view and get the weather data
                showWeatherDataView();

//                go through all the retrieved weather data strings and append them to
//                the weather data view
                for(String weatherString : weatherData) {
                    tv_weather_data.append(weatherString + "\n\n\n");
                }
            }
            else {
//                show the error message display
                showErrorMessageView();
            }
        }
    }
}
