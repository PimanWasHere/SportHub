package com.example.runit;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.protobuf.ByteString;
import com.hedera.hashgraph.sdk.AccountId;
import com.hedera.hashgraph.sdk.PrecheckStatusException;
import com.hedera.hashgraph.sdk.PrivateKey;
import com.hedera.hashgraph.sdk.ReceiptStatusException;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.util.concurrent.TimeoutException;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;

public class MainActivity extends AppCompatActivity {

    private Runitprofile runitprofile;
    private DecodeFileid decodedfile;

    ProgressBar spinmain;


    String accountinput = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        spinmain = (ProgressBar) findViewById(R.id.progressBarmain);

        Button createviraccntbutt = (Button) findViewById(R.id.createvirginaccnt2button);
        EditText mainactivitylogonpassword = (EditText) findViewById(R.id.editTextlogonTextPasswordmain);
        mainactivitylogonpassword.requestFocus();

        Button gorunrunit = (Button) findViewById((R.id.gorunbutton));


        Context context = MainActivity.this;

        File cacheFile = new File(context.getCacheDir(), "runitaccount");



        try (FileInputStream fileInputStream = new FileInputStream(cacheFile)) {
            int content;

            // reads a byte at a time, if it reached end of the file, returns -1
            while ((content = fileInputStream.read()) != -1) {
                accountinput = accountinput + (char) content;
            }

        } catch (FileNotFoundException e) {
            // cache file not there - show create account button
            createviraccntbutt.setVisibility(View.VISIBLE);
            Toast.makeText(getApplicationContext(), "First time in ? .. Please Create or Restore your Account (RUN AccountID needed) ", Toast.LENGTH_LONG).show();


            return;
        } catch (IOException e) {


            Toast.makeText(getApplicationContext(), "RUN cache file there but corrupted - RUN.it error when reading your Cache file as bytes - internal Error" + e, Toast.LENGTH_LONG).show();
            return;
        }


        System.out.println(".. got from cache file " + accountinput);

        spinmain.setVisibility(View.GONE);
        mainactivitylogonpassword.setVisibility(View.VISIBLE);
        gorunrunit.setVisibility(View.VISIBLE);

        Toast.makeText(getApplicationContext(), "Welcome, your password ?", Toast.LENGTH_LONG).show();

        gorunrunit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                spinmain.setVisibility(View.VISIBLE);

                // need to lock the UI as we bump to bkgrnd

                // bump the below to new thread

                LogonThread thread = new LogonThread(mainactivitylogonpassword.getText().toString());
                thread.start();

               //  we need to stop the spinner from the backgrnd thread spinmain.setVisibility(View.GONE);


            }
        });

        createviraccntbutt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                openActivityinstallacc();
            }

        });




    }


    class LogonThread extends Thread {
        String passwordin;

        LogonThread(String _passwordin) {
           this.passwordin = _passwordin;
        }

        @Override
        public void run() {

            HederaServices.createoperatorClient();
            // user pays for the password retrieval and balance check of their or ANY account
            // but platform has to pay for Hedera File read.

            ByteString encrypted;

            try {

                encrypted = HederaServices.gethederafile(accountinput);

            } catch (TimeoutException | PrecheckStatusException hederaStatusException) {
                showToast("Sorry this is not a valid Run.it logon ID in your phone stored Cache file! - corrupted");
                return;
            }




            try {
                decodedfile = new DecodeFileid(encrypted, passwordin);

                if (decodedfile.usraccnt.equals("0.0.00000"))
                {
                    showToast("Hedera RUN logon FileID contents less than all 4 parameters");
                    return;
                }

                // check to see if account in the hedera file id matches input AND the password matches

                if (!decodedfile.matchedok) {
                    showToast("Your Password on your Hedera File on the Ledger, does NOT match the Password you entered, Please re-enter the correct Password.");
                    return;
                }

            } catch (UnsupportedEncodingException e) {
                showToast("Your Password on your Hedera File on the Ledger, does NOT match the Password you entered, Please re-enter the correct Password.");
                    return;
            } catch (NoSuchAlgorithmException e) {
                showToast("Your Password on your Hedera File on the Ledger, does NOT match the Password you entered, Please re-enter the correct Password.");
                    return;
            } catch (NoSuchPaddingException e) {
                showToast("Your Password on your Hedera File on the Ledger, does NOT match the Password you entered, Please re-enter the correct Password.");
                return;

            } catch (InvalidKeyException e) {
                showToast("Your Password on your Hedera File on the Ledger, does NOT match the Password you entered, Please re-enter the correct Password.");
                return;

            } catch (BadPaddingException e) {
                showToast("Your Password on your Hedera File on the Ledger, does NOT match the Password you entered, Please re-enter the correct Password.");
                return;

            } catch (IllegalBlockSizeException e) {
                showToast("Your Password on your Hedera File on the Ledger, does NOT match the Password you entered, Please re-enter the correct Password.");
                return;

            } catch (InvalidKeySpecException e) {
                showToast("Your Password on your Hedera File on the Ledger, does NOT match the Password you entered, Please re-enter the correct Password.");
                return;

            }



            // do Hedera processing - create the user Client now we have the account retrieved

            HederaServices.createuserClient(AccountId.fromString(decodedfile.usraccnt), PrivateKey.fromString(decodedfile.usrpkey));



            // Now create instance of Runitprofile and pass the profile and Client object to dashboard activity


            // only open IF profile is found and object returned
            System.out.println(".. decoded file  user profile contractid " + decodedfile.usrprofilecontractid);


            try {
                runitprofile = HederaServices.getacontract(decodedfile.usrprofilecontractid);
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

            //.. call method to open dashboard - pass profile object and decodedfile object

            // System.out.println("runit profile in logon fname" + runitprofile.fname);
            // System.out.println("runit profile in logon rolecode" + runitprofile.rolecode);

            runitprofile.runitprofilescid = decodedfile.usrprofilecontractid;

            System.out.println(".. profile runit account " + runitprofile.runitrunaccountid);
            System.out.println(".. profile runit fname" + runitprofile.fname);

            // stop spinner
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    spinmain.setVisibility(View.GONE);
                }
            });

            openActivitydashboard();


        }


        public void showToast(final String toast)
        {

            // stop spinner
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    spinmain.setVisibility(View.GONE);
                }
            });



            runOnUiThread(() -> Toast.makeText(MainActivity.this, toast, Toast.LENGTH_LONG).show());


        }

    }


    public void openActivityinstallacc () {

        Intent intent = new Intent(this, com.example.runit.Activityinstalleracc.class);
        startActivity(intent);
    }



    public void openActivitydashboard () {

        System.out.println("profile obj fname " + runitprofile.fname + " " + runitprofile.toString());

        Intent intent = new Intent(this, com.example.runit.Activitydashboard.class);
        intent.putExtra("profileobj", runitprofile);

        startActivity(intent);
    }

}