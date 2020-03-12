package com.alfanshter.jatimpark.Rombongan

import android.os.Parcel
import android.os.Parcelable

class ParcelObjectName(val title: String?, val deksipsi: String?):Parcelable {
    constructor(parcel: Parcel) : this(

        parcel.readString(),
        parcel.readString()
    )

    override fun writeToParcel(dest: Parcel?, flags: Int) {
        dest?.writeString(title)
        dest?.writeString(deksipsi)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<ParcelObjectName> {
        override fun createFromParcel(parcel: Parcel): ParcelObjectName {
            return ParcelObjectName(parcel)
        }

        override fun newArray(size: Int): Array<ParcelObjectName?> {
            return arrayOfNulls(size)
        }
    }
}