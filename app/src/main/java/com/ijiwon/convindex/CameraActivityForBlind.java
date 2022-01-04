package com.ijiwon.convindex;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.hardware.Sensor;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.camerakit.CameraKitView;
import com.ijiwon.convindex.deeputils.Classifier;
import com.ijiwon.convindex.deeputils.TensorFlowImageClassifier;
import com.ijiwon.convindex.tools.OnSwipeTouchListener;
import com.ijiwon.convindex.tools.ShakeDetector;

import java.util.List;
import java.util.Locale;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class CameraActivityForBlind extends AppCompatActivity {

    private static final String MODEL_PATH = "model.tflite";
    private static final boolean QUANT = true;
    private static final String LABEL_PATH = "labels.txt";
    private static final int INPUT_SIZE = 300;

    private Classifier classifier;

    private Executor executor = Executors.newSingleThreadExecutor();
    private CameraKitView cameraKitView;

    public static Classifier.Recognition dialog_result=null;
    public static Bitmap dialog_bitmap=null;

    //shake detector
    // The following are used for the shake detection
    private SensorManager mSensorManager;
    private Sensor mAccelerometer;
    private ShakeDetector mShakeDetector;

    //for tts
    //소리나게 하는 모듈
    public static TextToSpeech textToSpeech;

    //for cursor
    private boolean selectBoolean=false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_camera);
        cameraKitView=findViewById(R.id.cameraBlind);

        //for gesture
        View gestureView = findViewById(R.id.gestureView);

        gestureView.setOnTouchListener(new OnSwipeTouchListener(CameraActivityForBlind.this) {

            public void LongTap(){

                if(!selectBoolean ){ //& stack!=null){
                    CameraActivityForBlind.textToSpeech.speak("먼저 아래로 스왑해 물건을 선택해주세요",TextToSpeech.QUEUE_ADD, null, null);

                }
                else{

                    //pop up a product from stack
                }
            }

            public void DoubleTap(){

                //take a photo
                cameraKitView.captureImage(new CameraKitView.ImageCallback() {
                    @Override
                    public void onImage(CameraKitView cameraKitView, final byte[] capturedImage) {

                        Bitmap whatIsThis= byteArrayToBitmap(capturedImage);

                        whatIsThis=Bitmap.createScaledBitmap(whatIsThis, INPUT_SIZE, INPUT_SIZE, false);


                        final List<Classifier.Recognition> results = classifier.recognizeImage(whatIsThis);
                        Toast.makeText(CameraActivityForBlind.this,results.toString(),Toast.LENGTH_SHORT).show();

                        if( afterPicture(results) != null ){

                            selectBoolean=true; // i decide to take this product.

                            //i have to put product json in stack.


                            dialog_bitmap=Bitmap.createScaledBitmap(whatIsThis, 150, 150, false);

                            // tts로 afterpicture의 정보를 바로 말해준다.

                        }
                    }
                });

            }

            public void onSwipeTop() {

                if(!selectBoolean ){ //& stack!=null){
                    CameraActivityForBlind.textToSpeech.speak("먼저 아래로 스왑해 물건을 선택해주세요",TextToSpeech.QUEUE_ADD, null, null);

                }
                else{

                    //pop up a product from stack
                }

            }
            public void onSwipeRight() {
                //Toast.makeText(CameraActivity.this, "right", Toast.LENGTH_SHORT).show();
            }

            public void onSwipeLeft() {
                //Toast.makeText(CameraActivity.this, "left", Toast.LENGTH_SHORT).show();
            }

            public void onSwipeBottom() {

                //FragmentDialog dialog = FragmentDialog.getInstance();                ///fragment dialog 불러오기
                //dialog.show(getSupportFragmentManager(), "dialog");        //나중에 변동사항 공지때 띄우

                //물건 장바구니 안으로 담기
                /*
                if(MyGlobals.getInstance().getMaxName()!=null) {
                    CameraActivity.textToSpeech.speak(MyGlobals.getInstance().getMaxName() + "을 장바구니에 담습니다!", TextToSpeech.QUEUE_ADD, null, null);
                    MainActivity.Cartmap.put(MyGlobals.getInstance().getMaxName(), MainActivity.map.get(MyGlobals.getInstance().getMaxName()));
                    totalPrice += MainActivity.Cartmap.get(MyGlobals.getInstance().getMaxName());
                    textView.setText(String.valueOf(totalPrice));
                }
                CameraActivity.textToSpeech.speak("방금 담은 물건을 취소하고 싶으시면 위쪽 방향로 스크롤 해주세요!!!", TextToSpeech.QUEUE_ADD, null, null);

                 */

            }

        });


        // 흔들면 작동하는 기능 추가하기
        mSensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        mAccelerometer = mSensorManager
                .getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        mShakeDetector = new ShakeDetector();
        mShakeDetector.setOnShakeListener(new ShakeDetector.OnShakeListener() {
            @Override
            public void onShake(int count) {

                if (count >= 1) {
                    //totalPrice=0;
                    //CameraActivity.textToSpeech.speak("장바구니를 비웠습니다!", TextToSpeech.QUEUE_ADD, null, null);
                    //textView.setText(String.valueOf(totalPrice));

                }
            }
        });


        //tensorflow init()
        initTensorFlowAndLoadModel();


        //소리나게 하는 기능
        textToSpeech = new TextToSpeech(getApplicationContext(), new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int status) {
                if (status == TextToSpeech.SUCCESS) {
                    int ttsLang = textToSpeech.setLanguage(Locale.KOREAN);

                    if (ttsLang == TextToSpeech.LANG_MISSING_DATA
                            || ttsLang == TextToSpeech.LANG_NOT_SUPPORTED) {
                        //Log.e("TTS", "The Language is not supported!");
                    } else {
                        //Log.i("TTS", "Language Supported.");
                    }
                    //Log.i("TTS", "Initialization success.");
                } else {
                    Toast.makeText(getApplicationContext(), "TTS Initialization failed!", Toast.LENGTH_SHORT).show();
                }
            }
        });
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
        mSensorManager.registerListener(mShakeDetector, mAccelerometer,    SensorManager.SENSOR_DELAY_UI);

    }

    @Override
    protected void onPause() {
        cameraKitView.onPause();
        mSensorManager.unregisterListener(mShakeDetector);
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