package com.example.runit;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.hedera.hashgraph.sdk.AccountId;
import com.hedera.hashgraph.sdk.BadMnemonicException;
import com.hedera.hashgraph.sdk.ContractId;
import com.hedera.hashgraph.sdk.FileId;
import com.hedera.hashgraph.sdk.PrecheckStatusException;
import com.hedera.hashgraph.sdk.ReceiptStatusException;
import com.hedera.hashgraph.sdk.TransactionRecord;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.math.BigInteger;
import java.security.GeneralSecurityException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.util.concurrent.TimeoutException;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;

public class Activitycreateacc extends AppCompatActivity {

    // Soul can be Athlete, Fan, Sponsor, Organizer, Content generator, Partner .. many roles at same or differing times
    // string public rolecodes;  // A/F/S/O/C/P


    BigInteger multiplier108 = new BigInteger("100000000");

    BigInteger multiplier1018 = new BigInteger("1000000000000000000");

    String rolecode;

    private GennedAccount newDetails;
    private AccountId newAccount;
    private FileId newhederaFileid;

    public Activitycreateacc() throws IOException, GeneralSecurityException {
       }

    private ProgressBar spinner;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_createacc);

        Button createprofilebut = (Button) findViewById(R.id.newprofilebutton);

        spinner=(ProgressBar)findViewById(R.id.progressBar);
        spinner.setVisibility(View.GONE);

        //  EditText nicknamein = (EditText) findViewById(R.id.nickname);
      //  EditText fnamein = (EditText) findViewById(R.id.fname);
      //  EditText lnamein = (EditText) findViewById(R.id.lname);
      //  EditText nationality = (EditText) findViewById(R.id.nationality);

        CheckBox participant = (CheckBox) findViewById(R.id.chkboxparticipant);
        CheckBox fan = (CheckBox) findViewById(R.id.chkboxfan);
        CheckBox spectator = (CheckBox) findViewById(R.id.chkboxspectator);
        CheckBox club = (CheckBox) findViewById(R.id.chkboxclub);
        CheckBox brand = (CheckBox) findViewById(R.id.chkboxbrand);
        CheckBox sponsor = (CheckBox) findViewById(R.id.chkboxsponsor);
        CheckBox developer = (CheckBox) findViewById(R.id.chkboxdeveloper);

        EditText newpassword =(EditText) findViewById(R.id.editTextTextPassword);

        TextView runitaccountnum = (TextView) findViewById(R.id.textViewnewaccountnum);
        runitaccountnum.setVisibility(View.GONE);



        // validation

        /*

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

*/


        createprofilebut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (!participant.hasSelection() && !fan.hasSelection() && !spectator.hasSelection() && !club.hasSelection() && !brand.hasSelection() && !sponsor.hasSelection() && !developer.hasSelection())
                {
                    Toast.makeText(getApplicationContext(), "Must have a current role, 1 selection minimum", Toast.LENGTH_LONG).show();
                    return;
                }

                // build role string


                if (participant.hasSelection()) {
                    //append to getString()

                    rolecode = rolecode + "P/";
                    return;
                }

                if (fan.hasSelection()) {
                    //append to getString()

                    rolecode = rolecode + "F/";
                    return;
                }
                if (spectator.hasSelection()) {
                    //append to getString()

                    rolecode = rolecode + "S/";
                    return;
                }
                if (club.hasSelection()) {
                    //append to getString()

                    rolecode = rolecode + "C/";
                    return;
                }
                if (brand.hasSelection()) {
                    //append to getString()

                    rolecode = rolecode + "B/";
                    return;
                }
                if (sponsor.hasSelection()) {
                    //append to getString()

                    rolecode = rolecode + "R/";
                    return;
                }

                if (developer.hasSelection()) {
                    //append to getString()

                    rolecode = rolecode + "D/";
                    return;
                }


                // completed role code appended string - now to start Spinner and create Hedera based Run.it new account and secure with password
                // THEN deploy profile Smart Contract with role definitions

                spinner.setVisibility(View.VISIBLE);

                // call hedera new account and keypair gen

                HederaServices.createoperatorClient();

                try {
                    newDetails = HederaServices.createnewkeypair();
                } catch (BadMnemonicException e) {
                    Toast.makeText(getApplicationContext(), "Exception hitting Hedera", Toast.LENGTH_LONG).show();

                    return;
                }



                try {
                    newAccount = HederaServices.createnewaccount();
                } catch (TimeoutException e) {
                    e.printStackTrace();
                    Toast.makeText(getApplicationContext(), "Exception hitting Hedera " + e, Toast.LENGTH_LONG).show();
                    return;
                } catch (PrecheckStatusException e) {
                    Toast.makeText(getApplicationContext(), "Exception hitting Hedera " + e, Toast.LENGTH_LONG).show();

                    return;
                } catch (ReceiptStatusException e) {
                    Toast.makeText(getApplicationContext(), "Exception hitting Hedera " + e, Toast.LENGTH_LONG).show();

                    return;
                }

                // HH account created - now to create a Run.it account as hedera


                if (newpassword.getText().equals(null) || (newpassword.getText().length() < 8)) {
                    Toast.makeText(getApplicationContext(), "Password MUST not be empty and must be a minimum 8 alphanumeric characters - Please re-enter", Toast.LENGTH_LONG).show();
                    return;
                }


                try {
                    newhederaFileid = HederaServices.createuserstore(newAccount, newpassword.getText().toString());

                } catch (TimeoutException e) {
                    e.printStackTrace();
                    Toast.makeText(getApplicationContext(), "Exception hitting Hedera -file create " + e, Toast.LENGTH_LONG).show();

                    return;
                } catch (PrecheckStatusException e) {
                    Toast.makeText(getApplicationContext(), "Exception hitting Hedera -file create " + e, Toast.LENGTH_LONG).show();

                    return;
                } catch (ReceiptStatusException | NoSuchAlgorithmException | InvalidKeySpecException | UnsupportedEncodingException | NoSuchPaddingException | InvalidKeyException | BadPaddingException | IllegalBlockSizeException e) {
                    Toast.makeText(getApplicationContext(), "Exception hitting Hedera - file create " + e, Toast.LENGTH_LONG).show();

                    return;
                }



                // Note platform has to pay 3HBAR or so to create Souls new Account so new Soul can OWN their own SC ie profile data SC

                try {
                    TransactionRecord resultback = HederaServices.transferhbarfromrunit((long) 3, newAccount.toString(), "Welcome - xfer from Run.it Operating Account");
                } catch (ReceiptStatusException e) {
                    Toast.makeText(getApplicationContext(), "Exception hitting Hedera " + e, Toast.LENGTH_LONG).show();

                    return;
                } catch (PrecheckStatusException e) {
                    Toast.makeText(getApplicationContext(), "Exception hitting Hedera " + e, Toast.LENGTH_LONG).show();

                    return;
                } catch (TimeoutException e) {
                    Toast.makeText(getApplicationContext(), "Exception hitting Hedera " + e, Toast.LENGTH_LONG).show();

                    return;
                }

                // now deploy the Souls profile SC under their own Client instance.


                ContractId newcontractid = null;

                BigInteger initialrunbal = new BigInteger("0");

                try {
                    newcontractid = HederaServices.createdeployedprofile("Simon" , "Jackson",  "Piman" , "14178490705", "Australian", rolecode, newcontractid.toString(), initialrunbal, newhederaFileid.toString(), "ipfs profile hash tbd");
                } catch (TimeoutException e) {
                    Toast.makeText(getApplicationContext(), "Exception hitting Hedera - Profile Contract not created " + e, Toast.LENGTH_LONG).show();
                     return;
                } catch (PrecheckStatusException e) {
                    Toast.makeText(getApplicationContext(), "Exception hitting Hedera - Profile Contract not created " + e, Toast.LENGTH_LONG).show();
                     return;
                } catch (ReceiptStatusException e) {
                    Toast.makeText(getApplicationContext(), "Exception hitting Hedera - Profile Contract not created " + e, Toast.LENGTH_LONG).show();
                    return;
                }

                if (newcontractid == null || newcontractid.toString().isEmpty()) {
                    Toast.makeText(getApplicationContext(), "Exception hitting Hedera - Profile Contract not created ", Toast.LENGTH_LONG).show();
                    return;
                }


                spinner.setVisibility(View.GONE);

                runitaccountnum.setText(newhederaFileid.toString());
                runitaccountnum.setVisibility(View.VISIBLE);

                Toast.makeText(getApplicationContext(), "Your Run.it Account has been created!, please keep your new Account " + newhederaFileid + " number written down and safe!, it is encrypted with and secured by your password.", Toast.LENGTH_LONG).show();

            }

        });



    }



}
