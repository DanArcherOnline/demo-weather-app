package weatherapp.danarcheronline.com.weatherapp.Data.Network;

import android.content.Context;
import android.util.Log;

import java.net.URL;

import weatherapp.danarcheronline.com.weatherapp.Data.Database.WeatherForecastEntity;
import weatherapp.danarcheronline.com.weatherapp.UI.Preferences.PreferenceUtils;

public class WeatherSync {

    private static final String TAG = WeatherSync.class.getSimpleName();

    synchronized public static WeatherForecastEntity[] syncWeather(Context context) {

//        get the first location string from the locations string array
//        there should only be one location string in the string array anyway
//        String location = strings[0];
        String location = PreferenceUtils.getPreferenceLocation(context);

        try {
//            convert a string url with the 'location' parameter as a query in the url to a URL object
            URL weatherRequestURL = NetworkUtils.buildUri(location);

//                get the json data using the URL object
            String jsonWeatherResponse = NetworkUtils.getResponseFromHttpUrl(weatherRequestURL);
            Log.d(TAG, "syncWeather: raw JSON data retrieved");

//                get the weather data from the retrieved json and store it in a string array
            WeatherForecastEntity[] weatherEntitiesFromJson = WeatherJsonUtils.getWeatherEntitiesFromJson(jsonWeatherResponse);
            Log.d(TAG, "syncWeather: raw JSON data converted into an array of weather entities");

            return weatherEntitiesFromJson;
        }
        catch(Exception e) {
            return null;
        }

    }

}
