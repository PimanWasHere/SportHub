import React, { useState, useEffect } from 'react';
import { Search, Filter, Grid, List, Star, Trophy, Eye, Heart } from 'lucide-react';
import axios from 'axios';

const Gallery = () => {
  const [nfts, setNfts] = useState([]);
  const [loading, setLoading] = useState(true);
  const [searchTerm, setSearchTerm] = useState('');
  const [selectedSport, setSelectedSport] = useState('all');
  const [selectedRarity, setSelectedRarity] = useState('all');
  const [viewMode, setViewMode] = useState('grid');
  const [sortBy, setSortBy] = useState('newest');
  const [sportsCategories, setSportsCategories] = useState([]);

  useEffect(() => {
    fetchNFTs();
    fetchSportsCategories();
  }, []);

  const fetchNFTs = async () => {
    try {
      const response = await axios.get(`${process.env.REACT_APP_BACKEND_URL}/api/nfts`);
      setNfts(response.data);
    } catch (error) {
      console.error('Error fetching NFTs:', error);
    } finally {
      setLoading(false);
    }
  };

  const fetchSportsCategories = async () => {
    try {
      const response = await axios.get(`${process.env.REACT_APP_BACKEND_URL}/api/sports`);
      setSportsCategories(response.data.categories);
    } catch (error) {
      console.error('Error fetching sports categories:', error);
    }
  };

  const filteredNFTs = nfts.filter(nft => {
    const matchesSearch = nft.name.toLowerCase().includes(searchTerm.toLowerCase()) ||
                         nft.description.toLowerCase().includes(searchTerm.toLowerCase());
    const matchesSport = selectedSport === 'all' || nft.sport_type === selectedSport;
    const matchesRarity = selectedRarity === 'all' || nft.rarity === selectedRarity;
    
    return matchesSearch && matchesSport && matchesRarity;
  });

  const sortedNFTs = [...filteredNFTs].sort((a, b) => {
    switch (sortBy) {
      case 'name':
        return a.name.localeCompare(b.name);
      case 'rarity':
        const rarityOrder = { 'Common': 1, 'Rare': 2, 'Epic': 3, 'Legendary': 4, 'Mythic': 5 };
        return (rarityOrder[b.rarity] || 0) - (rarityOrder[a.rarity] || 0);
      default:
        return parseInt(b.token_id) - parseInt(a.token_id);
    }
  });

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

  const NFTCard = ({ nft }) => (
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
        <div className="absolute bottom-4 left-4 right-4 opacity-0 group-hover:opacity-100 transition-opacity">
          <div className="flex space-x-2">
            <button className="flex-1 bg-white/90 backdrop-blur-sm text-gray-900 py-2 px-4 rounded-xl font-medium hover:bg-white transition-colors">
              <Eye className="w-4 h-4 inline mr-2" />
              View
            </button>
            <button className="bg-white/90 backdrop-blur-sm text-gray-900 p-2 rounded-xl hover:bg-white transition-colors">
              <Heart className="w-4 h-4" />
            </button>
          </div>
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
        <p className="text-gray-600 text-sm mb-4 line-clamp-2">{nft.description}</p>
        <div className="flex items-center justify-between">
          <div className="flex items-center space-x-2">
            <div className="w-6 h-6 bg-gradient-to-r from-blue-500 to-purple-600 rounded-full flex items-center justify-center">
              <Trophy className="w-3 h-3 text-white" />
            </div>
            <span className="text-sm font-medium text-blue-600">{nft.sport_type}</span>
          </div>
          <span className="text-sm text-gray-500">#{nft.token_id}</span>
        </div>
      </div>
    </div>
  );

  return (
    <div className="min-h-screen bg-gray-50 py-8">
      <div className="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8">
        {/* Header */}
        <div className="mb-8">
          <h1 className="text-3xl lg:text-4xl font-bold text-gray-900 mb-2">NFT Gallery</h1>
          <p className="text-xl text-gray-600">Discover amazing sports collectibles</p>
        </div>

        {/* Sports Categories */}
        <div className="mb-8">
          <div className="flex overflow-x-auto pb-4 space-x-4">
            <button
              onClick={() => setSelectedSport('all')}
              className={`flex-shrink-0 px-6 py-3 rounded-xl font-medium transition-colors ${
                selectedSport === 'all'
                  ? 'bg-blue-600 text-white'
                  : 'bg-white text-gray-600 hover:bg-gray-50 border border-gray-200'
              }`}
            >
              All Sports
            </button>
            {sportsCategories.map((category) => (
              <button
                key={category.name}
                onClick={() => setSelectedSport(category.name)}
                className={`flex-shrink-0 px-6 py-3 rounded-xl font-medium transition-colors flex items-center space-x-2 ${
                  selectedSport === category.name
                    ? 'bg-blue-600 text-white'
                    : 'bg-white text-gray-600 hover:bg-gray-50 border border-gray-200'
                }`}
              >
                <span>{category.icon}</span>
                <span>{category.name}</span>
                <span className="bg-blue-100 text-blue-600 px-2 py-1 rounded-full text-xs">
                  {category.count}
                </span>
              </button>
            ))}
          </div>
        </div>

        {/* Filters and Search */}
        <div className="bg-white rounded-2xl p-6 shadow-sm border border-gray-200 mb-8">
          <div className="grid grid-cols-1 lg:grid-cols-4 gap-4">
            {/* Search */}
            <div className="relative">
              <Search className="absolute left-3 top-1/2 transform -translate-y-1/2 text-gray-400 w-5 h-5" />
              <input
                type="text"
                placeholder="Search NFTs..."
                value={searchTerm}
                onChange={(e) => setSearchTerm(e.target.value)}
                className="w-full pl-10 pr-4 py-3 border border-gray-200 rounded-xl focus:outline-none focus:ring-2 focus:ring-blue-500 focus:border-transparent"
              />
            </div>

            {/* Rarity Filter */}
            <select
              value={selectedRarity}
              onChange={(e) => setSelectedRarity(e.target.value)}
              className="px-4 py-3 border border-gray-200 rounded-xl focus:outline-none focus:ring-2 focus:ring-blue-500 focus:border-transparent"
            >
              <option value="all">All Rarities</option>
              <option value="Common">Common</option>
              <option value="Rare">Rare</option>
              <option value="Epic">Epic</option>
              <option value="Legendary">Legendary</option>
              <option value="Mythic">Mythic</option>
            </select>

            {/* Sort */}
            <select
              value={sortBy}
              onChange={(e) => setSortBy(e.target.value)}
              className="px-4 py-3 border border-gray-200 rounded-xl focus:outline-none focus:ring-2 focus:ring-blue-500 focus:border-transparent"
            >
              <option value="newest">Newest First</option>
              <option value="name">Name A-Z</option>
              <option value="rarity">Rarity</option>
            </select>

            {/* View Mode */}
            <div className="flex rounded-xl border border-gray-200 overflow-hidden">
              <button
                onClick={() => setViewMode('grid')}
                className={`flex-1 py-3 px-4 transition-colors ${
                  viewMode === 'grid'
                    ? 'bg-blue-600 text-white'
                    : 'bg-white text-gray-600 hover:bg-gray-50'
                }`}
              >
                <Grid className="w-5 h-5 mx-auto" />
              </button>
              <button
                onClick={() => setViewMode('list')}
                className={`flex-1 py-3 px-4 transition-colors ${
                  viewMode === 'list'
                    ? 'bg-blue-600 text-white'
                    : 'bg-white text-gray-600 hover:bg-gray-50'
                }`}
              >
                <List className="w-5 h-5 mx-auto" />
              </button>
            </div>
          </div>
        </div>

        {/* Results Count */}
        <div className="flex items-center justify-between mb-6">
          <div className="text-gray-600">
            Showing {sortedNFTs.length} of {nfts.length} NFTs
          </div>
        </div>

        {/* NFT Grid */}
        {loading ? (
          <div className="flex justify-center items-center py-20">
            <div className="loading-spinner"></div>
          </div>
        ) : sortedNFTs.length > 0 ? (
          <div className={`grid gap-6 ${
            viewMode === 'grid' 
              ? 'grid-cols-1 md:grid-cols-2 lg:grid-cols-3 xl:grid-cols-4' 
              : 'grid-cols-1'
          }`}>
            {sortedNFTs.map((nft) => (
              <NFTCard key={nft.token_id} nft={nft} />
            ))}
          </div>
        ) : (
          <div className="text-center py-20">
            <div className="w-16 h-16 bg-gray-100 rounded-full flex items-center justify-center mx-auto mb-4">
              <Search className="w-8 h-8 text-gray-400" />
            </div>
            <h3 className="text-xl font-semibold text-gray-900 mb-2">No NFTs Found</h3>
            <p className="text-gray-600">Try adjusting your search or filter criteria</p>
          </div>
        )}
      </div>
    </div>
  );
};

export default Gallery;