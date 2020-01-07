package com.example.lenovo.examdemo.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Message;
import android.support.design.widget.Snackbar;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;


import com.example.lenovo.examdemo.Api.RequestApi;
import com.example.lenovo.examdemo.Bean.BaseObserver;
import com.example.lenovo.examdemo.Bean.GoLoginBean;
import com.example.lenovo.examdemo.Bean.GoTimeBean;
import com.example.lenovo.examdemo.Bean.ProgressListener;
import com.example.lenovo.examdemo.Bean.ResResult;
import com.example.lenovo.examdemo.Bean.ResponseBean;
import com.example.lenovo.examdemo.Bean.ResponseCallBack;
import com.example.lenovo.examdemo.Bean.TimeBean;
import com.example.lenovo.examdemo.Bean.TokenBean;
import com.example.lenovo.examdemo.R;
import com.example.lenovo.examdemo.Utils.ConstantData;
import com.example.lenovo.examdemo.Utils.RetrofitManager;
import com.example.lenovo.examdemo.Utils.ShapeLoadingDialog;
import com.google.gson.Gson;


import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginActivity extends AppCompatActivity {

    public static EditText et_num, et_password;
    private TextView tv_getnum,login_type;
    private Button btn_login;
    private CountDownTimer mTimer;
    private Boolean TimerFlag = false;
    private ShapeLoadingDialog shapeLoadingDialog;// 网络请求弹出窗
    private String telRegex = "[1][34578]\\d{9}";// 手机号正则表达式
    public static LoginActivity loginActivity;
    private RequestApi apiService;

    private String phone = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        apiService = RetrofitManager.getInstance().getRetrofit();
        loginActivity = LoginActivity.this;
        initToolbar();// 初始化标题栏
        initView();//控件初始化

    }

    // 初始化标题栏
    private void initToolbar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);// 初始化标题栏
        toolbar.setTitle("");
        TextView textView = (TextView) findViewById(R.id.toolbar_title);
        textView.setText("短信登录/注册");
        toolbar.setBackgroundResource(R.color.btnbackcolor);// 设置标题栏颜色
        this.setSupportActionBar(toolbar);// 添加标题栏
        this.getSupportActionBar().setDisplayHomeAsUpEnabled(true);// 设置返回键可点击
    }

    //控件初始化
    private void initView() {
        et_num = (EditText) findViewById(R.id.et_num);
        et_password = (EditText) findViewById(R.id.et_password);
        tv_getnum = (TextView) findViewById(R.id.tv_getnum);
        btn_login = (Button) findViewById(R.id.btn_login);
        login_type = (TextView) findViewById(R.id.login_type);


        MyClick myClick = new MyClick();
        tv_getnum.setOnClickListener(myClick);
        btn_login.setOnClickListener(myClick);
        login_type.setOnClickListener(myClick);
    }

    // 控件监听事件
    public class MyClick implements View.OnClickListener {

        @Override
        public void onClick(View view) {
            switch (view.getId()) {
                case R.id.tv_getnum: //获取验证码
                    if (!et_num.getText().toString().matches(telRegex)) {
                        Snackbar bar = Snackbar.make(findViewById(R.id.activity_login), "手机号格式不正确", Snackbar.LENGTH_SHORT);
                        View v1 = bar.getView();
                        v1.setBackgroundColor(ContextCompat.getColor(LoginActivity.this, R.color.snackbarcolor));
                        bar.show();
                    } else {
                        if (!TimerFlag) {
                            sendsms();// 调用获取验证码接口
                        }else if ((tv_getnum.getText().toString().equals("点击再次发送"))){
                            et_password.setText("");
                            mTimer = null;
                            sendsms();// 调用获取验证码接口
                        }
                    }
                    break;
                case R.id.btn_login: //登录
                    if (!et_num.getText().toString().matches(telRegex)) {
                        Snackbar bar = Snackbar.make(findViewById(R.id.activity_login), "手机号格式不正确", Snackbar.LENGTH_SHORT);
                        View v1 = bar.getView();
                        v1.setBackgroundColor(ContextCompat.getColor(LoginActivity.this, R.color.snackbarcolor));
                        bar.show();
                    } else if (et_password.getText().toString().equals("")) {
                        Snackbar bar = Snackbar.make(findViewById(R.id.activity_login), "请输入验证码", Snackbar.LENGTH_SHORT);
                        View v2 = bar.getView();
                        v2.setBackgroundColor(ContextCompat.getColor(LoginActivity.this, R.color.snackbarcolor));
                        bar.show();
                    } else {
                        String phone = et_num.getText().toString();
                        String code = et_password.getText().toString();
                        login(GoLoginBean.smsLoginBean(phone,code));
                    }
                    break;
                case R.id.login_type:
                    Intent intent = new Intent(LoginActivity.this,LoginPasswordActivity.class);
                    startActivity(intent);
                    break;
                default:
                    break;
            }
        }
    }

