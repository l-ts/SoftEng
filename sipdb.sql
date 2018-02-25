-- Database: `sipdb`

CREATE DATABASE sipdb;
USE sipdb;

CREATE TABLE `billing` (
  `user` varchar(40) NOT NULL,
  `total_cost` float NOT NULL
);

CREATE TABLE `blocks` (
  `blocker` varchar(40) NOT NULL,
  `blocked` varchar(40) NOT NULL
);


CREATE TABLE `forwards` (
  `fromUser` varchar(40) NOT NULL,
  `toUser` varchar(40) NOT NULL
);


CREATE TABLE `golden` (
  `user` varchar(40) NOT NULL
);


CREATE TABLE `user_info` (
  `username` varchar(40) NOT NULL,
  `password` varchar(40) NOT NULL
);

