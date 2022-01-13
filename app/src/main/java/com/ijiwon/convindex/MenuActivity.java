package com.ijiwon.convindex;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.LinearLayout;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.ijiwon.convindex.R;
import com.ijiwon.convindex.bottomSheetFrag.CameraPreviewFragment;
import com.ijiwon.convindex.bottomSheetFrag.HomeFragment;
import com.ijiwon.convindex.bottomSheetFrag.IndexFragment;
import com.ijiwon.convindex.bottomSheetFrag.MixingFragment;
import com.ijiwon.convindex.bottomSheetFrag.RecommendFragment;
import com.ijiwon.convindex.bottomSheetFrag.SettingFragment;
import com.ijiwon.convindex.camera2basic.Camera2BasicFragment;

public class MenuActivity extends AppCompatActivity {

    LinearLayout home_ly;
    BottomNavigationView bottomNavigationView;
    boolean isHome=false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);

        init(); //객체 정의
        SettingListener(); //리스너 등록 //맨 처음 시작할 탭 설정
        bottomNavigationView.setSelectedItemId(R.id.home);

        getSupportFragmentManager().beginTransaction() .replace(R.id.home_ly, new Camera2BasicFragment()) .commit();
    }




    private void init() {
        home_ly = findViewById(R.id.home_ly);
        bottomNavigationView = findViewById(R.id.bottomNavigationView);
    }
    private void SettingListener() { //선택 리스너 등록
        bottomNavigationView.setOnNavigationItemSelectedListener(new TabSelectedListener());
    }
    class TabSelectedListener implements BottomNavigationView.OnNavigationItemSelectedListener{
        @SuppressLint("NonConstantResourceId")
        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
            switch (menuItem.getItemId()) {
                case R.id.tab_home:
                    {
                        if(!isHome){
                            getSupportFragmentManager().beginTransaction() .replace(R.id.home_ly, new HomeFragment()) .commit();
                            isHome=true;
                        }
                        else {
                            getSupportFragmentManager().beginTransaction() .replace(R.id.home_ly, new Camera2BasicFragment()) .commit();
                            isHome=false;
                        }
                    return true;
                }
                case R.id.tab_index: {
                    getSupportFragmentManager().beginTransaction() .replace(R.id.home_ly, new IndexFragment()) .commit();
                    return true;
                }
                case R.id.tab_recommendation: {
                    getSupportFragmentManager().beginTransaction() .replace(R.id.home_ly, new RecommendFragment()) .commit();
                    return true;
                }
                case R.id.tab_mixing: {
                    getSupportFragmentManager().beginTransaction() .replace(R.id.home_ly, new MixingFragment()) .commit();
                    return true;
                }

                case R.id.tab_settings: {
                    getSupportFragmentManager().beginTransaction() .replace(R.id.home_ly, new SettingFragment()) .commit();
                    return true;
                }
            }
            return false;
        }
    }
}
