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
import com.hedera.hashgraph.sdk.FileId;
import com.yakivmospan.scytale.Store;
import java.math.BigInteger;


public class Activitycreateacc extends AppCompatActivity {

    // Soul can be Athlete, Fan, Sponsor, Organizer, Content generator, Partner .. many roles at same or differing times
    // string public rolecodes;  // A/F/S/O/C/P


    BigInteger multiplier108 = new BigInteger("100000000");

    BigInteger multiplier1018 = new BigInteger("1000000000000000000");

    String rolecode;

    private GennedAccount newDetails;
    private AccountId newAccount;
    private FileId newhederaFileid;

    private ProgressBar spinner;


    public Activitycreateacc()  {
       }



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_createacc);

        Button createprofilebut = (Button) findViewById(R.id.createaccountbutt);

        Store store = new Store(getApplicationContext());

        spinner=(ProgressBar)findViewById(R.id.progressBar);
        spinner.setVisibility(View.GONE);

        Intent i = getIntent();
        Pinobj pinobject = (Pinobj) i.getSerializableExtra("newpin");

        //  EditText nicknamein = (EditText) findViewById(R.id.nickname);
      //  EditText fnamein = (EditText) findViewById(R.id.fname);
      //  EditText lnamein = (EditText) findViewById(R.id.lname);
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

        System.out.println("in create.");
        System.out.println("got pin from suck in ." + pinobject.newpin);



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

                rolecode = null;

                System.out.println(" swtich sel test " +  participant.hasSelection());
                System.out.println(" swtich fan sel test " +  fan.hasSelection());


                if (participant.hasSelection()){
                    Toast.makeText(getApplicationContext(), "Mok test", Toast.LENGTH_LONG).show();
                    return;
                }


                if (!participant.hasSelection() && !fan.hasSelection()){
                    Toast.makeText(getApplicationContext(), "both not selected", Toast.LENGTH_LONG).show();
                    return;
                }
                if (participant.hasSelection() && fan.hasSelection()){
                    Toast.makeText(getApplicationContext(), "both selected", Toast.LENGTH_LONG).show();
                    return;
                }
                /*

                if (!participant.hasSelection() && !fan.hasSelection() && !spectator.hasSelection() && !club.hasSelection() && !brand.hasSelection() && !sponsor.hasSelection() && !developer.hasSelection())
                {
                    Toast.makeText(getApplicationContext(), "Must have at least one current role", Toast.LENGTH_LONG).show();
                    return;
                }

                // build role string


                if (participant.hasSelection()) {

                    rolecode = rolecode + "P/";
                }

                if (fan.hasSelection()) {

                    rolecode = rolecode + "F/";
                }
                if (spectator.hasSelection()) {

                    rolecode = rolecode + "S/";
                }
                if (club.hasSelection()) {

                    rolecode = rolecode + "C/";
                }
                if (brand.hasSelection()) {

                    rolecode = rolecode + "B/";
                }
                if (sponsor.hasSelection()) {

                    rolecode = rolecode + "R/";
                }

                if (developer.hasSelection()) {

                    rolecode = rolecode + "D/";

                }

                if (newpassword.getText().equals(null) || (newpassword.getText().length() == 0)) {
                    Toast.makeText(getApplicationContext(), "Password cannot be empty", Toast.LENGTH_LONG).show();
                    return;
                }


                // completed role code appended string - now to start Spinner and create Hedera based Run.it new account and secure with password
                // THEN deploy profile Smart Contract with role definitions
                System.out.println("ssetting spinner");

                spinner.setVisibility(View.VISIBLE);
                System.out.println("spinner set");


                // call hedera new account and keypair

                HederaServices.createoperatorClient();

                System.out.println("creating new hedera account.");

                try {
                    newDetails = HederaServices.createnewkeypair();
                } catch (BadMnemonicException e) {
                    spinner.setVisibility(View.GONE);
                    Toast.makeText(getApplicationContext(), "Exception hitting Hedera", Toast.LENGTH_LONG).show();
                    return;
                }



                try {
                    newAccount = HederaServices.createnewaccount();
                } catch (TimeoutException e) {
                    Toast.makeText(getApplicationContext(), "Exception hitting Hedera " + e, Toast.LENGTH_LONG).show();
                    spinner.setVisibility(View.GONE);

                    return;
                } catch (PrecheckStatusException e) {
                    spinner.setVisibility(View.GONE);
                    Toast.makeText(getApplicationContext(), "Exception hitting Hedera " + e, Toast.LENGTH_LONG).show();
                    return;
                } catch (ReceiptStatusException e) {
                    spinner.setVisibility(View.GONE);
                    Toast.makeText(getApplicationContext(), "Exception hitting Hedera " + e, Toast.LENGTH_LONG).show();
                    return;
                }

                // HH account created - now to create a Run.it account as hedera


                if (newpassword.getText().equals(null) || (newpassword.getText().length() < 8)) {
                    spinner.setVisibility(View.GONE);
                    Toast.makeText(getApplicationContext(), "Password MUST not be empty and must be a minimum 8 alphanumeric characters - Please re-enter", Toast.LENGTH_LONG).show();
                    return;
                }




                // Note platform has to pay 3HBAR or so to create Souls new Account so new Soul can OWN their own SC ie profile data SC

                try {
                    TransactionRecord resultback = HederaServices.transferhbarfromrunit((long) 3, newAccount.toString(), "Welcome - xfer from Run.it Operating Account");
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

                // now deploy the Souls profile SC under their own Client instance.


                ContractId newcontractid = null;

                BigInteger initialrunbal = new BigInteger("0");

                try {
                    newcontractid = HederaServices.createdeployedprofile("Simon" , "Jackson",  "Piman" , "14178490705", "Australian", rolecode, newAccount.toSolidityAddress(), initialrunbal, newhederaFileid.toString(), "ipfs profile hash tbd");
                } catch (TimeoutException e) {
                    spinner.setVisibility(View.GONE);

                    Toast.makeText(getApplicationContext(), "Exception hitting Hedera - Profile Contract not created " + e, Toast.LENGTH_LONG).show();
                     return;
                } catch (PrecheckStatusException e) {
                    spinner.setVisibility(View.GONE);

                    Toast.makeText(getApplicationContext(), "Exception hitting Hedera - Profile Contract not created " + e, Toast.LENGTH_LONG).show();
                     return;
                } catch (ReceiptStatusException e) {
                    spinner.setVisibility(View.GONE);

                    Toast.makeText(getApplicationContext(), "Exception hitting Hedera - Profile Contract not created " + e, Toast.LENGTH_LONG).show();
                    return;
                }

                if (newcontractid == null || newcontractid.toString().isEmpty()) {
                    spinner.setVisibility(View.GONE);

                    Toast.makeText(getApplicationContext(), "Exception hitting Hedera - Profile Contract not created ", Toast.LENGTH_LONG).show();
                    return;
                }


                // now create users run.it account which is the hedera fileid which holds their enrypted key and profile smart contractid

                try {
                    newhederaFileid = HederaServices.createuserstore(newAccount, newpassword.getText().toString(), newcontractid);

                } catch (TimeoutException e) {
                    spinner.setVisibility(View.GONE);
                    Toast.makeText(getApplicationContext(), "Exception hitting Hedera -file create " + e, Toast.LENGTH_LONG).show();

                    return;
                } catch (PrecheckStatusException e) {
                    spinner.setVisibility(View.GONE);
                    Toast.makeText(getApplicationContext(), "Exception hitting Hedera -file create " + e, Toast.LENGTH_LONG).show();

                    return;
                } catch (ReceiptStatusException | NoSuchAlgorithmException | InvalidKeySpecException | UnsupportedEncodingException | NoSuchPaddingException | InvalidKeyException | BadPaddingException | IllegalBlockSizeException e) {
                    spinner.setVisibility(View.GONE);
                    Toast.makeText(getApplicationContext(), "Exception hitting Hedera - file create " + e, Toast.LENGTH_LONG).show();
                    return;
                }




                //  Credit 1000 RUN tokens to new Account

                BigInteger rungift = new BigInteger("1000000000000000000000");


                try {
                    HederaServices.runtokensfromplatform(rungift,newAccount.toSolidityAddress());
                } catch (ReceiptStatusException e) {
                    spinner.setVisibility(View.GONE);

                    Toast.makeText(getApplicationContext(), "Exception gifting RUN tokens " + e, Toast.LENGTH_LONG).show();
                    return;
                } catch (PrecheckStatusException e) {
                    spinner.setVisibility(View.GONE);

                    Toast.makeText(getApplicationContext(), "Exception gifting RUN tokens " + e, Toast.LENGTH_LONG).show();
                    return;
                } catch (TimeoutException e) {
                    spinner.setVisibility(View.GONE);

                    Toast.makeText(getApplicationContext(), "Exception gifting RUN tokens " + e, Toast.LENGTH_LONG).show();
                    return;
                }


                spinner.setVisibility(View.GONE);

                runitaccountnum.setText(newAccount.toString());
                runitaccountnum.setVisibility(View.VISIBLE);

                runitlogonidnum.setText(newhederaFileid.toString());
                runitlogonidnum.setVisibility(View.VISIBLE);


                SecretKey key = store.generateSymmetricKey(pinobject.newpin, null);

                 */

                Toast.makeText(getApplicationContext(), "Your Run.it AccountID(for RUN tokens & your HBAR, and the important LogonID has been created!, please keep your PIN " + pinobject.newpin + " VERY safe, & written down. We gifted you 1000 RUN Tokens to your AccountID because you KYC'd !" + newAccount+ " and " + newhederaFileid + " number written down and safe!", Toast.LENGTH_LONG).show();

            }



        });



    }



}
