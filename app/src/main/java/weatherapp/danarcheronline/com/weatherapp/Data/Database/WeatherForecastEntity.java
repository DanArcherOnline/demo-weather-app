package weatherapp.danarcheronline.com.weatherapp.Data.Database;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Ignore;
import android.arch.persistence.room.PrimaryKey;

@Entity(tableName = "weather")
public class WeatherForecastEntity {

    @PrimaryKey(autoGenerate = true)
    public int id;
    public String weatherDescription;
    public String highTemperature;
    public String lowTemperature;

    @Ignore
    public WeatherForecastEntity(String weatherDescription, String highTemperature, String lowTemperature) {
        this.weatherDescription = weatherDescription;
        this.highTemperature = highTemperature;
        this.lowTemperature = lowTemperature;
    }

    public WeatherForecastEntity(int id, String weatherDescription, String highTemperature, String lowTemperature) {
        this.id = id;
        this.weatherDescription = weatherDescription;
        this.highTemperature = highTemperature;
        this.lowTemperature = lowTemperature;
    }

    public String getWeatherDescription() {
        return weatherDescription;
    }

    public void setWeatherDescription(String weatherDescription) {
        this.weatherDescription = weatherDescription;
    }

    public String getHighTemperature() {
        return highTemperature;
    }

    public void setHighTemperature(String highTemperature) {
        this.highTemperature = highTemperature;
    }

    public String getLowTemperature() {
        return lowTemperature;
    }

    public void setLowTemperature(String lowTemperature) {
        this.lowTemperature = lowTemperature;
    }
}
