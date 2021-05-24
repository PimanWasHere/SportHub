package com.example.runit;


import com.google.protobuf.ByteString;
import com.hedera.hashgraph.sdk.*;

import javax.crypto.*;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.SecretKeySpec;
import java.io.UnsupportedEncodingException;
import java.math.BigInteger;
import java.security.InvalidKeyException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.spec.InvalidKeySpecException;
import java.text.SimpleDateFormat;
import java.time.Duration;
import java.util.*;
import java.util.concurrent.TimeoutException;


public final class HederaServices {


    private static final AccountId OPERATOR_ID = AccountId.fromString("0.0.6655");
    private static final PrivateKey OPERATOR_KEY = PrivateKey.fromString("302e020100300506032b657004220420163d9853ea3297b26863c0956c8085516c86a756be0819d655ab61cfdadbb1ab");
    // final mainnet op accnt below..
    // final AccountId OPERATOR_ID = AccountId.fromString("0.0.199327");
    // final PrivateKey OPERATOR_KEY = PrivateKey.fromString("302e020100300506032b657004220420b0d50bf0fd1282ec23e164b99cb5921adbf63553dffba0bf2777935f031acd72");

    // private static final AccountId OPERATOR_ID = AccountId.fromString("0.0.910");
    // private static final PrivateKey OPERATOR_KEY = PrivateKey.fromString("302e020100300506032b657004220420ed273f33e01e572f82e556dcf72f671743fb177cee668d7aa8461409f9279ba1");

    private static Client OPERATING_ACCOUNT = null;
    private static Client USER_ACCOUNT = null;
    private static GennedAccount GENNED_ACCOUNT = null;

    private static final FileId hbarfuturesfileofscs = FileId.fromString("0.0.405428");

    private static final FileId hbarfutbytecodefile = FileId.fromString("0.0.611694");
    // test on mainnet fileid 0.0.193436


    public static void createoperatorClient() {

        System.out.println(".. connecting to Hedera nodes from Operating Account..");

        /*


        Client hederaClient = Client.fromJson("{\n" +
                " \"network\": {\n" +
                " \"0.0.4\" : \"1.testnet.hedera.com:50211\",\n" +
                " \"0.0.5\" : \"2.testnet.hedera.com:50211\",\n" +
                " \"0.0.6\" : \"3.testnet.hedera.com:50211\"\n" +
                "      }\n" +
                "}");

        Client hederaClient = Client.fromJson("{\n" +
                " \"network\": {\n" +
                " \"0.0.3\" : \"0.previewnet.hedera.com:50211\",\n" +
                " \"0.0.4\" : \"1.previewnet.hedera.com:50211\",\n" +
                " \"0.0.5\" : \"2.previewnet.hedera.com:50211\",\n" +
                " \"0.0.6\" : \"3.previewnet.hedera.com:50211\"\n" +
                "      }\n" +
                "}");
*/
        OPERATING_ACCOUNT = Client.forTestnet();
        //  OPERATING_ACCOUNT = Client.forMainnet();

        OPERATING_ACCOUNT.setOperator(OPERATOR_ID, OPERATOR_KEY);
        OPERATING_ACCOUNT.setMaxQueryPayment(new Hbar(10));
        OPERATING_ACCOUNT.setMaxTransactionFee(new Hbar(100));

        System.out.println("using operating Account.. " + OPERATOR_ID.toString());


    }

