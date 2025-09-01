import React, { useState, useEffect } from 'react';
import { TrendingUp, DollarSign, Clock, Zap, Star, Trophy, ArrowUpRight } from 'lucide-react';
import { useWallet } from '../context/WalletContext';
import axios from 'axios';
import toast from 'react-hot-toast';

const Marketplace = () => {
  const { isConnected, accountId } = useWallet();
  const [nfts, setNfts] = useState([]);
  const [loading, setLoading] = useState(true);
  const [activeTab, setActiveTab] = useState('all');
  const [marketStats, setMarketStats] = useState({
    totalVolume: 0,
    floorPrice: 0,
    avgPrice: 0,
    totalListings: 0
  });

  useEffect(() => {
    fetchMarketplaceData();
  }, []);

  const fetchMarketplaceData = async () => {
    try {
      const response = await axios.get(`${process.env.REACT_APP_BACKEND_URL}/api/nfts`);
      setNfts(response.data);
      
      // Calculate market stats
      const stats = calculateMarketStats(response.data);
      setMarketStats(stats);
    } catch (error) {
      console.error('Error fetching marketplace data:', error);
    } finally {
      setLoading(false);
    }
  };

  const calculateMarketStats = (nfts) => {
    // Mock price data - in production, this would come from actual market data
    const mockPrices = nfts.map(() => Math.floor(Math.random() * 100) + 10);
    
    return {
      totalVolume: mockPrices.reduce((sum, price) => sum + price, 0),
      floorPrice: Math.min(...mockPrices),
      avgPrice: Math.floor(mockPrices.reduce((sum, price) => sum + price, 0) / mockPrices.length),
      totalListings: nfts.length
    };
  };

  const handleBuyNFT = async (nft) => {
    if (!isConnected) {
      toast.error('Please connect your wallet first');
      return;
    }

    try {
      // Mock purchase - in production, this would interact with smart contracts
      toast.loading('Processing purchase...', { id: 'purchase' });
      
      // Simulate transaction delay
      await new Promise(resolve => setTimeout(resolve, 2000));
      
      toast.success(`Successfully purchased ${nft.name}!`, { id: 'purchase' });
    } catch (error) {
      console.error('Purchase failed:', error);
      toast.error('Purchase failed. Please try again.', { id: 'purchase' });
    }
  };

  const getRarityColor = (rarity) => {
    const colors = {
      'Common': 'text-gray-600 bg-gray-100 border-gray-200',
      'Rare': 'text-blue-600 bg-blue-100 border-blue-200',
      'Epic': 'text-purple-600 bg-purple-100 border-purple-200',
      'Legendary': 'text-yellow-600 bg-yellow-100 border-yellow-200',
      'Mythic': 'text-red-600 bg-red-100 border-red-200'
    };
    return colors[rarity] || 'text-gray-600 bg-gray-100 border-gray-200';
  };

  const MarketplaceCard = ({ nft }) => {
    const mockPrice = Math.floor(Math.random() * 100) + 10;
    const mockLastSale = Math.floor(Math.random() * 80) + 5;
    
    return (
      <div className="nft-card rounded-2xl overflow-hidden hover:shadow-2xl transition-all duration-300 group">
        <div className="relative">
          <div className="aspect-square bg-gradient-to-br from-gray-100 to-gray-200 overflow-hidden">
            <img 
              src={nft.image_url} 
              alt={nft.name}
              className="w-full h-full object-cover group-hover:scale-105 transition-transform duration-300"
            />
          </div>
          <div className="absolute top-4 right-4">
            <span className={`px-3 py-1 rounded-full text-xs font-semibold border ${getRarityColor(nft.rarity)}`}>
              {nft.rarity}
            </span>
          </div>
          <div className="absolute top-4 left-4">
            <span className="bg-green-500 text-white px-2 py-1 rounded-full text-xs font-semibold">
              FOR SALE
            </span>
          </div>
        </div>
        <div className="p-6">
          <div className="flex items-center justify-between mb-2">
            <h3 className="text-lg font-bold text-gray-900 truncate">{nft.name}</h3>
            <div className="flex items-center space-x-1">
              <Star className="w-4 h-4 text-yellow-400 fill-current" />
              <span className="text-sm text-gray-600">4.8</span>
            </div>
          </div>
          
          <div className="flex items-center space-x-2 mb-4">
            <div className="w-6 h-6 bg-gradient-to-r from-blue-500 to-purple-600 rounded-full flex items-center justify-center">
              <Trophy className="w-3 h-3 text-white" />
            </div>
            <span className="text-sm font-medium text-blue-600">{nft.sport_type}</span>
            <span className="text-sm text-gray-500">#{nft.token_id}</span>
          </div>

          <div className="space-y-3">
            <div className="flex items-center justify-between">
              <span className="text-sm text-gray-600">Current Price</span>
              <div className="text-right">
                <div className="text-lg font-bold text-gray-900">{mockPrice} HBAR</div>
                <div className="text-xs text-gray-500">${(mockPrice * 0.05).toFixed(2)} USD</div>
              </div>
            </div>
            
            <div className="flex items-center justify-between text-sm">
              <span className="text-gray-600">Last Sale</span>
              <div className="flex items-center space-x-1">
                <span className="text-gray-900 font-medium">{mockLastSale} HBAR</span>
                {mockPrice > mockLastSale ? (
                  <ArrowUpRight className="w-3 h-3 text-green-500" />
                ) : (
                  <ArrowUpRight className="w-3 h-3 text-red-500 transform rotate-90" />
                )}
              </div>
            </div>
          </div>

          <button
            onClick={() => handleBuyNFT(nft)}
            disabled={!isConnected}
            className="w-full mt-4 sport-button-primary text-white py-3 rounded-xl font-semibold disabled:opacity-50 disabled:cursor-not-allowed hover:shadow-lg transition-all"
          >
            {isConnected ? 'Buy Now' : 'Connect Wallet'}
          </button>
        </div>
      </div>
    );
  };

  const filteredNFTs = nfts.filter(nft => {
    if (activeTab === 'all') return true;
    if (activeTab === 'trending') return Math.random() > 0.7; // Mock trending logic
    if (activeTab === 'new') return parseInt(nft.token_id) > 2; // Mock new items
    return true;
  });

  return (
    <div className="min-h-screen bg-gray-50 py-8">
      <div className="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8">
        {/* Header */}
        <div className="mb-8">
          <h1 className="text-3xl lg:text-4xl font-bold text-gray-900 mb-2">Marketplace</h1>
          <p className="text-xl text-gray-600">Buy and sell sports NFTs</p>
        </div>

        {/* Market Stats */}
        <div className="grid grid-cols-2 lg:grid-cols-4 gap-6 mb-8">
          <div className="bg-white rounded-xl p-6 shadow-sm border border-gray-200">
            <div className="flex items-center space-x-3">
              <div className="w-12 h-12 bg-blue-100 rounded-xl flex items-center justify-center">
                <TrendingUp className="w-6 h-6 text-blue-600" />
              </div>
              <div>
                <div className="text-2xl font-bold text-gray-900">{marketStats.totalVolume.toLocaleString()}</div>
                <div className="text-sm text-gray-600">Total Volume</div>
              </div>
            </div>
          </div>
          
          <div className="bg-white rounded-xl p-6 shadow-sm border border-gray-200">
            <div className="flex items-center space-x-3">
              <div className="w-12 h-12 bg-green-100 rounded-xl flex items-center justify-center">
                <DollarSign className="w-6 h-6 text-green-600" />
              </div>
              <div>
                <div className="text-2xl font-bold text-gray-900">{marketStats.floorPrice} HBAR</div>
                <div className="text-sm text-gray-600">Floor Price</div>
              </div>
            </div>
          </div>
          
          <div className="bg-white rounded-xl p-6 shadow-sm border border-gray-200">
            <div className="flex items-center space-x-3">
              <div className="w-12 h-12 bg-purple-100 rounded-xl flex items-center justify-center">
                <Zap className="w-6 h-6 text-purple-600" />
              </div>
              <div>
                <div className="text-2xl font-bold text-gray-900">{marketStats.avgPrice} HBAR</div>
                <div className="text-sm text-gray-600">Avg Price</div>
              </div>
            </div>
          </div>
          
          <div className="bg-white rounded-xl p-6 shadow-sm border border-gray-200">
            <div className="flex items-center space-x-3">
              <div className="w-12 h-12 bg-yellow-100 rounded-xl flex items-center justify-center">
                <Clock className="w-6 h-6 text-yellow-600" />
              </div>
              <div>
                <div className="text-2xl font-bold text-gray-900">{marketStats.totalListings}</div>
                <div className="text-sm text-gray-600">Active Listings</div>
              </div>
            </div>
          </div>
        </div>

        {/* Tabs */}
        <div className="bg-white rounded-2xl shadow-sm border border-gray-200 overflow-hidden mb-8">
          <div className="border-b border-gray-200">
            <nav className="flex space-x-8 px-6">
              {[
                { key: 'all', label: 'All Items', count: nfts.length },
                { key: 'trending', label: 'Trending', count: Math.floor(nfts.length * 0.3) },
                { key: 'new', label: 'New', count: Math.floor(nfts.length * 0.4) }
              ].map((tab) => (
                <button
                  key={tab.key}
                  onClick={() => setActiveTab(tab.key)}
                  className={`py-4 px-1 border-b-2 font-medium text-sm transition-colors ${
                    activeTab === tab.key
                      ? 'border-blue-500 text-blue-600'
                      : 'border-transparent text-gray-500 hover:text-gray-700 hover:border-gray-300'
                  }`}
                >
                  {tab.label} ({tab.count})
                </button>
              ))}
            </nav>
          </div>
        </div>

        {/* NFT Grid */}
        {loading ? (
          <div className="flex justify-center items-center py-20">
            <div className="loading-spinner"></div>
          </div>
        ) : filteredNFTs.length > 0 ? (
          <div className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-3 xl:grid-cols-4 gap-6">
            {filteredNFTs.map((nft) => (
              <MarketplaceCard key={nft.token_id} nft={nft} />
            ))}
          </div>
        ) : (
          <div className="text-center py-20">
            <div className="w-16 h-16 bg-gray-100 rounded-full flex items-center justify-center mx-auto mb-4">
              <TrendingUp className="w-8 h-8 text-gray-400" />
            </div>
            <h3 className="text-xl font-semibold text-gray-900 mb-2">No Items Available</h3>
            <p className="text-gray-600">Check back later for new listings</p>
          </div>
        )}

        {/* Bottom CTA */}
        <div className="mt-16 bg-gradient-to-r from-blue-600 to-purple-600 rounded-2xl p-8 text-center text-white">
          <h2 className="text-2xl lg:text-3xl font-bold mb-4">Ready to Start Trading?</h2>
          <p className="text-lg text-blue-100 mb-6">
            Connect your HashPack wallet and start buying sports NFTs today
          </p>
          {!isConnected && (
            <button className="bg-white text-blue-600 px-8 py-3 rounded-xl font-semibold hover:bg-gray-100 transition-colors">
              Connect HashPack Wallet
            </button>
          )}
        </div>
      </div>
    </div>
  );
};

export default Marketplace;