package com.example.dell.jpim;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.jpush.im.android.api.JMessageClient;
import cn.jpush.im.android.api.callback.GetUserInfoCallback;
import cn.jpush.im.android.api.model.UserInfo;
import cn.jpush.im.api.BasicCallback;

public class LoginActivity extends AppCompatActivity {


    @BindView(R.id.name)
    EditText name;
    @BindView(R.id.pwd)
    EditText pwd;
    @BindView(R.id.login)
    TextView login;
    private String username;
    private String password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);

    }


    @OnClick({R.id.login})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.login:
                username = name.getText().toString().trim();
                password  = pwd.getText().toString().trim();
                Login(username,password);
                break;


        }
    }

    private void Login(final String username, final String password) {

        JMessageClient.register(username, password, new BasicCallback() {
            @Override
            public void gotResult(int i, String s) {
                if (i == 0) {

//                    Toast.makeText(LoginActivity.this, "已注册"+s, Toast.LENGTH_SHORT).show();
                    Logins(username, password);

                }else if (i==898001){

                    Logins(username, password);

                }
            }
        });


    }

    private void Logins(final String username, final String password) {
        JMessageClient.login(username, password, new BasicCallback() {
            @Override
            public void gotResult(int responseCode, String responseMessage) {
                if (responseCode == 0) {
//                    SharePreferenceManager.setCachedPsw(password);
//                    UserInfo myInfo = JMessageClient.getMyInfo();
//                    File avatarFile = myInfo.getAvatarFile();
//                    //登陆成功,如果用户有头像就把头像存起来,没有就设置null
//                    if (avatarFile != null) {
//                        SharePreferenceManager.setCachedAvatarPath(avatarFile.getAbsolutePath());
//                    } else {
//                        SharePreferenceManager.setCachedAvatarPath(null);
//                    }
//                    String username = myInfo.getUserName();
//                    String appKey = myInfo.getAppKey();
//                    UserEntry user = UserEntry.getUser(username, appKey);
//                    if (null == user) {
//                        user = new UserEntry(username, appKey);
//                        user.save();
//                    }
//                    初始化用户信息
                    JMessageClient.getUserInfo(username, "7af46ed85fc5e013242cd981", new GetUserInfoCallback() {
                        @Override
                        public void gotResult(int i, String s, UserInfo userInfo) {

                            if (i==0){
                                Toast.makeText(LoginActivity.this, "登录成功！", Toast.LENGTH_SHORT).show();
                                Intent intent=  new Intent(LoginActivity.this, MainActivity.class);
                                intent.putExtra("username", username);
                                startActivity(intent);
                                finish();
                            }
                            Log.v("GZM_getUserInfo","userInfo=="+userInfo);
                        }
                    });


                } else {

                    Toast.makeText(LoginActivity.this, "登录失败！" + responseCode, Toast.LENGTH_SHORT).show();
                }
            }
        });
    }


}
