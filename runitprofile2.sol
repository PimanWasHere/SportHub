//SPDX-License-Identifier: MIT
pragma solidity >= 0.4.22 <0.9.0;

// The agreement contract as called by Ricardian Fabric

interface ISimpleTerms {
    event NewTerms(string url);
    event NewParticipant(address indexed participantAddress);

    /**
     * setTerms
     *
     * Adds new terms to the smart contract.
     * This is called by Ricardian Fabric when a new agreement is deployed.
     * Can be only called by the issuer.
     * The url is the acceptable contracts page and value is the hash of the agreement passed in from Ricardian Fabric
     */
    function setTerms(string calldata url, string calldata value)
    external
    returns (bool);

    /**
     * getTerms
     *
     *  You can get the terms for the contract by calling this function.
     *
     */
    function getTerms() external view returns (string memory);

    /**
     * Accept
     *
     * When a contract is accepted in Ricardian Fabric, it calls this function.
     */
    function accept(string calldata value) external;

    /**
     * acceptedTerms
     *
     * This external view function is used to verify if an address has accepted the agreement already.
     *
     */
    function acceptedTerms(address _address) external view returns (bool);
}


pragma solidity >= 0.4.22 <0.9.0;


contract SimpleTerms is ISimpleTerms{
    //event NewTerms(string url);
    //event NewParticipant(address indexed participant);

    //The issuer must create the agreement on ricardian fabric
    address public issuer;

    Terms private terms;

    struct Terms {
        string url;
        bytes32 value;
    }

    // The key here is the hash from the terms hashed with the agreeing address.
    mapping(bytes32 => Participant) private agreements;

    // The participant any wallet that accepts the terms.
    struct Participant {
        bool signed;
    }

    constructor() {
        issuer = msg.sender;
    }

    /* The setTerms allows an issuer to add new Term to their contract
       Error code 901: "Only the deployer can call this."
    */
    function setTerms(string calldata url, string calldata value)
    external
    returns (bool)
    {
        require(msg.sender == issuer, "901");
        // If the issuer signature is detected, the terms can be updated
        terms = Terms({url: url, value: keccak256(abi.encodePacked(value))});
        emit NewTerms(url);
        return true;
    }

    /* The accept function is called when a user accepts an agreement represented by the hash

       Error code 902: "Invalid terms."
    */
    function accept(string calldata value) external {
        require(
            keccak256(abi.encodePacked(value)) == terms.value,
            "902"
        );
        bytes32 access = keccak256(abi.encodePacked(msg.sender, terms.value));
        agreements[access] = Participant({signed: true});
        emit NewParticipant(msg.sender);
    }

    // We can check if an address accepted the current terms or not
    function acceptedTerms(address _address) external view returns (bool) {
        bytes32 access = keccak256(abi.encodePacked(_address, terms.value));
        return agreements[access].signed;
    }

    // Get the terms url to display it so people can visit it and accept it
    function getTerms() external view returns (string memory) {
        return (terms.url);
    }

    /* The modifier allows a contract inheriting from this, to controll access easily based on agreement signing.

       Error code 903: "You must accept the terms first."
    */
    modifier checkAcceptance() {
        bytes32 access = keccak256(abi.encodePacked(msg.sender, terms.value));
        require(agreements[access].signed, "903");
        _;
    }
}


contract Ownable {
    address payable public owner;


    event OwnershipRenounced(address indexed previousOwner);
    event OwnershipTransferred(
        address indexed previousOwner,
        address indexed newOwner
    );


    /**
     * @dev The Ownable constructor sets the original `owner` of the contract to the sender
   * account.
   */
    constructor() {
        owner = payable(msg.sender);
    }

    /**
     * @dev Throws if called by any account other than the owner.
   */
    modifier onlyOwner() {
        require(msg.sender == owner);
        _;
    }

    // As below - A Soul cannot transfer their profile nor renounceOwnership (but they can delete the contract)


    /**
     * @dev Allows the current owner to transfer control of the contract to a newOwner.
   * @param newOwner The address to transfer ownership to.

  function transferOwnership(address newOwner) public onlyOwner {
    require(newOwner != address(0));
    emit OwnershipTransferred(owner, newOwner);
    owner = newOwner;
  }
  */

    /**
     * @dev Allows the current owner to relinquish control of the contract.

  function renounceOwnership() public onlyOwner {
    emit OwnershipRenounced(owner);
    owner = address(0);
  }
  */

}







