package com.finalproject.queerCalc.ui.main;

import static android.content.ContentValues.TAG;

import android.util.Log;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import org.mozilla.javascript.Context;
import org.mozilla.javascript.Scriptable;


public class MainViewModel extends ViewModel {
    public MutableLiveData<String> equation = new MutableLiveData<>("");
    public MutableLiveData<String> result = new MutableLiveData<>("");

    public void addtoEquation(char addedChar) {

        equation.setValue(equation.getValue().concat(String.valueOf(addedChar)));
        Log.d(TAG, "addtoEquation: " + equation.getValue());
    }

    public void clearEquation() {
        equation.setValue("");
        result.setValue("");
    }

    public boolean backspace() {
        String equation = this.equation.getValue();
        if (equation != null && equation.length() > 0)
            this.equation.setValue(equation.substring(0, equation.length() - 1));
        return false;
    }

    public void getResult() {
        int rawResult;
        try {
            Context rhino = Context.enter();
        // turn off optimization to work with android
            rhino.setOptimizationLevel(-1);

            String evaluation = equation.getValue();

            try {
                Scriptable scope = rhino.initStandardObjects();
                String result = rhino.evaluateString(scope, evaluation, "JavaScript", 1, null).toString();
                this.result.setValue(result);
                Log.d(TAG, "getResult: " + result);
            } finally {
                Context.exit();
            }

        } catch (Exception e) {
            System.out.println(e);
            result.setValue("N/A");
        }
    }


}