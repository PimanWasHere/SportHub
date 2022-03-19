package com.runnerup.runit;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.DrawableRes;
import androidx.appcompat.app.AppCompatActivity;

import com.hedera.hashgraph.sdk.AccountId;
import com.hedera.hashgraph.sdk.BadMnemonicException;
import com.hedera.hashgraph.sdk.ContractId;
import com.hedera.hashgraph.sdk.FileId;
import com.hedera.hashgraph.sdk.PrecheckStatusException;
import com.hedera.hashgraph.sdk.ReceiptStatusException;
import com.yakivmospan.scytale.Store;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.math.BigInteger;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.util.concurrent.TimeoutException;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;


public class Activitycreateacc extends AppCompatActivity {

    // Soul can be ..  so rolecode permitted values I/T/O   idivid, tewam, org


    BigInteger multiplier108 = new BigInteger("100000000");

    BigInteger multiplier1018 = new BigInteger("1000000000000000000");

    String passwordglobal, rolecodeglobal, nicknameglobal, fnameglobal, lnameglobal;

    private GennedAccount newDetails;
    private AccountId newAccount;
    private FileId newhederaFileid;
    private Runitprofile runitprofilecreated;
    String individualselected, teamselected, organizationselected;

    ProgressBar spincreate;
    Button individualbutt, teambutt, organisationbutt, createprofilebut, dataprefbutt;

    TextView runitlogonidnum;
    TextView runitaccountnum;
    TextView passwordquest;

    EditText newpassword;



    public Activitycreateacc() {
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_createacc);


        spincreate = (ProgressBar) findViewById(R.id.progressBarcreate);

        createprofilebut = (Button) findViewById(R.id.dataprofilebutton);

        dataprefbutt = (Button) findViewById(R.id.dataprefbuttonin);

        individualbutt = (Button) findViewById(R.id.individualbuttin);
        teambutt = (Button) findViewById(R.id.teambuttin);
        organisationbutt = (Button) findViewById(R.id.organisationbuttin);

        individualselected = " ";
        teamselected = " ";
        organizationselected = " ";
        //Store store = new Store(getApplicationContext());

        EditText nicknamein = (EditText) findViewById(R.id.editTextnicknamecreate);
        EditText fnamein = (EditText) findViewById(R.id.editTextfnamecreate);
        EditText lnamein = (EditText) findViewById(R.id.editTextlnamecreate);

        //  EditText nationality = (EditText) findViewById(R.id.nationality);


        newpassword = (EditText) findViewById(R.id.editTextTextPassword);

        runitaccountnum = (TextView) findViewById(R.id.textViewnewaccountlogon);
        runitlogonidnum = (TextView) findViewById(R.id.textViewnewlogonID);
        passwordquest = (TextView) findViewById(R.id.textView8);


