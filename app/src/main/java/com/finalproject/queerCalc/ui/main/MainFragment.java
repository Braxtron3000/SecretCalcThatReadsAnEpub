package com.finalproject.queerCalc.ui.main;

import static android.app.Activity.RESULT_OK;
import static android.content.ContentValues.TAG;

import androidx.lifecycle.LifecycleObserver;
import androidx.lifecycle.ViewModelProvider;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.preference.PreferenceManager;
import android.security.keystore.KeyGenParameterSpec;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.finalproject.queerCalc.R;

import androidx.databinding.DataBindingUtil;
import androidx.navigation.Navigation;
import androidx.security.crypto.EncryptedSharedPreferences;
import androidx.security.crypto.MasterKeys;

import com.finalproject.queerCalc.SecondActivity;
import com.finalproject.queerCalc.databinding.MainFragmentBinding;
import com.google.android.material.button.MaterialButton;

import static com.finalproject.queerCalc.BR.myViewModel;

import org.mozilla.javascript.Context;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.Objects;


public class MainFragment extends Fragment {

    private MainViewModel mViewModel;
    public MainFragmentBinding binding;

    public static MainFragment newInstance() {
        return new MainFragment();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        //return inflater.inflate(R.layout.main_fragment, container, false);
        binding = DataBindingUtil.inflate(inflater, R.layout.main_fragment, container, false);
        binding.setLifecycleOwner(this);
        return binding.getRoot();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        getView().findViewById(R.id.btn_divide).setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                try {
                    instigateNavigation(v);
                } catch (IOException | GeneralSecurityException e) {
                    e.printStackTrace();
                }
                return false;
            }
        });


        mViewModel = new ViewModelProvider(this).get(MainViewModel.class);
        binding.setVariable(myViewModel, mViewModel);
    }

    public void instigateNavigation(View v) throws IOException, GeneralSecurityException {
        String equation = ((TextView) getView().findViewById(R.id.tv_equation)).getText().toString();

        KeyGenParameterSpec keyGenParameterSpec = MasterKeys.AES256_GCM_SPEC;
        String mainKeyAlias = MasterKeys.getOrCreate(keyGenParameterSpec);

        //instance of encrypted sharedprefs
        SharedPreferences sharedPreferences = EncryptedSharedPreferences.create(
                "favoriteEquation",
                mainKeyAlias,
                requireActivity().getApplicationContext(),
                EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
                EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
        );

        String favorite = sharedPreferences.getString("myFavorite","defValue");
        Log.d(TAG, "instigateNavigation: checkingForFavorite | favorite= "+favorite);
        if (equation.equals(favorite))
            startActivityForResult((new Intent(getContext(), SecondActivity.class)), 1);
            //Navigation.findNavController(getActivity().findViewById(R.id.nav_host)).navigate(R.id.action_mainFragment_to_secrets);

        else if (equation.equals("111")) {
            mViewModel.wantsResult.setValue(false);
            mViewModel.getResult();
        }

        else if (!mViewModel.wantsResult.getValue()){
            favoriteEquation();
            mViewModel.wantsResult.setValue(true);
        }

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1 && resultCode == RESULT_OK) {
            mViewModel.equation.setValue("15*40+");
        }
    }


    private void favoriteEquation() throws GeneralSecurityException, IOException {
        //sharedPrefs file name
        String sharedPrefsFile = "favoriteEquation";

        //gets or creates the master key
        KeyGenParameterSpec keyGenParameterSpec = MasterKeys.AES256_GCM_SPEC;
        String mainKeyAlias = MasterKeys.getOrCreate(keyGenParameterSpec);

        //instance of encrypted sharedprefs
        SharedPreferences sharedPreferences = EncryptedSharedPreferences.create(
                sharedPrefsFile,
                mainKeyAlias,
                requireActivity().getApplicationContext(),
                EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
                EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
        );

        SharedPreferences.Editor sharedPrefsEditor = sharedPreferences.edit();
        sharedPrefsEditor.putString("myFavorite",mViewModel.equation.getValue());
        sharedPrefsEditor.apply();

        Log.d(TAG, "favoriteEquation: sharedPref="+sharedPreferences.getString("myFavorite","defValue"));
        Toast.makeText(getContext(), "yay! I ran!", Toast.LENGTH_SHORT).show();
    }

}
