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

}
