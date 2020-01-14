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
import com.example.lenovo.examdemo.Bean.ResponsePaperBean;
import com.example.lenovo.examdemo.Bean.ResponseQuestionBean;
import com.example.lenovo.examdemo.Bean.StuBean;
import com.example.lenovo.examdemo.Bean.TokenBean;
import com.example.lenovo.examdemo.R;
import com.example.lenovo.examdemo.Utils.ConstantData;
import com.example.lenovo.examdemo.Utils.RetrofitManager;
import com.example.lenovo.examdemo.Utils.ViewPagerScroller;
import com.example.lenovo.examdemo.View.VoteSubmitViewPager;
import com.google.gson.Gson;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;


/**
 * 类比考试答题
 */
public class AnalogyExaminationActivity extends Activity {

	private ImageView leftIv;
	private TextView titleTv;
	private TextView right;
	public String exam = "";

	VoteSubmitViewPager viewPager;
	ExaminationSubmitAdapter pagerAdapter;
	List<View> viewItems = new ArrayList<View>();
	List<AnSwerInfo> dataItems = new ArrayList<AnSwerInfo>();

	public int score = 0; //成绩
	private String errorMsg="";
	public List<String> questionId = new ArrayList<String>();
	public List<Integer> isRight = new ArrayList<Integer>();
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
						toUpload(RequestAnswerBean.toUpload(valuesList,ConstantData.answerId,isRight,ConstantData.stuid,exam,score));
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
		getQuestion(exam);
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
		exam = intent.getStringExtra("exam_name");
		right.setCompoundDrawables(drawable1, null, null, null);
		right.setText("15:00");
		titleTv.setText(exam);
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
		apiService = RetrofitManager.getInstance().getRetrofit();

//		this.getWindow().addFlags(WindowManager.LayoutParams.FLAG_SECURE );   //防止截屏
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
			info.setContent(ConstantData.Content.get(i));//题型描述
			info.setCaseQuestion(ConstantData.caseQuestion.get(i));//案例题案例
			info.setPerScore(ConstantData.perScore.get(i));//题型分值
			info.setquestionType(ConstantData.questionType.get(i));//案例题类型
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
		pagerAdapter.getVideo().resetPlayer();
	}

