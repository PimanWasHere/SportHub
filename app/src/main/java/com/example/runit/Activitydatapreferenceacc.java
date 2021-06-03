package com.example.runit;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.SeekBar;
import android.widget.Switch;

import androidx.appcompat.app.AppCompatActivity;

public class Activitydatapreferenceacc  extends AppCompatActivity {

    int min1 = 0, max1 = 100, min2 = 0, max2 = 100, current1 = 0, current2 = 0;

    public Activitydatapreferenceacc() {
    }

        Runitprofile runitprofiledatapref;

        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_datapreferenceacc);

            Intent intent = getIntent();
            runitprofiledatapref = (Runitprofile) intent.getSerializableExtra("profileobjdatapref");

            EditText like1 = (EditText) findViewById(R.id.editTextlike1); // behavior & likes
            EditText like2 = (EditText) findViewById(R.id.editTextlike2);  // interests
            EditText like3 = (EditText) findViewById(R.id.editTextlike3);   // demographics

            Switch switchdemo = (Switch) findViewById(R.id.switch1);
            Switch switchbehavioral = (Switch) findViewById(R.id.switch2behavioral);
            Switch switchint = (Switch) findViewById(R.id.switch3);

            SeekBar seekbar1 = (SeekBar) findViewById(R.id.seekBar1);

            seekbar1.setMax((max1 - min1));

            SeekBar seekbar2 = (SeekBar) findViewById(R.id.seekBar2);

            seekbar2.setMax((max2 - min2));


            Button dataprefconfirm = (Button) findViewById(R.id.createaccountbutt);


            // show existing settings from ledger POJO

            like1.setText(runitprofiledatapref.interest1);
            like2.setText(runitprofiledatapref.interest2);
            like3.setText(runitprofiledatapref.interest3);

            if (runitprofiledatapref.demographic)
            switchdemo.setChecked(true);

            if (runitprofiledatapref.behavioral)
                switchbehavioral.setChecked(true);

            if (runitprofiledatapref.interests)
                switchint.setChecked(true);

            current1 = runitprofiledatapref.sponsorslevel.intValue();
            current2 = runitprofiledatapref.grpsponsorslevel.intValue();

            seekbar1.setProgress(current1 - min1);
            System.out.println("seek bar 1 " + current1);

            seekbar2.setProgress(current2 - min2);
            System.out.println("seek bar 2 " + current2);


            dataprefconfirm.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    // call HederaServices and update profile


                }

            });




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






        }




    }


