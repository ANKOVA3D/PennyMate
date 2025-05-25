-- phpMyAdmin SQL Dump
-- version 5.2.1
-- https://www.phpmyadmin.net/
--
-- Host: 127.0.0.1
-- Creato il: Mag 25, 2025 alle 23:00
-- Versione del server: 10.4.32-MariaDB
-- Versione PHP: 8.2.12

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
START TRANSACTION;
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- Database: `pennymate`
--

-- --------------------------------------------------------

--
-- Struttura della tabella `budget`
--

CREATE TABLE `budget` (
  `id` bigint(20) UNSIGNED NOT NULL,
  `id_utente` int(11) NOT NULL,
  `week` decimal(10,2) DEFAULT NULL,
  `month` decimal(10,2) DEFAULT NULL,
  `year` decimal(10,2) DEFAULT NULL,
  `wDate` timestamp NULL DEFAULT NULL,
  `mDate` timestamp NULL DEFAULT NULL,
  `yDate` timestamp NULL DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dump dei dati per la tabella `budget`
--

INSERT INTO `budget` (`id`, `id_utente`, `week`, `month`, `year`, `wDate`, `mDate`, `yDate`) VALUES
(14, 18, 12.00, 100.00, 0.00, '2025-05-26 22:00:00', '2025-04-30 22:00:00', '2025-05-20 22:00:00'),
(15, 19, 0.00, 0.00, 0.00, NULL, NULL, NULL),
(16, 20, 100.00, 0.00, 0.00, '2025-05-10 22:00:00', NULL, NULL),
(17, 21, 0.00, 0.00, 0.00, NULL, NULL, NULL),
(18, 22, 0.00, 0.00, 0.00, NULL, NULL, NULL),
(19, 23, 0.00, 0.00, 0.00, NULL, NULL, NULL),
(20, 24, 0.00, 0.00, 0.00, NULL, NULL, NULL),
(21, 25, 0.00, 0.00, 0.00, NULL, NULL, NULL),
(23, 27, 100.00, 0.00, 0.00, '2025-05-06 22:00:00', NULL, NULL),
(24, 28, 100.00, 100.00, 0.00, '2024-12-31 23:00:00', NULL, NULL),
(26, 30, 0.00, 0.00, 0.00, NULL, NULL, NULL),
(27, 31, 0.00, 0.00, 0.00, NULL, NULL, NULL),
(28, 32, 0.00, 0.00, 0.00, NULL, NULL, NULL);

-- --------------------------------------------------------

--
-- Struttura della tabella `category`
--

CREATE TABLE `category` (
  `nome` varchar(20) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dump dei dati per la tabella `category`
--

INSERT INTO `category` (`nome`) VALUES
('Alimentare'),
('Auto'),
('Bollette'),
('Stipendio'),
('Svago');

-- --------------------------------------------------------

--
-- Struttura della tabella `transactions`
--

CREATE TABLE `transactions` (
  `id` int(11) NOT NULL,
  `user_id` int(11) NOT NULL,
  `amount` decimal(10,2) NOT NULL,
  `type` tinyint(1) NOT NULL,
  `category` varchar(20) DEFAULT NULL,
  `description` text DEFAULT NULL,
  `date` timestamp NOT NULL DEFAULT current_timestamp() ON UPDATE current_timestamp()
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dump dei dati per la tabella `transactions`
--

INSERT INTO `transactions` (`id`, `user_id`, `amount`, `type`, `category`, `description`, `date`) VALUES
(91, 20, -5516.00, 0, 'Bollette', '', '2025-05-11 22:00:00'),
(92, 22, 99999999.00, 1, 'Alimentare', 'Goida', '2025-05-24 22:00:00'),
(96, 23, -999999.00, 0, 'Svago', 'Sviluppo signor rettile', '2025-05-20 22:00:00'),
(97, 27, -50.00, 0, 'Alimentare', '', '2025-05-07 22:00:00'),
(98, 28, -50.00, 0, 'Alimentare', '', '2025-01-01 23:00:00'),
(115, 18, -12.00, 0, 'Alimentare', '12', '2025-05-08 22:00:00'),
(117, 18, 12.00, 1, 'Alimentare', 'as', '2025-05-16 22:00:00');

-- --------------------------------------------------------

--
-- Struttura della tabella `users`
--

CREATE TABLE `users` (
  `id` int(11) NOT NULL,
  `username` varchar(255) NOT NULL,
  `email` varchar(255) NOT NULL,
  `password_hash` varchar(255) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dump dei dati per la tabella `users`
--

INSERT INTO `users` (`id`, `username`, `email`, `password_hash`) VALUES
(18, 'One', 'one@gmail.com', '$2a$10$xml2PPwMNkjanDfENZUROeeMkbRmD4XsZ5Ztg8s1biSHvcX08xGiS'),

--
-- Indici per le tabelle scaricate
--

--
-- Indici per le tabelle `budget`
--
ALTER TABLE `budget`
  ADD PRIMARY KEY (`id`),
  ADD KEY `id_utente` (`id_utente`);

--
-- Indici per le tabelle `category`
--
ALTER TABLE `category`
  ADD PRIMARY KEY (`nome`);

--
-- Indici per le tabelle `transactions`
--
ALTER TABLE `transactions`
  ADD PRIMARY KEY (`id`),
  ADD KEY `user_id` (`user_id`),
  ADD KEY `fk_user` (`category`);

--
-- Indici per le tabelle `users`
--
ALTER TABLE `users`
  ADD PRIMARY KEY (`id`),
  ADD UNIQUE KEY `username` (`username`),
  ADD UNIQUE KEY `email` (`email`);

--
-- AUTO_INCREMENT per le tabelle scaricate
--

--
-- AUTO_INCREMENT per la tabella `budget`
--
ALTER TABLE `budget`
  MODIFY `id` bigint(20) UNSIGNED NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=29;

--
-- AUTO_INCREMENT per la tabella `transactions`
--
ALTER TABLE `transactions`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=118;

--
-- AUTO_INCREMENT per la tabella `users`
--
ALTER TABLE `users`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=33;

--
-- Limiti per le tabelle scaricate
--

--
-- Limiti per la tabella `budget`
--
ALTER TABLE `budget`
  ADD CONSTRAINT `budget_ibfk_1` FOREIGN KEY (`id_utente`) REFERENCES `users` (`id`) ON DELETE CASCADE;

--
-- Limiti per la tabella `transactions`
--
ALTER TABLE `transactions`
  ADD CONSTRAINT `fk_user` FOREIGN KEY (`category`) REFERENCES `category` (`nome`),
  ADD CONSTRAINT `transactions_ibfk_1` FOREIGN KEY (`user_id`) REFERENCES `users` (`id`) ON DELETE CASCADE;
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
