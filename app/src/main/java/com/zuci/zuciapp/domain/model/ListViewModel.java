package com.zuci.zuciapp.domain.model;

import android.os.Parcel;
import android.os.Parcelable;

public class ListViewModel implements Parcelable {
    String name;
    String rollNo;
    String gender;
    String address;
    String classDiv;
    String phone;
    String images;

    public ListViewModel(String name, String rollNo, String gender, String address, String classDiv, String phone, String images) {
        this.name = name;
        this.rollNo = rollNo;
        this.gender = gender;
        this.address = address;
        this.classDiv = classDiv;
        this.phone = phone;
        this.images = images;
    }

    protected ListViewModel(Parcel in) {
        name = in.readString();
        rollNo = in.readString();
        gender = in.readString();
        address = in.readString();
        classDiv = in.readString();
        phone = in.readString();
        images = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(name);
        dest.writeString(rollNo);
        dest.writeString(gender);
        dest.writeString(address);
        dest.writeString(classDiv);
        dest.writeString(phone);
        dest.writeString(images);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<ListViewModel> CREATOR = new Creator<ListViewModel>() {
        @Override
        public ListViewModel createFromParcel(Parcel in) {
            return new ListViewModel(in);
        }

        @Override
        public ListViewModel[] newArray(int size) {
            return new ListViewModel[size];
        }
    };

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getRollNo() {
        return rollNo;
    }

    public void setRollNo(String rollNo) {
        this.rollNo = rollNo;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getClassDiv() {
        return classDiv;
    }

    public void setClassDiv(String classDiv) {
        this.classDiv = classDiv;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getImages() {
        return images;
    }

    public void setImages(String images) {
        this.images = images;
    }
}
