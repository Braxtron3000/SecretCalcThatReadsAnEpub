package com.finalproject.queerCalc.ui.main;

import static android.content.ContentValues.TAG;

import android.util.Log;
import android.view.View;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import androidx.navigation.Navigation;

import org.mozilla.javascript.Context;
import org.mozilla.javascript.Scriptable;

import java.util.Objects;


public class MainViewModel extends ViewModel {
    public MutableLiveData<String> equation = new MutableLiveData<>("");
    public MutableLiveData<String> result = new MutableLiveData<>("");

    public void addtoEquation(char addedChar) {
        //if the result isn't empty it's a new equation so the screen will automatically be wiped for a new equation.
        if (!result.getValue().equals("")) {
            equation.setValue("");
            result.setValue("");
        }
        equation.setValue(equation.getValue().concat(String.valueOf(addedChar)));

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
        try {
            Context rhino = Context.enter();
            // turn off optimization to work with android
            rhino.setOptimizationLevel(-1);

            String evaluation = equation.getValue();

            //have to add these because the javascript machine could read these lines as comments
            String[] comments = new String[]{"/*", "*/", "//"};
            for (String comment : comments)
                if (evaluation.contains(comment))
                    result.setValue("N/A");

            if (!Objects.equals(result.getValue(), "N/A"))
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

    public void instigatenavigation() {

    }


}