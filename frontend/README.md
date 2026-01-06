🏦 Bank Account Front-End – React + Vite
✨ Aperçu

Ce projet front-end consomme l’API du compte bancaire pour permettre aux utilisateurs de :

Consulter le solde du compte

Visualiser l’historique des transactions

Effectuer des dépôts et des retraits

Le front-end est développé en React + TypeScript avec Vite pour un build rapide et efficace.

🎯 Objectifs

Intégrer l’API REST fournie par le back-end Spring Boot

Fournir une interface utilisateur simple et réactive

Gérer les erreurs et succès provenant du back-end

Trier et afficher les transactions par date décroissante

🛠 Choix technologiques
Technologie	Usage
React	Composants réutilisables, gestion efficace de l’état
TypeScript	Typage statique et sécurité à la compilation
Axios	Communication avec l’API REST
Vite	Build rapide et serveur de développement léger
🏗 Architecture & Logique
Composants principaux

BankAccount.tsx

Formulaire de saisie pour dépôt/retrait

Affichage du solde du compte

Tableau des transactions (triées par date décroissante)

Gestion des messages d’erreur ou de succès via useState

Intégration API

Toutes les interactions avec le back-end se font via Axios :

fetchData() : récupère les transactions et le solde

addTransaction(type, amount) : envoie un dépôt ou retrait et met à jour l’état

Gestion des erreurs

Les exceptions métier du back-end (montant invalide, fonds insuffisants, dépassement de limite) sont capturées et affichées dans l’UI

Messages clairs pour l’utilisateur final

⚡ Fonctionnalités

Dépôt et retrait avec validation côté serveur

Affichage du solde en temps réel

Historique des transactions trié par date décroissante

Gestion d’erreurs et feedback utilisateur

Intégration complète avec le back-end Event Sourcing

🔄 Flux des données
Utilisateur <-> Composant BankAccount <-> Axios <-> Back-End Spring Boot <-> Repository


Le front récupère les transactions et le solde via les endpoints /balance et /transactions

Les opérations de dépôt/retrait sont envoyées via /deposit et /withdraw

Le front se met à jour automatiquement après chaque opération

⚙ Installation & Exécution
Prérequis

Node.js 18+

npm ou yarn

Back-end en cours d’exécution sur http://localhost:8080/api

Installation
cd frontend
npm install

Démarrage
npm run dev


L’interface sera disponible sur : http://localhost:5174

🧪 Tests

Tests manuels via navigateur pour vérifier :

Dépôt et retrait fonctionnels

Solde correct après chaque transaction

Affichage de l’historique

Gestion des erreurs envoyées par le back-end

📦 Structure du projet
frontend/
├─ src/
│  ├─ components/BankAccount.tsx
│  ├─ assets/
│  ├─ App.tsx
│  ├─ main.tsx
│  ├─ index.css
│  └─ types.ts
├─ public/
│  └─ vite.svg
├─ package.json
├─ tsconfig.json
└─ vite.config.ts

✅ Conclusion

Ce front-end offre une interface simple mais fonctionnelle pour gérer le compte bancaire et tester toutes les fonctionnalités implémentées côté back-end.

Intégration API complète avec React + TypeScript

Transactions et solde en temps réel

Gestion claire des erreurs et feedback utilisateur
