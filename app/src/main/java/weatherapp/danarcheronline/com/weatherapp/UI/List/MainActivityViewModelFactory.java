package weatherapp.danarcheronline.com.weatherapp.UI.List;

import android.arch.lifecycle.ViewModel;
import android.arch.lifecycle.ViewModelProvider;
import android.support.annotation.NonNull;

import weatherapp.danarcheronline.com.weatherapp.Data.Repository;

public class MainActivityViewModelFactory extends ViewModelProvider.NewInstanceFactory {

    private final Repository mRepository;

    public MainActivityViewModelFactory(Repository repository) {
        this.mRepository = repository;
    }

    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        return (T) new MainActivityViewModel(mRepository);
    }
}
