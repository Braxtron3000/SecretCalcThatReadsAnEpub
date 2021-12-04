package com.finalproject.queerCalc.ui.main;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;
import androidx.room.Room;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.finalproject.queerCalc.R;
import com.finalproject.queerCalc.database.Pin;
import com.finalproject.queerCalc.database.PinDao;
import com.finalproject.queerCalc.database.PinRoomDatabase;

import io.reactivex.Completable;
import io.reactivex.schedulers.Schedulers;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link newSecretFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class newSecretFragment extends Fragment {

    private NewSecretViewModel mViewModel;


    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    //private SecretRepository repository;

    public newSecretFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment newSecret.
     */
    // TODO: Rename and change types and number of parameters
    public static newSecretFragment newInstance(String param1, String param2) {
        newSecretFragment fragment = new newSecretFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment

        return inflater.inflate(R.layout.fragment_new_secret, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        //mViewModel = new ViewModelProvider(this).get(NewSecretViewModel.class);
        requireView().findViewById(R.id.save_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String title = ((TextView) requireView().findViewById(R.id.tv_newSecretTitle)).getText().toString();
                String pin = ((TextView) requireView().findViewById(R.id.tv_newSecretPin)).getText().toString();

                if (title.matches("")||pin.matches("")){
                    Toast.makeText(getContext(), "you need a title and a pin number", Toast.LENGTH_SHORT).show();
                }
                else {
                    Log.d("insert secret"," secret title: "+title);
                    Log.d("insert secret"," secret pin: "+pin);
                    Pin secret = new Pin(title,Integer.parseInt(pin));
                    if (requireActivity().getApplicationContext()==null)Log.d("android is retard","duhhhh i'm null cuz im stupid like thta");
                    PinRoomDatabase db = Room.databaseBuilder(requireActivity().getApplicationContext(), PinRoomDatabase.class, "pins_database").build();
                    PinDao pinDao = db.pinDao();

                    Completable.fromRunnable(new Runnable() {
                        @Override
                        public void run() {
                            pinDao.insertAll(secret);
                        }
                    })
                            .subscribeOn(Schedulers.io())
                            .subscribe();

                    Toast.makeText(getContext(), "pin saved", Toast.LENGTH_SHORT).show();
                    Navigation.findNavController(getActivity().findViewById(R.id.nav_host)).navigate(R.id.action_newSecret_to_secrets);
                }


            };
        });
    }


}