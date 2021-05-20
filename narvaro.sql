-- phpMyAdmin SQL Dump
-- version 5.0.2
-- https://www.phpmyadmin.net/
--
-- Värd: 127.0.0.1:3306
-- Tid vid skapande: 18 maj 2021 kl 12:29
-- Serverversion: 5.7.31
-- PHP-version: 7.3.21

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
START TRANSACTION;
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- Databas: `narvaro`
--

-- --------------------------------------------------------

--
-- Tabellstruktur `kurs`
--

DROP TABLE IF EXISTS `kurs`;
CREATE TABLE IF NOT EXISTS `kurs` (
  `KursId` int(11) NOT NULL AUTO_INCREMENT,
  `KursNamn` varchar(50) NOT NULL,
  `startDatum` date DEFAULT NULL,
  `slutDatum` date DEFAULT NULL,
  `UtbId` int(11) NOT NULL,
  PRIMARY KEY (`KursId`),
  KEY `FK_55` (`UtbId`)
) ENGINE=MyISAM AUTO_INCREMENT=3 DEFAULT CHARSET=latin1;

--
-- Dumpning av Data i tabell `kurs`
--

INSERT INTO `kurs` (`KursId`, `KursNamn`, `startDatum`, `slutDatum`, `UtbId`) VALUES
(1, 'Java', '2021-05-01', '2021-06-01', 1),
(2, 'Webbutveckling', '2021-06-02', '2021-07-01', 2);

-- --------------------------------------------------------

--
-- Tabellstruktur `kurs_lärare`
--

DROP TABLE IF EXISTS `kurs_lärare`;
CREATE TABLE IF NOT EXISTS `kurs_lärare` (
  `PersonId` int(11) NOT NULL,
  `KursId` int(11) NOT NULL,
  KEY `FK_80` (`PersonId`),
  KEY `FK_83` (`KursId`)
) ENGINE=MyISAM DEFAULT CHARSET=latin1;

--
-- Dumpning av Data i tabell `kurs_lärare`
--

INSERT INTO `kurs_lärare` (`PersonId`, `KursId`) VALUES
(5, 1),
(4, 2);

-- --------------------------------------------------------

--
-- Tabellstruktur `lektion`
--

DROP TABLE IF EXISTS `lektion`;
CREATE TABLE IF NOT EXISTS `lektion` (
  `lekId` int(11) NOT NULL AUTO_INCREMENT,
  `datum` date NOT NULL,
  `KursId` int(11) NOT NULL,
  `Minuter` int(11) DEFAULT NULL,
  PRIMARY KEY (`lekId`),
  KEY `FK_120` (`KursId`)
) ENGINE=MyISAM AUTO_INCREMENT=8 DEFAULT CHARSET=latin1;

--
-- Dumpning av Data i tabell `lektion`
--

INSERT INTO `lektion` (`lekId`, `datum`, `KursId`, `Minuter`) VALUES
(1, '2021-05-01', 1, 120),
(2, '2021-05-03', 1, 60),
(3, '2021-05-05', 1, 180),
(4, '2021-04-02', 2, 180),
(5, '2021-04-04', 2, 180),
(6, '2021-04-06', 2, 180),
(7, '2021-05-17', 1, 120);

-- --------------------------------------------------------

--
-- Tabellstruktur `narvaro`
--

DROP TABLE IF EXISTS `narvaro`;
CREATE TABLE IF NOT EXISTS `narvaro` (
  `PersonId` int(11) NOT NULL,
  `lekId` int(11) NOT NULL,
  `Andel` int(11) DEFAULT NULL,
  KEY `FK_111` (`PersonId`),
  KEY `FK_117` (`lekId`)
) ENGINE=MyISAM DEFAULT CHARSET=latin1;

--
-- Dumpning av Data i tabell `narvaro`
--

