package weatherapp.danarcheronline.com.weatherapp.Data.Network;

import android.net.Uri;
import android.util.Log;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Scanner;

public class NetworkUtils {

//    tag for debugging purposes
    private static final String TAG = NetworkUtils.class.getSimpleName();

//    base url for the open weather maps api.
//    retrieves weather data for 5 days (today and the following 4 days) with 3 hour increments
//    giving a total of 40 weather data objects. 8 weather data objects for each day
    private static final String FORECAST_BASE_URL = "https://api.openweathermap.org/data/2.5/forecast";
//    personal open weather maps app id to be used with all api calls
    private static final String OWM_APPID = "2aa667f10e93d91e290fee31cad9c4cf";
//    query key/value key constants
    final static String PARAM_QUERY = "q";
    final static String APPID_QUERY = "APPID";

    /**
     * Get a parsed Uri from the base url string of the open weather maps api
     * and the necessary appended queries.
     * Then try to convert the Uri to a URL object and return it.
     *
     * @param locationQuery The location that will be queried for.
     * @return The URL to use to query the weather server.
     */
//    build a Uri from the base url string and necessary query key/value pairs
    public static URL buildUri(String locationQuery) {
        Uri builtURI = Uri.parse(FORECAST_BASE_URL).buildUpon()
                .appendQueryParameter(PARAM_QUERY, locationQuery)
                .appendQueryParameter(APPID_QUERY, OWM_APPID)
                .build();

        URL url = null;
        try {
//            convert the Uri into a URL object
            url = new URL(builtURI.toString());
        }
        catch (MalformedURLException e) {
            Log.e(TAG, "buildUri: could not form a URL from the Uri" + e);
        }

        return url;
    }

    /**
     * Try to get a response from open weather maps api using the passed in URL object
     * and return that response.
     * If failed, null will be returned.
     *
     * @param url the url to get the http request from.
     * @return String of received json data if successful. Returns null if not.
     * @throws IOException if something goes wrong with the network or stream reading.
     */
    public static String getResponseFromHttpUrl(URL url) throws IOException {

//        create a HttpURLConnection object with the passed in URL object to set up the connection
        HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
        try {
//            get an input stream from the open connection
            InputStream inputStream = httpURLConnection.getInputStream();

//            create a scanner (used to tokenize streams), to read the contents of the input stream
            Scanner scanner = new Scanner(inputStream);
//            setting the delimiter as \A forces the scanner to read the entire contents of the
//            input stream into the next token stream
            scanner.useDelimiter("\\A");

//            check there is still something to retrieve
            boolean hasInput = scanner.hasNext();

            if(hasInput) {
//                return all retrieved json data
                return scanner.next();
            }
            else {
                return null;
            }
        }
        finally {
//            close the Http URL connection whether the request was successful or not
            httpURLConnection.disconnect();
        }
    }

}
