package com.example.runit;


import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

import java.math.BigInteger;


public class Activityinstalleracc extends AppCompatActivity {

    String newpin;


    public void Activityinstalleraccc()  {
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_installeracc);

        Button acceptthishhac = (Button) findViewById(R.id.logonaccbutton);

        EditText pin1 = (EditText) findViewById(R.id.editTextnickname);
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

                System.out.println("checking pins.");

                BigInteger pin1in = new BigInteger(pin1.getText().toString());

                BigInteger pin2in = new BigInteger(pin2.getText().toString());

                if (!pin1in.equals(pin2in)) {

                    Toast.makeText(getApplicationContext(), "PINs do not match, please re-enter", Toast.LENGTH_LONG).show();

                    return;
                }


               // do this after account created ! - SecretKey key = store.generateSymmetricKey(pin1.getText().toString(), null);


                Pinobj newpinobject = new Pinobj();

                newpinobject.newpin = pin1.getText().toString();

                openActivitycreateacc(newpinobject);


            }

        });

    }

    public void openActivitycreateacc(Pinobj newpinobjectin) {
        Intent intent = new Intent(this, com.example.runit.Activitycreateacc.class);
        intent.putExtra("newpin", newpinobjectin);
        Toast.makeText(getApplicationContext(), "Please Create a quick short profile and your roles.", Toast.LENGTH_LONG).show();

        startActivity(intent);
    }


}
