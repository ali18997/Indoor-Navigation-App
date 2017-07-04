package com.ali.wifiscan;

import android.Manifest;
import android.content.Context;

import android.content.Intent;
import android.content.pm.PackageManager;


import android.net.wifi.ScanResult;

import android.net.wifi.WifiConfiguration;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;

import android.widget.Toast;

import java.util.List;



public class MainActivity extends AppCompatActivity {


    RadioGroup radioGroup;

    RadioButton top, bottom;

    int level;
    int numberOfLevels = 500;


    Button Scan;

    String testCorridorAP1 = "DCE-2";
    String testCorridorAP2 = "DCE-1";
    String testCorridorAP3 = "Project-LAB";
    String adjacentAP1 = "adjacentAP1";
    String adjacentAP2 = "adjacentAP2";
    String adjacentAP3 = "adjacentAP3";
    String adjacentAP4 = "adjacentAP4";
    String adjacentAP5 = "adjacentAP5";
    String adjacentAP6 = "adjacentAP6";

    String Floor = "Bottom";

    String currentLocation;
    String destinationLocation;

    int[] CorridorAPStrengths;
    int[] adjacentAPStrengths;

    TextView location;


    Boolean tAP1;
    Boolean tAP2;
    Boolean tAP3;
    Boolean aAP1;
    Boolean aAP2;
    Boolean aAP3;
    Boolean aAP4;
    Boolean aAP5;
    Boolean aAP6;

    Spinner spinner;
    Spinner manual;

    boolean spinnerFlag = true;
    ArrayAdapter<CharSequence> adapterBottom;
    ArrayAdapter<CharSequence> adapterTop;

    Button navigate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        Scan = (Button) findViewById(R.id.button);
        location = (TextView) findViewById(R.id.textView8) ;

        CorridorAPStrengths = new int[3];
        adjacentAPStrengths = new int[6];

        radioGroup = (RadioGroup) findViewById(R.id.RadioGroup);

        top = (RadioButton) findViewById(R.id.radioButton2);

        bottom = (RadioButton) findViewById(R.id.radioButton);

        spinner = (Spinner) findViewById(R.id.spinner2) ;
        manual = (Spinner) findViewById(R.id.spinner);

        if(spinnerFlag){
            manual.setVisibility(View.INVISIBLE);
            spinnerFlag = false;
        }

        adapterBottom = ArrayAdapter.createFromResource(this, R.array.destination_list_bottom_floor, android.R.layout.simple_spinner_item);
        adapterTop = ArrayAdapter.createFromResource(this, R.array.destination_list_top_floor, android.R.layout.simple_spinner_item);

