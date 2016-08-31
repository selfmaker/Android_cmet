package com.example.registerapp;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;

import com.example.registerapp.adapter.MyErrorQuestionListAdapter;
import com.example.registerapp.bean.AnSwerInfo;
import com.example.registerapp.bean.ErrorQuestion;
import com.example.registerapp.bean.ErrorQuestionInfo;
import com.example.registerapp.database.DBManager;
import com.example.registerapp.utils.ConstantData;

public class ItemListactivity extends Activity {

	private ImageView left;
	private TextView title;

	private List<Map<String, Object>> data = new ArrayList<Map<String, Object>>();// 列表数据
	private ListView listView;
	
	private List<AnSwerInfo> list=new ArrayList<AnSwerInfo>();

	private MyErrorQuestionListAdapter adapter;
	
	AnSwerInfo question;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.my_error_question);

		initView();
	}

	private void initView() {
		left = (ImageView) findViewById(R.id.left);
		title = (TextView) findViewById(R.id.title);
		title.setText("试题列表");
		listView = (ListView) findViewById(R.id.listview);

		left.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				finish();
			}
		});
		
		adapter = new MyErrorQuestionListAdapter(this, data, listView);
		listView.setAdapter(adapter);
		
		listView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int position,
					long arg3) {
				// TODO Auto-generated method stub
				Intent intent=new Intent(ItemListactivity.this,AnalogyExaminationActivity.class);
				intent.putExtra("position", position);
//				question=list.get(position);
//				intent.putExtra("questionName", question.getQuestionName());
//				intent.putExtra("questionType", question.getQuestionType());
//				intent.putExtra("questionAnswer", question.getQuestionAnswer());
//				intent.putExtra("questionSelect", question.getQuestionSelect());
//				intent.putExtra("isRight", question.getIsRight());
//				intent.putExtra("Analysis", question.getAnalysis());
//				intent.putExtra("optionA", question.getOptionA());
//				intent.putExtra("optionB", question.getOptionB());
//				intent.putExtra("optionC", question.getOptionC());
//				intent.putExtra("optionD", question.getOptionD());
//				intent.putExtra("optionE", question.getOptionE());
//				intent.putExtra("optionType", question.getOptionType());
				startActivity(intent);
				finish();
			}
		});
		
//		DBManager dbManager = new DBManager(ItemListactivity.this);
//		dbManager.openDB();
//
//		ErrorQuestionInfo[] errorQuestionInfos = dbManager.queryAllData();
//		if (errorQuestionInfos == null) {
//			Toast.makeText(ItemListactivity.this, "暂无数据",
//					Toast.LENGTH_SHORT).show();
//		} else {
//			Map<String, Object> map = null;
//			for (int i = 0; i < ConstantData.answerId.size(); i++) {
//				ErrorQuestion errorQuestion=new ErrorQuestion();
//				map = new HashMap<String, Object>();
//				map.put("title", ConstantData.);// 标题
//				map.put("type", errorQuestionInfos[i].questionType);// 标题
//				map.put("answer", errorQuestionInfos[i].questionAnswer);// 标题
//				map.put("isright", errorQuestionInfos[i].isRight);// 
//				map.put("selected", errorQuestionInfos[i].questionSelect);// 
//				map.put("analysis", errorQuestionInfos[i].Analysis);// 
//				data.add(map);
//				
//				errorQuestion.setQuestionName(errorQuestionInfos[i].questionName);
//				errorQuestion.setQuestionType(errorQuestionInfos[i].questionType);
//				errorQuestion.setQuestionAnswer(errorQuestionInfos[i].questionAnswer);
//				errorQuestion.setQuestionSelect(errorQuestionInfos[i].questionSelect);
//				errorQuestion.setIsRight(errorQuestionInfos[i].isRight);
//				errorQuestion.setAnalysis(errorQuestionInfos[i].Analysis);
//				errorQuestion.setOptionA(errorQuestionInfos[i].optionA);
//				errorQuestion.setOptionB(errorQuestionInfos[i].optionB);
//				errorQuestion.setOptionC(errorQuestionInfos[i].optionC);
//				errorQuestion.setOptionD(errorQuestionInfos[i].optionD);
//				errorQuestion.setOptionE(errorQuestionInfos[i].optionE);
//				errorQuestion.setOptionType(errorQuestionInfos[i].optionType);
//				list.add(errorQuestion);
//			}
				
				Iterator<String> iter1 = ConstantData.answerId.iterator();
				 Iterator<String> iter2 = ConstantData.answerName.iterator();
				 Iterator<String> iter3 = ConstantData.answerOptionA.iterator();
				 Iterator<String> iter4 = ConstantData.answerOptionB.iterator();
				 Iterator<String> iter5 = ConstantData.answerOptionC.iterator();
				 Iterator<String> iter6 = ConstantData.answerOptionD.iterator();
//				 Iterator<String> iter7 = ConstantData.isSelect.iterator();
				 int i = 0;
				 Map<String, Object> map = null;
				 while(iter1.hasNext()&&iter2.hasNext()&&iter3.hasNext()&&iter4.hasNext()&&iter5.hasNext()&&iter6.hasNext()){  
					 
					 map = new HashMap<String, Object>();
					 map.put("title", iter2.next());// 标题
//					 map.put("selected", iter7.next());// 
					 data.add(map);
						
					 i++;
			        }
				 
				 
		}

	}

