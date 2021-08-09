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
        TextView name = (TextView) findViewById(R.id.textViewusername);

        ImageView dashboard = (ImageView) findViewById(R.id.imageViewdashbaord);
        ImageView manage = (ImageView) findViewById(R.id.imageViewassets);
        ImageView create = (ImageView) findViewById(R.id.imageViewcreate);
        ImageView profile = (ImageView) findViewById(R.id.imageViewprofile);
        ImageView runitbalrefresh = (ImageView) findViewById(R.id.imageViewrfreshtoken);
        ImageView runitrandomrewards = (ImageView) findViewById(R.id.imageViewrandomrewards);


        ImageView menu = (ImageView) findViewById(R.id.imagemenubar);

            // action buttons.. switch on or off and label for each menu option selected.
        Button actionbutt1 = (Button) findViewById(R.id.action1butt);
        Button actionbutt2 = (Button) findViewById(R.id.action2butt);
        Button actionbutt3 = (Button) findViewById(R.id.action3butt);


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

        name.setText(" Welcome " + runitprofile.nickname + "!  " + roles);

        refreshbalance();

        menuselection.setText("DASHBOARD  " + runitbal+ " RUN Rewards, powered by your " + usrhbarbalst.trim() + " HBAR" );

        //  sett DASHBOARD create buttons - initially.

        // set dashboard as default on entry and default button for dashboard

        dashboardflag = 1;

        actionbutt1.setText("Wallet");
        actionbutt2.setText("My Events");

        if (!actionbutt1.isClickable()){
            actionbutt1.setClickable(true);}

        if (!actionbutt2.isClickable()){
            actionbutt2.setClickable(false);}

        runitrandomrewards.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                Toast.makeText(getApplicationContext(), " Processing more RUN rewards ! ", Toast.LENGTH_LONG).show();

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

                menuselection.setText("DASHBOARD  " + runitbal+ " RUN Rewards, powered by your " + usrhbarbalst + " HBAR" );

            }
        });


        runitbalrefresh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                refreshbalance();

                menuselection.setText("DASHBOARD  " + runitbal+ " RUN Rewards, powered by your " + usrhbarbalst + " HBAR" );


            }
        });



        dashboard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                menuselection.setText("DASHBOARD  " + runitbal+ " RUN Rewards, powered by your " + usrhbarbalst + " HBAR" );


                dashboardflag = 1;

                // set new image
                menu.setImageResource(R.drawable.footer_1);

                dashboard.setImageResource(R.drawable.icon_red_dashbaord);

                // set all others to inverse ie to white

                manage.setImageResource(R.drawable.icon_white_manage);
                create.setImageResource(R.drawable.icon_white_create);
                profile.setImageResource(R.drawable.icon_white_profile);

                if (actionbutt1.getVisibility() != View.VISIBLE){
                    actionbutt1.setVisibility(View.VISIBLE);
                    actionbutt1.setText("Wallet");
                }
                if (!actionbutt1.isClickable())
                actionbutt1.setClickable(true);


                if (actionbutt2.getVisibility() != View.VISIBLE) {
                    actionbutt2.setVisibility(View.VISIBLE);
                    actionbutt2.setText("Events");
                }
                if (actionbutt2.isClickable())
                actionbutt2.setClickable(false);

                if (actionbutt3.getVisibility() != View.VISIBLE) {
                    actionbutt3.setVisibility(View.VISIBLE);
                    actionbutt3.setText("Action 3");

                }
                if (actionbutt3.isClickable())
                actionbutt3.setClickable(false);


                // evaluate role array and display buttons/ image assets accordingly

            }
        });


        manage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                menuselection.setText("MANAGE NFTs " + runitbal+ " RUN Rewards, powered by your " + usrhbarbalst + " HBAR" );

                dashboardflag = 2;
                // set new image
                menu.setImageResource(R.drawable.footer_2);

                manage.setImageResource(R.drawable.icon_red_manage_1);

                // set all others to inverse ie to white

                dashboard.setImageResource(R.drawable.icon_white_dashboard);
                create.setImageResource(R.drawable.icon_white_create);
                profile.setImageResource(R.drawable.icon_white_profile);

                // evaluate role array and display buttons/ image assets accordingly

                if (actionbutt1.getVisibility() == View.VISIBLE){
                    actionbutt1.setVisibility(View.GONE);

                }
                if (actionbutt1.isClickable())
                actionbutt1.setClickable(false);


                if (actionbutt2.getVisibility() != View.VISIBLE) {
                    actionbutt2.setVisibility(View.VISIBLE);
                    actionbutt2.setText("Manage Assets");

                }
                if (!actionbutt2.isClickable())
                    actionbutt2.setClickable(true);

                if (actionbutt3.getVisibility() == View.VISIBLE) {
                    actionbutt3.setVisibility(View.GONE);
                }
                if (actionbutt3.isClickable())
                    actionbutt3.setClickable(false);




            }
        });


        create.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                dashboardflag = 3;
                menuselection.setText("CREATE   " + runitbal+ " RUN Rewards, powered by your " + usrhbarbalst + " HBAR" );


                // set new image
                menu.setImageResource(R.drawable.footer_3);

                create.setImageResource(R.drawable.icon_red_create);

                // set all others to inverse ie to white
                manage.setImageResource(R.drawable.icon_white_manage);
                dashboard.setImageResource(R.drawable.icon_white_dashboard);
                profile.setImageResource(R.drawable.icon_white_profile);

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

                if (actionbutt3.getVisibility() != View.VISIBLE) {
                    actionbutt3.setVisibility(View.VISIBLE);
                    actionbutt3.setText("Create NFT");

                }
                if (!actionbutt3.isClickable())
                    actionbutt3.setClickable(true);



                // evaluate role array and display buttons/ image assets accordingly


            }
        });

        profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                menuselection.setText("PROFILE  " + runitbal+ " RUN Rewards, powered by your " + usrhbarbalst + " HBAR" );

                dashboardflag = 4;

                // set new image
                menu.setImageResource(R.drawable.footer_4);

                profile.setImageResource(R.drawable.icon_red_profile);

                // set all others to inverse ie to white
                manage.setImageResource(R.drawable.icon_white_manage);
                dashboard.setImageResource(R.drawable.icon_white_dashboard);
                create.setImageResource(R.drawable.icon_white_create);

                //reset buttons

                if (actionbutt1.getVisibility() != View.VISIBLE){
                    actionbutt1.setVisibility(View.VISIBLE);
                    actionbutt1.setText("Data Preferences");
                    if (!actionbutt1.isClickable())
                    actionbutt1.setClickable(true);

                }
                else {
                    actionbutt1.setText("Data Preferences");
                    actionbutt1.setClickable(true);

                }

                if (actionbutt2.getVisibility() == View.VISIBLE) {
                    actionbutt2.setVisibility(View.GONE);
                    if (actionbutt2.isClickable())
                    actionbutt2.setClickable(false);
                }

                if (actionbutt3.getVisibility() == View.VISIBLE) {
                    actionbutt3.setVisibility(View.GONE);
                    if (actionbutt3.isClickable())
                    actionbutt3.setClickable(false);
                }

                // evaluate role array and display buttons/ image assets accordingly

            }
        });





        actionbutt1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                switch (dashboardflag) {
                    case 1:

                        // ok wallet clicked

                        Toast.makeText(getApplicationContext(), "Opening your wallet ..", Toast.LENGTH_LONG).show();
                        openActivitywallet();
                        break;
                    case 2:
                        break;
                    case 3:
                        break;
                    case 4:

                        Toast.makeText(getApplicationContext(), "Fetching your profile data preferences from the Ledger..", Toast.LENGTH_LONG).show();

                        openActivitydatapreferences();
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
