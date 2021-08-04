package com.example.runit;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;
import com.goodiebag.pinview.Pinview;
import com.hedera.hashgraph.sdk.BadMnemonicException;
import com.hedera.hashgraph.sdk.ContractId;
import com.hedera.hashgraph.sdk.PrecheckStatusException;
import com.hedera.hashgraph.sdk.ReceiptStatusException;
import com.yakivmospan.scytale.Store;

import java.io.UnsupportedEncodingException;
import java.math.BigInteger;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.util.concurrent.TimeoutException;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;

public class MainActivity extends AppCompatActivity {


    String pinstring;

    Pinview pinview;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Store store = new Store(getApplicationContext());
        Button createviraccntbutt = (Button) findViewById(R.id.createvirginaccnt);

        pinview = (Pinview) findViewById(R.id.mypinview);

        pinview.setPinViewEventListener(new Pinview.PinViewEventListener() {
            @Override
            public void onDataEntered(Pinview pinview, boolean fromUser) {


                // if pin entered is equal to keystore held

                String pinin = pinview.getValue();


                // 99999 to install new

                if (store.hasKey(pinin)) {

                    openActivitylogon();

                } else {


                    Toast.makeText(getApplicationContext(), pinview.getValue() + " is not in the keystore, enter valid PIN, or create a new Account", Toast.LENGTH_LONG).show();

                }


            }


        });


        createviraccntbutt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                openActivityinstallacc();
            }

        });



    }






    public void openActivitylogon() {

        Intent intent = new Intent(this, com.example.runit.Activitylogon.class);
       startActivity(intent);
    }

    public void openActivityinstallacc () {

        Intent intent = new Intent(this, com.example.runit.Activityinstalleracc.class);
        startActivity(intent);
    }



}