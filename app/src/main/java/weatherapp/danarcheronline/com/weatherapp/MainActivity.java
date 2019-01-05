package weatherapp.danarcheronline.com.weatherapp;

import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import java.net.URL;

import weatherapp.danarcheronline.com.weatherapp.RecyclerView.WeatherDataRecyclerViewAdapater;
import weatherapp.danarcheronline.com.weatherapp.Utils.NetworkUtils;
import weatherapp.danarcheronline.com.weatherapp.Utils.WeatherJsonUtils;

public class MainActivity extends AppCompatActivity {

//    tag for debugging purposes
    private static final String TAG = MainActivity.class.getSimpleName();

//    view variable instantiations
    RecyclerView rv_weather_data;
    TextView tv_error_message_display;
    ProgressBar pb_loading_indicator;

//    other instantiations
    WeatherDataRecyclerViewAdapater adapater;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forecast);

//        set up views
        initViews();

//        set up recycler view and adapter
        initRecyclerViewAdapter();

//        show weather data view and get weather data
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
        rv_weather_data = findViewById(R.id.rv_weather_data);
        tv_error_message_display = findViewById(R.id.tv_error_message_display);
        pb_loading_indicator = findViewById(R.id.pb_loading_indicator);
    }

    /**
     * Set up and connect recycler view and adapter
     */
    private void initRecyclerViewAdapter() {
//        make recycler view display as a linear list
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        adapater = new WeatherDataRecyclerViewAdapater();
        rv_weather_data.setLayoutManager(linearLayoutManager);
        rv_weather_data.setAdapter(adapater);
//        stop recycler view from requesting the layout whenever the adapters contents changes
//        (this recycler views width and height will not change regardless of data in the adapter
//        so it will always have a fixed size in terms of dimensions)
        rv_weather_data.setHasFixedSize(true);
    }


    /**
     * Makes the recycler view that holds all the weather data visible and the error message view invisible
     */
    private void showWeatherDataView() {
        tv_error_message_display.setVisibility(View.INVISIBLE);
        rv_weather_data.setVisibility(View.VISIBLE);
    }


    /**
     * Makes the recycler view that holds all the weather data invisible and the error message view visible
     */
    private void showErrorMessageView() {
        tv_error_message_display.setVisibility(View.VISIBLE);
        rv_weather_data.setVisibility(View.INVISIBLE);
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

//        tag for debugging purposes
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
                String[] jsonWeatherData = WeatherJsonUtils.getStringsFromJson(jsonWeatherResponse);

                return jsonWeatherData;
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

//                pass the extracted weather data to the adapter
                adapater.setWeatherDataArray(weatherData);
            }
            else {
//                show the error message display
                showErrorMessageView();
            }
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_forecast, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int itemClickedID = item.getItemId();
        if(itemClickedID == R.id.refresh) {
//            refresh the weather data by setting the adapters data to null and making a new request
            // TODO: (03/01/2019) replace refresh function with automatic refreshing
            adapater.setWeatherDataArray(null);
            loadWeatherData();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}