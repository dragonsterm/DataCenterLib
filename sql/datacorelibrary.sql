-- phpMyAdmin SQL Dump
-- version 5.2.1
-- https://www.phpmyadmin.net/
--
-- Host: 127.0.0.1
-- Generation Time: May 20, 2026 at 12:30 PM
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

--
-- Dumping data for table `data_center_room`
--

INSERT INTO `data_center_room` (`id_room`, `room_name`, `width_grid`, `height_grid`) VALUES
(1, 'Alpha Core Room', 64, 64),
(2, 'Test', 64, 64),
(3, 'RoomTest', 32, 32);

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

--
-- Dumping data for table `room_path`
--

INSERT INTO `room_path` (`id_path`, `id_room`, `x_coord`, `y_coord`) VALUES
(4, 1, 2, 3),
(5, 1, 1, 3),
(6, 1, 3, 3),
(7, 1, 4, 3),
(8, 1, 5, 3),
(9, 1, 6, 3),
(10, 1, 8, 3),
(11, 1, 9, 3),
(12, 1, 7, 4),
(13, 1, 7, 5),
(14, 1, 7, 6),
(15, 1, 7, 2),
(16, 1, 7, 1),
(17, 1, 7, 0),
(18, 1, 3, 0),
(19, 1, 3, 1),
(20, 1, 3, 2),
(21, 1, 3, 4),
(22, 1, 3, 5),
(23, 1, 3, 6),
(24, 1, 0, 3),
(25, 1, 1, 3),
(26, 1, 2, 3),
(27, 1, 3, 3),
(28, 1, 3, 2),
(29, 1, 3, 1),
(30, 1, 3, 0),
(31, 1, 3, 5),
(32, 1, 3, 4),
(33, 1, 3, 6),
(34, 1, 4, 3),
(35, 1, 6, 3),
(36, 1, 7, 3),
(37, 1, 7, 2),
(38, 1, 7, 0),
(39, 1, 7, 1),
(40, 1, 5, 3),
(41, 1, 8, 3),
(42, 1, 9, 3),
(43, 1, 10, 3),
(44, 1, 7, 4),
(45, 1, 7, 5),
(46, 1, 7, 6),
(47, 1, 11, 3);

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
('SRV-2', 'Dell PowerEdge', 1, 'Online', 'Intel Xeon', 64, 128, 'Linux', 1024, 108, 50, 50, 'test1', 1),
('SRV-4', 'Dell PowerEdge', 1, 'Offline', 'Intel Xeon', 64, 128, 'Linux', 1024, 0, 0, 0, 'test2', 3),
('SRV-5', 'Dell PowerEdge', 1, 'Offline', 'Intel Xeon', 64, 128, 'Linux', 1024, 0, 0, 0, 'Test', 2),
('SRV-6', 'Dell PowerEdge', 1, 'Offline', 'Intel Xeon', 64, 128, 'Linux', 1024, 0, 0, 0, 'Test', 3);

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
-- Dumping data for table `server_rack`
--

INSERT INTO `server_rack` (`rack_id`, `max_capacity_u`, `zone_name`, `x_coord`, `y_coord`, `id_room`) VALUES
('Test', 42, 'ZONA A', 8, 4, 1),
('test1', 42, 'ZONA A', 0, 0, 1),
('test2', 42, 'ZONA A', 4, 0, 1),
('test3', 12, 'Test zona', 0, 4, 1),
('test4', 12, 'ZONA B', 4, 4, 1),
('w', 42, 'ZONA A', 8, 0, 1);

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
  MODIFY `id_room` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=4;

--
-- AUTO_INCREMENT for table `room_path`
--
ALTER TABLE `room_path`
  MODIFY `id_path` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=48;

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
