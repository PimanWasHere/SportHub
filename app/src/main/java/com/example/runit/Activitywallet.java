package com.example.runit;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.hedera.hashgraph.sdk.AccountId;
import com.hedera.hashgraph.sdk.ContractId;
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wallet);

        Intent intent = getIntent();
        runitprofile3 = (Runitprofile) intent.getSerializableExtra("profileobjtowallet");

        System.out.println("runit profile3 obj " + runitprofile3.runitprofilescid);
        System.out.println("runit profile3 run it run accnt " + runitprofile3.runitrunaccountid);
        System.out.println("runit profile3 run it logon accnt " + runitprofile3.runitlogonaccountid);


        Button sendrunbutton = (Button) findViewById(R.id.sendrunbuttwallet);
        EditText runtokentosend = (EditText) findViewById(R.id.editTextrunmounttosend);
        EditText destaccnt = (EditText) findViewById(R.id.editTextrunaccountto);


        sendrunbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick (View view){

                // check inputs - to do

                BigInteger runtosendin = new BigInteger(runtokentosend.getText().toString()).multiply(multiplier1018);

                // call HederaServices to send RUN token.. dont send more than your balance - exception handling to add

                Toast.makeText(getApplicationContext(), "Sending your RUN ! - processing..", Toast.LENGTH_LONG).show();

                AccountId destaccountid = AccountId.fromString(destaccnt.getText().toString());

                System.out.println("account to " + destaccountid.toString());

                String destaccnt_sol = destaccountid.toSolidityAddress();

                System.out.println("account to in .sol " + destaccnt_sol);


                try {
                    HederaServices.runtokensfromuser(runtosendin, destaccnt_sol);
                } catch (ReceiptStatusException e) {
                    Toast.makeText(getApplicationContext(), "Ledger Error sending RUN tokens " +e, Toast.LENGTH_LONG).show();
                    return;
                } catch (PrecheckStatusException e) {
                    Toast.makeText(getApplicationContext(), "Ledger Error sending RUN tokens " +e, Toast.LENGTH_LONG).show();
                    return;
                } catch (TimeoutException e) {
                    Toast.makeText(getApplicationContext(), "Ledger Error sending RUN tokens " +e, Toast.LENGTH_LONG).show();
                    return;
                }

                Toast.makeText(getApplicationContext(), "RUN sent - Press back button -  refresh your balance.. ", Toast.LENGTH_LONG).show();

            }

        });

    }



}
