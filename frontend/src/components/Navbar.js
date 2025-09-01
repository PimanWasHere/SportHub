import React, { useState } from 'react';
import { Link, useLocation } from 'react-router-dom';
import { Menu, X, Wallet, User, Trophy, Store } from 'lucide-react';
import { useWallet } from '../context/WalletContext';

const Navbar = () => {
  const [isOpen, setIsOpen] = useState(false);
  const location = useLocation();
  const { isConnected, accountId, connectWallet, disconnectWallet, isLoading } = useWallet();

  const navigation = [
    { name: 'Home', href: '/', icon: Trophy },
    { name: 'Gallery', href: '/gallery', icon: Store },
    { name: 'Marketplace', href: '/marketplace', icon: Store },
    { name: 'Profile', href: '/profile', icon: User },
  ];

  const isActive = (path) => location.pathname === path;

  return (
    <nav className="fixed top-0 w-full bg-white/90 backdrop-blur-md border-b border-gray-200 z-50">
      <div className="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8">
        <div className="flex justify-between items-center h-16">
          {/* Logo */}
          <div className="flex items-center">
            <Link to="/" className="flex items-center space-x-2">
              <div className="w-10 h-10 bg-gradient-to-r from-blue-600 to-purple-600 rounded-xl flex items-center justify-center">
                <Trophy className="w-6 h-6 text-white" />
              </div>
              <span className="text-2xl font-bold bg-gradient-to-r from-blue-600 to-purple-600 bg-clip-text text-transparent">
                Sport Hub
              </span>
            </Link>
          </div>

          {/* Desktop Navigation */}
          <div className="hidden md:block">
            <div className="ml-10 flex items-baseline space-x-4">
              {navigation.map((item) => {
                const Icon = item.icon;
                return (
                  <Link
                    key={item.name}
                    to={item.href}
                    className={`px-4 py-2 rounded-xl text-sm font-medium transition-all duration-200 flex items-center space-x-2 ${
                      isActive(item.href)
                        ? 'bg-blue-50 text-blue-700 border border-blue-200'
                        : 'text-gray-600 hover:text-gray-900 hover:bg-gray-50'
                    }`}
                  >
                    <Icon className="w-4 h-4" />
                    <span>{item.name}</span>
                  </Link>
                );
              })}
            </div>
          </div>

          {/* Wallet Connection */}
          <div className="hidden md:block">
            {isConnected ? (
              <div className="flex items-center space-x-4">
                <div className="text-sm text-gray-600">
                  <span className="font-medium">{accountId}</span>
                </div>
                <button
                  onClick={disconnectWallet}
                  className="bg-red-50 text-red-700 px-4 py-2 rounded-xl hover:bg-red-100 transition-colors border border-red-200"
                >
                  Disconnect
                </button>
              </div>
            ) : (
              <button
                onClick={connectWallet}
                disabled={isLoading}
                className="sport-button-primary text-white px-6 py-2 rounded-xl font-medium flex items-center space-x-2 disabled:opacity-50"
              >
                {isLoading ? (
                  <div className="loading-spinner"></div>
                ) : (
                  <Wallet className="w-4 h-4" />
                )}
                <span>{isLoading ? 'Connecting...' : 'Connect HashPack'}</span>
              </button>
            )}
          </div>

          {/* Mobile menu button */}
          <div className="md:hidden">
            <button
              onClick={() => setIsOpen(!isOpen)}
              className="inline-flex items-center justify-center p-2 rounded-xl text-gray-600 hover:text-gray-900 hover:bg-gray-100"
            >
              {isOpen ? <X className="w-6 h-6" /> : <Menu className="w-6 h-6" />}
            </button>
          </div>
        </div>
      </div>

      {/* Mobile menu */}
      {isOpen && (
        <div className="md:hidden">
          <div className="px-2 pt-2 pb-3 space-y-1 sm:px-3 bg-white border-t border-gray-200">
            {navigation.map((item) => {
              const Icon = item.icon;
              return (
                <Link
                  key={item.name}
                  to={item.href}
                  className={`block px-3 py-2 rounded-xl text-base font-medium transition-colors flex items-center space-x-2 ${
                    isActive(item.href)
                      ? 'bg-blue-50 text-blue-700'
                      : 'text-gray-600 hover:text-gray-900 hover:bg-gray-50'
                  }`}
                  onClick={() => setIsOpen(false)}
                >
                  <Icon className="w-5 h-5" />
                  <span>{item.name}</span>
                </Link>
              );
            })}
            
            {/* Mobile wallet connection */}
            <div className="mt-4 pt-4 border-t border-gray-200">
              {isConnected ? (
                <div className="space-y-2">
                  <div className="px-3 py-2 text-sm text-gray-600">
                    Connected: {accountId}
                  </div>
                  <button
                    onClick={disconnectWallet}
                    className="w-full text-left px-3 py-2 text-red-700 hover:bg-red-50 rounded-xl"
                  >
                    Disconnect Wallet
                  </button>
                </div>
              ) : (
                <button
                  onClick={connectWallet}
                  disabled={isLoading}
                  className="w-full sport-button-primary text-white px-3 py-2 rounded-xl font-medium flex items-center justify-center space-x-2 disabled:opacity-50"
                >
                  {isLoading ? (
                    <div className="loading-spinner"></div>
                  ) : (
                    <Wallet className="w-4 h-4" />
                  )}
                  <span>{isLoading ? 'Connecting...' : 'Connect HashPack'}</span>
                </button>
              )}
            </div>
          </div>
        </div>
      )}
    </nav>
  );
};

export default Navbar;