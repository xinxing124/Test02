package com.example.test02;

import java.io.File;
import java.util.ArrayList;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

public class SelectActivity extends Activity {

	private ListView lv;
	private MyAdapter mAdapter;
	private ArrayList<String> list;
	private Button bt_selectall;
	private Button bt_cancel;
	private Button bt_deselectall;
	private Button bt_okselect;
	private int checkNum; // ��¼ѡ�е���Ŀ����
	private TextView tv_show;// ������ʾѡ�е���Ŀ����

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_select);

		lv = (ListView) findViewById(R.id.lv);
		bt_selectall = (Button) findViewById(R.id.bt_selectall);
		bt_okselect = (Button) findViewById(R.id.bt_okselect);
		bt_cancel = (Button) findViewById(R.id.bt_cancelselectall);
		bt_deselectall = (Button) findViewById(R.id.bt_deselectall);
		tv_show = (TextView) findViewById(R.id.tv);
		list = new ArrayList<String>();
		// ΪAdapter׼������
		initDate();
		// ʵ�����Զ����MyAdapter
		mAdapter = new MyAdapter(list, this);
		// ��Adapter
		lv.setAdapter(mAdapter);

		// ��listView�ļ�����
		lv.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,long arg3) {
				//
				// ȡ��ViewHolder����������ʡȥ��ͨ������findViewByIdȥʵ����������Ҫ��cbʵ���Ĳ���
				ViewHolder holder = (ViewHolder) arg1.getTag();
				// �ı�CheckBox��״̬
				holder.cb.toggle();
				// ��CheckBox��ѡ��״����¼����
				MyAdapter.getIsSelected().put(arg2, holder.cb.isChecked());
				// ����ѡ����Ŀ
				if (holder.cb.isChecked() == true) {
					checkNum++;
				} else {
					checkNum--;
				}
				// ��TextView��ʾ
				tv_show.setText("��ѡ��" + checkNum + "��");
				
				if(checkNum>3){
					
				}
			}
		});
		
		// ȫѡ��ť�Ļص��ӿ�
		bt_selectall.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// ����list�ĳ��ȣ���MyAdapter�е�mapֵȫ����Ϊtrue
				for (int i = 0; i < list.size(); i++) {
					MyAdapter.getIsSelected().put(i, true);
				}
				// ������Ϊlist�ĳ���
				checkNum = list.size();
				// ˢ��listview��TextView����ʾ
				dataChanged();

			}
		});
		// ȷ����ť�Ļص��ӿ�
		bt_okselect.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				try {
					//��Ӵ���ȷ������
					Intent data=new Intent();
					Bundle extras = new Bundle();
					//extras.putString("selectcount",Integer.toString(checkNum));
					extras.putInt("selectcount", checkNum);
					for (int i = 0,j=0; i < list.size(); i++) {
						if (MyAdapter.getIsSelected().get(i)) {
							extras.putString("Item"+j, list.get(i));
							j++;
							//MyAdapter.getIsSelected();
							//checkNum--;// ������1
						}
					}
					
					extras.putString("pwd", list.get(0));
					data.putExtras(extras);
					SelectActivity.this.setResult(RESULT_OK, data);//��������
					finish();
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		});

		// ȡ����ť�Ļص��ӿ�
		bt_cancel.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				/*
				// ����list�ĳ��ȣ�����ѡ�İ�ť��Ϊδѡ
				for (int i = 0; i < list.size(); i++) {
					if (MyAdapter.getIsSelected().get(i)) {
						MyAdapter.getIsSelected().put(i, false);
						checkNum--;// ������1
					}
				}
				// ˢ��listview��TextView����ʾ
				dataChanged();
				*/
				finish();
			}
		});

		// ��ѡ��ť�Ļص��ӿ�
		bt_deselectall.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// ����list�ĳ��ȣ�����ѡ����Ϊδѡ��δѡ����Ϊ��ѡ
				for (int i = 0; i < list.size(); i++) {
					if (MyAdapter.getIsSelected().get(i)) {
						MyAdapter.getIsSelected().put(i, false);
						checkNum--;
					} else {
						MyAdapter.getIsSelected().put(i, true);
						checkNum++;
					}

				}
				// ˢ��listview��TextView����ʾ
				dataChanged();
			}
		});

	}

	// ��ʼ������
	private void initDate() {
		for (int i = 0; i < 15; i++) {
			//list.add("data" + "   " + i);
		}
		File sdCardDir = Environment.getExternalStorageDirectory();
		File file = new File(sdCardDir, "/data");   //��dataĿ¼���粻����������
		if(file.exists()==false)file.mkdirs();
		
        File[] files = file.listFiles();
        //readFile(files);
        for(int i=0; i<files.length; i++) {
        	   //System.out.println(files[i].getName());
        		if(files[i].isFile())
        			list.add(files[i].getPath());
        	   //list.add(files[i].getPath() + "   " + files[i].getName());
        		
        }
        
	}

	// ˢ��listview��TextView����ʾ
	private void dataChanged() {
		// ֪ͨlistViewˢ��
		mAdapter.notifyDataSetChanged();
		// TextView��ʾ���µ�ѡ����Ŀ
		tv_show.setText("��ѡ��" + checkNum + "��");
	}

}