//倒计时服务
    private void Timer(){
        if (mTimer == null) {
            mTimer = new CountDownTimer((long) (60 * 1000), 1000) {

                @Override
                public void onTick(long millisUntilFinished) {
                    if (!LoginActivity.this.isFinishing()) {
                        int remainTime = (int) (millisUntilFinished / 1000L);
                        tv_getnum.setText(remainTime+"秒后重发");
                        if (remainTime==0){
                            tv_getnum.setText("点击再次发送");
                        }
                        TimerFlag = true;
                    }
                }

                @Override
                public void onFinish() {
                }
            };
            mTimer.start();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mTimer != null) {
            mTimer.cancel();
            mTimer = null;
        }
    }

    //发送验证码
    public void sendsms() {
        apiService.sendsms(et_num.getText().toString()).
                subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new BaseObserver<ResponseBean<TokenBean>>(new ResponseCallBack<TokenBean>() {
                    public void onSuccess(TokenBean t) {
                        Snackbar bar = Snackbar.make(findViewById(R.id.activity_login), "验证码已发送请注意查收", Snackbar.LENGTH_SHORT);
                        View v1 = bar.getView();
                        v1.setBackgroundColor(ContextCompat.getColor(LoginActivity.this, R.color.snackbarcolor));
                        bar.show();
                        Timer();
                    }
                    @Override
                    public void onLogin() {
                    }
                    @Override
                    public void onFault(String errorMsg) {
                        Toast.makeText(getApplicationContext(), errorMsg, Toast.LENGTH_LONG).show();

                    }}, new ProgressListener() {
                    @Override
                    public void startProgress() {
                        Toast.makeText(getApplicationContext(), "短信验证码发送中", Toast.LENGTH_LONG).show();
                    }
                    @Override
                    public void cancelProgress() {
                        // Toast.makeText(getApplicationContext(), "获取倒计时已取消", Toast.LENGTH_LONG).show();
                    }
                }));
    }

    //登录
//    public void login(GoLoginBean user) {
//        apiService = RetrofitManager.getInstance().getRetrofit();
//        Gson gson = new Gson();
//        //通过gson转换成json字符串,调用login接口请求
//        apiService.login(gson.toJson(user)).
//                subscribeOn(Schedulers.io())
//                .observeOn(AndroidSchedulers.mainThread())
//                .subscribe(new BaseObserver<ResponseBean<TokenBean>>(new ResponseCallBack<ResponseBean<TokenBean>>() {
//                    @Override
//                    public void onSuccess(ResponseBean<TokenBean> resResult) {
//                        if (resResult.getResult()!=0){
//                            Toast.makeText(LoginActivity.this, resResult.getErrMsg()+"", Toast.LENGTH_LONG).show();
//                        }else {
//                            if (resResult.getData().isNew()) {
//                                phone = et_num.getText().toString();
//                                Intent intent = new Intent(LoginActivity.this, MessageActivity.class);
//                                intent.putExtra("phone", phone);
//                                startActivity(intent);
//                            } else {
//                                ConstantData.token = resResult.getData().getToken();
//                                stuid = resResult.getData().getStuId();
//                                Intent intent = new Intent(LoginActivity.this,LocalActivity.class);
//                                intent.putExtra("stuid",stuid);
//                                startActivity(intent);
//                            }
//                        }
//
//                    }
//                    @Override
//                    public void onLogin() {
//                        Intent intent = new Intent(LoginActivity.this, LoginActivity.class);
//                        startActivity(intent);
//                        Toast.makeText(getApplicationContext(), "请重新登录", Toast.LENGTH_LONG).show();
//                    }
//                    @Override
//                    public void onFault(String errorMsg) {
//                        Toast.makeText(getApplicationContext(), errorMsg, Toast.LENGTH_LONG).show();
//
//                    }}, new ProgressListener() {
//                    @Override
//                    public void startProgress() {
//                        Toast.makeText(getApplicationContext(), "登录中", Toast.LENGTH_LONG).show();
//                    }
//
//                    @Override
//                    public void cancelProgress() {
//                        // Toast.makeText(getApplicationContext(), "获取倒计时已取消", Toast.LENGTH_LONG).show();
//                    }
//                }));
//    }

//    public void login(GoLoginBean user) {
//        Gson gson = new Gson();
//        //通过gson转换成json字符串,调用login接口请求
//        apiService.login(gson.toJson(user)).
//                subscribeOn(Schedulers.io())
//                .observeOn(AndroidSchedulers.mainThread())
//                .subscribe(new Observer<ResponseBean<TokenBean>>() {
//                    @Override
//                    public void onSubscribe(Disposable d) {
//                    }
//                    @Override
//                    public void onNext(ResponseBean<TokenBean> resResult) {
//                        if (resResult.getResult()!=0){
//                            Toast.makeText(LoginActivity.this, resResult.getErrMsg()+"", Toast.LENGTH_LONG).show();
//                        }else {
//                            if (resResult.getData().isNew()) {
//                                phone = et_num.getText().toString();
//                                Intent intent = new Intent(LoginActivity.this, MessageActivity.class);
//                                intent.putExtra("phone", phone);
//                                startActivity(intent);
//                            } else {
//                                ConstantData.token = resResult.getData().getToken();
//                                stuid = resResult.getData().getStuId();
//                                Intent intent = new Intent(LoginActivity.this, LocalActivity.class);
//                                intent.putExtra("id", stuid);
//                                startActivity(intent);
//                            }
//                        }
//
//                    }
//                    @Override
//                    public void onError(Throwable e) {
//                        Log.e("login", "onFailure: " + e.getMessage());
//                    }
//
//                    @Override
//                    public void onComplete() {
//                    }
//
//                });
//    }

    public void login(GoLoginBean user) {
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
                            Toast.makeText(LoginActivity.this, resResult.getErrMsg()+"", Toast.LENGTH_LONG).show();
                        }else {
                            if (resResult.getData().isNew()) {
                                phone = et_num.getText().toString();
                                Intent intent = new Intent(LoginActivity.this, MessageActivity.class);
                                intent.putExtra("phone", phone);
                                startActivity(intent);
                            } else {
                                ConstantData.token = resResult.getData().getToken();
                                ConstantData.stuid = resResult.getData().getStuId();
                                Intent intent = new Intent(LoginActivity.this, LocalActivity.class);
                                startActivity(intent);
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

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                LoginActivity.this.finish();
                break;
            default:
                break;
        }
        return super.onOptionsItemSelected(item);
    }
}
