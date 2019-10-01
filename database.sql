-- phpMyAdmin SQL Dump
-- version 4.8.5
-- https://www.phpmyadmin.net/
--
-- Host: localhost:8889
-- Generation Time: May 28, 2019 at 01:59 PM
-- Server version: 5.7.25
-- PHP Version: 7.3.1

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- Database: 'catering'
--

-- --------------------------------------------------------

--
-- Table structure for table 'Events'
--

CREATE TABLE 'Events' (
  'id' int(11) NOT NULL,
  'menu' int(11) DEFAULT NULL,
  'nome' varchar(50) DEFAULT NULL,
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

--
-- Dumping data for table 'Events'
--

INSERT INTO 'Events' ('id', 'menu', 'nome') VALUES
(1, 1, 'Matrimonio'),
(2, 1, 'Compleanno'),
(3, 1, 'Nubilato');

-- --------------------------------------------------------

--
-- Table structure for table 'MenuItems'
--

CREATE TABLE 'MenuItems' (
  'id' int(11) NOT NULL,
  'menu' int(11) DEFAULT NULL,
  'section' int(11) DEFAULT '0',
  'description' tinytext,
  'recipe' int(11) NOT NULL,
  'position' int(11) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

--
-- Dumping data for table 'MenuItems'
--

INSERT INTO 'MenuItems' ('id', 'menu', 'section', 'description', 'recipe', 'position') VALUES
(1, 3, 1, 'Voce 1', 1, 0),
(2, 3, 1, 'Voce 2', 1, 1),
(3, 3, 2, 'Voce 3', 1, 0),
(4, 3, 2, 'Voce 4', 1, 2),
(5, 3, 0, 'Voce 0', 1, 0),
(6, 3, 1, 'Voce 1.5', 1, 2),
(7, 3, 2, 'Voce 3.5', 1, 1),
(8, 3, 0, 'Voce 0.2', 1, 1),
(9, 3, 0, 'Voce 0.8', 1, 2),
(10, 3, 3, 'Voce 5', 1, 0),
(11, 3, 3, 'Voce 6', 1, 1),
(12, 3, 3, 'Voce 7', 1, 2);

-- --------------------------------------------------------

--
-- Table structure for table 'Menus'
--

CREATE TABLE 'Menus' (
  'id' int(11) NOT NULL,
  'title' tinytext NOT NULL,
  'menuowner' int(11) NOT NULL,
  'published' tinyint(1) DEFAULT NULL,
  'fingerFood' tinyint(1) DEFAULT NULL,
  'cookRequired' tinyint(1) DEFAULT NULL,
  'hotDishes' tinyint(1) DEFAULT NULL,
  'kitchenRequired' tinyint(1) DEFAULT NULL,
  'buffet' tinyint(1) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

--
-- Dumping data for table 'Menus'
--

INSERT INTO 'Menus' ('id', 'title', 'menuowner', 'published', 'fingerFood', 'cookRequired', 'hotDishes', 'kitchenRequired', 'buffet') VALUES
(1, 'prova in uso', 3, 1, 1, 0, 0, 0, 1),
(2, 'prova non in uso', 2, 0, 0, 1, 1, 1, 0),
(3, 'prova struttura', 3, 0, 0, 0, 0, 0, 1),
(4, 'Ciao', 3, 0, 0, 0, 0, 0, 0),
(7, 'prova struttura', 3, 0, 0, 0, 0, 0, 1);

-- --------------------------------------------------------

--
-- Table structure for table 'Recipes'
--

CREATE TABLE 'Recipes' (
  'id' int(11) NOT NULL,
  'name' varchar(128) DEFAULT NULL,
  'type' varchar(1) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

--
-- Dumping data for table 'Recipes'
--

INSERT INTO 'Recipes' ('id', 'name', 'type') VALUES
(1, 'Salsa Tonnata', 'p'),
(2, 'Vitello Tonnato', 'r'),
(3, 'Vitello Tonnato all\'Antica', 'r'),
(4, 'Brodo di Manzo Ristretto', 'p'),
(5, 'Risotto alla Milanese', 'r'),
(6, 'Pesto Ligure', 'p'),
(7, 'Trofie avvantaggiate al pesto', 'r'),
(8, 'Orata al forno con olive', 'r'),
(9, 'Insalata russa', 'r'),
(10, 'Bagnet vert', 'p'),
(11, 'Acciughe al verde', 'r'),
(12, 'Agnolotti del plin', 'p'),
(13, 'Agnolotti al sugo d\'arrosto', 'r'),
(14, 'Agnolotti burro e salvia', 'r'),
(15, 'Brasato al barolo', 'r'),
(16, 'Panna cotta', 'r'),
(17, 'Tarte tatin', 'r');

-- --------------------------------------------------------

--
-- Table structure for table 'Roles'
--

CREATE TABLE 'Roles' (
  'id' varchar(1) NOT NULL,
  'role' varchar(32) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

--
-- Dumping data for table 'Roles'
--

INSERT INTO 'Roles' ('id', 'role') VALUES
('c', 'Cuoco'),
('h', 'Chef'),
('o', 'Organizzatore'),
('s', 'Servizio');

-- --------------------------------------------------------

--
-- Table structure for table 'Sections'
--

CREATE TABLE 'Sections' (
  'menu' int(11) NOT NULL,
  'id' int(11) NOT NULL,
  'name' tinytext,
  'position' int(11) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

--
-- Dumping data for table 'Sections'
--

INSERT INTO 'Sections' ('menu', 'id', 'name', 'position') VALUES
(3, 1, 'Primi', NULL),
(3, 2, 'Secondi', NULL),
(3, 3, 'Dessert', NULL);

-- --------------------------------------------------------

--
-- Table structure for table 'UserRoles'
--

CREATE TABLE 'UserRoles' (
  'user' int(11) NOT NULL,
  'role' varchar(1) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

--
-- Dumping data for table 'UserRoles'
--

INSERT INTO 'UserRoles' ('user', 'role') VALUES
(1, 'o'),
(2, 'h'),
(2, 'c'),
(3, 'h'),
(4, 'o'),
(4, 'h'),
(5, 'c');

-- --------------------------------------------------------

--
-- Table structure for table 'Users'
--

CREATE TABLE 'Users' (
  'id' int(11) NOT NULL,
  'name' varchar(128) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

--
-- Dumping data for table 'Users'
--

INSERT INTO 'Users' ('id', 'name') VALUES
(1, 'Marco'),
(2, 'Tony'),
(3, 'Viola'),
(4, 'Anna'),
(5, 'Giovanni');

--
-- Indexes for dumped tables
--

--
-- Indexes for table 'Events'
--
ALTER TABLE 'Events'
  ADD PRIMARY KEY ('id');

--
-- Indexes for table 'MenuItems'
--
ALTER TABLE 'MenuItems'
  ADD PRIMARY KEY ('id');

--
-- Indexes for table 'Menus'
--
ALTER TABLE 'Menus'
  ADD PRIMARY KEY ('id');

--
-- Indexes for table 'Recipes'
--
ALTER TABLE 'Recipes'
  ADD PRIMARY KEY ('id'),
  ADD UNIQUE KEY 'id' ('id');

--
-- Indexes for table 'Roles'
--
ALTER TABLE 'Roles'
  ADD PRIMARY KEY ('id'),
  ADD UNIQUE KEY 'id' ('id');

--
-- Indexes for table 'Sections'
--
ALTER TABLE 'Sections'
  ADD PRIMARY KEY ('id'),
  ADD KEY 'Sections_Menu_id_fk' ('menu');

--
-- Indexes for table 'Users'
--
ALTER TABLE 'Users'
  ADD PRIMARY KEY ('id'),
  ADD UNIQUE KEY 'id' ('id');

--
-- AUTO_INCREMENT for dumped tables
--

--
-- AUTO_INCREMENT for table 'Events'
--
ALTER TABLE 'Events'
  MODIFY 'id' int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=2;

--
-- AUTO_INCREMENT for table 'MenuItems'
--
ALTER TABLE 'MenuItems'
  MODIFY 'id' int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=26;

--
-- AUTO_INCREMENT for table 'Menus'
--
ALTER TABLE 'Menus'
  MODIFY 'id' int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=8;

--
-- AUTO_INCREMENT for table 'Recipes'
--
ALTER TABLE 'Recipes'
  MODIFY 'id' int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=18;

--
-- AUTO_INCREMENT for table 'Sections'
--
ALTER TABLE 'Sections'
  MODIFY 'id' int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=10;

--
-- AUTO_INCREMENT for table 'Users'
--
ALTER TABLE 'Users'
  MODIFY 'id' int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=6;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;