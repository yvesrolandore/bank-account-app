import { BankAccount } from './components/BankAccount';

function App() {
  return (
    <div
      style={{
        width: '100vw',
        minHeight: '100vh',
        display: 'flex',
        justifyContent: 'center',
        alignItems: 'center',
        backgroundColor: '#1e1e1e',
      }}
    >
      <BankAccount />
    </div>
  );
}

export default App;

