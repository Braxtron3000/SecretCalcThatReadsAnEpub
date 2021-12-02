package com.finalproject.queerCalc;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

@Dao
public interface SecretDao {

    @Insert
    void insertSecret(Secret secret);

    @Query("SELECT * FROM secrets WHERE secretTitle = :title")
    List<Secret> findSecret(String title);

    @Query("DELETE FROM secrets WHERE secretTitle = :title")
    void deleteSecret(String title);

    @Query("SELECT * FROM secrets")
    LiveData<List<Secret>> getAllSecrets();
}
