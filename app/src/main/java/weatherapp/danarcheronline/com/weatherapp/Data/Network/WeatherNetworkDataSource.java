package weatherapp.danarcheronline.com.weatherapp.Data.Network;

import android.arch.lifecycle.MutableLiveData;
import android.content.Context;
import android.util.Log;

import java.util.prefs.AbstractPreferences;

import weatherapp.danarcheronline.com.weatherapp.Data.Database.WeatherForecastEntity;
import weatherapp.danarcheronline.com.weatherapp.Utils.AppExecutors;

public class WeatherNetworkDataSource {

    private static final String TAG = WeatherNetworkDataSource.class.getSimpleName();
    private static final Object LOCK = new Object();
    private static WeatherNetworkDataSource sInstance;

    private final AppExecutors mAppExecutors;

    //holds the downloaded data that been converted into WeatherForecastEntities
    //it is observed by the repository
    MutableLiveData<WeatherForecastEntity[]> mDownloadedWeatherForecasts;

    public WeatherNetworkDataSource(AppExecutors appExecutors) {
        this.mAppExecutors = appExecutors;
        this.mDownloadedWeatherForecasts = new MutableLiveData<WeatherForecastEntity[]>();
    }

    public static WeatherNetworkDataSource getInstance(AppExecutors appExecutors) {
        if(sInstance == null) {
            synchronized (LOCK) {
                sInstance = new WeatherNetworkDataSource(appExecutors);
            }
        }
        return sInstance;
    }

    public MutableLiveData<WeatherForecastEntity[]> getmDownloadedWeatherForecasts() {
        return mDownloadedWeatherForecasts;
    }

    public void fetchWeather(Context context) {
        mAppExecutors.diskIO().execute(() -> {
            WeatherForecastEntity[] weatherEntitiesFromJson = WeatherSync.syncWeather(context);

            if(weatherEntitiesFromJson != null && weatherEntitiesFromJson.length != 0) {
                Log.d(TAG, "JSON was not null and has " + weatherEntitiesFromJson.length
                        + " values");
                mDownloadedWeatherForecasts.postValue(weatherEntitiesFromJson);
                Log.d(TAG, "fetchWeather: mDownloadedWeatherForecasts has received new weather entities array");
            }

        });
    }
}
