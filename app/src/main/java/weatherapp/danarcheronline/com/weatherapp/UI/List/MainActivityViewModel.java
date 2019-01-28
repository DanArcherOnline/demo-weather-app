package weatherapp.danarcheronline.com.weatherapp.UI.List;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.ViewModel;

import java.util.List;

import weatherapp.danarcheronline.com.weatherapp.Data.Database.WeatherForecastEntity;
import weatherapp.danarcheronline.com.weatherapp.Data.Repository;

public class MainActivityViewModel extends ViewModel {

    private final Repository mRepository;
    private final LiveData<WeatherForecastEntity> mWeatherForecastEntity;

    public MainActivityViewModel(Repository repository) {
        this.mRepository = repository;
        this.mWeatherForecastEntity = mRepository.getAllWeather();
    }

    public LiveData<WeatherForecastEntity> getWeather() {
        return mWeatherForecastEntity;
    }
}
