package com.example.runit;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import com.goodiebag.pinview.Pinview;

public class MainActivity extends AppCompatActivity {


    String pinstring;

    Pinview pinview;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Store store = new Store(getApplicationContext());


        pinview = (Pinview) findViewById(R.id.mypinview);
        pinview.setPinViewEventListener(new Pinview.PinViewEventListener() {
            @Override
            public void onDataEntered(Pinview pinview, boolean fromUser) {
                Toast.makeText(getApplicationContext(), pinview.getValue(), Toast.LENGTH_LONG).show();

                // if pin entered is equal to keystore held

                String pinin = pinview.getValue();


                if (store.hasKey(pinin)) {

                    System.out.println("found pin in store !!");


                    // open wallet !
                    // pinstring = pinin;

                    openActivityhbal();
                }

                // temp to enable testing install

                String pin = "99999";


                // TO DO  read internal OR Hedera's keystore to see if there is a encrypted key.. if SO then set message
                // Wallet already has an account, must re-install the wallet to set new PIN


                if (pinview.getValue().equals(pin)) {

                    System.out.println("install - open to set new pin in wallet");

                    openActivityinstallacc();

                    //System.out.println("recognized install pin BUT Keystore NOT found");



                }




            }


        });
    }



    public void openActivityhbal() {
     //   Intent intent = new Intent(this, com.example.runit.Activityhbal.class);
        // intent.putExtra("pin", pinstring);
      //  startActivity(intent);
    }

    public void openActivityinstallacc () {
      //  Intent intent = new Intent(this, com.example.runit.Activityinstalleracc.class);
      //  startActivity(intent);
    }



}