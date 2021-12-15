package com.finalproject.queerCalc.ui.main;

import static android.app.Activity.RESULT_OK;
import static android.content.ContentValues.TAG;
import static android.content.Context.KEYGUARD_SERVICE;

import androidx.biometric.*;


import androidx.annotation.RequiresApi;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.ViewModelProvider;


import android.app.KeyguardManager;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.security.keystore.KeyGenParameterSpec;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.finalproject.queerCalc.R;

import androidx.databinding.DataBindingUtil;
import androidx.security.crypto.EncryptedSharedPreferences;
import androidx.security.crypto.MasterKeys;

import com.finalproject.queerCalc.SecondActivity;
import com.finalproject.queerCalc.databinding.MainFragmentBinding;

import static com.finalproject.queerCalc.BR.myViewModel;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.concurrent.Executor;


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
            @RequiresApi(api = Build.VERSION_CODES.P)
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

    @RequiresApi(api = Build.VERSION_CODES.P)
    public void instigateNavigation(View v) throws IOException, GeneralSecurityException {
        String equation = ((TextView) getView().findViewById(R.id.tv_equation)).getText().toString();

        //Todo: make encrypted sharedPreferences a private method to simplify code
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

        String favorite = sharedPreferences.getString("myFavorite", "defValue");
        Log.d(TAG, "instigateNavigation: checkingForFavorite | favorite= " + favorite);
        if (equation.equals(favorite)) {
            authenticate();
            //startActivityForResult((new Intent(getContext(), SecondActivity.class)), 1);
            //Navigation.findNavController(getActivity().findViewById(R.id.nav_host)).navigate(R.id.action_mainFragment_to_secrets);
        }
        else if (equation.equals("1.7.13")) {
            mViewModel.wantsResult.setValue(false);
            mViewModel.getResult();
        } else if (!mViewModel.wantsResult.getValue()) {
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
        sharedPrefsEditor.putString("myFavorite", mViewModel.equation.getValue());
        sharedPrefsEditor.apply();

        Log.d(TAG, "favoriteEquation: sharedPref=" + sharedPreferences.getString("myFavorite", "defValue"));
    }

    //Biometrics
    private Executor executor;
    private BiometricPrompt biometricPrompt;
    private BiometricPrompt.PromptInfo promptInfo;


    @RequiresApi(api = Build.VERSION_CODES.P)
    private void authenticate() {
        executor = ContextCompat.getMainExecutor(getActivity().getApplicationContext());
        KeyguardManager keyguardManager = (KeyguardManager) getContext().getSystemService(KEYGUARD_SERVICE);
        keyguardManager.isDeviceSecure();

        promptInfo = new BiometricPrompt.PromptInfo.Builder()
                .setTitle("Biometric login for my app")
                .setSubtitle("Log in using your biometric credential")
                //.setAllowedAuthenticators(BiometricManager.Authenticators.BIOMETRIC_STRONG | BiometricManager.Authenticators.DEVICE_CREDENTIAL)
                .setNegativeButtonText("cancel")
                .setConfirmationRequired(true)
                .build();

        biometricPrompt = new BiometricPrompt(MainFragment.this,
                executor, new BiometricPrompt.AuthenticationCallback() {
            @Override
            public void onAuthenticationError(int errorCode, @NonNull CharSequence errString) {
                super.onAuthenticationError(errorCode, errString);
            }

            @Override
            public void onAuthenticationSucceeded(@NonNull BiometricPrompt.AuthenticationResult result) {
                super.onAuthenticationSucceeded(result);
                startActivityForResult((new Intent(getContext(), SecondActivity.class)), 1);
            }

            @Override
            public void onAuthenticationFailed() {
                super.onAuthenticationFailed();
            }
        });

        if (keyguardManager.isDeviceSecure()) {
            biometricPrompt.authenticate(promptInfo);
            Log.d(TAG, "doBiometrics: device is Secure method");
        }
    }




}
