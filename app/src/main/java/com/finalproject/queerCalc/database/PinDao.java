package com.finalproject.queerCalc.database;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

@Dao
public interface PinDao {

    @Query("SELECT * FROM pin")
    List<Pin> getAll();

    @Query("SELECT * FROM pin WHERE pinId IN (:pinIds)")
    List<Pin> loadAllByIds(int[] pinIds);


    //Todo: search method can take either a string or an int. This may cause issues.
    @Query("SELECT * FROM pin WHERE pin_title LIKE :pinTitle")
    Pin search(String pinTitle);

    @Query("SELECT * FROM pin WHERE pin_number LIKE :pinNumber")
    Pin search(int pinNumber);


    @Insert
    void insertAll(Pin... pins);

    @Delete
    void delete(Pin pin);


}
