package com.example.lenovo.examdemo.Adapter;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.support.v4.view.PagerAdapter;
import android.support.v7.app.AlertDialog;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.style.ForegroundColorSpan;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.lenovo.examdemo.Activity.AnalogyExaminationActivity;
import com.example.lenovo.examdemo.Activity.ConnectTeacherActivity;
import com.example.lenovo.examdemo.Activity.MainActivity;
import com.example.lenovo.examdemo.Bean.AnSwerInfo;
import com.example.lenovo.examdemo.Bean.RequestAnswerBean;
import com.example.lenovo.examdemo.Bean.SaveQuestionInfo;
import com.example.lenovo.examdemo.Database.DBManager;
import com.example.lenovo.examdemo.R;
import com.example.lenovo.examdemo.Utils.ConstantData;
import com.example.lenovo.examdemo.Utils.ConstantUtil;
import com.tencent.liteav.demo.play.SuperPlayerModel;
import com.tencent.liteav.demo.play.SuperPlayerView;
import com.tencent.liteav.demo.play.v3.SuperPlayerVideoId;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class ExaminationSubmitAdapter extends PagerAdapter {

    AnalogyExaminationActivity mContext;
    // 传递过来的页面view的集合
    List<View> viewItems;
    // 每个item的页面view
    View convertView;
    // 传递过来的所有数据
    List<AnSwerInfo> dataItems;

    private Map<Integer, Boolean> map = new HashMap<Integer, Boolean>();
    private Map<Integer, Boolean> mapClick = new HashMap<Integer, Boolean>();
    private Map<Integer, String> mapMultiSelect = new HashMap<Integer, String>();


    boolean isNext = false;

    private int nullNum = 0;


    public ExaminationSubmitAdapter(AnalogyExaminationActivity context, List<View> viewItems, List<AnSwerInfo> dataItems) {
        mContext = context;
        this.viewItems = viewItems;
        this.dataItems = dataItems;
    }

    public long getItemId(int position) {
        return position;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView(viewItems.get(position));
    }

    @Override
    public Object instantiateItem(ViewGroup container, final int position) {
        final ViewHolder holder = new ViewHolder();
        convertView = viewItems.get(position);
        holder.questionType = (TextView) convertView.findViewById(R.id.activity_prepare_test_no);
        holder.question = (TextView) convertView.findViewById(R.id.activity_prepare_test_question);
        holder.previousBtn = (LinearLayout) convertView.findViewById(R.id.activity_prepare_test_upLayout);
        holder.nextBtn = (LinearLayout) convertView.findViewById(R.id.activity_prepare_test_nextLayout);
        holder.nextText = (TextView) convertView.findViewById(R.id.menu_bottom_nextTV);
        holder.errorBtn = (LinearLayout) convertView.findViewById(R.id.activity_prepare_test_errorLayout);
        holder.totalText = (TextView) convertView.findViewById(R.id.activity_prepare_test_totalTv);
        holder.totalLayout = (LinearLayout) convertView.findViewById(R.id.activity_prepare_test_totalLayout);
        holder.nextImage = (ImageView) convertView.findViewById(R.id.menu_bottom_nextIV);
//        holder.teacherLayout = (LinearLayout) convertView.findViewById(R.id.activity_prepare_test_teacherLayout);
        holder.explaindetailTv = (TextView) convertView.findViewById(R.id.activity_prepare_test_explaindetail);
        holder.layoutA = (LinearLayout) convertView.findViewById(R.id.activity_prepare_test_layout_a);
        holder.layoutB = (LinearLayout) convertView.findViewById(R.id.activity_prepare_test_layout_b);
        holder.layoutC = (LinearLayout) convertView.findViewById(R.id.activity_prepare_test_layout_c);
        holder.layoutD = (LinearLayout) convertView.findViewById(R.id.activity_prepare_test_layout_d);
        holder.layoutE = (LinearLayout) convertView.findViewById(R.id.activity_prepare_test_layout_e);
        holder.edit = (LinearLayout) convertView.findViewById(R.id.edit);
        holder.ivA = (ImageView) convertView.findViewById(R.id.vote_submit_select_image_a);
        holder.ivB = (ImageView) convertView.findViewById(R.id.vote_submit_select_image_b);
        holder.ivC = (ImageView) convertView.findViewById(R.id.vote_submit_select_image_c);
        holder.ivD = (ImageView) convertView.findViewById(R.id.vote_submit_select_image_d);
        holder.ivE = (ImageView) convertView.findViewById(R.id.vote_submit_select_image_e);
        holder.tvA = (TextView) convertView.findViewById(R.id.vote_submit_select_text_a);
        holder.tvB = (TextView) convertView.findViewById(R.id.vote_submit_select_text_b);
        holder.tvC = (TextView) convertView.findViewById(R.id.vote_submit_select_text_c);
        holder.tvD = (TextView) convertView.findViewById(R.id.vote_submit_select_text_d);
        holder.tvE = (TextView) convertView.findViewById(R.id.vote_submit_select_text_e);
        holder.content = (TextView) convertView.findViewById(R.id.content);
        holder.caseQuestion = (TextView) convertView.findViewById(R.id.caseQuestion);
        holder.mSuperPlayerView = (SuperPlayerView) convertView.findViewById(R.id.videoView);

        holder.totalText.setText(position + 1 + "/" + dataItems.size());

        holder.totalLayout.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                final EditText inputServer = new EditText(mContext);
                AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
                builder.setTitle("请输入想跳转的考试题号").setIcon(android.R.drawable.ic_dialog_info).setView(inputServer)
                        .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int i) {
                                dialog.dismiss();
                            }
                        });
                builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        String text = inputServer.getText().toString();
                        if (text != "") {
                            mContext.setCurrentView(Integer.parseInt(text) - 1);
                        }
                    }
                });
                builder.show();
            }
        });

