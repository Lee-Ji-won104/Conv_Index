package com.ijiwon.convindex;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.ijiwon.convindex.camera2basic.Camera2BasicFragment;


public class AnalyzingFragment extends Fragment {

    MenuActivity activity;

    public static AnalyzingFragment newInstance(){
        return new AnalyzingFragment();
    }

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


        View view=inflater.inflate(R.layout.fragment_analyzing, container, false);

        ImageView image=(ImageView) view.findViewById(R.id.miniBitmap);
        image.setImageBitmap(Camera2BasicFragment.dialog_bitmap);

        TextView name= (TextView) view.findViewById(R.id.name) ;
        name.setText(Camera2BasicFragment.a.get(0).getName());

        TextView price= (TextView) view.findViewById(R.id.price) ;
        price.setText(Camera2BasicFragment.a.get(0).getPrice());

        TextView diet= (TextView) view.findViewById(R.id.diet) ;
        diet.setText(Camera2BasicFragment.a.get(0).getDiet());

        TextView calories= (TextView) view.findViewById(R.id.calories) ;
        calories.setText(Camera2BasicFragment.a.get(0).getCalories());

        TextView short_info= (TextView) view.findViewById(R.id.short_info) ;
        short_info.setText(Camera2BasicFragment.a.get(0).getShort_info());

        TextView detail_info= (TextView) view.findViewById(R.id.detail_info) ;
        detail_info.setText(Camera2BasicFragment.a.get(0).getDetail_info());


        return view;
    }
}