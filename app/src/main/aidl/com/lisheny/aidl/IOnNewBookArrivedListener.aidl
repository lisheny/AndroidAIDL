// IOnNewBookArrivedListener.aidl
package com.lisheny.aidl;
import com.lisheny.aidl.Book;

interface IOnNewBookArrivedListener {
   void onNewBookArrived (in Book newBook);
}
