import requests
import sys
import json
from datetime import datetime

class SportHubAPITester:
    def __init__(self, base_url="http://localhost:8001"):
        self.base_url = base_url
        self.tests_run = 0
        self.tests_passed = 0

    def run_test(self, name, method, endpoint, expected_status, data=None):
        """Run a single API test"""
        url = f"{self.base_url}/{endpoint}" if endpoint else self.base_url
        headers = {'Content-Type': 'application/json'}

        self.tests_run += 1
        print(f"\n🔍 Testing {name}...")
        print(f"   URL: {url}")
        
        try:
            if method == 'GET':
                response = requests.get(url, headers=headers, timeout=10)
            elif method == 'POST':
                response = requests.post(url, json=data, headers=headers, timeout=10)

            success = response.status_code == expected_status
            if success:
                self.tests_passed += 1
                print(f"✅ Passed - Status: {response.status_code}")
                try:
                    response_data = response.json()
                    print(f"   Response: {json.dumps(response_data, indent=2)[:200]}...")
                except:
                    print(f"   Response: {response.text[:200]}...")
            else:
                print(f"❌ Failed - Expected {expected_status}, got {response.status_code}")
                print(f"   Response: {response.text[:200]}...")

            return success, response.json() if success and response.text else {}

        except requests.exceptions.ConnectionError:
            print(f"❌ Failed - Connection Error: Cannot connect to {url}")
            return False, {}
        except requests.exceptions.Timeout:
            print(f"❌ Failed - Timeout: Request timed out")
            return False, {}
        except Exception as e:
            print(f"❌ Failed - Error: {str(e)}")
            return False, {}

    def test_root_endpoint(self):
        """Test root endpoint"""
        return self.run_test("Root Endpoint", "GET", "", 200)

    def test_health_check(self):
        """Test health check endpoint"""
        return self.run_test("Health Check", "GET", "api/health", 200)

    def test_get_all_nfts(self):
        """Test getting all NFTs"""
        success, response = self.run_test("Get All NFTs", "GET", "api/nfts", 200)
        if success and isinstance(response, list):
            print(f"   Found {len(response)} NFTs")
            for nft in response[:2]:  # Show first 2 NFTs
                print(f"   - {nft.get('name', 'Unknown')} ({nft.get('sport_type', 'Unknown')} - {nft.get('rarity', 'Unknown')})")
        return success, response

    def test_get_nft_by_id(self, token_id="1"):
        """Test getting specific NFT by ID"""
        return self.run_test(f"Get NFT by ID ({token_id})", "GET", f"api/nfts/{token_id}", 200)

    def test_get_wallet_nfts(self, account_id="0.0.123456"):
        """Test getting NFTs for a specific wallet"""
        return self.run_test(f"Get Wallet NFTs ({account_id})", "GET", f"api/nfts/wallet/{account_id}", 200)

    def test_get_wallet_info(self, account_id="0.0.123456"):
        """Test getting wallet information"""
        return self.run_test(f"Get Wallet Info ({account_id})", "GET", f"api/wallet/{account_id}", 200)

    def test_get_sports_categories(self):
        """Test getting sports categories"""
        success, response = self.run_test("Get Sports Categories", "GET", "api/sports", 200)
        if success and 'categories' in response:
            print(f"   Found {len(response['categories'])} sports categories")
            for category in response['categories'][:3]:  # Show first 3 categories
                print(f"   - {category.get('name', 'Unknown')} {category.get('icon', '')} ({category.get('count', 0)} NFTs)")
        return success, response

    def test_mint_nft(self):
        """Test NFT minting endpoint"""
        test_nft = {
            "token_id": "test_001",
            "name": "Test Sports NFT",
            "description": "A test NFT for Sport Hub",
            "image_url": "https://via.placeholder.com/300x300/FF0000/FFFFFF?text=Test",
            "sport_type": "Football",
            "rarity": "Common",
            "owner": "0.0.999999"
        }
        return self.run_test("Mint NFT", "POST", "api/nfts/mint", 200, test_nft)

    def test_transfer_nft(self):
        """Test NFT transfer endpoint"""
        transfer_data = {
            "from_account": "0.0.123456",
            "to_account": "0.0.654321",
            "token_id": "1"
        }
        return self.run_test("Transfer NFT", "POST", "api/nfts/transfer", 200, transfer_data)

def main():
    print("🚀 Starting Sport Hub API Tests...")
    print("=" * 50)
    
    # Setup
    tester = SportHubAPITester("http://localhost:8001")
    
    # Run all tests
    print("\n📡 Testing Basic Endpoints...")
    tester.test_root_endpoint()
    tester.test_health_check()
    
    print("\n🎨 Testing NFT Endpoints...")
    success, nfts = tester.test_get_all_nfts()
    tester.test_get_nft_by_id("1")
    tester.test_get_nft_by_id("999")  # Test non-existent NFT
    tester.test_get_wallet_nfts("0.0.123456")
    tester.test_get_wallet_nfts("0.0.999999")  # Test wallet with no NFTs
    
    print("\n💰 Testing Wallet Endpoints...")
    tester.test_get_wallet_info("0.0.123456")
    
    print("\n🏆 Testing Sports Categories...")
    tester.test_get_sports_categories()
    
    print("\n🔨 Testing POST Endpoints...")
    tester.test_mint_nft()
    tester.test_transfer_nft()

    # Print final results
    print("\n" + "=" * 50)
    print(f"📊 Final Results: {tester.tests_passed}/{tester.tests_run} tests passed")
    
    if tester.tests_passed == tester.tests_run:
        print("🎉 All tests passed! Backend API is working correctly.")
        return 0
    else:
        print(f"⚠️  {tester.tests_run - tester.tests_passed} tests failed. Check the issues above.")
        return 1

if __name__ == "__main__":
    sys.exit(main())