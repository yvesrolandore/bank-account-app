import { createRoot } from 'react-dom/client';
import { BankAccount } from './components/BankAccount';

const container = document.getElementById('root');
const root = createRoot(container!);
root.render(<BankAccount />);
