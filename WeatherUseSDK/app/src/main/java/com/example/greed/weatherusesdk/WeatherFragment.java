package com.example.greed.weatherusesdk;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.greed.weatherusesdk.controller.MyToast;

import org.w3c.dom.Text;

import java.util.List;

import am.widget.circleprogressbar.CircleProgressBar;
import interfaces.heweather.com.interfacesmodule.bean.air.now.AirNow;
import interfaces.heweather.com.interfacesmodule.bean.weather.forecast.Forecast;
import interfaces.heweather.com.interfacesmodule.bean.weather.lifestyle.Lifestyle;
import interfaces.heweather.com.interfacesmodule.bean.weather.now.Now;
import interfaces.heweather.com.interfacesmodule.view.HeWeather;

public class WeatherFragment extends Fragment {

    TextView titleCity;
    TextView degree;
    TextView weather_info;
    TextView textQlty;
    TextView textMain;
    ImageView weatheric;
    TextView windir;
    TextView windsc;
    TextView hum;
    TextView pop;
    TextView vis;
    LinearLayout aqilayout;
    LinearLayout weatherfragmentlayout;
    LinearLayout forecastLayout;
    LinearLayout lifestyleLayout;
    private Context mContext;
    private ImageButton searchButton;
    int flag = 0;
    String location;

