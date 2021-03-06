package com.example.lenovo.examdemo.Activity;

import android.content.Intent;
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
import com.example.lenovo.examdemo.Bean.BaseObserver;
import com.example.lenovo.examdemo.Bean.ProgressListener;
import com.example.lenovo.examdemo.Bean.ResponseBean;
import com.example.lenovo.examdemo.Bean.ResponseCallBack;
import com.example.lenovo.examdemo.Bean.TimeBean;
import com.example.lenovo.examdemo.R;
import com.example.lenovo.examdemo.Utils.ConstantData;
import com.example.lenovo.examdemo.Utils.PublicStatic;
import com.example.lenovo.examdemo.Utils.RetrofitManager;

import java.util.Timer;
import java.util.TimerTask;

import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class WaitingActivity extends AppCompatActivity {

    private TextView text,duration;
    private String examName = "";
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

        duration = (TextView)findViewById(R.id.duration);
        text = (TextView)findViewById(R.id.text);
        Drawable drawable1 = getBaseContext().getResources().getDrawable(
                R.drawable.ic_practice_time);
        duration.setCompoundDrawables(drawable1, null, null, null);

        apiService = RetrofitManager.getInstance().getRetrofit();

        Intent intent = getIntent();
        examName = intent.getStringExtra("exam");
        Time(examName, ConstantData.stuid);
    }

//    @Override
//    public void onResume() {
//        super.onResume();  // Always call the superclass method first
//        Time("2015157061");
//        }



    //获取倒计时接口
    public void Time(final String examName, String stuId) {

        //通过gson转换成json字符串,调用login接口请求
        apiService.getTime(examName,stuId).
                subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new BaseObserver<ResponseBean<TimeBean>>(new ResponseCallBack<TimeBean>() {
                    @Override
                    public void onSuccess(TimeBean timeBean) {

                        if (timeBean.getSubmit()) {
                            Toast.makeText(WaitingActivity.this, "您已提交试卷", Toast.LENGTH_LONG).show();
                        } else {
                            int time = ((int) timeBean.getDuring()) / 60;
                            if (time > 0) {
//                            duration.setText(time+"");
                                minute = time;
                                Message msg = new Message();
                                msg.what = 1;
                                handlerStopTime.sendMessage(msg);
                            } else if (time <= 0 && time > -90) {
                                Toast.makeText(WaitingActivity.this, "考试已开始", Toast.LENGTH_LONG).show();
                                Intent intent = new Intent(WaitingActivity.this, AnalogyExaminationActivity.class);
                                intent.putExtra("exam_name",examName);
                                //   intent.putExtra("token", token);
                                startActivity(intent);
                            } else if (time <= -90) {
//                            department.setVisibility(View.GONE);
                                duration.setVisibility(View.GONE);
                                text.setText("考试已结束");
                                Toast.makeText(WaitingActivity.this, "考试已结束", Toast.LENGTH_LONG).show();
                            }
                        }
                    }

                    @Override
                    public void onLogin() {
                        Intent intent = new Intent(WaitingActivity.this, LoginActivity.class);
                        startActivity(intent);
                        Toast.makeText(getApplicationContext(), "请重新登录", Toast.LENGTH_LONG).show();
                    }
                    @Override
                    public void onFault(String errorMsg) {
                        Toast.makeText(getApplicationContext(), errorMsg, Toast.LENGTH_LONG).show();

                    }}, new ProgressListener() {
                    @Override
                    public void startProgress() {
                        Toast.makeText(getApplicationContext(), "获取倒计时中", Toast.LENGTH_LONG).show();
                    }

                    @Override
                    public void cancelProgress() {
                        // Toast.makeText(getApplicationContext(), "获取倒计时已取消", Toast.LENGTH_LONG).show();
                    }
                }));

    }
}
