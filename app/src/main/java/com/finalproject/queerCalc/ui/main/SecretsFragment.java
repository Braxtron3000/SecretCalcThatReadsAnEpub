package com.finalproject.queerCalc.ui.main;

import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.finalproject.queerCalc.R;

import androidx.databinding.DataBindingUtil;
import androidx.navigation.Navigation;

import com.finalproject.queerCalc.databinding.SecretsFragmentBinding;

import static com.finalproject.queerCalc.BR.mySecretViewModel;

public class SecretsFragment extends Fragment {

    public SecretsFragmentBinding binding;
    private SecretsViewModel mViewModel;

    public static SecretsFragment newInstance() {
        return new SecretsFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater,R.layout.secrets_fragment,container,false);
        binding.setLifecycleOwner(this);
        return binding.getRoot();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = new ViewModelProvider(this).get(SecretsViewModel.class);
        binding.setVariable(mySecretViewModel,mViewModel);
        getView().findViewById(R.id.floatingActionButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Navigation.findNavController(getActivity().findViewById(R.id.nav_host)).navigate(R.id.action_secrets_to_newSecret);
            }
        });
    }

}