    public static void createuserClient(AccountId useraccount, PrivateKey userskey)  {

        System.out.println(".. to pay for SC calls.., SC display list, Contract create, Payout etc");
 /*


        Client hederaClient = Client.fromJson("{\n" +
                " \"network\": {\n" +
                " \"0.0.4\" : \"1.testnet.hedera.com:50211\",\n" +
                " \"0.0.5\" : \"2.testnet.hedera.com:50211\",\n" +
                " \"0.0.6\" : \"3.testnet.hedera.com:50211\"\n" +
                "      }\n" +
                "}");

        Client hederaClient = Client.fromJson("{\n" +
                " \"network\": {\n" +
                " \"0.0.3\" : \"0.previewnet.hedera.com:50211\",\n" +
                " \"0.0.4\" : \"1.previewnet.hedera.com:50211\",\n" +
                " \"0.0.5\" : \"2.previewnet.hedera.com:50211\",\n" +
                " \"0.0.6\" : \"3.previewnet.hedera.com:50211\"\n" +
                "      }\n" +
                "}");
*/
        USER_ACCOUNT = Client.forTestnet();
        // USER_ACCOUNT = Client.forMainnet();

        USER_ACCOUNT.setOperator(useraccount, userskey);
        USER_ACCOUNT.setMaxQueryPayment(new Hbar(5));
        USER_ACCOUNT.setMaxTransactionFee(new Hbar(100));

        System.out.println("Connected to User's Account.. " + useraccount.toString());


    }


    public static GennedAccount createnewkeypair() throws BadMnemonicException {

        GENNED_ACCOUNT = new GennedAccount();

        return GENNED_ACCOUNT;

    }


    public static AccountId createnewaccount() throws TimeoutException, PrecheckStatusException, ReceiptStatusException {

        TransactionResponse newAccounttx = new AccountCreateTransaction()
                .setKey(GENNED_ACCOUNT.newPublicKey)
                .setInitialBalance(new Hbar(201))
                //.setInitialBalance(100_000_000) // not mandatory for create?
                .execute(OPERATING_ACCOUNT);

        TransactionReceipt receipt = newAccounttx.getReceipt(OPERATING_ACCOUNT);

        AccountId newAccountId = receipt.accountId;

        System.out.println("created new AccountID is  = " + newAccountId);

        return newAccountId;
    }


    public static FileId createuserstore(AccountId newaccountId, String pword) throws NoSuchAlgorithmException, InvalidKeySpecException, UnsupportedEncodingException, NoSuchPaddingException, InvalidKeyException, BadPaddingException, IllegalBlockSizeException, TimeoutException, PrecheckStatusException, ReceiptStatusException {

        // salt and then hash, then composite then encrypt with operating key then write to Hedera file
        // PBKDF2 in Java for hash - about as secure as it gets.. THEN it is AES/ECB/PKCS5Padding encrypted

        String generatedSecuredPasswordHash = generateStorngPasswordHash(pword);
        System.out.println(generatedSecuredPasswordHash);

        // random salt is pre-pended

        // for testing   String kycin = "0.0.12345/thebprivate999key/" + generatedSecuredPasswordHash;

        String accntandkeyhash = newaccountId.toString() + "/" + GENNED_ACCOUNT.newPrivKey.toString() + "/" + generatedSecuredPasswordHash;

        System.out.println("FOR TESTING - : " + accntandkeyhash);

        // less than 6K so no append needed

        SecretKeySpec secretKey;
        byte[] key;

        MessageDigest sha = null;

        key = pword.getBytes("UTF-8");
        sha = MessageDigest.getInstance("SHA-1");
        key = sha.digest(key);
        key = Arrays.copyOf(key, 16);
        secretKey = new SecretKeySpec(key, "AES");
        Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
        cipher.init(Cipher.ENCRYPT_MODE, secretKey);
        String encrytpedstring = Base64.getEncoder().encodeToString(cipher.doFinal(accntandkeyhash.getBytes("UTF-8")));

        // now writing encrypted to Hedera file Service

        // User's OR platform pay for FileID create ??? the question reimburse the
        // platform when funds deposited/ present upon contract create

        byte[] encrypt = encrytpedstring.getBytes();

        // for testing to be removed

        // System.out.println("encrypted as bytes to string on hedera : " + encrytpedstring);
        //System.out.println("encrypted as bytes on hedera : " + encrypt);

        TransactionResponse fileTxId2 = new FileCreateTransaction()
                .setKeys(OPERATOR_KEY.getPublicKey())
                .setContents(encrypt)
                .setMaxTransactionFee(new Hbar(3))
                .execute(OPERATING_ACCOUNT);

        TransactionReceipt fileReceipt2 = fileTxId2.getReceipt(OPERATING_ACCOUNT);

        FileId newFileId = fileReceipt2.fileId;

        return newFileId;
    }