//        holder.teacherLayout.setOnClickListener(new OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Intent intent = new Intent(mContext, ConnectTeacherActivity.class);
//                mContext.startActivity(intent);
//            }
//        });

        holder.errorBtn.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View arg0) {
                // TODO Auto-generated method stub
            }
        });

//        if (dataItems.get(position).getOptionA().equals("")) {
//            holder.layoutA.setVisibility(View.GONE);
//        }
//        if (dataItems.get(position).getOptionB().equals("")) {
//            holder.layoutB.setVisibility(View.GONE);
//        }
//        if (dataItems.get(position).getOptionC().equals("")) {
//            holder.layoutC.setVisibility(View.GONE);
//        }
//        if (dataItems.get(position).getOptionD().equals("")) {
//            holder.layoutD.setVisibility(View.GONE);
//        }
//        if (dataItems.get(position).getOptionA().equals("") && dataItems.get(position).getOptionB().equals("") && dataItems.get(position).getOptionC().equals("") && dataItems.get(position).getOptionD().equals("")) {
//            holder.layoutA.setVisibility(View.GONE);
//            holder.layoutB.setVisibility(View.GONE);
//            holder.layoutC.setVisibility(View.GONE);
//            holder.layoutD.setVisibility(View.GONE);
//            holder.edit.setVisibility(View.VISIBLE);
//        }
        holder.tvA.setVisibility(View.VISIBLE);
        holder.tvB.setVisibility(View.VISIBLE);
        holder.tvC.setVisibility(View.VISIBLE);
        holder.tvD.setVisibility(View.VISIBLE);
