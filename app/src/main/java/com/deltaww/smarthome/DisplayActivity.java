package com.deltaww.smarthome;

import android.app.Activity;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DisplayActivity extends Activity {
    private static final String TAG = "DisplayActivity";

    private Handler mHandler;
    private int[][] mCheckBoxState;

    //所有变量信息
    private String[] mAllNames;
    private String[] mAllValues;
    private int[] mAllImages;

    private List<ListView> lists;
    private List<SimpleAdapter> adapters;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display);

        initView();

        mHandler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                if (msg.what == 0x012) {

                }
            }
        };

        DataReceiveThread dataReceiveThread = new DataReceiveThread(mHandler);
        new Thread(dataReceiveThread).start();
    }

    private void initView() {
        //初始化所有变量信息
        Resources res = getResources();
        mAllNames = new String[]{res.getString(R.string.variate1), res.getString(R.string.variate2),
                res.getString(R.string.variate3), res.getString(R.string.variate4)
                , res.getString(R.string.variate5), res.getString(R.string.variate6),
                res.getString(R.string.variate7), res.getString(R.string.variate8)};
        mAllValues = new String[MainActivity.VARIATE_NUMBER];
        mAllImages=new int[MainActivity.VARIATE_NUMBER];

        //接收MainActivity传过来的CheckBox选中状态的数组mCheckBoxState
        mCheckBoxState = new int[MainActivity.VARIATE_NUMBER][MainActivity.ROOM_NUMBER];
        Intent intent = getIntent();
        for (int i = 0; i < MainActivity.VARIATE_NUMBER; i++) {
            mCheckBoxState[i] = intent.getIntArrayExtra(MainActivity.DATA + i);
            Log.d(TAG, "mCheckBoxState:" + Arrays.toString(mCheckBoxState[i]));
        }

        lists=new ArrayList<>();
        ListView list1= (ListView) findViewById(R.id.list1);
        lists.add(list1);
        ListView list2= (ListView) findViewById(R.id.list2);
        lists.add(list2);
        ListView list3= (ListView) findViewById(R.id.list3);
        lists.add(list3);
        ListView list4= (ListView) findViewById(R.id.list4);
        lists.add(list4);

        //为每个房间下的ListView配置adapter
        adapters=new ArrayList<>();
        for (int j = 0; j < MainActivity.ROOM_NUMBER; j++) {
            List<Map<String, Object>> listItems = new ArrayList<Map<String, Object>>();
            for (int i = 0; i < MainActivity.VARIATE_NUMBER; i++) {
                if (mCheckBoxState[i][j] == 1) {
                    Map<String, Object> listItem = new HashMap<>();
                    listItem.put("name", mAllNames[i]);
                    listItem.put("value", mAllValues[i]);
                    listItem.put("sign", mAllImages[i]);
                    listItems.add(listItem);
                }
            }
            SimpleAdapter adapter = new SimpleAdapter(this, listItems, R.layout.list_item,
                    new String[]{"name", "value", "sign"},
                    new int[]{R.id.list_variate_name, R.id.list_variate_value, R.id.list_variate_sign});
            lists.get(j).setAdapter(adapter);
            adapters.add(adapter);
        }
    }
}
