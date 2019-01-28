package weatherapp.danarcheronline.com.weatherapp.Utils;

import android.content.Context;

import weatherapp.danarcheronline.com.weatherapp.Data.Database.WeatherDatabase;
import weatherapp.danarcheronline.com.weatherapp.Data.Network.WeatherNetworkDataSource;
import weatherapp.danarcheronline.com.weatherapp.Data.Repository;
import weatherapp.danarcheronline.com.weatherapp.UI.List.MainActivityViewModel;
import weatherapp.danarcheronline.com.weatherapp.UI.List.MainActivityViewModelFactory;

public class InjectorUtils {

    public static Repository provideRepository(Context context) {
        AppExecutors executors = AppExecutors.getInstance();
        WeatherDatabase database = WeatherDatabase.getInstance(context.getApplicationContext());
        WeatherNetworkDataSource dataSource = WeatherNetworkDataSource.getInstance(executors);
        return Repository.getInstance(context, executors, database.weatherDAO(), dataSource);
    }

    public static MainActivityViewModelFactory provideMainActivityViewModelFactory(Context context) {
        Repository repository = provideRepository(context);
        return new MainActivityViewModelFactory(repository);
    }
}