//        holder.tvE.setVisibility(View.VISIBLE);
        holder.tvA.setText("A." + dataItems.get(position).getOptionA());
        holder.tvB.setText("B." + dataItems.get(position).getOptionB());
        holder.tvC.setText("C." + dataItems.get(position).getOptionC());
        holder.tvD.setText("D." + dataItems.get(position).getOptionD());
        if(dataItems.get(position).getContent() != null){
            holder.content.setVisibility(View.VISIBLE);
            holder.content.setText(dataItems.get(position).getContent()+"("+dataItems.get(position).getPerScore()+"分)");
        }else {
            holder.content.setVisibility(View.GONE);
        }

        if(dataItems.get(position).getCaseQuestion() != null){
            holder.caseQuestion.setVisibility(View.VISIBLE);
            holder.caseQuestion.setText("        "+dataItems.get(position).getCaseQuestion());
            if(dataItems.get(position).getquestionType().equals("video")){
                holder.caseQuestion.setVisibility(View.GONE);
            }
        }else {
            holder.caseQuestion.setVisibility(View.GONE);
        }

        if (dataItems.get(position).getquestionType() != null){
            if (dataItems.get(position).getquestionType().equals("case")){
                holder.question.setText(dataItems.get(position).getQuestionName());
                holder.mSuperPlayerView.setVisibility(View.GONE);
            }else {
                holder.mSuperPlayerView.setVisibility(View.VISIBLE);
                SuperPlayerModel model = new SuperPlayerModel();
                model.appId = 1300414804;// 配置 AppId
                model.videoId = new SuperPlayerVideoId();
                model.videoId.fileId = dataItems.get(position).getCaseQuestion(); // 配置 FileId
                holder.mSuperPlayerView.playWithModel(model);
            }
        }


        holder.question.setText(position+1+"."+dataItems.get(position).getQuestionName());

        holder.layoutA.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View arg0) {
                holder.ivA.setImageResource(R.drawable.xuanzhong);
                holder.ivB.setImageResource(R.drawable.ic_practice_test_normal);
                holder.ivC.setImageResource(R.drawable.ic_practice_test_normal);
                holder.ivD.setImageResource(R.drawable.ic_practice_test_normal);
                mContext.resultlist.put(position, "A");
                dataItems.get(position).setIsSelect("0");
                mContext.setCurrentView(position + 1);
            }
        });
        holder.layoutB.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View arg0) {
                holder.ivB.setImageResource(R.drawable.xuanzhong);
                holder.ivA.setImageResource(R.drawable.ic_practice_test_normal);
                holder.ivC.setImageResource(R.drawable.ic_practice_test_normal);
                holder.ivD.setImageResource(R.drawable.ic_practice_test_normal);
                mContext.resultlist.put(position, "B");
                dataItems.get(position).setIsSelect("0");
                mContext.setCurrentView(position + 1);
            }
        });

        holder.layoutC.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View arg0) {
                holder.ivC.setImageResource(R.drawable.xuanzhong);
                holder.ivA.setImageResource(R.drawable.ic_practice_test_normal);
                holder.ivB.setImageResource(R.drawable.ic_practice_test_normal);
                holder.ivD.setImageResource(R.drawable.ic_practice_test_normal);
                mContext.resultlist.put(position, "C");
                dataItems.get(position).setIsSelect("0");
                mContext.setCurrentView(position + 1);
            }
        });
        holder.layoutD.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View arg0) {
                holder.ivD.setImageResource(R.drawable.xuanzhong);
                holder.ivA.setImageResource(R.drawable.ic_practice_test_normal);
                holder.ivC.setImageResource(R.drawable.ic_practice_test_normal);
                holder.ivB.setImageResource(R.drawable.ic_practice_test_normal);
                mContext.resultlist.put(position, "D");
                dataItems.get(position).setIsSelect("0");
                mContext.setCurrentView(position + 1);
            }
        });

        ForegroundColorSpan blueSpan = new ForegroundColorSpan(Color.parseColor("#2b89e9"));

        SpannableStringBuilder builder1 = new SpannableStringBuilder(holder.question.getText().toString());
        builder1.setSpan(blueSpan, 0, 0, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        holder.question.setText(builder1);

        // 最后一页修改"下一步"按钮文字
        if (position == viewItems.size() - 1) {
            holder.nextText.setText("提交");
            holder.nextImage.setImageResource(R.drawable.vote_submit_finish);
            holder.nextImage.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
                    builder.setTitle("确认提交试卷？").setIcon(android.R.drawable.ic_dialog_info)
                            .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int i) {
                                    dialog.dismiss();
                                }
                            });
                    builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {

                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
//                                Toast.makeText(mContext, mContext.resultlist.toString()+"+"+mContext.resultlist.size(), Toast.LENGTH_LONG).show();
//                                Toast.makeText(mContext, mContext.valuesList.size()+"+"+mContext.resultlist.toString()+"+"+mContext.valuesList.toString()+"+"+mContext.resultlist.get(59)+"+"+mContext.lastList.toString(), Toast.LENGTH_LONG).show();
                            mContext.uploadExamination();
                            List<String> valuesList = new ArrayList<String>(mContext.resultlist.values());
//                            Toast.makeText(mContext, valuesList.toString(), Toast.LENGTH_LONG).show();
                            mContext.toUpload(RequestAnswerBean.toUpload(valuesList, ConstantData.answerId, mContext.isRight,ConstantData.stuid, mContext.exam,mContext.score));

                            valuesList.clear();
                        }
                    });
                    builder.show();

                }
            });
        }
        holder.previousBtn.setOnClickListener(new LinearOnClickListener(position - 1, false, position, holder));
        holder.nextBtn.setOnClickListener(new LinearOnClickListener(position + 1, true, position, holder));
        container.addView(viewItems.get(position));
        return viewItems.get(position);
    }

    /**
     * @author 设置上一步和下一步按钮监听
     */
    class LinearOnClickListener implements OnClickListener {

        private int mPosition;
        private int mPosition1;
        private boolean mIsNext;
        private ViewHolder viewHolder;

        public LinearOnClickListener(int position, boolean mIsNext, int position1, ViewHolder viewHolder) {
            mPosition = position;
            mPosition1 = position1;
//			this.viewHolder=viewHolder;
            this.mIsNext = mIsNext;
        }

        @Override
        public void onClick(View v) {
//            if (!map.containsKey(mPosition1)) {
//                Toast.makeText(mContext, "请选择选项", Toast.LENGTH_SHORT).show();
//                return;
////                    Intent intent = new Intent(mContext, MainActivity.class);
////                    mContext.startActivity(intent);
//            }
            if (mPosition == -1) {
                Toast.makeText(mContext, "已经是第一页", Toast.LENGTH_SHORT).show();
                return;
            } else {
                isNext = mIsNext;
                mContext.setCurrentView(mPosition);
            }
            isNext = mIsNext;
            mContext.setCurrentView(mPosition);
        }
    }

    @Override
    public int getCount() {
        if (viewItems == null)
            return 0;
        return viewItems.size();
    }

    @Override
    public boolean isViewFromObject(View arg0, Object arg1) {
        return arg0 == arg1;
    }

