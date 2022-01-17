package com.ijiwon.convindex.bottomSheetFrag;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.ijiwon.convindex.R;
import com.ijiwon.convindex.camera2basic.Camera2BasicFragment;

public class AnalActivity extends AppCompatActivity {


    //bottomSheet
    private LinearLayout bottomSheetLayout;
    private LinearLayout gestureLayout;
    private BottomSheetBehavior<LinearLayout> sheetBehavior;
    protected ImageView bottomSheetArrowImageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_anal);


        ImageView image=(ImageView) findViewById(R.id.miniBitmap);
        image.setImageBitmap(Camera2BasicFragment.dialog_bitmap);

        TextView name= (TextView) findViewById(R.id.name) ;
        //name.setText(Camera2BasicFragment.a.get(0).getName());

        TextView price= (TextView) findViewById(R.id.price) ;
        //price.setText(Camera2BasicFragment.a.get(0).getPrice());

        /*
        TextView diet= (TextView) findViewById(R.id.diet) ;
        diet.setText(Camera2BasicFragment.a.get(0).getDiet());

        TextView calories= (TextView) findViewById(R.id.calories) ;
        calories.setText(Camera2BasicFragment.a.get(0).getCalories());

        TextView short_info= (TextView) findViewById(R.id.short_info) ;
        short_info.setText(Camera2BasicFragment.a.get(0).getShort_info());

        TextView detail_info= (TextView) findViewById(R.id.detail_info) ;
        detail_info.setText(Camera2BasicFragment.a.get(0).getDetail_info());


         */
        //bottomSheetLayout

        bottomSheetLayout = findViewById(R.id.bottom_sheet_layout);
        gestureLayout = findViewById(R.id.gesture_layout);
        sheetBehavior = BottomSheetBehavior.from(bottomSheetLayout);
        bottomSheetArrowImageView = findViewById(R.id.bottom_sheet_arrow);

        ViewTreeObserver vto = gestureLayout.getViewTreeObserver();
        vto.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {

            //Code 상으로 View를 그려야 할 일이 있어 onLayout이 호출된 이후에 관련 작업을 처리하도록 했는데
            //특정 단말에서 오동작하는 경우가 있어 구글링을 해보니 다음과 같은 방법을 사용하라고 되어 있다

            @Override
            public void onGlobalLayout() {
                gestureLayout.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                //                int width = bottomSheetLayout.getMeasuredWidth();
                int height = gestureLayout.getMeasuredHeight();

                sheetBehavior.setPeekHeight(height);
            }
        });
        sheetBehavior.setHideable(false);

        sheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);

        sheetBehavior.setBottomSheetCallback(
                new BottomSheetBehavior.BottomSheetCallback() {
                    @Override
                    public void onStateChanged(@NonNull View bottomSheet, int newState) {

                        /*
                        Behavior 상태
BottomSheetBehavior의 state는 다음과 같습니다.
STATE_EXPANDED : 완전히 펼쳐진 상태
STATE_COLLAPSED : 접혀있는 상태
STATE_HIDDEN : 아래로 숨겨진 상태 (보이지 않음)
STATE_HALF_EXPANDED : 절반으로 펼쳐진 상태
STATE_DRAGGING : 드래깅되고 있는 상태
STATE_SETTLING : 드래그/스와이프 직후 고정된 상태
다음과 같이 state를 조정할 수 있습니다.
                         */

                        if (newState == BottomSheetBehavior.STATE_COLLAPSED) {
                            finish();
                        }
                    }

                    @Override
                    public void onSlide(@NonNull View bottomSheet, float slideOffset) {}
                });
    }
}