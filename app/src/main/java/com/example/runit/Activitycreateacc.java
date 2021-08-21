package com.example.runit;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;
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

    // Soul can be ..  so rolecode permitted values P/F/S/C/B/R/D   R=sponsor - for duplicate and ease of install.


    BigInteger multiplier108 = new BigInteger("100000000");

    BigInteger multiplier1018 = new BigInteger("1000000000000000000");

    String passwordglobal, rolecodeglobal, nicknameglobal, fnameglobal, lnameglobal;

    private GennedAccount newDetails;
    private AccountId newAccount;
    private FileId newhederaFileid;
    private Runitprofile runitprofilecreated;


    ProgressBar spincreate;

    Button createprofilebut;
    Button dataprefbutt;

    TextView runitlogonidnum;
    TextView runitaccountnum;

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


        Store store = new Store(getApplicationContext());


        EditText nicknamein = (EditText) findViewById(R.id.editTextnicknamecreate);
        EditText fnamein = (EditText) findViewById(R.id.editTextfnamecreate);
        EditText lnamein = (EditText) findViewById(R.id.editTextlnamecreate);

        //  EditText nationality = (EditText) findViewById(R.id.nationality);

        Switch participant = (Switch) findViewById(R.id.switch10);
        Switch fan = (Switch) findViewById(R.id.switch11);
        Switch spectator = (Switch) findViewById(R.id.switch12);
        Switch club = (Switch) findViewById(R.id.switch13);
        Switch brand = (Switch) findViewById(R.id.switch14);
        Switch sponsor = (Switch) findViewById(R.id.switch15);
        Switch developer = (Switch) findViewById(R.id.switch16);

        newpassword = (EditText) findViewById(R.id.editTextTextPassword);

        runitaccountnum = (TextView) findViewById(R.id.textViewnewaccountnum);
        runitlogonidnum = (TextView) findViewById(R.id.textViewnewlogonID);

        runitaccountnum.setVisibility(View.GONE);
        runitlogonidnum.setVisibility(View.GONE);


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


                rolecodeglobal = "";

                // System.out.println("particpant " +  participant.isChecked());

                if (!participant.isChecked() && !fan.isChecked() && !spectator.isChecked() && !club.isChecked() && !brand.isChecked() && !sponsor.isChecked() && !developer.isChecked()) {
                    Toast.makeText(getApplicationContext(), "You must have at least one role or many roles at any time", Toast.LENGTH_LONG).show();
                    return;
                }


                // build role string


                if (participant.isChecked()) {

                    rolecodeglobal = rolecodeglobal + "P/";
                }

                if (fan.isChecked()) {

                    rolecodeglobal = rolecodeglobal + "F/";
                }
                if (spectator.isChecked()) {

                    rolecodeglobal = rolecodeglobal + "S/";
                }
                if (club.isChecked()) {

                    rolecodeglobal = rolecodeglobal + "C/";
                }
                if (brand.isChecked()) {

                    rolecodeglobal = rolecodeglobal + "B/";
                }
                if (sponsor.isChecked()) {

                    rolecodeglobal = rolecodeglobal + "R/";
                }

                if (developer.isChecked()) {

                    rolecodeglobal = rolecodeglobal + "D/";

                }


                if (newpassword.getText().equals(null) || (newpassword.getText().length() == 0)) {
                    Toast.makeText(getApplicationContext(), "Password cannot be empty", Toast.LENGTH_LONG).show();
                    return;
                }


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

            BigInteger rungift = new BigInteger("1000000000000000000000");


            try {
                HederaServices.runtokensfromplatform(rungift, newAccount.toSolidityAddress());
            } catch (ReceiptStatusException e) {
                showToast("Exception gifting RUN tokens " + e);
                return;
            } catch (PrecheckStatusException e) {
                showToast("Exception gifting RUN tokens " + e);
                return;
            } catch (TimeoutException e) {
                showToast("Exception gifting RUN tokens " + e);
                return;
            }


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



                    runitaccountnum.setText("Run.it HBAR AccountID " + runitprofilecreated.runitrunaccountid);
                    runitaccountnum.setVisibility(View.VISIBLE);

                    runitlogonidnum.setText("Run.it logon AccountID " + runitprofilecreated.runitlogonaccountid);
                    runitlogonidnum.setVisibility(View.VISIBLE);

                    newpassword.setVisibility(View.GONE);
                    createprofilebut.setVisibility(View.GONE);

                    dataprefbutt.setVisibility(View.VISIBLE);

                    spincreate.setVisibility(View.GONE);

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

        Intent intent = new Intent(this, com.example.runit.Activitydatapreferenceacc.class);
        intent.putExtra("profileobjtodatapref", runitprofilecreated);

        startActivity(intent);
    }

}



