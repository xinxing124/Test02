package com.example.test02;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Random;

import org.achartengine.ChartFactory;
import org.achartengine.GraphicalView;
import org.achartengine.chart.PointStyle;
import org.achartengine.model.XYMultipleSeriesDataset;
import org.achartengine.model.XYSeries;
import org.achartengine.renderer.XYMultipleSeriesRenderer;
import org.achartengine.renderer.XYSeriesRenderer;

import android.os.Bundle;
import android.os.Environment;
import android.provider.ContactsContract;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Paint.Align;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.Toast;
import android.widget.FrameLayout.LayoutParams;

public class OpenActivity extends Activity {
	Button btnclose;
	Button btnfileopen;
	int num=0;
	static int MaxLen=1002;//202;
	double m[][];//=new double[202];
	double n[][];//new double[202];
	XYMultipleSeriesDataset dataset=new XYMultipleSeriesDataset();
	XYMultipleSeriesRenderer renderer = new XYMultipleSeriesRenderer();
	LinearLayout linechart;
	double list[];
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_open);
		
		 //����ID�ҵ�RadioGroupʵ��
		RadioGroup group = (RadioGroup)this.findViewById(R.id.radioGroup1);
		//��һ������������
        group.setOnCheckedChangeListener(new OnCheckedChangeListener() {
            
            @Override
            public void onCheckedChanged(RadioGroup arg0, int arg1) {
                // TODO Auto-generated method stub
                //��ȡ������ѡ�����ID
                int radioButtonId = arg0.getCheckedRadioButtonId();
                switch (radioButtonId)
                {
                case R.id.radio0:
                	//RadioButton rb = (RadioButton)OpenActivity.this.findViewById(radioButtonId);
                	//rb.getText();
                	SetXYSeries(linechart,dataset, renderer,m,true,num);
                	break;
                case R.id.radio1:
                	SetXYSeries(linechart,dataset, renderer,n,true,num);
                	break;
                }
                
                //����ID��ȡRadioButton��ʵ��
                //RadioButton rb = (RadioButton)MyActiviy.this.findViewById(radioButtonId);
                //�����ı����ݣ��Է���ѡ����
                //tv.setText("�����Ա��ǣ�" + rb.getText());
            }
        });
		
		linechart = (LinearLayout) findViewById(R.id.linechart);
		SetXYSeries(linechart,dataset,renderer, m,false,1);
		btnclose=(Button)findViewById(R.id.btnclose);
		btnclose.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View v) {
				//Toast.makeText(getApplicationContext(), "�����˳�", Toast.LENGTH_SHORT).show();
				finish();
//				Intent intent = new Intent();  //����Ҫ�������ǿ��Կ��ڱ��Activity�����õ���ʼ����ֵ�������޸ĺ��
//		        intent.setClass(MainActivity.this, SecondActivity.class);
//		        //startActivity(intent);
//		        startActivityForResult(intent,0);
			}
        });
        
		btnfileopen=(Button)findViewById(R.id.btnfileopen);
		btnfileopen.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View v) {
				//Intent intent = new Intent();  //����Ҫ�������ǿ��Կ��ڱ��Activity�����õ���ʼ����ֵ�������޸ĺ��
		        //intent.setClass(OpenActivity.this, SelectActivity.class);
		        //startActivity(intent);
		        //startActivity(new Intent(OpenActivity.this, SelectActivity.class));
				//startActivityForResult(new Intent(OpenActivity.this, SelectActivity.class),101);
				 //Intent intent=new Intent(Intent.ACTION_PICK,ContactsContract.Contacts.CONTENT_URI);
				Intent intent=new Intent(OpenActivity.this, SelectActivity.class);
				 startActivityForResult(intent,101);
				//Toast.makeText(getApplicationContext(), "���ļ�", Toast.LENGTH_SHORT).show();
			}
        });
		
