package com.example.runit;

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


        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_datapreferenceacc);


            EditText like1 = (EditText) findViewById(R.id.editTextlike1); // behavior & likes
            EditText like2 = (EditText) findViewById(R.id.editTextlike2);  // interests
            EditText like3 = (EditText) findViewById(R.id.editTextlike3);   // demographics

            Switch switchdemo = (Switch) findViewById(R.id.switch1);
            Switch switchactivity = (Switch) findViewById(R.id.switch2);
            Switch switchint = (Switch) findViewById(R.id.switch3);

            SeekBar seekbar1 = (SeekBar) findViewById(R.id.seekBar1);

            seekbar1.setMax((max1 - min1));

            SeekBar seekbar2 = (SeekBar) findViewById(R.id.seekBar2);

            seekbar2.setMax((max2 - min2));


            Button dataprefconfirm = (Button) findViewById(R.id.selectbutt);
            Button dataprefback = (Button) findViewById(R.id.dataprefbackbutt);


            dataprefconfirm.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    // call HederaServices and update profile


                }

            });


            dataprefback.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {


                }

            });


        }




    }


