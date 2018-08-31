// IMyBookmanager.aidl
package com.lisheny.aidl;
import com.lisheny.aidl.Book;
import com.lisheny.aidl.IOnNewBookArrivedListener;
// Declare any non-default types here with import statements

interface IMyBookmanager {

     List<Book> getBooks();
     void addBook(in Book book);

     void registerListener (IOnNewBookArrivedListener listener);
     void unRegisterListener (IOnNewBookArrivedListener listener);
}
