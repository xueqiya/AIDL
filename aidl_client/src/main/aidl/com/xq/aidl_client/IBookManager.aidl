// IBookManager.aidl
package com.xq.aidl_client;

import com.xq.aidl_client.Book;
// Declare any non-default types here with import statements

interface IBookManager {

     List<Book> getBookList();

     void addBook(in Book book);
}
