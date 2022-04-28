package com.runnerup.runit;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.hedera.hashgraph.sdk.AccountId;
import com.hedera.hashgraph.sdk.FileId;
import com.hedera.hashgraph.sdk.PrecheckStatusException;
import com.hedera.hashgraph.sdk.PrivateKey;
import com.hedera.hashgraph.sdk.ReceiptStatusException;

import java.math.BigInteger;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.util.concurrent.TimeoutException;


public class Activityupdateprofile extends AppCompatActivity {

    // Soul can be ..  so rolecode permitted values P/F/S/C/B/R/D   R=sponsor - for duplicate and ease of install.


    BigInteger multiplier108 = new BigInteger("100000000");

    BigInteger multiplier1018 = new BigInteger("1000000000000000000");

    String rolecode, nicknameglobal, fnameglobal, lnameglobal;
    ProgressBar spinupdate;

    EditText nicknameinputprof, fnameinputprof, lnameinputprof;

    TextView pkey, accountid, urllink;
    View viewbuttview = findViewById(R.id.viewcontractbutt);
    View acceptbuttview = findViewById(R.id.acceptcontractbutt);


    Switch indidivual, team, organisation, approvedvendor, showkeyswitch;

    private GennedAccount newDetails;
    private AccountId newAccount;
    private FileId newhederaFileid;

    private String rolearray[] = null;

    private static String accountoutsplit[] = null;

    private static String urllinktext1 = "https://docs.google.com/document/d/1w6O1DpdntnMCHLblgCN1Ima6sj6ADreh/edit?usp=sharing&ouid=111079660368986421108&rtpof=true&sd=true";

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
        Button senddataprefbut = (Button) findViewById(R.id.updatedataprefbutt);
        Button viewdocument = (Button) findViewById(R.id.viewcontractbutt);
        Button acceptterms = (Button) findViewById(R.id.acceptcontractbutt);


        nicknameinputprof = (EditText) findViewById(R.id.editTextnicknameupdate);
        fnameinputprof = (EditText) findViewById(R.id.editTextfnameedit);
        lnameinputprof = (EditText) findViewById(R.id.editTextlnameedit);

        //  EditText nationality = (EditText) findViewById(R.id.nationality);

        indidivual = (Switch) findViewById(R.id.switch1edit);
        team = (Switch) findViewById(R.id.switch2edit);
        organisation = (Switch) findViewById(R.id.switch3edit);
        approvedvendor = (Switch) findViewById(R.id.switch4edit);

        showkeyswitch = (Switch) findViewById(R.id.switchshow);

        pkey = (TextView) findViewById(R.id.TexViewkey);
        String pkeyout = HederaServices.getkey().toString();
        accountid = (TextView) findViewById(R.id.textViewAccount);
        String accountout = HederaServices.getAccount().toString();
        urllink = (TextView) findViewById(R.id.texViewurl);


        showkeyswitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                // do something, the isChecked will be
                // true if the switch is in the On position

                accountoutsplit = accountout.split("-");

                if (isChecked) {
                    pkey.setText(pkeyout);
                    accountid.setText(accountoutsplit[0]);
                } else {
                    pkey.setText("");
                    accountid.setText("");
                }
            }
        });

        approvedvendor.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean selected) {

                if (selected) {
                    urllink.setVisibility(View.VISIBLE);
                    urllink.setText(urllinktext1);
                    viewbuttview.setVisibility(View.VISIBLE);
                } else {
                    urllink.setVisibility(View.INVISIBLE);
                    viewbuttview.setVisibility(View.INVISIBLE);

                    if (acceptbuttview.getVisibility() == View.VISIBLE) {
                        acceptbuttview.setVisibility(View.INVISIBLE);
                    }

                }
            }
        });


        viewdocument.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                acceptbuttview.setVisibility(View.VISIBLE);
                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(urllinktext1));
                startActivity(browserIntent);
            }
        });


        acceptterms.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                // make calls to  Ricardian SimpleTerms SC and set state for this Account signing in the mapping

                //  start a new thread to do this and set a toast saying you can now Create your QR codes for your products
                spinupdate.setVisibility(View.VISIBLE);

                // need to lock the UI as we bump to bkgrnd

                Toast.makeText(getApplicationContext(), "Thankyou for your patience.. Binding Ricardian legal contract to your profile..", Toast.LENGTH_LONG).show();

                // bump the below to new thread

                Activityupdateprofile.SetRicardianThread threadsetric = new Activityupdateprofile.SetRicardianThread();
                threadsetric.start();



            }
        });


        senddataprefbut.setOnClickListener(new View.OnClickListener() {
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

                if (!indidivual.isChecked() && !team.isChecked() && !organisation.isChecked()) {
                    Toast.makeText(getApplicationContext(), "You must have at least one role or many roles at any time", Toast.LENGTH_LONG).show();
                    return;
                }


                // build role string


                if (indidivual.isChecked()) {

                    rolecode = rolecode + "I/";
                }

                if (team.isChecked()) {

                    rolecode = rolecode + "T/";
                }

                if (organisation.isChecked()) {

                    rolecode = rolecode + "O/";
                }

                if (approvedvendor.isChecked()) {

                    rolecode = rolecode + "V/";
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


            }


        });


        spinupdate.setVisibility(View.VISIBLE);

        // need to lock the UI as we bump to bkgrnd

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
                        if (rolearray[i].equals("I")) indidivual.setChecked(true);

                        if (rolearray[i].equals("T")) team.setChecked(true);

                        if (rolearray[i].equals("O")) organisation.setChecked(true);

                    }


                }
            });
        }

    }



    class SetRicardianThread extends Thread {

        SetRicardianThread() {
        }

        @Override
        public void run() {
            // we have to update the SimpleTest Ricardian mapping SC within the profile SC

            // edit below for calls to the SC ... in hedera services.

            // old code boolean success;

            try {
                HederaServices.setricardian(runitprofilesource.runitprofilescid, urllinktext1);
            } catch (ReceiptStatusException e) {
                showToast( "Could not create Ricardian binding : " + e);
                return;
            } catch (PrecheckStatusException e) {
                showToast( "Could not create Ricardian binding : " + e);
                return;
            } catch (TimeoutException e) {
                showToast( "Could not create Ricardian binding : " + e);
                return;
            } catch (NoSuchAlgorithmException e) {
                showToast( "Could not create Ricardian binding : on hashing " + e);
                return;
            } catch (InvalidKeySpecException e) {
                showToast( "Could not create Ricardian binding : on hashing " + e);
                return;
            }

            /* old code  check if bytes32 hash is ok

            if (!success) {
                showToast( "Terms hash Link is > 32Bytes ");
                return;
            }
            System.out.println("sc id from current " + runitprofilesource.runitprofilescid) ;
            System.out.println("link 1 " + urllinktext1 ) ;
            */
            showToast( "You agreement is now inforce - you may use this Run Wallet to generate QR codes for your Website products for sale.");

            // stop spinner
            runOnUiThread(new Runnable() {
                @Override
                public void run() {

                    spinupdate.setVisibility(View.GONE);

                    //  then make invisible the url and buttons if call successful !

                    urllink.setText("");
                    viewbuttview.setVisibility(View.GONE);
                    acceptbuttview.setVisibility(View.GONE);
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

        Intent intent = new Intent(this, Activitydatapreferenceaccupdate.class);
        intent.putExtra("profileobjtodataprefupdate", runitprofilecurrent);
        //intent.putExtra("profile obj", decodedfile);
        startActivity(intent);
    }


}
