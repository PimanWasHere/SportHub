package com.example.runit;

import java.math.BigInteger;
import java.time.Instant;
import java.util.Date;

private class Runitprofile {


    private Date timeofthisscinfo;  // utc time from hedera at time of this call to get this DTO

    public String timefromhederautc;  // time of instantiation of this object data ie DTO, so user can refresh if they need to

    private String nickname;

    private String fname;
    private String lname;
    private String gender; // NA
    private BigInteger phone;
    private String nationality;

// Soul can be Athlete, Fan, Sponsor, Organizer, Content generator, Partner .. many roles at same or differing times
    private String rolecodes;  // A/F/S/O/C/P
    private String profilehederafileid;
    private String profiledataipfshash;


    private Runitprofile() {

        timeofthisscinfo = Date.from(Instant.now());

    }

}
