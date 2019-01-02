package weatherapp.danarcheronline.com.weatherapp.Utils;

import android.util.Log;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.net.HttpURLConnection;

public final class WeatherJsonUtils {

    //    tag for debugging purposes
    private static final String TAG = WeatherJsonUtils.class.getSimpleName();

    /**
     * Makes sure the json data was successfuly retrieved, extracts the necessary
     * weather data from the json data, and puts the weather data into a string array
     *
     * @param weatherJsonString
     * @return A string array of weather data retrieved from open weather map
     * @throws JSONException
     */
    public static String[] getStringsFromJson(String weatherJsonString) throws JSONException {

//        json key/value pair key constants
        //holds the weather data for that forecast
        final String OWM_LIST = "list";
        //holds the temperature data and other metrics
        final String OWM_MAIN = "main";
        //hold the max temperature
        final String OWM_MAX = "temp_max";
        //holds the minimum temperature
        final String OWM_MIN = "temp_min";
        //holds the general weather data
        final String OWM_WEATHER = "weather";
        //holds the main description of the weather
        final String OWM_DESCRIPTION = "main";
        //holds the HTTP request code for checking if the correct json response was retrieved
        final String OWM_MESSAGE_CODE = "cod";

//        the string array to hold all the weather data
        String[] parsedWeatherDataArray;

//        convert the json string data into a json object
        JSONObject weatherJsonObject = new JSONObject(weatherJsonString);

//        if the json data has a key/value key of "cod"...
        if(weatherJsonObject.has(OWM_MESSAGE_CODE)) {

//            get the value for the key/value pair of "cod"
//            which tells us how our api request went
            int errorCode = weatherJsonObject.getInt(OWM_MESSAGE_CODE);

            switch (errorCode) {
//
                case HttpURLConnection.HTTP_OK:
                    Log.d(TAG, "getStringsFromJson: HTTP OK");
                    break;
                case HttpURLConnection.HTTP_NOT_FOUND:
                    //location invalid
                    Log.d(TAG, "getStringsFromJson: HTTP NOT FOUND");
                    return null;
                default:
                    //server might be down
                    Log.d(TAG, "getStringsFromJson: Server may be down");
                    return null;
            }
        }

//
        JSONArray weatherJsonArray = weatherJsonObject.getJSONArray(OWM_LIST);
        Log.d(TAG, "getStringsFromJson: weatherJsonArray: " + weatherJsonArray);

        parsedWeatherDataArray = new String[weatherJsonArray.length()];
        Log.d(TAG, "getStringsFromJson: parsedWeatherDataArray length: " + parsedWeatherDataArray.length);

        // TODO: (02/01/2019) make sure date is correct and convert timestamp to readable formatted date
        long localDate = System.currentTimeMillis();
        Log.d(TAG, "getStringsFromJson: localDate = " + localDate);

        for(int i = 0; i < weatherJsonArray.length(); i++) {

            double high;
            double low;
            String description;

//            get the weather forecast object
            JSONObject weatherForecastObject = weatherJsonArray.getJSONObject(i);

//            get the general weather information object from the weather forecast object
            JSONObject weatherObject =
                    weatherForecastObject.getJSONArray(OWM_WEATHER).getJSONObject(0);

//            get the detailed description of the weather from the general weather object
            description = weatherObject.getString(OWM_DESCRIPTION);

//            get the object that holds the temperature data
            JSONObject temperatureObject = weatherForecastObject.getJSONObject(OWM_MAIN);

//            get the highest temperature
            high = temperatureObject.getDouble(OWM_MAX);

//            get the lowest temperature
            low = temperatureObject.getDouble(OWM_MIN);

//            concatenate all extracted weather data into a single string and append it a string array
            parsedWeatherDataArray[i] =
                    "Date: " + localDate + " - " + description + "\n"
                    + "Lowest Temperature: " + low + "\n"
                    + "Highest Temperature" + high;
        }

        return  parsedWeatherDataArray;
    }
}
