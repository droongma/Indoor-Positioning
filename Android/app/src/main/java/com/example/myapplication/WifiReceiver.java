package com.example.myapplication;

import android.content.res.AssetFileDescriptor;
import android.content.res.AssetManager;
import android.os.Bundle;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiManager;
import android.os.Debug;
import android.os.Environment;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;
/*
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
*/
import org.tensorflow.lite.Interpreter;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

class WifiReceiver extends BroadcastReceiver {

    WifiManager wifiManager;
    StringBuilder sb;
    ListView wifiDeviceList;
    String APname="123";
    public int tflite_Result=0;

    public WifiReceiver(WifiManager wifiManager, ListView wifiDeviceList) {
        this.wifiManager = wifiManager;
        this.wifiDeviceList = wifiDeviceList;
    }

    public void onReceive(Context context, Intent intent) {
        String action = intent.getAction();
        if (WifiManager.SCAN_RESULTS_AVAILABLE_ACTION.equals(action)) {
            List<ScanResult> wifiList = wifiManager.getScanResults();

            ArrayList<String> deviceList = new ArrayList<>();
            for (ScanResult scanResult : wifiList) {
                if(!scanResult.SSID.equals(" ") && !scanResult.capabilities.equals(" ")){
                    deviceList.add("BSSID: "+ scanResult.BSSID + "\nSSID : "+ scanResult.SSID+"\nRSSI: " + scanResult.level );
                }
            }
            // 여긴 이제 안써도됨
            /*Workbook workbook=new HSSFWorkbook();
            Sheet sheet=workbook.createSheet();
            Row row=sheet.createRow(0);
            Cell cell;
            cell=row.createCell(0);
            cell.setCellValue("BSSID");
            cell=row.createCell(1);
            cell.setCellValue("SSID");
            cell=row.createCell(2);
            cell.setCellValue("RSSI");

            for(int i=0;i<wifiList.size();i++){
                row=sheet.createRow(i+1);
                cell=row.createCell(0);
                cell.setCellValue(wifiList.get(i).BSSID);
                cell=row.createCell(1);
                cell.setCellValue(wifiList.get(i).SSID);
                cell=row.createCell(2);
                cell.setCellValue(wifiList.get(i).level);
            }

            File xlsFile=new File(context.getExternalFilesDir(null),APname+".xls");
            if(!xlsFile.exists())
                xlsFile.mkdirs();
            File outputFile= new File(xlsFile.getPath(),APname+".xls");
            try {
                FileOutputStream OS= new FileOutputStream(outputFile);
                workbook.write(OS);
            } catch (IOException e) {
                e.printStackTrace();
            }*/


            HashMap<String,Integer> from_ap_mapping=new HashMap<>();
            AssetManager assetManager= context.getResources().getAssets();
            InputStreamReader is=null;
            try {
                is=new InputStreamReader(context.getAssets().open("ap_mapping.csv"));
                BufferedReader br = new BufferedReader(is);
                String line="";
                while((line=br.readLine())!=null){
                    String[] token=line.split(",");
                    from_ap_mapping.put(token[0],-110);
                    //Log.d("fromcsv",token[0] + ", "+token[1]);
                }
                for(ScanResult scanResult :wifiList){
                    from_ap_mapping.put(scanResult.BSSID, Math.max(scanResult.level, -110));
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
            /// reading csv file : csv 파일 읽기
            Object[] arr=from_ap_mapping.values().toArray();
            float[] scale_arr=new float[arr.length];
            try{
                is=new InputStreamReader(context.getAssets().open("feature_scale_list.csv"));
                BufferedReader br = new BufferedReader(is);
                String line="";
                int i=0;
                while((line=br.readLine())!=null) {
                    String[] token = line.split(",");
                    scale_arr[i]=(int)arr[i]*Float.parseFloat(token[0]);
                    i++;
                    //Log.d("csv2",i+":"+token[0]+" * "+arr[i]+"= "+scale_arr[i]);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }


            // tflite model creation : tflite 모델 읽어와서 interpreter로 만들기
            Interpreter tflite = null;
            try {
                AssetFileDescriptor fileDescriptor=context.getAssets().openFd("model_tflite_version.tflite");
                FileInputStream inputStream=new FileInputStream(fileDescriptor.getFileDescriptor());
                FileChannel fileChannel= inputStream.getChannel();
                long startOffset=fileDescriptor.getStartOffset();
                long declaredLength=fileDescriptor.getDeclaredLength();
                tflite=new Interpreter(fileChannel.map(FileChannel.MapMode.READ_ONLY,startOffset,declaredLength));
            } catch (IOException e) {
                e.printStackTrace();
            }
            float[][] output=new float[1][18];
            tflite.run(scale_arr,output);

            // post processing tflite : tflite 결과 후처리
            float max=-1;
            for(int i=0;i<output[0].length;i++){
                if(output[0][i]>max){
                    max=output[0][i];
                    tflite_Result=i;
                }
            }
            Log.d("tflite_Result", String.valueOf(tflite_Result));


            // for setting arrayAdapter in MainActivity : 메인에 값 표시하기위해 어댑터 설정
            ArrayAdapter arrayAdapter = new ArrayAdapter(context, android.R.layout.simple_list_item_1, deviceList.toArray());

            wifiDeviceList.setAdapter(arrayAdapter);
        }
    }
}
