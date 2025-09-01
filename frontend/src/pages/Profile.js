import React, { useState, useEffect } from 'react';
import { User, Wallet, Trophy, Settings, Copy, ExternalLink, Star, TrendingUp } from 'lucide-react';
import { useWallet } from '../context/WalletContext';
import axios from 'axios';
import toast from 'react-hot-toast';

const Profile = () => {
  const { isConnected, accountId, balance } = useWallet();
  const [userNFTs, setUserNFTs] = useState([]);
  const [loading, setLoading] = useState(false);
  const [activeTab, setActiveTab] = useState('collection');
  const [userStats, setUserStats] = useState({
    totalNFTs: 0,
    totalValue: 0,
    rarityBreakdown: {}
  });

  useEffect(() => {
    if (isConnected && accountId) {
      fetchUserData();
    }
  }, [isConnected, accountId]);

  const fetchUserData = async () => {
    setLoading(true);
    try {
      // Fetch user's NFTs
      const response = await axios.get(`${process.env.REACT_APP_BACKEND_URL}/api/nfts/wallet/${accountId}`);
      setUserNFTs(response.data);
      
      // Calculate stats
      const stats = calculateUserStats(response.data);
      setUserStats(stats);
    } catch (error) {
      console.error('Error fetching user data:', error);
      toast.error('Failed to load profile data');
    } finally {
      setLoading(false);
    }
  };

  const calculateUserStats = (nfts) => {
    const rarityBreakdown = {};
    nfts.forEach(nft => {
      rarityBreakdown[nft.rarity] = (rarityBreakdown[nft.rarity] || 0) + 1;
    });

    return {
      totalNFTs: nfts.length,
      totalValue: nfts.length * 50, // Mock value calculation
      rarityBreakdown
    };
  };

  const copyAddress = () => {
    navigator.clipboard.writeText(accountId);
    toast.success('Address copied to clipboard!');
  };

  const getRarityColor = (rarity) => {
    const colors = {
      'Common': 'text-gray-600 bg-gray-100',
      'Rare': 'text-blue-600 bg-blue-100',
      'Epic': 'text-purple-600 bg-purple-100',
      'Legendary': 'text-yellow-600 bg-yellow-100',
      'Mythic': 'text-red-600 bg-red-100'
    };
    return colors[rarity] || 'text-gray-600 bg-gray-100';
  };

  if (!isConnected) {
    return (
      <div className="min-h-screen flex items-center justify-center bg-gray-50">
        <div className="text-center max-w-md mx-auto p-8">
          <div className="w-16 h-16 bg-gray-200 rounded-full flex items-center justify-center mx-auto mb-4">
            <Wallet className="w-8 h-8 text-gray-400" />
          </div>
          <h2 className="text-2xl font-bold text-gray-900 mb-4">Connect Your Wallet</h2>
          <p className="text-gray-600 mb-6">
            Please connect your HashPack wallet to view your profile and NFT collection.
          </p>
        </div>
      </div>
    );
  }

  return (
    <div className="min-h-screen bg-gray-50 py-8">
      <div className="max-w-6xl mx-auto px-4 sm:px-6 lg:px-8">
        {/* Profile Header */}
        <div className="bg-white rounded-2xl shadow-sm border border-gray-200 overflow-hidden mb-8">
          <div className="h-32 bg-gradient-to-r from-blue-500 to-purple-600"></div>
          <div className="px-6 pb-6">
            <div className="flex items-center space-x-4 -mt-12">
              <div className="w-24 h-24 bg-white rounded-2xl shadow-lg flex items-center justify-center border-4 border-white">
                <User className="w-12 h-12 text-gray-400" />
              </div>
              <div className="pt-12">
                <h1 className="text-2xl font-bold text-gray-900">Sports Collector</h1>
                <div className="flex items-center space-x-2 mt-1">
                  <span className="text-gray-600">{accountId}</span>
                  <button 
                    onClick={copyAddress}
                    className="text-gray-400 hover:text-gray-600 transition-colors"
                  >
                    <Copy className="w-4 h-4" />
                  </button>
                  <a 
                    href={`https://hashscan.io/testnet/account/${accountId}`}
                    target="_blank"
                    rel="noopener noreferrer"
                    className="text-gray-400 hover:text-gray-600 transition-colors"
                  >
                    <ExternalLink className="w-4 h-4" />
                  </a>
                </div>
              </div>
            </div>
          </div>
        </div>

        {/* Stats Cards */}
        <div className="grid grid-cols-1 md:grid-cols-4 gap-6 mb-8">
          <div className="bg-white rounded-xl p-6 shadow-sm border border-gray-200">
            <div className="flex items-center space-x-3">
              <div className="w-12 h-12 bg-blue-100 rounded-xl flex items-center justify-center">
                <Trophy className="w-6 h-6 text-blue-600" />
              </div>
              <div>
                <div className="text-2xl font-bold text-gray-900">{userStats.totalNFTs}</div>
                <div className="text-sm text-gray-600">NFTs Owned</div>
              </div>
            </div>
          </div>
          
          <div className="bg-white rounded-xl p-6 shadow-sm border border-gray-200">
            <div className="flex items-center space-x-3">
              <div className="w-12 h-12 bg-green-100 rounded-xl flex items-center justify-center">
                <Wallet className="w-6 h-6 text-green-600" />
              </div>
              <div>
                <div className="text-2xl font-bold text-gray-900">{balance} HBAR</div>
                <div className="text-sm text-gray-600">Balance</div>
              </div>
            </div>
          </div>
          
          <div className="bg-white rounded-xl p-6 shadow-sm border border-gray-200">
            <div className="flex items-center space-x-3">
              <div className="w-12 h-12 bg-purple-100 rounded-xl flex items-center justify-center">
                <TrendingUp className="w-6 h-6 text-purple-600" />
              </div>
              <div>
                <div className="text-2xl font-bold text-gray-900">{userStats.totalValue}</div>
                <div className="text-sm text-gray-600">Portfolio Value</div>
              </div>
            </div>
          </div>
          
          <div className="bg-white rounded-xl p-6 shadow-sm border border-gray-200">
            <div className="flex items-center space-x-3">
              <div className="w-12 h-12 bg-yellow-100 rounded-xl flex items-center justify-center">
                <Star className="w-6 h-6 text-yellow-600" />
              </div>
              <div>
                <div className="text-2xl font-bold text-gray-900">
                  {Object.keys(userStats.rarityBreakdown).length}
                </div>
                <div className="text-sm text-gray-600">Rarity Types</div>
              </div>
            </div>
          </div>
        </div>

        {/* Tabs */}
        <div className="bg-white rounded-2xl shadow-sm border border-gray-200 overflow-hidden">
          <div className="border-b border-gray-200">
            <nav className="flex space-x-8 px-6">
              <button
                onClick={() => setActiveTab('collection')}
                className={`py-4 px-1 border-b-2 font-medium text-sm transition-colors ${
                  activeTab === 'collection'
                    ? 'border-blue-500 text-blue-600'
                    : 'border-transparent text-gray-500 hover:text-gray-700 hover:border-gray-300'
                }`}
              >
                My Collection ({userStats.totalNFTs})
              </button>
              <button
                onClick={() => setActiveTab('activity')}
                className={`py-4 px-1 border-b-2 font-medium text-sm transition-colors ${
                  activeTab === 'activity'
                    ? 'border-blue-500 text-blue-600'
                    : 'border-transparent text-gray-500 hover:text-gray-700 hover:border-gray-300'
                }`}
              >
                Activity
              </button>
            </nav>
          </div>

          <div className="p-6">
            {activeTab === 'collection' && (
              <div>
                {loading ? (
                  <div className="flex justify-center py-12">
                    <div className="loading-spinner"></div>
                  </div>
                ) : userNFTs.length > 0 ? (
                  <div className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-3 gap-6">
                    {userNFTs.map((nft) => (
                      <div key={nft.token_id} className="nft-card rounded-xl p-4 hover:shadow-lg transition-all">
                        <div className="aspect-square bg-gradient-to-br from-gray-100 to-gray-200 rounded-lg mb-4 overflow-hidden">
                          <img 
                            src={nft.image_url} 
                            alt={nft.name}
                            className="w-full h-full object-cover"
                          />
                        </div>
                        <div className="space-y-2">
                          <div className="flex items-center justify-between">
                            <h3 className="font-semibold text-gray-900">{nft.name}</h3>
                            <span className={`px-2 py-1 rounded-full text-xs font-medium ${getRarityColor(nft.rarity)}`}>
                              {nft.rarity}
                            </span>
                          </div>
                          <p className="text-sm text-gray-600">{nft.description}</p>
                          <div className="flex items-center justify-between text-sm">
                            <span className="text-blue-600 font-medium">{nft.sport_type}</span>
                            <span className="text-gray-500">#{nft.token_id}</span>
                          </div>
                        </div>
                      </div>
                    ))}
                  </div>
                ) : (
                  <div className="text-center py-12">
                    <div className="w-16 h-16 bg-gray-100 rounded-full flex items-center justify-center mx-auto mb-4">
                      <Trophy className="w-8 h-8 text-gray-400" />
                    </div>
                    <h3 className="text-lg font-medium text-gray-900 mb-2">No NFTs Yet</h3>
                    <p className="text-gray-600 mb-6">Start building your sports NFT collection today!</p>
                    <button className="sport-button-primary text-white px-6 py-2 rounded-xl font-medium">
                      Browse Marketplace
                    </button>
                  </div>
                )}
              </div>
            )}

            {activeTab === 'activity' && (
              <div className="text-center py-12">
                <div className="w-16 h-16 bg-gray-100 rounded-full flex items-center justify-center mx-auto mb-4">
                  <TrendingUp className="w-8 h-8 text-gray-400" />
                </div>
                <h3 className="text-lg font-medium text-gray-900 mb-2">No Activity Yet</h3>
                <p className="text-gray-600">Your transaction history will appear here.</p>
              </div>
            )}
          </div>
        </div>
      </div>
    </div>
  );
};

export default Profile;