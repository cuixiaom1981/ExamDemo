package com.example.lenovo.examdemo.Activity;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.example.lenovo.examdemo.Api.RequestApi;
import com.example.lenovo.examdemo.Bean.GoTimeBean;
import com.example.lenovo.examdemo.Bean.RequestAnswerBean;
import com.example.lenovo.examdemo.Bean.ResponseBean;
import com.example.lenovo.examdemo.Bean.TimeBean;
import com.example.lenovo.examdemo.R;
import com.example.lenovo.examdemo.Utils.ConstantData;
import com.example.lenovo.examdemo.Utils.PublicStatic;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.TimeUnit;

import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

public class WaitingActivity extends AppCompatActivity {

    private TextView department,duration;
    private String id = "";
    private String token = "";
    //服务器base地址
    public String BASE_URL = PublicStatic.SERVICE_HOST.concat(PublicStatic.API_URL);
    private RequestApi apiService;
    private int minute;
    int second = 0;
    int isFirst;
    Timer timer;
    TimerTask timerTask;

    Handler handlerTime = new Handler() {
        public void handleMessage(Message msg) {
            // 判断时间快到前5分钟字体颜色改变
            if (minute == 0) {
                if (second == 0) {
                    isFirst+=1;
                    // 时间到
                    if(isFirst==1){
                        Intent intent = new Intent(WaitingActivity.this,AnalogyExaminationActivity.class);
                        intent.putExtra("id",id);
                        intent.putExtra("token",token);
                        startActivity(intent);
                    }
                    duration.setText("00:00");
                    if (timer != null) {
                        timer.cancel();
                        timer = null;
                    }
                    if (timerTask != null) {
                        timerTask = null;
                    }
                } else {
                    second--;
                    if (second >= 10) {
                        duration.setText("0" + minute + ":" + second);
                    } else {
                        duration.setText("0" + minute + ":0" + second);
                    }
                }
            } else {
                if (second == 0) {
                    second = 59;
                    minute--;
                    if (minute >= 10) {
                        duration.setText(minute + ":" + second);
                    } else {
                        duration.setText("0" + minute + ":" + second);
                    }
                } else {
                    second--;
                    if (second >= 10) {
                        if (minute >= 10) {
                            duration.setText(minute + ":" + second);
                        } else {
                            duration.setText("0" + minute + ":" + second);
                        }
                    } else {
                        if (minute >= 10) {
                            duration.setText(minute + ":0" + second);
                        } else {
                            duration.setText("0" + minute + ":0" + second);
                        }
                    }
                }
            }
        };
    };

    private Handler handlerStopTime = new Handler() {

        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 0:
                    stopTime();
                    break;
                case 1:
                    startTime();
                    break;
                default:
                    break;
            }
            super.handleMessage(msg);
        }
    };

    private void startTime() {
        if (timer == null) {
            timer = new Timer();
        }
        if (timerTask == null) {
            timerTask = new TimerTask() {

                @Override
                public void run() {
                    Message msg = new Message();
                    msg.what = 0;
                    handlerTime.sendMessage(msg);
                }
            };
        }
        if (timer != null && timerTask != null) {
            timer.schedule(timerTask, 0, 1000);
        }
    }

    private void stopTime(){
        if(timer!=null){
            timer.cancel();
            timer=null;
        }
        if(timerTask!=null){
            timerTask.cancel();
            timerTask=null;
        }
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_waiting);

//        department = (TextView)findViewById(R.id.department);
        duration = (TextView)findViewById(R.id.duration);
        Drawable drawable1 = getBaseContext().getResources().getDrawable(
                R.drawable.ic_practice_time);
        duration.setCompoundDrawables(drawable1, null, null, null);

        apiService = initRetrofit1().create(RequestApi.class);

        Intent intent = getIntent();
        id = intent.getStringExtra("id");
        token = intent.getStringExtra("token");

        Time(GoTimeBean.getTimeBean(id));

    }

//    @Override
//    public void onResume() {
//        super.onResume();  // Always call the superclass method first
//        Time(GoTimeBean.getTimeBean("2015157061"));
//        }

        //初始化retrofit 集成rxjava
    public  Retrofit initRetrofit1(){
        //http设置，可添加拦截器是实现http参数统一配置
        OkHttpClient.Builder builder = new OkHttpClient.Builder();
        builder.connectTimeout(3000, TimeUnit.SECONDS);//连接 超时时间
        builder.writeTimeout(3000,TimeUnit.SECONDS);//写操作 超时时间
        builder.readTimeout(3000,TimeUnit.SECONDS);//读操作 超时时间
        builder.retryOnConnectionFailure(true);//错误重连

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .client(builder.build())
                //添加rxjava支持
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        return retrofit;
    }

    //获取倒计时接口
    public void Time(GoTimeBean time) {
        Gson gson = new Gson();
        //通过gson转换成json字符串,调用login接口请求
        apiService.getTime(gson.toJson(time)).
                subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<ResponseBean<TimeBean>>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                    }
                    @Override
                    public void onNext(ResponseBean<TimeBean> resResult) {
                        if (resResult.getResult()!=0){
                            Toast.makeText(WaitingActivity.this, resResult.getErrMsg()+"", Toast.LENGTH_LONG).show();
                        }else {
                            if (resResult.getData().getSubmit()) {
                                Toast.makeText(WaitingActivity.this, "您已提交试卷", Toast.LENGTH_LONG).show();
                            }else {
                                int time = ((int) resResult.getData().getDuring()) / 60;
                                if (time > 0) {
//                            duration.setText(time+"");
                                    minute = time;
//                            for (int i = 0;i<resResult.getData().getDepartment().size();i++){
//                                title = title+resResult.getData().getDepartment().get(i)+" ";
//                            }
//                            department.setText("考试");
                                    Message msg = new Message();
                                    msg.what = 1;
                                    handlerStopTime.sendMessage(msg);
                                } else if (time <= 0 && time > -60) {
                                    Toast.makeText(WaitingActivity.this, "考试已开始", Toast.LENGTH_LONG).show();
                                    Intent intent = new Intent(WaitingActivity.this, AnalogyExaminationActivity.class);
                                    intent.putExtra("id", id);
                                    intent.putExtra("token", token);
                                    startActivity(intent);
                                } else if (time <= -90) {
//                            department.setVisibility(View.GONE);
                                    duration.setText("0");
                                    Toast.makeText(WaitingActivity.this, "考试已结束", Toast.LENGTH_LONG).show();
                                }
                            }
                        }
                    }
                    @Override
                    public void onError(Throwable e) {
                        Log.e("login", "onFailure: " + e.getMessage());
                        Toast.makeText(WaitingActivity.this, "接口连接失败", Toast.LENGTH_LONG).show();
                    }

                    @Override
                    public void onComplete() {
                    }

                });
    }
}
