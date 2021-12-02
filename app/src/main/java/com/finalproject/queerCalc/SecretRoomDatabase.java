package com.finalproject.queerCalc;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

@Database(entities = {Secret.class}, version = 1)
public abstract class SecretRoomDatabase extends RoomDatabase {

    public abstract SecretDao secretDao();
    private static SecretRoomDatabase INSTANCE;

    static SecretRoomDatabase getDatabase(final Context context) {
        if (INSTANCE == null) {
            synchronized (SecretRoomDatabase.class) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(
                            context.getApplicationContext(),
                            SecretRoomDatabase.class,
                            "secret_database"
                    ).build();
                }
            }
        }
        return INSTANCE;
    }
}
