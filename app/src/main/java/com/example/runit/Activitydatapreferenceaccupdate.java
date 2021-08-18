package com.example.runit;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.SeekBar;
import android.widget.Switch;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.hedera.hashgraph.sdk.ContractId;
import com.hedera.hashgraph.sdk.PrecheckStatusException;
import com.hedera.hashgraph.sdk.ReceiptStatusException;

import java.math.BigInteger;
import java.util.concurrent.TimeoutException;

import static android.widget.Toast.makeText;

public class Activitydatapreferenceaccupdate extends AppCompatActivity {

    int min1 = 0, max1 = 100, min2 = 0, max2 = 100, current1 = 0, current2 = 0;

    public Activitydatapreferenceaccupdate() {
    }

        Runitprofile runitprofiledataprefupdate;

        Runitprofile runitprofiledataprefnewupdate;

        ProgressBar progressbardataprefupdate;


        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_datapreferenceaccupdate);

            Intent intent = getIntent();
            runitprofiledataprefupdate = (Runitprofile) intent.getSerializableExtra("profileobjtodataprefupdate");

            progressbardataprefupdate = (ProgressBar) findViewById(R.id.progressbardataprefupdate);
            System.out.println("runit profile2 obj " + runitprofiledataprefupdate.runitprofilescid);

            // now pull the data pref from the profile object IF this is a update..  if its a create.. will be just blank entries

            try {
                runitprofiledataprefnewupdate = HederaServices.getdataprefsettings(runitprofiledataprefupdate.runitprofilescid);
            } catch (TimeoutException e) {
                Toast.makeText(getApplicationContext(), "Ledger Error getting data preferences " +e, Toast.LENGTH_LONG).show();
                return;
            } catch (PrecheckStatusException e) {
                Toast.makeText(getApplicationContext(), "Ledger Error getting data preferences " +e, Toast.LENGTH_LONG).show();
                return;
            } catch (ReceiptStatusException e) {
                Toast.makeText(getApplicationContext(), "Ledger Error getting data preferences " +e, Toast.LENGTH_LONG).show();
                return;
            }

            System.out.println("interest 1 " + runitprofiledataprefnewupdate.interest1);

            EditText like1 = (EditText) findViewById(R.id.editTextlike1update); // behavior & likes
            EditText like2 = (EditText) findViewById(R.id.editTextlike2update);  // interests
            EditText like3 = (EditText) findViewById(R.id.editTextlike3update);   // demographics

            Switch switchdemo = (Switch) findViewById(R.id.switch1update);
            Switch switchbehavioral = (Switch) findViewById(R.id.switch2update);
            Switch switchint = (Switch) findViewById(R.id.switch3update);

            SeekBar seekbar1 = (SeekBar) findViewById(R.id.seekBar1update);

            seekbar1.setMax((max1 - min1));

            SeekBar seekbar2 = (SeekBar) findViewById(R.id.seekBar2update);

            seekbar2.setMax((max2 - min2));


            Button dataprefconfirmupdate = (Button) findViewById(R.id.dataprefbuttupdate);


            // show existing settings from ledger POJO

            like1.setText(runitprofiledataprefupdate.interest1);
            like2.setText(runitprofiledataprefupdate.interest2);
            like3.setText(runitprofiledataprefupdate.interest3);

            if (runitprofiledataprefnewupdate.demographic)
            switchdemo.setChecked(true);

            if (runitprofiledataprefnewupdate.behavioral)
                switchbehavioral.setChecked(true);

            if (runitprofiledataprefnewupdate.interests)
                switchint.setChecked(true);

            current1 = runitprofiledataprefnewupdate.sponsorslevel.intValue();
            current2 = runitprofiledataprefnewupdate.grpsponsorslevel.intValue();

            seekbar1.setProgress(current1 - min1);
            System.out.println("seek bar 1 " + current1);

            seekbar2.setProgress(current2 - min2);
            System.out.println("seek bar 2 " + current2);





            seekbar1.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
                @Override
                public void onProgressChanged(SeekBar seekbar1, int progress1, boolean fromUser) {
                    current1 = progress1 + min1;

                }

                @Override
                public void onStartTrackingTouch(SeekBar seekbar1) {

                }

                @Override
                public void onStopTrackingTouch(SeekBar seekbar1) {

                }
            });



            seekbar2.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
                @Override
                public void onProgressChanged(SeekBar seekbar2, int progress2, boolean fromUser) {
                    current2 = progress2 + min2;

                }

                @Override
                public void onStartTrackingTouch(SeekBar seekbar2) {

                }

                @Override
                public void onStopTrackingTouch(SeekBar seekbar2) {

                }
            });



            dataprefconfirmupdate.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    // call HederaServices and update profile


                    try {
                        HederaServices.updatedataprefsettings(ContractId.fromString(runitprofiledataprefupdate.runitprofilescid), like1.getText().toString(),like2.getText().toString(), like3.getText().toString(), switchdemo.isChecked(), switchbehavioral.isChecked(), switchint.isChecked(), BigInteger.valueOf(current1), BigInteger.valueOf(current2));
                    } catch (TimeoutException e) {
                        Toast.makeText(getApplicationContext(), "Ledger Error updating data preferences " +e, Toast.LENGTH_LONG).show();
                        return;
                    } catch (PrecheckStatusException e) {
                        Toast.makeText(getApplicationContext(), "Ledger Error updating data preferences " +e, Toast.LENGTH_LONG).show();
                        return;
                    } catch (ReceiptStatusException e) {
                        Toast.makeText(getApplicationContext(), "Ledger Error updating data preferences " +e, Toast.LENGTH_LONG).show();
                        return;

                    }

                    Toast.makeText(getApplicationContext(), "Press back button -  Data preferences have been updated", Toast.LENGTH_LONG).show();

                }

            });





        }




    }


