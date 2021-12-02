package com.finalproject.queerCalc;

import android.app.Application;
import android.os.AsyncTask;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import java.util.List;

public class SecretRepository {
    
    private MutableLiveData<List<Secret>> searchResults = new MutableLiveData<>();
    private LiveData<List<Secret>> allSecrets;
    
    private SecretDao secretDao;
    
    public SecretRepository(Application application){
        SecretRoomDatabase db = SecretRoomDatabase.getDatabase(application);
        secretDao = db.secretDao();
        allSecrets = secretDao.getAllSecrets();
    }

    private void asynchFinished(List<Secret> results){
        searchResults.setValue(results);
    }

    private static class QueryAsynchTask extends AsyncTask<String, Void,List<Secret>> {

        private SecretDao asynchTaskDao;
        private SecretRepository delegate = null;

        QueryAsynchTask(SecretDao dao){
            asynchTaskDao = dao;
        }

        @Override
        protected List<Secret> doInBackground(final String... strings) {
            return asynchTaskDao.findSecret(strings[0]);
        }

        @Override
        protected void onPostExecute(List<Secret> result) {
            delegate.asynchFinished(result);
        }
    }

    private static class InsertAsyncTask extends AsyncTask<Secret,Void,Void>{

        private SecretDao asyncTaskDao;

        InsertAsyncTask(SecretDao dao){
            asyncTaskDao = dao;
        }

        @Override
        protected Void doInBackground(final Secret... Secrets) {
            asyncTaskDao.insertSecret(Secrets[0]);
            return null;
        }
    }

    private static class DeleteAsyncTask extends AsyncTask<String,Void,Void>{

        private SecretDao asynchTaskDao;

        DeleteAsyncTask(SecretDao dao){
            asynchTaskDao = dao;
        }

        @Override
        protected Void doInBackground(final String... strings) {
            asynchTaskDao.deleteSecret(strings[0]);
            return null;
        }
    }

    public void insertSecret(Secret newSecret){
        InsertAsyncTask task = new InsertAsyncTask(secretDao);
        task.execute(newSecret);
    }

    public void deleteSecret(String name){
        DeleteAsyncTask task = new DeleteAsyncTask(secretDao);
        task.execute(name);
    }

    public void findSecret(String name){
        QueryAsynchTask task = new QueryAsynchTask(secretDao);
        task.delegate = this;
        task.execute(name);
    }

    //to complete the repository add methods that the viewmodel can call to obtain references to the allSecrets and searchResults objects
    public LiveData<List<Secret>> getAllSecrets(){
        return allSecrets;
    }

    public MutableLiveData<List<Secret>> getSearchResults() {
        return searchResults;
    }


}
