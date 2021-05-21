package com.example.runit;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import java.io.IOException;
import java.math.BigInteger;
import java.security.GeneralSecurityException;

public class Activitycreateacc extends AppCompatActivity {

    // Soul can be Athlete, Fan, Sponsor, Organizer, Content generator, Partner .. many roles at same or differing times
    // string public rolecodes;  // A/F/S/O/C/P


    BigInteger multiplier108 = new BigInteger("100000000");

    BigInteger multiplier1018 = new BigInteger("1000000000000000000");

    public Activitycreateacc() throws IOException, GeneralSecurityException {
       }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_createacc);

        Button requesttokenBtn = (Button) findViewById(R.id.requestbutton);

        EditText howmanycoin = (EditText) findViewById(R.id.buycoineditText);



    }



}
