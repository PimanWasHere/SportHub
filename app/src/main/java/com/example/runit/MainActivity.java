package com.example.runit;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.ProgressBar;
import android.widget.Toast;
import com.goodiebag.pinview.Pinview;
import com.yakivmospan.scytale.Store;

public class MainActivity extends AppCompatActivity {


    String pinstring;

    Pinview pinview;

    private ProgressBar spinner;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Store store = new Store(getApplicationContext());

        spinner = (ProgressBar)findViewById(R.id.progressBar);
        spinner.setVisibility(View.GONE);

        pinview = (Pinview) findViewById(R.id.mypinview);

        pinview.setPinViewEventListener(new Pinview.PinViewEventListener() {
            @Override
            public void onDataEntered(Pinview pinview, boolean fromUser) {

                spinner.setVisibility(View.VISIBLE);

                // if pin entered is equal to keystore held

                String pinin = pinview.getValue();


                // 99999 to install new

                if (pinview.getValue().equals("99999")) {

                    System.out.println("install - open to set new pin in wallet");

                    openActivityinstallacc();

                } else if (store.hasKey(pinin)) {

                    openActivitylogon();

                } else {
                    spinner.setVisibility(View.GONE);

                    Toast.makeText(getApplicationContext(), pinview.getValue() + " is not in the keystore.", Toast.LENGTH_LONG).show();

                }


            }


        });
    }



    public void openActivitylogon() {
        spinner.setVisibility(View.GONE);

        Intent intent = new Intent(this, com.example.runit.Activitylogon.class);
     // intent.putExtra("pin", pinstring);
      startActivity(intent);
    }

    public void openActivityinstallacc () {
        spinner.setVisibility(View.GONE);
        Intent intent = new Intent(this, com.example.runit.Activityinstalleracc.class);

        startActivity(intent);
    }



}