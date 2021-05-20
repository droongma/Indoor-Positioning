package com.example.myapplication;

import android.Manifest;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.net.wifi.WifiManager;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    TextView textView;
    ImageView imageView;
    private WifiManager wifiManager;
    private final int MY_PERMISSIONS_ACCESS_COARSE_LOCATION = 1;
    AlertDialog.Builder builder;
    String apinput="init";
    public static Context context;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        builder= new AlertDialog.Builder(this);
        setContentView(R.layout.activity_main);
        WifiReceiver receiverWifi;
        Button button = findViewById(R.id.button);
        imageView = findViewById(R.id.imageView);
        textView = findViewById(R.id.textView);

        context = MainActivity.this;

        wifiManager = (WifiManager) getApplicationContext().getSystemService(Context.WIFI_SERVICE);
        if (!wifiManager.isWifiEnabled()) {
            //Toast.makeText(getApplicationContext(), "Turning WiFi ON...", Toast.LENGTH_LONG).show();
            wifiManager.setWifiEnabled(true);
        }

        WifiReceiver finalReceiverWifi =  new WifiReceiver(wifiManager);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                apinput ="";
                int output = 19;
                if (ActivityCompat.checkSelfPermission(MainActivity.this, Manifest.permission.ACCESS_COARSE_LOCATION)
                        != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(
                            MainActivity.this,
                            new String[]{Manifest.permission.ACCESS_COARSE_LOCATION}, MY_PERMISSIONS_ACCESS_COARSE_LOCATION
                    );
                } else {

                    finalReceiverWifi.run();
                    Toast.makeText(getApplicationContext(),"scanning",Toast.LENGTH_LONG).show();
                }
                //input을 예쁘게 요리하기 ~~
                output = finalReceiverWifi.tflite_Result;
                int region = GetRegion(output);
                ShowLocation(region);


            }
        });
    }

    void ShowLocation(int region){
        String floor;
        String Building;
        if (region <4 ||(region<13 || region>9)) {
            floor = "1st";
        }else if (region<7 || (region >13 || region <17)){
            floor = "2nd";
        }else floor = "3rd";

        if (region<10) Building = "제2과학관";
        else Building = "제1과학관";

        textView.setText("You are in " + Building + " Building\nOn the "+ floor +" floor");

        if (region==1 ||region==6 ||region==7){
            imageView.setImageResource(R.drawable.i167);
        }else if(region ==2 || region==5 || region ==8){
            imageView.setImageResource(R.drawable.i258);
        }else if (region==3||region==4||region==9 ){
            imageView.setImageResource(R.drawable.i349);
        }else if (region==10||region==13||region==16){
            imageView.setImageResource(R.drawable.i101316);
        }else if (region==11 ||region==14||region==17){
            imageView.setImageResource(R.drawable.i111417);
        }else
            imageView.setImageResource(R.drawable.i121518);

    }

    int GetRegion(int Region){
        int output;
        /*
        double percent=1.000;
        for (int i = 0 ; i < 18 ; i ++){
            if (output[0][i]<percent) {
                percent = output[0][i];
                Region = i;
            }
        }
        /*
        0->region1

        1->region10     2->region11     3->region12     4->region13
        5->region14     6->region15     7->region16     8->region17     9->region18

        10->region2     11->region3     12->region4     13->region5
        14->region6     15->region7     16->region8     17->region9
         */
        if(Region>9) output = Region-8;
        else if (Region>0) output = Region+9;
        else output=1;

        return output;
    }



    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        //Toast.makeText(MainActivity.this, "permission granted", Toast.LENGTH_SHORT).show();
        //wifiManager.startScan();
        //Toast.makeText(MainActivity.this, "permission not granted", Toast.LENGTH_SHORT).show();
    }
}
