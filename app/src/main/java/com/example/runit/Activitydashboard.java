package com.example.runit;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.hedera.hashgraph.sdk.PrecheckStatusException;
import com.hedera.hashgraph.sdk.ReceiptStatusException;

import java.util.concurrent.TimeoutException;

public class Activitydashboard extends AppCompatActivity {

    public Activitydashboard() {

    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);

        Intent intent = getIntent();
        Runitprofile runitprofile = (Runitprofile) intent.getSerializableExtra("profileobj");

        System.out.println("profileobj fname.." + runitprofile.fname);

        TextView menuselection = (TextView) findViewById(R.id.textviewmenuselection);
        TextView name = (TextView) findViewById(R.id.textViewusername);

        ImageView dashboard = (ImageView) findViewById(R.id.imageViewdashbaord);
        ImageView manage = (ImageView) findViewById(R.id.imageViewassets);
        ImageView create = (ImageView) findViewById(R.id.imageViewcreate);
        ImageView profile = (ImageView) findViewById(R.id.imageViewprofile);

        ImageView menu = (ImageView) findViewById(R.id.imagemenubar);


        menuselection.setText("Dashboard - Events");

        // set name and role list
        // rolecode permitted values P/F/S/C/B/R/D   R=sponsor - for duplicate and ease of install.

        System.out.println("profileobj rolecode.." + runitprofile.rolecode);

        String roles = "I'm a ";
        String array[] = (runitprofile.rolecode).split("/");

        for(int i = 0; i < array.length; i++)
        {
            if (array[i].equals("P")) roles = roles + "Participant, ";
            if (array[i].equals("F")) roles = roles + "Fan, ";
            if (array[i].equals("S")) roles = roles + "Spectator, ";
            if (array[i].equals("C")) roles = roles + "Club rep, ";
            if (array[i].equals("B")) roles = roles + "Brand rep, ";
            if (array[i].equals("R")) roles = roles + "Sponsor, ";
            if (array[i].equals("D")) roles = roles + "Developer. ";

        }

        name.setText("Welcome " + runitprofile.nickname + "!  " + runitprofile.fname + " " + runitprofile.lname + " " + roles);

        try {
            menuselection.setText("Dashboard. " + HederaServices.getruntokenbal().toString() + " Run.it Token Balance.");
        } catch (ReceiptStatusException e) {
            Toast.makeText(getApplicationContext(), "Ledger Error getting your Run token Balance " +e, Toast.LENGTH_LONG).show();
        return;
        } catch (PrecheckStatusException e) {
            Toast.makeText(getApplicationContext(), "Ledger Error getting your Run token Balance" + e, Toast.LENGTH_LONG).show();
        return;
        } catch (TimeoutException e) {
            Toast.makeText(getApplicationContext(), "Ledger Error getting your Run token Balance" + e, Toast.LENGTH_LONG).show();
        return;
        }




        dashboard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // set new image
                menu.setImageResource(R.drawable.footer_1);

                dashboard.setImageResource(R.drawable.icon_red_dashbaord);

                // set all others to inverse ie to white

                manage.setImageResource(R.drawable.icon_white_manage);
                create.setImageResource(R.drawable.icon_white_create);
                profile.setImageResource(R.drawable.icon_white_profile);

                // evaluate role array and display buttons/ image assets accordingly

            }
        });


        manage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // set new image
                menu.setImageResource(R.drawable.footer_2);

                manage.setImageResource(R.drawable.icon_red_manage_1);

                // set all others to inverse ie to white

                dashboard.setImageResource(R.drawable.icon_white_dashboard);
                create.setImageResource(R.drawable.icon_white_create);
                profile.setImageResource(R.drawable.icon_white_profile);

                // evaluate role array and display buttons/ image assets accordingly


            }
        });


        create.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                // set new image
                menu.setImageResource(R.drawable.footer_3);

                create.setImageResource(R.drawable.icon_red_create);

                // set all others to inverse ie to white
                manage.setImageResource(R.drawable.icon_white_manage);
                dashboard.setImageResource(R.drawable.icon_white_dashboard);
                profile.setImageResource(R.drawable.icon_white_profile);

                // evaluate role array and display buttons/ image assets accordingly


            }
        });

        profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // set new image
                menu.setImageResource(R.drawable.footer_4);

                profile.setImageResource(R.drawable.icon_red_profile);

                // set all others to inverse ie to white
                manage.setImageResource(R.drawable.icon_white_manage);
                dashboard.setImageResource(R.drawable.icon_white_dashboard);
                create.setImageResource(R.drawable.icon_white_create);

                // evaluate role array and display buttons/ image assets accordingly

            }
        });

    }

}
