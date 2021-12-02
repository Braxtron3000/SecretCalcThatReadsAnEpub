package com.finalproject.queerCalc;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "secrets")
public class Secret {

    @PrimaryKey(autoGenerate = true)
    @NonNull
    @ColumnInfo(name = "secretId")
    private int id;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getSecretTitle() {
        return secretTitle;
    }

    public void setSecretTitle(String secretTitle) {
        this.secretTitle = secretTitle;
    }

    public int getSecretPin() {
        return secretPin;
    }

    public void setSecretPin(int secretPin) {
        this.secretPin = secretPin;
    }

    private String secretTitle;
    private int secretPin;

    public Secret(String secretTitle, int secretPin){
        this.id=id;
        this.secretTitle=secretTitle;
        this.secretPin=secretPin;
    }


}
