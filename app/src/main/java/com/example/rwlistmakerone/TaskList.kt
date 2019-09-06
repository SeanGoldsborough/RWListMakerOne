package com.example.rwlistmakerone

import android.os.Parcel
import android.os.Parcelable

class TaskList(val name: String, val tasks: ArrayList<String> = ArrayList()) : Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readString()!!,
        parcel.createStringArrayList()!!
    )

    override fun describeContents() = 0

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(name)
        parcel.writeStringList(tasks)
    }



    companion object CREATOR: Parcelable.Creator<TaskList> {
        override fun createFromParcel(source: Parcel): TaskList = TaskList(source)

        override fun newArray(size: Int): Array<TaskList?> = arrayOfNulls(size)

    }


}