    private static String generateStorngPasswordHash(String password) throws NoSuchAlgorithmException, InvalidKeySpecException {
        int iterations = 1000;
        char[] chars = password.toCharArray();
        byte[] salt = getSalt();

        PBEKeySpec spec = new PBEKeySpec(chars, salt, iterations, 64 * 8);
        SecretKeyFactory skf = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA1");
        byte[] hash = skf.generateSecret(spec).getEncoded();
        return iterations + ":" + toHex(salt) + ":" + toHex(hash);
    }

    private static byte[] getSalt() throws NoSuchAlgorithmException {
        SecureRandom sr = SecureRandom.getInstance("SHA1PRNG");
        byte[] salt = new byte[16];
        sr.nextBytes(salt);
        return salt;
    }

    private static String toHex(byte[] array) throws NoSuchAlgorithmException {
        BigInteger bi = new BigInteger(1, array);
        String hex = bi.toString(16);
        int paddingLength = (array.length * 2) - hex.length();
        if (paddingLength > 0) {
            return String.format("%0" + paddingLength + "d", 0) + hex;
        } else {
            return hex;
        }
    }


    public static ByteString gethederafile(String hederafileid) throws TimeoutException, PrecheckStatusException {
        // Defaults the operator account ID and key such that all generated transactions will be paid for
        // by this account and be signed by this key

        FileId existingfileid = FileId.fromString(hederafileid);

        ByteString hederafilecontents = new FileContentsQuery()
                .setFileId(existingfileid)
                .execute(OPERATING_ACCOUNT);

        return hederafilecontents;
    }


    public static Hbar getbalance(String accountEntry) throws TimeoutException, PrecheckStatusException {

        Hbar balance;

        AccountId accounttoquery = AccountId.fromString(accountEntry);

        balance = new AccountBalanceQuery()
                .setAccountId(accounttoquery)
                .execute(USER_ACCOUNT)
                .hbars;

        return balance;
    }


    public static ContractId createdeployedprofile(String _fname, String _lname, String _nickname, String _phone, String _nationality, String _rolecodes, String _profilehederafileid, String _profiledataipfshash) throws TimeoutException, PrecheckStatusException, ReceiptStatusException {
        String newcontractid = null;

        // constructor(string _fname, string _lname, string _nickname, string _phone, string _nationality, string _rolecodes, string _profilehederafileid, string _profiledataipfshash, address _platformaddress) public {

        // set admin key to zero address - this is done by Hedera as default.

        System.out.println("bytecode file " +  hbarfutbytecodefile.toString());

        TransactionResponse contractcreatetran = new ContractCreateTransaction()
                .setAutoRenewPeriod(Duration.ofSeconds(7890000)) //   90 days in seconds, is the autorenew when the creator account will have to pay modest renewfee
                .setGas(gasinlong) // set by user
                .setBytecodeFileId(hbarfutbytecodefile)
                .setConstructorParameters(
                        new ContractFunctionParameters()
                                .addString(_fname)
                                .addString(_lname)
                                .addString(_nickname)
                                .addString(_phone)
                                .addString(_nationality)
                                .addString(_rolecodes)
                                .addString(_profilehederafileid)
                                .addString(_profiledataipfshash))
                .setContractMemo("A Run.it profile Smart Contract")
                .execute(USER_ACCOUNT);

        TransactionReceipt createreceipt = contractcreatetran.getReceipt(USER_ACCOUNT);

        ContractId provisionalcontractid = Objects.requireNonNull(createreceipt.contractId);

        return provisionalcontractid;


    }

    public static String getutcstringtimestamp(BigInteger timeinmilliseconds) {

        // to show time in UTC Zulu to ALL global traders.

        long seconds = 1320105600;
        long millis = timeinmilliseconds.longValue() * 1000;

        Date hhutcdate = new Date(millis);
        SimpleDateFormat sdf = new SimpleDateFormat("EEEE,MMMM d,yyyy h:mm,a", Locale.ENGLISH);
        sdf.setTimeZone(TimeZone.getTimeZone("UTC"));
        String formattedDate = sdf.format(hhutcdate);

        return formattedDate;
    }

