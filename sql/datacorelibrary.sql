-- phpMyAdmin SQL Dump
-- version 5.2.1
-- https://www.phpmyadmin.net/
--
-- Host: 127.0.0.1
-- Generation Time: May 15, 2026 at 12:59 PM
-- Server version: 10.4.32-MariaDB
-- PHP Version: 8.2.12

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
START TRANSACTION;
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- Database: `datacorelibrary`
--

-- --------------------------------------------------------

--
-- Table structure for table `server`
--

CREATE TABLE `server` (
  `id_asset` varchar(50) NOT NULL,
  `model_name` varchar(100) NOT NULL,
  `size_in_u` int(11) NOT NULL,
  `status` varchar(50) NOT NULL,
  `cpu_name` varchar(100) DEFAULT 'Intel Xeon',
  `cpu_cores` int(11) NOT NULL,
  `total_ram_gb` int(11) NOT NULL,
  `os_type` varchar(50) NOT NULL,
  `total_storage_gb` int(11) NOT NULL,
  `used_storage_gb` int(11) NOT NULL,
  `cpu_utilization` double DEFAULT 0,
  `ram_utilization` double DEFAULT 0,
  `rack_id` varchar(50) DEFAULT NULL,
  `start_slot` int(11) DEFAULT 0
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `server`
--

INSERT INTO `server` (`id_asset`, `model_name`, `size_in_u`, `status`, `cpu_name`, `cpu_cores`, `total_ram_gb`, `os_type`, `total_storage_gb`, `used_storage_gb`, `cpu_utilization`, `ram_utilization`, `rack_id`, `start_slot`) VALUES
('SRV-1', 'Dell PowerEdge', 1, 'Offline', 'Intel Xeon', 64, 128, 'Linux', 1024, 0, 0, 0, 'test1', 0),
('SRV-2', 'Dell PowerEdge', 1, 'Online', 'Intel Xeon', 64, 128, 'Linux', 1024, 0, 50, 50, 'test1', 1),
('SRV-3', 'Dell PowerEdge', 1, 'Offline', 'Intel Xeon', 64, 128, 'Linux', 1024, 0, 0, 0, 'test2', 2),
('SRV-4', 'Dell PowerEdge', 1, 'Offline', 'Intel Xeon', 64, 128, 'Linux', 1024, 0, 0, 0, 'test2', 3);

-- --------------------------------------------------------

--
-- Table structure for table `server_rack`
--

CREATE TABLE `server_rack` (
  `rack_id` varchar(50) NOT NULL,
  `max_capacity_u` int(11) NOT NULL,
  `zone_name` varchar(50) NOT NULL,
  `x_coord` int(11) NOT NULL,
  `y_coord` int(11) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `server_rack`
--

INSERT INTO `server_rack` (`rack_id`, `max_capacity_u`, `zone_name`, `x_coord`, `y_coord`) VALUES
('test1', 42, 'ZONA A', 0, 0),
('test2', 42, 'ZONA A', 1, 0);

--
-- Indexes for dumped tables
--

--
-- Indexes for table `server`
--
ALTER TABLE `server`
  ADD PRIMARY KEY (`id_asset`),
  ADD KEY `rack_id` (`rack_id`);

--
-- Indexes for table `server_rack`
--
ALTER TABLE `server_rack`
  ADD PRIMARY KEY (`rack_id`);

--
-- Constraints for dumped tables
--

--
-- Constraints for table `server`
--
ALTER TABLE `server`
  ADD CONSTRAINT `server_ibfk_1` FOREIGN KEY (`rack_id`) REFERENCES `server_rack` (`rack_id`) ON DELETE SET NULL;
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