//    public int totalNull() {
//        for (int i = 0; i < mContext.questionInfos.size(); i++) {
//            if (mContext.questionInfos.get(i).getAnswer() == null) {
//                nullNum++;
//            } else {
//                nullNum = nullNum;
//            }
//        }
//        return nullNum;
//    }


    public class ViewHolder {
        TextView questionType;
        TextView question;
        LinearLayout previousBtn, nextBtn, errorBtn;
        TextView nextText;
        TextView totalText;
        ImageView nextImage;
        LinearLayout totalLayout;
        //        LinearLayout teacherLayout;
        TextView explaindetailTv;
        LinearLayout layoutA;
        LinearLayout layoutB;
        LinearLayout layoutC;
        LinearLayout layoutD;
        LinearLayout layoutE;
        LinearLayout edit;
        ImageView ivA;
        ImageView ivB;
        ImageView ivC;
        ImageView ivD;
        ImageView ivE;
        TextView tvA;
        TextView tvB;
        TextView tvC;
        TextView tvD;
        TextView tvE;
        TextView content;
        TextView caseQuestion;
        SuperPlayerView mSuperPlayerView;
    }

    public SuperPlayerView getVideo(){
        ViewHolder holder = new ViewHolder();
        return holder.mSuperPlayerView;
    }


}
