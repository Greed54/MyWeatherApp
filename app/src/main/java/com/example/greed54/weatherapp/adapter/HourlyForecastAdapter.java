package com.example.greed54.weatherapp.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.greed54.weatherapp.R;
import com.example.greed54.weatherapp.common.Common;
import com.example.greed54.weatherapp.common.IconManager;
import com.example.greed54.weatherapp.model.HourlyForecastEntity;
import com.example.greed54.weatherapp.model.forecast.MyList;
import com.example.greed54.weatherapp.model.forecast.WeatherForecastResult;

import java.util.ArrayList;
import java.util.List;

public class HourlyForecastAdapter extends RecyclerView.Adapter<HourlyForecastAdapter.MyViewHolder> {

    private Context context;
    private WeatherForecastResult weatherForecastResult;
    private List<HourlyForecastEntity> hourlyForecast;

    public HourlyForecastAdapter(Context context, WeatherForecastResult weatherForecastResult) {
        this.context = context;
        this.weatherForecastResult = weatherForecastResult;

        hourlyForecast = new ArrayList<>();

        initHourlyForecast();
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(context).inflate(R.layout.item_hourly_forecast, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        holder.card_time_txt.setText(hourlyForecast.get(position).getDt());

        try {
            holder.img_hour_weather_card.setImageResource(IconManager.getIconIdByWeatherId(hourlyForecast.get(position).getId(),
                    hourlyForecast.get(position).getIcon(),
                    holder.getItemView().getResources()));

        } catch (NoSuchFieldException | IllegalAccessException e) {
            e.printStackTrace();
        }

        holder.card_temp_txt.setText(new StringBuilder(String.valueOf(hourlyForecast.get(position).getTemp())).append("°"));
    }

    @Override
    public int getItemCount() {
        return hourlyForecast.size();
    }

    private void initHourlyForecast(){
        boolean isFirstIteration = true;
        String currentHour = "";
        for (MyList weatherList : weatherForecastResult.getList()){
            if (isFirstIteration){
                hourlyForecast.add(new HourlyForecastEntity(weatherList.getWeather().get(0).getId(),
                        weatherList.getWeather().get(0).getIcon(),
                        weatherList.getMain().getTemp(),
                        "Сейчас"));
                currentHour = Common.convertUnixToHour(weatherList.getDt());
                isFirstIteration = false;
            } else {
                if (!currentHour.equals(Common.convertUnixToHour(weatherList.getDt()))) {
                    hourlyForecast.add(new HourlyForecastEntity(weatherList.getWeather().get(0).getId(),
                            weatherList.getWeather().get(0).getIcon(),
                            weatherList.getMain().getTemp(),
                            Common.convertUnixToHour(weatherList.getDt())));
                } else {
                    break;
                }
            }
        }
    }

    class MyViewHolder extends RecyclerView.ViewHolder{

        private TextView card_time_txt, card_temp_txt;
        private ImageView img_hour_weather_card;
        private View itemView;

        MyViewHolder(View itemView) {
            super(itemView);
            this.itemView = itemView;

            card_time_txt = (TextView)itemView.findViewById(R.id.card_time_txt);
            card_temp_txt = (TextView)itemView.findViewById(R.id.card_temp_txt);
            img_hour_weather_card = (ImageView)itemView.findViewById(R.id.img_hour_weather_card);
        }

        View getItemView() {
            return itemView;
        }
    }
}
