package com.example.lenovo.examdemo.Activity;

import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.lenovo.examdemo.Api.RequestApi;
import com.example.lenovo.examdemo.Bean.ResponseBean;
import com.example.lenovo.examdemo.Bean.ResponseGradeBean;
import com.example.lenovo.examdemo.R;
import com.example.lenovo.examdemo.Utils.ConstantData;
import com.example.lenovo.examdemo.Utils.RetrofitManager;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import io.reactivex.Observer;

public class MainActivity extends AppCompatActivity {
    private ListView listView1;
    private ListView listView2;
    private List<ResponseGradeBean> gradeBeans1 = new ArrayList<ResponseGradeBean>();
    private List<ResponseGradeBean> gradeBeans2 = new ArrayList<ResponseGradeBean>();
    private RequestApi apiService;
    private Context mContext;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initToolbar();
        apiService = RetrofitManager.getInstance().getRetrofit();
        listView1 = (ListView)findViewById(R.id.list1);
        listView2 = (ListView)findViewById(R.id.list2);

        mContext = this;
        ExamMessage(ConstantData.stuid);

        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                listView1.setAdapter(new ListAdapter(mContext,gradeBeans1));
                listView2.setAdapter(new ListAdapter(mContext,gradeBeans2));
            }
        }, 500);//

    }

    @Override
    protected void onResume() {
        // TODO Auto-generated method stub
//        ExamMessage(ConstantData.stuid);
        super.onResume();
    }

    // 初始化标题栏
    private void initToolbar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);// 初始化标题栏
        toolbar.setTitle("");
        TextView textView = (TextView) findViewById(R.id.toolbar_title);
        textView.setText("考试信息");
        toolbar.setBackgroundResource(R.color.btnbackcolor);// 设置标题栏颜色
        this.setSupportActionBar(toolbar);// 添加标题栏
        this.getSupportActionBar().setDisplayHomeAsUpEnabled(true);// 设置返回键可点击
    }


    public class ListAdapter extends BaseAdapter {

        private Context context;
        private List<ResponseGradeBean> list;


        public ListAdapter(Context context, List<ResponseGradeBean> list) {
            this.context = context;
            this.list = list;
        }

        @Override
        public int getCount() {
            return list.size();
        }

        @Override
        public Object getItem(int position) {
            return list.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @NonNull
        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
            ViewHolder viewHolder;

            if (convertView == null) {
                convertView = View.inflate(context,R.layout.list_item,null);
                viewHolder = new ViewHolder();
                viewHolder.name = (TextView) convertView.findViewById(R.id.name);
                viewHolder.time = (TextView) convertView.findViewById(R.id.time);
                viewHolder.exam = (Button)convertView.findViewById(R.id.exam);
                convertView.setTag(viewHolder);//保存
            } else {
                viewHolder = (ViewHolder) convertView.getTag();//取出
            }

            if (list.get(position).getIsUpload() == 0){
                viewHolder.name.setText(list.get(position).getExamName());
                viewHolder.time.setText(list.get(position).getExamTime());
                viewHolder.exam.setText("开始考试");
                viewHolder.exam.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(MainActivity.this,WaitingActivity.class);
                        intent.putExtra("exam",list.get(position).getExamName());
                        startActivity(intent);
                    }
                });
            }else {
                viewHolder.name.setText(list.get(position).getExamName());
                viewHolder.time.setText(list.get(position).getScore()+"分");
                viewHolder.exam.setVisibility(View.GONE);
            }

            return convertView;
        }

        private class ViewHolder {
            TextView name;
            TextView time;
            Button exam;
        }
    }
    //获取考试信息
    public void ExamMessage(String stuId) {
        apiService.getGrade(stuId).
                subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe( new Observer<ResponseBean<List<ResponseGradeBean>>>() {

                    @Override
                    public void onSubscribe(Disposable d) {
                    }
                    @Override
                    public void onNext(ResponseBean<List<ResponseGradeBean>> resResult) {
                        if (resResult.getResult()!=0){
                            Toast.makeText(MainActivity.this, resResult.getErrMsg()+"", Toast.LENGTH_LONG).show();
                        }else {
                            for(int i = 0; i < resResult.getData().size(); i++){
                                if (resResult.getData().get(i).getIsUpload() == 0){
                                    gradeBeans1.add(resResult.getData().get(i));
                                }else{
                                    gradeBeans2.add(resResult.getData().get(i));
                                }
                            }
                        }
                    }
                    @Override
                    public void onError(Throwable e) {
                        Log.e("login", "onFailure: " + e.getMessage());
                        Toast.makeText(MainActivity.this, "接口连接失败", Toast.LENGTH_LONG).show();
                    }

                    @Override
                    public void onComplete() {
                    }

                });
    }

}
