CREATE DATABASE  IF NOT EXISTS `housinggame` /*!40100 DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci */;
USE `housinggame`;
-- MariaDB dump 10.18  Distrib 10.5.8-MariaDB, for Win64 (AMD64)
--
-- Host: 127.0.0.1    Database: housinggame
-- ------------------------------------------------------
-- Server version	10.11.5-MariaDB

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;

--
-- Table structure for table `bid`
--

DROP TABLE IF EXISTS `bid`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `bid` (
  `id` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `price` int(11) NOT NULL,
  `groupround_id` int(10) unsigned NOT NULL,
  `house_id` int(10) unsigned NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `id_UNIQUE` (`id`),
  KEY `fk_bid_groupround1_idx` (`groupround_id`),
  KEY `fk_bid_house1_idx` (`house_id`),
  CONSTRAINT `fk_bid_groupround1` FOREIGN KEY (`groupround_id`) REFERENCES `groupround` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `fk_bid_house1` FOREIGN KEY (`house_id`) REFERENCES `house` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `bid`
--

LOCK TABLES `bid` WRITE;
/*!40000 ALTER TABLE `bid` DISABLE KEYS */;
INSERT INTO `bid` VALUES (4,222000,11,47);
/*!40000 ALTER TABLE `bid` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `community`
--

DROP TABLE IF EXISTS `community`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `community` (
  `id` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `name` varchar(255) NOT NULL,
  `capacity` int(11) NOT NULL,
  `protection_rain_flood` int(11) NOT NULL,
  `protection_river_flood` int(11) NOT NULL,
  `gameversion_id` int(10) unsigned NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `id_UNIQUE` (`id`),
  KEY `fk_community_gameversion1_idx` (`gameversion_id`),
  CONSTRAINT `fk_community_gameversion1` FOREIGN KEY (`gameversion_id`) REFERENCES `gameversion` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB AUTO_INCREMENT=16 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `community`
--

LOCK TABLES `community` WRITE;
/*!40000 ALTER TABLE `community` DISABLE KEYS */;
INSERT INTO `community` VALUES (7,'Natucity',7,8,10,3),(8,'Dyktown',10,6,10,3),(9,'Unbesvillage',10,8,7,3),(13,'Natucity',7,8,10,5),(14,'Dyktown',10,6,10,5),(15,'Unbesvillage',10,8,7,5);
/*!40000 ALTER TABLE `community` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `facilitator`
--

DROP TABLE IF EXISTS `facilitator`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `facilitator` (
  `id` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `name` varchar(16) NOT NULL,
  `user_id` int(10) unsigned NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `Id_UNIQUE` (`id`),
  KEY `fk_facilitator_user1_idx` (`user_id`),
  CONSTRAINT `fk_facilitator_user1` FOREIGN KEY (`user_id`) REFERENCES `user` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `facilitator`
--

LOCK TABLES `facilitator` WRITE;
/*!40000 ALTER TABLE `facilitator` DISABLE KEYS */;
/*!40000 ALTER TABLE `facilitator` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `gamesession`
--

DROP TABLE IF EXISTS `gamesession`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `gamesession` (
  `id` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `name` varchar(32) NOT NULL,
  `password` varchar(32) NOT NULL,
  `location` varchar(255) DEFAULT NULL,
  `create_time` timestamp NULL DEFAULT current_timestamp(),
  `date` date DEFAULT NULL,
  `start_time` datetime DEFAULT NULL,
  `end_time` datetime DEFAULT NULL,
  `gameversion_id` int(10) unsigned NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `Id_UNIQUE` (`id`),
  UNIQUE KEY `name_UNIQUE` (`name`),
  KEY `fk_gamesession_gameversion1_idx` (`gameversion_id`),
  CONSTRAINT `fk_gamesession_gameversion1` FOREIGN KEY (`gameversion_id`) REFERENCES `gameversion` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB AUTO_INCREMENT=7 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `gamesession`
--

LOCK TABLES `gamesession` WRITE;
/*!40000 ALTER TABLE `gamesession` DISABLE KEYS */;
INSERT INTO `gamesession` VALUES (5,'Ommen23 Morning','ommen','Ommen EPA','2023-10-07 09:45:47','2023-09-26','2023-09-26 09:00:00','2023-11-26 12:00:00',3),(6,'Ommen23 Afternoon','ommen','Ommen EPA','2023-10-07 21:39:23','2023-09-26','2023-11-10 09:00:00',NULL,3);
/*!40000 ALTER TABLE `gamesession` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `gameversion`
--

DROP TABLE IF EXISTS `gameversion`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `gameversion` (
  `id` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `name` varchar(255) NOT NULL,
  `languagegroup_id` int(10) unsigned NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `id_UNIQUE` (`id`),
  UNIQUE KEY `name_UNIQUE` (`name`),
  KEY `fk_gameversion_languagegroup1_idx` (`languagegroup_id`),
  CONSTRAINT `fk_gameversion_languagegroup1` FOREIGN KEY (`languagegroup_id`) REFERENCES `languagegroup` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB AUTO_INCREMENT=6 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `gameversion`
--

LOCK TABLES `gameversion` WRITE;
/*!40000 ALTER TABLE `gameversion` DISABLE KEYS */;
INSERT INTO `gameversion` VALUES (3,'Game Version 2023-09 English',1),(5,'Game Versie 2023-11 Nederlands',1);
/*!40000 ALTER TABLE `gameversion` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `group`
--

DROP TABLE IF EXISTS `group`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `group` (
  `id` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `name` varchar(32) NOT NULL,
  `password` varchar(32) NOT NULL,
  `gamesession_id` int(10) unsigned NOT NULL,
  `scenario_id` int(10) unsigned NOT NULL,
  `facilitator_id` int(10) unsigned DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `Id_UNIQUE` (`id`),
  KEY `fk_group_gamesession1_idx` (`gamesession_id`),
  KEY `fk_group_facilitator1_idx` (`facilitator_id`),
  KEY `fk_group_scenario1_idx` (`scenario_id`),
  CONSTRAINT `fk_group_facilitator1` FOREIGN KEY (`facilitator_id`) REFERENCES `facilitator` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `fk_group_gamesession1` FOREIGN KEY (`gamesession_id`) REFERENCES `gamesession` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `fk_group_scenario1` FOREIGN KEY (`scenario_id`) REFERENCES `scenario` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB AUTO_INCREMENT=39 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `group`
--

LOCK TABLES `group` WRITE;
/*!40000 ALTER TABLE `group` DISABLE KEYS */;
INSERT INTO `group` VALUES (27,'Table1','ommen',5,3,NULL),(28,'Table2','ommen',5,3,NULL),(29,'Table3','ommen',5,3,NULL),(30,'Table4','ommen',5,4,NULL),(31,'Table5','ommen',5,4,NULL),(32,'Table6','ommen',5,4,NULL),(33,'Table1','ommen',6,3,NULL),(34,'Table2','ommen',6,3,NULL),(35,'Table3','ommen',6,3,NULL),(36,'Table4','ommen',6,4,NULL),(37,'Table5','ommen',6,4,NULL),(38,'Table6','ommen',6,4,NULL);
/*!40000 ALTER TABLE `group` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `groupround`
--

DROP TABLE IF EXISTS `groupround`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `groupround` (
  `id` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `pluvial_flood_intensity` int(11) DEFAULT NULL,
  `fluvial_flood_intensity` int(11) DEFAULT NULL,
  `start_time` datetime DEFAULT NULL,
  `round_state` varchar(16) DEFAULT 'INIT',
  `group_id` int(10) unsigned NOT NULL,
  `round_id` int(10) unsigned NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `id_UNIQUE` (`id`),
  UNIQUE KEY `id_group_round` (`group_id`,`round_id`) COMMENT 'Ensure the combination of group and round is unique, so each group can play a round only once.',
  KEY `fk_groupround_group1_idx` (`group_id`),
  KEY `fk_groupround_round1_idx` (`round_id`),
  CONSTRAINT `fk_groupround_group1` FOREIGN KEY (`group_id`) REFERENCES `group` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `fk_groupround_round1` FOREIGN KEY (`round_id`) REFERENCES `round` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB AUTO_INCREMENT=17 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `groupround`
--

LOCK TABLES `groupround` WRITE;
/*!40000 ALTER TABLE `groupround` DISABLE KEYS */;
INSERT INTO `groupround` VALUES (11,0,0,'2023-10-22 09:00:00','INIT',27,7),(15,0,0,NULL,'INIT',29,13),(16,0,0,NULL,'HOUSE',30,14);
/*!40000 ALTER TABLE `groupround` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `house`
--

DROP TABLE IF EXISTS `house`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `house` (
  `id` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `price` int(10) unsigned NOT NULL,
  `address` varchar(45) NOT NULL,
  `available_round` int(10) unsigned NOT NULL DEFAULT 1,
  `description` text NOT NULL,
  `rating` int(10) unsigned NOT NULL,
  `initial_pluvial_protection` int(11) NOT NULL,
  `initial_fluvial_protection` int(11) NOT NULL,
  `community_id` int(10) unsigned NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `id_UNIQUE` (`id`),
  KEY `fk_house_community1_idx` (`community_id`),
  CONSTRAINT `fk_house_community1` FOREIGN KEY (`community_id`) REFERENCES `community` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB AUTO_INCREMENT=110 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `house`
--

LOCK TABLES `house` WRITE;
/*!40000 ALTER TABLE `house` DISABLE KEYS */;
INSERT INTO `house` VALUES (29,425000,'N01',1,'Stickroad 17',9,0,0,7),(30,200000,'N02',5,'Twiglane 7\r\n',6,1,1,7),(31,425000,'N03',4,'Stickroad 21\r\n\r\n',9,1,1,7),(32,200000,'N04',1,'Twiglane 8\r\n',6,0,0,7),(33,200000,'N05',2,'Twiglane 5\r\n\r\n\r\n',6,0,0,7),(34,100000,'N06',3,'Leafstreet 28\r\n\r\n',3,1,1,7),(35,100000,'N07',1,'Leafstreet 26\r\n',3,0,0,7),(36,300000,'D01',1,'Stonelane 13\r\n',8,0,0,8),(37,300000,'D02',2,'Stonelane 11\r\n\r\n',8,0,0,8),(38,300000,'D03',3,'Stonelane 15\r\n\r\n',8,0,0,8),(39,160000,'D04',1,'Steelstreet 22\r\n',5,0,0,8),(40,160000,'D05',1,'Steelstreet 20\r\n',5,0,0,8),(41,160000,'D06',3,'Steelstreet 25\r\n\r\n',5,1,0,8),(42,160000,'D07',4,'Steelstreet 23\r\n\r\n',5,1,2,8),(43,80000,'D08',4,'Brickroad 8\r\n\r\n',2,1,2,8),(44,80000,'D09',2,'Brickroad 4\r\n\r\n',2,0,0,8),(45,80000,'D10',1,'Brickroad 6\r\n',2,0,0,8),(46,200000,'U01',1,'Woolstreet 33\r\n',6,0,0,9),(47,200000,'U02',3,'Woolstreet 37\r\n\r\n',6,0,1,9),(48,200000,'U03',4,'Woolstreet 35\r\n\r\n',6,1,1,9),(49,125000,'U04',1,'Grassroad 54\r\n',4,0,0,9),(50,125000,'U05',2,'Grassroad 52\r\n\r\n',4,0,0,9),(51,125000,'U06',3,'Grassroad 51\r\n\r\n',4,1,1,9),(52,125000,'U07',5,'Grassroad 53\r\n\r\n',4,1,2,9),(53,70000,'U08',4,'Twiglane 46\r\n\r\n',2,1,2,9),(54,70000,'U09',2,'Twiglane 44\r\n\r\n',2,0,0,9),(55,70000,'U10',1,'Twiglane 48\r\n',2,0,0,9),(83,425000,'N01',1,'Stickroad 17',9,0,0,13),(84,200000,'N02',5,'Twiglane 7\r\n',6,1,1,13),(85,425000,'N03',4,'Stickroad 21\r\n\r\n',9,1,1,13),(86,200000,'N04',1,'Twiglane 8\r\n',6,0,0,13),(87,200000,'N05',2,'Twiglane 5\r\n\r\n\r\n',6,0,0,13),(88,100000,'N06',3,'Leafstreet 28\r\n\r\n',3,1,1,13),(89,100000,'N07',1,'Leafstreet 26\r\n',3,0,0,13),(90,300000,'D01',1,'Stonelane 13\r\n',8,0,0,14),(91,300000,'D02',2,'Stonelane 11\r\n\r\n',8,0,0,14),(92,300000,'D03',3,'Stonelane 15\r\n\r\n',8,0,0,14),(93,160000,'D04',1,'Steelstreet 22\r\n',5,0,0,14),(94,160000,'D05',1,'Steelstreet 20\r\n',5,0,0,14),(95,160000,'D06',3,'Steelstreet 25\r\n\r\n',5,1,0,14),(96,160000,'D07',4,'Steelstreet 23\r\n\r\n',5,1,2,14),(97,80000,'D08',4,'Brickroad 8\r\n\r\n',2,1,2,14),(98,80000,'D09',2,'Brickroad 4\r\n\r\n',2,0,0,14),(99,80000,'D10',1,'Brickroad 6\r\n',2,0,0,14),(100,200000,'U01',1,'Woolstreet 33\r\n',6,0,0,15),(101,200000,'U02',3,'Woolstreet 37\r\n\r\n',6,0,1,15),(102,200000,'U03',4,'Woolstreet 35\r\n\r\n',6,1,1,15),(103,125000,'U04',1,'Grassroad 54\r\n',4,0,0,15),(104,125000,'U05',2,'Grassroad 52\r\n\r\n',4,0,0,15),(105,125000,'U06',3,'Grassroad 51\r\n\r\n',4,1,1,15),(106,125000,'U07',5,'Grassroad 53\r\n\r\n',4,1,2,15),(107,70000,'U08',4,'Twiglane 46\r\n\r\n',2,1,2,15),(108,70000,'U09',2,'Twiglane 44\r\n\r\n',2,0,0,15),(109,70000,'U10',1,'Twiglane 48\r\n',2,0,0,15);
/*!40000 ALTER TABLE `house` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `initialhousemeasure`
--

DROP TABLE IF EXISTS `initialhousemeasure`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `initialhousemeasure` (
  `id` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `name` varchar(45) NOT NULL,
  `round` int(10) unsigned NOT NULL,
  `measuretype_id` int(10) unsigned NOT NULL,
  `house_id` int(10) unsigned NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `id_UNIQUE` (`id`),
  KEY `fk_initialhousemeasure_measuretype1_idx` (`measuretype_id`),
  KEY `fk_initialhousemeasure_house1_idx` (`house_id`),
  CONSTRAINT `fk_initialhousemeasure_house1` FOREIGN KEY (`house_id`) REFERENCES `house` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `fk_initialhousemeasure_measuretype1` FOREIGN KEY (`measuretype_id`) REFERENCES `measuretype` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB AUTO_INCREMENT=55 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `initialhousemeasure`
--

LOCK TABLES `initialhousemeasure` WRITE;
/*!40000 ALTER TABLE `initialhousemeasure` DISABLE KEYS */;
INSERT INTO `initialhousemeasure` VALUES (1,'U06 sandbags',2,26,51),(2,'U02 bulkhead',2,22,47),(3,'D06 water pump',1,16,41),(4,'N06 sandbags',2,26,34),(5,'U02 ground level',2,20,53),(6,'U03 green garden',1,12,48),(7,'U03 bulkhead',3,22,48),(8,'D08 Green Garden',2,12,43),(9,'D08 Ground Level',2,20,43),(10,'N03 Rail Barrel',3,18,31),(11,'N03 Bulkhead',3,22,31),(12,'D07 steel walls',3,14,42),(13,'D07 water resist',1,24,42),(14,'N02 steel walls',4,14,30),(15,'N02 water pump',4,16,30),(16,'U07 water pump',3,16,52),(17,'U07 bulkhead',4,22,52),(18,'U07 walls floors',3,24,52),(37,'N02 steel walls',4,36,84),(38,'N02 water pump',4,37,84),(39,'N03 Rail Barrel',3,38,85),(40,'N03 Bulkhead',3,40,85),(41,'N06 sandbags',2,42,88),(42,'D06 water pump',1,37,95),(43,'D07 steel walls',3,36,96),(44,'D07 water resist',1,41,96),(45,'D08 Green Garden',2,35,97),(46,'D08 Ground Level',2,39,97),(47,'U02 bulkhead',2,40,101),(48,'U03 green garden',1,35,102),(49,'U03 bulkhead',3,40,102),(50,'U06 sandbags',2,42,105),(51,'U07 water pump',3,37,106),(52,'U07 bulkhead',4,40,106),(53,'U07 walls floors',3,41,106),(54,'U02 ground level',2,39,107);
/*!40000 ALTER TABLE `initialhousemeasure` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `label`
--

DROP TABLE IF EXISTS `label`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `label` (
  `id` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `key` varchar(45) NOT NULL,
  `value1` text NOT NULL,
  `value2` text DEFAULT NULL,
  `value3` text DEFAULT NULL,
  `value4` text DEFAULT NULL,
  `languagegroup_id` int(10) unsigned NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `id_UNIQUE` (`id`),
  UNIQUE KEY `key_UNIQUE` (`languagegroup_id`,`key`),
  KEY `fk_label_languagegroup1_idx` (`languagegroup_id`),
  CONSTRAINT `fk_label_languagegroup1` FOREIGN KEY (`languagegroup_id`) REFERENCES `languagegroup` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB AUTO_INCREMENT=47 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `label`
--

LOCK TABLES `label` WRITE;
/*!40000 ALTER TABLE `label` DISABLE KEYS */;
INSERT INTO `label` VALUES (8,'login/signin','Sign in','Inloggen','','',1),(9,'login/groupname','Group Name *','Groepsnaam *','','',1),(10,'login/password','Password *','Password *','','',1),(11,'login/playercode','Player Code *','Code Speler *','','',1),(13,'login/button/signin','SIGN IN','INLOGGEN','','',1),(14,'welcome/header','Welcome to Where We Move!','Welkom bij Where We Move!','','',1),(15,'welcome/button/back','BACK','TERUG','','',1),(16,'welcome/button/continue','CONTINUE','VERDER','','',1),(17,'welcome/1/header','At the top of the screen you can see your player information','Bovenaan het scherm staat de spelersinformatie','','',1),(18,'welcome/1/text','From the left to the right you can first see your group number $group$, then your player name $player$. After that you can find the round number $round$, and the preferred house rating which is $rating$. In the second line your yearly income $income$, the satisfaction score you have $satisfaction$, your savings amount $savings$ and the maximum mortgage you have $maxmortgage$. Your spendable wealth is the sum of your savings and your yearly income Hence it is $savings$ + $income$.','Van links naar rechts zie je eerst je groepsnummer $group$, daarna je spelersnaam $player$. Daarna vind je de ronde van het spel $round$, en de gewenste woningwaardering die $rating$ is. In de tweede regel vind je het jaarinkomen $income$, je tevredenheidsscore $satisfaction$, je spaarbedrag $savings$ en de maximale hypotheek die u heeft $maxmortgage$. Uw besteedbaar vermogen is de som van je spaargeld en je jaarinkomen. Het is dus $savings$ + $income$.','','',1),(19,'welcome/2/header','Please select a house to buy','Selecteer een huis om te kopen','','',1),(20,'welcome/2/text','Every round starts with choosing a house, you don\'t need to change your house every round though. Every round the houses which you can choose from can be found on your group table. If you have any questions, please ask your facilitator. Please don\'t choose a house which is already chosen by another player. Your input will always be validated!','Elke ronde begint met het kiezen van een huis, maar je hoeft niet elke ronde van huis te veranderen. Iedere ronde vind je op je groepstafel de huizen waaruit je kunt kiezen. Als je vragen hebt, kan je deze aan de facilitator stellen. Kies geen huis dat al door een andere speler is gekozen. Je keuzes worden altijd gevalideerd!','','',1),(21,'welcome/3/header','Optional: buy a measure to improve your house or buy satisfaction points','Optioneel: koop een maatregel om je huis te verbeteren, of koop tevredenheidspunten','','',1),(22,'welcome/3/text','Next, you can buy a measure to improve your house and reduce the damage you might get if a flood happen. You can also buy satisfaction points or you can save your money for later.','Vervolgens kan je een maatregel kopen om je huis te verbeteren en potenti&euml;schade te verminderen die je zou kunnen oplopen als er een overstroming plaatsvindt. Je kan ook tevredenheidspunten kopen of je geld bewaren voor later.','','',1),(23,'welcome/4/header','Please select two questions to answer','Selecteer de twee vragen om te beantwoorden','','',1),(24,'welcome/4/text','In every round you can select an answers for two different questions. Please, select two questions and answer them.','In elke ronde kun je een antwoord kiezen voor twee verschillende vragen. Selecteer de twee vragen en beantwoord ze.','','',1),(25,'welcome/5/header','What\'s next?','Wat volgt er dan?','','',1),(26,'welcome/5/text','After you have answered the questions, and entered your selections for all other steps, you can click on the \'Next Round\' button when the facilitator says so. When you click on this button, a new round starts. Note that you can not go back to the previous round and that a new round starts when the facilitator says so.','Nadat je de vragen hebt beantwoord en je de selecties voor alle overige stappen hebt ingevoerd, kan je op de knop \'Volgende ronde\' klikken als de facilitator dit aangeeft. Wanneer je op deze knop klikt, begint een nieuwe ronde. Houd er rekening mee dat je niet terug kunt naar de vorige ronde en dat een nieuwe ronde pas begint als de facilitator toestemming geeft.','','',1),(27,'welcome/6/header','Enjoy the game!','Veel plezier met het spel!','','',1),(28,'welcome/6/text','We hope you enjoy the game!','We hopen dat je het spel leuk vindt!','','',1),(29,'welcome/button/finish','FINISH','AFSLUITEN','','',1),(30,'welcome/footer/ready','Are you ready reading? - if yes, you can start the game','Klaar met lezen? Zo ja, dan kan je het spel starten','','',1),(31,'welcome/button/start','START THE GAME!','START HET SPEL!','','',1),(32,'general/header','The Housing Game','The Housing Game','','',1),(33,'house/instructions/header','Please select a house','Selecteer een huis','','',1),(34,'house/instructions/text','This is the house selection page. Please select a house from the list below. You will see the price and the rate of the selected house after making the selection. Please keep in mind that you will pay 10% of the house price as a yearly mortgage payment. And that you cannot select a house which price is higher than the maximum mortgage <import <AccountBalanceIcon /> + your savings <WalletIcon />.','Dit is de huizenselectiepagina. Selecteer een huis uit de onderstaande lijst. Na het maken van de selectie zie je de prijs en de klasse van de geselecteerde woning. Houd er rekening mee dat je jaarlijks 10% van de woningprijs als hypotheekbetaling betaalt. En dat je geen huis kunt kiezen waarvan de prijs hoger is dan de maximale hypotheek <AccountBalanceIcon /> + je spaargeld <WalletIcon />.','','',1),(35,'house/choice/house','House *','Huis *','','',1),(36,'house/choice/submit','SUBMIT CHOICES','BEVESTIG KEUZES','','',1),(37,'house/choice/skip','SKIP','OVERSLAAN','','',1),(38,'house/price/header','Price','Prijs','','',1),(39,'house/price/text','Yearly Mortgage (payment per round):','Jaarlijkse hypotheek (betaling per ronde):','','',1),(40,'house/rate/header','House Rate:','Waardering huis:','','',1),(41,'house/rate/text','Your satisfaction will be affected by this','Je tevredenheidsscore wordt hierdoor be&iuml;nvloed','','',1),(42,'house/pluvial/header','Pluvial protection:','Bescherming regen:','','',1),(43,'house/pluvial/text','This is the amount of protection you have from pluvial (rain) flooding','Dit is de bescherming die het huis heeft tegen overvloedige regenval','','',1),(44,'house/fluvial/header','Fluvial protection:','Bescherming overstroming:','','',1),(45,'house/fluvial/text','This is the amount of protection you have from fluvial (river) flooding','Dit is de bescherming die het huis heeft tegen overstroming van de rivier','','',1),(46,'error/roundnotstarted','Round did not start yet!','Ronde is nog niet begonnen!','','',1);
/*!40000 ALTER TABLE `label` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `language`
--

DROP TABLE IF EXISTS `language`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `language` (
  `id` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `code` varchar(2) NOT NULL,
  `name` varchar(45) NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `id_UNIQUE` (`id`),
  UNIQUE KEY `code_UNIQUE` (`code`),
  UNIQUE KEY `name_UNIQUE` (`name`)
) ENGINE=InnoDB AUTO_INCREMENT=6 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `language`
--

LOCK TABLES `language` WRITE;
/*!40000 ALTER TABLE `language` DISABLE KEYS */;
INSERT INTO `language` VALUES (1,'EN','English'),(2,'NL','Nederlands');
/*!40000 ALTER TABLE `language` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `languagegroup`
--

DROP TABLE IF EXISTS `languagegroup`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `languagegroup` (
  `id` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `name` varchar(45) NOT NULL,
  `language_id1` int(10) unsigned NOT NULL,
  `language_id2` int(10) unsigned DEFAULT NULL,
  `language_id3` int(10) unsigned DEFAULT NULL,
  `language_id4` int(10) unsigned DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `id_UNIQUE` (`id`),
  UNIQUE KEY `name_UNIQUE` (`name`),
  KEY `fk_languages_language1_idx` (`language_id1`),
  KEY `fk_languages_language2_idx` (`language_id2`),
  KEY `fk_languages_language3_idx` (`language_id3`),
  KEY `fk_languages_language4_idx` (`language_id4`),
  CONSTRAINT `fk_languages_language1` FOREIGN KEY (`language_id1`) REFERENCES `language` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `fk_languages_language2` FOREIGN KEY (`language_id2`) REFERENCES `language` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `fk_languages_language3` FOREIGN KEY (`language_id3`) REFERENCES `language` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `fk_languages_language4` FOREIGN KEY (`language_id4`) REFERENCES `language` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `languagegroup`
--

LOCK TABLES `languagegroup` WRITE;
/*!40000 ALTER TABLE `languagegroup` DISABLE KEYS */;
INSERT INTO `languagegroup` VALUES (1,'default EN-NL',1,2,NULL,NULL);
/*!40000 ALTER TABLE `languagegroup` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `measure`
--

DROP TABLE IF EXISTS `measure`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `measure` (
  `id` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `value` float unsigned NOT NULL DEFAULT 1,
  `playerround_id` int(10) unsigned NOT NULL,
  `measuretype_id` int(10) unsigned NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `id_UNIQUE` (`id`),
  KEY `fk_measure_measuretype1_idx` (`measuretype_id`),
  KEY `fk_measure_playerround1_idx` (`playerround_id`),
  CONSTRAINT `fk_measure_measuretype1` FOREIGN KEY (`measuretype_id`) REFERENCES `measuretype` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `fk_measure_playerround1` FOREIGN KEY (`playerround_id`) REFERENCES `playerround` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `measure`
--

LOCK TABLES `measure` WRITE;
/*!40000 ALTER TABLE `measure` DISABLE KEYS */;
INSERT INTO `measure` VALUES (1,2,76,26),(2,1,76,12);
/*!40000 ALTER TABLE `measure` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `measuretype`
--

DROP TABLE IF EXISTS `measuretype`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `measuretype` (
  `id` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `name` varchar(255) NOT NULL,
  `description` text NOT NULL,
  `price` int(10) unsigned NOT NULL,
  `satisfaction` int(11) NOT NULL,
  `pluvial_protection_level` int(11) NOT NULL,
  `fluvial_protection_level` int(11) NOT NULL,
  `gameversion_id` int(10) unsigned NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `id_UNIQUE` (`id`),
  KEY `fk_measuretype_gameversion1_idx` (`gameversion_id`),
  CONSTRAINT `fk_measuretype_gameversion1` FOREIGN KEY (`gameversion_id`) REFERENCES `gameversion` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB AUTO_INCREMENT=43 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `measuretype`
--

LOCK TABLES `measuretype` WRITE;
/*!40000 ALTER TABLE `measuretype` DISABLE KEYS */;
INSERT INTO `measuretype` VALUES (12,'Green Garden','Creating a green garden space can help your garden soak up more water during long periods of rain. This can help in, e.g., urban areas.\r\n\r\n',20000,2,1,0,3),(14,'Automatic steel walls','Install steel walls which will automatically rise when a flood is detected. This will shut the water out of your house.&#8203;\r\n\r\n\r\n',12000,1,1,0,3),(16,'Install a water pump','A water pump can help you to remove excess water from your basement or garden. It is not fast enough to remove water from an actual flood.\r\n\r\n\r\n',6000,0,1,0,3),(18,'Underground rain barrel','Rain barrels are typically connected to gutter downspouts and collect the runoff from roofs. You can use this stored water for uses such as watering the garden, washing your car or flushing your toilet.',11000,1,1,0,3),(20,'Rise ground level','Rising the ground level of your house is an expensive, but incredibly effective way to protect your house from flood water. ',35000,3,1,2,3),(22,'Self rising bulkhead','A self-rising bulkhead seals passages such as doors and prevents water from entering during a flood. It forms a permanent part of your house and rises automatically when it comes into contact with inflowing water.',8000,0,0,1,3),(24,'Water resistent walls and floors','Choose walling and floors that are not easily damaged by water and will protect the structure underneath. If a flood does happen, this will lessen the damage the water does.',20000,1,1,1,3),(26,'Sandbags','When a flood is happening, you can use these sandbags to protect your doors and house. It is a cheaper alternative, but they cannot be used again once they are used.',3000,0,1,1,3),(35,'Green Garden','Creating a green garden space can help your garden soak up more water during long periods of rain. This can help in, e.g., urban areas.\r\n\r\n',20000,2,1,0,5),(36,'Automatic steel walls','Install steel walls which will automatically rise when a flood is detected. This will shut the water out of your house.&#8203;\r\n\r\n\r\n',12000,1,1,0,5),(37,'Install a water pump','A water pump can help you to remove excess water from your basement or garden. It is not fast enough to remove water from an actual flood.\r\n\r\n\r\n',6000,0,1,0,5),(38,'Underground rain barrel','Rain barrels are typically connected to gutter downspouts and collect the runoff from roofs. You can use this stored water for uses such as watering the garden, washing your car or flushing your toilet.',11000,1,1,0,5),(39,'Rise ground level','Rising the ground level of your house is an expensive, but incredibly effective way to protect your house from flood water. ',35000,3,1,2,5),(40,'Self rising bulkhead','A self-rising bulkhead seals passages such as doors and prevents water from entering during a flood. It forms a permanent part of your house and rises automatically when it comes into contact with inflowing water.',8000,0,0,1,5),(41,'Water resistent walls and floors','Choose walling and floors that are not easily damaged by water and will protect the structure underneath. If a flood does happen, this will lessen the damage the water does.',20000,1,1,1,5),(42,'Sandbags','When a flood is happening, you can use these sandbags to protect your doors and house. It is a cheaper alternative, but they cannot be used again once they are used.',3000,0,1,1,5);
/*!40000 ALTER TABLE `measuretype` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `newseffects`
--

DROP TABLE IF EXISTS `newseffects`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `newseffects` (
  `id` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `name` varchar(45) NOT NULL,
  `house_discount_euros` tinyint(4) NOT NULL DEFAULT 0,
  `house_discount_percent` tinyint(4) NOT NULL DEFAULT 0,
  `house_discount_year1` int(10) unsigned NOT NULL DEFAULT 0 COMMENT 'House discount year 1 after flooding, in Euros or as a percentage',
  `house_discount_year2` int(10) unsigned NOT NULL DEFAULT 0 COMMENT 'House discount year 2 after flooding, in Euros or as a percentage',
  `house_discount_year3` int(10) unsigned NOT NULL DEFAULT 0 COMMENT 'House discount year 3 after flooding, in Euros or as a percentage',
  `pluvial_protection_change` int(11) NOT NULL DEFAULT 0,
  `fluvial_protection_change` int(11) NOT NULL DEFAULT 0,
  `tax_change` float NOT NULL DEFAULT 0,
  `satisfaction_living_bonus` int(11) NOT NULL DEFAULT 0,
  `satisfaction_move_change` int(11) NOT NULL DEFAULT 0,
  `newsitem_id` int(10) unsigned NOT NULL,
  `community_id` int(10) unsigned DEFAULT NULL COMMENT 'When community_id is NOT filled, all communities are affected equally.',
  PRIMARY KEY (`id`),
  UNIQUE KEY `id_UNIQUE` (`id`),
  KEY `fk_newseffects_newsitem1_idx` (`newsitem_id`),
  KEY `fk_newseffects_community1_idx` (`community_id`),
  CONSTRAINT `fk_newseffects_community1` FOREIGN KEY (`community_id`) REFERENCES `community` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `fk_newseffects_newsitem1` FOREIGN KEY (`newsitem_id`) REFERENCES `newsitem` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB AUTO_INCREMENT=41 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `newseffects`
--

LOCK TABLES `newseffects` WRITE;
/*!40000 ALTER TABLE `newseffects` DISABLE KEYS */;
INSERT INTO `newseffects` VALUES (1,'House discounts 10-5-2',1,0,10000,5000,2000,0,0,0,0,0,15,NULL),(2,'House discounts 10-5-2',1,0,10000,5000,2000,0,0,0,0,0,22,NULL),(3,'Rain effects',0,0,0,0,0,-2,-1,0,0,0,23,NULL),(4,'Rain effects',0,0,0,0,0,-2,-1,0,0,0,16,NULL),(5,'Taxes Natucity +5',0,0,0,0,0,0,0,5,0,0,17,7),(6,'Taxes Natucity +5',0,0,0,0,0,0,0,5,0,0,24,7),(7,'NbS satisfaction',0,0,0,0,0,0,0,0,2,2,25,7),(8,'NbS satisfaction',0,0,0,0,0,0,0,0,2,2,18,7),(33,'House discounts 10-5-2',1,0,10000,5000,2000,0,0,0,0,0,69,NULL),(34,'Rain effects',0,0,0,0,0,-2,-1,0,0,0,70,NULL),(35,'Taxes Natucity +5',0,0,0,0,0,0,0,5,0,0,71,13),(36,'NbS satisfaction',0,0,0,0,0,0,0,0,2,2,72,13),(37,'House discounts 10-5-2',1,0,10000,5000,2000,0,0,0,0,0,78,NULL),(38,'Rain effects',0,0,0,0,0,-2,-1,0,0,0,79,NULL),(39,'Taxes Natucity +5',0,0,0,0,0,0,0,5,0,0,80,13),(40,'NbS satisfaction',0,0,0,0,0,0,0,0,2,2,81,13);
/*!40000 ALTER TABLE `newseffects` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `newsitem`
--

DROP TABLE IF EXISTS `newsitem`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `newsitem` (
  `id` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `name` varchar(255) NOT NULL,
  `summary` text NOT NULL,
  `content` text NOT NULL,
  `round_id` int(10) unsigned NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `id_UNIQUE` (`id`),
  KEY `fk_newsitem_round1_idx` (`round_id`),
  CONSTRAINT `fk_newsitem_round1` FOREIGN KEY (`round_id`) REFERENCES `round` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB AUTO_INCREMENT=82 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `newsitem`
--

LOCK TABLES `newsitem` WRITE;
/*!40000 ALTER TABLE `newsitem` DISABLE KEYS */;
INSERT INTO `newsitem` VALUES (12,'0a: Introduction','Welcome to Where we Move, a serious game that analyses the choices made by participants. All results will be anonymised.','Welcome to Where we Move, this is a serious game involving houses, floodings and various measures you can take. Today we will play this game which will allow you to explore options on how you could protect your house and yourself. During the game, we will ask you to fill in some data sheets that will allow us to conduct some research on the game. It helps us analyse which choices were made during the game and your feelings regarding floodings. These forms will be anonymized and analysed later. But please know that there are no wrong answers or choices, we just want to know what you would do in the game, not test you. Still, take some care while filling in the forms during the game so that we can gather the data and use it in our research.',13),(13,'0b: Introduction','Find your House: increase your satisfaction by finding your ideal house in one of the three communities.','Before we start, let me introduce you to the area we will be playing in. As you can see on the table, we have three communities on the map. We have a dark green, nature-filled area that is protected from the river with a nature-based solution. This means that there is less space for houses. We have an urban area, a cityscape, which is protected from the river by an artificial dike. Lastly, we have a farmland area directly connected to the river. Each of these areas provides a certain amount of protection against river and rain flooding. You will represent a household living in one of these three areas. Which one that is, is up to you. In a little moment, you will be able to purchase your house. The goal of the game is to increase your satisfaction as much as possible. The house you choose influences your satisfaction. If you don\'t manage to buy your dream house right away, you will be allowed to move in the following rounds. We hope you can find and buy your dream house, keep your feet dry and have some money left to increase your satisfaction and pay off your house. I will now explain the rules of the game.\r\n\r\n',13),(14,'1: Private measures','How a household can protect itself: More floods appear in the Netherlands, be prepared on how it might hit you.','As you might have seen, households have the possibility to purchase private flooding measures to increase their protection against floods. If the house of a household is not protected and the area is flooded, the house will take damage and the household will lose satisfaction. Households can protect against two types of flooding: River and Rain flooding. Rain flooding happens when rain falls down in large quantities for a long period of time. This causes strain on the sewage system and results in damage. River flooding happens when a lot of excess water from neighbouring countries is brought back to our country and causes the riverbanks to be overtopped. A river overtopping has a large impact, larger than rain flooding. Streets can become rivers and gardens can become temporary pools. To see how much each private measure protects against the two types of flooding, households can look at the overview made by the government. (explain that prices are about 3x/5x more expensive for game balance, real prices on the facilitator sheet).\r\n\r\n',7),(15,'2: House discounts','Floods result in house discounts: When an area was flooded, houses decrease in value: 10k in year 1, 5k in year 2, and 2k in year 3.\r\n','Unfortunately, we see floods happen more often. The world is preparing itself for the water to come. We see the sea level rising every year which means that in the future it could overtop the dikes and riverbanks that keep our feet dry.\r\n\r\nWhen an area is flooded it not only can cause problems when the water is there, but also afterwards. Repairs are needed if houses are damaged, but also potential buyers are discouraged from buying any homes in the area. This means that the house value of a house decreases, resulting in less profit if the house is sold.\r\n\r\nWe do see, however, that in a couple of years, the financial impact of a flood on house value decreases and disappears eventually. \r\n\r\n(Explain house discount rules)\r\n\r\n\r\n',8),(16,'3: More rain','More rain on the way: More heavy rainfall over the next years will result in flooding of nature-based solutions. Protection for rain floods in Natucity decrease by 2, and for river floods by 1.','In 2021, in the Netherlands and neighbouring countries, a devastating flood happened in Limburg. In that year, we had a record amount of rain, and the amount of short heavy rains increased. In urban areas, the ground has more difficulty soaking up all this rain, resulting in flooded basements and parks turning into swamps. Nature-based solutions and more green areas such as farmlands in the neighbourhoods can help. But can it help enough? More and more people are looking into ways to keep the water outside their homes or to create better-protected homes against water that does enter.\r\n\r\n<b>Now, the protection level for Rain floods by nature-based solutions, dikes and farmlands decreases by 2 and the protection level for River floods by nature-based solutions, dikes and farmlands decrease by 1.</b>\r\n\r\n',9),(17,'4: Taxes increase','Taxes increase in nature-based solution areas: Over the last years demand in nature-based solution areas has skyrocketed, therefore all taxes in Natucity increase with 5k/year.','Areas that are protected by nature-based solutions are known to enhance water quality, reduce air pollution, and conserve natural habitats. This, in turn, results in human well-being and biodiversity benefits. However, due to the increasing demand in such areas, citizenship increases. As a result, such areas are forced to increase taxes in order to maintain the area.\r\n\r\nFrom this round, the taxes of the nature-based solution area for each household number increases with 5k per year.\r\n\r\n\r\n',10),(18,'5: NbS','The beauty of nature-based solutions: Nature-based solutions can offer more than protection against floodings. Therefore you receive a one-time bonus satisfaction of 2 points if you live in Natucity, and you gain 1 satisfaction point in you move there (instead of losing 1 point for moving).','As you might have seen, one of the public measures is presented as a nature-based solution. These solutions make use of, well, nature. It often gives the river space to expand when needed. This means that whenever the river doesn\'t need it, these spaces can be used by farmers to let their cattle grace, or by people to walk, sport and relax around in. Just make sure you get out of the nature-based solution area when the river level rises. But at that point, it\'s good to stay inside anyway.\r\n\r\nBecause of the multifunctional aspect of nature-based solutions, an increasing amount of people enjoy living around these solutions. If you live around the nature-based solution area, you can increase your satisfaction score by 2. If a new household relocates to the nature-based solution area it will also gain 2 satisfaction points. (Thus, relocating to this area means they gain 1 satisfaction, normally relocating costs 1 satisfaction).\r\n\r\n',11),(19,'0a: Introduction','Welcome to Where we Move, a serious game that analyses the choices made by participants. All results will be anonymised.','Welcome to Where we Move, this is a serious game involving houses, floodings and various measures you can take. Today we will play this game which will allow you to explore options on how you could protect your house and yourself. During the game, we will ask you to fill in some data sheets that will allow us to conduct some research on the game. It helps us analyse which choices were made during the game and your feelings regarding floodings. These forms will be anonymized and analysed later. But please know that there are no wrong answers or choices, we just want to know what you would do in the game, not test you. Still, take some care while filling in the forms during the game so that we can gather the data and use it in our research.\r\n',14),(20,'0b: Introduction','Find your House: increase your satisfaction by finding your ideal house in one of the three communities.','Before we start, let me introduce you to the area we will be playing in. As you can see on the table, we have three communities on the map. We have a dark green, nature-filled area that is protected from the river with a nature-based solution. This means that there is less space for houses. We have an urban area, a cityscape, which is protected from the river by an artificial dike. Lastly, we have a farmland area directly connected to the river. Each of these areas provides a certain amount of protection against river and rain flooding. You will represent a household living in one of these three areas. Which one that is, is up to you. In a little moment, you will be able to purchase your house. The goal of the game is to increase your satisfaction as much as possible. The house you choose influences your satisfaction. If you don\'t manage to buy your dream house right away, you will be allowed to move in the following rounds. We hope you can find and buy your dream house, keep your feet dry and have some money left to increase your satisfaction and pay off your house. I will now explain the rules of the game.\r\n\r\n\r\n',14),(21,'1: Private measures','How a household can protect itself: More floods appear in the Netherlands, be prepared on how it might hit you.','As you might have seen, households have the possibility to purchase private flooding measures to increase their protection against floods. If the house of a household is not protected and the area is flooded, the house will take damage and the household will lose satisfaction. Households can protect against two types of flooding: River and Rain flooding. Rain flooding happens when rain falls down in large quantities for a long period of time. This causes strain on the sewage system and results in damage. River flooding happens when a lot of excess water from neighbouring countries is brought back to our country and causes the riverbanks to be overtopped. A river overtopping has a large impact, larger than rain flooding. Streets can become rivers and gardens can become temporary pools. To see how much each private measure protects against the two types of flooding, households can look at the overview made by the government. (explain that prices are about 3x/5x more expensive for game balance, real prices on the facilitator sheet).\r\n\r\n',15),(22,'2: House discounts','Floods result in house discounts: When an area was flooded, houses decrease in value: 10k in year 1, 5k in year 2, and 2k in year 3.','Unfortunately, we see floods happen more often. The world is preparing itself for the water to come. We see the sea level rising every year which means that in the future it could overtop the dikes and riverbanks that keep our feet dry.\r\n\r\nWhen an area is flooded it not only can cause problems when the water is there, but also afterwards. Repairs are needed if houses are damaged, but also potential buyers are discouraged from buying any homes in the area. This means that the house value of a house decreases, resulting in less profit if the house is sold.\r\n\r\nWe do see, however, that in a couple of years, the financial impact of a flood on house value decreases and disappears eventually. \r\n\r\n(Explain house discount rules)\r\n\r\n\r\n',16),(23,'3: More rain','More rain on the way: More heavy rainfall over the next years will result in flooding of nature-based solutions. Protection for rain floods in Natucity decrease by 2, and for river floods by 1.\r\n','In 2021, in the Netherlands and neighbouring countries, a devastating flood happened in Limburg. In that year, we had a record amount of rain, and the amount of short heavy rains increased. In urban areas, the ground has more difficulty soaking up all this rain, resulting in flooded basements and parks turning into swamps. Nature-based solutions and more green areas such as farmlands in the neighbourhoods can help. But can it help enough? More and more people are looking into ways to keep the water outside their homes or to create better-protected homes against water that does enter.\r\n\r\n<b>Now, the protection level for Rain floods by nature-based solutions, dikes and farmlands decreases by 2 and the protection level for River floods by nature-based solutions, dikes and farmlands decrease by 1.</b>\r\n\r\n\r\n\r\n',17),(24,'4: Taxes increase','Taxes increase in nature-based solution areas: Over the last years demand in nature-based solution areas has skyrocketed, therefore all taxes in Natucity increase with 5k/year.\r\n','Areas that are protected by nature-based solutions are known to enhance water quality, reduce air pollution, and conserve natural habitats. This, in turn, results in human well-being and biodiversity benefits. However, due to the increasing demand in such areas, citizenship increases. As a result, such areas are forced to increase taxes in order to maintain the area.\r\n\r\nFrom this round, the taxes of the nature-based solution area for each household number increases with 5k per year.\r\n\r\n\r\n\r\n',18),(25,'5: NbS','The beauty of nature-based solutions: Nature-based solutions can offer more than protection against floodings. Therefore you receive a one-time bonus satisfaction of 2 points if you live in Natucity, and you gain 1 satisfaction point in you move there (instead of losing 1 point for moving).','As you might have seen, one of the public measures is presented as a nature-based solution. These solutions make use of, well, nature. It often gives the river space to expand when needed. This means that whenever the river doesn\'t need it, these spaces can be used by farmers to let their cattle grace, or by people to walk, sport and relax around in. Just make sure you get out of the nature-based solution area when the river level rises. But at that point, it\'s good to stay inside anyway.\r\n\r\nBecause of the multifunctional aspect of nature-based solutions, an increasing amount of people enjoy living around these solutions. If you live around the nature-based solution area, you can increase your satisfaction score by 2. If a new household relocates to the nature-based solution area it will also gain 2 satisfaction points. (Thus, relocating to this area means they gain 1 satisfaction, normally relocating costs 1 satisfaction).\r\n\r\n',19),(68,'1: Private measures','How a household can protect itself: More floods appear in the Netherlands, be prepared on how it might hit you.','As you might have seen, households have the possibility to purchase private flooding measures to increase their protection against floods. If the house of a household is not protected and the area is flooded, the house will take damage and the household will lose satisfaction. Households can protect against two types of flooding: River and Rain flooding. Rain flooding happens when rain falls down in large quantities for a long period of time. This causes strain on the sewage system and results in damage. River flooding happens when a lot of excess water from neighbouring countries is brought back to our country and causes the riverbanks to be overtopped. A river overtopping has a large impact, larger than rain flooding. Streets can become rivers and gardens can become temporary pools. To see how much each private measure protects against the two types of flooding, households can look at the overview made by the government. (explain that prices are about 3x/5x more expensive for game balance, real prices on the facilitator sheet).\r\n\r\n',56),(69,'2: House discounts','Floods result in house discounts: When an area was flooded, houses decrease in value: 10k in year 1, 5k in year 2, and 2k in year 3.\r\n','Unfortunately, we see floods happen more often. The world is preparing itself for the water to come. We see the sea level rising every year which means that in the future it could overtop the dikes and riverbanks that keep our feet dry.\r\n\r\nWhen an area is flooded it not only can cause problems when the water is there, but also afterwards. Repairs are needed if houses are damaged, but also potential buyers are discouraged from buying any homes in the area. This means that the house value of a house decreases, resulting in less profit if the house is sold.\r\n\r\nWe do see, however, that in a couple of years, the financial impact of a flood on house value decreases and disappears eventually. \r\n\r\n(Explain house discount rules)\r\n\r\n\r\n',57),(70,'3: More rain','More rain on the way: More heavy rainfall over the next years will result in flooding of nature-based solutions. Protection for rain floods in Natucity decrease by 2, and for river floods by 1.','In 2021, in the Netherlands and neighbouring countries, a devastating flood happened in Limburg. In that year, we had a record amount of rain, and the amount of short heavy rains increased. In urban areas, the ground has more difficulty soaking up all this rain, resulting in flooded basements and parks turning into swamps. Nature-based solutions and more green areas such as farmlands in the neighbourhoods can help. But can it help enough? More and more people are looking into ways to keep the water outside their homes or to create better-protected homes against water that does enter.\r\n\r\n<b>Now, the protection level for Rain floods by nature-based solutions, dikes and farmlands decreases by 2 and the protection level for River floods by nature-based solutions, dikes and farmlands decrease by 1.</b>\r\n\r\n',58),(71,'4: Taxes increase','Taxes increase in nature-based solution areas: Over the last years demand in nature-based solution areas has skyrocketed, therefore all taxes in Natucity increase with 5k/year.','Areas that are protected by nature-based solutions are known to enhance water quality, reduce air pollution, and conserve natural habitats. This, in turn, results in human well-being and biodiversity benefits. However, due to the increasing demand in such areas, citizenship increases. As a result, such areas are forced to increase taxes in order to maintain the area.\r\n\r\nFrom this round, the taxes of the nature-based solution area for each household number increases with 5k per year.\r\n\r\n\r\n',59),(72,'5: NbS','The beauty of nature-based solutions: Nature-based solutions can offer more than protection against floodings. Therefore you receive a one-time bonus satisfaction of 2 points if you live in Natucity, and you gain 1 satisfaction point in you move there (instead of losing 1 point for moving).','As you might have seen, one of the public measures is presented as a nature-based solution. These solutions make use of, well, nature. It often gives the river space to expand when needed. This means that whenever the river doesn\'t need it, these spaces can be used by farmers to let their cattle grace, or by people to walk, sport and relax around in. Just make sure you get out of the nature-based solution area when the river level rises. But at that point, it\'s good to stay inside anyway.\r\n\r\nBecause of the multifunctional aspect of nature-based solutions, an increasing amount of people enjoy living around these solutions. If you live around the nature-based solution area, you can increase your satisfaction score by 2. If a new household relocates to the nature-based solution area it will also gain 2 satisfaction points. (Thus, relocating to this area means they gain 1 satisfaction, normally relocating costs 1 satisfaction).\r\n\r\n',60),(73,'0a: Introduction','Welcome to Where we Move, a serious game that analyses the choices made by participants. All results will be anonymised.','Welcome to Where we Move, this is a serious game involving houses, floodings and various measures you can take. Today we will play this game which will allow you to explore options on how you could protect your house and yourself. During the game, we will ask you to fill in some data sheets that will allow us to conduct some research on the game. It helps us analyse which choices were made during the game and your feelings regarding floodings. These forms will be anonymized and analysed later. But please know that there are no wrong answers or choices, we just want to know what you would do in the game, not test you. Still, take some care while filling in the forms during the game so that we can gather the data and use it in our research.',61),(74,'0b: Introduction','Find your House: increase your satisfaction by finding your ideal house in one of the three communities.','Before we start, let me introduce you to the area we will be playing in. As you can see on the table, we have three communities on the map. We have a dark green, nature-filled area that is protected from the river with a nature-based solution. This means that there is less space for houses. We have an urban area, a cityscape, which is protected from the river by an artificial dike. Lastly, we have a farmland area directly connected to the river. Each of these areas provides a certain amount of protection against river and rain flooding. You will represent a household living in one of these three areas. Which one that is, is up to you. In a little moment, you will be able to purchase your house. The goal of the game is to increase your satisfaction as much as possible. The house you choose influences your satisfaction. If you don\'t manage to buy your dream house right away, you will be allowed to move in the following rounds. We hope you can find and buy your dream house, keep your feet dry and have some money left to increase your satisfaction and pay off your house. I will now explain the rules of the game.\r\n\r\n',61),(75,'0a: Introduction','Welcome to Where we Move, a serious game that analyses the choices made by participants. All results will be anonymised.','Welcome to Where we Move, this is a serious game involving houses, floodings and various measures you can take. Today we will play this game which will allow you to explore options on how you could protect your house and yourself. During the game, we will ask you to fill in some data sheets that will allow us to conduct some research on the game. It helps us analyse which choices were made during the game and your feelings regarding floodings. These forms will be anonymized and analysed later. But please know that there are no wrong answers or choices, we just want to know what you would do in the game, not test you. Still, take some care while filling in the forms during the game so that we can gather the data and use it in our research.\r\n',62),(76,'0b: Introduction','Find your House: increase your satisfaction by finding your ideal house in one of the three communities.','Before we start, let me introduce you to the area we will be playing in. As you can see on the table, we have three communities on the map. We have a dark green, nature-filled area that is protected from the river with a nature-based solution. This means that there is less space for houses. We have an urban area, a cityscape, which is protected from the river by an artificial dike. Lastly, we have a farmland area directly connected to the river. Each of these areas provides a certain amount of protection against river and rain flooding. You will represent a household living in one of these three areas. Which one that is, is up to you. In a little moment, you will be able to purchase your house. The goal of the game is to increase your satisfaction as much as possible. The house you choose influences your satisfaction. If you don\'t manage to buy your dream house right away, you will be allowed to move in the following rounds. We hope you can find and buy your dream house, keep your feet dry and have some money left to increase your satisfaction and pay off your house. I will now explain the rules of the game.\r\n\r\n\r\n',62),(77,'1: Private measures','How a household can protect itself: More floods appear in the Netherlands, be prepared on how it might hit you.','As you might have seen, households have the possibility to purchase private flooding measures to increase their protection against floods. If the house of a household is not protected and the area is flooded, the house will take damage and the household will lose satisfaction. Households can protect against two types of flooding: River and Rain flooding. Rain flooding happens when rain falls down in large quantities for a long period of time. This causes strain on the sewage system and results in damage. River flooding happens when a lot of excess water from neighbouring countries is brought back to our country and causes the riverbanks to be overtopped. A river overtopping has a large impact, larger than rain flooding. Streets can become rivers and gardens can become temporary pools. To see how much each private measure protects against the two types of flooding, households can look at the overview made by the government. (explain that prices are about 3x/5x more expensive for game balance, real prices on the facilitator sheet).\r\n\r\n',63),(78,'2: House discounts','Floods result in house discounts: When an area was flooded, houses decrease in value: 10k in year 1, 5k in year 2, and 2k in year 3.','Unfortunately, we see floods happen more often. The world is preparing itself for the water to come. We see the sea level rising every year which means that in the future it could overtop the dikes and riverbanks that keep our feet dry.\r\n\r\nWhen an area is flooded it not only can cause problems when the water is there, but also afterwards. Repairs are needed if houses are damaged, but also potential buyers are discouraged from buying any homes in the area. This means that the house value of a house decreases, resulting in less profit if the house is sold.\r\n\r\nWe do see, however, that in a couple of years, the financial impact of a flood on house value decreases and disappears eventually. \r\n\r\n(Explain house discount rules)\r\n\r\n\r\n',64),(79,'3: More rain','More rain on the way: More heavy rainfall over the next years will result in flooding of nature-based solutions. Protection for rain floods in Natucity decrease by 2, and for river floods by 1.\r\n','In 2021, in the Netherlands and neighbouring countries, a devastating flood happened in Limburg. In that year, we had a record amount of rain, and the amount of short heavy rains increased. In urban areas, the ground has more difficulty soaking up all this rain, resulting in flooded basements and parks turning into swamps. Nature-based solutions and more green areas such as farmlands in the neighbourhoods can help. But can it help enough? More and more people are looking into ways to keep the water outside their homes or to create better-protected homes against water that does enter.\r\n\r\n<b>Now, the protection level for Rain floods by nature-based solutions, dikes and farmlands decreases by 2 and the protection level for River floods by nature-based solutions, dikes and farmlands decrease by 1.</b>\r\n\r\n\r\n\r\n',65),(80,'4: Taxes increase','Taxes increase in nature-based solution areas: Over the last years demand in nature-based solution areas has skyrocketed, therefore all taxes in Natucity increase with 5k/year.\r\n','Areas that are protected by nature-based solutions are known to enhance water quality, reduce air pollution, and conserve natural habitats. This, in turn, results in human well-being and biodiversity benefits. However, due to the increasing demand in such areas, citizenship increases. As a result, such areas are forced to increase taxes in order to maintain the area.\r\n\r\nFrom this round, the taxes of the nature-based solution area for each household number increases with 5k per year.\r\n\r\n\r\n\r\n',66),(81,'5: NbS','The beauty of nature-based solutions: Nature-based solutions can offer more than protection against floodings. Therefore you receive a one-time bonus satisfaction of 2 points if you live in Natucity, and you gain 1 satisfaction point in you move there (instead of losing 1 point for moving).','As you might have seen, one of the public measures is presented as a nature-based solution. These solutions make use of, well, nature. It often gives the river space to expand when needed. This means that whenever the river doesn\'t need it, these spaces can be used by farmers to let their cattle grace, or by people to walk, sport and relax around in. Just make sure you get out of the nature-based solution area when the river level rises. But at that point, it\'s good to stay inside anyway.\r\n\r\nBecause of the multifunctional aspect of nature-based solutions, an increasing amount of people enjoy living around these solutions. If you live around the nature-based solution area, you can increase your satisfaction score by 2. If a new household relocates to the nature-based solution area it will also gain 2 satisfaction points. (Thus, relocating to this area means they gain 1 satisfaction, normally relocating costs 1 satisfaction).\r\n\r\n',67);
/*!40000 ALTER TABLE `newsitem` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `player`
--

DROP TABLE IF EXISTS `player`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `player` (
  `id` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `code` varchar(16) NOT NULL,
  `user_id` int(10) unsigned DEFAULT NULL,
  `group_id` int(10) unsigned NOT NULL,
  `welfaretype_id` int(10) unsigned NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `id_UNIQUE` (`id`),
  KEY `fk_player_user1_idx` (`user_id`),
  KEY `fk_player_group1_idx` (`group_id`),
  KEY `fk_player_welfaretype1_idx` (`welfaretype_id`),
  CONSTRAINT `fk_player_group1` FOREIGN KEY (`group_id`) REFERENCES `group` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `fk_player_user1` FOREIGN KEY (`user_id`) REFERENCES `user` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `fk_player_welfaretype1` FOREIGN KEY (`welfaretype_id`) REFERENCES `welfaretype` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB AUTO_INCREMENT=252 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `player`
--

LOCK TABLES `player` WRITE;
/*!40000 ALTER TABLE `player` DISABLE KEYS */;
INSERT INTO `player` VALUES (156,'t1p1',NULL,27,9),(157,'t1p2',NULL,27,13),(158,'t1p3',NULL,27,11),(159,'t1p4',NULL,27,12),(160,'t1p5',NULL,27,10),(161,'t1p6',NULL,27,8),(162,'t1p7',NULL,27,9),(163,'t1p8',NULL,27,13),(164,'t2p1',NULL,28,9),(165,'t2p2',NULL,28,13),(166,'t2p3',NULL,28,11),(167,'t2p4',NULL,28,12),(168,'t2p5',NULL,28,10),(169,'t2p6',NULL,28,8),(170,'t2p7',NULL,28,9),(171,'t2p8',NULL,28,13),(172,'t3p1',NULL,29,9),(173,'t3p2',NULL,29,13),(174,'t3p3',NULL,29,11),(175,'t3p4',NULL,29,12),(176,'t3p5',NULL,29,10),(177,'t3p6',NULL,29,8),(178,'t3p7',NULL,29,9),(179,'t3p8',NULL,29,13),(180,'t4p1',NULL,30,18),(181,'t4p2',NULL,30,16),(182,'t4p3',NULL,30,17),(183,'t4p4',NULL,30,15),(184,'t4p5',NULL,30,14),(185,'t4p6',NULL,30,19),(186,'t4p7',NULL,30,18),(187,'t4p8',NULL,30,16),(188,'t5p1',NULL,31,18),(189,'t5p2',NULL,31,16),(190,'t5p3',NULL,31,17),(191,'t5p4',NULL,31,15),(192,'t5p5',NULL,31,14),(193,'t5p6',NULL,31,19),(194,'t5p7',NULL,31,18),(195,'t5p8',NULL,31,16),(196,'t6p1',NULL,32,18),(197,'t6p2',NULL,32,16),(198,'t6p3',NULL,32,17),(199,'t6p4',NULL,32,15),(200,'t6p5',NULL,32,14),(201,'t6p6',NULL,32,19),(202,'t6p7',NULL,32,18),(203,'t6p8',NULL,32,16),(204,'t1p1',NULL,33,8),(205,'t1p2',NULL,33,9),(206,'t1p3',NULL,33,10),(207,'t1p4',NULL,33,12),(208,'t1p5',NULL,33,13),(209,'t1p6',NULL,33,11),(210,'t1p7',NULL,33,8),(211,'t1p8',NULL,33,9),(212,'t2p1',NULL,34,8),(213,'t2p2',NULL,34,9),(214,'t2p3',NULL,34,10),(215,'t2p4',NULL,34,12),(216,'t2p5',NULL,34,13),(217,'t2p6',NULL,34,11),(218,'t2p7',NULL,34,8),(219,'t2p8',NULL,34,9),(220,'t3p1',NULL,35,8),(221,'t3p2',NULL,35,9),(222,'t3p3',NULL,35,10),(223,'t3p4',NULL,35,12),(224,'t3p5',NULL,35,13),(225,'t3p6',NULL,35,11),(226,'t3p7',NULL,35,8),(227,'t3p8',NULL,35,9),(228,'t4p1',NULL,36,18),(229,'t4p2',NULL,36,14),(230,'t4p3',NULL,36,16),(231,'t4p4',NULL,36,17),(232,'t4p5',NULL,36,15),(233,'t4p6',NULL,36,19),(234,'t4p7',NULL,36,18),(235,'t4p8',NULL,36,14),(236,'t5p1',NULL,37,18),(237,'t5p2',NULL,37,14),(238,'t5p3',NULL,37,16),(239,'t5p4',NULL,37,17),(240,'t5p5',NULL,37,15),(241,'t5p6',NULL,37,19),(242,'t5p7',NULL,37,18),(243,'t5p8',NULL,37,14),(244,'t6p1',NULL,38,18),(245,'t6p2',NULL,38,14),(246,'t6p3',NULL,38,16),(247,'t6p4',NULL,38,17),(248,'t6p5',NULL,38,15),(249,'t6p6',NULL,38,19),(250,'t6p7',NULL,38,18),(251,'t6p8',NULL,38,14);
/*!40000 ALTER TABLE `player` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `playerround`
--

DROP TABLE IF EXISTS `playerround`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `playerround` (
  `id` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `satisfaction` int(10) unsigned NOT NULL,
  `savings` int(10) unsigned NOT NULL,
  `maximum_mortgage` int(10) unsigned NOT NULL,
  `mortgage` int(10) unsigned NOT NULL,
  `living_costs` int(10) unsigned NOT NULL,
  `income` int(10) unsigned NOT NULL,
  `debt` int(10) unsigned NOT NULL,
  `current_wealth` int(11) NOT NULL,
  `preferred_house_rating` int(10) unsigned NOT NULL,
  `satisfaction_cost_per_point` int(10) unsigned NOT NULL,
  `house_price_sold` int(10) unsigned DEFAULT NULL,
  `house_price_bought` int(10) unsigned DEFAULT NULL,
  `spent_savings_for_buying_house` int(10) unsigned DEFAULT NULL,
  `paid_off_debt` int(10) unsigned DEFAULT NULL,
  `measure_bought` int(10) unsigned DEFAULT NULL,
  `pluvial_damage` int(10) unsigned DEFAULT NULL,
  `fluvial_damage` int(10) unsigned DEFAULT NULL,
  `repaired_damage` int(10) unsigned DEFAULT NULL,
  `satisfaction_point_bought` int(10) unsigned DEFAULT NULL,
  `create_time` timestamp NULL DEFAULT current_timestamp(),
  `moving_reason` text DEFAULT NULL,
  `player_state` varchar(16) NOT NULL DEFAULT 'INIT',
  `house_id` int(10) unsigned DEFAULT NULL,
  `player_id` int(10) unsigned NOT NULL,
  `groupround_id` int(10) unsigned NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `id_UNIQUE` (`id`),
  UNIQUE KEY `id_player_groupround` (`player_id`,`groupround_id`) COMMENT 'A player for a gamesession can only play every groupround once.',
  KEY `fk_playerround_house1_idx` (`house_id`),
  KEY `fk_playerround_player1_idx` (`player_id`),
  KEY `fk_playerround_groupround1_idx` (`groupround_id`),
  CONSTRAINT `fk_playerround_groupround1` FOREIGN KEY (`groupround_id`) REFERENCES `groupround` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `fk_playerround_house1` FOREIGN KEY (`house_id`) REFERENCES `house` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `fk_playerround_player1` FOREIGN KEY (`player_id`) REFERENCES `player` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB AUTO_INCREMENT=85 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `playerround`
--

LOCK TABLES `playerround` WRITE;
/*!40000 ALTER TABLE `playerround` DISABLE KEYS */;
INSERT INTO `playerround` VALUES (76,3,0,0,0,30000,65000,0,0,4,6000,0,0,0,0,0,0,0,0,0,'2023-10-26 15:42:37','Initial move into the house. Player thought for a long time which house to choose.\r\n','INIT',29,156,11),(80,5,5000,0,0,30000,65000,0,5000,4,6000,0,0,0,0,0,0,0,NULL,0,'2023-11-17 09:55:44','','INIT',NULL,172,15),(81,5,80000,0,0,105000,180000,0,80000,8,21000,0,0,0,0,0,0,0,NULL,0,'2023-11-17 10:01:10','','INIT',NULL,173,15),(82,5,50000,200000,0,65000,120000,0,50000,7,13000,0,0,0,0,0,0,0,NULL,0,'2023-11-17 17:42:32','','INIT',NULL,180,16),(83,5,15000,130000,0,40000,80000,0,15000,5,8000,0,0,0,0,0,0,0,NULL,0,'2023-11-17 19:39:59','','INIT',NULL,181,16),(84,5,30000,170000,0,50000,100000,0,30000,6,10000,0,0,0,0,0,0,0,NULL,0,'2023-11-17 19:40:09','','INIT',NULL,182,16);
/*!40000 ALTER TABLE `playerround` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `question`
--

DROP TABLE IF EXISTS `question`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `question` (
  `id` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `question_number` int(10) unsigned NOT NULL,
  `description` text NOT NULL,
  `name` varchar(45) NOT NULL,
  `scenario_id` int(10) unsigned NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `id_UNIQUE` (`id`),
  KEY `fk_question_scenario1_idx` (`scenario_id`),
  CONSTRAINT `fk_question_scenario1` FOREIGN KEY (`scenario_id`) REFERENCES `scenario` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB AUTO_INCREMENT=21 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `question`
--

LOCK TABLES `question` WRITE;
/*!40000 ALTER TABLE `question` DISABLE KEYS */;
INSERT INTO `question` VALUES (1,1,'1. I won\'t get flooded\r\n2. I won\'t get damaged\r\n3. I might suffer minor damage\r\n4. I will suffer minor damage\r\n5. I will get seriously damaged','Question 1',3),(2,2,'1. I fully trust the public measure in my area to protect me\r\n2. I trust the public measure in my area to protect me\r\n3. I\'m inconclusive whether the public measure in my area will protect me\r\n4. I don\'t trust the public measure in my area  to protect me\r\n5. I absolutely don\'t trust the public measure in my area to protect me','Question 2',3),(3,1,'1. I won\'t get flooded\r\n2. I won\'t get damaged\r\n3. I might suffer minor damage\r\n4. I will suffer minor damage\r\n5. I will get seriously damaged','Question 1',4),(4,2,'1. I fully trust the public measure in my area to protect me\r\n2. I trust the public measure in my area to protect me\r\n3. I\'m inconclusive whether the public measure in my area will protect me\r\n4. I don\'t trust the public measure in my area  to protect me\r\n5. I absolutely don\'t trust the public measure in my area to protect me','Question 2',4),(17,1,'1. I won\'t get flooded\r\n2. I won\'t get damaged\r\n3. I might suffer minor damage\r\n4. I will suffer minor damage\r\n5. I will get seriously damaged\r\n','Vraag 1',11),(18,2,'1. I fully trust the public measure in my area to protect me\r\n2. I trust the public measure in my area to protect me\r\n3. I\'m inconclusive whether the public measure in my area will protect me\r\n4. I don\'t trust the public measure in my area  to protect me\r\n5. I absolutely don\'t trust the public measure in my area to protect me\r\n','Vraag 2',11),(19,1,'1. I won\'t get flooded\r\n2. I won\'t get damaged\r\n3. I might suffer minor damage\r\n4. I will suffer minor damage\r\n5. I will get seriously damaged','Question 1',12),(20,2,'1. I fully trust the public measure in my area to protect me\r\n2. I trust the public measure in my area to protect me\r\n3. I\'m inconclusive whether the public measure in my area will protect me\r\n4. I don\'t trust the public measure in my area  to protect me\r\n5. I absolutely don\'t trust the public measure in my area to protect me','Question 2',12);
/*!40000 ALTER TABLE `question` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `questionscore`
--

DROP TABLE IF EXISTS `questionscore`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `questionscore` (
  `id` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `score` int(11) NOT NULL,
  `playerround_id` int(10) unsigned NOT NULL,
  `question_id` int(10) unsigned NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `id_UNIQUE` (`id`),
  UNIQUE KEY `id_playerround_question` (`playerround_id`,`question_id`),
  KEY `fk_questionscore_playerround1_idx` (`playerround_id`),
  KEY `fk_questionscore_question1_idx` (`question_id`),
  CONSTRAINT `fk_questionscore_playerround1` FOREIGN KEY (`playerround_id`) REFERENCES `playerround` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `fk_questionscore_question1` FOREIGN KEY (`question_id`) REFERENCES `question` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB AUTO_INCREMENT=7 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `questionscore`
--

LOCK TABLES `questionscore` WRITE;
/*!40000 ALTER TABLE `questionscore` DISABLE KEYS */;
INSERT INTO `questionscore` VALUES (1,1,76,1),(2,3,76,2);
/*!40000 ALTER TABLE `questionscore` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `round`
--

DROP TABLE IF EXISTS `round`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `round` (
  `id` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `round_number` int(11) NOT NULL,
  `scenario_id` int(10) unsigned NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `id_UNIQUE` (`id`),
  KEY `fk_round_scenario1_idx` (`scenario_id`),
  CONSTRAINT `fk_round_scenario1` FOREIGN KEY (`scenario_id`) REFERENCES `scenario` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB AUTO_INCREMENT=68 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `round`
--

LOCK TABLES `round` WRITE;
/*!40000 ALTER TABLE `round` DISABLE KEYS */;
INSERT INTO `round` VALUES (7,1,3),(8,2,3),(9,3,3),(10,4,3),(11,5,3),(13,0,3),(14,0,4),(15,1,4),(16,2,4),(17,3,4),(18,4,4),(19,5,4),(56,1,11),(57,2,11),(58,3,11),(59,4,11),(60,5,11),(61,0,11),(62,0,12),(63,1,12),(64,2,12),(65,3,12),(66,4,12),(67,5,12);
/*!40000 ALTER TABLE `round` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `scenario`
--

DROP TABLE IF EXISTS `scenario`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `scenario` (
  `id` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `name` varchar(45) NOT NULL,
  `information_amount` int(11) NOT NULL,
  `scenarioparameters_id` int(10) unsigned NOT NULL,
  `gameversion_id` int(10) unsigned NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `id_UNIQUE` (`id`),
  KEY `fk_scenario_scenarioparameters1_idx` (`scenarioparameters_id`),
  KEY `fk_scenario_gameversion1_idx` (`gameversion_id`),
  CONSTRAINT `fk_scenario_gameversion1` FOREIGN KEY (`gameversion_id`) REFERENCES `gameversion` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `fk_scenario_scenarioparameters1` FOREIGN KEY (`scenarioparameters_id`) REFERENCES `scenarioparameters` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB AUTO_INCREMENT=13 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `scenario`
--

LOCK TABLES `scenario` WRITE;
/*!40000 ALTER TABLE `scenario` DISABLE KEYS */;
INSERT INTO `scenario` VALUES (3,'With Info',1,1,3),(4,'Without Info',0,1,3),(11,'Met informatie',1,7,5),(12,'Zonder informatie',0,7,5);
/*!40000 ALTER TABLE `scenario` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `scenarioparameters`
--

DROP TABLE IF EXISTS `scenarioparameters`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `scenarioparameters` (
  `id` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `name` varchar(255) NOT NULL,
  `pluvial_repair_costs` float unsigned NOT NULL,
  `pluvial_satisfaction_penalty` float unsigned NOT NULL,
  `fluvial_repair_costs` float unsigned NOT NULL,
  `fluvial_satisfaction_penalty` float unsigned NOT NULL,
  `satisfaction_debt_penalty` float unsigned NOT NULL,
  `satisfaction_house_rating_change` float unsigned NOT NULL,
  `satisfaction_move_penalty` float unsigned NOT NULL,
  `flood_repair_cost` float unsigned NOT NULL,
  `flood_satisfaction_penalty` float unsigned NOT NULL,
  `mortgage_percentage` float unsigned NOT NULL,
  `default_language_id` int(10) unsigned NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `Id_UNIQUE` (`id`),
  UNIQUE KEY `name_UNIQUE` (`name`),
  KEY `fk_scenarioparameters_language1_idx` (`default_language_id`),
  CONSTRAINT `fk_scenarioparameters_language1` FOREIGN KEY (`default_language_id`) REFERENCES `language` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB AUTO_INCREMENT=8 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `scenarioparameters`
--

LOCK TABLES `scenarioparameters` WRITE;
/*!40000 ALTER TABLE `scenarioparameters` DISABLE KEYS */;
INSERT INTO `scenarioparameters` VALUES (1,'Standard params EN',2,1,1,1,1,1,1,1,1,10,1),(7,'Standard params NL',2,1,1,1,1,1,1,1,1,10,2);
/*!40000 ALTER TABLE `scenarioparameters` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `tax`
--

DROP TABLE IF EXISTS `tax`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `tax` (
  `id` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `name` varchar(45) NOT NULL,
  `minimum_inhabitants` int(10) unsigned NOT NULL,
  `maximum_inhabitants` int(10) unsigned NOT NULL,
  `tax_cost` float unsigned NOT NULL,
  `community_id` int(10) unsigned NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `id_UNIQUE` (`id`),
  KEY `fk_tax_community1_idx` (`community_id`),
  CONSTRAINT `fk_tax_community1` FOREIGN KEY (`community_id`) REFERENCES `community` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB AUTO_INCREMENT=38 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `tax`
--

LOCK TABLES `tax` WRITE;
/*!40000 ALTER TABLE `tax` DISABLE KEYS */;
INSERT INTO `tax` VALUES (11,'Tax1-2',1,2,20000,8),(12,'Tax3-4',3,4,15000,8),(13,'Tax5-10',5,10,25000,8),(14,'Tax1-2',1,2,20000,7),(15,'Tax3',3,3,15000,7),(16,'Tax4-7',4,7,25000,7),(17,'Tax1-2',1,2,20000,9),(18,'Tax3-4',3,4,15000,9),(19,'Tax5-10',5,10,25000,9),(29,'Tax1-2',1,2,20000,13),(30,'Tax3',3,3,15000,13),(31,'Tax4-7',4,7,25000,13),(32,'Tax1-2',1,2,20000,14),(33,'Tax3-4',3,4,15000,14),(34,'Tax5-10',5,10,25000,14),(35,'Tax1-2',1,2,20000,15),(36,'Tax3-4',3,4,15000,15),(37,'Tax5-10',5,10,25000,15);
/*!40000 ALTER TABLE `tax` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `user`
--

DROP TABLE IF EXISTS `user`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `user` (
  `id` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `username` varchar(16) NOT NULL,
  `email` varchar(255) DEFAULT NULL,
  `password` varchar(32) NOT NULL,
  `create_time` timestamp NULL DEFAULT current_timestamp(),
  `administrator` tinyint(4) NOT NULL DEFAULT 0,
  PRIMARY KEY (`id`),
  UNIQUE KEY `id_UNIQUE` (`id`),
  UNIQUE KEY `username_UNIQUE` (`username`)
) ENGINE=InnoDB AUTO_INCREMENT=7 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `user`
--

LOCK TABLES `user` WRITE;
/*!40000 ALTER TABLE `user` DISABLE KEYS */;
INSERT INTO `user` VALUES (3,'ali','a.kahawati@student.tudelft.nl','ali','2023-10-01 18:47:49',1),(4,'juliette','V.J.CortesArevalo@tudelft.nl','juliette','2023-10-06 21:09:02',1),(5,'alexander','a.verbraeck@tudelft.nl','alexander','2023-10-06 21:09:20',1),(6,'lieke','','lieke','2023-11-11 16:03:23',1);
/*!40000 ALTER TABLE `user` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `welfaretype`
--

DROP TABLE IF EXISTS `welfaretype`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `welfaretype` (
  `id` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `name` varchar(255) NOT NULL,
  `initial_satisfaction` int(10) unsigned NOT NULL,
  `initial_money` int(10) unsigned NOT NULL,
  `maximum_mortgage` int(10) unsigned NOT NULL,
  `living_costs` int(10) unsigned NOT NULL,
  `income` int(10) unsigned NOT NULL,
  `satisfaction_cost_per_point` int(10) unsigned NOT NULL,
  `preferred_house_rating` int(10) unsigned NOT NULL,
  `scenario_id` int(10) unsigned NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `id_UNIQUE` (`id`),
  KEY `fk_welfaretype_scenario1_idx` (`scenario_id`),
  CONSTRAINT `fk_welfaretype_scenario1` FOREIGN KEY (`scenario_id`) REFERENCES `scenario` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB AUTO_INCREMENT=69 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `welfaretype`
--

LOCK TABLES `welfaretype` WRITE;
/*!40000 ALTER TABLE `welfaretype` DISABLE KEYS */;
INSERT INTO `welfaretype` VALUES (8,'Welfare 050',5,0,80000,20000,50000,4000,3,3),(9,'Welfare 065',5,5000,110000,30000,65000,6000,4,3),(10,'Welfare 080',5,15000,130000,40000,80000,8000,5,3),(11,'Welfare 100',5,30000,170000,50000,100000,10000,6,3),(12,'Welfare 120',5,50000,200000,65000,120000,13000,7,3),(13,'Welfare 180',5,80000,300000,105000,180000,21000,8,3),(14,'Welfare 050',5,0,80000,20000,50000,4000,3,4),(15,'Welfare 065',5,5000,110000,30000,65000,6000,4,4),(16,'Welfare 080',5,15000,130000,40000,80000,8000,5,4),(17,'Welfare 100',5,30000,170000,50000,100000,10000,6,4),(18,'Welfare 120',5,50000,200000,65000,120000,13000,7,4),(19,'Welfare 180',5,80000,300000,105000,180000,21000,8,4),(56,'Welfare 050',5,0,80000,20000,50000,4000,3,11),(57,'Welfare 065',5,5000,110000,30000,65000,6000,4,11),(58,'Welfare 080',5,15000,130000,40000,80000,8000,5,11),(59,'Welfare 100',5,30000,170000,50000,100000,10000,6,11),(60,'Welfare 120',5,50000,200000,65000,120000,13000,7,11),(61,'Welfare 180',5,80000,300000,105000,180000,21000,8,11),(62,'Welfare 050',5,0,80000,20000,50000,4000,3,12),(63,'Welfare 065',5,5000,110000,30000,65000,6000,4,12),(64,'Welfare 080',5,15000,130000,40000,80000,8000,5,12),(65,'Welfare 100',5,30000,170000,50000,100000,10000,6,12),(66,'Welfare 120',5,50000,200000,65000,120000,13000,7,12),(67,'Welfare 180',5,80000,300000,105000,180000,21000,8,12);
/*!40000 ALTER TABLE `welfaretype` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2023-11-18 13:49:47
