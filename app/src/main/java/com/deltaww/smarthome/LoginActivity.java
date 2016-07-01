package com.deltaww.smarthome;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Toast;

/**
 * SharedPreferences文件存储格式
 * 文件名：USER_INFO = "userInfo"
 * key:RememberChecked 记住密码复选框状态 value:boolean
 * key:AutoLoginChecked 自动登录复选框状态 value:boolean
 * key:user_name 用户名  value:String
 * key:password 密码  value:String
 * */
public class LoginActivity extends AppCompatActivity {

    public static final String USER_INFO = "userInfo";

    private AutoCompleteTextView mViewUserName;
    private EditText mViewPassword;
    private CheckBox mRememberPassword;
    private CheckBox mAutoLogin;
    private Button mButtonLogin;

    //用户名和密码信息
    private String mUserName;
    private String mPassword;

    private SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        initView();

        sharedPreferences = getSharedPreferences(USER_INFO, Context.MODE_PRIVATE);
        if (sharedPreferences.getBoolean("RememberChecked", false)) {
            mRememberPassword.setChecked(true);
            mViewUserName.setText(sharedPreferences.getString("user_name", ""));
            mViewPassword.setText(sharedPreferences.getString("password", ""));
            if (sharedPreferences.getBoolean("AutoLoginChecked", false)) {
                mAutoLogin.setChecked(true);
                Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                startActivity(intent);
            }
        }
    }

    private void initView() {
        mViewUserName = (AutoCompleteTextView) findViewById(R.id.user_name);
        mViewPassword = (EditText) findViewById(R.id.password);
        mRememberPassword = (CheckBox) findViewById(R.id.remember_password);
        mAutoLogin = (CheckBox) findViewById(R.id.auto_login);
        mButtonLogin = (Button) findViewById(R.id.login_button);

        //设置登录按钮的监听事件
        mButtonLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mUserName=mViewUserName.getText().toString();
                mPassword=mViewPassword.getText().toString();

                if(mUserName.equals("abc")&&mPassword.equals("123")){
                    Toast.makeText(LoginActivity.this,"登录成功",Toast.LENGTH_SHORT).show();

                    //登录成功并且记住密码框为选中状态时才保存用户信息
                    if (mRememberPassword.isChecked()){
                        //记住用户名、密码
                        SharedPreferences.Editor editor=sharedPreferences.edit();
                        editor.putString("user_name",mUserName);
                        editor.putString("password",mPassword);
                        editor.commit();
                    }

                    Intent intent=new Intent(LoginActivity.this,MainActivity.class);
                    LoginActivity.this.startActivity(intent);
                }else {
                    Toast.makeText(LoginActivity.this,"用户名或密码错误，请重新登录！",Toast.LENGTH_LONG).show();
                }
            }
        });

        //设置记住密码多选框事件监听器
        mRememberPassword.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (mRememberPassword.isChecked()){
                    sharedPreferences.edit().putBoolean("RememberChecked",true).commit();
                }else {
                    sharedPreferences.edit().putBoolean("RememberChecked",false).commit();
                }
            }
        });

        //设置自动登录多选框事件监听器
        mAutoLogin.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if(mAutoLogin.isChecked()){
                    sharedPreferences.edit().putBoolean("AutoLoginChecked",true).commit();
                }else {
                    sharedPreferences.edit().putBoolean("AutoLoginChecked",false).commit();
                }
            }
        });
    }
}
