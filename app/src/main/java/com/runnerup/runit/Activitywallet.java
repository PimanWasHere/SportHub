package com.runnerup.runit;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.hedera.hashgraph.sdk.AccountId;
import com.hedera.hashgraph.sdk.PrecheckStatusException;
import com.hedera.hashgraph.sdk.ReceiptStatusException;

import java.math.BigInteger;
import java.util.concurrent.TimeoutException;

import static android.widget.Toast.makeText;

public class Activitywallet extends AppCompatActivity {

    public Activitywallet() {
    }

    Runitprofile runitprofile3;

    BigInteger multiplier108 = new BigInteger("100000000");

    BigInteger multiplier1018 = new BigInteger("1000000000000000000");

    ProgressBar spinwallet;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wallet);

        spinwallet = (ProgressBar) findViewById(R.id.progressBarwallet);

        Intent intent = getIntent();
        runitprofile3 = (Runitprofile) intent.getSerializableExtra("profileobjtowallet");

        System.out.println("runit profile3 obj " + runitprofile3.runitprofilescid);
        System.out.println("runit profile3 run it run accnt " + runitprofile3.runitrunaccountid);
        System.out.println("runit profile3 run it logon accnt " + runitprofile3.runitlogonaccountid);


        Button sendrunbutton = (Button) findViewById(R.id.sendrunbuttwallet);
        Button sendhbarbutton = (Button) findViewById(R.id.sendrunbuttwallethbar);

        EditText amountsend = (EditText) findViewById(R.id.editTextrunmounttosend);
        EditText destaccnt = (EditText) findViewById(R.id.editTextrunaccountto);
        destaccnt.setTextIsSelectable(true);


        sendrunbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick (View view){

                // check inputs - to do

                if (amountsend.getText().equals(null) || destaccnt.getText().equals(null)) {
                    // to do.. validation
                }

                spinwallet.setVisibility(View.VISIBLE);

                // need to lock the UI as we bump to bkgrnd

                // bump the below to new thread


                Activitywallet.SendRunThread thread = new Activitywallet.SendRunThread( amountsend.getText().toString(),destaccnt.getText().toString());
                thread.start();


            }

        });


        sendhbarbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick (View view){

                // check inputs - to do

                spinwallet.setVisibility(View.VISIBLE);

                // need to lock the UI as we bump to bkgrnd

                // bump the below to new thread


                Activitywallet.SendHbarThread thread2 = new Activitywallet.SendHbarThread(amountsend.getText().toString(),destaccnt.getText().toString());
                thread2.start();


            }

        });

    }



    class SendRunThread extends Thread {

        String amounttosend;
        String destaccount;

        SendRunThread(String _amountin, String _destaccount) {
            this.amounttosend = _amountin;
            this.destaccount = _destaccount;
        }

        @Override
        public void run() {

            BigInteger runtosendin = new BigInteger(amounttosend).multiply(multiplier1018);

            // call HederaServices to send RUN token.. dont send more than your balance - exception handling to add

            showToast("Sending RUN now .. ");

            AccountId destaccountid = AccountId.fromString(destaccount);

            System.out.println("account to " + destaccountid.toString());

            String destaccnt_sol = destaccountid.toSolidityAddress();

            System.out.println("account to in .sol " + destaccnt_sol);


            try {
                HederaServices.runtokensfromuser(runtosendin, destaccnt_sol);

                showToast("RUN token send successful - Press back button -  refresh your balance.. ");

            } catch (ReceiptStatusException e) {
                showToast("Ledger Error sending RUN tokens " + e);
            } catch (PrecheckStatusException e) {
                showToast("Ledger Error sending RUN tokens " + e);
            } catch (TimeoutException e) {
                showToast("Ledger Error sending RUN tokens " + e);

            }



        }



        public void showToast(final String toast)
        {

            // stop spinner
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    spinwallet.setVisibility(View.GONE);
                }
            });


            runOnUiThread(() -> Toast.makeText(Activitywallet.this, toast, Toast.LENGTH_LONG).show());


        }

    }




    class SendHbarThread extends Thread {

        String amounttosend;
        String destaccount;

        SendHbarThread(String _amountin, String _destaccount) {
            this.amounttosend = _amountin;
            this.destaccount = _destaccount;
        }

        @Override
        public void run() {

            BigInteger hbartosend = new BigInteger(amounttosend);

            // call HederaServices to send RUN token.. dont send more than your balance - exception handling to add

            showToast("Sending HBAR now .. ");


            System.out.println(hbartosend + " to account " + destaccount);

            try {
                HederaServices.sendhbar(hbartosend, destaccount);

                showToast("HBAR send successful - Press back button -  refresh your balance.. ");

            } catch (ReceiptStatusException e) {
                showToast("Ledger Error sending RUN tokens " + e);
            } catch (PrecheckStatusException e) {
                showToast("Ledger Error sending RUN tokens " + e);
            } catch (TimeoutException e) {
                showToast("Ledger Error sending RUN tokens " + e);
            } catch (IllegalArgumentException e) {
                showToast("Invalid recipient AccountID ! " + e);

            }
        }


        public void showToast(final String toast) {

            // stop spinner
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    spinwallet.setVisibility(View.GONE);
                }
            });


            runOnUiThread(() -> Toast.makeText(Activitywallet.this, toast, Toast.LENGTH_LONG).show());


        }
    }




}
