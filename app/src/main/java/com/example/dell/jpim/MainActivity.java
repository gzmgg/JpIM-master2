package com.example.dell.jpim;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bartoszlipinski.recyclerviewheader2.RecyclerViewHeader;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.jpush.im.android.api.JMessageClient;
import cn.jpush.im.android.api.content.PromptContent;
import cn.jpush.im.android.api.content.TextContent;
import cn.jpush.im.android.api.enums.ContentType;
import cn.jpush.im.android.api.event.MessageEvent;
import cn.jpush.im.android.api.model.Conversation;
import cn.jpush.im.android.api.model.Message;
import cn.jpush.im.android.api.model.UserInfo;

public class MainActivity extends AppCompatActivity {

    @BindView(R.id.fragment_main_rv)
    RecyclerView fragmentMainRv;
    @BindView(R.id.fragment_main_none)
    TextView fragmentMainNone;
    @BindView(R.id.fragment_main_group)
    RelativeLayout fragmentMainGroup;
    @BindView(R.id.fragment_main_header)
    RecyclerViewHeader fragmentMainHeader;
    private List<MessageBean> data = new ArrayList<>();
    private List<Conversation> list = new ArrayList<>();
    private MessageRecyclerAdapter adapter;
    Handler handler = new Handler();
    private MessageBean bean;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
//        Intent intent = getIntent();
//        String name = intent.getStringExtra("username");
        JMessageClient.registerEventReceiver(this);
        list = JMessageClient.getConversationList();
        initView();
    }


    /*接收消息*/
    public void onEvent(final MessageEvent event) {
        final Message msg = event.getMessage();
        this.runOnUiThread(new Runnable() {
            @Override
            public void run() {
//                Log.e("Log:新消息", "消息啊" + msg.getContentType().name() + "\n" + msg);

                if (JMessageClient.getMyInfo().getUserName() == "1006" || JMessageClient.getMyInfo().getUserName().equals("1006")) {

                    final Message message1 =
                            JMessageClient.createSingleTextMessage(((UserInfo)msg.getTargetInfo()).getUserName(), SharedPrefHelper.getInstance().getAppKey(), "[自动回复]你好，我是机器人");
//                    for (int i=0;i<list.size();i++){
//                        conversation = list.get(i);
//                        Message message=conversation.createSendMessage(new TextContent("[自动回复]你好，我是机器人"));
                    JMessageClient.sendMessage(message1);
//                    }
                }
                updataData();

            }
        });

    }
    private void updataData() {
        data.clear();
        adapter.clear();
        initDataBean();
    }

    private void initView() {
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                updataData();
            }
        }, 2000);
//        initRefresh();
        initData();
//        initGroup();
        onClickItem();
    }

    private void initData() {

        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        fragmentMainRv.setLayoutManager(layoutManager);
        adapter = new MessageRecyclerAdapter(data, this);
        //分割线
        fragmentMainRv.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));
        fragmentMainRv.setAdapter(adapter);
        fragmentMainHeader.attachTo(fragmentMainRv);

    }

    /*监听item*/
    private void onClickItem() {
        adapter.setOnItemClickListener(new MessageRecyclerAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                if (view != null) {
//                    Intent intent = new Intent(getActivity(), ChatMsgActivity.class);
//                    intent.putExtra("USERNAME", data.get(position).getUserName());
//                    intent.putExtra("NAKENAME", data.get(position).getTitle());
//                    intent.putExtra("MSGID", data.get(position).getMsgID());
//                    intent.putExtra("position", position);
////                    intent.putExtra("bean", data.get(position));
//                    startActivity(intent);
                }
            }

            @Override
            public void onItemLongClick(View view, final int position) {
                String[] strings = {"删除会话"};
                MyAlertDialog dialog = new MyAlertDialog(MainActivity.this, strings,
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                if (i == 0) {
                                    JMessageClient.deleteSingleConversation(data.get(position).getUserName());
                                    data.remove(position);
                                    adapter.notifyDataSetChanged();
                                    Toast.makeText(MainActivity.this, "删除成功", Toast.LENGTH_SHORT).show();

                                }
                            }
                        });
                dialog.initDialog();

            }
        });
    }


    private void initDataBean() {
        list = JMessageClient.getConversationList();
//        conversation=list.get()
        Log.e("Log:会话消息数", list.size() + "");
        if (list.size() <= 0) {
            fragmentMainNone.setVisibility(View.VISIBLE);
            fragmentMainRv.setVisibility(View.GONE);
        } else {
            fragmentMainNone.setVisibility(View.VISIBLE);
            fragmentMainRv.setVisibility(View.GONE);
            for (int i = 0; i < list.size(); i++) {
                bean = new MessageBean();
                try {
                    //这里进行撤回消息的判断
//                    Log.e("type", list.get(i).getTitle()+","+list.get(i).getLatestMessage().getContent().getContentType());
                    if (list.get(i).getLatestMessage().getContent().getContentType() == ContentType.prompt) {
                        bean.setContent(((PromptContent) (list.get(i).getLatestMessage()).getContent()).getPromptText());
                    } else {
                        bean.setContent(((TextContent) (list.get(i).getLatestMessage()).getContent()).getText());
                    }
                } catch (Exception e) {
                    bean.setContent("最近没有消息！");
                    Log.e("Exception:MessageFM", e.getMessage());
                }
                bean.setMsgID(list.get(i).getId());
                bean.setUserName(((UserInfo) list.get(i).getTargetInfo()).getUserName());
                bean.setTitle(list.get(i).getTitle());
                bean.setTime(list.get(i).getUnReadMsgCnt() + "");
                bean.setConversation(list.get(i));
//                Log.e("Log:Conversation", list.get(i).getAllMessage()+"");

                try {
                    bean.setImg(list.get(i).getAvatarFile().toURI() + "");
                } catch (Exception e) {
                }
                data.add(bean);
            }
        }

        adapter.notifyDataSetChanged();
    }

}
