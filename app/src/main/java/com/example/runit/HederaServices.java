package com.example.runit;


import com.google.protobuf.ByteString;
import com.hedera.hashgraph.sdk.*;
import org.threeten.bp.Duration;
import javax.crypto.*;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.SecretKeySpec;

import java.io.Serializable;
import java.io.UnsupportedEncodingException;
import java.math.BigInteger;
import java.security.InvalidKeyException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.spec.InvalidKeySpecException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.TimeoutException;


public final class HederaServices implements  Serializable{


    private static final AccountId OPERATOR_ID = AccountId.fromString("0.0.6655");
    private static final PrivateKey OPERATOR_KEY = PrivateKey.fromString("302e020100300506032b657004220420163d9853ea3297b26863c0956c8085516c86a756be0819d655ab61cfdadbb1ab");

    private static Client OPERATING_ACCOUNT = null;
    private static Client USER_ACCOUNT = null;
    private static GennedAccount GENNED_ACCOUNT = null;

    private static final ContractId runtokensc= ContractId.fromString("0.0.651281");
    private static final FileId runitprofilefile = FileId.fromString("0.0.1863411");

    private static ContractId profileid;

    private static final BigInteger multiplier1018 = new BigInteger("1000000000000000000");


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

        System.out.println(".. to pay for SC deploy.. and subsequent calls.., SC display list, Contract create, Payout etc");
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
        USER_ACCOUNT.setMaxTransactionFee(new Hbar(50));

