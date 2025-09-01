// SPDX-License-Identifier: MIT
pragma solidity ^0.8.19;

import "@openzeppelin/contracts/token/ERC721/ERC721.sol";
import "@openzeppelin/contracts/token/ERC721/extensions/ERC721URIStorage.sol";
import "@openzeppelin/contracts/access/Ownable.sol";
import "@openzeppelin/contracts/utils/Counters.sol";

/**
 * @title SportHub NFT Contract
 * @dev Upgraded from run.it NFT contract for sports collectibles
 * Optimized for Hedera network compatibility
 */
contract SportHubNFT is ERC721, ERC721URIStorage, Ownable {
    using Counters for Counters.Counter;
    
    Counters.Counter private _tokenIdCounter;
    
    // Sport Hub specific mappings
    mapping(uint256 => string) public sportType;
    mapping(uint256 => string) public rarity;
    mapping(uint256 => uint256) public mintTimestamp;
    mapping(string => uint256) public sportTypeCount;
    
    // Events
    event SportNFTMinted(
        address indexed to,
        uint256 indexed tokenId,
        string sportType,
        string rarity,
        string tokenURI
    );
    
    event NFTTransferred(
        address indexed from,
        address indexed to,
        uint256 indexed tokenId,
        string sportType
    );
    
    // Sport types enum for validation
    string[] public validSportTypes = [
        "Football",
        "Basketball", 
        "Soccer",
        "Baseball",
        "Tennis",
        "Hockey",
        "Golf",
        "Swimming",
        "Running",
        "Cycling"
    ];
    
    // Rarity levels
    string[] public validRarities = [
        "Common",
        "Rare", 
        "Epic",
        "Legendary",
        "Mythic"
    ];
    
    constructor() ERC721("Sport Hub NFT", "SPNFT") {}
    
    /**
     * @dev Mint a new Sport Hub NFT
     * @param to Address to mint the NFT to
     * @param uri Token URI for metadata
     * @param _sportType Type of sport (Football, Basketball, etc.)
     * @param _rarity Rarity level of the NFT
     */
    function mintSportNFT(
        address to,
        string memory uri,
        string memory _sportType,
        string memory _rarity
    ) public onlyOwner returns (uint256) {
        require(isValidSportType(_sportType), "Invalid sport type");
        require(isValidRarity(_rarity), "Invalid rarity level");
        
        uint256 tokenId = _tokenIdCounter.current();
        _tokenIdCounter.increment();
        
        _safeMint(to, tokenId);
        _setTokenURI(tokenId, uri);
        
        // Set sport-specific metadata
        sportType[tokenId] = _sportType;
        rarity[tokenId] = _rarity;
        mintTimestamp[tokenId] = block.timestamp;
        sportTypeCount[_sportType]++;
        
        emit SportNFTMinted(to, tokenId, _sportType, _rarity, uri);
        
        return tokenId;
    }
    
    /**
     * @dev Batch mint multiple NFTs
     */
    function batchMintSportNFTs(
        address[] memory recipients,
        string[] memory uris,
        string[] memory sportTypes,
        string[] memory rarities
    ) public onlyOwner {
        require(
            recipients.length == uris.length &&
            uris.length == sportTypes.length &&
            sportTypes.length == rarities.length,
            "Array lengths must match"
        );
        
        for (uint256 i = 0; i < recipients.length; i++) {
            mintSportNFT(recipients[i], uris[i], sportTypes[i], rarities[i]);
        }
    }
    
    /**
     * @dev Get NFT details including sport metadata
     */
    function getNFTDetails(uint256 tokenId) public view returns (
        address owner,
        string memory uri,
        string memory sport,
        string memory rarityLevel,
        uint256 mintTime
    ) {
        require(_exists(tokenId), "NFT does not exist");
        
        return (
            ownerOf(tokenId),
            tokenURI(tokenId),
            sportType[tokenId],
            rarity[tokenId],
            mintTimestamp[tokenId]
        );
    }
    
    /**
     * @dev Get all NFTs owned by an address
     */
    function getOwnerNFTs(address owner) public view returns (uint256[] memory) {
        uint256 ownerBalance = balanceOf(owner);
        uint256[] memory ownerTokens = new uint256[](ownerBalance);
        uint256 currentIndex = 0;
        
        for (uint256 i = 0; i < _tokenIdCounter.current(); i++) {
            if (_exists(i) && ownerOf(i) == owner) {
                ownerTokens[currentIndex] = i;
                currentIndex++;
            }
        }
        
        return ownerTokens;
    }
    
    /**
     * @dev Get NFTs by sport type
     */
    function getNFTsBySport(string memory _sportType) public view returns (uint256[] memory) {
        uint256 count = sportTypeCount[_sportType];
        uint256[] memory sportTokens = new uint256[](count);
        uint256 currentIndex = 0;
        
        for (uint256 i = 0; i < _tokenIdCounter.current(); i++) {
            if (_exists(i) && keccak256(bytes(sportType[i])) == keccak256(bytes(_sportType))) {
                sportTokens[currentIndex] = i;
                currentIndex++;
            }
        }
        
        return sportTokens;
    }
    
    /**
     * @dev Transfer NFT with sport-specific event
     */
    function transferFrom(address from, address to, uint256 tokenId) public override {
        super.transferFrom(from, to, tokenId);
        emit NFTTransferred(from, to, tokenId, sportType[tokenId]);
    }
    
    /**
     * @dev Safe transfer with sport-specific event
     */
    function safeTransferFrom(address from, address to, uint256 tokenId) public override {
        super.safeTransferFrom(from, to, tokenId);
        emit NFTTransferred(from, to, tokenId, sportType[tokenId]);
    }
    
    /**
     * @dev Get total supply of minted NFTs
     */
    function totalSupply() public view returns (uint256) {
        return _tokenIdCounter.current();
    }
    
    /**
     * @dev Validate sport type
     */
    function isValidSportType(string memory _sportType) internal view returns (bool) {
        for (uint256 i = 0; i < validSportTypes.length; i++) {
            if (keccak256(bytes(validSportTypes[i])) == keccak256(bytes(_sportType))) {
                return true;
            }
        }
        return false;
    }
    
    /**
     * @dev Validate rarity
     */
    function isValidRarity(string memory _rarity) internal view returns (bool) {
        for (uint256 i = 0; i < validRarities.length; i++) {
            if (keccak256(bytes(validRarities[i])) == keccak256(bytes(_rarity))) {
                return true;
            }
        }
        return false;
    }
    
    // Override required functions
    function _burn(uint256 tokenId) internal override(ERC721, ERC721URIStorage) {
        super._burn(tokenId);
    }
    
    function tokenURI(uint256 tokenId) public view override(ERC721, ERC721URIStorage) returns (string memory) {
        return super.tokenURI(tokenId);
    }
    
    function supportsInterface(bytes4 interfaceId) public view override(ERC721, ERC721URIStorage) returns (bool) {
        return super.supportsInterface(interfaceId);
    }
}