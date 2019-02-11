package com.example.dell.jpim;

import android.app.Application;
import android.content.Context;

import com.github.yuweiguocn.library.greendao.MigrationHelper;

import org.greenrobot.greendao.query.QueryBuilder;

import java.util.HashSet;
import java.util.Set;

import cn.jpush.im.android.api.JMessageClient;

import static cn.jpush.im.android.api.JMessageClient.FLAG_NOTIFY_SILENCE;


/**
 * Created by Administrator on 2018/1/11.
 */

public class MyApplication extends Application {

    public static MyApplication baseApplication;
    private SharedPrefHelper sharedPrefHelper;
    private MySQLiteOpenHelper helper;
    private DaoMaster master;
    private MyApplication mContext;

    public void onCreate() {
        super.onCreate();

        baseApplication = this;
        sharedPrefHelper = SharedPrefHelper.getInstance();
        sharedPrefHelper.setRoaming(true);
        //开启极光调试

        //初始化sdk
        JMessageClient.setDebugMode(true);//正式版的时候设置false，关闭调试
        mContext = MyApplication.this;
        JMessageClient.init(this);
        //实例化极光IM,并自动同步聊天记录
        JMessageClient.init(getApplicationContext(), true);
        JMessageClient.setDebugMode(true);
        //初始化数据库
        setupDatabase();
        //通知管理,通知栏开启，其他关闭
        JMessageClient.setNotificationFlag(FLAG_NOTIFY_SILENCE);
        //建议添加tag标签，发送消息的之后就可以指定tag标签来发送了
//        Set<String> set = new HashSet<>();
//        set.add("andfixdemo");//名字任意，可多添加几个
//        JMessageClient.setTags(this, set, null);//设置标签
    }
    private void setupDatabase() {
        //是否开启调试
        MigrationHelper.DEBUG = true;
        QueryBuilder.LOG_SQL = true;
        QueryBuilder.LOG_VALUES = true;
        //数据库升级
        helper = new MySQLiteOpenHelper(mContext, "text");
        master = new DaoMaster(helper.getWritableDb());

    }

}
