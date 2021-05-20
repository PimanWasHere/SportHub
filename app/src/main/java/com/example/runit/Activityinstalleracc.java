package com.example.runit;


import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.yakivmospan.scytale.Store;

import java.math.BigInteger;

import javax.crypto.SecretKey;

public class Activityinstalleracc extends AppCompatActivity {


    static Activityinstalleracc context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_installeracc);



        Store store = new Store(getApplicationContext());

        Button acceptthishhac = (Button) findViewById(R.id.usethisaccbutton);
        Button createprofile = (Button) findViewById(R.id.newprofilebutton);

        EditText pin1 = (EditText) findViewById(R.id.newpin1editText);
        EditText pin2 = (EditText) findViewById(R.id.newpin2editText);
        TextView messagelineout = (TextView) findViewById(R.id.msglinetextView2);


        acceptthishhac.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                messagelineout.setText(" ");

                Toast.makeText(getApplicationContext(), "Created a new PIN# and Run.it Hedera Ledger Account, keep it safe.", Toast.LENGTH_LONG).show();


                //  ask for new 5 digit pin twice

                // set secretkey to pin


                if ((pin1.getText().equals("")) || (pin2.getText().equals(""))) {

                    messagelineout.setText("Your PINs must NOT be empty.");

                    return;
                }

                if ((pin1.length()!= 5) || (pin2.length()!=5)) {

                    messagelineout.setText("Your PINs must both be 5 digits.");

                    return;
                }


                BigInteger pin1in = new BigInteger(pin1.getText().toString());

                BigInteger pin2in = new BigInteger(pin2.getText().toString());

                if (!pin1in.equals(pin2in)) {

                    messagelineout.setText("Your Pins must match.");

                    System.out.println("Pins do not match ");

                    return;
                }


                //  here we show Spinner !   TBD

                SecretKey key = store.generateSymmetricKey(pin1.getText().toString(), null);


                //  disable Spinner and show exceptions or success.

               // messagelineout.setText("The new PIN# " + pin1in + "is safely encrypted in your phone's keystore !");


                Toast.makeText(getApplicationContext(), "Your Run.it App is now secured, Create a quick short profile and your roles.", Toast.LENGTH_LONG).show();

                Intent intent = new Intent(this, com.example.runit.Activitycreateacc.class);
                startActivity(intent);


            }

        });

/*
        createprofile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

               // Toast.makeText(getApplicationContext(), "Coming Soon !", Toast.LENGTH_LONG).show();

               // messagelineout.setText("Created a new Run.it Account will be in the MVP App");

                Intent intent = new Intent(this, com.example.runit.Activitycreateacc.class);
                startActivity(intent);

            }

        });

*/

    }



}
