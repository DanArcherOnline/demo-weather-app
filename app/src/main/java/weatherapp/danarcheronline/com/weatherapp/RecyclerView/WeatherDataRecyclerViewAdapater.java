package weatherapp.danarcheronline.com.weatherapp.RecyclerView;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import weatherapp.danarcheronline.com.weatherapp.R;

public class WeatherDataRecyclerViewAdapater extends RecyclerView.Adapter<WeatherDataRecyclerViewAdapater.WeatherDataViewHolder> {

    //    tag for debuggin purposes
    private static final String TAG = WeatherDataRecyclerViewAdapater.class.getSimpleName();

//    member variable to hold weather data
    String[] weatherDataArray;

    /**
     * Called when the recycler view instantiates a new view holder instance.
     * Creates a reusable view with an inflated layout.
     * @param viewGroup
     * @param viewType
     * @return a view holder for a single item, with the layout from weather_data_item.xml
     */
    @NonNull
    @Override
    public WeatherDataViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
        View weather_data_item_layout = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.weather_data_item, viewGroup, false);
        return new WeatherDataViewHolder(weather_data_item_layout);

    }

    /**
     * Called when the recycler view wants to insert data into a view holder.
     * Puts weather data strings into their corresponding views.
     * @param weatherDataViewHolder
     * @param adapterPosition
     */
    @Override
    public void onBindViewHolder(@NonNull WeatherDataViewHolder weatherDataViewHolder, int adapterPosition) {
        String weatherItemString = weatherDataArray[adapterPosition];
        weatherDataViewHolder.tv_weather_data_item_info.setText(weatherItemString);
    }

    /**
     * Used to tell the recycler view how many items to create.
     * @return number of items in the data source.
     */
    @Override
    public int getItemCount() {
        if(weatherDataArray == null) {
            return 0;
        }
        return weatherDataArray.length;
    }

    /**
     * The weather data view holder
     */
    public class WeatherDataViewHolder extends RecyclerView.ViewHolder {

//        view variable instantiations
        TextView tv_weather_data_item_info;

        /**
         * Constructor for the view holder.
         * Caches the necessary views within the view holder
         * @param itemView
         */
        public WeatherDataViewHolder(@NonNull View itemView) {
            super(itemView);

//            cache views
            tv_weather_data_item_info = itemView.findViewById(R.id.tv_weather_data_item_info);

        }
    }

    /**
     * Set the weather data and notify the recycler view that changes have been made to the adapter
     * @param weatherDataArray
     */
    public void setWeatherDataArray(String[] weatherDataArray) {
        this.weatherDataArray = weatherDataArray;
//        let the recycler view know that the data has changed and ti should reflect these updates
        notifyDataSetChanged();
    }
}