INSERT INTO `narvaro` (`PersonId`, `lekId`, `Andel`) VALUES
(1, 1, 120),
(2, 1, 120),
(1, 2, 60),
(2, 2, 60),
(1, 3, 180),
(2, 3, 180),
(3, 1, 120),
(3, 2, 20),
(3, 3, 45),
(1, 4, NULL),
(1, 5, NULL),
(1, 6, NULL),
(2, 4, 180),
(2, 5, 180),
(2, 6, 180),
(3, 4, NULL),
(3, 5, NULL),
(3, 6, NULL),
(1, 7, 0),
(2, 7, 120),
(3, 7, 30);

-- --------------------------------------------------------

--
-- Tabellstruktur `ort`
--

DROP TABLE IF EXISTS `ort`;
CREATE TABLE IF NOT EXISTS `ort` (
  `OrtId` int(11) NOT NULL AUTO_INCREMENT,
  `OrtNamn` varchar(50) NOT NULL,
  PRIMARY KEY (`OrtId`)
) ENGINE=MyISAM AUTO_INCREMENT=3 DEFAULT CHARSET=latin1;

--
-- Dumpning av Data i tabell `ort`
--

INSERT INTO `ort` (`OrtId`, `OrtNamn`) VALUES
(1, 'Helsingborg'),
(2, 'Malmö');

-- --------------------------------------------------------

--
-- Tabellstruktur `ort_utbildning`
--

DROP TABLE IF EXISTS `ort_utbildning`;
CREATE TABLE IF NOT EXISTS `ort_utbildning` (
  `UtbId` int(11) NOT NULL,
  `OrtId` int(11) NOT NULL,
  KEY `FK_Utbid` (`UtbId`),
  KEY `FK_OrtId` (`OrtId`)
) ENGINE=MyISAM DEFAULT CHARSET=latin1;

--
-- Dumpning av Data i tabell `ort_utbildning`
--

INSERT INTO `ort_utbildning` (`UtbId`, `OrtId`) VALUES
(1, 2);

-- --------------------------------------------------------

--
-- Tabellstruktur `person`
--

DROP TABLE IF EXISTS `person`;
CREATE TABLE IF NOT EXISTS `person` (
  `PersonId` int(11) NOT NULL AUTO_INCREMENT,
  `förNamn` varchar(50) NOT NULL,
  `EfterNamn` varchar(50) NOT NULL,
  `Roll` varchar(50) NOT NULL,
  `OrtId` int(11) NOT NULL,
  PRIMARY KEY (`PersonId`),
  KEY `FK_66` (`OrtId`)
) ENGINE=MyISAM AUTO_INCREMENT=6 DEFAULT CHARSET=latin1;

--
-- Dumpning av Data i tabell `person`
--

INSERT INTO `person` (`PersonId`, `förNamn`, `EfterNamn`, `Roll`, `OrtId`) VALUES
(1, 'Hamodi', 'Alshaikhli', 'Student', 1),
(2, 'Adam', 'Johnson', 'Student', 2),
(3, 'Hoi', 'Man Phung', 'Student', 2),
(4, 'Christina', 'Rönne', 'Utbildningsledare', 2),
(5, 'Annika', 'Ros', 'Lärare', 2);

-- --------------------------------------------------------

--
-- Tabellstruktur `utbildning`
--

DROP TABLE IF EXISTS `utbildning`;
CREATE TABLE IF NOT EXISTS `utbildning` (
  `UtbId` int(11) NOT NULL AUTO_INCREMENT,
  `UtbNamn` varchar(50) NOT NULL,
  `Utbildningsledare` varchar(50) NOT NULL,
  PRIMARY KEY (`UtbId`)
) ENGINE=MyISAM AUTO_INCREMENT=2 DEFAULT CHARSET=latin1;

--
-- Dumpning av Data i tabell `utbildning`
--

INSERT INTO `utbildning` (`UtbId`, `UtbNamn`, `Utbildningsledare`) VALUES
(1, 'Javautvecklare', 'Christina Rönne');
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
