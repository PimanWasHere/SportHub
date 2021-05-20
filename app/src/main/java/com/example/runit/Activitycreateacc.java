package com.example.runit;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.hedera.hashgraph.sdk.AccountId;
import com.hedera.hashgraph.sdk.Client;
import com.hedera.hashgraph.sdk.FileId;
import com.hedera.hashgraph.sdk.PrivateKey;

import java.io.IOException;
import java.math.BigInteger;
import java.security.GeneralSecurityException;

public class Activitycreateacc extends AppCompatActivity {

    // Soul can be Athlete, Fan, Sponsor, Organizer, Content generator, Partner .. many roles at same or differing times
    // string public rolecodes;  // A/F/S/O/C/P


    BigInteger multiplier108 = new BigInteger("100000000");

    BigInteger multiplier1018 = new BigInteger("1000000000000000000");

    public Activitycreateacc() throws IOException, GeneralSecurityException {
       }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_createacc);

        Button requesttokenBtn = (Button) findViewById(R.id.requestbutton);
        Button sendtokenBtn = (Button) findViewById(R.id.sendbutton);
        Button buycoin = (Button) findViewById(R.id.buycoinbutton);

        Button myprofileBtn = (Button) findViewById(R.id.profilebutton);

        TextView HbarbaltextView = (TextView) findViewById(R.id.HbarbaltextView);
        TextView accntsolidityaddrtextView = (TextView) findViewById(R.id.accntsolidityaddrtextView);
        TextView yourtokenbalTextView = (TextView) findViewById(R.id.accnttokenbaltextView);
        TextView mesglinetextView = (TextView) findViewById(R.id.messagelinetextView);
        mesglinetextView.setText(" ");

        // ImageView csicon = (ImageView) findViewById(R.id.cshomeImageView);
        ImageView csrefresh = (ImageView) findViewById(R.id.refreshImageView);
        ImageView hburger = (ImageView) findViewById(R.id.hamimageView5);

        TextView coinpriceTextView = (TextView) findViewById(R.id.TextViewcoinprice);
        EditText howmanycoin = (EditText) findViewById(R.id.buycoineditText);

        registerForContextMenu(hburger);

        ContractId exchangercontractid = ContractId.fromString(coincontroller);

        ContractId existingcontractid = ContractId.fromString(tokenidstringfull);

    }



}
