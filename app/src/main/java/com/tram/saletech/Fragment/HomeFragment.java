package com.tram.saletech.Fragment;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;

import com.google.android.material.snackbar.Snackbar;
import com.tram.saletech.Activities.MainActivity;
import com.tram.saletech.Interface.SendData;
import com.tram.saletech.R;


public class HomeFragment extends Fragment{


    EditText mInputSearch;
    ImageView mIconSearch;
    MainActivity mainActivity;
    private  SendData iSendData;

    public HomeFragment() {
        // Required empty public constructor
    }
    public static HomeFragment getInstance(String string) {
        HomeFragment homeFragment = new HomeFragment();
        Bundle bundle = new Bundle();
        bundle.putString("home", string);
        homeFragment.setArguments(bundle);
        return homeFragment;
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        mInputSearch = view.findViewById(R.id.mysearch);
        mIconSearch = view.findViewById(R.id.iconSearch);
//       mSearchData = (SearchData) getActivity();
        mainActivity = (MainActivity) getActivity();
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    @Override
    public void onStart() {
//        getSearchInput();
//        Log.d("BBB",mainActivity.getSend());
//        mainActivity.setGet("Tr get get");
//        sendDataToActivity();
//        String receive_fragment = mainActivity.mHomeFragment.getArguments().getString("Send_fragment");
//        Log.d("BBB","Tu home fragment nhan du lieu tu Main: " + receive_fragment);

        super.onStart();
        mIconSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendDataToActivity();
            }
        });

    }

    public void sendDataToActivity(){
        iSendData = mainActivity;
        String string = mInputSearch.getText().toString().trim();
        iSendData.send_from_HomeFragment(string);
    }


}