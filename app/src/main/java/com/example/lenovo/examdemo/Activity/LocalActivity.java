package com.example.lenovo.examdemo.Activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.example.lenovo.examdemo.R;
import com.example.lenovo.examdemo.Utils.ConstantData;

public class LocalActivity extends AppCompatActivity {

    private LinearLayout exam_message,un_login;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_local);

        exam_message = (LinearLayout)findViewById(R.id.kaoshixinxi);
        un_login = (LinearLayout)findViewById(R.id.zhuxiaodenglu);

        MyClick myClick = new MyClick();
        exam_message.setOnClickListener(myClick);
        un_login.setOnClickListener(myClick);
    }
    public class MyClick implements View.OnClickListener {

        @Override
        public void onClick(View view) {
            switch (view.getId()) {
                case R.id.kaoshixinxi:
                    Intent intent = new Intent(LocalActivity.this,MainActivity.class);
                    startActivity(intent);
                    break;

                case R.id.zhuxiaodenglu:
                    ConstantData.token = "";
                    Toast.makeText(LocalActivity.this, "已注销", Toast.LENGTH_LONG).show();
                    Intent intent1 = new Intent(LocalActivity.this,LoginActivity.class);
                    startActivity(intent1);
                    break;
            }
        }
    }
}
