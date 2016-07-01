package com.deltaww.smarthome;

import android.app.Activity;
import android.app.TabActivity;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TabHost;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DisplayActivityTabHost extends TabActivity {
    private static final String TAG = "DisplayActivityTabHost";

    private Handler mHandler;
    private int[][] mCheckBoxState;

    //所有变量信息
    private String[] mAllNames;
    private String[] mAllValues = new String[8];
    private int[] mAllImages = new int[]{0, 0, R.drawable.switch_off, 0, 0, R.drawable.light_off, R.drawable.light_off, R.drawable.sound_off};

    //记录每个房间列表项的数量
    private int[] itemNumbers = new int[4];

    //每个房间对应的ListView组件
    private List<ListView> lists;
    //每个ListView组件对应的Adapter
    private List<SimpleAdapter> adapters;
    //存放每个adapter对应的List<Map<String, Object>>数据
    private List<List<Map<String, Object>>> listItemsList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_tabhost);

        TabHost tabHost = getTabHost();
        TabHost.TabSpec tab1 = tabHost.newTabSpec("tab1").setIndicator("房间1").setContent(R.id.tablist1);
        tabHost.addTab(tab1);
        TabHost.TabSpec tab2 = tabHost.newTabSpec("tab2").setIndicator("房间2").setContent(R.id.tablist2);
        tabHost.addTab(tab2);
        TabHost.TabSpec tab3 = tabHost.newTabSpec("tab3").setIndicator("房间3").setContent(R.id.tablist3);
        tabHost.addTab(tab3);
        TabHost.TabSpec tab4 = tabHost.newTabSpec("tab4").setIndicator("房间4").setContent(R.id.tablist4);
        tabHost.addTab(tab4);

        initView();

        mHandler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                if (msg.what == 0x012) {
                    byte[] bytes = (byte[]) msg.obj;
                    //转换成字符串输出到Log
                    StringBuilder stringBuilder = new StringBuilder();
                    for (int i = 0; i < 8; i++) {
                        stringBuilder.append(Integer.toHexString(bytes[i] & 0xFF) + " ");
                    }
                    Log.d(TAG, "handleMessage: " + stringBuilder.toString());

                    if (bytes[0] == 0x7f) {
                        int address = (int) ((bytes[3] & 0xff) | ((bytes[2] & 0xff) << 8));
//                        Log.d(TAG, "handleMessage: address " + Integer.toHexString(address));
                        int tempValue = (int) ((bytes[5] & 0xff) | ((bytes[4] & 0xff) << 8));
//                        Log.d(TAG, "handleMessage: tempValue " + Integer.toHexString(tempValue));
                        if (bytes[1] == 0x01) {
//                            Log.d(TAG, "handleMessage: tempValue " + tempValue);
                            double tempure = 3435.0 * 298.0 / (3435.0 + 298.0 * Math.log(tempValue / (4096.0 - tempValue))) - 273.0;
                            tempure = tempure * 10;
                            tempure = Math.round(tempure);
//                            Log.d(TAG, "handleMessage: tempure " + tempure);
                            updateData((int) tempure, 0);
                        } else if (bytes[1] == 0x02) {
                            updateData(tempValue, 1);
                        } else if (bytes[1] == 0x03) {
                            if (tempValue == 256) {
                                tempValue = 1;
                            }
                            updateData(tempValue, 2);
                        } else if (bytes[1] == 0x04) {
                            updateData(tempValue, 3);
                        } else if (bytes[1] == 0x11) {
                            updateData(tempValue, 4);
                        } else if (bytes[1] == 0x12) {
                            updateData(tempValue, 5);
                        } else if (bytes[1] == 0x13) {
                            if (tempValue == 256) {
                                tempValue = 1;
                            }
                            updateData(tempValue, 6);
                        } else if (bytes[1] == 0x14) {
                            updateData(tempValue, 7);
                        }
                        Log.d(TAG, "handleMessage: " + Arrays.toString(mAllValues));
                    }
                }
            }
        };

        DataReceiveThread dataReceiveThread = new DataReceiveThread(mHandler);
        new Thread(dataReceiveThread).start();

        Log.d(TAG, "onCreate: finished.");
    }

    private void initView() {
        //初始化所有变量信息
        Resources res = getResources();
        mAllNames = new String[]{res.getString(R.string.variate1), res.getString(R.string.variate2),
                res.getString(R.string.variate3), res.getString(R.string.variate4)
                , res.getString(R.string.variate5), res.getString(R.string.variate6),
                res.getString(R.string.variate7), res.getString(R.string.variate8)};
        for (int i = 0; i < 8; i++) {
            mAllValues[i] = "0";
        }

        //接收MainActivity传过来的CheckBox选中状态的数组mCheckBoxState
        mCheckBoxState = new int[MainActivity.VARIATE_NUMBER][MainActivity.ROOM_NUMBER];
        Intent intent = getIntent();
        for (int i = 0; i < MainActivity.VARIATE_NUMBER; i++) {
            mCheckBoxState[i] = intent.getIntArrayExtra(MainActivity.DATA + i);
//            Log.d(TAG, "mCheckBoxState:" + Arrays.toString(mCheckBoxState[i]));
        }

        lists = new ArrayList<>();
        ListView list1 = (ListView) findViewById(R.id.tablist1);
        lists.add(list1);
        ListView list2 = (ListView) findViewById(R.id.tablist2);
        lists.add(list2);
        ListView list3 = (ListView) findViewById(R.id.tablist3);
        lists.add(list3);
        ListView list4 = (ListView) findViewById(R.id.tablist4);
        lists.add(list4);

        //为每个房间下的ListView配置adapter
        adapters = new ArrayList<>();
        List<Map<String, Object>> listItems;
        listItemsList = new ArrayList<List<Map<String, Object>>>();
        for (int j = 0; j < MainActivity.ROOM_NUMBER; j++) {
            listItems = new ArrayList<Map<String, Object>>();
            int itemNumber = 0;
            for (int i = 0; i < MainActivity.VARIATE_NUMBER; i++) {
                if (mCheckBoxState[i][j] == 1) {
                    Map<String, Object> listItem = new HashMap<>();
                    listItem.put("name", mAllNames[i]);
                    listItem.put("value", mAllValues[i]);
                    listItem.put("sign", mAllImages[i]);
                    listItems.add(listItem);
                    itemNumber++;
                }
            }
            itemNumbers[j] = itemNumber;
            SimpleAdapter adapter = new SimpleAdapter(this, listItems, R.layout.list_item,
                    new String[]{"name", "value", "sign"},
                    new int[]{R.id.list_variate_name, R.id.list_variate_value, R.id.list_variate_sign});
            adapters.add(adapter);
            listItemsList.add(listItems);
            lists.get(j).setAdapter(adapter);
        }
    }

    //int i 表示第i个变量
    private void updateData(int tempValue, int i) {
        if (i == 0) {
            mAllValues[i] = Double.toString(tempValue / 10.0);
        } else {
            mAllValues[i] = Integer.toString(tempValue);
        }
        List<Map<String, Object>> listItems;
        for (int j = 0; j < 4; j++) {
            if (mCheckBoxState[i][j] == 1) {
                //获取每个房间列表的List<Map<String,Object>>对象
                listItems = listItemsList.get(j);
                //获取该变量对应的列表项在对应房间的列表中的位置
                int k = i;
                for (int m = 0; m <= i; m++) {
                    if (mCheckBoxState[m][j] == 0)
                        k--;
                }
                Map<String, Object> listItem = listItems.get(k);
                //重新设置key="value"的Map对象
                listItem.put("value", mAllValues[i]);
                if ((i == 2) && (tempValue == 1)) {
                    listItem.put("sign", R.drawable.switch_on);
                } else if ((i == 2) && (tempValue == 0)) {
                    listItem.put("sign", R.drawable.switch_off);
                }
                if ((i == 5 || i == 6) && (tempValue == 1)) {
                    listItem.put("sign", R.drawable.light_on);
                } else if ((i == 5 || i == 6) && (tempValue == 0)) {
                    listItem.put("sign", R.drawable.light_off);
                }
                if ((i == 7) && (tempValue == 1)) {
                    listItem.put("sign", R.drawable.light_on);
                } else if ((i == 7) && (tempValue == 0)) {
                    listItem.put("sign", R.drawable.light_off);
                }
                //更新每个房间的adapter对应的数据
                adapters.get(j).notifyDataSetChanged();
            }
        }
    }
}
