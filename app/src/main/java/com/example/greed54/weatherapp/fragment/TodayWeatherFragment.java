package com.example.greed54.weatherapp.fragment;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.greed54.weatherapp.adapter.DailyForecastAdapter;
import com.example.greed54.weatherapp.adapter.HourlyForecastAdapter;
import com.example.greed54.weatherapp.common.Common;
import com.example.greed54.weatherapp.common.IconManager;
import com.example.greed54.weatherapp.model.forecast.WeatherForecastResult;
import com.example.greed54.weatherapp.model.forecast.WeatherResult;
import com.example.greed54.weatherapp.model.sugarEntities.PlaceEntity;
import com.example.greed54.weatherapp.R;
import com.example.greed54.weatherapp.Retrofit.IOpenWeatherMap;
import com.example.greed54.weatherapp.Retrofit.RetrofitClient;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;
import retrofit2.Retrofit;


public class TodayWeatherFragment extends Fragment {

    public static final String CURRENT_LOCATION_MODE = "1";
    public static final String OTHER_LOCATION_MODE = "2";

    private PlaceEntity place;
    private String mode;

    private ImageView imgWeather;
    private TextView txtCityName, txtHumidity, txtSunrise,
            txtSunset, txtPressure, txtTemperature,
            txtDescription, txtWind,
            txtDay, txtMaxTmp, txtMinTmp;
    private LinearLayout weatherPanel;
    private ProgressBar loading;
    private ScrollView daily_info;
    private RecyclerView hourly_forecast;
    private RecyclerView daily_forecast;

    private CompositeDisposable compositeDisposable;
    private IOpenWeatherMap mService;

    private static final int LAYOUT = R.layout.fragment_today_weather;

    public TodayWeatherFragment() {
        compositeDisposable = new CompositeDisposable();
        Retrofit retrofit = RetrofitClient.getInstance();
        mService = retrofit.create(IOpenWeatherMap.class);
    }

