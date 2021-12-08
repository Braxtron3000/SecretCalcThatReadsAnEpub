package com.finalproject.queerCalc.ui.main;

import androidx.lifecycle.LifecycleObserver;
import androidx.lifecycle.ViewModelProvider;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.finalproject.queerCalc.R;
import androidx.databinding.DataBindingUtil;
import androidx.navigation.Navigation;

import com.finalproject.queerCalc.SecondActivity;
import com.finalproject.queerCalc.databinding.MainFragmentBinding;
import com.google.android.material.button.MaterialButton;

import static com.finalproject.queerCalc.BR.myViewModel;

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
        binding = DataBindingUtil.inflate(inflater,R.layout.main_fragment,container,false);
        binding.setLifecycleOwner(this);
        return binding.getRoot();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        getView().findViewById(R.id.btn_divide).setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                instigateNavigation(v);
                return false;
            }
        });


        mViewModel = new ViewModelProvider(this).get(MainViewModel.class);
        binding.setVariable(myViewModel,mViewModel);
    }

    public void instigateNavigation(View v){
        String equation = ((TextView)getView().findViewById(R.id.tv_equation)).getText().toString();


        if (equation.equals("//5318008")) startActivity((new Intent(getContext(),SecondActivity.class)));
            //Navigation.findNavController(getActivity().findViewById(R.id.nav_host)).navigate(R.id.action_mainFragment_to_secrets);

    }


}