    public static final String TAG = "WeatherFragment";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.mContext = getActivity();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_weather, container, false);
        Toolbar toolbar = (Toolbar) view.findViewById(R.id.toolbar);
        titleCity = (TextView) view.findViewById(R.id.title_city);
        degree = (TextView) view.findViewById(R.id.degree_text);
        weather_info = (TextView) view.findViewById(R.id.weather_info_text);
        textQlty = (TextView) view.findViewById(R.id.Qlty_text);
        textMain = (TextView) view.findViewById(R.id.main_text);
        windir = (TextView) view.findViewById(R.id.wind_dir_text);
        windsc = (TextView) view.findViewById(R.id.wind_sc_text);
        hum = (TextView) view.findViewById(R.id.hum_text);
        pop = (TextView) view.findViewById(R.id.pop_text);
        vis = (TextView) view.findViewById(R.id.vis_text);
        searchButton = (ImageButton) view.findViewById(R.id.search_Imagebutton);
        weatheric = (ImageView) view.findViewById(R.id.weather_icon);
        aqilayout = (LinearLayout) view.findViewById(R.id.layout_aqi);
        weatherfragmentlayout = (LinearLayout) view.findViewById(R.id.weather_fragment);
        forecastLayout = (LinearLayout) view.findViewById(R.id.forecast_layout);
        lifestyleLayout = (LinearLayout) view.findViewById(R.id.lifestyle_layout);
        return view;
    }

    public void onActivityCreated(Bundle saveInstanceState) {
        super.onActivityCreated(saveInstanceState);
        //读取保存的地址信息
        final SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(mContext);
        location = sharedPreferences.getString("location", null);
        if (location != null) {
            getWeather(location);
        }
        //切换地址按钮点击事件
        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
                builder.setIcon(R.drawable.ic_dialog);
                builder.setTitle("切换城市");
                View view = LayoutInflater.from(mContext).inflate(R.layout.dialog, null);
                builder.setView(view);

                final EditText location_edittext = (EditText) view.findViewById(R.id.location_input);
                builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        location = location_edittext.getText().toString().trim();
                        if (location.equals("")) {
                            dialog.dismiss();
                        } else {
                            getWeather(location);
                            SharedPreferences.Editor editor = PreferenceManager.getDefaultSharedPreferences(mContext).edit();
                            editor.putString("location", location);
                            editor.apply();
                            Toast.makeText(mContext, "正在切换城市", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
                builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                    }
                });
                builder.show();
            }
        });
        //aqi点击事件
        aqilayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
                View view = LayoutInflater.from(mContext).inflate(R.layout.aqi_dialog, null);
                final TextView pm25 = (TextView) view.findViewById(R.id.pm25_d);
                final TextView pm10 = (TextView) view.findViewById(R.id.pm10_d);
                final TextView so2 = (TextView) view.findViewById(R.id.so2_d);
                final TextView no2 = (TextView) view.findViewById(R.id.no2_d);
                final TextView o3 = (TextView) view.findViewById(R.id.o3_d);
                final TextView co = (TextView) view.findViewById(R.id.co_d);
                final TextView airtext = (TextView) view.findViewById(R.id.air_text);
                final TextView qlty = (TextView) view.findViewById(R.id.qlty_dialog_text);
                final CircleProgressBar aqi_progressbar = (CircleProgressBar) view.findViewById(R.id.aqi_progressbar);
                pm25.setText(sharedPreferences.getString("Pm2.5", null));
                pm10.setText(sharedPreferences.getString("Pm10", null));
                so2.setText(sharedPreferences.getString("So2", null));
                no2.setText(sharedPreferences.getString("No2", null));
                o3.setText(sharedPreferences.getString("O3", null));
                co.setText(sharedPreferences.getString("Co", null));
                qlty.setText(sharedPreferences.getString("Qlty", null));
                airtext.setText(sharedPreferences.getString("airtxt", "暂无提醒"));
                aqi_progressbar.setProgress(Integer.parseInt(sharedPreferences.getString("Aqi", "0")));
                builder.setView(view);
                Toast.makeText(mContext, "查看空气质量详情", Toast.LENGTH_SHORT).show();
                builder.show();
            }
        });
    }

    public void getWeather(String location) {
        //获取实况天气
        HeWeather.getWeatherNow(mContext, location,
                new HeWeather.OnResultWeatherNowBeanListener() {
                    @Override
                    public void onError(Throwable e) {
                        String msg = e.getLocalizedMessage();
                        Toast.makeText(mContext, msg, Toast.LENGTH_LONG).show();
                    }

                    @Override
                    public void onSuccess(List<Now> dataObject) {
                        String status = dataObject.get(0).getStatus();//接口状态
                        if (status.equals("ok")) {
                            String Tmp = dataObject.get(0).getNow().getTmp();//温度
                            degree.setText(Tmp + "°");
                        }
                    }
                });
        //获取三天天气-----认证为个人开发者可获取7天
        HeWeather.getWeatherForecast(mContext, location,
                new HeWeather.OnResultWeatherForecastBeanListener() {
                    @Override
                    public void onError(Throwable e) {
                        String msg = e.getLocalizedMessage();
                        Toast.makeText(mContext, msg, Toast.LENGTH_LONG).show();
                    }

                    @Override
                    public void onSuccess(List<Forecast> dataObject) {
                        forecastLayout.removeAllViews();
                        String status = dataObject.get(0).getStatus();//接口状态
                        String cityname = dataObject.get(0).getBasic().getLocation();
                        if (status.equals("ok")) {
                            List ForecastBase = dataObject.get(0).getDaily_forecast();
                            String[] Date = new String[3];//日期
                            String[] Tmp_max = new String[3];
                            String[] Tmp_min = new String[3];
                            String[] Cond_code_d = new String[3];//天气状况代码
                            String[] Cond_txt_d = new String[3];//天气状况
                            String[] Wind_dir = new String[3];//风向
                            String[] Wind_sc = new String[3];//风力
                            String[] Hum = new String[3];//相对湿度
                            String[] Pop = new String[3];//降水概率
                            String[] Vis = new String[3];//能见度

                            Cond_code_d[0] = dataObject.get(0).getDaily_forecast().get(0).getCond_code_d();
                            Wind_dir[0] = dataObject.get(0).getDaily_forecast().get(0).getWind_dir();
                            Wind_sc[0] = dataObject.get(0).getDaily_forecast().get(0).getWind_sc();
                            Hum[0] = dataObject.get(0).getDaily_forecast().get(0).getHum();
                            Pop[0] = dataObject.get(0).getDaily_forecast().get(0).getPop();
                            Vis[0] = dataObject.get(0).getDaily_forecast().get(0).getVis();
                            for (int i = 0; i < ForecastBase.size(); i++) {
                                Date[i] = dataObject.get(0).getDaily_forecast().get(i).getDate();
                                Tmp_max[i] = dataObject.get(0).getDaily_forecast().get(i).getTmp_max();
                                Tmp_min[i] = dataObject.get(0).getDaily_forecast().get(i).getTmp_min();
                                Cond_txt_d[i] = dataObject.get(0).getDaily_forecast().get(i).getCond_txt_d();

                                View view = LayoutInflater.from(mContext).inflate(R.layout.forecast_item,
                                        forecastLayout, false);
                                TextView dateText = (TextView) view.findViewById(R.id.date_text);
                                TextView infoText = (TextView) view.findViewById(R.id.info_text);
                                TextView maxText = (TextView) view.findViewById(R.id.max_text);
                                TextView minText = (TextView) view.findViewById(R.id.min_text);
                                dateText.setText(Date[i]);
                                infoText.setText(Cond_txt_d[i]);
                                maxText.setText(Tmp_min[i] + "°/");
                                minText.setText(Tmp_max[i] + "°");
                                forecastLayout.addView(view);
                            }
                            titleCity.setText(cityname);
                            weather_info.setText(Cond_txt_d[0]);
                            String URL = "https://cdn.heweather.com/cond_icon/" + Cond_code_d[0] + ".png";
                            Glide.with(mContext).load(URL).override(300, 300).into(weatheric);
                            windir.setText(Wind_dir[0]);
                            windsc.setText(Wind_sc[0] + "级");
                            hum.setText(Hum[0] + "%");
                            pop.setText(Pop[0] + "%");
                            vis.setText(Vis[0] + "km");
                        }
                    }
                });
        //获取天气指数
        HeWeather.getWeatherLifeStyle(mContext, location,
                new HeWeather.OnResultWeatherLifeStyleBeanListener() {
                    @Override
                    public void onError(Throwable e) {
                        String msg = e.getLocalizedMessage();
                        Toast.makeText(mContext, msg, Toast.LENGTH_LONG).show();
                    }

                    @Override
                    public void onSuccess(List<Lifestyle> dataObject) {
                        lifestyleLayout.removeAllViews();
                        String status = dataObject.get(0).getStatus();//接口状态
                        if (status.equals("ok")) {
                            List LifestyleBase = dataObject.get(0).getLifestyle();
                            String[] Type = new String[8];
                            String[] Brf = new String[8];
                            final String[] Txt = new String[8];

                            for (int i = 0; i < LifestyleBase.size(); i++) {
                                Type[i] = dataObject.get(0).getLifestyle().get(i).getType();
                                Brf[i] = dataObject.get(0).getLifestyle().get(i).getBrf();
                                Txt[i] = dataObject.get(0).getLifestyle().get(i).getTxt();

                                View view = LayoutInflater.from(mContext).inflate(R.layout.lifestyle_item,
                                        lifestyleLayout, false);
                                ImageButton life = (ImageButton) view.findViewById(R.id.lifestyleic);
                                TextView BrfText = (TextView) view.findViewById(R.id.lifestylebrf);
                                if (Type[i].equals("cw")) {
                                    life.setImageResource(R.drawable.ic_carwash);
                                    final String message = Txt[i];
                                    life.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {
                                            MyToast.makeText(mContext, message, Toast.LENGTH_LONG).show();
                                        }
                                    });
                                    BrfText.setText(Brf[i]);
                                    lifestyleLayout.addView(view);
                                } else if (Type[i].equals("flu")) {
                                    life.setImageResource(R.drawable.ic_flu);
                                    final String message = Txt[i];
                                    life.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {
                                            MyToast.makeText(mContext, message, Toast.LENGTH_LONG).show();
                                        }
                                    });
                                    BrfText.setText(Brf[i]);
                                    lifestyleLayout.addView(view);
                                } else if (Type[i].equals("sport")) {
                                    life.setImageResource(R.drawable.ic_sport);
                                    final String message = Txt[i];
                                    life.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {
                                            MyToast.makeText(mContext, message, Toast.LENGTH_LONG).show();
                                        }
                                    });
                                    BrfText.setText(Brf[i]);
                                    lifestyleLayout.addView(view);
                                } else if (Type[i].equals("drsg")) {
                                    life.setImageResource(R.drawable.ic_clothes);
                                    final String message = Txt[i];
                                    life.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {
                                            MyToast.makeText(mContext, message, Toast.LENGTH_LONG).show();
                                        }
                                    });
                                    BrfText.setText(Brf[i]);
                                    lifestyleLayout.addView(view);
                                } else if (Type[i].equals("air")) {
                                    final String message = Txt[i];
                                    SharedPreferences.Editor editor = PreferenceManager.getDefaultSharedPreferences(mContext).edit();
                                    editor.putString("airtxt", message);
                                    editor.apply();
                                }
                            }
                        }
                    }
                });
        //获取空气质量实况
        HeWeather.getAirNow(mContext, location,
                new HeWeather.OnResultAirNowBeansListener() {
                    @Override
                    public void onError(Throwable e) {
                        String msg = e.getLocalizedMessage();
                        Toast.makeText(mContext, msg, Toast.LENGTH_LONG).show();
                    }

                    @Override
                    public void onSuccess(List<AirNow> dataObject) {
                        //Log.i("Log-AirNow", "onSuccess: " + Gson().toJson(dataObject));
                        String status = dataObject.get(0).getStatus();//接口状态
                        if (status.equals("ok")) {
                            String Aqi = dataObject.get(0).getAir_now_city().getAqi();//aqi指数
                            String Co = dataObject.get(0).getAir_now_city().getCo();//aqi指数
                            String Main = dataObject.get(0).getAir_now_city().getMain();//主要污染物
                            String No2 = dataObject.get(0).getAir_now_city().getNo2();//no2指数
                            String O3 = dataObject.get(0).getAir_now_city().getO3();//o3指数
                            String Pm10 = dataObject.get(0).getAir_now_city().getPm10();//pm10指数
                            String Pm25 = dataObject.get(0).getAir_now_city().getPm25();//pm2.5指数
                            String Qlty = dataObject.get(0).getAir_now_city().getQlty();//空气质量
                            String So2 = dataObject.get(0).getAir_now_city().getSo2();//so2指数

                            SharedPreferences.Editor editor = PreferenceManager.getDefaultSharedPreferences(mContext).edit();
                            editor.putString("Aqi", Aqi);
                            editor.putString("Co", Co);
                            editor.putString("No2", No2);
                            editor.putString("O3", O3);
                            editor.putString("Pm10", Pm10);
                            editor.putString("Pm2.5", Pm25);
                            editor.putString("Qlty", Qlty);
                            editor.putString("So2", So2);
                            editor.apply();

                            textQlty.setText(Qlty);
                            textMain.setText(Main);
                        }
                    }
                });
    }
}
