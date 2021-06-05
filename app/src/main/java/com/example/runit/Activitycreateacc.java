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


public class Activitycreateacc extends AppCompatActivity {

    // Soul can be ..  so rolecode permitted values P/F/S/C/B/R/D   R=sponsor - for duplicate and ease of install.


    BigInteger multiplier108 = new BigInteger("100000000");

    BigInteger multiplier1018 = new BigInteger("1000000000000000000");

    String rolecode;

    private GennedAccount newDetails;
    private AccountId newAccount;
    private FileId newhederaFileid;

    private ProgressBar spinner2 = (ProgressBar) findViewById(R.id.progressBar2);

    public Activitycreateacc()  {
       }



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_createacc);

        Button createprofilebut = (Button) findViewById(R.id.createaccountbutt);

        Store store = new Store(getApplicationContext());

        spinner2.setVisibility(View.GONE);

        Intent i = getIntent();
        Pinobj pinobject = (Pinobj) i.getSerializableExtra("newpin");

        System.out.println("got pin " + pinobject.newpin);

        EditText nicknamein = (EditText) findViewById(R.id.editTextnickname);
        EditText fnamein = (EditText) findViewById(R.id.editTextfname);
        EditText lnamein = (EditText) findViewById(R.id.editTextlname);

      //  EditText nationality = (EditText) findViewById(R.id.nationality);

        Switch participant = (Switch) findViewById(R.id.switch10);
        Switch fan = (Switch) findViewById(R.id.switch11);
        Switch spectator = (Switch) findViewById(R.id.switch12);
        Switch club = (Switch) findViewById(R.id.switch13);
        Switch brand = (Switch) findViewById(R.id.switch14);
        Switch sponsor = (Switch) findViewById(R.id.switch15);
        Switch developer = (Switch) findViewById(R.id.switch16);

        EditText newpassword =(EditText) findViewById(R.id.editTextTextPassword);

        TextView runitaccountnum = (TextView) findViewById(R.id.textViewnewaccountnum);
        TextView runitlogonidnum = (TextView) findViewById(R.id.textViewnewlogonID);

        runitaccountnum.setVisibility(View.GONE);
        runitlogonidnum.setVisibility((View.GONE));



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




        createprofilebut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {



                rolecode = "";

                Toast.makeText(getApplicationContext(), "Thankyou for your patience.. creating account now ..", Toast.LENGTH_LONG).show();


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

                if (newpassword.getText().equals(null) || (newpassword.getText().length() == 0)) {
                    Toast.makeText(getApplicationContext(), "Password cannot be empty", Toast.LENGTH_LONG).show();
                    return;
                }


                // completed role code appended string - now to start Spinner and create Hedera based Run.it new account and secure with password
                // THEN deploy profile Smart Contract with role definitions


                spinner2.setVisibility(View.VISIBLE);

                // call hedera new account and keypair

                HederaServices.createoperatorClient();

                System.out.println("creating new hedera account.");

                try {
                    newDetails = HederaServices.createnewkeypair();
                } catch (BadMnemonicException e) {
                    spinner2.setVisibility(View.GONE);
                    Toast.makeText(getApplicationContext(), "Exception hitting Hedera", Toast.LENGTH_LONG).show();
                    return;
                }



                try {
                    newAccount = HederaServices.createnewaccount();
                } catch (TimeoutException e) {
                    Toast.makeText(getApplicationContext(), "Exception hitting Hedera " + e, Toast.LENGTH_LONG).show();
                    spinner2.setVisibility(View.GONE);

                    return;
                } catch (PrecheckStatusException e) {
                    spinner2.setVisibility(View.GONE);
                    Toast.makeText(getApplicationContext(), "Exception hitting Hedera " + e, Toast.LENGTH_LONG).show();
                    return;
                } catch (ReceiptStatusException e) {
                    spinner2.setVisibility(View.GONE);
                    Toast.makeText(getApplicationContext(), "Exception hitting Hedera " + e, Toast.LENGTH_LONG).show();
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

                HederaServices.createuserClient(newAccount,newDetails.newPrivKey);

                // now deploy the Souls profile SC under their own Client instance.


                ContractId newcontractid = null;

                BigInteger initialrunbal = new BigInteger("0");

                try {
                    newcontractid = HederaServices.createdeployedprofile(fnamein.getText().toString() , lnamein.getText().toString(), nicknamein.getText().toString() , "0", "Earthling", rolecode, newAccount.toSolidityAddress(), initialrunbal, "0.0.000000", "ipfs profile hash tbd");
                } catch (TimeoutException e) {
                    spinner2.setVisibility(View.GONE);
System.out.println("ex1" + e);
                    Toast.makeText(getApplicationContext(), "Profile Contract not created " + e, Toast.LENGTH_LONG).show();
                     return;
                } catch (PrecheckStatusException e) {
                    spinner2.setVisibility(View.GONE);
                    System.out.println("ex2" + e);

                    Toast.makeText(getApplicationContext(), "Profile Contract not created " + e, Toast.LENGTH_LONG).show();
                     return;
                } catch (ReceiptStatusException e) {
                    spinner2.setVisibility(View.GONE);
                    System.out.println("ex3" + e);

                    Toast.makeText(getApplicationContext(), "Profile Contract not created " + e, Toast.LENGTH_LONG).show();
                    return;
                }

                if (newcontractid == null || newcontractid.toString().isEmpty()) {
                    spinner2.setVisibility(View.GONE);
                    System.out.println("ex4" );


                    Toast.makeText(getApplicationContext(), "Exception hitting Hedera - Profile Contract not created ", Toast.LENGTH_LONG).show();
                    return;
                }

                System.out.println("users profile SC id " + newcontractid);


                // now create users run.it account which is the hedera fileid which holds their account, encrypted key , password hash, and profile smart contractid

                try {
                    newhederaFileid = HederaServices.createuserstore(newAccount, newpassword.getText().toString(), newcontractid);

                } catch (TimeoutException e) {
                    spinner2.setVisibility(View.GONE);
                    Toast.makeText(getApplicationContext(), "Exception hitting Hedera -file create " + e, Toast.LENGTH_LONG).show();

                    return;
                } catch (PrecheckStatusException e) {
                    spinner2.setVisibility(View.GONE);
                    Toast.makeText(getApplicationContext(), "Exception hitting Hedera -file create " + e, Toast.LENGTH_LONG).show();

                    return;
                } catch (ReceiptStatusException | NoSuchAlgorithmException | InvalidKeySpecException | UnsupportedEncodingException | NoSuchPaddingException | InvalidKeyException | BadPaddingException | IllegalBlockSizeException e) {
                    spinner2.setVisibility(View.GONE);
                    Toast.makeText(getApplicationContext(), "Exception hitting Hedera - file create " + e, Toast.LENGTH_LONG).show();
                    return;
                }

                System.out.println("file id " + newhederaFileid);


                //  Credit 1000 RUN tokens to new Account IF/Assuming TBD the User's KYC status is True

                BigInteger rungift = new BigInteger("1000000000000000000000");


                try {
                    HederaServices.runtokensfromplatform(rungift,newAccount.toSolidityAddress());
                } catch (ReceiptStatusException e) {
                    spinner2.setVisibility(View.GONE);

                    Toast.makeText(getApplicationContext(), "Exception gifting RUN tokens " + e, Toast.LENGTH_LONG).show();
                    return;
                } catch (PrecheckStatusException e) {
                    spinner2.setVisibility(View.GONE);

                    Toast.makeText(getApplicationContext(), "Exception gifting RUN tokens " + e, Toast.LENGTH_LONG).show();
                    return;
                } catch (TimeoutException e) {
                    spinner2.setVisibility(View.GONE);

                    Toast.makeText(getApplicationContext(), "Exception gifting RUN tokens " + e, Toast.LENGTH_LONG).show();
                    return;
                }


                // now we have to update the profile contract with the Users hedera fileid  (run.it account number), account, password hash, profile contract id

                // TBD


                try {
                    HederaServices.updaterunitaccountid_inprofile(newcontractid, newhederaFileid.toString());
                } catch (TimeoutException e) {
                    spinner2.setVisibility(View.GONE);
                    Toast.makeText(getApplicationContext(), "Exception updating profile with run.it fileid(accnt) " + e, Toast.LENGTH_LONG).show();
                    return;
                } catch (PrecheckStatusException e) {
                    spinner2.setVisibility(View.GONE);
                    Toast.makeText(getApplicationContext(), "Exception updating profile with run.it fileid(accnt) " + e, Toast.LENGTH_LONG).show();
                     return;
                } catch (ReceiptStatusException e)  {
                    spinner2.setVisibility(View.GONE);
                    Toast.makeText(getApplicationContext(), "Exception updating profile with run.it fileid(accnt) " + e, Toast.LENGTH_LONG).show();
                    return;
                }



                spinner2.setVisibility(View.GONE);

                runitaccountnum.setText("Run.it HBAR AccountID " + newAccount.toString());
                runitaccountnum.setVisibility(View.VISIBLE);

                runitlogonidnum.setText("Run.it logon AccountID " + newhederaFileid.toString());
                runitlogonidnum.setVisibility(View.VISIBLE);

                newpassword.setVisibility(View.GONE);
                createprofilebut.setVisibility(View.GONE);

                SecretKey key = store.generateSymmetricKey(pinobject.newpin, null);



                Toast.makeText(getApplicationContext(), "Your Run.it AccountID(for RUN tokens & your HBAR, and the important LogonID has been created!, please keep your PIN " + pinobject.newpin + " VERY safe, & written down. We gifted you 1000 RUN Tokens to your AccountID because you KYC'd !" + newAccount+ " and " + newhederaFileid + " number written down and safe!",Toast.LENGTH_SHORT).show();

                Toast.makeText(getApplicationContext(), "Press your back button to continue .. ",Toast.LENGTH_LONG).show();


            }



        });



    }



}
