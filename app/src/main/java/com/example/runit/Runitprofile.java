package com.example.runit;

import java.math.BigInteger;

public class Runitprofile {


    // to as a POJO for the profile SC

    public String fname;
    public String lname;
    public String nickname;
    public String phonenum;
    public String nationality;
    public String rolecode;
    public String runitrunaccountid;
    public BigInteger runtokenbal;
    public String runitaccountid;
    public String runitipfshash;

    public String accountid; // the contract ID in this context
    public String adminkey;
    public String autorenew;
    public String sizeinkbytes;
    public String expiration;

    // Likes and interests - PoC small interest selection for demo purposes. 'categories to choose from or indexes'.

    public String interest1;
    public String interest2;
    public String interest3;

    public boolean demographic;
    public boolean behavioral;
    public boolean interests;

    public BigInteger sponsorslevel;
    public BigInteger grpsponsorslevel;

    public boolean kycapproved;           //  set true or false - after 3rd party plugin/ or in App KYC - driv lic pic or other TBD



    public Runitprofile() {

    }

}
