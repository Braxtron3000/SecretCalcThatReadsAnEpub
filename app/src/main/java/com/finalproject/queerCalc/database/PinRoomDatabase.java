package com.finalproject.queerCalc.database;

import androidx.room.Database;
import androidx.room.RoomDatabase;

@Database(entities = {Pin.class},version = 1)
public abstract class PinRoomDatabase extends RoomDatabase {
    public abstract PinDao pinDao();
}
