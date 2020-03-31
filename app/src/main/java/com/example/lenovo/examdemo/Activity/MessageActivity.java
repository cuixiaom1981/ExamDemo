package com.example.lenovo.examdemo.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.lenovo.examdemo.Api.RequestApi;
import com.example.lenovo.examdemo.Bean.GoRegistBean;
import com.example.lenovo.examdemo.Bean.ResponseBean;
import com.example.lenovo.examdemo.Bean.TokenBean;
import com.example.lenovo.examdemo.R;
import com.example.lenovo.examdemo.Utils.ConstantData;
import com.example.lenovo.examdemo.Utils.RetrofitManager;
import com.google.gson.Gson;


import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class MessageActivity extends AppCompatActivity {
    private EditText message_password,message_password1,message_id;
    private TextView message_user;
    private ImageView iv_eye, iv_eye1;
    private Button stu_mess;
    private int i = 0;
    private int j = 0;
    private String stuid;
    private String phone;
    private String telRegex = "[1][34578]\\d{9}";// 手机号正则表达式
    private RequestApi apiService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message);

        initView();
        initToolbar();
    }

    //控件初始化
    private void initView() {
        message_password = (EditText)findViewById(R.id.message_password);
        message_password1 = (EditText)findViewById(R.id.message_password1);
        message_user = (TextView) findViewById(R.id.message_user);
        message_id = (EditText) findViewById(R.id.message_id);
        iv_eye = (ImageView) findViewById(R.id.iv_eye);
        iv_eye1 = (ImageView) findViewById(R.id.iv_eye1);
        stu_mess = (Button)findViewById(R.id.stu_mess);

        MyClick myClick = new MyClick();
        iv_eye.setOnClickListener(myClick);
        iv_eye1.setOnClickListener(myClick);
        stu_mess.setOnClickListener(myClick);

        Intent intent = getIntent();
        phone = intent.getExtras().getString("phone");
        message_user.setText(phone);
        apiService = RetrofitManager.getInstance().getRetrofit();
    }


    public class MyClick implements View.OnClickListener {

        @Override
        public void onClick(View view) {
            switch (view.getId()) {
                case R.id.iv_eye:  //输入密码显示
                    if (i % 2 == 0) {
                        iv_eye.setImageResource(R.mipmap.login5);
                        message_password.setInputType(0x90);
                    } else {
                        iv_eye.setImageResource(R.mipmap.login4);
                        message_password.setInputType(0x81);
                    }
                    i++;
                    break;
                case R.id.iv_eye1:  //再次输入密码显示
                    if (j % 2 == 0) {
                        iv_eye1.setImageResource(R.mipmap.login5);
                        message_password1.setInputType(0x90);
                    } else {
                        iv_eye1.setImageResource(R.mipmap.login4);
                        message_password1.setInputType(0x81);
                    }
                    j++;
                    break;

                case R.id.stu_mess:  //注册
                    if (!message_user.getText().toString().matches(telRegex)) {
                        Snackbar bar = Snackbar.make(findViewById(R.id.activity_message), "手机号格式不正确", Snackbar.LENGTH_SHORT);
                        View v1 = bar.getView();
                        v1.setBackgroundColor(ContextCompat.getColor(MessageActivity.this, R.color.snackbarcolor));
                        bar.show();
                    } else if (message_password.getText().toString().equals("")||message_password1.getText().toString().equals("")) {
                        Snackbar bar = Snackbar.make(findViewById(R.id.activity_message), "请输入密码", Snackbar.LENGTH_SHORT);
                        View v2 = bar.getView();
                        v2.setBackgroundColor(ContextCompat.getColor(MessageActivity.this, R.color.snackbarcolor));
                        bar.show();
                    }
                    else if (!message_password.getText().toString().equals(message_password1.getText().toString())) {
                        Snackbar bar = Snackbar.make(findViewById(R.id.activity_message), "两次密码输入不一致", Snackbar.LENGTH_SHORT);
                        View v2 = bar.getView();
                        v2.setBackgroundColor(ContextCompat.getColor(MessageActivity.this, R.color.snackbarcolor));
                        bar.show();
                    }
                    else {
                        String phone = message_user.getText().toString();
                        String stuid = message_id.getText().toString();
                        String message_password = message_password1.getText().toString();
                        registe(GoRegistBean.registBean(phone,stuid,message_password));
                    }
                    break;
            }
        }
    }

    public void registe(GoRegistBean user) {
        Gson gson = new Gson();
        //通过gson转换成json字符串,调用login接口请求
        apiService.registe(gson.toJson(user)).
                subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<ResponseBean<TokenBean>>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                    }
                    @Override
                    public void onNext(ResponseBean<TokenBean> resResult) {
                        ConstantData.token = resResult.getData().getToken();
                        ConstantData.stuid = message_id.getText().toString();
                        Intent intent = new Intent(MessageActivity.this,MainActivity.class);
                        startActivity(intent);
                    }
                    @Override
                    public void onError(Throwable e) {
                        Log.e("login", "onFailure: " + e.getMessage());
                        Toast.makeText(MessageActivity.this, e.getMessage(), Toast.LENGTH_LONG).show();
                    }

                    @Override
                    public void onComplete() {
                    }

                });
    }

    //获取倒计时接口
//    public void Time(TimeBean time) {
//        Gson gson = new Gson();
//        //通过gson转换成json字符串,调用login接口请求
//        apiService.getTime(gson.toJson(time)).
//                subscribeOn(Schedulers.io())
//                .observeOn(AndroidSchedulers.mainThread())
//                .subscribe(new Observer<ResponseBean<TimeBean>>() {
//                    @Override
//                    public void onSubscribe(Disposable d) {
//                    }
//                    @Override
//                    public void onNext(ResponseBean<TimeBean> resResult) {
//                        if (resResult.getResult()!=0){
//                            Toast.makeText(MessageActivity.this, resResult.getErrMsg()+"", Toast.LENGTH_LONG).show();
//                        }else {
//                            int time = (int) resResult.getData().getDuring() / 60;
//                            if (time > 0) {
//                                Intent intent = new Intent(MessageActivity.this, WaitingActivity.class);
//                                intent.putExtra("id", stuid);
//                                intent.putExtra("token", token);
//                                startActivity(intent);
//                            } else if (time <= 0 && time > -60) {
//                                Toast.makeText(MessageActivity.this, "考试已开始", Toast.LENGTH_LONG).show();
//                                Intent intent = new Intent(MessageActivity.this, AnalogyExaminationActivity.class);
//                                intent.putExtra("id", stuid);
//                                intent.putExtra("token", token);
//                                startActivity(intent);
//                            } else if (time < -90) {
//                                Toast.makeText(MessageActivity.this, "考试已结束", Toast.LENGTH_LONG).show();
//                            }
//                        }
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

    // 初始化标题栏
    private void initToolbar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);// 初始化标题栏
        toolbar.setTitle("");
        TextView textView = (TextView) findViewById(R.id.toolbar_title);
        textView.setText("信息管理");
        toolbar.setBackgroundResource(R.color.btnbackcolor);// 设置标题栏颜色
        this.setSupportActionBar(toolbar);// 添加标题栏
        this.getSupportActionBar().setDisplayHomeAsUpEnabled(true);// 设置返回键可点击
    }
}