contract runitprofile2 is Ownable, SimpleTerms {
    using SafeMath for uint256;

    // Simple terms added as acceptance - Ricardian contract attached images accepted - held in mapping
    //
    // owner set via ownable constructor to deployer
    // Run.it has 'Souls' not 'Users'.. they own their own data.


    string private fname;
    string private lname;
    string private nickname;
    string private phone;
    string private nationality;
    string private rolecode;
    // rolecode permitted values P/F/S/C/B/R/D   R=sponsor - for duplicate and ease of install.
    address private runitaccountid; // this is the Run.it accountid that holds HBARs and holds RUN tokens in the ERC20
    uint256 private runitbal;  // updated from Run token SC when inquiry refresh _isApprovedOrOwner
    string private hederafileid;    // this is the Run.it accountid in Users terms (holds account/keys,pwrdhash,profile scc addr)
    string private dataipfshash;
    string private vendordomain;
    address private platformaddress;





    // demo, behavioral, interests
    // can be kept on a tiny cost hedera file or pinned to IPFS for PoC / pre-production
    // hedera files, like SCs are on Hedera to ABFT math defined level of security. - the highest known.

    // mapping(address => uint256) hedera interests file; // for use later for PoC .. maybe keep interest keywords on a hedera file - cheaper in GAS

    // Likes and interests - PoC small interest selection for demo purposes. 'categories to choose from or indexes'.

    string private interest1;
    string private interest2;
    string private interest3;

    // the following are public so platform can see the openness OR not of the Owner, as permissions for the platform and Sponsors to
    // see the data owners decisions ie so Data owner canb get RUN token rewards
    // - but only Contract OnlyOwner can update.

    bool public demographic;
    bool public behavioral;
    bool public interests;

    uint256 public sponsorslevel;
    uint256 public grpsponsorslevel;
    bool public kycapproved;           //  set true or false - after 3rd praty plugin/ or in App KYC - driv lic pic or other TBD


    // Sponsor exposure measure 0 - 10 0= none, 3 low, 5 medium, 10 high  to maximize rewards.

    // If a approved vendor - by ricardian - then User can store official domains and a products specific keypair in their profile

    Officialproduct private officialproduct;

    struct Officialproduct {
        string domain;
        string pubkey;
        string prikey;
        bytes32 value;
    }

    mapping(bytes32 => Officialvendor) private validdomainandproduct;

    // If the vendors site and product are still valid. ie currently official in the RUN system
    struct Officialvendor{
        bool valid;
    }



    constructor(string memory _fname, string memory _lname, string memory _nickname, string memory _phone, string memory _nationality, string memory _rolecode, address _aacountid, uint256 _initialrunbal, string memory _hederafileid, string memory _dataipfshash, address _platformaddress) {

        // Run.it account is a hedera public key/assigned Account assigned at time of onboarding.
        // impersonation is IMPOSSIBLE if friends aware of the public key and Run.it account# for this Soul

        runitaccountid = msg.sender;
        platformaddress = _platformaddress;

        fname = _fname;
        lname = _lname;
        nickname = _nickname;
        phone = _phone;
        nationality = _nationality;
        rolecode = _rolecode;
        runitaccountid = _aacountid;
        runitbal = _initialrunbal;
        hederafileid = _hederafileid;
        dataipfshash = _dataipfshash;

        kycapproved = false;


    }


    // Events broadcast to ledger as public but anonymous receipt, if needs be.


    event Profilecreated(
        address smartcontractid
    );

    event Profileupdated(
        address runitaccountid
    );

    event Interestsupdated(
        address runitaccountid
    );

    event Opennessupdated(
        address runitaccountid
    );


    // custom Modifier for Owner OR Platform - Run.it

    modifier onlyOwnerorrunit() {
        require((msg.sender == platformaddress) || (msg.sender == owner));
        _;
    }

    modifier onlyrunit() {
        require(msg.sender == platformaddress);
        _;
    }

    // new logic to set, get and update official domains and product keypairs

    //  "Only the owner can call this and ONLY after they have a signed Ricardian Official Vendor terms contract - see modifier check checkAcceptance


    function setOfficialproduct(string calldata domain, string calldata pubkey, string calldata prikey, string calldata value) onlyOwner checkAcceptance
    external
    returns (bool)
    {
        officialproduct = Officialproduct({domain: domain, pubkey: pubkey, prikey: prikey, value: keccak256(abi.encodePacked(value))});
        // emit. ?
        return true;
    }

    // The updatedomainproduct function is called by the platform when domain is no longer official - on the platform

    function updatedomainproduct(string calldata value) onlyrunit external {
        require(
            keccak256(abi.encodePacked(value)) == officialproduct.value
        //emit ?
        );
        bytes32 access = keccak256(abi.encodePacked(msg.sender, officialproduct.value));
        validdomainandproduct[access] = Officialvendor({valid: false});
        // emit ?
    }

    // We can check if a domain and product key is valid or not for the calling owner
    function checkvalidOfficialproduct(address _address) onlyOwnerorrunit external view returns (bool) {
        bytes32 access = keccak256(abi.encodePacked(_address, officialproduct.value));
        return validdomainandproduct[access].valid;
    }

    //TO BE DONE Get the domain and keys if caller is the Owner
    function getOfficialproduct() onlyOwner external view returns (Officialproduct memory) {

        return (officialproduct);
    }







    // getters for Owner only!

    function getfname() view onlyOwner public returns(string memory) {

        return fname;
    }


    function getlname() view onlyOwner public returns(string memory) {

        return lname;
    }

    function getnickname() view onlyOwner public returns(string memory) {

        return nickname;
    }


    function getphone() view onlyOwner public returns(string memory ) {

        return phone;
    }


    function getnationality() view onlyOwner public returns(string memory) {

        return nationality;
    }

    // only platform and Owner can see the roles - needed by DApp to custom the dashboard



    function getrolecode() view onlyOwnerorrunit public returns(string memory) {

        return rolecode;
    }

    function getrunaccountid() view onlyOwnerorrunit public returns(address) {

        return runitaccountid;
    }

    // in case Soul forgets their Runit account id. ie the hedera fileid.
    function gethederafileid() view onlyOwnerorrunit public returns(string memory) {

        return hederafileid;
    }

    function getdataipfshash() view onlyOwner public returns(string memory) {

        return dataipfshash;
    }


    function getvendordomain() view onlyOwner public returns(string memory) {
        return vendordomain;
    }


    function getinterest1() view onlyOwner public returns(string memory) {

        return interest1;
    }


    function getinterest2() view onlyOwner public returns(string memory) {

        return interest2;
    }


    function getinterest3() view onlyOwner public returns(string memory) {

        return interest3;
    }

    // Run.it OR orfile Owner can see these - for matching purposes IF they have profile openness permission - TBD on degree of/ granularity
    // this will a re-write for an array of interest.. more interests more gas it will cost user to maintain - micro cents!
    // this will also apply to profile getter methods for MvP later on .. for demo and  behaviorial

    function runitgetinterest1() view onlyOwnerorrunit public returns(string memory) {

        return interest1;
    }


    function runitgetinterest2() view onlyOwnerorrunit public returns(string memory) {

        return interest2;
    }


    function runitgetinterest3() view onlyOwnerorrunit public returns(string memory) {

        return interest3;
    }






    // update profile by Soul ONLY ie OnlyOwner.


    function updateprofile (string calldata _fname, string calldata _lname, string calldata _nickname, string calldata _phone, string calldata _nationality, string calldata _rolecode)  public  onlyOwner{

        fname = _fname;
        lname = _lname;
        nickname = _nickname;
        phone = _phone;
        nationality = _nationality;
        rolecode = _rolecode;

    }

    // used to update the profile because the profile SC contractID is stored in the Run.it fileID(ieaccount) But
    // the profile also is to hold the run.it account (hedera fileid).. chick & egg. So this method below is called after the File create in the DApp

    function updaterunitaccountid(string calldata _createdrunitfileid) public onlyOwner {

        hederafileid = _createdrunitfileid;

    }

    // update profile User MUST have accepted Core image link of Ricardian SC - RUN DApp terms of Updating Profile Interests Acceptance Doc


    function updateinterests(string calldata _interest1, string calldata _interest2, string calldata _interest3,  bool _demo, bool _behav, bool  _inter, uint256 _sponsorslevel, uint256 _grpsponsorslevel) public checkAcceptance onlyOwner {


        interest1 = _interest1;
        interest2 = _interest2;
        interest3 = _interest3;

        demographic = _demo;
        behavioral = _behav;
        interests = _inter;

        sponsorslevel = _sponsorslevel;
        grpsponsorslevel = _grpsponsorslevel;

    }



    // ONLY a RUN Vendor can update their official domain - for product registry controlled by Ricardian accepted Smart Contract Terms - images hash mapped.

    function updatedomain(string calldata _officialdomainofvendor) public checkAcceptance onlyOwner {
        vendordomain = _officialdomainofvendor;
    }


    function removeprofile() public onlyOwner{

        // selfdestruct profile smart contract by the Run.it Soul only  any hbar held in it goes to the Owner.

        selfdestruct(owner);

    }



}



library SafeMath {

    /**
    * @dev Multiplies two numbers, throws on overflow.
  */
    function mul(uint256 a, uint256 b) internal pure returns (uint256) {
        if (a == 0) {
            return 0;
        }
        uint256 c = a * b;
        assert(c / a == b);
        return c;
    }

    /**
    * @dev Integer division of two numbers, truncating the quotient.
  */
    function div(uint256 a, uint256 b) internal pure returns (uint256) {
        // assert(b > 0); // Solidity automatically throws when dividing by 0
        uint256 c = a / b;
        // assert(a == b * c + a % b); // There is no case in which this doesn't hold
        return c;
    }

    /**
    * @dev Subtracts two numbers, throws on overflow (i.e. if subtrahend is greater than minuend).
  */
    function sub(uint256 a, uint256 b) internal pure returns (uint256) {
        assert(b <= a);
        return a - b;
    }

    /**
    * @dev Adds two numbers, throws on overflow.
  */
    function add(uint256 a, uint256 b) internal pure returns (uint256) {
        uint256 c = a + b;
        assert(c >= a);
        return c;
    }
}