        System.out.println("Connected to User's Account.. " + useraccount.toString());

    }


    public static GennedAccount createnewkeypair() throws BadMnemonicException {

        GENNED_ACCOUNT = new GennedAccount();

        return GENNED_ACCOUNT;

    }


    public static AccountId createnewaccount() throws TimeoutException, PrecheckStatusException, ReceiptStatusException {

        TransactionResponse newAccounttx = new AccountCreateTransaction()
                .setKey(GENNED_ACCOUNT.newPublicKey)
                .setInitialBalance(new Hbar(1000))
                //.setInitialBalance(100_000_000) // not mandatory for create?
                .execute(OPERATING_ACCOUNT);

        TransactionReceipt receipt = newAccounttx.getReceipt(OPERATING_ACCOUNT);

        AccountId newAccountId = receipt.accountId;

        System.out.println("created new AccountID is  = " + newAccountId);

        return newAccountId;
    }


    public static FileId createuserstore(AccountId newaccountId, String pword, ContractId profileid) throws NoSuchAlgorithmException, InvalidKeySpecException, UnsupportedEncodingException, NoSuchPaddingException, InvalidKeyException, BadPaddingException, IllegalBlockSizeException, TimeoutException, PrecheckStatusException, ReceiptStatusException {

        // salt and then hash, then composite then encrypt with operating key then write to Hedera file
        // PBKDF2 in Java for hash - about as secure as it gets.. THEN it is AES/ECB/PKCS5Padding encrypted

        String generatedSecuredPasswordHash = generateStorngPasswordHash(pword);
        System.out.println(generatedSecuredPasswordHash);

        // random salt is pre-pended

        // for testing   String kycin = "0.0.12345/thebprivate999key/" + generatedSecuredPasswordHash;

        String accntandkeyhash = newaccountId.toString() + "/" + GENNED_ACCOUNT.newPrivKey.toString() + "/" + generatedSecuredPasswordHash + "/" + profileid.toString();


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


    public static ContractId createdeployedprofile(String _fname, String _lname, String _nickname, String _phone, String _nationality, String _rolecode, String _accountid, BigInteger _runbal, String _hederafileid, String _dataipfshash) throws TimeoutException, PrecheckStatusException, ReceiptStatusException {
        String newcontractid = null;

        // constructor(string _fname, string _lname, string _nickname, string _phone, string _nationality, string _rolecodes, string _profilehederafileid, string _profiledataipfshash, address _platformaddress) public {

        // set admin key to zero address - this is done by Hedera as default.

        TransactionResponse contractcreatetran = new ContractCreateTransaction()
                .setAutoRenewPeriod(Duration.ofDays(90)) //   90 days in seconds, is the autorenew when the creator account will have to pay modest renewfee
                .setGas(100_000_000) // set by user
                .setBytecodeFileId(runitprofilefile)
                .setConstructorParameters(
                        new ContractFunctionParameters()
                                .addString(_fname)
                                .addString(_lname)
                                .addString(_nickname)
                                .addString(_phone)
                                .addString(_nationality)
                                .addString(_rolecode)
                                .addAddress(_accountid)
                                .addUint256(_runbal)
                                .addString(_hederafileid)
                                .addString(_dataipfshash)
                                .addAddress(OPERATOR_ID.toSolidityAddress()))
                .setContractMemo("This is a Run.it profile Smart Contract")
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




    public static Runitprofile getacontract(String existingcontractid)  throws TimeoutException, PrecheckStatusException, ReceiptStatusException{


        // creating contract POJO

        Runitprofile runitdetails = new Runitprofile();

        // .. get all the profile details - all held private in the SC - only accessible via modifier 'only owner' - see the solidity for details


        ContractFunctionResult result_1 = new ContractCallQuery()
                .setGas(30000)
                .setContractId(ContractId.fromString(existingcontractid))
                .setFunction("getfname")
                .execute(USER_ACCOUNT);

        if (result_1.errorMessage != null) {
            System.out.println("Error calling Contract " + result_1.errorMessage);
            return runitdetails;
        }

        runitdetails.fname = result_1.getString(0);



        ContractFunctionResult result_2 = new ContractCallQuery()
                .setGas(30000)
                .setContractId(ContractId.fromString(existingcontractid))
                .setFunction("getlname")
                .execute(USER_ACCOUNT);

        if (result_2.errorMessage != null) {
            System.out.println("Error calling Contract " + result_2.errorMessage);
            return runitdetails;
        }

        runitdetails.lname = result_2.getString(0);

        ContractFunctionResult result_3 = new ContractCallQuery()
                .setGas(30000)
                .setContractId(ContractId.fromString(existingcontractid))
                .setFunction("getnickname")
                .execute(USER_ACCOUNT);

        if (result_3.errorMessage != null) {
            System.out.println("Error calling Contract " + result_3.errorMessage);
            return runitdetails;
        }

        runitdetails.nickname = result_3.getString(0);

        ContractFunctionResult result_4 = new ContractCallQuery()
                .setGas(30000)
                .setContractId(ContractId.fromString(existingcontractid))
                .setFunction("getphone")
                .execute(USER_ACCOUNT);

        if (result_4.errorMessage != null) {
            System.out.println("Error calling Contract " + result_4.errorMessage);
            return runitdetails;
        }

        runitdetails.phonenum = result_4.getString(0);


        ContractFunctionResult result_5 = new ContractCallQuery()
                .setGas(30000)
                .setContractId(ContractId.fromString(existingcontractid))
                .setFunction("getnationality")
                .execute(USER_ACCOUNT);

        if (result_5.errorMessage != null) {
            System.out.println("Error calling Contract " + result_5.errorMessage);
            return runitdetails;
        }

        runitdetails.nationality = result_5.getString(0);



        ContractFunctionResult result_6 = new ContractCallQuery()
                .setGas(30000)
                .setContractId(ContractId.fromString(existingcontractid))
                .setFunction("getrolecode")
                .execute(USER_ACCOUNT);

        if (result_6.errorMessage != null) {
            System.out.println("Error calling Contract " + result_6.errorMessage);
            return runitdetails;
        }

        runitdetails.rolecode = result_6.getString(0);


        ContractFunctionResult result_7 = new ContractCallQuery()
                .setGas(30000)
                .setContractId(ContractId.fromString(existingcontractid))
                .setFunction("getrunaccountid")
                .execute(USER_ACCOUNT);

        if (result_7.errorMessage != null) {
            System.out.println("Error calling Contract " + result_7.errorMessage);
            return runitdetails;
        }

        // using contractId but it is a fileID - Re Greg Scullard suggestion
        runitdetails.accountid = ContractId.fromSolidityAddress(result_7.getAddress(0)).toString();

        // 8 cut

        ContractFunctionResult result_9 = new ContractCallQuery()
                .setGas(30000)
                .setContractId(ContractId.fromString(existingcontractid))
                .setFunction("gethederafileid")
                .execute(USER_ACCOUNT);

        if (result_9.errorMessage != null) {
            System.out.println("Error calling Contract " + result_9.errorMessage);
            return runitdetails;
        }

        runitdetails.runitlogonaccountid = result_9.getString(0);

        // also get the hedera info
        // for transparency and pas back in the object

        ContractInfo info = new ContractInfoQuery()
                .setContractId(ContractId.fromString(existingcontractid))
                .execute(USER_ACCOUNT);

        runitdetails.accountid = info.accountId.toString();
        runitdetails.adminkey = info.adminKey.toString();
        runitdetails.memo = info.contractMemo;
        runitdetails.autorenew = Long.toString(info.autoRenewPeriod.toDays());
        runitdetails.sizeinkbytes = Long.toString(info.storage);
        runitdetails.expiration = Long.toString(info.expirationTime.toEpochMilli());


        return runitdetails;
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


    public static void runtokensfromplatform(BigInteger runtosendlong, String destaccnt) throws ReceiptStatusException, PrecheckStatusException, TimeoutException {

        // platform pays the gas to send Welcome tokens - IF USER is KYCd

        TransactionResponse contractExecTransactionResponse = new ContractExecuteTransaction()
                .setContractId(runtokensc)
                .setGas(100_000_000)
                .setFunction("transfer", new ContractFunctionParameters()
                        .addAddress(destaccnt)
                        .addUint256(runtosendlong))
                .execute(OPERATING_ACCOUNT);


        // if this doesn't throw then we know the contract executed successfully

        contractExecTransactionResponse.getReceipt(OPERATING_ACCOUNT);

    }


    public static void runtokensfromuser(BigInteger runtosendlong, String destaccnt) throws ReceiptStatusException, PrecheckStatusException, TimeoutException {

        // platform pays the gas to send Welcome tokens

        TransactionResponse contractExecTransactionResponse = new ContractExecuteTransaction()
                .setContractId(runtokensc)
                .setGas(100_000_000)
                .setFunction("transfer", new ContractFunctionParameters()
                        .addAddress(destaccnt)
                        .addUint256(runtosendlong))
                .execute(USER_ACCOUNT);


        // if this doesn't throw then we know the contract executed successfully

        contractExecTransactionResponse.getReceipt(USER_ACCOUNT);

    }


        // uses runitaccount ie the hedera account of HBAR bal and RUN token balance

    public static BigInteger getruntokenbal() throws ReceiptStatusException, PrecheckStatusException, TimeoutException {

        BigInteger tokenbal = new BigInteger("0");

        ContractFunctionResult contractCallResult01 = new ContractCallQuery()
                .setGas(3000000)
                .setContractId(runtokensc)
                .setFunction("balanceOf", new ContractFunctionParameters()
                        .addAddress(USER_ACCOUNT.getOperatorAccountId().toSolidityAddress()))
                .execute(USER_ACCOUNT);


        if (contractCallResult01.errorMessage != null) {
            System.out.println("error calling contract: " + contractCallResult01.errorMessage);
            return tokenbal;
        }

        BigInteger coinsto18 = contractCallResult01.getInt256(0);

        tokenbal = coinsto18.divide(multiplier1018);

        return tokenbal;
    }


    // used to update the profile because the profile SC contractID is stored in the Run.it fileID(ieaccount) But
    // the profile also is to hold the run.it account (hedera fileid).. chick & egg. So this method below is called after the File create in the DApp


    public static void updaterunitaccountid_inprofile(ContractId usersprofilescID, String runitaccountid) throws TimeoutException, PrecheckStatusException, ReceiptStatusException {

        TransactionResponse contractExecTransactionResponse = new ContractExecuteTransaction()
                .setContractId(usersprofilescID)
                .setGas(100_000_000)
                .setFunction("updaterunitaccountid", new ContractFunctionParameters()
                        .addString(runitaccountid))
                .execute(USER_ACCOUNT);


        // if this doesn't throw then we know the contract executed successfully

        contractExecTransactionResponse.getReceipt(USER_ACCOUNT);
    }

}