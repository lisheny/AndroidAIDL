package com.lisheny.aidl;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.os.RemoteCallbackList;
import android.os.RemoteException;
import android.support.annotation.Nullable;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * Created by Administrator on 2018/6/15 0015.
 */

public class AIDLService extends Service {

    private CopyOnWriteArrayList<Book> mBooks = new CopyOnWriteArrayList<>();
    private AtomicBoolean isServieceDestoryed = new AtomicBoolean(false);
    private RemoteCallbackList<IOnNewBookArrivedListener> listeners = new RemoteCallbackList<>();

    private Binder mBinder = new IMyBookmanager.Stub() {
        @Override
        public List<Book> getBooks() throws RemoteException {
//            SystemClock.sleep(5000);
            return mBooks;
        }

        @Override
        public void addBook(Book book) throws RemoteException {
           mBooks.add(book);
        }

        @Override
        public void registerListener(IOnNewBookArrivedListener listener) throws RemoteException {
//            if (!listeners.contains(listener)){
//                listeners.add(listener);
//            }else {
//                Log.d("服务端：","已经注册监听");
//            }

            listeners.register(listener);
        }

        @Override
        public void unRegisterListener(IOnNewBookArrivedListener listener) throws RemoteException {
//            if (listeners.contains(listener)){
//                listeners.remove(listener);
//                Log.d("服务端：","已经移除监听");
//            }else {
//                Log.d("服务端：","没有要移除的对象");
//            }

            //上面注释掉的方式是取消注册不了的，用下面这个方法
            listeners.unregister(listener);
        }
    };

    @Override
    public void onCreate() {
        super.onCreate();

        //开个线程，5秒发出一次通知
        new Thread(new Runnable() {
            @Override
            public void run() {
                while (!isServieceDestoryed.get()){
                    try {
                        Thread.sleep(5000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    Book newBook = new Book("boook",1);
                    try {
                        onNewBookArrived(newBook);
                    } catch (RemoteException e) {
                        e.printStackTrace();
                    }
                }
            }
        }).start();
    }

    private void onNewBookArrived (Book book) throws RemoteException{
        mBooks.add(book);
//        for (IOnNewBookArrivedListener listener : listeners){
//            listener.onNewBookArrived(book);
//        }

        //RemoteCallbackList 并不是一个 List，遍历要用下面方式，beginBroadcast 、finishBroadcast 函数要配对使用
        final int N = listeners.beginBroadcast();
        for (int i =0; i < N ; i++){
            IOnNewBookArrivedListener listener = listeners.getBroadcastItem(i);
            if (listener != null){
                listener.onNewBookArrived(book);
            }
        }
        listeners.finishBroadcast();

    }

    @Override
    public void onDestroy() {
        isServieceDestoryed.set(true);
        super.onDestroy();

    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return mBinder;
    }
}
