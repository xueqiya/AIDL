package com.xq.aidl_client

import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.ServiceConnection
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.IBinder
import android.util.Log
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    val TAG = this.javaClass.simpleName
    //由AIDL生成的类
    private lateinit var mIBookManager: IBookManager
    //标志当前与服务端连接状况的布尔值，false 为未连接，true
    private var mBound: Boolean = false
    //包含Book对象的List
    private lateinit var mBooks: List<Book>

    val book = Book(333, "天黑以后")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        initEvent()
    }

    private fun initEvent() {
        addBook.setOnClickListener {
            if (!mBound) {
                attemptToBindService()
                //toast("当前与服务端处于未连接状态，正在尝试重连，请稍后再试")
                return@setOnClickListener
            }

            mIBookManager.addBook(book)
        }
        getBookList.setOnClickListener {
            if (!mBound) {
                attemptToBindService()
                //toast("当前与服务端处于未连接状态，正在尝试重连，请稍后再试")
                return@setOnClickListener
            }
            Log.i(TAG, "点击getBookList")
            mBooks = mIBookManager.bookList ?: mBooks
            var sb = StringBuffer()
            for (book in mBooks) {
                sb.append("{" + book.bookId + "}," + "{" + book.bookName + "}\n")
            }
            text.text = sb.toString()
            text.text = "asd"
        }
    }

    fun attemptToBindService() {
        val intent = Intent()
        intent.action = "com.xq.aidl_server.AIDLService"
        intent.`package` = "com.xq.aidl_server"
        //1,绑定一个远程的服务
        bindService(intent, mServiceConnection, Context.BIND_AUTO_CREATE)
    }

    var mServiceConnection: ServiceConnection = object : ServiceConnection {
        override fun onServiceDisconnected(name: ComponentName?) {
            Log.i(TAG, "service disconnected")
            mBound = false
        }

        override fun onServiceConnected(name: ComponentName?, service: IBinder?) {
            Log.i(TAG, "service connected")
            //注意：这里的service对象就是KTAIDLService中onBind()方法中返回的AIDL的实现
            //2,将服务返回的Binder对象换成AIDL接口
            mIBookManager = IBookManager.Stub.asInterface(service)
            mBound = true
            mBooks = mIBookManager.bookList
        }
    }

    override fun onStart() {
        super.onStart()
        if (!mBound) {
            attemptToBindService()
        }
    }

    override fun onStop() {
        super.onStop()
        if (mBound) {
            //解绑远程服务
            unbindService(mServiceConnection)
            mBound = false
        }
    }
}