        adapterBottom.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        adapterTop.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        spinner.setAdapter(adapterBottom);
        manual.setAdapter(adapterBottom);

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener(){
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                destinationLocation = parent.getItemAtPosition(position).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

                // sometimes you need nothing here
            }
        });

        manual.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener(){
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                currentLocation = parent.getItemAtPosition(position).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

                // sometimes you need nothing here
            }
        });

        navigate = (Button) findViewById(R.id.button2);

        navigate.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent nav = new Intent(MainActivity.this, Navigation.class);
                nav.putExtra("key1", Floor);
                nav.putExtra("key2", currentLocation);
                nav.putExtra("key3", destinationLocation);
                startActivity(nav);
                //finish();
            }
        });

        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {


            @Override

            public void onCheckedChanged(RadioGroup group, int checkedId) {

                // find which radio button is selected

                if (checkedId == top.getId()) {

                    Floor = "Top";

                    floorWifiChange();

                    spinner.setAdapter(adapterTop);
                    manual.setAdapter(adapterTop);


                } else if (checkedId == bottom.getId()) {

                    Floor = "Bottom";

                    floorWifiChange();

                    spinner.setAdapter(adapterBottom);
                    manual.setAdapter(adapterBottom);


                } else {

                    Toast.makeText(getApplicationContext(), "Select a Floor",

                            Toast.LENGTH_SHORT).show();
                }

            }
        });


        Scan.setOnClickListener(new View.OnClickListener() {


            public void onClick(View v) {


                for(int i = 0; i  <3; i++) {
                    CorridorAPStrengths[i] = 0;
                }
                for(int i = 0; i < 6; i++) {
                    adjacentAPStrengths[i] = 0;
                }
                tAP1 = false;
                tAP2 = false;
                tAP3 = false;
                aAP1 = false;
                aAP2 = false;
                aAP3 = false;
                aAP4 = false;
                aAP5 = false;
                aAP6 = false;
                getWifi();
                String s = returnLocation();
                location.setText(s);

            }

        });



    }

    private void getWifi() {
        manual.setVisibility(View.INVISIBLE);

        final WifiManager wifiManager = (WifiManager) this.getApplicationContext().getSystemService(Context.WIFI_SERVICE);
        if (checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(new String[]{Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION}, 0x12345);
        } else {
            final WifiInfo wifiInfo;
            wifiInfo = wifiManager.getConnectionInfo();
            final List<ScanResult> results = wifiManager.getScanResults();
            for (int i = 0; i < results.size(); i++) {

                if(results.get(i).SSID.equals(testCorridorAP1)){
                    tAP1 = true;
                    WifiConfiguration wc = new WifiConfiguration();
                    wc.SSID = results.get(i).SSID;
                    wc.allowedKeyManagement.set(WifiConfiguration.KeyMgmt.NONE);
                    wc.BSSID = results.get(i).BSSID;

                    //No password. it should be an open network
                    wc.status = WifiConfiguration.Status.ENABLED;
                    wc.priority = 100000;
                    wc.hiddenSSID = false;
                    int netId = wifiManager.addNetwork(wc);

                    if (netId == -1) {

                        Toast.makeText(getApplicationContext(), "Error connecting to network.", Toast.LENGTH_SHORT).show();
                        return;
                    }

                    wifiManager.enableNetwork(netId, true);
                    wifiManager.setWifiEnabled(true);

                    //Recrod its Strength in Array;
                    //wifiInfo = wifiManager.getConnectionInfo();
                    level = WifiManager.calculateSignalLevel(wifiInfo.getRssi(), numberOfLevels);
                    CorridorAPStrengths[0]=level;

                }
                if(results.get(i).SSID.equals(testCorridorAP2)){
                    tAP2 = true;
                    WifiConfiguration wc = new WifiConfiguration();
                    wc.SSID = results.get(i).SSID;
                    wc.allowedKeyManagement.set(WifiConfiguration.KeyMgmt.NONE);
                    wc.BSSID = results.get(i).BSSID;

                    //No password. it should be an open network
                    wc.status = WifiConfiguration.Status.ENABLED;
                    wc.priority = 100000;
                    wc.hiddenSSID = false;
                    int netId = wifiManager.addNetwork(wc);

                    if (netId == -1) {

                        Toast.makeText(getApplicationContext(), "Error connecting to network.", Toast.LENGTH_SHORT).show();
                        return;
                    }

                    wifiManager.enableNetwork(netId, true);
                    wifiManager.setWifiEnabled(true);

                    //Recrod its Strength in Array;
                    //wifiInfo = wifiManager.getConnectionInfo();
                    level = WifiManager.calculateSignalLevel(wifiInfo.getRssi(), numberOfLevels);
                    CorridorAPStrengths[1]=level;

                }
                if(results.get(i).SSID.equals(testCorridorAP3)){
                    tAP3 = true;
                    WifiConfiguration wc = new WifiConfiguration();
                    wc.SSID = results.get(i).SSID;
                    wc.allowedKeyManagement.set(WifiConfiguration.KeyMgmt.NONE);
                    wc.BSSID = results.get(i).BSSID;

                    //No password. it should be an open network
                    wc.status = WifiConfiguration.Status.ENABLED;
                    wc.priority = 100000;
                    wc.hiddenSSID = false;
                    int netId = wifiManager.addNetwork(wc);

                    if (netId == -1) {

                        Toast.makeText(getApplicationContext(), "Error connecting to network.", Toast.LENGTH_SHORT).show();
                        return;
                    }

                    wifiManager.enableNetwork(netId, true);
                    wifiManager.setWifiEnabled(true);

                    //Record its Strength in Array;
                    //wifiInfo = wifiManager.getConnectionInfo();
                    level = WifiManager.calculateSignalLevel(wifiInfo.getRssi(), numberOfLevels);
                    CorridorAPStrengths[2]=level;

                }
                if(results.get(i).SSID.equals(adjacentAP1)){
                    aAP1 = true;
                    WifiConfiguration wc = new WifiConfiguration();
                    wc.SSID = results.get(i).SSID;
                    wc.allowedKeyManagement.set(WifiConfiguration.KeyMgmt.NONE);
                    wc.BSSID = results.get(i).BSSID;

                    //No password. it should be an open network
                    wc.status = WifiConfiguration.Status.ENABLED;
                    wc.priority = 100000;
                    wc.hiddenSSID = false;
                    int netId = wifiManager.addNetwork(wc);

                    if (netId == -1) {

                        Toast.makeText(getApplicationContext(), "Error connecting to network.", Toast.LENGTH_SHORT).show();
                        return;
                    }

                    wifiManager.enableNetwork(netId, true);
                    wifiManager.setWifiEnabled(true);

                    //Record its Strength in Array;
                    //wifiInfo = wifiManager.getConnectionInfo();
                    level = WifiManager.calculateSignalLevel(wifiInfo.getRssi(), numberOfLevels);
                    adjacentAPStrengths[0]=level;

                }
                if(results.get(i).SSID.equals(adjacentAP2)){
                    aAP2  = true;
                    WifiConfiguration wc = new WifiConfiguration();
                    wc.SSID = results.get(i).SSID;
                    wc.allowedKeyManagement.set(WifiConfiguration.KeyMgmt.NONE);
                    wc.BSSID = results.get(i).BSSID;

                    //No password. it should be an open network
                    wc.status = WifiConfiguration.Status.ENABLED;
                    wc.priority = 100000;
                    wc.hiddenSSID = false;
                    int netId = wifiManager.addNetwork(wc);

                    if (netId == -1) {

                        Toast.makeText(getApplicationContext(), "Error connecting to network.", Toast.LENGTH_SHORT).show();
                        return;
                    }

                    wifiManager.enableNetwork(netId, true);
                    wifiManager.setWifiEnabled(true);

                    //Record its Strength in Array;
                    //wifiInfo = wifiManager.getConnectionInfo();
                    level = WifiManager.calculateSignalLevel(wifiInfo.getRssi(), numberOfLevels);
                    adjacentAPStrengths[1]=level;

                }
                if(results.get(i).SSID.equals(adjacentAP3)){
                    aAP3 = true;
                    WifiConfiguration wc = new WifiConfiguration();
                    wc.SSID = results.get(i).SSID;
                    wc.allowedKeyManagement.set(WifiConfiguration.KeyMgmt.NONE);
                    wc.BSSID = results.get(i).BSSID;

                    //No password. it should be an open network
                    wc.status = WifiConfiguration.Status.ENABLED;
                    wc.priority = 100000;
                    wc.hiddenSSID = false;
                    int netId = wifiManager.addNetwork(wc);

                    if (netId == -1) {

                        Toast.makeText(getApplicationContext(), "Error connecting to network.", Toast.LENGTH_SHORT).show();
                        return;
                    }

                    wifiManager.enableNetwork(netId, true);
                    wifiManager.setWifiEnabled(true);

                    //Record its Strength in Array;
                    //wifiInfo = wifiManager.getConnectionInfo();
                    level = WifiManager.calculateSignalLevel(wifiInfo.getRssi(), numberOfLevels);
                    adjacentAPStrengths[2]=level;

                }
                if(results.get(i).SSID.equals(adjacentAP4)){
                    aAP4 = true;
                    WifiConfiguration wc = new WifiConfiguration();
                    wc.SSID = results.get(i).SSID;
                    wc.allowedKeyManagement.set(WifiConfiguration.KeyMgmt.NONE);
                    wc.BSSID = results.get(i).BSSID;

                    //No password. it should be an open network
                    wc.status = WifiConfiguration.Status.ENABLED;
                    wc.priority = 100000;
                    wc.hiddenSSID = false;
                    int netId = wifiManager.addNetwork(wc);

                    if (netId == -1) {

                        Toast.makeText(getApplicationContext(), "Error connecting to network.", Toast.LENGTH_SHORT).show();
                        return;
                    }

                    wifiManager.enableNetwork(netId, true);
                    wifiManager.setWifiEnabled(true);

                    //Record its Strength in Array;
                    //wifiInfo = wifiManager.getConnectionInfo();
                    level = WifiManager.calculateSignalLevel(wifiInfo.getRssi(), numberOfLevels);
                    adjacentAPStrengths[3]=level;

                }
                if(results.get(i).SSID.equals(adjacentAP5)){
                    aAP5 = true;
                    WifiConfiguration wc = new WifiConfiguration();
                    wc.SSID = results.get(i).SSID;
                    wc.allowedKeyManagement.set(WifiConfiguration.KeyMgmt.NONE);
                    wc.BSSID = results.get(i).BSSID;

                    //No password. it should be an open network
                    wc.status = WifiConfiguration.Status.ENABLED;
                    wc.priority = 100000;
                    wc.hiddenSSID = false;
                    int netId = wifiManager.addNetwork(wc);

                    if (netId == -1) {

                        Toast.makeText(getApplicationContext(), "Error connecting to network.", Toast.LENGTH_SHORT).show();
                        return;
                    }

                    wifiManager.enableNetwork(netId, true);
                    wifiManager.setWifiEnabled(true);

                    //Record its Strength in Array;
                    //wifiInfo = wifiManager.getConnectionInfo();
                    level = WifiManager.calculateSignalLevel(wifiInfo.getRssi(), numberOfLevels);
                    adjacentAPStrengths[4]=level;

                }
                if(results.get(i).SSID.equals(adjacentAP6)){
                    aAP6 = true;
                    WifiConfiguration wc = new WifiConfiguration();
                    wc.SSID = results.get(i).SSID;
                    wc.allowedKeyManagement.set(WifiConfiguration.KeyMgmt.NONE);
                    wc.BSSID = results.get(i).BSSID;

                    //No password. it should be an open network
                    wc.status = WifiConfiguration.Status.ENABLED;
                    wc.priority = 100000;
                    wc.hiddenSSID = false;
                    int netId = wifiManager.addNetwork(wc);

                    if (netId == -1) {

                        Toast.makeText(getApplicationContext(), "Error connecting to network.", Toast.LENGTH_SHORT).show();
                        return;
                    }

                    wifiManager.enableNetwork(netId, true);
                    wifiManager.setWifiEnabled(true);

                    //Record its Strength in Array;
                    //wifiInfo = wifiManager.getConnectionInfo();
                    level = WifiManager.calculateSignalLevel(wifiInfo.getRssi(), numberOfLevels);
                    adjacentAPStrengths[5]=level;

                }


            }

        }

    }




    public String returnLocation ()
    {
        int maxCoridoor = CorridorAPStrengths[0];
        for(int i =1;i<3;i++){if(CorridorAPStrengths[i]>maxCoridoor)maxCoridoor=CorridorAPStrengths[i];}
        int maxAdjacent= adjacentAPStrengths[0];
        for(int i =1;i<6;i++){if(adjacentAPStrengths[i]>maxAdjacent)maxAdjacent=adjacentAPStrengths[i];}

        if(Floor.equals("Bottom")){
            if(maxCoridoor==CorridorAPStrengths[0] && tAP1){
                if(maxAdjacent==adjacentAPStrengths[0]){
                    currentLocation = "Faculty Offices Bottom Floor Right";
                    return "Faculty Offices Bottom Floor Right";
                }
                else if(maxAdjacent==adjacentAPStrengths[1]){
                    currentLocation = "Electronics Lab";
                    return "Electronics Lab";
                }
                else if(maxAdjacent==adjacentAPStrengths[2]){
                    currentLocation = "DSP Lab";
                    return "DSP Lab";
                }
                else {
                    manual.setVisibility(View.VISIBLE);
                    return "Right Side, Bottom Floor";
                }
            }

            else if(maxCoridoor==CorridorAPStrengths[2] && tAP3){
                if(maxAdjacent==adjacentAPStrengths[3]){
                    currentLocation = "Networks Lab";
                    return "Networks Lab";
                }
                else if(maxAdjacent==adjacentAPStrengths[4]){
                    currentLocation = "DIP Lab";
                    return "DIP Lab";
                }
                else if(maxAdjacent==adjacentAPStrengths[5]){
                    currentLocation = "Faculty Offices Bottom Floor Left";
                    return "Faculty Offices Bottom Floor Left";
                }
                else {
                    manual.setVisibility(View.VISIBLE);
                    return "Left Side, Bottom Floor";
                }
            }

            else if(maxCoridoor == CorridorAPStrengths[1] && tAP2){
                currentLocation = "Reception";
                return "Enterance/Reception";
            }
        }

        else if(Floor.equals("Top")){
            if(maxCoridoor==CorridorAPStrengths[0] && tAP1){
                if(maxAdjacent==adjacentAPStrengths[0] && aAP1){
                    currentLocation = "Computation (CTC) Lab";
                    return "Computation (CTC) Lab";
                }
                else if(maxAdjacent==adjacentAPStrengths[1] && aAP2){
                    currentLocation = "CRC-13";
                    return "CRC-13/CRC-14";
                }
                else if(maxAdjacent==adjacentAPStrengths[2] && aAP3){
                    currentLocation = "CRC-11";
                    return "CRC-11";
                }
                else {
                    manual.setVisibility(View.VISIBLE);
                    return "Right Side, Top Floor";
                }
            }
            else if(maxCoridoor==CorridorAPStrengths[2] && tAP3){
                if(maxAdjacent==adjacentAPStrengths[3] && aAP4){
                    currentLocation = "CRC-15";
                    return "CRC-16/CRC-15";
                }
                else if(maxAdjacent==adjacentAPStrengths[4] && aAP5){
                    currentLocation = "ECR";
                    return "ECR";
                }
                else if(maxAdjacent==adjacentAPStrengths[5] && aAP6){
                    currentLocation = "Faculty Offices";
                    return "Faculty Offices";
                }
                else {
                    manual.setVisibility(View.VISIBLE);
                    return "Left Side, Top Floor";
                }
            }
            else if(maxCoridoor == CorridorAPStrengths[1] && tAP2){
                currentLocation = "Faculty Cabins";
                return "Faculty Cabins";
            }
        }

        manual.setVisibility(View.VISIBLE);

        return "Sorry, cannot find your location. Select location manually below:";
    }

    public void floorWifiChange () {

        adjacentAP1 = "adjacentAP1";
        adjacentAP2 = "adjacentAP2";
        adjacentAP3 = "adjacentAP3";
        adjacentAP4 = "adjacentAP4";
        adjacentAP5 = "adjacentAP5";
        adjacentAP6 = "adjacentAP6";
        testCorridorAP1 = "DCE-2";
        testCorridorAP3 = "Project-LAB";

        if(Floor.equals("Bottom")) {
            testCorridorAP2 = "DCE-1";
        }

        else if(Floor.equals("Top")){
            testCorridorAP2 = "DCE-3";
        }

    }





}
