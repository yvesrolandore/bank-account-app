🏦 Take-Home Test – Bank Account Implementation (Java & React)
✨ Aperçu

Ce projet implémente un compte bancaire simple respectant les contraintes du test.

Back-End : Java 17 + Spring Boot

Front-End : React + Vite

L’objectif est de permettre la gestion des transactions bancaires (dépôt, retrait) avec Event Sourcing et Repository Pattern, tout en offrant une interface simple pour visualiser le solde et l’historique.

🎯 Objectifs du test

Implémenter un compte bancaire avec dépôt, retrait et consultation de transactions.

Assurer la traçabilité et l’historique des transactions (Event Sourcing).

Fournir un front-end minimal pour visualiser le solde et l’historique.

Séparer les responsabilités lecture/écriture (CQRS) pour un design moderne et scalable.

🛠 Back-End – Java 17
Choix technologiques
Technologie	Raison
Java 17	Stabilité et fonctionnalités modernes (records, pattern matching, sealed classes)
Spring Boot 3.x	Création rapide d’API REST et injection de dépendances
JUnit 5	Tests unitaires modernes et fiables
Maven	Gestion de dépendances et build standardisé
Architecture & Design

Domain Model :

Transaction : record immuable encapsulant date, type, montant et solde après transaction.

BankAccountImpl : logique métier du compte.

StatementPrinter : génération des états de compte (console ou extension possible).

Event Sourcing :

Les transactions sont la source de vérité : le solde est dérivé de l’historique des transactions.

Repository Pattern :

TransactionRepository abstrait la persistence, permettant un stockage interchangeable (mémoire, base de données, etc.).

CQRS (Command Query Responsibility Segregation) :

CommandHandler pour les opérations d’écriture (dépôt/retrait)

QueryHandler pour les opérations de lecture (solde/transactions)

Injection de dépendances :

DateProvider : rend les tests unitaires déterministes.

TransactionRepository : permet d’isoler la persistence pour les tests.

Exceptions métier dédiées :

AmountExceedsLimitException, InvalidAmountException, InsufficientFundsException

Fournissent des messages clairs pour le front-end et améliorent la traçabilité.

Encapsulation :

La liste des transactions est interne et non exposée directement.

Validations centralisées via méthodes privées.

Limitations & pistes d’amélioration :

Pas de support multi-devises ni frais bancaires.

Pas de gestion multi-comptes ou authentification.

StatementPrinter pourrait retourner une String plutôt que d’afficher sur la console.

🌐 API REST – Spring Boot
Méthode	URL	Description
GET	/api/transactions	Récupère toutes les transactions
GET	/api/balance	Récupère le solde actuel
POST	/api/deposit	Dépose un montant
POST	/api/withdraw	Retire un montant

Gestion des erreurs :

Les exceptions métier sont traduites en messages clairs.

Exemple : AmountExceedsLimitException → « Le montant dépasse la limite maximale de 1 000 000 par opération. »

CORS :

Autorisation pour le front React (http://localhost:5173) via configuration Spring Boot.

💻 Front-End – React avec Vite
Choix technologiques
Technologie	Usage
React	Composants réutilisables et gestion efficace de l’état
TypeScript	Typage statique pour plus de robustesse
Axios	Requêtes HTTP vers le back-end
Vite	Build rapide et simple pour le développement
Architecture

Composant unique BankAccount :

Saisie de montant

Dépôt / retrait

Affichage du solde

Tableau des transactions

Transactions triées par date décroissante

Gestion des erreurs et succès via useState.
Appels API centralisés dans fetchData() et addTransaction().

⚡ Installation & exécution
Prérequis

JDK 17+

Maven 3.8+

Node.js 18+

npm ou yarn

Back-End
# Aller dans le dossier backend
cd backend
mvn test
mvn clean compile
mvn spring-boot:run


API disponible : http://localhost:8080/api

Front-End
# Aller dans le dossier frontend
cd frontend
npm install
npm run dev


Interface disponible : http://localhost:5174

🧪 Tests
Back-End

Tests unitaires JUnit 5 pour toutes les opérations (dépôt, retrait).

Validation des exceptions et isolation via InMemoryTransactionRepository pour l’Event Sourcing.

Front-End

Tests manuels pour valider l’interaction avec l’API.

📖 OpenAPI / Swagger

Documentation via springdoc-openapi

Permet :

Documentation API claire

Tests simplifiés via Swagger UI

Collaboration facile avec front-end et mobile

Swagger UI : http://localhost:8080/swagger-ui.html

🔄 Event Sourcing, Repository & CQRS

Event Sourcing : toutes les opérations sont des transactions immuables.

Repository Pattern : TransactionRepository abstrait la persistence.

CQRS : séparation lecture/écriture via CommandHandler et QueryHandler.

Tests injectent InMemoryTransactionRepository pour simuler le stockage et garantir l’isolation.

✅ Conclusion

Le projet démontre :

Maîtrise de Java 17 et Spring Boot

Conception solide : séparation des responsabilités, encapsulation, validations centralisées

Event Sourcing appliqué : solde dérivé des transactions

Repository Pattern pour abstraction de la persistence

CQRS pour séparer lecture et écriture

Front-End React + TypeScript intégré et fonctionnel

Gestion claire des erreurs et contraintes métier

🙏 J’espère avoir passé mon test !