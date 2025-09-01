import React, { createContext, useContext, useState, useEffect } from 'react';
import { HashConnect } from '@hashgraph/hashconnect';
import toast from 'react-hot-toast';

const WalletContext = createContext();

export const useWallet = () => {
  const context = useContext(WalletContext);
  if (!context) {
    throw new Error('useWallet must be used within a WalletProvider');
  }
  return context;
};

export const WalletProvider = ({ children }) => {
  const [isConnected, setIsConnected] = useState(false);
  const [accountId, setAccountId] = useState('');
  const [balance, setBalance] = useState('0');
  const [hashConnect, setHashConnect] = useState(null);
  const [isLoading, setIsLoading] = useState(false);

  // Initialize HashConnect
  useEffect(() => {
    const initHashConnect = async () => {
      try {
        const hashConnectInstance = new HashConnect(true);
        setHashConnect(hashConnectInstance);
        
        // Check for existing connection
        const savedData = localStorage.getItem('hashconnect-data');
        if (savedData) {
          const data = JSON.parse(savedData);
          if (data.accountId) {
            setAccountId(data.accountId);
            setIsConnected(true);
            setBalance(data.balance || '0');
          }
        }
      } catch (error) {
        console.error('Failed to initialize HashConnect:', error);
      }
    };

    initHashConnect();
  }, []);

  const connectWallet = async () => {
    if (!hashConnect) {
      toast.error('HashConnect not initialized');
      return;
    }

    setIsLoading(true);
    try {
      // For demo purposes, we'll simulate a connection
      // In production, this would use actual HashConnect integration
      
      // Simulate connection delay
      await new Promise(resolve => setTimeout(resolve, 2000));
      
      // Mock connection data
      const mockAccountId = '0.0.123456';
      const mockBalance = '1,250.50';
      
      setAccountId(mockAccountId);
      setBalance(mockBalance);
      setIsConnected(true);
      
      // Save to localStorage
      localStorage.setItem('hashconnect-data', JSON.stringify({
        accountId: mockAccountId,
        balance: mockBalance,
        connected: true
      }));
      
      toast.success('HashPack wallet connected successfully!');
    } catch (error) {
      console.error('Wallet connection failed:', error);
      toast.error('Failed to connect wallet');
    } finally {
      setIsLoading(false);
    }
  };

  const disconnectWallet = async () => {
    try {
      setAccountId('');
      setBalance('0');
      setIsConnected(false);
      
      // Clear localStorage
      localStorage.removeItem('hashconnect-data');
      
      toast.success('Wallet disconnected');
    } catch (error) {
      console.error('Wallet disconnection failed:', error);
      toast.error('Failed to disconnect wallet');
    }
  };

  const value = {
    isConnected,
    accountId,
    balance,
    isLoading,
    connectWallet,
    disconnectWallet,
    hashConnect
  };

  return (
    <WalletContext.Provider value={value}>
      {children}
    </WalletContext.Provider>
  );
};