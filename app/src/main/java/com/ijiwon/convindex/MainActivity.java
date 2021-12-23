package com.ijiwon.convindex;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.camerakit.CameraKitView;
import com.google.gson.Gson;
import com.ijiwon.convindex.tools.ProductClass;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.InputStream;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        //use thread
        ArrayList<ProductClass> a= getProductList();


        Button button1 = (Button) findViewById(R.id.button) ;
        button1.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent= new Intent(MainActivity.this, CameraActivity.class);
                startActivity(intent);
            }
        });

        Button button2 = (Button) findViewById(R.id.button2) ;
        button2.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent= new Intent(MainActivity.this, CameraActivityForBlind.class);
                startActivity(intent);
            }
        });
    }

    public ArrayList<ProductClass> getProductList() {
        ArrayList<ProductClass> list_product = new ArrayList<>();
        Gson gson = new Gson();

        try {
            InputStream is = getAssets().open("product.json");
            byte[] buffer = new byte[is.available()];
            is.read(buffer);
            is.close();
            String json = new String(buffer, "UTF-8");

            JSONObject jsonObject = new JSONObject(json);
            JSONArray jsonArray = jsonObject.getJSONArray("product");

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