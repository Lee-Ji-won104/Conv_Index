package com.ijiwon.convindex;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.camerakit.CameraKitView;
import com.ijiwon.convindex.deeputils.Classifier;
import com.ijiwon.convindex.deeputils.TensorFlowImageClassifier;

import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class CameraActivity extends AppCompatActivity {

    private static final String MODEL_PATH = "model.tflite";
    private static final boolean QUANT = true;
    private static final String LABEL_PATH = "class-names.txt";
    private static final int INPUT_SIZE = 512;

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

        Button buttonTensor = findViewById(R.id.buttonTensor);
        buttonTensor.setOnClickListener(view -> cameraKitView.captureImage((cameraKitView, capturedImage) -> {

            Bitmap whatIsThis= byteArrayToBitmap(capturedImage);

            whatIsThis=Bitmap.createScaledBitmap(whatIsThis, INPUT_SIZE, INPUT_SIZE, false);


            final List<Classifier.Recognition> results = classifier.recognizeImage(whatIsThis);

            if( afterPicture(results) != null ){
                Toast.makeText(CameraActivity.this,afterPicture(results).toString(),Toast.LENGTH_SHORT).show();

                dialog_bitmap=Bitmap.createScaledBitmap(whatIsThis, 150, 150, false);

            }
        }));
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