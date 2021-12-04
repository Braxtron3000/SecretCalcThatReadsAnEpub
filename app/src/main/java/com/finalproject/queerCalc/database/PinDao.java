package com.finalproject.queerCalc.database;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import java.util.List;

import io.reactivex.Single;

@Dao
public interface PinDao {

    @Query("SELECT * FROM pin")
    Single<Pin> getAll();

    @Query("SELECT * FROM pin WHERE pinId IN (:pinIds)")
    Single<Pin> loadAllByIds(int[] pinIds);

    //Todo: search method can take either a string or an int. This may cause issues.
    @Query("SELECT * FROM pin WHERE pin_title LIKE :pinTitle")
    Single<Pin> find(String pinTitle);

    @Query("SELECT * FROM pin WHERE pin_number LIKE :pinNumber")
    Single<Pin> find(int pinNumber);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertAll(Pin... pins);

    @Delete
    void delete(Pin pin);


}