//		XYSeries series;
//		XYSeriesRenderer xyRenderer;
//		int lines,num;
//		//dataset= new XYMultipleSeriesDataset();
//		
//        Random r = new Random();
//        double OverrunX[][]={{500,500},{500,500},{500,500},{500,500}};
//    	double OverrunY[][]={{0.3,0.1},{0.1,-0.2},{0.3,0},{0.3,-0.3}};
//
//    	lines=3;
//    	
//    	if((num=dataset.getSeriesCount())>0)
//    	{
//    		//for(int i=0;i<num;i++)
//    			dataset.clear();
//    			//Log.v("random", "TT:"+Integer.toString(dataset.getSeriesCount())); 
//    		
//    	}
//    	//Log.v("random", Integer.toString(num)); 
//    		//dataset.clear();
//    	
//	        for (int i = 0; i < lines; i++) {
//	            // ע��,�����������XYSeries ��һ�㲻ͬ!!����ʹ��CategroySeries
//	            //CategorySeries series = new CategorySeries("test" + (i + 1));
//	        	//series = new XYSeries("test" +i+Type*3);
//	        	series = new XYSeries("test");
//	        	if(i<2)
//	        	{
//	        		series.clear();
//	        		series.add(0,0);
//	        		//series.add(OverrunX[Type][i],OverrunY[Type][i]);
//	        		series.add(1000,0);
//	        		
//	        	}
//	        	else
//	        	{
////	        		if(ValueFlag)
////	        		{
////	        			series.clear();
////		                // �������
////		                for (int k = 0,l=0; k < 1005; k+=5,l++) {
////		                    // ֱ��������Ҫ��ʾ������,��:Y���ֵ
////		                	//series.add(k,Math.abs(1 + r.nextInt() % 100));
////		                	//series.add(k, r.nextDouble()/8-0.05);
////		                	//series.add(k, roundDouble(r.nextDouble()/8-0.05,3));
////		                	
////		                	series.add(k, XYValue[l]);
////	
////		                }
////	        		}
//	        	}
//	        	
//	            // ����Ҫ����ת��
//	        	dataset.addSeries(series);
//	        	
//	        	//series.clear();
//	        }
//	        //Log.v("random", "TT:"+Integer.toString(dataset.getSeriesCount()));
//	        //Log.v("random", "add"); 
//        // 1, ������ʾ����Ⱦͼ
//        //renderer = new XYMultipleSeriesRenderer();
//	    if((num=renderer.getSeriesRendererCount())>0)
//	    {
//	    	//for(int i=0;i<num;i++)
//	    		renderer.removeAllRenderers();
//	    		 //Log.v("random", "TT1:"+Integer.toString(renderer.getSeriesRendererCount())); 
//	    }
//	    //Log.v("random", "TT2:"+Integer.toString(renderer.getSeriesRendererCount())); 
//	    
//        renderer.setApplyBackgroundColor(true);
//        renderer.setBackgroundColor(Color.argb(0, 0, 0, 0));
//        
//	        for(int i=0;i<lines;i++)
//	        {
//	        	// 3, �Ե�Ļ��ƽ�������
//	        	xyRenderer = new XYSeriesRenderer();
//	        	if(i<2)
//	        	{
//	        	// 3.1������ɫ
//	        	xyRenderer.setColor(Color.RED);
//	        	// 3.2���õ����ʽ
//	        	//xyRenderer.setPointStyle(PointStyle.SQUARE);
//	        	}
//	        	else
//	        	{
////	        		if(ValueFlag)
////	        		{
////	        		xyRenderer.setColor(Color.BLUE);
////	        		
////	        		}
//	        	}
//	        	// 3.3, ��Ҫ���Ƶĵ���ӵ����������
//	        	renderer.addSeriesRenderer(xyRenderer);
//	        	 
//	        }
//	        //Log.v("random", "TT3:"+Integer.toString(renderer.getSeriesRendererCount())); 
//        
//	        /*
//            SimpleSeriesRenderer ssr = renderer.getSeriesRendererAt(2);
//            // ��֪�����ߵľ�������ô�����,Ĭ����Align.CENTER,���Ƕ����������ϵ�������ʾ
//            // �ͻ��������ұ�
//            ssr.setChartValuesTextAlign(Align.RIGHT);
//            ssr.setChartValuesTextSize(12);
//            ssr.setDisplayChartValues(true);
//            */
//        
//        // ע������x,y min ��Ҫ��ͬ
//        // ������һ�����õ�����x,y��Χ�ķ���
//        
//        //˳����:minX, maxX, minY, maxY
//        //double[] range = { 0, 1000, -0.5, 0.5 };
//        //renderer.setRange(range);
//        
//        // �ȼ���:
//        // -------------------
//        // renderer.setXAxisMin(0);
//        // renderer.setXAxisMax(10);
//        // renderer.setYAxisMin(1);
//        // renderer.setYAxisMax(200);
//        // -------------------
// 
//        // ���ú��ʵĿ̶�,��������ʾ�������� MAX / labels
//        renderer.setXLabels(21);
//        renderer.setYLabels(10);
//        //renderer.setXLabels(0); //����X�᲻��ʾ���֣������Զ����ֵ��  
//        //renderer.addTextLabel(1, "����"); //����X������1��ʾ��ֵ
//        
//        // ����x,y����ʾ������,Ĭ���� Align.CENTER
//        renderer.setXLabelsAlign(Align.CENTER);
//        renderer.setYLabelsAlign(Align.RIGHT);
//        
//        //renderer.setBarSpacing(1);//���ü��
//        //renderer.setXLabels(0);//���� X �᲻��ʾ���֣����������ֶ���ӵ����ֱ�ǩ����;//����X����ʾ�Ŀ̶ȱ�ǩ�ĸ���
//        //renderer.setYLabels(15);// ���ú��ʵĿ̶ȣ���������ʾ�������� MAX / labels
//        //renderer.setMargins(new int[] { 40, 40, 40, 40 });//ͼ�� 4 �߾� ����4������  ����ͼ�����߿�
//        //renderer.setYLabelsAlign(Align.RIGHT);//����y����ʾ�ķ��У�Ĭ���� Align.CENTER
//        //renderer.setPanEnabled(false,true);//����x������Ի�����y���򲻿��Ի���
//        //renderer.setZoomEnabled(false,false);//����x��y���򶼲����ԷŴ����С
//        
//        //renderer.setDisplayValues(true);
//        //renderer.setDisplayChartValuesDistance(30);
//        //renderer.setBarSpacing(1);//���ü��
//        renderer.setBarSpacing(20);//���ü��
//        //renderer.setGridColor(Color.RED);
//        //renderer.setXLabelsColor(Color.BLACK);
//        //renderer.setYLabelsColor(0, Color.BLACK);
//        // ����������,�����ɫ
//        //renderer.setAxesColor(Color.RED);
//        //renderer.setBackgroundColor(Color.BLACK);
//        
//        // ��ʾ����
//        renderer.setShowGrid(true);
//        // ����x,y���ϵĿ̶ȵ���ɫ
//        renderer.setLabelsColor(Color.RED);
// 
//        // ����ҳ�߿հ׵���ɫ
//        renderer.setMarginsColor(Color.BLACK);
//        // �����Ƿ���ʾ,���������,Ĭ��Ϊ true
//        renderer.setShowAxes(true);
//        //renderer.setShowLabels(false);
//        renderer.setShowLegend(false);
//      
//        //GraphicalView chartView= ChartFactory.getLineChartView(this, dataset,renderer);
//
//        //chart.addView(chartView, new LayoutParams(LayoutParams.FILL_PARENT,LayoutParams.WRAP_CONTENT));
//        //chartView.invalidate();
//
//        GraphicalView chartView= ChartFactory.getLineChartView(this, dataset,renderer);
//        chart.removeAllViewsInLayout();  
//        chart.addView(chartView, new LayoutParams(LayoutParams.FILL_PARENT,LayoutParams.WRAP_CONTENT));
//        chartView.invalidate();
//        chart.invalidate();

	}
	
	private void SetXYSeries(LinearLayout chart,XYMultipleSeriesDataset dataset,XYMultipleSeriesRenderer renderer,double Values[][],boolean ValueFlag,int lines){
		XYSeries series;
		XYSeriesRenderer xyRenderer;

		int num;
		int jump=1;
    	if((num=dataset.getSeriesCount())>0) dataset.clear();
        for (int i = 0; i < lines; i++) {
        	series = new XYSeries("test");
        	if(ValueFlag)
        	{
        		series.clear();
                //for (int k = 0,l=0; k < 1005; k+=5,l++)
        		if(MaxLen<1000) jump=5;
        		for (int k = 0,l=0; k < MaxLen-1; k++,l++)
                {
                	//series.add(k, XYValue[l]);
                	series.add(k*jump, Values[i][l]);
                }
        	}
        	else
        	{
        		series.clear();
                //for (int k = 0,l=0; k < 1005; k+=5,l++) {
                //	series.add(k, XYValue[l]);
                //}
        	}
        	dataset.addSeries(series);
        }
        
	    if((num=renderer.getSeriesRendererCount())>0) renderer.removeAllRenderers();
	    
        renderer.setApplyBackgroundColor(true);
        renderer.setBackgroundColor(Color.argb(0, 0, 0, 0));
        for(int i=0;i<lines;i++)
        {
        	// 3, �Ե�Ļ��ƽ�������
        	xyRenderer = new XYSeriesRenderer();
        	if(ValueFlag)
        	{	switch(i)
        		{
	        	case 0:
	        		xyRenderer.setColor(Color.RED);
	        		break;
	        	case 1:
	        		xyRenderer.setColor(Color.YELLOW  );
	        		break;
	        	case 2:
	        		xyRenderer.setColor(Color.BLUE  );
	        		break;
	        	case 3:
	        		xyRenderer.setColor(Color.GREEN);
	        		break;
	        	case 4:
	        		xyRenderer.setColor(Color.CYAN );
	        		break;
	        	case 5:
	        		xyRenderer.setColor(Color.MAGENTA);
	        		break;
        		}
        		
        	}

        	//xyRenderer.setPointStyle(PointStyle.SQUARE);
        	// 3.3, ��Ҫ���Ƶĵ���ӵ����������
        	renderer.addSeriesRenderer(xyRenderer);
        } 
        
        // ע������x,y min ��Ҫ��ͬ
        // ������һ�����õ�����x,y��Χ�ķ���
        
        //˳����:minX, maxX, minY, maxY
        //double[] range = { 0, 1000, -0.5, 0.5 };
        //renderer.setRange(range);
        
        // �ȼ���:
        // -------------------
         renderer.setXAxisMin(0);
         renderer.setXAxisMax(1000);
        // renderer.setYAxisMin(1);
        // renderer.setYAxisMax(200);
        // -------------------
 
        // ���ú��ʵĿ̶�,��������ʾ�������� MAX / labels
        renderer.setXLabels(21);
        renderer.setYLabels(10);
        //renderer.setXLabels(0); //����X�᲻��ʾ���֣������Զ����ֵ��  
        //renderer.addTextLabel(1, "����"); //����X������1��ʾ��ֵ
        
        // ����x,y����ʾ������,Ĭ���� Align.CENTER
        renderer.setXLabelsAlign(Align.CENTER);
        renderer.setYLabelsAlign(Align.RIGHT);
        
        //renderer.setBarSpacing(1);//���ü��
        //renderer.setXLabels(0);//���� X �᲻��ʾ���֣����������ֶ���ӵ����ֱ�ǩ����;//����X����ʾ�Ŀ̶ȱ�ǩ�ĸ���
        //renderer.setYLabels(15);// ���ú��ʵĿ̶ȣ���������ʾ�������� MAX / labels
        //renderer.setMargins(new int[] { 40, 40, 40, 40 });//ͼ�� 4 �߾� ����4������  ����ͼ�����߿�
        //renderer.setYLabelsAlign(Align.RIGHT);//����y����ʾ�ķ��У�Ĭ���� Align.CENTER
        //renderer.setPanEnabled(false,true);//����x������Ի�����y���򲻿��Ի���
        //renderer.setZoomEnabled(false,false);//����x��y���򶼲����ԷŴ����С
        
		// ������Ⱦ����ʾ���Ű�ť
		renderer.setZoomButtonsVisible(true);
		// ������Ⱦ������Ŵ���С
		renderer.setZoomEnabled(true);
		renderer.setClickEnabled(true);//����ͼ���Ƿ�������
		renderer.setLabelsTextSize(18.0f);
        renderer.setDisplayChartValues(true);
        renderer.setChartValuesTextSize(18.0f);
        
        //renderer.setDisplayValues(true);
        //renderer.setDisplayChartValuesDistance(30);
        renderer.setBarSpacing(1);//���ü��
        //renderer.setBarSpacing(20);//���ü��
        //renderer.setGridColor(Color.RED);
        //renderer.setXLabelsColor(Color.BLACK);
        //renderer.setYLabelsColor(0, Color.BLACK);
        // ����������,�����ɫ
        //renderer.setAxesColor(Color.RED);
        //renderer.setBackgroundColor(Color.BLACK);
        
        // ��ʾ����
        renderer.setShowGrid(true);
        // ����x,y���ϵĿ̶ȵ���ɫ
        renderer.setLabelsColor(Color.RED);
 
        // ����ҳ�߿հ׵���ɫ
        renderer.setMarginsColor(Color.BLACK);
        // �����Ƿ���ʾ,���������,Ĭ��Ϊ true
        renderer.setShowAxes(true);
        //renderer.setShowLabels(false);
        renderer.setShowLegend(false);
      
        //GraphicalView chartView= ChartFactory.getLineChartView(this, dataset,renderer);

        //chart.addView(chartView, new LayoutParams(LayoutParams.FILL_PARENT,LayoutParams.WRAP_CONTENT));
        //chartView.invalidate();

        GraphicalView chartView= ChartFactory.getLineChartView(this, dataset,renderer);
        chart.removeAllViewsInLayout();  
        chart.addView(chartView, new LayoutParams(LayoutParams.FILL_PARENT,LayoutParams.WRAP_CONTENT));
        chartView.invalidate();
        chart.invalidate();
        
	}

	//����Activity������Activity���ؽ��ʱ�����Ļص�����
	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data){
	if(resultCode==RESULT_OK){
	Bundle bundle=data.getExtras();
	//Toast.makeText(this, bundle.getString("selectcount")+"/"+bundle.getString("pwd"), Toast.LENGTH_LONG);

		num=bundle.getInt("selectcount");
		//double m[][]=new double[num][201];
		//double n[][]=new double[num][201];
		/*
		m=new double[num][MaxLen];
		n=new double[num][MaxLen];

		for(int i=0;i<num;i++)
		{
			//btnfileopen.setText(bundle.getString("Item" +i));
			FiletoDoublesEx(bundle.getString("Item" +i),m[i],n[i]);
		}
*/
		m=new double[num][];
		n=new double[num][];

		for(int i=0;i<num;i++)
		{
			//btnfileopen.setText(bundle.getString("Item" +i));
			FiletoDoublesEx_1(bundle.getString("Item" +i),i);
		}
		SetXYSeries(linechart,dataset, renderer,m,true,num);
	}
	super.onActivityResult(requestCode, resultCode, data);
	}
	public boolean DoublestoFile(String filename,double m[],double n[]){
		try {
			//File sdCardDir = Environment.getExternalStorageDirectory();  //�õ�SD����Ŀ¼
			//File BuildDir = new File(sdCardDir, "/data");   //��dataĿ¼���粻����������
			//if(BuildDir.exists()==false)BuildDir.mkdirs();
			//File saveFile =new File(BuildDir, filename);  //�½��ļ���������Ѵ������½��ĵ�
			String status = Environment.getExternalStorageState(); 
			// �ж�SD���Ƿ���� 
			if (status.equals(Environment.MEDIA_MOUNTED)) { 
				String dir=Environment.getExternalStorageDirectory()+"/ANC";
			java.io.File a=new java.io.File(dir);
			if (!a.exists())a.mkdir();
			File file=new File(a,filename);
			
			//File f = new File(filename);
			FileOutputStream stream = new FileOutputStream(file);  //���ļ�������
			String wmsg="";
			for(int i=0;i<MaxLen-1;i++){
				wmsg+=m[i];
				if(i==MaxLen-2)
					wmsg+="\r\n";
				else
					wmsg+=";";
			}
			for(int i=MaxLen,j=0;i<(MaxLen*2-1);i++,j++){
				wmsg+=n[j];
				if(i==(MaxLen*2-2))
					wmsg+="\r\n";
				else
					wmsg+=";";
			}

			stream.write(wmsg.getBytes());
			stream.close();
			}
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return true;
	}
	public boolean FiletoDoubles(String filename,double m[],double n[])
	{
		try {
			String status = Environment.getExternalStorageState(); 
			// �ж�SD���Ƿ���� 
			if (status.equals(Environment.MEDIA_MOUNTED)) { 
			//	String dir=Environment.getExternalStorageDirectory()+"/data";
			//java.io.File a=new java.io.File(dir);
			//if (!a.exists())a.mkdir();
			//File file=new File(a,filename);
			File file=new File(filename);
			if(!file.exists()) return false;
			
			  //File f = new File(filename);
			  //if(!f.exists()) return false;
			  FileInputStream fis = new FileInputStream(file);
			  byte[] b = new byte[(int)file.length()];
			  fis.read(b);
			  fis.close();
			  
			  String bb = new String(b);
			  String cc[]=bb.split("\n");
			  
			  for(int i=2;i<cc.length;i++)
			  {
			   //System.out.print(b[i]);
				  String dd[]=cc[i].split(";");
				  for(int j=0;j<dd.length;j++)
				  {
					  if(i==2)
						  m[j]=Double.parseDouble(dd[j]);
					  else
						  n[j]=Double.parseDouble(dd[j]);
				  }
			  }
			}  
//		  String bb = new String(b);
//		  System.out.println(bb);
//		  boolean a = true;
//		  //File ff = new File("d://temp//newread2.txt");
//		  //FileOutputStream fos = new FileOutputStream(ff,a);
//		  //fos.write(b);
//		  String abc = "abc";
//		  byte[] ab = abc.getBytes();
//		  //fos.write(ab);
//		  
//		  //fos.close();
		} catch (NumberFormatException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return true;  
		 
	}
	public boolean FiletoDoublesEx(String filename,double m[],double n[])
	{
		try {
			String status = Environment.getExternalStorageState(); 
			// �ж�SD���Ƿ���� 
			if (status.equals(Environment.MEDIA_MOUNTED)) { 
			//	String dir=Environment.getExternalStorageDirectory()+"/data";
			//java.io.File a=new java.io.File(dir);
			//if (!a.exists())a.mkdir();
			//File file=new File(a,filename);
			File file=new File(filename);
			if(!file.exists()) return false;
			
			  //File f = new File(filename);
			  //if(!f.exists()) return false;
			  FileInputStream fis = new FileInputStream(file);
			  byte[] b = new byte[(int)file.length()];
			  fis.read(b);
			  fis.close();
			  
			  String bb = new String(b);
			  String cc[]=bb.split("\n");
			  MaxLen=cc.length-3;
			  for(int i=2;i<cc.length;i++)
			  {
			   //System.out.print(b[i]);
				  String dd[]=cc[i].split(",");
				  
				  m[i-2]=Double.parseDouble(dd[0]);
				  n[i-2]=Double.parseDouble(dd[1]);
				  
				  /*for(int j=0;j<dd.length;j++)
				  {
					  if(i==2)
						  m[j]=Double.parseDouble(dd[j]);
					  else
						  n[j]=Double.parseDouble(dd[j]);
				  }*/
			  }
			}  
//		  String bb = new String(b);
//		  System.out.println(bb);
//		  boolean a = true;
//		  //File ff = new File("d://temp//newread2.txt");
//		  //FileOutputStream fos = new FileOutputStream(ff,a);
//		  //fos.write(b);
//		  String abc = "abc";
//		  byte[] ab = abc.getBytes();
//		  //fos.write(ab);
//		  
//		  //fos.close();
		} catch (NumberFormatException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return true;  
		 
	}
	public boolean FiletoDoublesEx_1(String filename,int num)
	{
		try {
			String status = Environment.getExternalStorageState(); 
			// �ж�SD���Ƿ���� 
			if (status.equals(Environment.MEDIA_MOUNTED)) { 
			//	String dir=Environment.getExternalStorageDirectory()+"/data";
			//java.io.File a=new java.io.File(dir);
			//if (!a.exists())a.mkdir();
			//File file=new File(a,filename);
			File file=new File(filename);
			if(!file.exists()) return false;
			
			  //File f = new File(filename);
			  //if(!f.exists()) return false;
			  FileInputStream fis = new FileInputStream(file);
			  byte[] b = new byte[(int)file.length()];
			  fis.read(b);
			  fis.close();
			  
			  String bb = new String(b);
			  String cc[]=bb.split("\n");
			  
			  MaxLen=cc.length-2;
			  m[num]=new double[MaxLen];
			  n[num]=new double[MaxLen];
			  for(int i=2;i<cc.length;i++)
			  {
			   //System.out.print(b[i]);
				  String dd[]=cc[i].split(",");
				  
				  m[num][i-2]=Double.parseDouble(dd[0]);
				  n[num][i-2]=Double.parseDouble(dd[1]);
				  
				  /*for(int j=0;j<dd.length;j++)
				  {
					  if(i==2)
						  m[j]=Double.parseDouble(dd[j]);
					  else
						  n[j]=Double.parseDouble(dd[j]);
				  }*/
			  }
			}  
//		  String bb = new String(b);
//		  System.out.println(bb);
//		  boolean a = true;
//		  //File ff = new File("d://temp//newread2.txt");
//		  //FileOutputStream fos = new FileOutputStream(ff,a);
//		  //fos.write(b);
//		  String abc = "abc";
//		  byte[] ab = abc.getBytes();
//		  //fos.write(ab);
//		  
//		  //fos.close();
		} catch (NumberFormatException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return true;  
		 
	}
}
