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

import com.hedera.hashgraph.sdk.BadMnemonicException;
import com.hedera.hashgraph.sdk.ContractId;
import com.hedera.hashgraph.sdk.PrecheckStatusException;
import com.hedera.hashgraph.sdk.ReceiptStatusException;

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

import static android.widget.Toast.makeText;

public class Activitydatapreferenceacc  extends AppCompatActivity {

    int min1 = 0, max1 = 100, min2 = 0, max2 = 100, current1 = 0, current2 = 0;

    Runitprofile runitprofiledatapref;

    Runitprofile runitprofiledataprefnew;

    ProgressBar spinnerdatapref;

    EditText like1, like2, like3;
    String like1stg, like2stg,like3stg;
    Boolean switchdemobool, switchbehavioralbool, switchintbool;
    BigInteger current1big, current2big;

    Switch switchdemo, switchbehavioral, switchint;
    SeekBar seekbar1, seekbar2;


    public Activitydatapreferenceacc() {
    }


        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_datapreferenceacc);

            Intent intent = getIntent();
            runitprofiledatapref = (Runitprofile) intent.getSerializableExtra("profileobjtodatapref");
            System.out.println("runit profile2 obj " + runitprofiledatapref.runitprofilescid);

            // first time creat - so set to defaults

            spinnerdatapref = (ProgressBar) findViewById(R.id.progressbardatapref);


            like1 = (EditText) findViewById(R.id.editTextlike1); // behavior & likes
            like2 = (EditText) findViewById(R.id.editTextlike2);  // interests
            like3 = (EditText) findViewById(R.id.editTextlike3);   // demographics

            switchdemo = (Switch) findViewById(R.id.switch1);
            switchbehavioral = (Switch) findViewById(R.id.switch2);
            switchint = (Switch) findViewById(R.id.switch3);

            seekbar1 = (SeekBar) findViewById(R.id.seekBar1);

            seekbar1.setMax((max1 - min1));

            seekbar2 = (SeekBar) findViewById(R.id.seekBar2);

            seekbar2.setMax((max2 - min2));

            Button dataprefcreate = (Button) findViewById(R.id.dataprefbutt);

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



            dataprefcreate.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    // now pull the data pref from the screen as this is a  create..

                    like1stg = like1.getText().toString();
                    like2stg = like2.getText().toString();
                    like3stg = like3.getText().toString();
                    switchdemobool = switchdemo.isChecked();
                    switchbehavioralbool = switchbehavioral.isChecked();
                    switchintbool = switchint.isChecked();
                    current1big = BigInteger.valueOf(current1);
                    current2big = BigInteger.valueOf(current2);


                    spinnerdatapref.setVisibility(View.VISIBLE);

                    // need to lock the UI as we bump to bkgrnd

                    // bump the below to new thread

                    Activitydatapreferenceacc.CreatedataprefThread threaddatapref = new Activitydatapreferenceacc.CreatedataprefThread();
                    threaddatapref.start();


                }

            });





        }


    class CreatedataprefThread extends Thread {

        CreatedataprefThread() {
        }

        @Override
        public void run() {

            // call HederaServices and update profile


            try {
                HederaServices.updatedataprefsettings(ContractId.fromString(runitprofiledatapref.runitprofilescid), like1stg,like2stg, like3stg, switchdemobool, switchbehavioralbool, switchintbool, current1big, current2big);
                showToast("Your Data preferences created Successfully");

            } catch (TimeoutException e) {
                showToast("Ledger Error updating data preferences " + e);
            } catch (PrecheckStatusException e) {
                showToast("Ledger Error updating data preferences " + e);
            } catch (ReceiptStatusException e) {
                showToast("Ledger Error updating data preferences " + e);
            }



        }


        public void showToast(final String toast) {

            // stop spinner
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    spinnerdatapref.setVisibility(View.GONE);
                }
            });


            runOnUiThread(() -> Toast.makeText(Activitydatapreferenceacc.this, toast, Toast.LENGTH_LONG).show());


        }

    }




    }


