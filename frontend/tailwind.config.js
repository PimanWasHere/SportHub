/** @type {import('tailwindcss').Config} */
module.exports = {
  content: [
    "./src/**/*.{js,jsx,ts,tsx}",
  ],
  theme: {
    extend: {
      colors: {
        'sport-primary': '#FF6B35',
        'sport-secondary': '#004E89', 
        'sport-accent': '#FFD700',
        'sport-dark': '#1A1A2E',
        'sport-light': '#F8F9FA',
      },
      fontFamily: {
        'sport': ['Inter', 'sans-serif'],
      },
      animation: {
        'bounce-slow': 'bounce 2s infinite',
        'pulse-slow': 'pulse 3s infinite',
      }
    },
  },
  plugins: [],
}