package com.xq.aidl_client

import android.os.Parcel
import android.os.Parcelable

class Book(var bookId: Int, var bookName: String) : Parcelable {
    constructor(source: Parcel): this(source.readInt(), source.readString().toString())
    /**
     * @return Int 当前对象的描述，几乎所有情况都是0
     * */
    override fun describeContents(): Int {
        return 0
    }

    /**
     * @param dest 将当前对象写入序列化结构中(即写入Parcel类中)
     * @param flags 1：表示当前对象需要作为返回值返回，不能立即释放，几乎所有情况都是0
     * */
    override fun writeToParcel(dest: Parcel?, flags: Int) {
        dest?.writeInt(bookId)
        dest?.writeString(bookName)
    }
    /**
     * @param dest Parcel类内部包装了可序列化数据，可以在Binder中存储与传输数据
     * */
    fun readFromParcel(dest: Parcel): Unit{
        //此处的读值顺序应当是和writeToParcel()方法中一致的
        bookId = dest.readInt()
        bookName = dest.readString().toString()
    }
    /**
     * 用伴生对象和注解@JvmField来标识一个属性来表示一个java中的静态对象
     * */
    companion object {
        @JvmField final var CREATOR: Parcelable.Creator<Book> = object : Parcelable.Creator<Book> {
            override fun newArray(size: Int): Array<Book?> {
                return arrayOfNulls(size)
            }

            override fun createFromParcel(source: Parcel): Book {
                return Book(source)
            }

        }
    }
}