package com.lisheny.aidl;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.os.RemoteException;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import java.util.List;

public class MainActivity extends AppCompatActivity {

    private IMyBookmanager myBookmanager;
    private List<Book> mBooks;

    private Button btnGet,btnSet;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Intent serviceIntent = new Intent();
        serviceIntent.setAction("com.lisheny.service"); //识别服务
        serviceIntent.setComponent(new ComponentName("com.lisheny.service","com.lisheny.aidl.AIDLService"));
        bindService(serviceIntent, connection, Context.BIND_AUTO_CREATE);

        btnGet = (Button)findViewById(R.id.btn_get);
        btnSet = (Button)findViewById(R.id.btn_set);
        btnGet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    mBooks = myBookmanager.getBooks();
                    for (Book book : mBooks){
                        Log.i("从服务端得到数据：",book.getName());
                    }
                } catch (RemoteException e) {
                    e.printStackTrace();
                }
            }
        });
        btnSet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    myBookmanager.addBook(new Book("php",45));
                } catch (RemoteException e) {
                    e.printStackTrace();
                }
            }
        });

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        //取消接收服务端通知
        if (myBookmanager != null && myBookmanager.asBinder().isBinderAlive()){
            try {
                myBookmanager.unRegisterListener(listener);
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }

        unbindService(connection);
    }

    private ServiceConnection connection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
            myBookmanager = IMyBookmanager.Stub.asInterface(iBinder);

            try {
                myBookmanager.registerListener(listener);
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }

        @Override
        public void onServiceDisconnected(ComponentName componentName) {
            // TODO: 2018/6/15 0015 dosomething
            //服务端挂了，可以在这里做一些重新连接的操作
        }
    };

    private IOnNewBookArrivedListener listener = new IOnNewBookArrivedListener.Stub() {
        @Override
        public void onNewBookArrived(Book newBook) throws RemoteException {
            Log.d("客户端：","收到通知了"+ newBook.getName());
        }
    };


}
