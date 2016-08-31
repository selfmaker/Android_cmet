package com.example.registerapp.adapter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONObject;

import android.content.Intent;
import android.graphics.Color;
import android.support.v4.view.PagerAdapter;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.style.ForegroundColorSpan;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.registerapp.AnalogyExaminationActivity;
import com.example.registerapp.ItemListactivity;
import com.example.registerapp.JSONParser;
import com.example.registerapp.R;

import com.example.registerapp.adapter.ExaminationSubmitAdapter.ViewHolder;
import com.example.registerapp.bean.AnSwerInfo;
import com.example.registerapp.bean.ErrorQuestionInfo;
import com.example.registerapp.bean.SaveQuestionInfo;
import com.example.registerapp.bean.StudentAnswer;
import com.example.registerapp.database.DBManager;
import com.example.registerapp.utils.ConstantData;
import com.example.registerapp.utils.ConstantUtil;

public class ItemListAdapter extends PagerAdapter {
	
	
	//更改记录：1. 8.26 在单选题里 每一选项都进行了如下更改
	
//注释掉了这些 1,2,3
	
//1(原来每次只能答案只能选一次，现在可再次更改作答选项)	
//	if(map.containsKey(position)){
//	return;
//}
	
//2(注释掉 做错选项显示红色)	
//holder.ivA.setImageResource(R.drawable.ic_practice_test_wrong);
//	holder.tvA.setTextColor(Color.parseColor("#d53235"));
	
//3(注释掉错题解释)
//	holder.wrongLayout.setVisibility(View.VISIBLE);
//	holder.explaindetailTv.setText(""+dataItems.get(position).getAnalysis());

	
	
//增加了下面这些（单选题，选中的颜色变绿，其他则变成普通颜色）：	
//	holder.ivA.setImageResource(R.drawable.ic_practice_test_right);
//	holder.tvA.setTextColor(Color.parseColor("#61bc31"));
//	holder.ivB.setImageResource(R.drawable.ic_practice_test_normal);
//	holder.tvB.setTextColor(Color.parseColor("#9a9a9a"));
//	holder.ivC.setImageResource(R.drawable.ic_practice_test_normal);
//	holder.tvC.setTextColor(Color.parseColor("#9a9a9a"));
//	holder.ivD.setImageResource(R.drawable.ic_practice_test_normal);
//	holder.tvD.setTextColor(Color.parseColor("#9a9a9a"));
//	holder.ivE.setImageResource(R.drawable.ic_practice_test_normal);
//	holder.tvE.setTextColor(Color.parseColor("#9a9a9a"));
	

	//新增一个记录学生答案的java
	StudentAnswer stuAnswer =new StudentAnswer();
	private String url = "http://202.38.70.138/cmetTest/compete.php?username="+ConstantUtil.username+"&question_id=";

	
	
	AnalogyExaminationActivity mContext;
	// 传递过来的页面view的集合
	List<View> viewItems;
	// 每个item的页面view
	View convertView;
	// 传递过来的所有数据
	List<AnSwerInfo> dataItems;
	
	String imgServerUrl="";

	private Map<Integer, Boolean> map = new HashMap<Integer, Boolean>();
	private Map<Integer, Boolean> mapClick = new HashMap<Integer, Boolean>();
	private Map<Integer, String> mapMultiSelect = new HashMap<Integer, String>();
	
	boolean isClick=false;
	
	boolean isNext = false;
	
	StringBuffer answer=new StringBuffer();
	StringBuffer answerLast=new StringBuffer();
	StringBuffer answer1=new StringBuffer();
	
	DBManager dbManager;
	
	String isCorrect=ConstantUtil.isCorrect;//1对，0错
	
	int errortopicNum=0;
	
	String resultA="";
	String resultB="";
	String resultC="";
	String resultD="";
	String resultE="";

	public ItemListAdapter(AnalogyExaminationActivity context, List<View> viewItems, List<AnSwerInfo> dataItems,String imgServerUrl) {
		mContext = context;
		this.viewItems = viewItems;
		this.dataItems = dataItems;
		this.imgServerUrl = imgServerUrl;
		dbManager = new DBManager(context);
		dbManager.openDB();
	}
	
	public long getItemId(int position) {
		return position;
	}

	@Override
	public void destroyItem(ViewGroup container, int position, Object object) {
		container.removeView(viewItems.get(position));
	}

