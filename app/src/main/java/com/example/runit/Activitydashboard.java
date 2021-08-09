package com.example.runit;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.hedera.hashgraph.sdk.AccountId;
import com.hedera.hashgraph.sdk.Hbar;
import com.hedera.hashgraph.sdk.PrecheckStatusException;
import com.hedera.hashgraph.sdk.ReceiptStatusException;

import java.math.BigInteger;
import java.util.Random;
import java.util.concurrent.TimeoutException;

public class Activitydashboard extends AppCompatActivity {

    public Activitydashboard() {

    }

    Runitprofile runitprofile;


    int dashboardflag = 0;

    String runitbal = null;
    String runitrunaccountid_assol = null;

    Hbar usrhbarbal = null;
    String usrhbarbalst = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);


        Intent intent = getIntent();
        runitprofile = (Runitprofile) intent.getSerializableExtra("profileobj");

        // new Account obj

        System.out.println("profileobj indashboard fname.." + runitprofile.fname);

        System.out.println("profileobj run it hbar accountid.. " + runitprofile.runitrunaccountid);

        System.out.println("profileobj runitaccountid logon.. " + runitprofile.runitlogonaccountid);


        AccountId usraccount = AccountId.fromString(runitprofile.runitrunaccountid);

        // keep as a .sol addr for ease of use

        runitrunaccountid_assol = usraccount.toSolidityAddress();


        TextView menuselection = (TextView) findViewById(R.id.textviewmenuselection);
        TextView mainmenu1 = (TextView) findViewById(R.id.textViewmainmenu1);
        TextView mainmenu2 = (TextView) findViewById(R.id.textViewmainmenu2);
        TextView mainmenu3 = (TextView) findViewById(R.id.textViewmainmenu3);


        TextView name = (TextView) findViewById(R.id.textViewusername);

        ImageView home = (ImageView) findViewById(R.id.imageViewhome);
        ImageView manage = (ImageView) findViewById(R.id.imageViewmanage);
        ImageView assets = (ImageView) findViewById(R.id.imageViewassets);
        ImageView create = (ImageView) findViewById(R.id.imageViewcreate);
        ImageView account = (ImageView) findViewById(R.id.imageViewaccount);

        ImageView runitbalrefresh = (ImageView) findViewById(R.id.imageViewrfreshtoken);
        ImageView runitrandomrewards = (ImageView) findViewById(R.id.imageViewrandomrewards);

            // action buttons.. switch on or off and label for each menu option selected.
        Button actionbutt1 = (Button) findViewById(R.id.action1butt);
        Button actionbutt2 = (Button) findViewById(R.id.action2butt);
        Button actionbutt3 = (Button) findViewById(R.id.action3butt);

        mainmenu1.setText("Home");
        mainmenu2.setText("News");


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

        name.setText(" Welcome " + runitprofile.nickname + "!  " + roles);

        refreshbalance();

        menuselection.setText(runitbal+ " RUN Rewards, powered by your " + usrhbarbalst.trim() + " HBAR" );

        //  sett DASHBOARD create buttons - initially.

        // set dashboard as default on entry and default button for dashboard

        dashboardflag = 1;

        actionbutt1.setText("NFT 1");
        actionbutt2.setText("NFT 2");
        actionbutt2.setText("NFT 3");


        if (!actionbutt1.isClickable()){
            actionbutt1.setClickable(true);}

        if (!actionbutt2.isClickable()){
            actionbutt2.setClickable(false);}

        runitrandomrewards.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                // 7/20/21  SJ request random RUN reward increase and balance refresh
                int min = 1, max = 5;
                Random rn = new Random();
                int randomNum = rn.nextInt((max - min) + 1) + min;

                //i.e. (1 = 50%)   (2 = 25%)   (3 = 12.5%)   (4 = 7.25) ... etc...

                BigInteger runrewardsonrefresh;

                BigInteger runitbalbigint = new BigInteger(runitbal);

                switch(randomNum)
                {
                    case 1:
                        runrewardsonrefresh = new BigInteger("1000000000000000000").multiply(runitbalbigint.divide(BigInteger.valueOf(2)));
                    case 2:
                        runrewardsonrefresh = new BigInteger("1000000000000000000").multiply(runitbalbigint.divide(BigInteger.valueOf(4)));
                    case 3:
                        runrewardsonrefresh = new BigInteger("1000000000000000000").multiply(runitbalbigint.divide(BigInteger.valueOf(8)));
                    case 4:
                        runrewardsonrefresh = new BigInteger("1000000000000000000").multiply(runitbalbigint.divide(BigInteger.valueOf(16)));
                    case 5:
                        runrewardsonrefresh = new BigInteger("1000000000000000000").multiply(runitbalbigint.divide(BigInteger.valueOf(32)));
                    default:
                        runrewardsonrefresh = new BigInteger("1000000000000000000").multiply(runitbalbigint.divide(BigInteger.valueOf(4)));

                }

                // xfer RUN tokens from treasury accnt to User.


                try {

                    HederaServices.runtokensfromplatform(runrewardsonrefresh,runitrunaccountid_assol);
                } catch (ReceiptStatusException e) {
                    Toast.makeText(getApplicationContext(), "Exception gifting RUN tokens " + e, Toast.LENGTH_LONG).show();
                    return;
                } catch (PrecheckStatusException e) {
                    Toast.makeText(getApplicationContext(), "Exception gifting RUN tokens " + e, Toast.LENGTH_LONG).show();
                    return;
                } catch (TimeoutException e) {
                    Toast.makeText(getApplicationContext(), "Exception gifting RUN tokens " + e, Toast.LENGTH_LONG).show();
                    return;
                }

                // pause ui app thread to gain consensus - this will be on new thread in next version of PoC - with a Spinner.

                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    Toast.makeText(getApplicationContext(), "Thread sleep exception " + e, Toast.LENGTH_LONG).show();
                    e.printStackTrace();
                }


                refreshbalance();

                menuselection.setText( runitbal+ " RUN Rewards, powered by your " + usrhbarbalst.trim() + " HBAR" );

            }
        });


        runitbalrefresh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                refreshbalance();

                menuselection.setText(runitbal+ " RUN Rewards, powered by your " + usrhbarbalst.trim() + " HBAR" );


            }
        });



        home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                menuselection.setText(runitbal+ " RUN Rewards, powered by your " + usrhbarbalst + " HBAR" );

                mainmenu1.setText("Home");
                mainmenu2.setText("News");
                mainmenu3.setText("");

                dashboardflag = 1;

                home.setImageResource(R.drawable.logo_1_red_duplicatepng);

                // set all others to inverse ie to black

                manage.setImageResource(R.drawable.logo_2_black_duplicatepng);
                create.setImageResource(R.drawable.logo_3_black_duplicatepng);
                assets.setImageResource(R.drawable.logo_4_black_duplicatepng);
                account.setImageResource(R.drawable.logo_5_black_duplicatepng);

                if (actionbutt1.getVisibility() != View.VISIBLE){
                    actionbutt1.setVisibility(View.VISIBLE);
                    actionbutt1.setText("NFT1");
                }
                if (!actionbutt1.isClickable())
                actionbutt1.setClickable(true);


                if (actionbutt2.getVisibility() != View.VISIBLE) {
                    actionbutt2.setVisibility(View.VISIBLE);
                    actionbutt2.setText("NFT2");
                }

                if (actionbutt2.isClickable())
                actionbutt2.setClickable(false);

                if (actionbutt3.getVisibility() != View.VISIBLE) {
                    actionbutt3.setVisibility(View.VISIBLE);
                    actionbutt3.setText("NFT3");

                }
                if (actionbutt3.isClickable())
                actionbutt3.setClickable(false);


                // evaluate role array and display buttons/ image assets accordingly

            }
        });


        manage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                menuselection.setText( runitbal+ " RUN Rewards, powered by your " + usrhbarbalst + " HBAR" );

                mainmenu1.setText("My Events");
                mainmenu2.setText("Current Event");
                mainmenu3.setText("");

                dashboardflag = 2;

                manage.setImageResource(R.drawable.logo_2_red_duplicatepng);

                // set all others to inverse ie to black

                home.setImageResource(R.drawable.logo_1_black_duplicatepng);
                create.setImageResource(R.drawable.logo_3_black_duplicatepng);
                assets.setImageResource(R.drawable.logo_4_black_duplicatepng);
                account.setImageResource(R.drawable.logo_5_black_duplicatepng);


                // evaluate role array and display buttons/ image assets accordingly

                if (actionbutt1.getVisibility() != View.VISIBLE){
                    actionbutt1.setVisibility(View.VISIBLE);
                    actionbutt1.setText("Event 1");


                }

                if (!actionbutt1.isClickable())
                actionbutt1.setClickable(true);


                if (actionbutt2.getVisibility() != View.VISIBLE) {
                    actionbutt2.setVisibility(View.VISIBLE);
                    actionbutt2.setText("Event 2");

                }

                if (actionbutt2.isClickable())
                    actionbutt2.setClickable(false);

                if (actionbutt3.getVisibility() != View.VISIBLE) {
                    actionbutt3.setVisibility(View.VISIBLE);
                    actionbutt3.setText("Event 3");

                }

                if (actionbutt3.isClickable())
                    actionbutt3.setClickable(false);




            }
        });


                        // create to be decided.

        create.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                dashboardflag = 3;
                menuselection.setText( runitbal+ " RUN Rewards, powered by your " + usrhbarbalst + " HBAR" );
                mainmenu1.setText("Events");
                mainmenu2.setText("Team");
                mainmenu3.setText("Organization");

                create.setImageResource(R.drawable.logo_3_red_duplicatepng);

                // set all others to inverse ie to black
                home.setImageResource(R.drawable.logo_1_black_duplicatepng);
                manage.setImageResource(R.drawable.logo_2_black_duplicatepng);
                assets.setImageResource(R.drawable.logo_4_black_duplicatepng);
                account.setImageResource(R.drawable.logo_5_black_duplicatepng);

                //reset buttons

                if (actionbutt1.getVisibility() == View.VISIBLE){
                    actionbutt1.setVisibility(View.GONE);

                }
                if (actionbutt1.isClickable())
                    actionbutt1.setClickable(false);

                if (actionbutt2.getVisibility() == View.VISIBLE) {
                    actionbutt2.setVisibility(View.GONE);

                }
                if (actionbutt2.isClickable())
                    actionbutt2.setClickable(false);

                if (actionbutt3.getVisibility() == View.VISIBLE) {
                    actionbutt3.setVisibility(View.GONE);
                }
                if (!actionbutt3.isClickable())
                    actionbutt3.setClickable(false);



                // evaluate role array and display buttons/ image assets accordingly


            }
        });


        assets.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                menuselection.setText( runitbal+ " RUN Rewards, powered by your " + usrhbarbalst + " HBAR" );
                mainmenu1.setText("My Apps");
                mainmenu2.setText("Marketplace");
                mainmenu3.setText("");

                dashboardflag = 4;

                assets.setImageResource(R.drawable.logo_4_red_duplicatepng);

                // set all others to inverse ie to black
                home.setImageResource(R.drawable.logo_1_black_duplicatepng);
                manage.setImageResource(R.drawable.logo_2_black_duplicatepng);
                create.setImageResource(R.drawable.logo_3_black_duplicatepng);
                account.setImageResource(R.drawable.logo_5_black_duplicatepng);

                // .. assets.. TBD.. simon ?

                //reset buttons

                if (actionbutt1.getVisibility() == View.VISIBLE){
                    actionbutt1.setVisibility(View.GONE);

                }
                if (actionbutt1.isClickable())
                    actionbutt1.setClickable(false);

                if (actionbutt2.getVisibility() == View.VISIBLE) {
                    actionbutt2.setVisibility(View.GONE);

                }
                if (actionbutt2.isClickable())
                    actionbutt2.setClickable(false);

                if (actionbutt3.getVisibility() == View.VISIBLE) {
                    actionbutt3.setVisibility(View.GONE);
                }
                if (!actionbutt3.isClickable())
                    actionbutt3.setClickable(false);

            }
        });


        account.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                menuselection.setText( runitbal+ " RUN Rewards, powered by your " + usrhbarbalst + " HBAR" );

                mainmenu1.setText("Profile");
                mainmenu2.setText("Inventory");
                mainmenu3.setText("Settings");

                dashboardflag = 5;

                account.setImageResource(R.drawable.logo_5_red_duplicatepng);

                // set all others to inverse ie to black
                home.setImageResource(R.drawable.logo_1_black_duplicatepng);
                manage.setImageResource(R.drawable.logo_2_black_duplicatepng);
                create.setImageResource(R.drawable.logo_3_black_duplicatepng);
                assets.setImageResource(R.drawable.logo_4_black_duplicatepng);



                //turn off buttons for account page

                if (actionbutt1.getVisibility() == View.VISIBLE){
                    actionbutt1.setVisibility(View.GONE);

                }

                if (actionbutt2.getVisibility() == View.VISIBLE) {
                    actionbutt2.setVisibility(View.GONE);

                }

                if (actionbutt3.getVisibility() == View.VISIBLE) {
                    actionbutt3.setVisibility(View.GONE);

                }



            }
        });




