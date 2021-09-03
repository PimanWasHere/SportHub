package com.example.runit;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Switch;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.hedera.hashgraph.sdk.AccountId;
import com.hedera.hashgraph.sdk.FileId;
import com.hedera.hashgraph.sdk.PrecheckStatusException;
import com.hedera.hashgraph.sdk.ReceiptStatusException;

import java.math.BigInteger;
import java.util.concurrent.TimeoutException;


public class Activityupdateprofile extends AppCompatActivity {

    // Soul can be ..  so rolecode permitted values P/F/S/C/B/R/D   R=sponsor - for duplicate and ease of install.


    BigInteger multiplier108 = new BigInteger("100000000");

    BigInteger multiplier1018 = new BigInteger("1000000000000000000");

    String rolecode, nicknameglobal, fnameglobal, lnameglobal;
    ProgressBar spinupdate;

    EditText nicknameinputprof, fnameinputprof, lnameinputprof;

    Switch participant, fan, spectator, club, brand, sponsor, developer;

    private GennedAccount newDetails;
    private AccountId newAccount;
    private FileId newhederaFileid;

    private String rolearray[] = null;


    public Activityupdateprofile() {
    }

    Runitprofile runitprofilesource;

    Runitprofile runitprofilecurrent;

    Runitprofile runitprofileupdated;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_updateprofile);

        spinupdate = (ProgressBar) findViewById(R.id.progressBarupdate);

        Intent intent = getIntent();
        runitprofilesource = (Runitprofile) intent.getSerializableExtra("profileobjtupdateprof");

        System.out.println("runit profile3 obj " + runitprofilesource.runitprofilescid);
        System.out.println("nickname " + runitprofilesource.nickname);


        Button updateprofilebut = (Button) findViewById(R.id.updateprofbutton);
        Button sendataprefbut = (Button) findViewById(R.id.updatedataprefbutt);

        nicknameinputprof = (EditText) findViewById(R.id.editTextnicknameupdate);
        fnameinputprof = (EditText) findViewById(R.id.editTextfnameedit);
        lnameinputprof = (EditText) findViewById(R.id.editTextlnameedit);

        //  EditText nationality = (EditText) findViewById(R.id.nationality);

        participant = (Switch) findViewById(R.id.switch10edit);
        fan = (Switch) findViewById(R.id.switch11edit);
        spectator = (Switch) findViewById(R.id.switch12edit);
        club = (Switch) findViewById(R.id.switch13edit);
        brand = (Switch) findViewById(R.id.switch14edit);
        sponsor = (Switch) findViewById(R.id.switch15edit);
        developer = (Switch) findViewById(R.id.switch16edit);



        sendataprefbut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                openActivitydatapreferencesupdate();

            }
        });

        updateprofilebut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                if (nicknameinputprof.getText().equals(null)) {
                    Toast.makeText(getApplicationContext(), "Nickname cannot be blank", Toast.LENGTH_LONG).show();
                    return;
                }

                if (fnameinputprof.getText().equals(null)) {
                    Toast.makeText(getApplicationContext(), "First name cannot be blank", Toast.LENGTH_LONG).show();
                    return;
                }

                if (lnameinputprof.getText().equals(null)) {
                    Toast.makeText(getApplicationContext(), "Last name cannot be blank", Toast.LENGTH_LONG).show();
                    return;
                }


                rolecode = "";


                // System.out.println("particpant " +  participant.isChecked());

                if (!participant.isChecked() && !fan.isChecked() && !spectator.isChecked() && !club.isChecked() && !brand.isChecked() && !sponsor.isChecked() && !developer.isChecked()) {
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

                System.out.println("new role code string" + rolecode);

                // set global to local

                nicknameglobal = nicknameinputprof.getText().toString();
                fnameglobal = fnameinputprof.getText().toString();
                lnameglobal = lnameinputprof.getText().toString();

                // bump to background thread and update the profile SC

                Toast.makeText(getApplicationContext(), "Thankyou for your patience.. updating your profile  now ..", Toast.LENGTH_LONG).show();

                spinupdate.setVisibility(View.VISIBLE);

                // need to lock the UI as we bump to bkgrnd

                // bump the below to new thread

                Activityupdateprofile.UpdateaccThread threadupdate = new Activityupdateprofile.UpdateaccThread();
                threadupdate.start();


                //  we  stop the spinner from the backgrnd thread spincreate.setVisibility(View.GONE);
            }


        });


        spinupdate.setVisibility(View.VISIBLE);

        // need to lock the UI as we bump to bkgrnd

        // bump the below to new thread

        Activityupdateprofile.GetLatestaccThread threadget = new Activityupdateprofile.GetLatestaccThread();
        threadget.start();



    }



    class GetLatestaccThread extends Thread {

        GetLatestaccThread() {
        }

        @Override
        public void run() {
            // we have to re-read the profile from ledger as consensus is consensus - we MUST
            // get latest profile form ledger.. cannot use old object that dashboard passes!


            try {
                runitprofilecurrent = HederaServices.getacontract(runitprofilesource.runitprofilescid);
                System.out.println("sc id from current " + runitprofilesource.runitprofilescid) ;

            } catch (TimeoutException e) {
                showToast( "Failed to get profile ! ");
                return;
            } catch (PrecheckStatusException e) {
                showToast( "Failed to get profile ! ");
                return;
            } catch (ReceiptStatusException e) {
                showToast( "Failed to get profile ! ");
                return;
            }

            fnameglobal = runitprofilecurrent.fname;
            lnameglobal = runitprofilecurrent.lname;
            nicknameglobal = runitprofilecurrent.nickname;

            // parse the rolecode and se the switches


            rolearray = (runitprofilecurrent.rolecode).split("/");



            // stop spinner
            runOnUiThread(new Runnable() {
                @Override
                public void run() {

                    spinupdate.setVisibility(View.GONE);

                    nicknameinputprof.setText(nicknameglobal);
                    fnameinputprof.setText(fnameglobal);
                    lnameinputprof.setText(lnameglobal);

                    // has to have at min 1 role


                    for (int i = 0; i < rolearray.length; ++i) {
                        if (rolearray[i].equals("P")) participant.setChecked(true);

                        if (rolearray[i].equals("F")) fan.setChecked(true);

                        if (rolearray[i].equals("S")) spectator.setChecked(true);

                        if (rolearray[i].equals("C")) club.setChecked(true);

                        if (rolearray[i].equals("R")) sponsor.setChecked(true);

                        if (rolearray[i].equals("B")) brand.setChecked(true);

                        if (rolearray[i].equals("D")) developer.setChecked(true);

                    }


                }
            });
        }

    }


    class UpdateaccThread extends Thread {

        UpdateaccThread() {
        }

        @Override
        public void run() {


            // call hedera update profile basics

            // create new profile object for updating the SC

            runitprofileupdated= new Runitprofile();

            runitprofileupdated.nickname = nicknameglobal;

            System.out.println("new role code string 1" + rolecode);

            runitprofileupdated.fname = fnameglobal;
            runitprofileupdated.lname = lnameglobal;
            runitprofileupdated.rolecode = rolecode;
            // not needed as it is a subset update of profile in the SC
            /*
            runitprofileupdated.runitrunaccountid = runitprofilecurrent.runitrunaccountid;

            runitprofileupdated.runitprofilescid = runitprofilecurrent.runitprofilescid;
            runitprofileupdated.runitlogonaccountid = runitprofilecurrent.runitlogonaccountid;
            runitprofileupdated.interest1 = runitprofilecurrent.interest1;
            runitprofileupdated.interest2 = runitprofilecurrent.interest2;
            runitprofileupdated.interest3 = runitprofilecurrent.interest3;
            runitprofileupdated.grpsponsorslevel = runitprofilecurrent.grpsponsorslevel;
            runitprofileupdated.sponsorslevel = runitprofilecurrent.sponsorslevel;
            runitprofileupdated.interests = runitprofilecurrent.interests;
            runitprofileupdated.demographic = runitprofilecurrent.demographic;
            runitprofileupdated.behavioral = runitprofilecurrent.behavioral;
            */

            // now update just the name and role selections

             String phonenum = "417 300 4812"; // to be added later.
             String nationality = "Australian"; // ditto

       //    public static void updateprofile(String usersprofilescID , String _fname, String _lname, String _nickname, String _phone, String _nationality, String _rolecode) throws TimeoutException, PrecheckStatusException, ReceiptStatusException {

            try {

                System.out.println(" fname" + fnameglobal);

                HederaServices.updateprofile(runitprofilecurrent.runitprofilescid,fnameglobal,lnameglobal,nicknameglobal,phonenum,nationality,rolecode);
                showToast("Your profile has been successfully updated");

            } catch (TimeoutException e) {
                showToast("Exception updating Profile SC" + e);
            } catch (PrecheckStatusException e) {
                showToast("Exception updating Profile SC" + e);
            } catch (ReceiptStatusException e) {
                showToast("Exception updating Profile SC" + e);

            }


            // stop spinner
            runOnUiThread(new Runnable() {
                @Override
                public void run() {

                    spinupdate.setVisibility(View.GONE);


                }
            });


        }




    }


    public void showToast(final String toast) {

        // stop spinner
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                spinupdate.setVisibility(View.GONE);
            }
        });


        runOnUiThread(() -> Toast.makeText(Activityupdateprofile.this, toast, Toast.LENGTH_LONG).show());


    }



    public void openActivitydatapreferencesupdate () {

        Intent intent = new Intent(this, com.example.runit.Activitydatapreferenceaccupdate.class);
        intent.putExtra("profileobjtodataprefupdate", runitprofilecurrent);
        //intent.putExtra("profile obj", decodedfile);
        startActivity(intent);
    }


}
