package com.xq.aidl_server

import android.app.Service
import android.content.Intent
import android.os.IBinder
import android.util.Log

class AIDLService : Service() {
    val TAG = this.javaClass.simpleName
    var mBooks = mutableListOf<Book>()
    /**
     * 创建⼀个继承⾃IBookManager.Stub类型的匿名类的对象，即实现AIDL中的Binder的接口
     * */
    var mIBookManager = object : IBookManager.Stub() {

        override fun getBookList(): MutableList<Book> {
            return if (mBooks == null) ArrayList<Book>() else mBooks as MutableList<Book>
        }

        override fun addBook(book: Book?) {
            if (book != null) {
                //TODO 此处这个判断无效
                if (!(mBooks as MutableList<Book>).contains(book)) {
                    (mBooks as MutableList<Book>).add(book)
                }
            } else {
                var booknew = Book(111, "百年孤独")
                (mBooks as MutableList<Book>).add(booknew)
            }
        }
    }

    override fun onCreate() {
        Log.i(TAG, "onCreate()")
        var book = Book(222, "毒木圣经")
        mBooks .add(book)
        super.onCreate()
    }

    override fun onBind(intent: Intent?): IBinder {
        Log.i(TAG, String.format("on bind,intent = %s", intent.toString()))
        return mIBookManager
    }

}
