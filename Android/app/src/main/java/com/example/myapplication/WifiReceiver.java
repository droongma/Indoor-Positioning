package com.example.myapplication;

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

import com.chaquo.python.PyObject;
import com.chaquo.python.Python;
import com.chaquo.python.android.AndroidPlatform;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

class WifiReceiver extends BroadcastReceiver {

    WifiManager wifiManager;
    StringBuilder sb;
    ListView wifiDeviceList;
    String APname="123";

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
            Workbook workbook=new HSSFWorkbook();
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
            }

            Log.d("python","pythonstarting");
            if(!Python.isStarted()){
                Python.start(new AndroidPlatform(context));
                Log.d("python","pythonstart success");
            }
            Python py = Python.getInstance();
            //PyObject python=py.getModule("pythontest");
            // here we add arguments for func backwards
            //PyObject obj=python.callAttr("main");
            PyObject python=py.getModule("using_model_example");
            PyObject obj=python.callAttr("main");

            Log.d("pythonresult",obj.toString());


            ArrayAdapter arrayAdapter = new ArrayAdapter(context, android.R.layout.simple_list_item_1, deviceList.toArray());

            wifiDeviceList.setAdapter(arrayAdapter);
        }
    }
}
