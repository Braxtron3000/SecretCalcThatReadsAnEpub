package com.finalproject.queerCalc.database;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class Pin {

    @PrimaryKey
    public int pinId;

    @ColumnInfo(name = "pin_title")
    public String title;

    @ColumnInfo(name = "pin_number")
    public int pinNumber;


    public Pin(String title, int pinNumber) {
        this.title = title;
        this.pinNumber = pinNumber;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getPinNumber() {
        return pinNumber;
    }

    public void setPinNumber(int pinNumber) {
        this.pinNumber = pinNumber;
    }
}
