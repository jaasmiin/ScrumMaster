package com.example.scrummaster.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.activity.result.ActivityResultLauncher;

import com.aldebaran.qi.sdk.QiContext;
import com.aldebaran.qi.sdk.QiSDK;
import com.aldebaran.qi.sdk.RobotLifecycleCallbacks;
import com.aldebaran.qi.sdk.design.activity.RobotActivity;
import com.example.scrummaster.R;
import com.journeyapps.barcodescanner.ScanContract;
import com.journeyapps.barcodescanner.ScanOptions;

import java.util.ArrayList;

public class  MainActivity extends RobotActivity implements RobotLifecycleCallbacks {

    Button btn_scan;
    Button btn_auswahlmnu;
    ArrayList <String> teilnehmerliste = new ArrayList<String>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        // Register the RobotLifecycleCallbacks to this Activity.
        QiSDK.register(this, this);
        setContentView(R.layout.activity_main);
        btn_scan =findViewById(R.id.btn_scan);
        btn_auswahlmnu= findViewById(R.id.btn_auswahlmnu);
        //Beim klicken auf den Button "Scan" wird der BarcodeScanner geöffnet
        btn_scan.setOnClickListener(v -> {scanCode();

        });
        //Beim klicken auf den Button "Auswahlmenü" wechselt die View zum Auswahlmenü, die


        btn_auswahlmnu.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
            startActivity(new Intent(MainActivity.this,AuswahlmenueActivity.class));
            }
        });

    }


        private void scanCode() {
            ScanOptions options =  new ScanOptions();
            options.setPrompt("Herzlich Willkommen");
            options.setBeepEnabled(true);
            options.setOrientationLocked(true);
            options.setCaptureActivity(CaptureAct.class);
            barLauncher.launch(options);
    }

    //Prüfen ob etwas vom scanner returend wurde,wenn ja wechseln in WillkommenActivity, gescannter Name wird per Intent übertragen
    // an WillkommenActivity
    ActivityResultLauncher<ScanOptions> barLauncher =registerForActivityResult(new ScanContract(), result -> {
        if (result.getContents() != null) {

            result.getContents();
            startActivity(new Intent(MainActivity.this, WillkommenActivity.class));

            Intent intent = new Intent(MainActivity.this, WillkommenActivity.class);
            intent.putExtra("scannende_person", result.getContents());
            startActivity(intent);

        }
    });

    @Override
    protected void onDestroy() {
        setContentView(R.layout.activity_main);
        // Unregister the RobotLifecycleCallbacks for this Activity.
        QiSDK.unregister(this, this);
        super.onDestroy();
    }

    @Override
    public void onRobotFocusGained(QiContext qiContext) {
        // The robot focus is gained.
    }

    @Override
    public void onRobotFocusLost() {
        // The robot focus is lost.
    }

    @Override
    public void onRobotFocusRefused(String reason) {
        // The robot focus is refused.
    }
}
