package weatherapp.danarcheronline.com.weatherapp.Data.Database;

import android.arch.lifecycle.LiveData;
import android.arch.paging.DataSource;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;

import java.util.List;

@Dao
public interface WeatherDAO {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    public void bulkInsert(WeatherForecastEntity... weatherForecastEntities);

    @Query("SELECT * FROM weather")
    public LiveData<WeatherForecastEntity> getAll();

}
