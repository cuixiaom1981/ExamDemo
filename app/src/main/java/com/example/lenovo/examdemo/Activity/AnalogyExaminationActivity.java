package com.example.lenovo.examdemo.Activity;

import android.app.Activity;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnKeyListener;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.lenovo.examdemo.Adapter.ExaminationSubmitAdapter;
import com.example.lenovo.examdemo.Api.RequestApi;
import com.example.lenovo.examdemo.Bean.AnSwerInfo;
import com.example.lenovo.examdemo.Bean.RequestAnswerBean;
import com.example.lenovo.examdemo.Bean.ResponseBean;
import com.example.lenovo.examdemo.Bean.ResponseQuestionBean;
import com.example.lenovo.examdemo.Bean.StuBean;
import com.example.lenovo.examdemo.Bean.TokenBean;
import com.example.lenovo.examdemo.R;
import com.example.lenovo.examdemo.Utils.ConstantData;
import com.example.lenovo.examdemo.Utils.PublicStatic;
import com.example.lenovo.examdemo.Utils.ViewPagerScroller;
import com.example.lenovo.examdemo.View.VoteSubmitViewPager;
import com.google.gson.Gson;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.TimeUnit;

import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;


/**
 * 类比考试答题
 */
public class AnalogyExaminationActivity extends Activity {

	private ImageView leftIv;
	private TextView titleTv;
	private TextView right;
	private List<String> department = new ArrayList<String>();
	private String title = "";
	public String stuid ="";
	public String token = "";

	VoteSubmitViewPager viewPager;
	ExaminationSubmitAdapter pagerAdapter;
	List<View> viewItems = new ArrayList<View>();
	List<AnSwerInfo> dataItems = new ArrayList<AnSwerInfo>();

	public int score = 0; //成绩
	private String errorMsg="";
	public List<String> questionId = new ArrayList<String>();
//	public List<String> resultlist = new ArrayList<String>();
	//服务器base地址
	public String BASE_URL = PublicStatic.SERVICE_HOST.concat(PublicStatic.API_URL);
	private RequestApi apiService;

	//接口取消订阅
	CompositeDisposable mCompositeDisposable;
	
	Dialog builderSubmit;

	public Map<Integer,String> resultlist = new HashMap<Integer,String>();
	Timer timer;
	TimerTask timerTask;
	int minute = 90;
	int second = 0;

	boolean isPause = false;
	int isFirst;


//	private Handler handlerSubmit = new Handler(){
//		@Override
//		public void handleMessage(Message msg) {
//			// TODO Auto-generated method stub
//			super.handleMessage(msg);
//
//			switch (msg.what) {
//			case 1:
//				showSubmitDialog();
//				new Handler().postDelayed(new Runnable() {
//
//					@Override
//					public void run() {
//						builderSubmit.dismiss();
//						finish();
//					}
//				}, 3000);
//				break;
//			default:
//				break;
//			}
//
//		}
//	};

