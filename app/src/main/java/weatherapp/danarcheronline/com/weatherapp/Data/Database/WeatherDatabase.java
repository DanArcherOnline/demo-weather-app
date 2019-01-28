package weatherapp.danarcheronline.com.weatherapp.Data.Database;

import android.arch.persistence.db.SupportSQLiteOpenHelper;
import android.arch.persistence.room.Database;
import android.arch.persistence.room.DatabaseConfiguration;
import android.arch.persistence.room.InvalidationTracker;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.content.Context;
import android.support.annotation.NonNull;

@Database(entities = WeatherForecastEntity.class, version = 1, exportSchema = false)
public abstract class WeatherDatabase extends RoomDatabase {

    private static final String TAG = WeatherDatabase.class.getSimpleName();
    private static final String DATABASE_NAME = "weather";

    private static final Object LOCK = new Object();
    private static WeatherDatabase sInstance;

    public static WeatherDatabase getInstance(Context context) {
        if(sInstance == null) {
            synchronized (LOCK) {
                sInstance = Room.databaseBuilder(context.getApplicationContext(),
                        WeatherDatabase.class,
                        WeatherDatabase.DATABASE_NAME).build();
            }
        }
        return sInstance;
    }

    public abstract WeatherDAO weatherDAO();

}
