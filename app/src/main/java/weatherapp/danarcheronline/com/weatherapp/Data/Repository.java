package weatherapp.danarcheronline.com.weatherapp.Data;

import android.arch.lifecycle.LiveData;
import android.content.Context;
import android.util.Log;

import java.util.List;

import weatherapp.danarcheronline.com.weatherapp.Data.Database.WeatherDAO;
import weatherapp.danarcheronline.com.weatherapp.Data.Database.WeatherDatabase;
import weatherapp.danarcheronline.com.weatherapp.Data.Database.WeatherForecastEntity;
import weatherapp.danarcheronline.com.weatherapp.Data.Network.WeatherNetworkDataSource;
import weatherapp.danarcheronline.com.weatherapp.Utils.AppExecutors;

public class Repository {

    private static final String TAG = Repository.class.getSimpleName();
    private static final Object LOCK = new Object();
    private static Repository sInstance;
    private static boolean mInitialized = false;

    private Context mContext;
    private AppExecutors mAppExecutors;
    private WeatherDAO mWeatherDAO;

    private WeatherNetworkDataSource mWeatherNetworkDataSource;

    private Repository(Context context, AppExecutors appExecutors, WeatherDAO dao, WeatherNetworkDataSource dataSource) {
        this.mContext = context;
        this.mAppExecutors = appExecutors;
        this.mWeatherDAO = dao;
        this.mWeatherNetworkDataSource = dataSource;

        LiveData<WeatherForecastEntity[]> weatherDataFromNetwork = mWeatherNetworkDataSource.getmDownloadedWeatherForecasts();
        weatherDataFromNetwork.observeForever(newDataFromNetwork -> {
            mAppExecutors.diskIO().execute(() -> {
                dao.bulkInsert(newDataFromNetwork);
                Log.d(TAG, "Repository: Bulk inserted new weather entities into database");
            });
        });
    }

    public static Repository getInstance(Context context, AppExecutors appExecutors, WeatherDAO dao, WeatherNetworkDataSource dataSource) {
        if(sInstance == null) {
            synchronized (LOCK) {
                sInstance = new Repository(context, appExecutors, dao, dataSource);
            }
        }
        return sInstance;
    }

    public LiveData<List<WeatherForecastEntity>> getAllWeather() {
        initializeData();
        return mWeatherDAO.getAll();
    }

    private synchronized void initializeData() {
        if(mInitialized) {
            return;
        }
        mInitialized = true;

        fetchWeather(mContext);
    }

    private void fetchWeather(Context context) {
        mWeatherNetworkDataSource.fetchWeather(context);
        Log.d(TAG, "fetchWeather: Repository asked network datasource to fetch the weather");
    }

}
