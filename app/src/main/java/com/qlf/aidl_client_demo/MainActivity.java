package com.qlf.aidl_client_demo;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.os.RemoteException;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import java.util.List;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{
    private EditText mEditText;
    //由AIDL文件生成的java类
    private MessageCenter messageCenter = null;

    //判断当前与服务端链接状况的值,false 未连接
    private boolean mBound = false;

    //包含Info对象的List
    private List<Info> mInfoList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mEditText = findViewById(R.id.editText);
        findViewById(R.id.button).setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.button:
                String content = mEditText.getText().toString();
                addMessage(content);
                break;
        }
    }

    /**
     * 调用服务端的addINfo方法
     * @param content
     */
    private void addMessage(String content) {
        //如果与服务器处于未连接状态，则尝试链接
        if (!mBound){
            attemptToBindService();
            Toast.makeText(this, "当前与服务器处于未连接状态，正在尝试链接，请稍后重试", Toast.LENGTH_SHORT).show();
            return;
        }

        if (messageCenter == null){
            return;
        }

        Info info = new Info();
        info.setContent(content);
        try {
            messageCenter.addInfo(info);
            Log.i(getLocalClassName(), "客户端:" + info.toString());
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    /**
     * 尝试与服务器建立连接
     */
    private void attemptToBindService() {
        Intent intent = new Intent();
        intent.setAction("com.vvv.aidl");
        intent.setPackage("com.qlf.aidl_service_demo");
 //       startService(intent);
        bindService(intent,mServiceConnection,Context.BIND_AUTO_CREATE);
    }

    private ServiceConnection mServiceConnection = new ServiceConnection() {

        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            messageCenter = MessageCenter.Stub.asInterface(service);
            mBound = true;

            if (messageCenter != null){
                try {
                    mInfoList = messageCenter.getInfo();
                    Log.i(getLocalClassName(), mInfoList.toString());
                } catch (RemoteException e) {
                    e.printStackTrace();
                }
            }
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            Log.i(getLocalClassName(), "service disconnected");
            mBound = false;
        }
    };


    @Override
    protected void onStart() {
        super.onStart();
        if (!mBound){
            attemptToBindService();
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (mBound){
            unbindService(mServiceConnection);
            mBound = false;
        }
    }
}
