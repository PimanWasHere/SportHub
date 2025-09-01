import React, { useState, useEffect } from 'react';
import { BrowserRouter as Router, Routes, Route } from 'react-router-dom';
import { Toaster } from 'react-hot-toast';
import Navbar from './components/Navbar';
import Home from './pages/Home';
import Profile from './pages/Profile';
import Gallery from './pages/Gallery';
import Marketplace from './pages/Marketplace';
import { WalletProvider } from './context/WalletContext';
import './App.css';

function App() {
  return (
    <WalletProvider>
      <Router>
        <div className="App min-h-screen bg-gradient-to-br from-slate-50 to-blue-50">
          <Navbar />
          <main className="pt-16">
            <Routes>
              <Route path="/" element={<Home />} />
              <Route path="/profile" element={<Profile />} />
              <Route path="/gallery" element={<Gallery />} />
              <Route path="/marketplace" element={<Marketplace />} />
            </Routes>
          </main>
          <Toaster 
            position="top-right"
            toastOptions={{
              style: {
                background: '#1f2937',
                color: '#fff',
                borderRadius: '12px',
              },
            }}
          />
        </div>
      </Router>
    </WalletProvider>
  );
}

export default App;