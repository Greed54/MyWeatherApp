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
import com.example.greed54.weatherapp.model.DailyForecastEntity;
import com.example.greed54.weatherapp.model.forecast.MyList;
import com.example.greed54.weatherapp.model.forecast.WeatherForecastResult;

import java.util.ArrayList;
import java.util.List;

public class DailyForecastAdapter extends RecyclerView.Adapter<DailyForecastAdapter.MyViewHolder> {

    private Context context;
    private WeatherForecastResult weatherForecastResult;
    private List<DailyForecastEntity> dailyForecast;

    public DailyForecastAdapter(Context context, WeatherForecastResult weatherForecastResult) {
        this.context = context;
        this.weatherForecastResult = weatherForecastResult;
        dailyForecast = new ArrayList<>();

        initDailyForecast();
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(context).inflate(R.layout.item_daily_forecast, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {

        try {
            holder.img_weather_card.setImageResource(IconManager.getIconIdByWeatherId(dailyForecast.get(position).getId(),
                    dailyForecast.get(position).getIcon(),
                    holder.getItemView().getResources()));

        } catch (NoSuchFieldException | IllegalAccessException e) {
            e.printStackTrace();
        }

        holder.card_day_temp_txt.setText(String.valueOf(dailyForecast.get(position).getTemp_day()));

        holder.card_night_temp_txt.setText(String.valueOf(dailyForecast.get(position).getTemp_night()));

        holder.card_date_time_txt.setText(Common.convertUnixToWeekDay(dailyForecast.get(position).getDt()));
    }

    @Override
    public int getItemCount() {
        return dailyForecast.size();
    }

    private void initDailyForecast(){
        int dayTemp = 0;
        int nightTemp = 0;
        for (MyList weatherList : weatherForecastResult.getList()){
            if (!Common.convertUnixToWeekDay(weatherList.getDt()).equals(Common.getCurrentWeekDay())){
                if (Common.convertUnixToHour(weatherList.getDt()).equals(Common.NIGHT_TIME)){
                    nightTemp = weatherList.getMain().getTemp();
                }
                if (Common.convertUnixToHour(weatherList.getDt()).equals(Common.DAY_TIME)){
                    dayTemp = weatherList.getMain().getTemp();
                    dailyForecast.add(new DailyForecastEntity(weatherList.getWeather().get(0).getId(),
                            weatherList.getWeather().get(0).getIcon(),
                            dayTemp,
                            nightTemp,
                            weatherList.getDt()));
                }
            }
        }
    }

    class MyViewHolder extends RecyclerView.ViewHolder{
        private TextView card_date_time_txt, card_day_temp_txt, card_night_temp_txt;
        private ImageView img_weather_card;
        private View itemView;

        MyViewHolder(View itemView) {
            super(itemView);
            this.itemView = itemView;

            card_date_time_txt = (TextView)itemView.findViewById(R.id.card_date_time_txt);
            card_day_temp_txt = (TextView)itemView.findViewById(R.id.card_day_temp_txt);
            card_night_temp_txt = (TextView)itemView.findViewById(R.id.card_night_temp_txt);
            img_weather_card = (ImageView)itemView.findViewById(R.id.img_weather_card);
        }

        View getItemView() {
            return itemView;
        }
    }


}
