package weatherapp.danarcheronline.com.weatherapp.UI.List;

import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import weatherapp.danarcheronline.com.weatherapp.Data.Database.WeatherDatabase;
import weatherapp.danarcheronline.com.weatherapp.Data.Database.WeatherForecastEntity;
import weatherapp.danarcheronline.com.weatherapp.Data.Repository;
import weatherapp.danarcheronline.com.weatherapp.UI.Preferences.PreferenceSettingsActivity;
import weatherapp.danarcheronline.com.weatherapp.R;
import weatherapp.danarcheronline.com.weatherapp.RecyclerView.WeatherDataRecyclerViewAdapter;
import weatherapp.danarcheronline.com.weatherapp.Data.Network.WeatherSync;
import weatherapp.danarcheronline.com.weatherapp.UI.Detail.WeatherDetailsActivity;
import weatherapp.danarcheronline.com.weatherapp.Utils.AppExecutors;
import weatherapp.danarcheronline.com.weatherapp.Utils.InjectorUtils;

public class MainActivity extends AppCompatActivity
        implements WeatherDataRecyclerViewAdapter.WeatherItemClickListener,
        SharedPreferences.OnSharedPreferenceChangeListener {

//    tag for debugging purposes
    private static final String TAG = MainActivity.class.getSimpleName();

//    view variable instantiations
    RecyclerView rv_weather_data;
    TextView tv_error_message_display;
    ProgressBar pb_loading_indicator;

//    other instantiations
    WeatherDataRecyclerViewAdapter adapter;
    Toast mainUiToast;
    boolean preferencesUpdated;

    private MainActivityViewModel mainActivityViewModel;

    @Override
    protected void onStart() {
        super.onStart();

        if(preferencesUpdated) {
//            start the background task to retrieve and display the weather data
            new GetWeatherDataAsyncTask().execute();
            preferencesUpdated = false;
        }
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forecast);

        MainActivityViewModelFactory factory = InjectorUtils.provideMainActivityViewModelFactory(this);
        mainActivityViewModel = ViewModelProviders.of(this, factory).get(MainActivityViewModel.class);
        mainActivityViewModel.getWeather().observe(this, weatherForecastEntity -> {
            //updat ui here
            Log.d(TAG, "onCreate: info from MainActivityViewModel: " + weatherForecastEntity);
        });

//        set up views
        initViews();

//        set up recycler view and adapter
        initRecyclerViewAdapter();

//        register the preference changed listener
        PreferenceManager.getDefaultSharedPreferences(this)
                .registerOnSharedPreferenceChangeListener(this);


//        show weather data view and get weather data
        loadWeatherData();

    }


    @Override
    protected void onDestroy() {
        super.onDestroy();

//        unregister the preference changed listener
        PreferenceManager.getDefaultSharedPreferences(this)
                .unregisterOnSharedPreferenceChangeListener(this);

    }


    /**
     *  Turns the view for the weather data on, starts the async task to get and display
     *  the weather data. If it's not successful, an error message is displayed.
     */
    private void loadWeatherData() {
        showWeatherDataView();
//        start the async task with the location string parameter to get the weather data
        new GetWeatherDataAsyncTask().execute();
    }


    /**
     * Assign the views in activity_forecast.xml to matching variables
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
        adapter = new WeatherDataRecyclerViewAdapter(MainActivity.this);
        rv_weather_data.setLayoutManager(linearLayoutManager);
        rv_weather_data.setAdapter(adapter);
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
     * Opens {@link WeatherDetailsActivity} and passes along the corresponding weather data to it
     * @param weatherItemString
     */
    @Override
    public void onClickWeatherItem(String weatherItemString) {
//        intent to open the weather details activity
        Intent weatherDetailsIntent = new Intent(MainActivity.this, WeatherDetailsActivity.class);
//        pass along the weather data in the intent
        weatherDetailsIntent.putExtra(Intent.EXTRA_TEXT, weatherItemString);
        startActivity(weatherDetailsIntent);
    }


    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String preferenceKey) {
        if(preferenceKey.equals("location")) {
            preferencesUpdated = true;
        }
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
    public class GetWeatherDataAsyncTask extends AsyncTask<Void, Void, WeatherForecastEntity[]> {

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
         *
         * @return simpleJsonWeatherData
         * @return null
         */
        @Override
        protected WeatherForecastEntity[] doInBackground(Void... voids) {
            return WeatherSync.syncWeather(MainActivity.this);
        }


        /**
         *After the background task has been completed:
         * hides the loading loading progress bar and inserts the data into the weather data view.
         * If the weather data could not be retrieved, the error message display is shown instead.
         *
         * @param weatherData
         */
        @Override
        protected void onPostExecute(WeatherForecastEntity[] weatherData) {

//            make the loading progress bar invisible
            pb_loading_indicator.setVisibility(View.INVISIBLE);

//            if there is weather data
            if(weatherData != null) {
//                show the weather data view and get the weather data
                showWeatherDataView();

//                pass the extracted weather data to the adapter
                adapter.setWeatherDataArray(weatherData);
            }
            else {
//                show the error message display
                showErrorMessageView();
            }
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.options_menu_main, menu);
        return true;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int itemClickedID = item.getItemId();
        switch(itemClickedID) {
            case R.id.options_menu_refresh:
//                refresh the weather data by setting the adapters data to null and making a new request
                // TODO: (03/01/2019) replace refresh function with firebase job scheduler
                adapter.setWeatherDataArray(null);
                loadWeatherData();
                return true;
            case R.id.options_menu_preference_settings:
//                open preference settings screen
                Intent preferenceSettingsIntent = new Intent(MainActivity.this, PreferenceSettingsActivity.class);
                startActivity(preferenceSettingsIntent);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}