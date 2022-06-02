package com.ijiwon.convindex;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Point;
import android.os.Bundle;
import android.os.Handler;
import android.view.Display;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.camerakit.CameraKitView;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.gson.Gson;

import com.ijiwon.convindex.bottomSheetFrag.CartFragment;
import com.ijiwon.convindex.bottomSheetFrag.IndexFragment;
import com.ijiwon.convindex.bottomSheetFrag.RecommendFragment;
import com.ijiwon.convindex.bottomSheetFrag.SettingFragment;
import com.ijiwon.convindex.deeputils.Classifier;
import com.ijiwon.convindex.deeputils.TensorFlowImageClassifier;
import com.ijiwon.convindex.tools.ProductClass;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class CameraActivity extends AppCompatActivity {

    private static final String MODEL_PATH = "model.tflite";
    private static final boolean QUANT = true;
    private static final String LABEL_PATH = "labels.txt";
    private static final int INPUT_SIZE = 300;

    private Classifier classifier;

    private Executor executor = Executors.newSingleThreadExecutor();
    private CameraKitView cameraKitView;

    public static ArrayList<ProductClass> a;

    public static Bitmap dialog_bitmap=null;

    ProgressDialog progressDialog;

    AnalyzingFragment analyzingFragment;

    //bottomSheet
    private LinearLayout bottomSheetLayout;
    private LinearLayout gestureLayout;
    private BottomSheetBehavior<LinearLayout> sheetBehavior;
    protected ImageView bottomSheetArrowImageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_camera);
        cameraKitView=findViewById(R.id.camera);

        initTensorFlowAndLoadModel();

        bottomSheetLayout = findViewById(R.id.bottom_sheet_layout);
        gestureLayout = findViewById(R.id.gesture_layout);
        sheetBehavior = BottomSheetBehavior.from(bottomSheetLayout);
        //bottomSheetArrowImageView = findViewById(R.id.bottom_sheet_arrow);

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

        sheetBehavior.setState(BottomSheetBehavior.STATE_HIDDEN);

        sheetBehavior.setBottomSheetCallback(
                new BottomSheetBehavior.BottomSheetCallback() {
                    @Override
                    public void onStateChanged(@NonNull View bottomSheet, int newState) {

                        switch (newState) {

                            case BottomSheetBehavior.STATE_HIDDEN:
                                break;

                            case BottomSheetBehavior.STATE_EXPANDED:
                            {
                                bottomSheetArrowImageView.setImageResource(R.drawable.icn_chevron_down);
                            }
                            break;

                            case BottomSheetBehavior.STATE_COLLAPSED:
                            {
                                bottomSheetArrowImageView.setImageResource(R.drawable.icn_chevron_up);
                            }
                            break;

                            case BottomSheetBehavior.STATE_DRAGGING:
                                break;

                            case BottomSheetBehavior.STATE_SETTLING:
                                bottomSheetArrowImageView.setImageResource(R.drawable.icn_chevron_up);
                                break;
                        }
                    }

                    @Override
                    public void onSlide(@NonNull View bottomSheet, float slideOffset) {}
                });


        Button buttonTensor = (Button) findViewById(R.id.buttonTensor) ;
        buttonTensor.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View view) {
                cameraKitView.captureImage(new CameraKitView.ImageCallback() {
                    @Override
                    public void onImage(CameraKitView cameraKitView, final byte[] capturedImage) {

                        progressDialog = new ProgressDialog(CameraActivity.this);
                        progressDialog.setMessage("무엇인지 알아내는 중...");
                        progressDialog.setCancelable(false);
                        progressDialog.setProgressStyle(android.R.style.Widget_ProgressBar_Horizontal);
                        progressDialog.show();

                        Bitmap whatIsThis= byteArrayToBitmap(capturedImage);

                        whatIsThis=Bitmap.createScaledBitmap(whatIsThis, INPUT_SIZE, INPUT_SIZE, false);

                        dialog_bitmap=whatIsThis;

                        startRecognize(whatIsThis);

                    }
                });
            }
        });

        //tensorflow init()

    }

    @Override
    protected void onStart() {
        super.onStart();
        cameraKitView.onStart();
    }

    @Override
    protected void onResume() {
        super.onResume();
        cameraKitView.onResume();

    }

    @Override
    protected void onPause() {
        cameraKitView.onPause();
        super.onPause();
    }

    @Override
    protected void onStop() {
        cameraKitView.onStop();
        super.onStop();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        cameraKitView.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    private String afterPicture(List<Classifier.Recognition> results){
        float maxFloat=0;
        Classifier.Recognition maxResult = null;

        for (Classifier.Recognition result:results){
            if(result.getConfidence()>maxFloat){
                maxResult= result;
            }
        }

        return maxResult.getTitle();
    }

    private void makeResultBitmap(Classifier.Recognition result, Bitmap bitmap){

    }

    private void initTensorFlowAndLoadModel() {
        executor.execute(new Runnable() {
            @Override
            public void run() {
                try {
                    classifier = TensorFlowImageClassifier.create(
                            getAssets(),
                            MODEL_PATH,
                            LABEL_PATH,
                            INPUT_SIZE,
                            QUANT);
                } catch (final Exception e) {
                    throw new RuntimeException("Error initializing TensorFlow!", e);
                }
            }
        });
    }

    public Bitmap byteArrayToBitmap( byte[] $byteArray ) {
        return BitmapFactory.decodeByteArray( $byteArray, 0, $byteArray.length );
    }

    private void startRecognize(Bitmap bitmap) {


        Handler handler = new Handler();
        handler.post(new Runnable() {
            @Override
            public void run() {
                final List<Classifier.Recognition> results = classifier.recognizeImage(bitmap);

                //제품 어레이 갖고 오기

                if(results.size()>0){
                    a= getProductList(afterPicture(results));
                }
                progressDialog.dismiss();

                if(a!=null){
                    analyzingFragment = new AnalyzingFragment();
                    getSupportFragmentManager().beginTransaction().replace(R.id.container, analyzingFragment).commit();
                }

            }
        });
    }

    public ArrayList<ProductClass> getProductList(String product) {
        ArrayList<ProductClass> list_product = new ArrayList<>();
        Gson gson = new Gson();

        try {
            InputStream is = getAssets().open("product.json");
            byte[] buffer = new byte[is.available()];
            is.read(buffer);
            is.close();
            String json = new String(buffer, "UTF-8");

            JSONObject jsonObject = new JSONObject(json);
            JSONArray jsonArray = jsonObject.getJSONArray(product);

            int index = 0;
            while (index < jsonArray.length()) {
                ProductClass productInfo = gson.fromJson(jsonArray.get(index).toString(), ProductClass.class);
                list_product.add(productInfo);

                index++;
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return list_product;
    }

}