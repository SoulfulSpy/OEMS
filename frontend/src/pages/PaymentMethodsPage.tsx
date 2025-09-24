import React, { useState } from 'react';
import { motion } from 'framer-motion';
import { ArrowLeft, CreditCard, Plus, Wallet, Smartphone } from 'lucide-react';
import { useNavigate } from 'react-router-dom';
import Button from '../components/Button';
import Input from '../components/Input';

interface PaymentMethod {
  id: string;
  type: 'card' | 'wallet' | 'digital';
  name: string;
  details: string;
  icon: React.ReactNode;
  isDefault: boolean;
}

const PaymentMethodsPage: React.FC = () => {
  const navigate = useNavigate();
  const [showAddCard, setShowAddCard] = useState(false);
  const [paymentMethods, setPaymentMethods] = useState<PaymentMethod[]>([
    {
      id: '1',
      type: 'card',
      name: 'Visa',
      details: '**** **** **** 1234',
      icon: <CreditCard className="w-6 h-6" />,
      isDefault: true
    },
    {
      id: '2',
      type: 'wallet',
      name: 'OEMS Wallet',
      details: '₹250.00',
      icon: <Wallet className="w-6 h-6" />,
      isDefault: false
    },
    {
      id: '3',
      type: 'digital',
      name: 'Google Pay',
      details: 'john.doe@gmail.com',
      icon: <Smartphone className="w-6 h-6" />,
      isDefault: false
    }
  ]);

  const [newCard, setNewCard] = useState({
    cardNumber: '',
    expiryDate: '',
    cvv: '',
    cardholderName: ''
  });

  const handleAddCard = () => {
    // Mock add card functionality
    const newMethod: PaymentMethod = {
      id: Date.now().toString(),
      type: 'card',
      name: 'Visa',
      details: `**** **** **** ${newCard.cardNumber.slice(-4)}`,
      icon: <CreditCard className="w-6 h-6" />,
      isDefault: false
    };
    
    setPaymentMethods([...paymentMethods, newMethod]);
    setShowAddCard(false);
    setNewCard({ cardNumber: '', expiryDate: '', cvv: '', cardholderName: '' });
  };

  const handleSetDefault = (id: string) => {
    setPaymentMethods(methods =>
      methods.map(method => ({
        ...method,
        isDefault: method.id === id
      }))
    );
  };

  if (showAddCard) {
    return (
      <div className="min-h-screen bg-gray-50">
        {/* Header */}
        <div className="glass-panel mx-4 mt-12 mb-6 p-4">
          <div className="flex items-center gap-4">
            <button
              onClick={() => setShowAddCard(false)}
              className="p-2 rounded-full hover:bg-white/20"
            >
              <ArrowLeft className="w-6 h-6" />
            </button>
            <h1 className="text-lg font-semibold">Add Payment Method</h1>
          </div>
        </div>

        <div className="px-4 space-y-6">
          {/* Card Form */}
          <motion.div
            className="glass-panel p-6"
            initial={{ opacity: 0, y: 20 }}
            animate={{ opacity: 1, y: 0 }}
          >
            <h3 className="text-lg font-semibold mb-6">Card Details</h3>
            
            <div className="space-y-4">
              <div>
                <label className="block text-sm font-medium text-gray-700 mb-2">
                  Cardholder Name
                </label>
                <Input
                  type="text"
                  placeholder="John Doe"
                  value={newCard.cardholderName}
                  onChange={(e) => setNewCard({ ...newCard, cardholderName: e.target.value })}
                />
              </div>
              
              <div>
                <label className="block text-sm font-medium text-gray-700 mb-2">
                  Card Number
                </label>
                <Input
                  type="text"
                  placeholder="1234 5678 9012 3456"
                  value={newCard.cardNumber}
                  onChange={(e) => setNewCard({ ...newCard, cardNumber: e.target.value })}
                  maxLength={19}
                />
              </div>
              
              <div className="grid grid-cols-2 gap-4">
                <div>
                  <label className="block text-sm font-medium text-gray-700 mb-2">
                    Expiry Date
                  </label>
                  <Input
                    type="text"
                    placeholder="MM/YY"
                    value={newCard.expiryDate}
                    onChange={(e) => setNewCard({ ...newCard, expiryDate: e.target.value })}
                    maxLength={5}
                  />
                </div>
                
                <div>
                  <label className="block text-sm font-medium text-gray-700 mb-2">
                    CVV
                  </label>
                  <Input
                    type="text"
                    placeholder="123"
                    value={newCard.cvv}
                    onChange={(e) => setNewCard({ ...newCard, cvv: e.target.value })}
                    maxLength={3}
                  />
                </div>
              </div>
            </div>
            
            <div className="flex gap-3 mt-6">
              <Button
                onClick={handleAddCard}
                className="flex-1"
                disabled={!newCard.cardNumber || !newCard.expiryDate || !newCard.cvv || !newCard.cardholderName}
              >
                Add Card
              </Button>
              <Button
                onClick={() => setShowAddCard(false)}
                variant="secondary"
                className="flex-1"
              >
                Cancel
              </Button>
            </div>
          </motion.div>

          {/* Security Notice */}
          <motion.div
            className="glass-panel p-6"
            initial={{ opacity: 0, y: 20 }}
            animate={{ opacity: 1, y: 0 }}
            transition={{ delay: 0.1 }}
          >
            <h4 className="font-semibold mb-2">🔒 Secure Payment</h4>
            <p className="text-sm text-gray-600">
              Your payment information is encrypted and stored securely. 
              OEMS never stores your full card details on our servers.
            </p>
          </motion.div>
        </div>
      </div>
    );
  }

  return (
    <div className="min-h-screen bg-gray-50">
      {/* Header */}
      <div className="glass-panel mx-4 mt-12 mb-6 p-4">
        <div className="flex items-center gap-4">
          <button
            onClick={() => navigate('/')}
            className="p-2 rounded-full hover:bg-white/20"
          >
            <ArrowLeft className="w-6 h-6" />
          </button>
          <h1 className="text-lg font-semibold">Payment</h1>
        </div>
      </div>

      <div className="px-4 space-y-6">
        {/* Payment Methods */}
        <motion.div
          className="glass-panel p-6"
          initial={{ opacity: 0, y: 20 }}
          animate={{ opacity: 1, y: 0 }}
        >
          <div className="flex items-center justify-between mb-4">
            <h3 className="text-lg font-semibold">Payment Methods</h3>
            <button
              onClick={() => setShowAddCard(true)}
              className="p-2 text-teal-600 hover:bg-teal-50 rounded-full"
            >
              <Plus className="w-5 h-5" />
            </button>
          </div>
          
          <div className="space-y-3">
            {paymentMethods.map((method) => (
              <motion.button
                key={method.id}
                onClick={() => handleSetDefault(method.id)}
                className={`w-full p-4 rounded-xl border-2 transition-all ${
                  method.isDefault
                    ? 'border-teal-500 bg-teal-50'
                    : 'border-gray-200 bg-white/50'
                } hover:scale-102`}
                whileHover={{ scale: 1.02 }}
                whileTap={{ scale: 0.98 }}
              >
                <div className="flex items-center gap-4">
                  <div className="p-2 bg-white rounded-lg">
                    {method.icon}
                  </div>
                  <div className="flex-1 text-left">
                    <div className="font-medium">{method.name}</div>
                    <div className="text-sm text-gray-600">{method.details}</div>
                  </div>
                  {method.isDefault && (
                    <div className="text-xs font-medium text-teal-600 bg-teal-100 px-2 py-1 rounded-full">
                      Default
                    </div>
                  )}
                </div>
              </motion.button>
            ))}
          </div>
        </motion.div>

        {/* OEMS Wallet */}
        <motion.div
          className="glass-panel p-6"
          initial={{ opacity: 0, y: 20 }}
          animate={{ opacity: 1, y: 0 }}
          transition={{ delay: 0.1 }}
        >
          <h3 className="text-lg font-semibold mb-4">OEMS Wallet</h3>
          
          <div className="bg-gradient-to-r from-teal-500 to-teal-600 text-white p-6 rounded-2xl mb-4">
            <div className="flex items-center justify-between mb-2">
              <span className="text-teal-100">Current Balance</span>
              <Wallet className="w-6 h-6" />
            </div>
            <div className="text-3xl font-bold">₹250.00</div>
          </div>
          
          <Button
            variant="secondary"
            className="w-full"
            onClick={() => console.log('Add money to wallet')}
          >
            Add Money
          </Button>
        </motion.div>

        {/* Digital Wallets */}
        <motion.div
          className="glass-panel p-6"
          initial={{ opacity: 0, y: 20 }}
          animate={{ opacity: 1, y: 0 }}
          transition={{ delay: 0.2 }}
        >
          <h3 className="text-lg font-semibold mb-4">Digital Wallets</h3>
          <p className="text-gray-600 mb-4">Connect your digital wallets for faster payments</p>
          
          <div className="grid grid-cols-2 gap-3">
            <button className="p-4 border border-gray-200 rounded-xl hover:bg-gray-50 transition-colors">
              <div className="text-center">
                <div className="w-12 h-12 bg-blue-100 rounded-full flex items-center justify-center mx-auto mb-2">
                  💳
                </div>
                <span className="text-sm font-medium">PayPal</span>
              </div>
            </button>
            
            <button className="p-4 border border-gray-200 rounded-xl hover:bg-gray-50 transition-colors">
              <div className="text-center">
                <div className="w-12 h-12 bg-green-100 rounded-full flex items-center justify-center mx-auto mb-2">
                  💰
                </div>
                <span className="text-sm font-medium">Apple Pay</span>
              </div>
            </button>
          </div>
        </motion.div>
      </div>
    </div>
  );
};

export default PaymentMethodsPage;