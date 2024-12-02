package com.capstone.bankit.data.models;

import android.os.Parcel;
import android.os.Parcelable;

public class TransactionModel implements Parcelable {
    private int id;
    private String type;
    private long date;
    private String desc;
    private double amount;
    private int percentage;
    private String flagExpenseIncome;

    public TransactionModel() {
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public int getPercentage() {
        return percentage;
    }

    public void setPercentage(int percentage) {
        this.percentage = percentage;
    }

    protected TransactionModel(Parcel in) {
        id = in.readInt();
        type = in.readString();
        date = in.readLong();
        desc = in.readString();
        amount = in.readDouble();
        percentage = in.readInt();
        flagExpenseIncome = in.readString();
    }

    public static final Creator<TransactionModel> CREATOR = new Creator<TransactionModel>() {
        @Override
        public TransactionModel createFromParcel(Parcel in) {
            return new TransactionModel(in);
        }

        @Override
        public TransactionModel[] newArray(int size) {
            return new TransactionModel[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(id);
        dest.writeString(type);
        dest.writeLong(date);
        dest.writeString(desc);
        dest.writeDouble(amount);
        dest.writeInt(percentage);
        dest.writeString(flagExpenseIncome);
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public long getDate() {
        return date;
    }

    public void setDate(long date) {
        this.date = date;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public Double getAmount() {
        return amount;
    }

    public void setAmount(Double amount) {
        this.amount = amount;
    }

    public String getFlagExpenseIncome() {
        return flagExpenseIncome;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setFlagExpenseIncome(String flagExpenseIncome) {
        this.flagExpenseIncome = flagExpenseIncome;
    }
}
