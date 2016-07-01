package com.deltaww.smarthome;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "MainActivity";
    public static final String DATA = "CheckBoxState";

    private Button mButtonSelect;
    private Button mButtonDelete;
    private Button mButtonOK;
    private List<List<CheckBox>> checkBoxList;
    //记录CheckBox选中状态的数组，0为未选中，1为选中
    private int[][] mCheckBoxState;

    public static final int ROOM_NUMBER = 4;
    public static final int VARIATE_NUMBER = 8;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initView();

        Log.d(TAG, "onCreate: finished.");
    }

    private void initView() {
        checkBoxList = new ArrayList<>();
        List<CheckBox> variate1 = new ArrayList<CheckBox>();
        variate1.add((CheckBox) findViewById(R.id.variate1_room1));
        variate1.add((CheckBox) findViewById(R.id.variate1_room2));
        variate1.add((CheckBox) findViewById(R.id.variate1_room3));
        variate1.add((CheckBox) findViewById(R.id.variate1_room4));
        checkBoxList.add(variate1);
        List<CheckBox> variate2 = new ArrayList<CheckBox>();
        variate2.add((CheckBox) findViewById(R.id.variate2_room1));
        variate2.add((CheckBox) findViewById(R.id.variate2_room2));
        variate2.add((CheckBox) findViewById(R.id.variate2_room3));
        variate2.add((CheckBox) findViewById(R.id.variate2_room4));
        checkBoxList.add(variate2);
        List<CheckBox> variate3 = new ArrayList<CheckBox>();
        variate3.add((CheckBox) findViewById(R.id.variate3_room1));
        variate3.add((CheckBox) findViewById(R.id.variate3_room2));
        variate3.add((CheckBox) findViewById(R.id.variate3_room3));
        variate3.add((CheckBox) findViewById(R.id.variate3_room4));
        checkBoxList.add(variate3);
        List<CheckBox> variate4 = new ArrayList<CheckBox>();
        variate4.add((CheckBox) findViewById(R.id.variate4_room1));
        variate4.add((CheckBox) findViewById(R.id.variate4_room2));
        variate4.add((CheckBox) findViewById(R.id.variate4_room3));
        variate4.add((CheckBox) findViewById(R.id.variate4_room4));
        checkBoxList.add(variate4);
        List<CheckBox> variate5 = new ArrayList<CheckBox>();
        variate5.add((CheckBox) findViewById(R.id.variate5_room1));
        variate5.add((CheckBox) findViewById(R.id.variate5_room2));
        variate5.add((CheckBox) findViewById(R.id.variate5_room3));
        variate5.add((CheckBox) findViewById(R.id.variate5_room4));
        checkBoxList.add(variate5);
        List<CheckBox> variate6 = new ArrayList<CheckBox>();
        variate6.add((CheckBox) findViewById(R.id.variate6_room1));
        variate6.add((CheckBox) findViewById(R.id.variate6_room2));
        variate6.add((CheckBox) findViewById(R.id.variate6_room3));
        variate6.add((CheckBox) findViewById(R.id.variate6_room4));
        checkBoxList.add(variate6);
        List<CheckBox> variate7 = new ArrayList<CheckBox>();
        variate7.add((CheckBox) findViewById(R.id.variate7_room1));
        variate7.add((CheckBox) findViewById(R.id.variate7_room2));
        variate7.add((CheckBox) findViewById(R.id.variate7_room3));
        variate7.add((CheckBox) findViewById(R.id.variate7_room4));
        checkBoxList.add(variate7);
        List<CheckBox> variate8 = new ArrayList<CheckBox>();
        variate8.add((CheckBox) findViewById(R.id.variate8_room1));
        variate8.add((CheckBox) findViewById(R.id.variate8_room2));
        variate8.add((CheckBox) findViewById(R.id.variate8_room3));
        variate8.add((CheckBox) findViewById(R.id.variate8_room4));
        checkBoxList.add(variate8);

        mButtonSelect = (Button) findViewById(R.id.select_all);
        mButtonDelete = (Button) findViewById(R.id.delete_all);
        mButtonOK = (Button) findViewById(R.id.button_ok);

        mCheckBoxState = new int[VARIATE_NUMBER][ROOM_NUMBER];

        //设置全选按钮的监听事件
        mButtonSelect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                for (int i = 0; i < VARIATE_NUMBER; i++) {
                    for (int j = 0; j < ROOM_NUMBER; j++) {
                        CheckBox temp = checkBoxList.get(i).get(j);
                        if (!temp.isChecked()) {
                            temp.setChecked(true);
                        }
                    }
                }
            }
        });

        //设置清空按钮的点击事件
        mButtonDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                for (int i = 0; i < VARIATE_NUMBER; i++) {
                    for (int j = 0; j < ROOM_NUMBER; j++) {
                        CheckBox temp = checkBoxList.get(i).get(j);
                        if (temp.isChecked()) {
                            temp.setChecked(false);
                        }
                    }
                }
            }
        });

        //设置确定按钮的点击事件
        mButtonOK.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                for (int i = 0; i < VARIATE_NUMBER; i++) {
                    for (int j = 0; j < ROOM_NUMBER; j++) {
                        CheckBox temp = checkBoxList.get(i).get(j);
                        if (temp.isChecked()) {
                            mCheckBoxState[i][j] = 1;
                        } else {
                            mCheckBoxState[i][j] = 0;
                        }
                    }
                    Log.d(TAG, "mCheckBoxState:" + Arrays.toString(mCheckBoxState[i]));
                }
                Intent intent = new Intent(MainActivity.this, DisplayActivityTabHost.class);
                for (int i = 0; i < VARIATE_NUMBER; i++) {
                    intent.putExtra(DATA + i, mCheckBoxState[i]);
                }
                startActivity(intent);
            }
        });
    }
}