	Handler handlerTime = new Handler() {
		public void handleMessage(Message msg) {
			// 判断时间快到前5分钟字体颜色改变
			if (minute < 5) {
				right.setTextColor(Color.RED);
			} else {
				right.setTextColor(Color.WHITE);
			}
			if (minute == 0) {
				if (second == 0) {
					isFirst+=1;
					// 时间到
					if(isFirst==1){
						showTimeOutDialog(true, "0");
						uploadExamination();
						List<String> valuesList = new ArrayList<String>(resultlist.values());
//						Toast.makeText(AnalogyExaminationActivity.this, valuesList.toString(), Toast.LENGTH_LONG).show();
						toUpload(token,RequestAnswerBean.toUpload(valuesList,ConstantData.answerId,stuid,score));
					}
					right.setText("00:00");
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
						right.setText("0" + minute + ":" + second);
					} else {
						right.setText("0" + minute + ":0" + second);
					}
				}
			} else {
				if (second == 0) {
					second = 59;
					minute--;
					if (minute >= 10) {
						right.setText(minute + ":" + second);
					} else {
						right.setText("0" + minute + ":" + second);
					}
				} else {
					second--;
					if (second >= 10) {
						if (minute >= 10) {
							right.setText(minute + ":" + second);
						} else {
							right.setText("0" + minute + ":" + second);
						}
					} else {
						if (minute >= 10) {
							right.setText(minute + ":0" + second);
						} else {
							right.setText("0" + minute + ":0" + second);
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

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_practice_test);
		initView();
		getQuestion(new StuBean(stuid));
		loadData();

	}

	public void initView() {
		mCompositeDisposable = new CompositeDisposable();
		leftIv = (ImageView) findViewById(R.id.left);
		titleTv = (TextView) findViewById(R.id.title);
		right = (TextView) findViewById(R.id.right);
		Drawable drawable1 = getBaseContext().getResources().getDrawable(
				R.drawable.ic_practice_time);
		drawable1.setBounds(0, 0, drawable1.getMinimumWidth(),
				drawable1.getMinimumHeight());
		Intent intent = getIntent();
		stuid = intent.getExtras().getString("id");
//		stuid = "2015157011";
		token = intent.getExtras().getString("token");
//		token = "eyJhbGciOiJIUzUxMiJ9.eyJtb2RlIjoicGFzc3dvcmQiLCJzdWIiOiIxODk0NjEwNDAzMiIsImlzcyI6ImN1aSIsImV4cCI6MTU3MjkyMzE5OSwiaWF0IjoxNTcyMzE4Mzk5LCJyb2wiOiJQQSJ9.BW_ryOZVFFd57rl3WDRCz2E0Wd0lAjMxAE2AXaB5hRzDzrCPpINQJuuh-iot2fLywyx_hP7rW6AZom1ffqrUJg";
		right.setCompoundDrawables(drawable1, null, null, null);
		right.setText("15:00");
		viewPager = (VoteSubmitViewPager) findViewById(R.id.vote_submit_viewpager);
		leftIv.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// finish();
				isPause = true;
				showTimeOutDialog(true, "1");
				Message msg = new Message();
				msg.what = 0;
				handlerStopTime.sendMessage(msg);
			}
		});
		
		initViewPagerScroll();
		apiService = initRetrofit1().create(RequestApi.class);

		this.getWindow().addFlags(WindowManager.LayoutParams.FLAG_SECURE );
	}

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

	private void loadData(){
		for (int i = 0; i < ConstantData.answerName.size(); i++) {
			AnSwerInfo info = new AnSwerInfo();
			info.setQuestionId(ConstantData.answerId.get(i));// 试题主键
			info.setQuestionName(ConstantData.answerName.get(i));// 试题题目
			info.setOptionA(ConstantData.answerOptionA.get(i));// 试题选项A
			info.setOptionB(ConstantData.answerOptionB.get(i));// 试题选项B
			info.setOptionC(ConstantData.answerOptionC.get(i));// 试题选项C
		    info.setOptionD(ConstantData.answerOptionD.get(i));// 试题选项D
			info.setRightAnswer(ConstantData.rightAnswer.get(i));//试题正确答案
		    info.setOption_type("0");
		    dataItems.add(info);
		    for (int j = 0; j < dataItems.size();j++){
				resultlist.put(j,null);
			}

	}
		for (int i = 0; i < dataItems.size(); i++) {
			viewItems.add(getLayoutInflater().inflate(
					R.layout.vote_submit_viewpager_item, null));
		}
		pagerAdapter = new ExaminationSubmitAdapter(
				AnalogyExaminationActivity.this, viewItems,
				dataItems);
		viewPager.setAdapter(pagerAdapter);
		viewPager.getParent()
				.requestDisallowInterceptTouchEvent(false);
	}
	
