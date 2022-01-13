package com.ijiwon.convindex.bottomSheetFrag;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.ijiwon.convindex.CameraActivity;
import com.ijiwon.convindex.MenuActivity;
import com.ijiwon.convindex.R;


public class IndexFragment extends Fragment {

    MenuActivity activity;

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        //이 메소드가 호출될떄는 프래그먼트가 엑티비티위에 올라와있는거니깐 getActivity메소드로 엑티비티참조가능
        activity = (MenuActivity) getActivity();

    }

    @Override
    public void onDetach() {
        super.onDetach();
        //이제 더이상 엑티비티 참초가안됨
        activity = null;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view=inflater.inflate(R.layout.fragment_index, container, false);

        TextView name= (TextView) view.findViewById(R.id.index1) ;
        name.setText("index");

        return view;
    }
}