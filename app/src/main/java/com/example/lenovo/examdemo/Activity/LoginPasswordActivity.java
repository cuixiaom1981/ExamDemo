package com.example.lenovo.examdemo.Activity;

import android.content.Intent;
import android.support.design.widget.Snackbar;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.lenovo.examdemo.Api.RequestApi;
import com.example.lenovo.examdemo.Bean.GoLoginBean;
import com.example.lenovo.examdemo.Bean.GoTimeBean;
import com.example.lenovo.examdemo.Bean.ResponseBean;
import com.example.lenovo.examdemo.Bean.TimeBean;
import com.example.lenovo.examdemo.Bean.TokenBean;
import com.example.lenovo.examdemo.R;
import com.example.lenovo.examdemo.Utils.PublicStatic;
import com.google.gson.Gson;

import java.util.concurrent.TimeUnit;

import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

public class LoginPasswordActivity extends AppCompatActivity {
    private EditText et_num,et_password;
    private TextView tv_login_sms;
    private ImageView iv_eye;
    private Button bt_login;
    private String telRegex = "[1][34578]\\d{9}";// 手机号正则表达式
    private String pasRegex = "^[a-zA-Z0-9]{6,16}$";// 密码正则表达式
    private int i = 0;
    private String token = "";
    private String stuid = "";
    //服务器base地址
    public String BASE_URL = PublicStatic.SERVICE_HOST.concat(PublicStatic.API_URL);
    private RequestApi apiService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_password);

        initToolbar();
        initView();
    }

    //初始化retrofit 集成rxjava
    public Retrofit initRetrofit(){
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
    private void initView() {
        et_num = (EditText) findViewById(R.id.et_num);
        et_password = (EditText) findViewById(R.id.et_password);
        iv_eye = (ImageView) findViewById(R.id.iv_eye);
        iv_eye.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (i % 2 == 0) {
                    iv_eye.setImageResource(R.mipmap.login5);
                    et_password.setInputType(0x90);
                } else {
                    iv_eye.setImageResource(R.mipmap.login4);
                    et_password.setInputType(0x81);
                }
                i++;
            }
        });
        bt_login = (Button) findViewById(R.id.bt_login);
        bt_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!et_num.getText().toString().matches(telRegex) || !et_password.getText().toString().matches(pasRegex)) {
                    Snackbar bar = Snackbar.make(findViewById(R.id.activity_login_password), "手机号或密码格式不正确", Snackbar.LENGTH_SHORT);
                    View v1 = bar.getView();
                    v1.setBackgroundColor(ContextCompat.getColor(LoginPasswordActivity.this, R.color.snackbarcolor));
                    bar.show();
                } else {
                    String phone = et_num.getText().toString();
                    String password = et_password.getText().toString();
                    login(GoLoginBean.passLoginBean(phone,password));
                }

            }
        });
//        tv_forget = (TextView)findViewById(R.id.tv_forget);
//        tv_forget.setOnClickListener(new View.OnClickListener(){
//            @Override
//            public void onClick(View v) {
//                Toast.makeText(LoginPasswordActivity.this, "请使用验证码登录，完成后可在信息管理界面修改密码", Toast.LENGTH_LONG).show();
//            }
//        });
        tv_login_sms = (TextView)findViewById(R.id.tv_login_sms);
        tv_login_sms.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginPasswordActivity.this,LoginActivity.class);
                startActivity(intent);
            }
        });

    }

    // 初始化标题栏
    private void initToolbar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);// 初始化标题栏
        toolbar.setTitle("");
        TextView textView = (TextView) findViewById(R.id.toolbar_title);
        textView.setText("密码登录");
        toolbar.setBackgroundResource(R.color.circle);// 设置标题栏颜色
        this.setSupportActionBar(toolbar);// 添加标题栏
        this.getSupportActionBar().setDisplayHomeAsUpEnabled(true);// 设置返回键可点击
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                LoginPasswordActivity.this.finish();
                break;
            default:
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    public void login(GoLoginBean user) {
        apiService = initRetrofit().create(RequestApi.class);
        Gson gson = new Gson();
        //通过gson转换成json字符串,调用login接口请求
        apiService.login(gson.toJson(user)).
                subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<ResponseBean<TokenBean>>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                    }
                    @Override
                    public void onNext(ResponseBean<TokenBean> resResult) {
                        if (resResult.getResult()!=0){
                            Toast.makeText(LoginPasswordActivity.this, resResult.getErrMsg()+"", Toast.LENGTH_LONG).show();
                        }else {
//                            Toast.makeText(LoginPasswordActivity.this, resResult.getData().getToken(), Toast.LENGTH_LONG).show();
                            token = resResult.getData().getToken();
                            stuid = resResult.getData().getStuId();
                            Time(GoTimeBean.getTimeBean(stuid));
                        }
                    }
                    @Override
                    public void onError(Throwable e) {
                        Log.e("login", "onFailure: " + e.getMessage());
                    }

                    @Override
                    public void onComplete() {
                    }

                });
    }
    //获取倒计时接口
    public void Time(GoTimeBean time) {
        apiService = initRetrofit().create(RequestApi.class);
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
                            Toast.makeText(LoginPasswordActivity.this, resResult.getErrMsg()+"", Toast.LENGTH_LONG).show();
                        }else {
                            if (resResult.getData().getSubmit()) {
                                Toast.makeText(LoginPasswordActivity.this, "您已提交试卷", Toast.LENGTH_LONG).show();
                            } else {
                                int time = (int) resResult.getData().getDuring() / 60;
                                if (time > 0) {
                                    Intent intent = new Intent(LoginPasswordActivity.this, WaitingActivity.class);
                                    intent.putExtra("id", stuid);
                                    intent.putExtra("token", token);
                                    startActivity(intent);
                                } else if (time <= 0 && time > -60) {
                                    Intent intent = new Intent(LoginPasswordActivity.this, AnalogyExaminationActivity.class);
                                    intent.putExtra("id", stuid);
                                    intent.putExtra("token", token);
                                    startActivity(intent);
                                } else if (time < -90) {
                                    Toast.makeText(LoginPasswordActivity.this, "考试已结束", Toast.LENGTH_LONG).show();
                                }
                            }
                        }
                    }
                    @Override
                    public void onError(Throwable e) {
                        Log.e("login", "onFailure: " + e.getMessage());
                    }

                    @Override
                    public void onComplete() {
                    }

                });
    }
}
