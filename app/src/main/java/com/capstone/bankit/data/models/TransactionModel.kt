package com.capstone.bankit.data.models

import android.os.Parcel
import android.os.Parcelable

data class TransactionModel(
    var id: Int = 0,
    var type: String? = null,
    var date: Long = 0,
    var desc: String? = null,
    var amount: Double = 0.0,
    var percentage: Int = 0,
    var flagExpenseIncome: String? = null
) : Parcelable {

    constructor(parcel: Parcel) : this(
        id = parcel.readInt(),
        type = parcel.readString(),
        date = parcel.readLong(),
        desc = parcel.readString(),
        amount = parcel.readDouble(),
        percentage = parcel.readInt(),
        flagExpenseIncome = parcel.readString()
    )

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeInt(id)
        parcel.writeString(type)
        parcel.writeLong(date)
        parcel.writeString(desc)
        parcel.writeDouble(amount)
        parcel.writeInt(percentage)
        parcel.writeString(flagExpenseIncome)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<TransactionModel> {
        override fun createFromParcel(parcel: Parcel): TransactionModel {
            return TransactionModel(parcel)
        }

        override fun newArray(size: Int): Array<TransactionModel?> {
            return arrayOfNulls(size)
        }
    }
}