    public static String getlocalstringtimestamp(BigInteger timeinmilliseconds) {

        // to show time in Traders Server ip address timezone

        long seconds = 1320105600;
        long millis = timeinmilliseconds.longValue() * 1000;

        Date localdate = new Date(millis);
        SimpleDateFormat sdf = new SimpleDateFormat("EEEE,MMMM d,yyyy h:mm,a", Locale.ENGLISH);
        sdf.setTimeZone(TimeZone.getDefault());
        String formattedDate = sdf.format(localdate);

        return formattedDate;
    }


    public static Runitprofile getacontract(ContractId existingcontractid) throws TimeoutException, PrecheckStatusException, ReceiptStatusException {

        System.out.println(" contract id in " + existingcontractid.toString());

        // first we call the timecheck function to refesh any status - ie state

        TransactionResponse refreshstatusduetotime = new ContractExecuteTransaction()
                .setGas(100000000)
                .setContractId(existingcontractid)
                .setFunction("timecheckstatuschange")
                .execute(USER_ACCOUNT);

        refreshstatusduetotime.getReceipt(USER_ACCOUNT);

        System.out.println("1" +  refreshstatusduetotime.getReceipt(USER_ACCOUNT).toString());
        // creating contract POJO

        Runitprofile contractdetails = new Runitprofile();


        ContractFunctionResult result1 = new ContractCallQuery()
                .setGas(30000)
                .setContractId(existingcontractid)
                .setFunction("lastconsensustimestamp")
                .execute(USER_ACCOUNT);

        if (result1.errorMessage != null) {
            System.out.println("Error calling Contract " + result1.errorMessage);
            return contractdetails;
        }
        System.out.println("2");

        System.out.println("time from hedera consensus-time in seconds from sc is " + result1.getUint256(0));

        contractdetails.timefromhederautc = getutcstringtimestamp(result1.getUint256(0));


        ContractFunctionResult result2 = new ContractCallQuery()
                .setGas(30000)
                .setContractId(existingcontractid)
                .setFunction("offeringclosedday_inseconds")
                .execute(USER_ACCOUNT);

        if (result2.errorMessage != null) {
            System.out.println("Error calling Contract " + result2.errorMessage);
            return contractdetails;
        }

        contractdetails.offeringclosedday_inseconds = result2.getUint256(0);

        System.out.println("3");

        ContractFunctionResult result3 = new ContractCallQuery()
                .setGas(30000)
                .setContractId(existingcontractid)
                .setFunction("expirationday_inseconds")
                .execute(USER_ACCOUNT);

        if (result3.errorMessage != null) {
            System.out.println("Error calling Contract " + result3.errorMessage);
            return contractdetails;
        }

        System.out.println("4");

        contractdetails.expirationday_inseconds = result3.getUint256(0);
        System.out.println("expiration time in seconds from sc is " + contractdetails.expirationday_inseconds);

        // in hbar not tbar denominations
        ContractFunctionResult result4 = new ContractCallQuery()
                .setGas(30000)
                .setContractId(existingcontractid)
                .setFunction("hbarheldlockedin")
                .execute(USER_ACCOUNT);

        if (result4.errorMessage != null) {
            System.out.println("Error calling Contract " + result4.errorMessage);

        }

        System.out.println("5");

        contractdetails.hbarheldlockedin = result4.getUint256(0);

        // get contractcreatted tstamp

        ContractFunctionResult result5 = new ContractCallQuery()
                .setGas(30000)
                .setContractId(existingcontractid)
                .setFunction("contractcreatedtimestamp")
                .execute(USER_ACCOUNT);

        if (result5.errorMessage != null) {
            System.out.println("Error calling Contract " + result5.errorMessage);
        }

        contractdetails.contractcreatedtimestamp = result5.getUint256(0);

        System.out.println("6");


        ContractFunctionResult result7 = new ContractCallQuery()
                .setGas(30000)
                .setContractId(existingcontractid)
                .setFunction("creatordepositaccount")
                .execute(USER_ACCOUNT);

        if (result7.errorMessage != null) {
            System.out.println("Error calling Contract " + result7.errorMessage);
            return contractdetails;
        }

        contractdetails.creatordepositaccount = result7.getAddress(0);

        System.out.println("8");

        ContractFunctionResult result8 = new ContractCallQuery()
                .setGas(30000)
                .setContractId(existingcontractid)
                .setFunction("creatorpayoutaccount")
                .execute(USER_ACCOUNT);

        if (result8.errorMessage != null) {
            System.out.println("Error calling Contract " + result8.errorMessage);
            return contractdetails;
        }

        contractdetails.creatorpayoutaccount = result8.getAddress(0);

        System.out.println("9");

        ContractFunctionResult result9 = new ContractCallQuery()
                .setGas(30000)
                .setContractId(existingcontractid)
                .setFunction("creatorhbarprice")
                .execute(USER_ACCOUNT);

        if (result9.errorMessage != null) {
            System.out.println("Error calling Contract " + result9.errorMessage);
            return contractdetails;
        }

        contractdetails.creatorhbarprice = result9.getUint256(0);

        // get ALL statuses !

        System.out.println("11");

        ContractFunctionResult result11 = new ContractCallQuery()
                .setGas(30000)
                .setContractId(existingcontractid)
                .setFunction("contract_open")
                .execute(USER_ACCOUNT);

        if (result11.errorMessage != null) {
            System.out.println("Error calling Contract " + result11.errorMessage);
            return contractdetails;
        }

        contractdetails.contract_open = result11.getBool(0);
        System.out.println("12");


        ContractFunctionResult result12 = new ContractCallQuery()
                .setGas(30000)
                .setContractId(existingcontractid)
                .setFunction("contract_expired")
                .execute(USER_ACCOUNT);

        if (result12.errorMessage != null) {
            System.out.println("Error calling Contract " + result12.errorMessage);
            return contractdetails;
        }

        contractdetails.contract_expired = result12.getBool(0);
        System.out.println("13");


        ContractFunctionResult result13 = new ContractCallQuery()
                .setGas(30000)
                .setContractId(existingcontractid)
                .setFunction("contract_paidout")
                .execute(USER_ACCOUNT);

        if (result13.errorMessage != null) {
            System.out.println("Error calling Contract " + result13.errorMessage);
            return contractdetails;
        }

        contractdetails.contract_paidout = result13.getBool(0);

        System.out.println("14");

        ContractFunctionResult result14 = new ContractCallQuery()
                .setGas(30000)
                .setContractId(existingcontractid)
                .setFunction("contract_locked_until_expiration")
                .execute(USER_ACCOUNT);

        if (result14.errorMessage != null) {
            System.out.println("Error calling Contract " + result14.errorMessage);
            return contractdetails;
        }

        contractdetails.contract_locked_until_expiration = result14.getBool(0);
        System.out.println("15");

        ContractFunctionResult result15 = new ContractCallQuery()
                .setGas(30000)
                .setContractId(existingcontractid)
                .setFunction("counterpartydepositaccount")
                .execute(USER_ACCOUNT);

        if (result15.errorMessage != null) {
            System.out.println("Error calling Contract " + result15.errorMessage);
            return contractdetails;
        }

        contractdetails.counterpartydepositaccount = result15.getAddress(0);

        System.out.println("16");

        ContractFunctionResult result16 = new ContractCallQuery()
                .setGas(30000)
                .setContractId(existingcontractid)
                .setFunction("counterpartypayoutaccount")
                .execute(USER_ACCOUNT);

        if (result16.errorMessage != null) {
            System.out.println("Error calling Contract " + result16.errorMessage);
            return contractdetails;
        }

        contractdetails.counterpartypayoutaccount = result16.getAddress(0);

        System.out.println("17");

        ContractFunctionResult result17 = new ContractCallQuery()
                .setGas(30000)
                .setContractId(existingcontractid)
                .setFunction("counterpartyhbarprice")
                .execute(USER_ACCOUNT);

        if (result17.errorMessage != null) {
            System.out.println("Error calling Contract " + result17.errorMessage);
            return contractdetails;
        }

        contractdetails.counterpartyhbarprice = result17.getUint256(0);

        System.out.println("18");

        ContractFunctionResult result18 = new ContractCallQuery()
                .setGas(30000)
                .setContractId(existingcontractid)
                .setFunction("hbarstrikeprice")
                .execute(USER_ACCOUNT);

        if (result18.errorMessage != null) {
            System.out.println("Error calling Contract " + result18.errorMessage);
            return contractdetails;
        }

        contractdetails.hbarstrikeprice = result18.getUint256(0);
        System.out.println("19");


        ContractFunctionResult result19 = new ContractCallQuery()
                .setGas(30000)
                .setContractId(existingcontractid)
                .setFunction("creatordifferential")
                .execute(USER_ACCOUNT);

        if (result19.errorMessage != null) {
            System.out.println("Error calling Contract " + result19.errorMessage);
            return contractdetails;
        }

        contractdetails.creatordifferential = result19.getUint256(0);
        System.out.println("20");

        ContractFunctionResult result20 = new ContractCallQuery()
                .setGas(30000)
                .setContractId(existingcontractid)
                .setFunction("counterpartydifferential")
                .execute(USER_ACCOUNT);

        if (result20.errorMessage != null) {
            System.out.println("Error calling Contract " + result20.errorMessage);
            return contractdetails;
        }

        contractdetails.counterpartydifferential = result20.getUint256(0);
        System.out.println("21");

        ContractFunctionResult result21 = new ContractCallQuery()
                .setGas(30000)
                .setContractId(existingcontractid)
                .setFunction("paidouttoaccount_nosplit")
                .execute(USER_ACCOUNT);

        if (result21.errorMessage != null) {
            System.out.println("Error calling Contract " + result21.errorMessage);
            return contractdetails;
        }

        contractdetails.paidouttoaccount_nosplit = result21.getAddress(0);

        // get trade data creator
        System.out.println("22");

        ContractFunctionResult result22 = new ContractCallQuery()
                .setGas(30000)
                .setContractId(existingcontractid)
                .setFunction("creator_stake_forsale_flag")
                .execute(USER_ACCOUNT);

        if (result22.errorMessage != null) {
            System.out.println("Error calling Contract " + result22.errorMessage);
            return contractdetails;
        }

        contractdetails.creator_stake_forsale_flag = result22.getBool(0);
        System.out.println("23");

        ContractFunctionResult result23 = new ContractCallQuery()
                .setGas(30000)
                .setContractId(existingcontractid)
                .setFunction("creator_forsale_price")
                .execute(USER_ACCOUNT);

        if (result23.errorMessage != null) {
            System.out.println("Error calling Contract " + result23.errorMessage);
            return contractdetails;
        }

        contractdetails.creator_forsale_price = result23.getUint256(0);
        System.out.println("24");


        ContractFunctionResult result24 = new ContractCallQuery()
                .setGas(30000)
                .setContractId(existingcontractid)
                .setFunction("previous_creators_dep_account")
                .execute(USER_ACCOUNT);

        if (result24.errorMessage != null) {
            System.out.println("Error calling Contract " + result24.errorMessage);
            return contractdetails;
        }
        System.out.println("25");

        contractdetails.previous_creators_dep_account = result24.getAddress(0);

        ContractFunctionResult result25 = new ContractCallQuery()
                .setGas(30000)
                .setContractId(existingcontractid)
                .setFunction("previous_creators_pay_account")
                .execute(USER_ACCOUNT);

        if (result25.errorMessage != null) {
            System.out.println("Error calling Contract " + result25.errorMessage);
            return contractdetails;
        }

        contractdetails.previous_creators_pay_account = result25.getAddress(0);
        System.out.println("26");

        ContractFunctionResult result25_1 = new ContractCallQuery()
                .setGas(30000)
                .setContractId(existingcontractid)
                .setFunction("currentcreatorseller_nameemailphone")
                .execute(USER_ACCOUNT);

        if (result25_1.errorMessage != null) {
            System.out.println("Error calling Contract " + result25_1.errorMessage);
            return contractdetails;
        }

        System.out.println("27");

        // System.out.println("contact in as bytes32 " + result25_1.getBytes32(0));
        // System.out.println("contact in as bytes32 tostring " + infoin);

        contractdetails.currentcreator_as_seller_contactinfo =  result25_1.getString(0);


        // now for counterparty trade data..

        ContractFunctionResult result26 = new ContractCallQuery()
                .setGas(30000)
                .setContractId(existingcontractid)
                .setFunction("counterparty_stake_forsale_flag")
                .execute(USER_ACCOUNT);

        if (result26.errorMessage != null) {
            System.out.println("Error calling Contract " + result26.errorMessage);
            return contractdetails;
        }

        contractdetails.counterparty_stake_forsale_flag = result26.getBool(0);
        System.out.println("28");

        ContractFunctionResult result27 = new ContractCallQuery()
                .setGas(30000)
                .setContractId(existingcontractid)
                .setFunction("counterparty_forsale_price")
                .execute(USER_ACCOUNT);

        if (result27.errorMessage != null) {
            System.out.println("Error calling Contract " + result27.errorMessage);
            return contractdetails;
        }

        contractdetails.counterparty_forsale_price = result27.getUint256(0);

        System.out.println("29");

        ContractFunctionResult result28 = new ContractCallQuery()
                .setGas(30000)
                .setContractId(existingcontractid)
                .setFunction("previous_counterparty_dep_account")
                .execute(USER_ACCOUNT);

        if (result28.errorMessage != null) {
            System.out.println("Error calling Contract " + result28.errorMessage);
            return contractdetails;
        }

        contractdetails.previous_counterparty_dep_account = result28.getAddress(0);

        System.out.println("30");

        ContractFunctionResult result29 = new ContractCallQuery()
                .setGas(30000)
                .setContractId(existingcontractid)
                .setFunction("previous_counterparty_pay_account")
                .execute(USER_ACCOUNT);

        if (result29.errorMessage != null) {
            System.out.println("Error calling Contract " + result29.errorMessage);
            return contractdetails;
        }

        contractdetails.previous_counterparty_pay_account = result29.getAddress(0);


        ContractFunctionResult result29_1 = new ContractCallQuery()
                .setGas(30000)
                .setContractId(existingcontractid)
                .setFunction("currentcounterpartyseller_nameemailphone")
                .execute(USER_ACCOUNT);

        if (result29_1.errorMessage != null) {
            System.out.println("Error calling Contract " + result29_1.errorMessage);
            return contractdetails;
        }

        System.out.println("31");

        contractdetails.currentcounterparty_as_seller_contactinfo = result29_1.getString(0);


        // also get the hedera info
        // for transparency and pas back in the object

        ContractInfo info = new ContractInfoQuery()
                .setContractId(existingcontractid)
                .execute(USER_ACCOUNT);

        contractdetails.accountid = info.accountId.toString();
        contractdetails.adminkey = info.adminKey.toString();
        contractdetails.autorenew = Long.toString(info.autoRenewPeriod.toDays());
        contractdetails.sizeinkbytes = Long.toString(info.storage);
        contractdetails.expiration = Long.toString(info.expirationTime.toEpochMilli());


        return contractdetails;
    }









    public static TransactionRecord transferhbarfromrunit(Long hbartosendlong, String destaccnt, String memo) throws ReceiptStatusException, PrecheckStatusException, TimeoutException {

        Boolean resultstate = false;

        AccountId recipientaccount = AccountId.fromString(destaccnt);

        if (memo.isEmpty()) memo = " ";

        Hbar amount = new Hbar(hbartosendlong);

        TransactionResponse transactionResponse = new TransferTransaction()
                // .addSender and .addRecipient can be called as many times as you want as long as the total sum from
                // both sides is equivalent
                .addHbarTransfer(OPERATING_ACCOUNT.getOperatorAccountId(), amount.negated())
                .addHbarTransfer(recipientaccount, amount)
                .setTransactionMemo(memo)
                .execute(OPERATING_ACCOUNT);

        TransactionRecord record = transactionResponse.getRecord(OPERATING_ACCOUNT);

        return record;

    }

}