        createprofilebut.setOnClickListener(new View.OnClickListener() {
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

                nicknameglobal = nicknamein.getText().toString();
                fnameglobal = fnamein.getText().toString();
                lnameglobal = lnamein.getText().toString();


                if (individualselected.equals(" ") && teamselected.equals(" ") && organizationselected.equals(" ")) {
                    Toast.makeText(getApplicationContext(), "You must click Individual, team or organisation, at least one or more", Toast.LENGTH_LONG).show();
                    return;

                }

                if (newpassword.getText().equals(null) || (newpassword.getText().length() == 0)) {
                    Toast.makeText(getApplicationContext(), "Password cannot be empty", Toast.LENGTH_LONG).show();
                    return;
                }

                rolecodeglobal = "";

                if (individualselected.equals("I")) rolecodeglobal = rolecodeglobal + "I/";

                if (teamselected.equals("T")) rolecodeglobal = rolecodeglobal + "T/";

                if (organizationselected.equals("O")) rolecodeglobal = rolecodeglobal + "O/";


                passwordglobal = newpassword.getText().toString();

                spincreate.setVisibility(View.VISIBLE);

                // need to lock the UI as we bump to bkgrnd

                // bump the below to new thread

                Activitycreateacc.CreateaccThread thread = new Activitycreateacc.CreateaccThread();
                thread.start();

                //  we  stop the spinner from the backgrnd thread spincreate.setVisibility(View.GONE);

            }


        });




        dataprefbutt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // open data preferences with new account object tbd.
                openActivitydatapreferences();

            }
        });

        individualbutt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (individualselected.equals("I")) {
                    individualbutt.setBackground(getResources().getDrawable(R.drawable.mywhitebutton));
                    individualbutt.setTextColor(getResources().getColor(R.color.colorPrimary));
                    individualselected = " ";
                    return;

                }
                individualbutt.setBackground(getResources().getDrawable(R.drawable.myrndredkbutton));
                individualbutt.setTextColor(getResources().getColor(R.color.colorPrimaryDark));
                individualselected = "I";
            }
        });


        teambutt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (teamselected.equals("T")) {
                    teambutt.setBackground(getResources().getDrawable(R.drawable.mywhitebutton));
                    teambutt.setTextColor(getResources().getColor(R.color.colorPrimary));
                    teamselected = " ";
                    return;

                }
                teambutt.setBackground(getResources().getDrawable(R.drawable.myrndredkbutton));
                teambutt.setTextColor(getResources().getColor(R.color.colorPrimaryDark));
                teamselected = "T";
            }
        });



        organisationbutt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (organizationselected.equals("O")) {
                    organisationbutt.setBackground(getResources().getDrawable(R.drawable.mywhitebutton));
                    organisationbutt.setTextColor(getResources().getColor(R.color.colorPrimary));
                    organizationselected = " ";
                    return;

                }
                organisationbutt.setBackground(getResources().getDrawable(R.drawable.myrndredkbutton));
                organisationbutt.setTextColor(getResources().getColor(R.color.colorPrimaryDark));
                organizationselected = "O";

            }
        });
    }


    class CreateaccThread extends Thread {

        CreateaccThread() {
        }

        @Override
        public void run() {


            // call hedera new account and keypair

            HederaServices.createoperatorClient();

            System.out.println("creating new hedera account.");

            try {
                newDetails = HederaServices.createnewkeypair();
            } catch (BadMnemonicException e) {
                showToast("Exception hitting Hedera " + e);
                return;
            }

            System.out.println("new genned accnt ." + newDetails.newPublicKey);


            try {
                newAccount = HederaServices.createnewaccount();
            } catch (TimeoutException e) {
                showToast("Exception hitting Hedera " + e);
                return;
            } catch (PrecheckStatusException e) {
                showToast("Exception hitting Hedera " + e);
                return;
            } catch (ReceiptStatusException e) {
                showToast("Exception hitting Hedera " + e);
                return;
            }

            // HH account created - now to create a Run.it account as hedera

                /*  not needed as we create the new account with 5 hbar
                try {
                    TransactionRecord resultback = HederaServices.transferhbarfromrunit((long) 1000000000, newAccount.toString(), "Welcome - xfer from Run.it Operating Account");
                } catch (ReceiptStatusException e) {
                    spinner.setVisibility(View.GONE);

                    Toast.makeText(getApplicationContext(), "Exception hitting Hedera " + e, Toast.LENGTH_LONG).show();

                    return;
                } catch (PrecheckStatusException e) {
                    spinner.setVisibility(View.GONE);

                    Toast.makeText(getApplicationContext(), "Exception hitting Hedera " + e, Toast.LENGTH_LONG).show();

                    return;
                } catch (TimeoutException e) {
                    spinner.setVisibility(View.GONE);

                    Toast.makeText(getApplicationContext(), "Exception hitting Hedera " + e, Toast.LENGTH_LONG).show();

                    return;
                }
                */

            // grab the Users Client of new account - new instance and use their keypair to create their own profile SC
            // ie the DATA owner will be the OWNER !

            HederaServices.createuserClient(newAccount, newDetails.newPrivKey);

            // now deploy the Souls profile SC under their own Client instance.


            ContractId newcontractid = null;

            BigInteger initialrunbal = new BigInteger("0");

            System.out.println(" new account as .sol " + newAccount.toSolidityAddress());
            System.out.println(" rolecodeglobal on create  " + rolecodeglobal);


            try {
                newcontractid = HederaServices.createdeployedprofile(fnameglobal, lnameglobal, nicknameglobal, "0", "Earthling", rolecodeglobal, newAccount.toSolidityAddress(), initialrunbal, "0.0.000000", "ipfs profile hash tbd");
            } catch (TimeoutException e) {
                showToast("Exception hitting Hedera " + e);
                System.out.println(" profile create exception " + e);
                return;
            } catch (PrecheckStatusException e) {
                System.out.println(" profile create exception " + e);
                showToast("Exception hitting Hedera " + e);
                return;
            } catch (ReceiptStatusException e) {
                System.out.println(" profile create exception " + e);
                showToast("Exception hitting Hedera " + e);
                return;
            }

            if (newcontractid == null || newcontractid.toString().isEmpty()) {
                System.out.println(" profile create exception ");
                showToast("Exception hitting Hedera - Profile Contract not created ");
                return;
            }

            System.out.println("users profile SC id " + newcontractid);

            // create profile object for passing to dashboard

            runitprofilecreated = new Runitprofile();

            runitprofilecreated.nickname = nicknameglobal;
            runitprofilecreated.fname = fnameglobal;
            runitprofilecreated.lname = lnameglobal;
            runitprofilecreated.rolecode = rolecodeglobal;
            runitprofilecreated.runitrunaccountid = newAccount.toString();

            runitprofilecreated.runitprofilescid = newcontractid.toString();


            // now create users run.it account which is the hedera fileid which holds their account, encrypted key , password hash, and profile smart contractid

            try {
                newhederaFileid = HederaServices.createuserstore(newAccount, newpassword.getText().toString(), newcontractid);

            } catch (TimeoutException e) {
                showToast("Exception hitting Hedera File Create " + e);
                return;
            } catch (PrecheckStatusException e) {
                showToast("Exception hitting Hedera File Create " + e);
                return;
            } catch (ReceiptStatusException | NoSuchAlgorithmException | InvalidKeySpecException | UnsupportedEncodingException | NoSuchPaddingException | InvalidKeyException | BadPaddingException | IllegalBlockSizeException e) {
                showToast("Exception hitting Hedera File Create " + e);
                return;
            }

            System.out.println("file id ie the run.it account is.." + newhederaFileid);

            runitprofilecreated.runitlogonaccountid = newhederaFileid.toString();

            //  Credit 1000 RUN tokens to new Account IF/Assuming TBD the User's KYC status is True

            BigInteger rungift = new BigInteger("100000000000000000000");


            try {
                HederaServices.runtokensfromplatform(rungift, newAccount.toSolidityAddress());
            } catch (ReceiptStatusException e) {
                showToast("Exception gifting RUN tokens " + e);
                System.out.println("exp 1.." +e);

                return;
            } catch (PrecheckStatusException e) {

                showToast("Exception gifting RUN tokens " + e);
                System.out.println("exp 2.." +e);

                return;
            } catch (TimeoutException e) {
                showToast("Exception gifting RUN tokens " + e);
                System.out.println("exp 3.." +e);

                return;
            }

            System.out.println("gifting tokens ..");

            // now we have to update the profile contract with the Users hedera fileid  (run.it account number), account, password hash, profile contract id

            // TBD
            try {
                Thread.sleep(2000);
                showToast(".. Quick couple of seconds for Hedera to reach consensus.. ");

            } catch (InterruptedException e) {
                showToast(".. thread sleep exception " + e);
                return;
            }


            try {
                System.out.println("updating profile w fileid..");

                HederaServices.updaterunitaccountid_fileid_inprofile(newcontractid, newhederaFileid.toString());
            } catch (TimeoutException e) {
                showToast("Exception updating profile with run.it fileid(accnt) " + e);
                return;
            } catch (PrecheckStatusException e) {
                showToast("Exception updating profile with run.it fileid(accnt) " + e);
                return;
            } catch (ReceiptStatusException e) {
                showToast("Exception updating profile with run.it fileid(accnt) " + e);
                return;
            }


            System.out.println("SC updated for fileid (runit account id) " + newhederaFileid.toString());


            // write Run.it Account ID and Hedera Account ID to local file (safe as they are public anyway

            // tbd - check for existance .. if so make visible a account ID prompt and re-save it - as if phone has limited storage, phone may remove this


            try {

                File.createTempFile("runitaccount", null, Activitycreateacc.this.getCacheDir());

            } catch (IOException e) {
                showToast("Difficulty creating your local secure file for RUN.it account id " + e);
                return;
            }


            //File file = new File(getCacheDir(), userEmalFileName);
            //FileOutputStream fileOutputStream = new FileOutputStream(file);

            try {
                File runitaccountfile = new File(getCacheDir(), "runitaccount");

                FileOutputStream runitfileout = new FileOutputStream(runitaccountfile);

                runitfileout.write(newhederaFileid.toString().getBytes());

            } catch (FileNotFoundException e) {
                showToast("RUN.it error when trying to find newly created account Cache file and then writing to your Cache file - internal " + e);
                return;
            } catch (IOException e) {
                showToast("RUN.it error when trying to write to Cache file - internal " + e);
                return;

            }
            System.out.println(".. saved to cache file as string " + newhederaFileid.toString());

            System.out.println(".. saved to cache file as bytes " + newhederaFileid.toString().getBytes());

            showToast("Your Run.it AccountID(for RUN tokens & your HBAR, and the important LogonID has been created and stored on your Phone in its safe cache,  We gifted you 1000 RUN Tokens to your AccountID because you KYC'd ! " + newAccount + " and " + newhederaFileid + " number written down and safe! ");


            // stop spinner
            runOnUiThread(new Runnable() {
                @Override
                public void run() {


                    spincreate.setVisibility(View.GONE);

                    runitaccountnum.setVisibility(View.VISIBLE);
                    runitaccountnum.setText("Run.it HBAR AccountID " + newAccount.toString());

                    runitlogonidnum.setVisibility(View.VISIBLE);
                    runitlogonidnum.setText("Run.it logon AccountID " + runitprofilecreated.runitlogonaccountid);

                    newpassword.setVisibility(View.GONE);
                    passwordquest.setVisibility(View.GONE);
                    createprofilebut.setVisibility(View.GONE);

                    dataprefbutt.setVisibility(View.VISIBLE);



                }
            });


        }


        public void showToast(final String toast) {

            // stop spinner
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    spincreate.setVisibility(View.GONE);
                }
            });


            runOnUiThread(() -> Toast.makeText(Activitycreateacc.this, toast, Toast.LENGTH_LONG).show());


        }

    }


    public void openActivitydatapreferences() {

        System.out.println("profile obj fname " + runitprofilecreated.fname + " " + runitprofilecreated.toString());

        Intent intent = new Intent(this, Activitydatapreferenceacc.class);
        intent.putExtra("profileobjtodatapref", runitprofilecreated);

        startActivity(intent);
    }

}



