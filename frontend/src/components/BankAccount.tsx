import React, { useEffect, useState } from 'react';
import axios from 'axios';

interface Transaction {
  date: string;
  type: 'DEPOSIT' | 'WITHDRAWAL';
  amount: number;
  balanceAfter: number;
}

export const BankAccount: React.FC = () => {
  const [transactions, setTransactions] = useState<Transaction[]>([]);
  const [amount, setAmount] = useState<number>(0);
  const [error, setError] = useState<string | null>(null);
  const [balance, setBalance] = useState<number>(0);
  const [success, setSuccess] = useState<string | null>(null);

  const fetchData = async () => {
    try {
      const [txRes, balanceRes] = await Promise.all([
        axios.get<Transaction[]>('/api/transactions'),
        axios.get<number>('/api/balance')
      ]);
      setTransactions(txRes.data.sort((a, b) => b.date.localeCompare(a.date)));
      setBalance(balanceRes.data);
    } catch (err: any) {
      setError(err.message || 'Erreur lors de la récupération des données');
    }
  };

  useEffect(() => { fetchData(); }, []);

  const addTransaction = async (type: 'DEPOSIT' | 'WITHDRAWAL') => {
    try {
      if (amount <= 0) throw new Error('Le montant doit être positif');
      const url = type === 'DEPOSIT' ? '/api/deposit' : '/api/withdraw';
      await axios.post(url, null, { params: { amount } });
      setError(null);
      setSuccess(type === 'DEPOSIT' ? 'Dépôt effectué avec succès' : 'Retrait effectué avec succès');
      setAmount(0);
      fetchData();
      setTimeout(() => setSuccess(null), 3000);
    } catch (err: any) {
      setError(err.response?.data?.message || err.message);
    }
  };

  return (
    <div style={{
      maxWidth: 700,
      margin: '40px auto',
      fontFamily: '"Segoe UI", Tahoma, Geneva, Verdana, sans-serif',
      padding: 20,
      backgroundColor: '#f5f7fa',
      borderRadius: 12,
      boxShadow: '0 4px 12px rgba(0,0,0,0.1)'
    }}>
      <h2 style={{ textAlign: 'center', color: '#1b3a57', marginBottom: 20 }}>Roland Bank Versus</h2>
      
      <div style={{ display: 'flex', gap: 10, marginBottom: 10, justifyContent: 'center' }}>
        <input
          type="number"
          value={amount}
          onChange={(e) => setAmount(parseInt(e.target.value))}
          placeholder="Montant"
          style={{
            padding: 10,
            borderRadius: 8,
            border: '1px solid #ccc',
            width: 150
          }}
        />
        <button
          onClick={() => addTransaction('DEPOSIT')}
          style={{
            padding: '10px 20px',
            backgroundColor: '#4caf50',
            color: 'white',
            border: 'none',
            borderRadius: 8,
            cursor: 'pointer'
          }}
        >
          Dépôt
        </button>
        <button
          onClick={() => addTransaction('WITHDRAWAL')}
          style={{
            padding: '10px 20px',
            backgroundColor: '#f44336',
            color: 'white',
            border: 'none',
            borderRadius: 8,
            cursor: 'pointer'
          }}
        >
          Retrait
        </button>
      </div>

      {error && <div style={{ color: '#f44336', textAlign: 'center', marginBottom: 10 }}>{error}</div>}
      {success && <div style={{ color: '#4caf50', textAlign: 'center', marginBottom: 10 }}>{success}</div>}

      <h3 style={{ color: '#1b3a57', textAlign: 'center' }}>Solde: {balance.toLocaleString()} FCFA</h3>

      <table style={{
        width: '100%',
        borderCollapse: 'collapse',
        marginTop: 20,
        backgroundColor: 'white',
        borderRadius: 8,
        overflow: 'hidden',
        boxShadow: '0 2px 8px rgba(0,0,0,0.05)'
      }}>
        <thead style={{ backgroundColor: '#1b3a57', color: 'white' }}>
          <tr>
            <th style={{ padding: 12, textAlign: 'left' }}>Date</th>
            <th style={{ padding: 12, textAlign: 'left' }}>Type de transaction</th>
            <th style={{ padding: 12, textAlign: 'right' }}>Montant</th>
            <th style={{ padding: 12, textAlign: 'right' }}>Solde</th>
          </tr>
        </thead>
        <tbody>
          {transactions.map((tx, index) => (
            <tr key={index} style={{ backgroundColor: index % 2 === 0 ? 'rgb(57 57 57)' : 'rgb(57 57 57)', transition: 'background-color 0.3s' }}>
              <td style={{ padding: 10 }}>{tx.date}</td>
              <td style={{ padding: 10 }}>{tx.type}</td>
              <td style={{ padding: 10, textAlign: 'right', color: tx.type === 'DEPOSIT' ? '#4caf50' : '#f44336' }}>
                {tx.amount.toLocaleString()} FCFA
              </td>
              <td style={{ padding: 10, textAlign: 'right' }}>{tx.balanceAfter.toLocaleString()} FCFA</td>
            </tr>
          ))}
        </tbody>
      </table>
    </div>
  );
};
