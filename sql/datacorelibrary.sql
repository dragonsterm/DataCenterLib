-- phpMyAdmin SQL Dump
-- version 5.2.1
-- https://www.phpmyadmin.net/
--
-- Host: 127.0.0.1
-- Generation Time: May 27, 2026 at 07:07 PM
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
-- Table structure for table `data_center_room`
--

CREATE TABLE `data_center_room` (
  `id_room` int(11) NOT NULL,
  `room_name` varchar(100) NOT NULL,
  `width_grid` int(11) NOT NULL DEFAULT 64,
  `height_grid` int(11) NOT NULL DEFAULT 64
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- --------------------------------------------------------

--
-- Table structure for table `room_path`
--

CREATE TABLE `room_path` (
  `id_path` int(11) NOT NULL,
  `id_room` int(11) NOT NULL,
  `x_coord` int(11) NOT NULL,
  `y_coord` int(11) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

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

-- --------------------------------------------------------

--
-- Table structure for table `server_rack`
--

CREATE TABLE `server_rack` (
  `rack_id` varchar(50) NOT NULL,
  `max_capacity_u` int(11) NOT NULL,
  `zone_name` varchar(50) NOT NULL,
  `x_coord` int(11) NOT NULL,
  `y_coord` int(11) NOT NULL,
  `id_room` int(11) DEFAULT 1
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Indexes for dumped tables
--

--
-- Indexes for table `data_center_room`
--
ALTER TABLE `data_center_room`
  ADD PRIMARY KEY (`id_room`);

--
-- Indexes for table `room_path`
--
ALTER TABLE `room_path`
  ADD PRIMARY KEY (`id_path`),
  ADD KEY `fk_path_room` (`id_room`);

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
  ADD PRIMARY KEY (`rack_id`),
  ADD KEY `fk_room_rack` (`id_room`);

--
-- AUTO_INCREMENT for dumped tables
--

--
-- AUTO_INCREMENT for table `data_center_room`
--
ALTER TABLE `data_center_room`
  MODIFY `id_room` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=5;

--
-- AUTO_INCREMENT for table `room_path`
--
ALTER TABLE `room_path`
  MODIFY `id_path` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=156;

--
-- Constraints for dumped tables
--

--
-- Constraints for table `room_path`
--
ALTER TABLE `room_path`
  ADD CONSTRAINT `fk_path_room` FOREIGN KEY (`id_room`) REFERENCES `data_center_room` (`id_room`) ON DELETE CASCADE;

--
-- Constraints for table `server`
--
ALTER TABLE `server`
  ADD CONSTRAINT `server_ibfk_1` FOREIGN KEY (`rack_id`) REFERENCES `server_rack` (`rack_id`) ON DELETE SET NULL;

--
-- Constraints for table `server_rack`
--
ALTER TABLE `server_rack`
  ADD CONSTRAINT `fk_room_rack` FOREIGN KEY (`id_room`) REFERENCES `data_center_room` (`id_room`) ON DELETE CASCADE;
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