//	@Override
//	protected void onStop() {
//		super.onStop();
//		pagerAdapter.getVideo().resetPlayer();
//	}



	// 保存题号答案、计算所得成绩
	public void uploadExamination() {
		if (resultlist.size() > 0) {
			for (int i = 0; i < dataItems.size(); i++) {
				if (resultlist.get(i) == (null)) {
					score += 0;
					isRight.add(0);
				} else {
					if (resultlist.get(i).equals(dataItems.get(i).getRightAnswer())) {
						score += 5;
						isRight.add(1);
					} else {
						score += 0;
						isRight.add(0);
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
				clearData();
				Intent intent = new Intent(AnalogyExaminationActivity.this,MainActivity.class);
				startActivity(intent);
//				finish();
//				Toast.makeText(AnalogyExaminationActivity.this, ConstantData.token, Toast.LENGTH_LONG).show();
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
	public void getQuestion(String examName){
		apiService.getPaper(examName).
				subscribeOn(Schedulers.io())
				.observeOn(AndroidSchedulers.mainThread())
				.subscribe( new Observer<ResponseBean<ResponsePaperBean>>(){
					@Override
					public void onSubscribe(Disposable d) {
						disposeableAdd(d);
					}
					@Override
					public void onNext(ResponseBean<ResponsePaperBean> resResult) {
						if (resResult.getResult()!=0){
								Toast.makeText(AnalogyExaminationActivity.this, resResult.getErrMsg()+"", Toast.LENGTH_LONG).show();
						}else {
							if (resResult.getData().getSelect() != null){
								for (int i = 0; i < resResult.getData().getSelect().getQuestions().size(); i++) {
									ConstantData.answerName.add(resResult.getData().getSelect().getQuestions().get(i).getQuestion());
									ConstantData.answerOptionA.add(resResult.getData().getSelect().getQuestions().get(i).getA());
									ConstantData.answerOptionB.add(resResult.getData().getSelect().getQuestions().get(i).getB());
									ConstantData.answerOptionC.add(resResult.getData().getSelect().getQuestions().get(i).getC());
									ConstantData.answerOptionD.add(resResult.getData().getSelect().getQuestions().get(i).getD());
									ConstantData.rightAnswer.add(resResult.getData().getSelect().getQuestions().get(i).getRightAnswer());
									ConstantData.answerId.add(resResult.getData().getSelect().getQuestions().get(i).getQuestionId());
									ConstantData.caseQuestion.add(null);   //存入选择题时，案例题案例一直add null
									ConstantData.questionType.add(null);
									if (i == 0){
										ConstantData.Content.add(resResult.getData().getSelect().getIntroduce());
										ConstantData.perScore.add(resResult.getData().getSelect().getPerScoer());
									}else {
										ConstantData.Content.add(null);
										ConstantData.perScore.add(0);
									}
								}
							}
							if(resResult.getData().getCaseQuestions().size() != 0){
								for(int i = 0; i < resResult.getData().getCaseQuestions().size(); i++){
									ConstantData.Content.add(resResult.getData().getCaseQuestions().get(i).getIntroduce());
									ConstantData.caseQuestion.add(resResult.getData().getCaseQuestions().get(i).getQuestion());
									ConstantData.perScore.add(resResult.getData().getCaseQuestions().get(i).getPerScore());
									ConstantData.questionType.add(resResult.getData().getCaseQuestions().get(i).getQuestionType());
									for(int j = 0; j < resResult.getData().getCaseQuestions().get(i).getQuestions().size(); j++){

										ConstantData.answerName.add(resResult.getData().getCaseQuestions().get(i).getQuestions().get(j).getQuestion());
										ConstantData.answerOptionA.add(resResult.getData().getCaseQuestions().get(i).getQuestions().get(j).getA());
										ConstantData.answerOptionB.add(resResult.getData().getCaseQuestions().get(i).getQuestions().get(j).getB());
										ConstantData.answerOptionC.add(resResult.getData().getCaseQuestions().get(i).getQuestions().get(j).getC());
										ConstantData.answerOptionD.add(resResult.getData().getCaseQuestions().get(i).getQuestions().get(j).getD());
										ConstantData.rightAnswer.add(resResult.getData().getCaseQuestions().get(i).getQuestions().get(j).getRightAnswer());
										ConstantData.answerId.add(resResult.getData().getCaseQuestions().get(i).getQuestions().get(j).getQuestionId());
										if (j > 0){
											ConstantData.Content.add(null);    //每个案例对应多道选择题，存入一个案例其他position对应存入null
											ConstantData.caseQuestion.add(null);
											ConstantData.questionType.add(null);
											ConstantData.perScore.add(0);
										}
									}
								}

							}

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
	public void toUpload(RequestAnswerBean toload) {
		Gson gson = new Gson();
		//通过gson转换成json字符串,调用login接口请求
		apiService.upload(gson.toJson(toload)).
				subscribeOn(Schedulers.io())
				.observeOn(AndroidSchedulers.mainThread())
				.subscribe(new Observer<ResponseBean>() {
					@Override
					public void onSubscribe(Disposable d) {
						disposeableAdd(d);
					}

					@Override
					public void onNext(ResponseBean resResult) {
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
									Intent intent = new Intent(AnalogyExaminationActivity.this,LocalActivity.class);
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
	//清理ConstantData
	public void clearData(){
		ConstantData.answerName.clear();
		ConstantData.answerOptionA.clear();
		ConstantData.answerOptionB.clear();
		ConstantData.answerOptionC.clear();
		ConstantData.answerOptionD.clear();
		ConstantData.rightAnswer.clear();
		ConstantData.answerId.clear();
		ConstantData.Content.clear();
		ConstantData.caseQuestion.clear();
		ConstantData.perScore.clear();
		ConstantData.questionType.clear();
	}

}