// - below TBD..

        actionbutt1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                switch (dashboardflag) {
                    case 1:

                        // ok wallet clicked

                      //  Toast.makeText(getApplicationContext(), "Opening your wallet ..", Toast.LENGTH_LONG).show();
                       // openActivitywallet();
                        break;
                    case 2:
                        break;
                    case 3:
                        break;
                    case 4:

                      //  Toast.makeText(getApplicationContext(), "Fetching your profile data preferences from the Ledger..", Toast.LENGTH_LONG).show();

                       // openActivitydatapreferences();
                        break;

                }


            }
        });


        actionbutt2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                switch (dashboardflag) {
                    case 1:
                        Toast.makeText(getApplicationContext(), "Event Button not enabled yet - TBD", Toast.LENGTH_LONG).show();

                        break;
                    case 2:
                        break;
                    case 3:
                        break;
                    case 4:


                }



            }
        });



        actionbutt3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                switch (dashboardflag) {
                    case 1:
                        break;
                    case 2:
                        break;
                    case 3:
                        Toast.makeText(getApplicationContext(), "Creation of new NFT Asset - TBD", Toast.LENGTH_LONG).show();
                        break;
                    case 4:


                }


            }
        });





    }

    public void refreshbalance () {

        try {
            runitbal = HederaServices.getruntokenbal().toString();

        } catch (ReceiptStatusException e) {
            Toast.makeText(getApplicationContext(), "Ledger Error getting your Run token Balance " + e, Toast.LENGTH_LONG).show();
            return;
        } catch (PrecheckStatusException e) {
            Toast.makeText(getApplicationContext(), "Ledger Error getting your Run token Balance" + e, Toast.LENGTH_LONG).show();
            return;
        } catch (TimeoutException e) {
            Toast.makeText(getApplicationContext(), "Ledger Error getting your Run token Balance" + e, Toast.LENGTH_LONG).show();
            return;
        }

        // get hbar bal of user account

        try {
            usrhbarbal = HederaServices.getbalance(runitprofile.runitrunaccountid);
            usrhbarbalst = usrhbarbal.toString();

        } catch (TimeoutException e) {
            Toast.makeText(getApplicationContext(), "Ledger Error getting your HBAR Balance " + e, Toast.LENGTH_LONG).show();

        } catch (PrecheckStatusException e) {
            Toast.makeText(getApplicationContext(), "Ledger Error getting your HBAR Balance " + e, Toast.LENGTH_LONG).show();

        }

    }


    public void openActivitydatapreferences () {

       // System.out.println("profile obj fname " + runitprofile.fname + " " + runitprofile.toString());

        Intent intent = new Intent(this, com.example.runit.Activitydatapreferenceacc.class);
        intent.putExtra("profileobjtodatapref", runitprofile);
        //intent.putExtra("profile obj", decodedfile);
        startActivity(intent);
    }


    public void openActivitywallet () {

        // System.out.println("profile obj fname " + runitprofile.fname + " " + runitprofile.toString());

        Intent intent = new Intent(this, com.example.runit.Activitywallet.class);
        intent.putExtra("profileobjtowallet", runitprofile);
        //intent.putExtra("profile obj", decodedfile);
        startActivity(intent);
    }

}