    public static TodayWeatherFragment getInstance(PlaceEntity place, String mode){
        Bundle args = new Bundle();
        TodayWeatherFragment fragment = new TodayWeatherFragment();
        fragment.place = place;
        fragment.mode = mode;
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View itemView = inflater.inflate(LAYOUT, container, false);

        imgWeather = (ImageView) itemView.findViewById(R.id.img_weather);
        txtCityName = (TextView) itemView.findViewById(R.id.txt_city_name);
        txtDay = (TextView) itemView.findViewById(R.id.txt_day);
        txtMaxTmp = (TextView) itemView.findViewById(R.id.txt_max_tmp);
        txtMinTmp = (TextView) itemView.findViewById(R.id.txt_min_tmp);
        txtHumidity = (TextView) itemView.findViewById(R.id.txt_humidity);
        txtSunrise = (TextView) itemView.findViewById(R.id.txt_sunrise);
        txtSunset = (TextView) itemView.findViewById(R.id.txt_sunset);
        txtPressure = (TextView) itemView.findViewById(R.id.txt_pressure);
        txtTemperature = (TextView) itemView.findViewById(R.id.txt_temperature);
        txtDescription = (TextView) itemView.findViewById(R.id.txt_description);
        txtWind = (TextView) itemView.findViewById(R.id.txt_wind);

        daily_info = (ScrollView) itemView.findViewById(R.id.daily_info);

        hourly_forecast = (RecyclerView) itemView.findViewById(R.id.hourly_forecast);
        hourly_forecast.setHasFixedSize(true);
        hourly_forecast.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));

        daily_forecast = (RecyclerView) itemView.findViewById(R.id.daily_forecast);
        daily_forecast.setHasFixedSize(true);
        daily_forecast.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false){
            @Override
            public boolean canScrollVertically() {
                return false;
            }
        });

        update();

        weatherPanel = (LinearLayout) itemView.findViewById(R.id.weather_panel);
        loading = (ProgressBar) itemView.findViewById(R.id.loading);

        return itemView;
    }

    private void getWeatherInformation(double latitude, double longitude) {
        compositeDisposable.add(mService.getWeatherByLatLng(String.valueOf(latitude),
                String.valueOf(longitude),
                Common.APP_ID,
                "metric",
                "ru")
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<WeatherResult>() {
                    @Override
                    public void accept(WeatherResult weatherResult) throws Exception {
                        if (isAdded() && getActivity() != null) {
                            imgWeather.setImageResource(IconManager.getIconIdByWeatherId(weatherResult.getWeather().get(0).getId(),
                                    weatherResult.getWeather().get(0).getIcon(),
                                    getResources()));


                            txtCityName.setText(weatherResult.getName());

                            txtDescription.setText(weatherResult.getWeather().get(0).getDescription());

                            txtTemperature.setText(new StringBuilder(String.valueOf(weatherResult.getMain().getTemp()))
                                    .append("°"));
                            txtDay.setText(Common.convertUnixToWeekDay(weatherResult.getDt()));
                            txtMaxTmp.setText(String.valueOf((int) weatherResult.getMain().getTemp_max()));
                            txtMinTmp.setText(String.valueOf((int) weatherResult.getMain().getTemp_min()));


                            txtPressure.setText(new StringBuilder(String.valueOf(weatherResult.getMain().getPressure()))
                                    .append(" гПа"));
                            txtHumidity.setText(new StringBuilder(String.valueOf(weatherResult.getMain().getHumidity()))
                                    .append(" %"));
                            txtSunrise.setText(Common.convertUnixToHourAndMinute(weatherResult.getSys().getSunrise()));
                            txtSunset.setText(Common.convertUnixToHourAndMinute(weatherResult.getSys().getSunset()));
                            txtWind.setText(new StringBuilder(String.valueOf(weatherResult.getWind().getSpeed())).append(" м/с"));

                            weatherPanel.setVisibility(View.VISIBLE);
                            loading.setVisibility(View.GONE);
                        }

                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        Toast.makeText(getActivity(), ""+throwable.getMessage(), Toast.LENGTH_LONG).show();
                        Log.e("Err", "" + throwable.getMessage());
                    }
                })
        );
    }

    private void getForecastWeatherInformation(double latitude, double longitude){
        compositeDisposable.add(mService.getForecastWeatherByLatLng(String.valueOf(latitude),
                String.valueOf(longitude),
                Common.APP_ID,
                "metric",
                "ru")
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<WeatherForecastResult>() {
                    @Override
                    public void accept(WeatherForecastResult weatherForecastResult) throws Exception {
                        HourlyForecastAdapter hourlyForecastAdapter = new HourlyForecastAdapter(getContext(), weatherForecastResult);
                        hourly_forecast.setAdapter(hourlyForecastAdapter);

                        DailyForecastAdapter adapter = new DailyForecastAdapter(getContext(), weatherForecastResult);
                        daily_forecast.setAdapter(adapter);
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        Toast.makeText(getActivity(), ""+throwable.getMessage(), Toast.LENGTH_LONG).show();
                        Log.e("Err", "" + throwable.getMessage());
                    }
                }));
    }

    public void update(){
        if (mode.equals(OTHER_LOCATION_MODE) && place != null){
            getWeatherInformation(place.getLatitude(), place.getLongitude());
            getForecastWeatherInformation(place.getLatitude(), place.getLongitude());
            Log.d("Coord", place.getLatitude() + " " + place.getLongitude());

        } else if (mode.equals(CURRENT_LOCATION_MODE)){
            getWeatherInformation(Common.CURRENT_LOCATION.getLatitude(), Common.CURRENT_LOCATION.getLongitude());
            getForecastWeatherInformation(Common.CURRENT_LOCATION.getLatitude(), Common.CURRENT_LOCATION.getLongitude());
        }
    }

    public PlaceEntity getPlace() {
        return place;
    }

    @NonNull
    @Override
    public String toString() {
        return "TodayWeatherFragment{" +
                "place=" + place.getName() +
                '}';
    }


}
