package com.example.runit;

import android.os.Bundle;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import java.io.IOException;
import java.math.BigInteger;
import java.security.GeneralSecurityException;

public class Activitycreateacc extends AppCompatActivity {

    // Soul can be Athlete, Fan, Sponsor, Organizer, Content generator, Partner .. many roles at same or differing times
    // string public rolecodes;  // A/F/S/O/C/P


    BigInteger multiplier108 = new BigInteger("100000000");

    BigInteger multiplier1018 = new BigInteger("1000000000000000000");

    String rolecode;

    public Activitycreateacc() throws IOException, GeneralSecurityException {
       }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_createacc);

        Button createprofilebut = (Button) findViewById(R.id.newprofilebutton);

        EditText nicknamein = (EditText) findViewById(R.id.nickname);
        EditText fnamein = (EditText) findViewById(R.id.fname);
        EditText lnamein = (EditText) findViewById(R.id.lname);
        EditText nationality = (EditText) findViewById(R.id.nationality);

        CheckBox athlete = (CheckBox) findViewById(R.id.chkboxathlete);
        CheckBox fan = (CheckBox) findViewById(R.id.chkboxfan);
        CheckBox sponsor = (CheckBox) findViewById(R.id.chkboxsponsor);
        CheckBox organiser = (CheckBox) findViewById(R.id.chkboxevntorg);
        CheckBox partner = (CheckBox) findViewById(R.id.chkboxpartner);
        CheckBox contentprov = (CheckBox) findViewById(R.id.chkboxcontentprov);


        // Soul can be Athlete, Fan, Sponsor, Organizer, Content generator, Partner .. many roles at same or differing times
       // string private rolecodes;  // A/F/S/O/C/P

        // validation

        if (nicknamein.getText().equals(null)) {
            Toast.makeText(getApplicationContext(), "Nickname cannot be blank", Toast.LENGTH_LONG).show();
            return;
        }

        if (fnamein.getText().equals(null)) {
            Toast.makeText(getApplicationContext(), "First name cannot be blank", Toast.LENGTH_LONG).show();
            return;
        }

        if (lnamein.getText().equals(null)) {
            Toast.makeText(getApplicationContext(), "Last name cannot be blank", Toast.LENGTH_LONG).show();
            return;
        }


        if (!athlete.hasSelection() && !fan.hasSelection() && !sponsor.hasSelection() && !organiser.hasSelection() && !partner.hasSelection() && !contentprov.hasSelection())
        {
        Toast.makeText(getApplicationContext(), "Must have a current role, 1 selection minimum", Toast.LENGTH_LONG).show();
        return;
        }

            // build role string


        if (athlete.hasSelection()) {
            //append to getString()

            rolecode = rolecode + "A/";
            return;
        }

        if (fan.hasSelection()) {
            //append to getString()

            rolecode = rolecode + "F/";
            return;
        }
        if (sponsor.hasSelection()) {
            //append to getString()

            rolecode = rolecode + "S";
            return;
        }
        if (organiser.hasSelection()) {
            //append to getString()

            rolecode = rolecode + "O/";
            return;
        }
        if (contentprov.hasSelection()) {
            //append to getString()

            rolecode = rolecode + "C/";
            return;
        }
        if (partner.hasSelection()) {
            //append to getString()

            rolecode = rolecode + "P/";
            return;
        }



    }



}