	/**
     * 设置ViewPager的滑动速度
     * 
     * */
    private void initViewPagerScroll( ){
    try {
            Field mScroller = null;
            mScroller = ViewPager.class.getDeclaredField("mScroller");
            mScroller.setAccessible(true); 
            ViewPagerScroller scroller = new ViewPagerScroller(viewPager.getContext());
            mScroller.set(viewPager, scroller);
        }catch(NoSuchFieldException e){
       
        }catch (IllegalArgumentException e){
       
        }catch (IllegalAccessException e){
       
        }
    }
	/**
	 * @param index
	 *            根据索引值切换页面
	 */
	public void setCurrentView(int index) {
		viewPager.setCurrentItem(index);
	}

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		stopTime();
		minute = -1;
		second = -1;
		if (mCompositeDisposable != null) {
			mCompositeDisposable.clear(); // clear时网络请求会随即cancel
			mCompositeDisposable = null;
		}
		super.onDestroy();
	}

	// 保存题号答案、计算所得成绩
	public void uploadExamination() {
		if (resultlist.size() > 0) {
			for (int i = 0; i < dataItems.size(); i++) {
				if (resultlist.get(i) == (null)) {
					score += 0;
				} else {
					if (resultlist.get(i).equals(dataItems.get(i).getRightAnswer())) {
						score += 5;
					} else {
						score += 0;
					}
				}
			}
		} else {
			score = 0;
		}
	}

	// 弹出对话框通知用户答题时间到
	protected void showTimeOutDialog(final boolean flag, final String backtype) {
		final Dialog builder = new Dialog(this, R.style.dialog);
		builder.setContentView(R.layout.my_dialog);
		TextView title = (TextView) builder.findViewById(R.id.dialog_title);
		TextView content = (TextView) builder.findViewById(R.id.dialog_content);
		if (backtype.equals("0")) {
			content.setText("您的答题时间结束,是否提交试卷?");
		} else if(backtype.equals("1")){
			content.setText("您要结束本次模拟答题吗？");
		}else{
			content.setText(errorMsg+"");
		}
		final Button confirm_btn = (Button) builder
				.findViewById(R.id.dialog_sure);
		Button cancel_btn = (Button) builder.findViewById(R.id.dialog_cancle);
		if (backtype.equals("0")) {
			confirm_btn.setText("提交");
			cancel_btn.setText("退出");
		} else if(backtype.equals("1")){
			confirm_btn.setText("退出");
			cancel_btn.setText("继续答题");
		}else{
			confirm_btn.setText("确定");
			cancel_btn.setVisibility(View.GONE);
		}
		cancel_btn.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if (backtype.equals("0")) {
					finish();
					builder.dismiss();
				} else {
					isPause = false;
					builder.dismiss();
					Message msg = new Message();
					msg.what = 1;
					handlerStopTime.sendMessage(msg);
				}
			}
		});
		confirm_btn.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				finish();
				builder.dismiss();
			}
		});
		builder.setCanceledOnTouchOutside(false);// 设置点击Dialog外部任意区域关闭Dialog
		builder.setOnKeyListener(new OnKeyListener() {

			@Override
			public boolean onKey(DialogInterface dialog, int keyCode,
                                 KeyEvent event) {

				return flag;
			}
		});
		builder.show();
	}
	// 弹出对话框通知用户提交成功
	protected void showSubmitDialog() {
		builderSubmit = new Dialog(this, R.style.dialog);
		builderSubmit.setContentView(R.layout.my_dialog);
		TextView title = (TextView) builderSubmit.findViewById(R.id.dialog_title);
		TextView content = (TextView) builderSubmit.findViewById(R.id.dialog_content);
		content.setText("提交成功!");
		final Button confirm_btn = (Button) builderSubmit
				.findViewById(R.id.dialog_sure);
		confirm_btn.setVisibility(View.GONE);
		Button cancel_btn = (Button) builderSubmit.findViewById(R.id.dialog_cancle);
		cancel_btn.setVisibility(View.GONE);
		builderSubmit.setCanceledOnTouchOutside(false);// 设置点击Dialog外部任意区域关闭Dialog
		builderSubmit.setOnKeyListener(new OnKeyListener() {
			
			@Override
			public boolean onKey(DialogInterface dialog, int keyCode, KeyEvent event) {
				
				return true;
			}
		});
		builderSubmit.show();
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if ((keyCode == KeyEvent.KEYCODE_BACK)) {
			isPause = true;
			showTimeOutDialog(true, "1");
			Message msg = new Message();
			msg.what = 0;
			handlerStopTime.sendMessage(msg);
			return true;
		}
		return super.onKeyDown(keyCode, event);
	}

	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		Message msg = new Message();
		msg.what = 0;
		handlerStopTime.sendMessage(msg);
		super.onPause();
	}
	
	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
			Message msg = new Message();
			msg.what = 1;
			handlerStopTime.sendMessage(msg);
		super.onResume();
	}

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

	private void disposeableAdd(Disposable d) {
		if (mCompositeDisposable != null && !mCompositeDisposable.isDisposed()) {
			mCompositeDisposable.add(d);
		}
	}

	//获取试题内容接口
	public void getQuestion(StuBean stu){
		//3.新rxjava接口服务对象调用接口中方法
		// apiService = initRetrofit1().create(RequestApi.class);
		Gson gson=new Gson();
		//通过gson转换成json字符串,调用login接口请求
		//token为本地保存的token
		apiService.getQuestion(gson.toJson(stu),token).
				subscribeOn(Schedulers.io())
				.observeOn(AndroidSchedulers.mainThread())
				.subscribe( new Observer<ResponseBean<ResponseQuestionBean>>(){
					@Override
					public void onSubscribe(Disposable d) {
						disposeableAdd(d);
					}
					@Override
					public void onNext(ResponseBean<ResponseQuestionBean> resResult) {
						if (resResult.getResult()!=0){
							Toast.makeText(AnalogyExaminationActivity.this, resResult.getErrMsg()+"", Toast.LENGTH_LONG).show();
						}else {
						for (int i = 0; i < resResult.getData().getQuestion().size(); i++) {
							ConstantData.answerName.add(resResult.getData().getQuestion().get(i));
							ConstantData.answerOptionA.add(resResult.getData().getA().get(i));
							ConstantData.answerOptionB.add(resResult.getData().getB().get(i));
							ConstantData.answerOptionC.add(resResult.getData().getC().get(i));
							ConstantData.answerOptionD.add(resResult.getData().getD().get(i));
							ConstantData.rightAnswer.add(resResult.getData().getRightAnswer().get(i));
							ConstantData.answerId.add(resResult.getData().getQuestionId().get(i));
						}for (int i = 0; i < resResult.getData().getDepartment().size(); i++){
							department.add(resResult.getData().getDepartment().get(i));
						}
						for (int i = 0;i<department.size();i++){
							title = title+department.get(i)+" ";
						}
						titleTv.setText(title);
						loadData();
						}
					}

					@Override
					public void onError(Throwable e) {
						Log.e("login", "onFailure: " + e.getMessage());
						Toast.makeText( AnalogyExaminationActivity.this,"获取试题失败", Toast.LENGTH_LONG).show();
					}

					@Override
					public void onComplete() {
					}

				});

	}
	//提交答案
	public void toUpload(String token, RequestAnswerBean toload) {
		Gson gson = new Gson();
		//通过gson转换成json字符串,调用login接口请求
		apiService.upload(token,gson.toJson(toload)).
				subscribeOn(Schedulers.io())
				.observeOn(AndroidSchedulers.mainThread())
				.subscribe(new Observer<ResponseBean<TokenBean>>() {
					@Override
					public void onSubscribe(Disposable d) {
						disposeableAdd(d);
					}

					@Override
					public void onNext(ResponseBean<TokenBean> resResult) {
						if (resResult.getResult()!=0){
							Toast.makeText(AnalogyExaminationActivity.this, resResult.getErrMsg()+"", Toast.LENGTH_LONG).show();
						}else {
							Toast.makeText(AnalogyExaminationActivity.this, "答案提交成功", Toast.LENGTH_LONG).show();
							AlertDialog.Builder builder = new AlertDialog.Builder(AnalogyExaminationActivity.this);
							builder.setTitle("答案提交成功，您的考试成绩为："+score).setIcon(android.R.drawable.ic_dialog_info)
									.setNegativeButton("取消", new DialogInterface.OnClickListener() {
										@Override
										public void onClick(DialogInterface dialog, int i) {
											dialog.dismiss();
										}
									});
							builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {

								@Override
								public void onClick(DialogInterface dialogInterface, int i) {
									Intent intent = new Intent(AnalogyExaminationActivity.this,LoginActivity.class);
									startActivity(intent);
								}
							});
							builder.show();
						}

//						moveTaskToBack(true);
//						android.os.Process.killProcess(android.os.Process.myPid());
//						System.exit(1);
					}
					@Override
					public void onError(Throwable e) {
						Log.e("login", "onFailure: " + e.getMessage());
						Toast.makeText(AnalogyExaminationActivity.this, "接口连接失败", Toast.LENGTH_LONG).show();
					}
					@Override
					public void onComplete() {
					}

				});
	}
}
