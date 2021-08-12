package com.example.runit;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.hedera.hashgraph.sdk.AccountId;
import com.hedera.hashgraph.sdk.FileId;

import java.math.BigInteger;


public class Activityupdateprofile extends AppCompatActivity {

    // Soul can be ..  so rolecode permitted values P/F/S/C/B/R/D   R=sponsor - for duplicate and ease of install.


    BigInteger multiplier108 = new BigInteger("100000000");

    BigInteger multiplier1018 = new BigInteger("1000000000000000000");

    String rolecode;

    private GennedAccount newDetails;
    private AccountId newAccount;
    private FileId newhederaFileid;

    private String rolearray[];



    public Activityupdateprofile()  {
       }

    Runitprofile runitprofilecurrent;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_createacc);

        Intent intent = getIntent();
        runitprofilecurrent = (Runitprofile) intent.getSerializableExtra("profileobjtupdateprof");

        System.out.println("runit profile3 obj " + runitprofilecurrent.runitprofilescid);

        Button updateprofilebut = (Button) findViewById(R.id.updateaccountbutt);

        EditText nicknamein = (EditText) findViewById(R.id.editTextnicknameedit);
        EditText fnamein = (EditText) findViewById(R.id.editTextfnameedit);
        EditText lnamein = (EditText) findViewById(R.id.editTextlnameedit);

      //  EditText nationality = (EditText) findViewById(R.id.nationality);

        Switch participant = (Switch) findViewById(R.id.switch10edit);
        Switch fan = (Switch) findViewById(R.id.switch11edit);
        Switch spectator = (Switch) findViewById(R.id.switch12edit);
        Switch club = (Switch) findViewById(R.id.switch13edit);
        Switch brand = (Switch) findViewById(R.id.switch14edit);
        Switch sponsor = (Switch) findViewById(R.id.switch15edit);
        Switch developer = (Switch) findViewById(R.id.switch16edit);


        // displaying the existing profile

        nicknamein.setText(runitprofilecurrent.nickname);
        fnamein.setText(runitprofilecurrent.fname);
        lnamein.setText(runitprofilecurrent.lname);


        // parse the rolecode and se the switches


        rolearray = (runitprofilecurrent.rolecode).split("/");

        // has to have at min 1 role


        for (int i = 0; i < rolearray.length; ++i)

        {
             if (rolearray[i].equals("P")) participant.setChecked(true);

            if (rolearray[i].equals("F")) fan.setChecked(true);

            if (rolearray[i].equals("S")) spectator.setChecked(true);

            if (rolearray[i].equals("C")) club.setChecked(true);

            if (rolearray[i].equals("R")) sponsor.setChecked(true);

            if (rolearray[i].equals("B")) brand.setChecked(true);

            if (rolearray[i].equals("D")) developer.setChecked(true);

        }







        updateprofilebut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


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


                rolecode = "";

                Toast.makeText(getApplicationContext(), "Thankyou for your patience.. updating your profile  now ..", Toast.LENGTH_LONG).show();


                // System.out.println("particpant " +  participant.isChecked());

                if (!participant.isChecked() && !fan.isChecked() && !spectator.isChecked() && !club.isChecked() && !brand.isChecked() && !sponsor.isChecked() && !developer.isChecked()){
                    Toast.makeText(getApplicationContext(), "You must have at least one role or many roles at any time", Toast.LENGTH_LONG).show();
                    return;
                }


                // build role string


                if (participant.isChecked()) {

                    rolecode = rolecode + "P/";
                }

                if (fan.isChecked()) {

                    rolecode = rolecode + "F/";
                }
                if (spectator.isChecked()) {

                    rolecode = rolecode + "S/";
                }
                if (club.isChecked()) {

                    rolecode = rolecode + "C/";
                }
                if (brand.isChecked()) {

                    rolecode = rolecode + "B/";
                }
                if (sponsor.isChecked()) {

                    rolecode = rolecode + "R/";
                }

                if (developer.isChecked()) {

                    rolecode = rolecode + "D/";

                }


                // bump to background thread and update the profile SC

            }



        });



    }



}
