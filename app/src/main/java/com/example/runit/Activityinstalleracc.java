package com.example.runit;


import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.yakivmospan.scytale.Store;

import java.io.IOException;
import java.math.BigInteger;
import java.security.GeneralSecurityException;

import javax.crypto.SecretKey;

public class Activityinstalleracc extends AppCompatActivity {


    public void Activityinstalleraccc() throws IOException, GeneralSecurityException {
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_installeracc);



        Store store = new Store(getApplicationContext());

        Button acceptthishhac = (Button) findViewById(R.id.logonaccbutton);
        Button createprofile = (Button) findViewById(R.id.dataprefconfirmbutt);

        EditText pin1 = (EditText) findViewById(R.id.newpin1editText);
        EditText pin2 = (EditText) findViewById(R.id.runitaccountidText);

        acceptthishhac.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                //  ask for new 5 digit pin twice

                // set secretkey to pin


                if ((pin1.getText().equals("")) || (pin2.getText().equals(""))) {


                    Toast.makeText(getApplicationContext(), "PIN cannot be blank", Toast.LENGTH_LONG).show();


                    return;
                }

                if ((pin1.length()!= 5) || (pin2.length()!=5)) {


                    Toast.makeText(getApplicationContext(), "PIN must be 5 digits.", Toast.LENGTH_LONG).show();

                    return;
                }


                BigInteger pin1in = new BigInteger(pin1.getText().toString());

                BigInteger pin2in = new BigInteger(pin2.getText().toString());

                if (!pin1in.equals(pin2in)) {

                    Toast.makeText(getApplicationContext(), "PINs do not match, please re-enter", Toast.LENGTH_LONG).show();

                    return;
                }


                SecretKey key = store.generateSymmetricKey(pin1.getText().toString(), null);

                Toast.makeText(getApplicationContext(), "Your Run.it App is now secured, Create a quick short profile and your roles.", Toast.LENGTH_LONG).show();

                openActivitycreateacc();


            }

        });

    }

    public void openActivitycreateacc () {
        Intent intent = new Intent(this, com.example.runit.Activitycreateacc.class);
        startActivity(intent);
    }


}
