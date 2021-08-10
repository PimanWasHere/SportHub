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
import com.goodiebag.pinview.Pinview;
import com.google.protobuf.ByteString;
import com.hedera.hashgraph.sdk.AccountId;
import com.hedera.hashgraph.sdk.BadMnemonicException;
import com.hedera.hashgraph.sdk.ContractId;
import com.hedera.hashgraph.sdk.PrecheckStatusException;
import com.hedera.hashgraph.sdk.PrivateKey;
import com.hedera.hashgraph.sdk.ReceiptStatusException;
import com.yakivmospan.scytale.Store;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
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
import javax.crypto.SecretKey;

public class MainActivity extends AppCompatActivity {

    private Runitprofile runitprofile;
    private DecodeFileid decodedfile;

    String accountinput = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Store store = new Store(getApplicationContext());
        Button createviraccntbutt = (Button) findViewById(R.id.createvirginaccnt);
        Button gorunrunit = (Button) findViewById((R.id.gorunbutton));

        EditText mainactivitylogonpassword = (EditText) findViewById(R.id.editTextlogonTextPasswordmain);

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

        // make password entry visible and 'Go Run!' button.

        mainactivitylogonpassword.setVisibility(View.VISIBLE);
        gorunrunit.setVisibility(View.VISIBLE);

        Toast.makeText(getApplicationContext(), "Welcome, your password ?", Toast.LENGTH_LONG).show();




        gorunrunit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                HederaServices.createoperatorClient();
                // user pays for the password retrieval and balance check of their or ANY account
                // but platform has to pay for Hedera File read.

                ByteString encrypted;

                try {

                    encrypted = HederaServices.gethederafile(accountinput);

                } catch (TimeoutException | PrecheckStatusException hederaStatusException) {
                    Toast.makeText(getApplicationContext(), "Sorry this is not a valid Run.it logon ID in your phone stored Cache file! - corrupted", Toast.LENGTH_LONG).show();
                    return;
                }




                try {
                    decodedfile = new DecodeFileid(encrypted, mainactivitylogonpassword.getText().toString());

                    // check to see if account in the hedera file id matches input AND the password matches

                    if (!decodedfile.matchedok) {
                        Toast.makeText(getApplicationContext(), "Your Password on your Hedera File on the Ledger, does NOT match the Password you entered, Please re-enter the correct Password.", Toast.LENGTH_LONG).show();
                        return;
                    }

                } catch (UnsupportedEncodingException e) {
                    Toast.makeText(getApplicationContext(), "Incorrect Password, please re-enter", Toast.LENGTH_LONG).show();
                    mainactivitylogonpassword.getText().clear();
                    return;
                } catch (NoSuchAlgorithmException e) {
                    Toast.makeText(getApplicationContext(), "Incorrect Password, please re-enter", Toast.LENGTH_LONG).show();
                    mainactivitylogonpassword.getText().clear();
                    return;
                } catch (NoSuchPaddingException e) {
                    Toast.makeText(getApplicationContext(), "Incorrect Password, please re-enter", Toast.LENGTH_LONG).show();
                    mainactivitylogonpassword.getText().clear();
                    return;
                } catch (InvalidKeyException e) {
                    Toast.makeText(getApplicationContext(), "Incorrect Password, please re-enter", Toast.LENGTH_LONG).show();
                    mainactivitylogonpassword.getText().clear();
                    return;
                } catch (BadPaddingException e) {
                    Toast.makeText(getApplicationContext(), "Incorrect Password, please re-enter", Toast.LENGTH_LONG).show();
                    mainactivitylogonpassword.getText().clear();
                    return;
                } catch (IllegalBlockSizeException e) {
                    Toast.makeText(getApplicationContext(), "Incorrect Password, please re-enter", Toast.LENGTH_LONG).show();
                    mainactivitylogonpassword.getText().clear();
                    return;
                } catch (InvalidKeySpecException e) {
                    Toast.makeText(getApplicationContext(), "Incorrect Password, please re-enter", Toast.LENGTH_LONG).show();
                    mainactivitylogonpassword.getText().clear();
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
                    Toast.makeText(getApplicationContext(), "Failed to get profile ! " + e, Toast.LENGTH_LONG).show();
                    return;
                } catch (PrecheckStatusException e) {
                    Toast.makeText(getApplicationContext(), "Failed to get profile ! " + e, Toast.LENGTH_LONG).show();
                    return;
                } catch (ReceiptStatusException e) {
                    Toast.makeText(getApplicationContext(), "Failed to get profile ! " + e, Toast.LENGTH_LONG).show();
                    return;
                }

                //.. call method to open dashboard - pass profile object and decodedfile object

                // System.out.println("runit profile in logon fname" + runitprofile.fname);
                // System.out.println("runit profile in logon rolecode" + runitprofile.rolecode);

                runitprofile.runitprofilescid = decodedfile.usrprofilecontractid;

                System.out.println(".. profile runit account " + runitprofile.runitrunaccountid);
                System.out.println(".. profile runit fname" + runitprofile.fname);


                openActivitydashboard();


            }
        });

        createviraccntbutt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                openActivityinstallacc();
            }

        });




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