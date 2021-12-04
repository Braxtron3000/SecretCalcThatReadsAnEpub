package com.finalproject.queerCalc.ui.main;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import java.util.List;

public class NewSecretViewModel /*extends AndroidViewModel*/ {

    /*private SecretRepository repository;
    private LiveData<List<Secret>> allSecrets;
    private MutableLiveData<List<Secret>> searchResults;

    public NewSecretViewModel(@NonNull Application application) {
        super(application);
        repository = new SecretRepository(application);
        allSecrets = repository.getAllSecrets();
        searchResults = repository.getSearchResults();
    }

    MutableLiveData<List<Secret>> getSearchResults() {
        return searchResults;
    }

    LiveData<List<Secret>> getAllSecrets() {
        return allSecrets;
    }

    public void insertSecret(Secret secret){
        repository.insertSecret(secret);
    }

    public void findSecret(String name){
        repository.findSecret(name);
    }

    public void deleteSecret(String name){
        repository.deleteSecret(name);
    }

*/
}