	@Override
	public Object instantiateItem(ViewGroup container,final int position) {
		final ViewHolder holder = new ViewHolder();
		convertView = viewItems.get(position);
		holder.questionType = (TextView) convertView.findViewById(R.id.activity_prepare_test_no);
		holder.question = (TextView) convertView.findViewById(R.id.activity_prepare_test_question);
		holder.previousBtn = (LinearLayout) convertView.findViewById(R.id.activity_prepare_test_upLayout);
		holder.nextBtn = (LinearLayout) convertView.findViewById(R.id.activity_prepare_test_nextLayout);
		holder.itemsBtn = (LinearLayout) convertView.findViewById(R.id.activity_prepare_test_totalLayout);
		
		holder.nextText = (TextView) convertView.findViewById(R.id.menu_bottom_nextTV);
//		holder.errorBtn =(LinearLayout) convertView.findViewById(R.id.activity_prepare_test_errorLayout);
		holder.totalText = (TextView) convertView.findViewById(R.id.activity_prepare_test_totalTv);
		holder.nextImage = (ImageView) convertView.findViewById(R.id.menu_bottom_nextIV);
		holder.wrongLayout = (LinearLayout) convertView.findViewById(R.id.activity_prepare_test_wrongLayout);
		holder.explaindetailTv = (TextView) convertView.findViewById(R.id.activity_prepare_test_explaindetail);
		holder.layoutA=(LinearLayout) convertView.findViewById(R.id.activity_prepare_test_layout_a);
		holder.layoutB=(LinearLayout) convertView.findViewById(R.id.activity_prepare_test_layout_b);
		holder.layoutC=(LinearLayout) convertView.findViewById(R.id.activity_prepare_test_layout_c);
		holder.layoutD=(LinearLayout) convertView.findViewById(R.id.activity_prepare_test_layout_d);
		holder.layoutE=(LinearLayout) convertView.findViewById(R.id.activity_prepare_test_layout_e);
		holder.ivA=(ImageView) convertView.findViewById(R.id.vote_submit_select_image_a);
		holder.ivB=(ImageView) convertView.findViewById(R.id.vote_submit_select_image_b);
		holder.ivC=(ImageView) convertView.findViewById(R.id.vote_submit_select_image_c);
		holder.ivD=(ImageView) convertView.findViewById(R.id.vote_submit_select_image_d);
		holder.ivE=(ImageView) convertView.findViewById(R.id.vote_submit_select_image_e);
		holder.tvA=(TextView) convertView.findViewById(R.id.vote_submit_select_text_a);
		holder.tvB=(TextView) convertView.findViewById(R.id.vote_submit_select_text_b);
		holder.tvC=(TextView) convertView.findViewById(R.id.vote_submit_select_text_c);
		holder.tvD=(TextView) convertView.findViewById(R.id.vote_submit_select_text_d);
		holder.tvE=(TextView) convertView.findViewById(R.id.vote_submit_select_text_e);
		holder.ivA_=(ImageView) convertView.findViewById(R.id.vote_submit_select_image_a_);
		holder.ivB_=(ImageView) convertView.findViewById(R.id.vote_submit_select_image_b_);
		holder.ivC_=(ImageView) convertView.findViewById(R.id.vote_submit_select_image_c_);
		holder.ivD_=(ImageView) convertView.findViewById(R.id.vote_submit_select_image_d_);
		holder.ivE_=(ImageView) convertView.findViewById(R.id.vote_submit_select_image_e_);
		
		holder.totalText.setText(position+1+"/"+dataItems.size());
		
//		holder.errorBtn.setOnClickListener(new OnClickListener() {
//			
//			@Override
//			public void onClick(View arg0) {
//				// TODO Auto-generated method stub
//				Intent intent=new Intent(mContext,MyErrorQuestionActivity.class);
//				mContext.startActivity(intent);
//			}
//		});
		
		if(dataItems.get(position).getOptionA().equals("")){
			holder.layoutA.setVisibility(View.GONE);
		}if(dataItems.get(position).getOptionB().equals("")){
			holder.layoutB.setVisibility(View.GONE);
		}if(dataItems.get(position).getOptionC().equals("")){
			holder.layoutC.setVisibility(View.GONE);
		}if(dataItems.get(position).getOptionD().equals("")){
			holder.layoutD.setVisibility(View.GONE);
		}if(dataItems.get(position).getOptionE().equals("")){
			holder.layoutE.setVisibility(View.GONE);
		}
		
		//判断是否文字图片题目
			//文字题目
			holder.ivA_.setVisibility(View.GONE);
			holder.ivB_.setVisibility(View.GONE);
			holder.ivC_.setVisibility(View.GONE);
			holder.ivD_.setVisibility(View.GONE);
			holder.ivE_.setVisibility(View.GONE);
			holder.tvA.setVisibility(View.VISIBLE);
			holder.tvB.setVisibility(View.VISIBLE);
			holder.tvC.setVisibility(View.VISIBLE);
			holder.tvD.setVisibility(View.VISIBLE);
			holder.tvE.setVisibility(View.VISIBLE);
			holder.tvA.setText("A." + dataItems.get(position).getOptionA());
			holder.tvB.setText("B." + dataItems.get(position).getOptionB());
			holder.tvC.setText("C." + dataItems.get(position).getOptionC());
			holder.tvD.setText("D." + dataItems.get(position).getOptionD());
			holder.tvE.setText("E." + dataItems.get(position).getOptionE());
		
			
			
			
		//判断题型
		if(dataItems.get(position).getQuestionType().equals("0")){
			//单选题
			
			holder.question.setText("(单选题)"+dataItems.get(position).getQuestionName());
			
			if(!ConstantData.isSelect.containsKey(position)){
				switch (ConstantData.isSelect.get(position)) {
				
				case "0":
					holder.ivA.setImageResource(R.drawable.ic_practice_test_right);
					holder.tvA.setTextColor(Color.parseColor("#61bc31"));
					holder.ivB.setImageResource(R.drawable.ic_practice_test_normal);
					holder.tvB.setTextColor(Color.parseColor("#9a9a9a"));
					holder.ivC.setImageResource(R.drawable.ic_practice_test_normal);
					holder.tvC.setTextColor(Color.parseColor("#9a9a9a"));
					holder.ivD.setImageResource(R.drawable.ic_practice_test_normal);
					holder.tvD.setTextColor(Color.parseColor("#9a9a9a"));
					holder.ivE.setImageResource(R.drawable.ic_practice_test_normal);
					holder.tvE.setTextColor(Color.parseColor("#9a9a9a"));
					
					break;
				case "1":
					holder.ivB.setImageResource(R.drawable.ic_practice_test_right);
					holder.tvB.setTextColor(Color.parseColor("#61bc31"));
					holder.ivA.setImageResource(R.drawable.ic_practice_test_normal);
					holder.tvA.setTextColor(Color.parseColor("#9a9a9a"));
					holder.ivC.setImageResource(R.drawable.ic_practice_test_normal);
					holder.tvC.setTextColor(Color.parseColor("#9a9a9a"));
					holder.ivD.setImageResource(R.drawable.ic_practice_test_normal);
					holder.tvD.setTextColor(Color.parseColor("#9a9a9a"));
					holder.ivE.setImageResource(R.drawable.ic_practice_test_normal);
					holder.tvE.setTextColor(Color.parseColor("#9a9a9a"));
					
					break;
					
				case "2":
					holder.ivC.setImageResource(R.drawable.ic_practice_test_right);
					holder.tvC.setTextColor(Color.parseColor("#61bc31"));
					holder.ivA.setImageResource(R.drawable.ic_practice_test_normal);
					holder.tvA.setTextColor(Color.parseColor("#9a9a9a"));
					holder.ivB.setImageResource(R.drawable.ic_practice_test_normal);
					holder.tvB.setTextColor(Color.parseColor("#9a9a9a"));
					
					holder.ivD.setImageResource(R.drawable.ic_practice_test_normal);
					holder.tvD.setTextColor(Color.parseColor("#9a9a9a"));
					holder.ivE.setImageResource(R.drawable.ic_practice_test_normal);
					holder.tvE.setTextColor(Color.parseColor("#9a9a9a"));
					
					break;
					
				case "3":
					holder.ivD.setImageResource(R.drawable.ic_practice_test_right);
					holder.tvD.setTextColor(Color.parseColor("#61bc31"));
					holder.ivA.setImageResource(R.drawable.ic_practice_test_normal);
					holder.tvA.setTextColor(Color.parseColor("#9a9a9a"));
					holder.ivB.setImageResource(R.drawable.ic_practice_test_normal);
					holder.tvB.setTextColor(Color.parseColor("#9a9a9a"));
					holder.ivC.setImageResource(R.drawable.ic_practice_test_normal);
					holder.tvC.setTextColor(Color.parseColor("#9a9a9a"));
					
					holder.ivE.setImageResource(R.drawable.ic_practice_test_normal);
					holder.tvE.setTextColor(Color.parseColor("#9a9a9a"));
					
					break;
					

				default:
					break;
				}
				
			}
			
			
			holder.layoutA.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View arg0) {
				
					holder.ivA.setImageResource(R.drawable.ic_practice_test_right);
					holder.tvA.setTextColor(Color.parseColor("#61bc31"));
					holder.ivB.setImageResource(R.drawable.ic_practice_test_normal);
					holder.tvB.setTextColor(Color.parseColor("#9a9a9a"));
					holder.ivC.setImageResource(R.drawable.ic_practice_test_normal);
					holder.tvC.setTextColor(Color.parseColor("#9a9a9a"));
					holder.ivD.setImageResource(R.drawable.ic_practice_test_normal);
					holder.tvD.setTextColor(Color.parseColor("#9a9a9a"));
					holder.ivE.setImageResource(R.drawable.ic_practice_test_normal);
					holder.tvE.setTextColor(Color.parseColor("#9a9a9a"));

					//保存数据
					SaveQuestionInfo questionInfo=new SaveQuestionInfo();
					questionInfo.setQuestionId(dataItems.get(position).getQuestionId());
					questionInfo.setQuestionType(dataItems.get(position).getQuestionType());
					questionInfo.setRealAnswer(dataItems.get(position).getCorrectAnswer());
					questionInfo.setScore(dataItems.get(position).getScore());
					questionInfo.setIs_correct(isCorrect);
					mContext.questionInfos.add(questionInfo);
					dataItems.get(position).setIsSelect("0");
					
					//保存学生作答了此题的记录
					if(!ConstantData.isSelect.containsKey(position)){
						
//						ConstantData.isSelect.add(""+position);
					}

					//保存学生作答的答案
					stuAnswer.setQuestion_id(dataItems.get(position).getQuestionId());
					stuAnswer.setAnswer("1");
					
					List<NameValuePair> params = new ArrayList<NameValuePair>();
		              params.add(new BasicNameValuePair("username", ConstantUtil.username));
		              params.add(new BasicNameValuePair("question_id", dataItems.get(position).getQuestionId()));
		              params.add(new BasicNameValuePair("answer", "1"));
		              
		              JSONParser jsonParser = new JSONParser();
		              
		              try{   
//		                  JSONObject json = jsonParser.makeHttpRequest(url,"POST", params);
		                  String jsonstring = jsonParser.makeHttpRequest(url+dataItems.get(position).getQuestionId(),"POST", params);
		                  JSONObject json = new JSONObject(jsonstring);
		                  Log.i("main",json.toString());
		                  if(json.getString("responseCode").equals("success")){
		                	  
		                	  //上传成功
		                	
		                  }
//		                  Log.i("main",json);
		                  Log.v("uploadsucceed", "uploadsucceed");   
		                
		              }catch(Exception e){   
		                  e.printStackTrace(); 
		              }   
		              
				}
			});
			holder.layoutB.setOnClickListener(new OnClickListener() {
							
							@Override
							public void onClick(View arg0) {
//								if(map.containsKey(position)){
//									return;
//								}
								map.put(position, true);
								if(dataItems.get(position).getCorrectAnswer().contains("B")){
									mContext.setCurrentView(position+1);
									holder.ivB.setImageResource(R.drawable.ic_practice_test_right);
									holder.tvB.setTextColor(Color.parseColor("#61bc31"));
									isCorrect=ConstantUtil.isCorrect;
								}else{
									isCorrect=ConstantUtil.isError;
									errortopicNum+=1;
									//自动添加错误题目
									ErrorQuestionInfo errorQuestionInfo=new ErrorQuestionInfo();
									errorQuestionInfo.setQuestionName(dataItems.get(position).getQuestionName());
									errorQuestionInfo.setQuestionType(dataItems.get(position).getQuestionType());
									errorQuestionInfo.setQuestionAnswer(dataItems.get(position).getCorrectAnswer());
									errorQuestionInfo.setIsRight(isCorrect);
									errorQuestionInfo.setQuestionSelect("B");
									errorQuestionInfo.setAnalysis(dataItems.get(position).getAnalysis());
									errorQuestionInfo.setOptionType(dataItems.get(position).getOption_type());
									if(dataItems.get(position).getOption_type().equals("0")){
										errorQuestionInfo.setOptionA(dataItems.get(position).getOptionA());
										errorQuestionInfo.setOptionB(dataItems.get(position).getOptionB());
										errorQuestionInfo.setOptionC(dataItems.get(position).getOptionC());
										errorQuestionInfo.setOptionD(dataItems.get(position).getOptionD());
										errorQuestionInfo.setOptionE(dataItems.get(position).getOptionE());
									}else{
										errorQuestionInfo.setOptionA(dataItems.get(position).getOptionA().equals("")?"":imgServerUrl+dataItems.get(position).getOptionA());
										errorQuestionInfo.setOptionB(dataItems.get(position).getOptionB().equals("")?"":imgServerUrl+dataItems.get(position).getOptionB());
										errorQuestionInfo.setOptionC(dataItems.get(position).getOptionC().equals("")?"":imgServerUrl+dataItems.get(position).getOptionC());
										errorQuestionInfo.setOptionD(dataItems.get(position).getOptionD().equals("")?"":imgServerUrl+dataItems.get(position).getOptionD());
										errorQuestionInfo.setOptionE(dataItems.get(position).getOptionE().equals("")?"":imgServerUrl+dataItems.get(position).getOptionE());
									}
//									long colunm=dbManager.insertErrorQuestion(errorQuestionInfo);
//									
//									if(colunm == -1)
//									{
//										Toast.makeText(mContext, "添加错误", Toast.LENGTH_SHORT).show();
//									}
									
//									holder.ivB.setImageResource(R.drawable.ic_practice_test_wrong);
//									holder.tvB.setTextColor(Color.parseColor("#d53235"));
									holder.ivB.setImageResource(R.drawable.ic_practice_test_right);
									holder.tvB.setTextColor(Color.parseColor("#61bc31"));
									holder.ivA.setImageResource(R.drawable.ic_practice_test_normal);
									holder.tvA.setTextColor(Color.parseColor("#9a9a9a"));
									holder.ivC.setImageResource(R.drawable.ic_practice_test_normal);
									holder.tvC.setTextColor(Color.parseColor("#9a9a9a"));
									holder.ivD.setImageResource(R.drawable.ic_practice_test_normal);
									holder.tvD.setTextColor(Color.parseColor("#9a9a9a"));
									holder.ivE.setImageResource(R.drawable.ic_practice_test_normal);
									holder.tvE.setTextColor(Color.parseColor("#9a9a9a"));
									//提示
//									holder.wrongLayout.setVisibility(View.VISIBLE);
//									holder.explaindetailTv.setText(""+dataItems.get(position).getAnalysis());
									//显示正确选项
									if(dataItems.get(position).getCorrectAnswer().contains("A")){
										holder.ivA.setImageResource(R.drawable.ic_practice_test_right);
										holder.tvA.setTextColor(Color.parseColor("#61bc31"));
									}else if(dataItems.get(position).getCorrectAnswer().contains("B")){
										holder.ivB.setImageResource(R.drawable.ic_practice_test_right);
										holder.tvB.setTextColor(Color.parseColor("#61bc31"));
									}else if(dataItems.get(position).getCorrectAnswer().contains("C")){
										holder.ivC.setImageResource(R.drawable.ic_practice_test_right);
										holder.tvC.setTextColor(Color.parseColor("#61bc31"));
									}else if(dataItems.get(position).getCorrectAnswer().contains("D")){
										holder.ivD.setImageResource(R.drawable.ic_practice_test_right);
										holder.tvD.setTextColor(Color.parseColor("#61bc31"));
									}else if(dataItems.get(position).getCorrectAnswer().contains("E")){
										holder.ivE.setImageResource(R.drawable.ic_practice_test_right);
										holder.tvE.setTextColor(Color.parseColor("#61bc31"));
									}
								}
								//保存数据
								SaveQuestionInfo questionInfo=new SaveQuestionInfo();
								questionInfo.setQuestionId(dataItems.get(position).getQuestionId());
								questionInfo.setQuestionType(dataItems.get(position).getQuestionType());
								questionInfo.setRealAnswer(dataItems.get(position).getCorrectAnswer());
								questionInfo.setScore(dataItems.get(position).getScore());
								questionInfo.setIs_correct(isCorrect);
								mContext.questionInfos.add(questionInfo);
								dataItems.get(position).setIsSelect("0");
								
								
								//保存学生作答了此题的记录
								if(!ConstantData.isSelect.containsKey(position)){
									
//									ConstantData.isSelect.add(""+position);
								}

								
								
								//保存学生作答的答案
								stuAnswer.setQuestion_id(dataItems.get(position).getQuestionId());
								stuAnswer.setAnswer("2");
								
								List<NameValuePair> params = new ArrayList<NameValuePair>();
					              params.add(new BasicNameValuePair("username", ConstantUtil.username));
					              params.add(new BasicNameValuePair("question_id", dataItems.get(position).getQuestionId()));
					              params.add(new BasicNameValuePair("answer", "2"));
					              
					              JSONParser jsonParser = new JSONParser();
					              
					              try{   
//					                  JSONObject json = jsonParser.makeHttpRequest(url,"POST", params);
					                  String jsonstring = jsonParser.makeHttpRequest(url+dataItems.get(position).getQuestionId(),"POST", params);
					                  JSONObject json = new JSONObject(jsonstring);
					                  Log.i("main",json.toString());
					                  if(json.getString("responseCode").equals("success")){
					                	  
					                	  //上传成功
					                	
					                  }
//					                  Log.i("main",json);
					                  Log.v("uploadsucceed", "uploadsucceed");   
					                
					              }catch(Exception e){   
					                  e.printStackTrace(); 
					              }   
					              
					              
							}
						});
			holder.layoutC.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View arg0) {
//					if(map.containsKey(position)){
//						return;
//					}
					map.put(position, true);
					if(dataItems.get(position).getCorrectAnswer().contains("C")){
						mContext.setCurrentView(position+1);
						holder.ivC.setImageResource(R.drawable.ic_practice_test_right);
						holder.tvC.setTextColor(Color.parseColor("#61bc31"));
						isCorrect=ConstantUtil.isCorrect;
					}else{
						isCorrect=ConstantUtil.isError;
						errortopicNum+=1;
						//自动添加错误题目
						ErrorQuestionInfo errorQuestionInfo=new ErrorQuestionInfo();
						errorQuestionInfo.setQuestionName(dataItems.get(position).getQuestionName());
						errorQuestionInfo.setQuestionType(dataItems.get(position).getQuestionType());
						errorQuestionInfo.setQuestionAnswer(dataItems.get(position).getCorrectAnswer());
						errorQuestionInfo.setIsRight(isCorrect);
						errorQuestionInfo.setQuestionSelect("C");
						errorQuestionInfo.setAnalysis(dataItems.get(position).getAnalysis());
						errorQuestionInfo.setOptionType(dataItems.get(position).getOption_type());
						if(dataItems.get(position).getOption_type().equals("0")){
							errorQuestionInfo.setOptionA(dataItems.get(position).getOptionA());
							errorQuestionInfo.setOptionB(dataItems.get(position).getOptionB());
							errorQuestionInfo.setOptionC(dataItems.get(position).getOptionC());
							errorQuestionInfo.setOptionD(dataItems.get(position).getOptionD());
							errorQuestionInfo.setOptionE(dataItems.get(position).getOptionE());
						}else{
							errorQuestionInfo.setOptionA(dataItems.get(position).getOptionA().equals("")?"":imgServerUrl+dataItems.get(position).getOptionA());
							errorQuestionInfo.setOptionB(dataItems.get(position).getOptionB().equals("")?"":imgServerUrl+dataItems.get(position).getOptionB());
							errorQuestionInfo.setOptionC(dataItems.get(position).getOptionC().equals("")?"":imgServerUrl+dataItems.get(position).getOptionC());
							errorQuestionInfo.setOptionD(dataItems.get(position).getOptionD().equals("")?"":imgServerUrl+dataItems.get(position).getOptionD());
							errorQuestionInfo.setOptionE(dataItems.get(position).getOptionE().equals("")?"":imgServerUrl+dataItems.get(position).getOptionE());
						}
//						long colunm=dbManager.insertErrorQuestion(errorQuestionInfo);
//						
//						if(colunm == -1)
//						{
//							Toast.makeText(mContext, "添加错误", Toast.LENGTH_SHORT).show();
//						}
						
//						holder.ivC.setImageResource(R.drawable.ic_practice_test_wrong);
//						holder.tvC.setTextColor(Color.parseColor("#d53235"));
						holder.ivC.setImageResource(R.drawable.ic_practice_test_right);
						holder.tvC.setTextColor(Color.parseColor("#61bc31"));
						holder.ivA.setImageResource(R.drawable.ic_practice_test_normal);
						holder.tvA.setTextColor(Color.parseColor("#9a9a9a"));
						holder.ivB.setImageResource(R.drawable.ic_practice_test_normal);
						holder.tvB.setTextColor(Color.parseColor("#9a9a9a"));
						
						holder.ivD.setImageResource(R.drawable.ic_practice_test_normal);
						holder.tvD.setTextColor(Color.parseColor("#9a9a9a"));
						holder.ivE.setImageResource(R.drawable.ic_practice_test_normal);
						holder.tvE.setTextColor(Color.parseColor("#9a9a9a"));
						//提示
//						holder.wrongLayout.setVisibility(View.VISIBLE);
//						holder.explaindetailTv.setText(""+dataItems.get(position).getAnalysis());
						//显示正确选项
						if(dataItems.get(position).getCorrectAnswer().contains("A")){
							holder.ivA.setImageResource(R.drawable.ic_practice_test_right);
							holder.tvA.setTextColor(Color.parseColor("#61bc31"));
						}else if(dataItems.get(position).getCorrectAnswer().contains("B")){
							holder.ivB.setImageResource(R.drawable.ic_practice_test_right);
							holder.tvB.setTextColor(Color.parseColor("#61bc31"));
						}else if(dataItems.get(position).getCorrectAnswer().contains("C")){
							holder.ivC.setImageResource(R.drawable.ic_practice_test_right);
							holder.tvC.setTextColor(Color.parseColor("#61bc31"));
						}else if(dataItems.get(position).getCorrectAnswer().contains("D")){
							holder.ivD.setImageResource(R.drawable.ic_practice_test_right);
							holder.tvD.setTextColor(Color.parseColor("#61bc31"));
						}else if(dataItems.get(position).getCorrectAnswer().contains("E")){
							holder.ivE.setImageResource(R.drawable.ic_practice_test_right);
							holder.tvE.setTextColor(Color.parseColor("#61bc31"));
						}
					}
					//保存数据
					SaveQuestionInfo questionInfo=new SaveQuestionInfo();
					questionInfo.setQuestionId(dataItems.get(position).getQuestionId());
					questionInfo.setQuestionType(dataItems.get(position).getQuestionType());
					questionInfo.setRealAnswer(dataItems.get(position).getCorrectAnswer());
					questionInfo.setScore(dataItems.get(position).getScore());
					questionInfo.setIs_correct(isCorrect);
					mContext.questionInfos.add(questionInfo);
					dataItems.get(position).setIsSelect("0");
					
					
					//保存学生作答了此题的记录
					if(!ConstantData.isSelect.containsKey(position)){
						
//						ConstantData.isSelect.add(""+position);
					}

					
					
					//保存学生作答的答案
					stuAnswer.setQuestion_id(dataItems.get(position).getQuestionId());
					stuAnswer.setAnswer("3");
					
					List<NameValuePair> params = new ArrayList<NameValuePair>();
		              params.add(new BasicNameValuePair("username", ConstantUtil.username));
		              params.add(new BasicNameValuePair("question_id", dataItems.get(position).getQuestionId()));
		              params.add(new BasicNameValuePair("answer", "3"));
		              
		              JSONParser jsonParser = new JSONParser();
		              
		              try{   
//		                  JSONObject json = jsonParser.makeHttpRequest(url,"POST", params);
		                  String jsonstring = jsonParser.makeHttpRequest(url+dataItems.get(position).getQuestionId(),"POST", params);
		                  JSONObject json = new JSONObject(jsonstring);
		                  Log.i("main",json.toString());
		                  if(json.getString("responseCode").equals("success")){
		                	  
		                	  //上传成功
		                	
		                  }
//		                  Log.i("main",json);
		                  Log.v("uploadsucceed", "uploadsucceed");   
		                
		              }catch(Exception e){   
		                  e.printStackTrace(); 
		              }   
		              
		              
				}
			});
			holder.layoutD.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View arg0) {
//					if(map.containsKey(position)){
//						return;
//					}
					map.put(position, true);
					if(dataItems.get(position).getCorrectAnswer().contains("D")){
						mContext.setCurrentView(position+1);
						holder.ivD.setImageResource(R.drawable.ic_practice_test_right);
						holder.tvD.setTextColor(Color.parseColor("#61bc31"));
						isCorrect=ConstantUtil.isCorrect;
					}else{
						isCorrect=ConstantUtil.isError;
						errortopicNum+=1;
						//自动添加错误题目
						ErrorQuestionInfo errorQuestionInfo=new ErrorQuestionInfo();
						errorQuestionInfo.setQuestionName(dataItems.get(position).getQuestionName());
						errorQuestionInfo.setQuestionType(dataItems.get(position).getQuestionType());
						errorQuestionInfo.setQuestionAnswer(dataItems.get(position).getCorrectAnswer());
						errorQuestionInfo.setIsRight(isCorrect);
						errorQuestionInfo.setQuestionSelect("D");
						errorQuestionInfo.setAnalysis(dataItems.get(position).getAnalysis());
						errorQuestionInfo.setOptionType(dataItems.get(position).getOption_type());
						if(dataItems.get(position).getOption_type().equals("0")){
							errorQuestionInfo.setOptionA(dataItems.get(position).getOptionA());
							errorQuestionInfo.setOptionB(dataItems.get(position).getOptionB());
							errorQuestionInfo.setOptionC(dataItems.get(position).getOptionC());
							errorQuestionInfo.setOptionD(dataItems.get(position).getOptionD());
							errorQuestionInfo.setOptionE(dataItems.get(position).getOptionE());
						}else{
							errorQuestionInfo.setOptionA(dataItems.get(position).getOptionA().equals("")?"":imgServerUrl+dataItems.get(position).getOptionA());
							errorQuestionInfo.setOptionB(dataItems.get(position).getOptionB().equals("")?"":imgServerUrl+dataItems.get(position).getOptionB());
							errorQuestionInfo.setOptionC(dataItems.get(position).getOptionC().equals("")?"":imgServerUrl+dataItems.get(position).getOptionC());
							errorQuestionInfo.setOptionD(dataItems.get(position).getOptionD().equals("")?"":imgServerUrl+dataItems.get(position).getOptionD());
							errorQuestionInfo.setOptionE(dataItems.get(position).getOptionE().equals("")?"":imgServerUrl+dataItems.get(position).getOptionE());
						}
//						long colunm=dbManager.insertErrorQuestion(errorQuestionInfo);
//						
//						if(colunm == -1)
//						{
//							Toast.makeText(mContext, "添加错误", Toast.LENGTH_SHORT).show();
//						}
						
//						holder.ivD.setImageResource(R.drawable.ic_practice_test_wrong);
//						holder.tvD.setTextColor(Color.parseColor("#d53235"));
						holder.ivD.setImageResource(R.drawable.ic_practice_test_right);
						holder.tvD.setTextColor(Color.parseColor("#61bc31"));
						holder.ivA.setImageResource(R.drawable.ic_practice_test_normal);
						holder.tvA.setTextColor(Color.parseColor("#9a9a9a"));
						holder.ivB.setImageResource(R.drawable.ic_practice_test_normal);
						holder.tvB.setTextColor(Color.parseColor("#9a9a9a"));
						holder.ivC.setImageResource(R.drawable.ic_practice_test_normal);
						holder.tvC.setTextColor(Color.parseColor("#9a9a9a"));
						
						holder.ivE.setImageResource(R.drawable.ic_practice_test_normal);
						holder.tvE.setTextColor(Color.parseColor("#9a9a9a"));
						//提示
//						holder.wrongLayout.setVisibility(View.VISIBLE);
//						holder.explaindetailTv.setText(""+dataItems.get(position).getAnalysis());
						//显示正确选项
						if(dataItems.get(position).getCorrectAnswer().contains("A")){
							holder.ivA.setImageResource(R.drawable.ic_practice_test_right);
							holder.tvA.setTextColor(Color.parseColor("#61bc31"));
						}else if(dataItems.get(position).getCorrectAnswer().contains("B")){
							holder.ivB.setImageResource(R.drawable.ic_practice_test_right);
							holder.tvB.setTextColor(Color.parseColor("#61bc31"));
						}else if(dataItems.get(position).getCorrectAnswer().contains("C")){
							holder.ivC.setImageResource(R.drawable.ic_practice_test_right);
							holder.tvC.setTextColor(Color.parseColor("#61bc31"));
						}else if(dataItems.get(position).getCorrectAnswer().contains("D")){
							holder.ivD.setImageResource(R.drawable.ic_practice_test_right);
							holder.tvD.setTextColor(Color.parseColor("#61bc31"));
						}else if(dataItems.get(position).getCorrectAnswer().contains("E")){
							holder.ivE.setImageResource(R.drawable.ic_practice_test_right);
							holder.tvE.setTextColor(Color.parseColor("#61bc31"));
						}
					}
					//保存数据
					SaveQuestionInfo questionInfo=new SaveQuestionInfo();
					questionInfo.setQuestionId(dataItems.get(position).getQuestionId());
					questionInfo.setQuestionType(dataItems.get(position).getQuestionType());
					questionInfo.setRealAnswer(dataItems.get(position).getCorrectAnswer());
					questionInfo.setScore(dataItems.get(position).getScore());
					questionInfo.setIs_correct(isCorrect);
					mContext.questionInfos.add(questionInfo);
					dataItems.get(position).setIsSelect("0");
					
					
					//保存学生作答了此题的记录
					if(!ConstantData.isSelect.containsKey(position)){
						
//						ConstantData.isSelect.add(""+position);
					}

					
					//保存学生作答的答案
					stuAnswer.setQuestion_id(dataItems.get(position).getQuestionId());
					stuAnswer.setAnswer("4");
					
					
					List<NameValuePair> params = new ArrayList<NameValuePair>();
		              params.add(new BasicNameValuePair("username", ConstantUtil.username));
		              params.add(new BasicNameValuePair("question_id", dataItems.get(position).getQuestionId()));
		              params.add(new BasicNameValuePair("answer", "4"));
		              
		              JSONParser jsonParser = new JSONParser();
		              
		              try{   
//		                  JSONObject json = jsonParser.makeHttpRequest(url,"POST", params);
		                  String jsonstring = jsonParser.makeHttpRequest(url+dataItems.get(position).getQuestionId(),"POST", params);
		                  JSONObject json = new JSONObject(jsonstring);
		                  Log.i("main",json.toString());
		                  if(json.getString("responseCode").equals("success")){
		                	  
		                	  //上传成功
		                	
		                  }
//		                  Log.i("main",json);
		                  Log.v("uploadsucceed", "uploadsucceed");   
		                
		              }catch(Exception e){   
		                  e.printStackTrace(); 
		              }   
		              
		              
				}
			});
			
		}
		
		
		ForegroundColorSpan blueSpan = new ForegroundColorSpan(Color.parseColor("#2b89e9"));
		
		SpannableStringBuilder builder1 = new SpannableStringBuilder(holder.question.getText().toString());
		builder1.setSpan(blueSpan, 0, 5, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
		holder.question.setText(builder1);
		
		// 最后一页修改"下一步"按钮文字
		if (position == viewItems.size() - 1) {
			holder.nextText.setText("提交");
			holder.nextImage.setImageResource(R.drawable.vote_submit_finish);
		}
		holder.previousBtn.setOnClickListener(new LinearOnClickListener(position - 1, false,position,holder));
		holder.nextBtn.setOnClickListener(new LinearOnClickListener(position + 1, true,position,holder));
		holder.itemsBtn.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent=new Intent(mContext,ItemListactivity.class);
				mContext.startActivity(intent);
			}
		});
		container.addView(viewItems.get(position));
		return viewItems.get(position);
	}
	
	/**
	 * @author  设置上一步和下一步按钮监听
	 * 
	 */
	class LinearOnClickListener implements OnClickListener {

		private int mPosition;
		private int mPosition1;
		private boolean mIsNext;
		private ViewHolder viewHolder;

		public LinearOnClickListener(int position, boolean mIsNext,int position1,ViewHolder viewHolder) {
			mPosition = position;
			mPosition1 = position1;
			this.viewHolder = viewHolder;
			this.mIsNext = mIsNext;
		}

		@Override
		public void onClick(View v) {
			if (mPosition == viewItems.size()) {
				//单选
				if(dataItems.get(mPosition1).getQuestionType().equals("0")){
//					if(!map.containsKey(mPosition1)){
//						Toast.makeText(mContext, "请选择选项", Toast.LENGTH_SHORT).show();
//						return;
//					}
					mContext.uploadExamination(errortopicNum);
				}
			} else {
				if(mPosition ==-1){
					Toast.makeText(mContext, "已经是第一页", Toast.LENGTH_SHORT).show();
					return;
				}else{
					//单选
					if(dataItems.get(mPosition1).getQuestionType().equals("0")){
//						if(mIsNext){
//							if(!map.containsKey(mPosition1)){
//								Toast.makeText(mContext, "请选择选项", Toast.LENGTH_SHORT).show();
//								return;
//							}
//						}
						isNext = mIsNext;
						mContext.setCurrentView(mPosition);
					}
				}
			}
			
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
	
	//错题数
	public int errorTopicNum(){
		if(errortopicNum!=0){
			return errortopicNum;
		}
		return 0;
	}

	public class ViewHolder {
		TextView questionType;
		TextView question;
		LinearLayout previousBtn, nextBtn,errorBtn,itemsBtn;
		TextView nextText;
		TextView totalText;
		ImageView nextImage;
		LinearLayout wrongLayout;
		TextView explaindetailTv;
		LinearLayout layoutA;
		LinearLayout layoutB;
		LinearLayout layoutC;
		LinearLayout layoutD;
		LinearLayout layoutE;
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
		ImageView ivA_;
		ImageView ivB_;
		ImageView ivC_;
		ImageView ivD_;
		ImageView ivE_;
	}
	
}