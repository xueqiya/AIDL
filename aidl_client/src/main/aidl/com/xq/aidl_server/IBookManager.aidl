// IBookManager.aidl
package com.xq.aidl_server;

import com.xq.aidl_server.Book;
import java.util.List;
// Declare any non-default types here with import statements

interface IBookManager {

     List<Book> getBookList();

     void addBook(in Book book);
}
