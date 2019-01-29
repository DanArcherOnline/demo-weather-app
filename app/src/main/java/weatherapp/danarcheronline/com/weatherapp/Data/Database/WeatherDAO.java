package weatherapp.danarcheronline.com.weatherapp.Data.Database;

import android.arch.lifecycle.LiveData;
import android.arch.paging.DataSource;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;

import java.util.List;

@Dao
public interface WeatherDAO {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void bulkInsert(WeatherForecastEntity... weatherForecastEntities);

    @Query("SELECT * FROM weather")
    LiveData<List<WeatherForecastEntity>> getAll();

    @Query("DELETE FROM weather")
    void deleteAll();

}
