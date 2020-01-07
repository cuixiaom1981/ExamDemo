package com.example.lenovo.examdemo.Activity;

import android.content.Intent;
import android.support.design.widget.Snackbar;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.lenovo.examdemo.Api.RequestApi;
import com.example.lenovo.examdemo.Bean.BaseObserver;
import com.example.lenovo.examdemo.Bean.GoLoginBean;
import com.example.lenovo.examdemo.Bean.ProgressListener;
import com.example.lenovo.examdemo.Bean.ResponseBean;
import com.example.lenovo.examdemo.Bean.ResponseCallBack;
import com.example.lenovo.examdemo.Bean.TokenBean;
import com.example.lenovo.examdemo.R;
import com.example.lenovo.examdemo.Utils.ConstantData;
import com.example.lenovo.examdemo.Utils.RetrofitManager;
import com.google.gson.Gson;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

public class LoginPasswordActivity extends AppCompatActivity {
    private EditText et_num, et_password;
    private TextView tv_login_sms;
    private ImageView iv_eye;
    private Button bt_login;
    private String telRegex = "[1][34578]\\d{9}";// 手机号正则表达式
    private String pasRegex = "^[a-zA-Z0-9]{6,16}$";// 密码正则表达式
    private int i = 0;
    private RequestApi apiService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_password);

        initToolbar();
        initView();
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
                    login(GoLoginBean.passLoginBean(phone, password));
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
        tv_login_sms = (TextView) findViewById(R.id.tv_login_sms);
        tv_login_sms.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginPasswordActivity.this, LoginActivity.class);
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
        apiService = RetrofitManager.getInstance().getRetrofit();
        Gson gson = new Gson();
        //通过gson转换成json字符串,调用login接口请求
        apiService.login(gson.toJson(user)).
                subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new BaseObserver<ResponseBean<TokenBean>>(new ResponseCallBack<ResponseBean<TokenBean>>() {
                    @Override
                    public void onSuccess(ResponseBean<TokenBean> resResult) {
                        if (resResult.getResult() != 0) {
                            Toast.makeText(LoginPasswordActivity.this, resResult.getErrMsg() + "", Toast.LENGTH_LONG).show();
                        } else {
                            ConstantData.token = resResult.getData().getToken();
                            ConstantData.stuid = resResult.getData().getStuId();
                            Intent intent = new Intent(LoginPasswordActivity.this, LocalActivity.class);
                            startActivity(intent);
                        }
                    }

                    @Override
                    public void onLogin() {
                        Intent intent = new Intent(LoginPasswordActivity.this, LoginActivity.class);
                        startActivity(intent);
                        Toast.makeText(getApplicationContext(), "请重新登录", Toast.LENGTH_LONG).show();
                    }

                    @Override
                    public void onFault(String errorMsg) {
                        Toast.makeText(getApplicationContext(), errorMsg, Toast.LENGTH_LONG).show();

                    }
                }, new ProgressListener() {
                    @Override
                    public void startProgress() {
                        Toast.makeText(getApplicationContext(), "登录中", Toast.LENGTH_LONG).show();
                    }

                    @Override
                    public void cancelProgress() {
                        // Toast.makeText(getApplicationContext(), "获取倒计时已取消", Toast.LENGTH_LONG).show();
                    }
                }));
    }
}