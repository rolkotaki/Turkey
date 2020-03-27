-- phpMyAdmin SQL Dump
-- version 3.5.1
-- http://www.phpmyadmin.net
--
-- Hoszt: localhost
-- Létrehozás ideje: 2013. márc. 19. 19:20
-- Szerver verzió: 5.5.24-log
-- PHP verzió: 5.3.13

SET SQL_MODE="NO_AUTO_VALUE_ON_ZERO";
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8 */;

--
-- Adatbázis: `turkeybase`
--

CREATE DATABASE IF NOT EXISTS `turkeybase`;
USE `turkeybase`;

-- --------------------------------------------------------

--
-- Tábla szerkezet: `consumption_buck`
--

CREATE TABLE IF NOT EXISTS `consumption_buck` (
  `week` tinyint(4) NOT NULL,
  `cons_real` smallint(5) unsigned NOT NULL,
  PRIMARY KEY (`week`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

-- --------------------------------------------------------

--
-- Tábla szerkezet: `consumption_hen`
--

CREATE TABLE IF NOT EXISTS `consumption_hen` (
  `week` tinyint(4) NOT NULL,
  `cons_real` smallint(5) unsigned NOT NULL,
  PRIMARY KEY (`week`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

-- --------------------------------------------------------

--
-- Tábla szerkezet: `consumption_prescribed`
--

CREATE TABLE IF NOT EXISTS `consumption_prescribed` (
  `week` tinyint(4) NOT NULL,
  `prescribed_buck` float NOT NULL,
  `prescribed_hen` float NOT NULL,
  PRIMARY KEY (`week`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

-- --------------------------------------------------------

--
-- Tábla szerkezet: `fodder_buck`
--

CREATE TABLE IF NOT EXISTS `fodder_buck` (
  `date` date NOT NULL,
  `fodder` smallint(6) NOT NULL,
  PRIMARY KEY (`date`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

-- --------------------------------------------------------

--
-- Tábla szerkezet: `fodder_hen`
--

CREATE TABLE IF NOT EXISTS `fodder_hen` (
  `date` date NOT NULL,
  `fodder` smallint(6) NOT NULL,
  PRIMARY KEY (`date`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

-- --------------------------------------------------------

--
-- Tábla szerkezet: `medicine`
--

CREATE TABLE IF NOT EXISTS `medicine` (
  `date` date NOT NULL,
  `description` varchar(200) NOT NULL,
  PRIMARY KEY (`date`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- --------------------------------------------------------

--
-- Tábla szerkezet: `price_change`
--

CREATE TABLE IF NOT EXISTS `price_change` (
  `date` date NOT NULL,
  `description` varchar(200) NOT NULL,
  PRIMARY KEY (`date`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- --------------------------------------------------------

--
-- Tábla szerkezet: `stock`
--

CREATE TABLE IF NOT EXISTS `stock` (
  `date` date NOT NULL,
  `straw_buck` tinyint(3) unsigned NOT NULL,
  `straw_hen` tinyint(3) unsigned NOT NULL,
  `fodder_order` smallint(6) NOT NULL,
  `fodder_receive` smallint(6) NOT NULL,
  PRIMARY KEY (`date`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

-- --------------------------------------------------------

--
-- Tábla szerkezet: `stock_now`
--

CREATE TABLE IF NOT EXISTS `stock_now` (
  `name` varchar(50) NOT NULL,
  `amount` mediumint(9) NOT NULL,
  PRIMARY KEY (`name`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

-- --------------------------------------------------------

--
-- Tábla szerkezet: `stock_start`
--

CREATE TABLE IF NOT EXISTS `stock_start` (
  `buck_stock` smallint(6) NOT NULL,
  `hen_stock` smallint(6) NOT NULL,
  `on_stock` mediumint(9) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

-- --------------------------------------------------------

--
-- Tábla szerkezet: `technical_event`
--

CREATE TABLE IF NOT EXISTS `technical_event` (
  `date` date NOT NULL,
  `description` varchar(200) NOT NULL,
  PRIMARY KEY (`date`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- --------------------------------------------------------

--
-- Tábla szerkezet: `turkey_arrive`
--

CREATE TABLE IF NOT EXISTS `turkey_arrive` (
  `arrive_date` date NOT NULL,
  `age` tinyint(4) NOT NULL,
  `buck_number` smallint(6) NOT NULL,
  `hen_number` smallint(6) NOT NULL,
  PRIMARY KEY (`arrive_date`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

-- --------------------------------------------------------

--
-- Tábla szerkezet: `turkey_lost`
--

CREATE TABLE IF NOT EXISTS `turkey_lost` (
  `date` date NOT NULL,
  `faulty_buck` smallint(6) NOT NULL,
  `faulty_hen` smallint(6) NOT NULL,
  `death_buck` smallint(6) NOT NULL,
  `death_hen` smallint(6) NOT NULL,
  PRIMARY KEY (`date`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

-- --------------------------------------------------------

--
-- Tábla szerkezet: `turkey_number`
--

CREATE TABLE IF NOT EXISTS `turkey_number` (
  `buck` smallint(6) NOT NULL,
  `hen` smallint(6) NOT NULL,
  `allturkey` smallint(6) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

-- --------------------------------------------------------

--
-- Tábla szerkezet: `turkey_number_change`
--

CREATE TABLE IF NOT EXISTS `turkey_number_change` (
  `date` date NOT NULL,
  `buck_to_hen` smallint(6) DEFAULT NULL,
  `hen_to_buck` smallint(6) DEFAULT NULL,
  PRIMARY KEY (`date`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

-- --------------------------------------------------------

--
-- Tábla szerkezet: `unit_fodder`
--

CREATE TABLE IF NOT EXISTS `unit_fodder` (
  `bag` tinyint(4) NOT NULL,
  `kg` float NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

-- --------------------------------------------------------

--
-- Tábla szerkezet: `user`
--

CREATE TABLE IF NOT EXISTS `user` (
  `username` varchar(25) NOT NULL,
  `password` varchar(25) NOT NULL,
  PRIMARY KEY (`username`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- A tábla adatainak kiíratása `user`
--

INSERT INTO `user` (`username`, `password`) VALUES
('user', 'password');

-- --------------------------------------------------------

--
-- Tábla szerkezet: `weighing`
--

CREATE TABLE IF NOT EXISTS `weighing` (
  `week` tinyint(4) NOT NULL,
  `buck_weight` float NOT NULL,
  `hen_weight` float NOT NULL,
  `all_average_weight` float NOT NULL,
  PRIMARY KEY (`week`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
