package com.example.runit;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wallet);

        Intent intent = getIntent();
        runitprofile3 = (Runitprofile) intent.getSerializableExtra("profileobjtowallet");

        System.out.println("runit profile3 obj " + runitprofile3.runitprofilescid);

        Button sendrunbutton = (Button) findViewById(R.id.sendrunbuttwallet);
        EditText runtokentosend = (EditText) findViewById(R.id.editTextrunmounttosend);
        EditText destaccnt = (EditText) findViewById(R.id.editTextrunaccountto);


        sendrunbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick (View view){

                // check inputs - to do


                BigInteger runtosendin = new BigInteger(runtokentosend.getText().toString());

                // call HederaServices to send RUN token.. dont send more than your balance - exception handling to add

                Toast.makeText(getApplicationContext(), "Sending your RUN ! - processing..", Toast.LENGTH_LONG).show();

                try {
                    HederaServices.runtokensfromuser(runtosendin, destaccnt.getText().toString());
                } catch (ReceiptStatusException e) {
                    makeText(getApplicationContext(), "Ledger Error sending RUN tokens " +e, Toast.LENGTH_LONG).show();
                    return;
                } catch (PrecheckStatusException e) {
                    makeText(getApplicationContext(), "Ledger Error sending RUN tokens " +e, Toast.LENGTH_LONG).show();
                    return;
                } catch (TimeoutException e) {
                    makeText(getApplicationContext(), "Ledger Error sending RUN tokens " +e, Toast.LENGTH_LONG).show();
                    return;
                }

                Toast.makeText(getApplicationContext(), "RUN sent - Press back button -  refresh your balance.. ", Toast.LENGTH_LONG).show();

            }

        });

    }



}
