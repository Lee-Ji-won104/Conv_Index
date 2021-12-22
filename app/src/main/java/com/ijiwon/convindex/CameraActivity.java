package com.ijiwon.convindex;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.camerakit.CameraKitView;
import com.ijiwon.convindex.deeputils.Classifier;
import com.ijiwon.convindex.deeputils.TensorFlowImageClassifier;
import com.ijiwon.convindex.tools.FragmentDialog;

import java.io.File;
import java.io.FileOutputStream;
import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class CameraActivity extends AppCompatActivity {

    private static final String MODEL_PATH = "mobilenet_quant_v1_224.tflite";
    private static final boolean QUANT = true;
    private static final String LABEL_PATH = "labels.txt";
    private static final int INPUT_SIZE = 224;

    private Classifier classifier;

    private Executor executor = Executors.newSingleThreadExecutor();
    private CameraKitView cameraKitView;

    public static Classifier.Recognition dialog_result=null;
    public static Bitmap dialog_bitmap=null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_camera);
        cameraKitView=findViewById(R.id.camera);

        Button buttonTensor = (Button) findViewById(R.id.buttonTensor) ;
        buttonTensor.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View view) {
                cameraKitView.captureImage(new CameraKitView.ImageCallback() {
                    @Override
                    public void onImage(CameraKitView cameraKitView, final byte[] capturedImage) {

                        Bitmap whatIsThis= byteArrayToBitmap(capturedImage);

                        whatIsThis=Bitmap.createScaledBitmap(whatIsThis, INPUT_SIZE, INPUT_SIZE, false);


                        final List<Classifier.Recognition> results = classifier.recognizeImage(whatIsThis);
                        Toast.makeText(CameraActivity.this,results.toString(),Toast.LENGTH_SHORT).show();

                        if( afterPicture(results) != null ){
                            dialog_bitmap=Bitmap.createScaledBitmap(whatIsThis, 150, 150, false);
                            FragmentDialog dialog = FragmentDialog.getInstance();                ///fragment dialog 불러오기
                            dialog.show(getSupportFragmentManager(), "dialog");        //나중에 변동사항 공지때 띄우

                        }
                    }
                });
            }
        });
        //tensorflow init()
        initTensorFlowAndLoadModel();
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

    private Classifier.Recognition afterPicture(List<Classifier.Recognition> results){
        float maxFloat=0;
        Classifier.Recognition maxResult = null;

        for (Classifier.Recognition result:results){
            if(result.getConfidence()>maxFloat){
                maxResult= result;
            }
        }
        dialog_result=maxResult;

        return maxResult;
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


}