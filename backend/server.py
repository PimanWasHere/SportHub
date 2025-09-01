from fastapi import FastAPI, HTTPException
from fastapi.middleware.cors import CORSMiddleware
from pydantic import BaseModel
import os
from typing import List, Optional
import uvicorn

app = FastAPI(title="Sport Hub API", version="1.0.0")

# CORS setup for frontend integration
app.add_middleware(
    CORSMiddleware,
    allow_origins=["*"],  # Configure appropriately for production
    allow_credentials=True,
    allow_methods=["*"],
    allow_headers=["*"],
)

# Pydantic models
class NFTModel(BaseModel):
    token_id: str
    name: str
    description: str
    image_url: str
    sport_type: str
    rarity: str
    owner: str

class WalletModel(BaseModel):
    account_id: str
    balance: str

class TransferModel(BaseModel):
    from_account: str
    to_account: str
    token_id: str

# Root endpoint
@app.get("/")
async def root():
    return {"message": "Sport Hub API - Web3 Sports NFT Platform"}

# Health check
@app.get("/api/health")
async def health_check():
    return {"status": "healthy", "service": "Sport Hub Backend"}

# NFT endpoints
@app.get("/api/nfts", response_model=List[NFTModel])
async def get_all_nfts():
    """Get all NFTs in the collection"""
    # Mock data for now - will integrate with Hedera in next phase
    mock_nfts = [
        {
            "token_id": "1",
            "name": "Champion Football",
            "description": "Legendary football from the championship game",
            "image_url": "https://via.placeholder.com/300x300/FF6B35/FFFFFF?text=Football",
            "sport_type": "Football",
            "rarity": "Legendary",
            "owner": "0.0.123456"
        },
        {
            "token_id": "2", 
            "name": "Golden Basketball",
            "description": "Rare basketball with golden finish",
            "image_url": "https://via.placeholder.com/300x300/FFD700/000000?text=Basketball",
            "sport_type": "Basketball",
            "rarity": "Rare",
            "owner": "0.0.123457"
        },
        {
            "token_id": "3",
            "name": "Victory Soccer Ball",
            "description": "Soccer ball from the World Cup final",
            "image_url": "https://via.placeholder.com/300x300/00A86B/FFFFFF?text=Soccer",
            "sport_type": "Soccer",
            "rarity": "Epic",
            "owner": "0.0.123458"
        }
    ]
    return mock_nfts

@app.get("/api/nfts/wallet/{account_id}", response_model=List[NFTModel])
async def get_wallet_nfts(account_id: str):
    """Get NFTs owned by a specific wallet"""
    # Mock implementation - will integrate with Hedera
    all_nfts = await get_all_nfts()
    wallet_nfts = [nft for nft in all_nfts if nft['owner'] == account_id]
    return wallet_nfts

@app.get("/api/nfts/{token_id}", response_model=NFTModel)
async def get_nft_details(token_id: str):
    """Get details of a specific NFT"""
    all_nfts = await get_all_nfts()
    nft = next((nft for nft in all_nfts if nft.token_id == token_id), None)
    if not nft:
        raise HTTPException(status_code=404, detail="NFT not found")
    return nft

@app.post("/api/nfts/mint")
async def mint_nft(nft_data: NFTModel):
    """Mint a new Sport Hub NFT"""
    # Will integrate with Hedera smart contracts
    return {"message": "NFT minting initiated", "token_id": nft_data.token_id}

@app.post("/api/nfts/transfer")
async def transfer_nft(transfer_data: TransferModel):
    """Transfer NFT between wallets"""
    # Will integrate with Hedera smart contracts
    return {
        "message": "NFT transfer initiated",
        "from": transfer_data.from_account,
        "to": transfer_data.to_account,
        "token_id": transfer_data.token_id
    }

# Wallet endpoints
@app.get("/api/wallet/{account_id}", response_model=WalletModel)
async def get_wallet_info(account_id: str):
    """Get wallet information"""
    return {
        "account_id": account_id,
        "balance": "1000.0"  # Mock balance
    }

# Sports categories endpoint
@app.get("/api/sports")
async def get_sports_categories():
    """Get available sports categories"""
    return {
        "categories": [
            {"name": "Football", "icon": "🏈", "count": 150},
            {"name": "Basketball", "icon": "🏀", "count": 230},
            {"name": "Soccer", "icon": "⚽", "count": 180},
            {"name": "Baseball", "icon": "⚾", "count": 120},
            {"name": "Tennis", "icon": "🎾", "count": 90},
            {"name": "Hockey", "icon": "🏒", "count": 85}
        ]
    }

if __name__ == "__main__":
    uvicorn.run(app, host="0.0.0.0", port=8001)