import React, { useState, useEffect } from 'react';
import { Link } from 'react-router-dom';
import { Trophy, Users, Zap, Shield, ArrowRight, Star } from 'lucide-react';
import { useWallet } from '../context/WalletContext';
import axios from 'axios';

const Home = () => {
  const { isConnected } = useWallet();
  const [stats, setStats] = useState({
    totalNFTs: 0,
    activeUsers: 0,
    totalVolume: 0
  });
  const [featuredNFTs, setFeaturedNFTs] = useState([]);

  useEffect(() => {
    fetchHomeData();
  }, []);

  const fetchHomeData = async () => {
    try {
      const nftsResponse = await axios.get(`${process.env.REACT_APP_BACKEND_URL}/api/nfts`);
      setFeaturedNFTs(nftsResponse.data.slice(0, 3));
      
      // Mock stats for demo
      setStats({
        totalNFTs: nftsResponse.data.length,
        activeUsers: 1247,
        totalVolume: 892.5
      });
    } catch (error) {
      console.error('Error fetching home data:', error);
    }
  };

  const features = [
    {
      icon: Trophy,
      title: 'Sports NFT Collectibles',
      description: 'Discover and collect unique sports memorabilia as NFTs on the Hedera network.'
    },
    {
      icon: Shield,
      title: 'Secure & Transparent',
      description: 'Built on Hedera\'s secure and fast blockchain technology for ultimate transparency.'
    },
    {
      icon: Users,
      title: 'Community Driven',
      description: 'Join a thriving community of sports fans and collectors from around the world.'
    },
    {
      icon: Zap,
      title: 'Lightning Fast',
      description: 'Experience instant transactions and low fees with Hedera\'s efficient consensus.'
    }
  ];

  return (
    <div className="min-h-screen">
      {/* Hero Section */}
      <section className="hero-gradient text-white py-20 lg:py-32">
        <div className="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8">
          <div className="text-center">
            <h1 className="text-4xl lg:text-6xl font-bold mb-6 leading-tight">
              The Future of
              <span className="block bg-gradient-to-r from-yellow-400 to-orange-400 bg-clip-text text-transparent">
                Sports Collectibles
              </span>
            </h1>
            <p className="text-xl lg:text-2xl text-blue-100 mb-8 max-w-3xl mx-auto">
              Discover, collect, and trade authentic sports NFTs on the Hedera network. 
              Connect with HashPack and join the premier Web3 sports community.
            </p>
            <div className="flex flex-col sm:flex-row gap-4 justify-center">
              <Link
                to="/gallery"
                className="sport-button-primary text-white px-8 py-4 rounded-xl font-semibold text-lg flex items-center justify-center space-x-2 hover:shadow-xl transition-all"
              >
                <span>Explore Gallery</span>
                <ArrowRight className="w-5 h-5" />
              </Link>
              <Link
                to="/marketplace"
                className="bg-white/20 backdrop-blur-sm text-white px-8 py-4 rounded-xl font-semibold text-lg border border-white/30 hover:bg-white/30 transition-all"
              >
                View Marketplace
              </Link>
            </div>
          </div>
        </div>
      </section>

      {/* Stats Section */}
      <section className="py-16 bg-white">
        <div className="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8">
          <div className="grid grid-cols-1 md:grid-cols-3 gap-8">
            <div className="text-center">
              <div className="text-4xl font-bold text-blue-600 mb-2">{stats.totalNFTs}+</div>
              <div className="text-gray-600">Unique NFTs</div>
            </div>
            <div className="text-center">
              <div className="text-4xl font-bold text-blue-600 mb-2">{stats.activeUsers.toLocaleString()}+</div>
              <div className="text-gray-600">Active Users</div>
            </div>
            <div className="text-center">
              <div className="text-4xl font-bold text-blue-600 mb-2">{stats.totalVolume}k HBAR</div>
              <div className="text-gray-600">Total Volume</div>
            </div>
          </div>
        </div>
      </section>

      {/* Featured NFTs */}
      <section className="py-16 bg-gray-50">
        <div className="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8">
          <div className="text-center mb-12">
            <h2 className="text-3xl lg:text-4xl font-bold text-gray-900 mb-4">
              Featured Collections
            </h2>
            <p className="text-xl text-gray-600">Discover the most popular sports NFTs</p>
          </div>
          
          <div className="grid grid-cols-1 md:grid-cols-3 gap-8">
            {featuredNFTs.map((nft, index) => (
              <div key={index} className="nft-card rounded-2xl p-6 hover:shadow-2xl transition-all">
                <div className="aspect-square bg-gradient-to-br from-blue-100 to-purple-100 rounded-xl mb-4 flex items-center justify-center overflow-hidden">
                  <img 
                    src={nft.image_url} 
                    alt={nft.name}
                    className="w-full h-full object-cover"
                  />
                </div>
                <div className="flex items-center justify-between mb-2">
                  <h3 className="text-lg font-semibold text-gray-900">{nft.name}</h3>
                  <div className="flex items-center space-x-1">
                    <Star className="w-4 h-4 text-yellow-400 fill-current" />
                    <span className="text-sm text-gray-600">{nft.rarity}</span>
                  </div>
                </div>
                <p className="text-gray-600 text-sm mb-4">{nft.description}</p>
                <div className="flex items-center justify-between">
                  <span className="text-sm text-blue-600 font-medium">{nft.sport_type}</span>
                  <span className="text-sm text-gray-500">#{nft.token_id}</span>
                </div>
              </div>
            ))}
          </div>
          
          <div className="text-center mt-12">
            <Link
              to="/gallery"
              className="sport-button-primary text-white px-8 py-3 rounded-xl font-semibold inline-flex items-center space-x-2"
            >
              <span>View All NFTs</span>
              <ArrowRight className="w-5 h-5" />
            </Link>
          </div>
        </div>
      </section>

      {/* Features Section */}
      <section className="py-16 bg-white">
        <div className="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8">
          <div className="text-center mb-12">
            <h2 className="text-3xl lg:text-4xl font-bold text-gray-900 mb-4">
              Why Choose Sport Hub?
            </h2>
            <p className="text-xl text-gray-600">Built for the future of sports collectibles</p>
          </div>
          
          <div className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-4 gap-8">
            {features.map((feature, index) => {
              const Icon = feature.icon;
              return (
                <div key={index} className="text-center p-6 rounded-2xl hover:bg-gray-50 transition-colors">
                  <div className="w-16 h-16 bg-gradient-to-r from-blue-500 to-purple-600 rounded-2xl flex items-center justify-center mx-auto mb-4">
                    <Icon className="w-8 h-8 text-white" />
                  </div>
                  <h3 className="text-xl font-semibold text-gray-900 mb-2">{feature.title}</h3>
                  <p className="text-gray-600">{feature.description}</p>
                </div>
              );
            })}
          </div>
        </div>
      </section>

      {/* CTA Section */}
      <section className="py-16 bg-gradient-to-r from-blue-600 to-purple-600 text-white">
        <div className="max-w-4xl mx-auto text-center px-4 sm:px-6 lg:px-8">
          <h2 className="text-3xl lg:text-4xl font-bold mb-4">
            Ready to Start Collecting?
          </h2>
          <p className="text-xl text-blue-100 mb-8">
            {isConnected 
              ? "You're all set! Start exploring our NFT collections."
              : "Connect your HashPack wallet and dive into the world of sports NFTs."
            }
          </p>
          <div className="flex flex-col sm:flex-row gap-4 justify-center">
            <Link
              to="/gallery"
              className="bg-white text-blue-600 px-8 py-3 rounded-xl font-semibold hover:bg-gray-100 transition-colors"
            >
              Browse Collection
            </Link>
            <Link
              to="/profile"
              className="border-2 border-white text-white px-8 py-3 rounded-xl font-semibold hover:bg-white hover:text-blue-600 transition-colors"
            >
              View Profile
            </Link>
          </div>
        </div>
      </section>
    </div>
  );
};

export default Home;