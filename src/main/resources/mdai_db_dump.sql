-- MySQL dump 10.13  Distrib 8.0.43, for Linux (x86_64)
--
-- Host: localhost    Database: mdai_db
-- ------------------------------------------------------
-- Server version	8.0.43

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!50503 SET NAMES utf8mb4 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;

--
-- Table structure for table `artista`
--

DROP TABLE IF EXISTS `artista`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `artista` (
  `id_usuario` bigint NOT NULL,
  `biografia` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id_usuario`),
  CONSTRAINT `FK6upqxudyejhuvoe6nq3gltemc` FOREIGN KEY (`id_usuario`) REFERENCES `usuario` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `artista`
--

LOCK TABLES `artista` WRITE;
/*!40000 ALTER TABLE `artista` DISABLE KEYS */;
INSERT INTO `artista` VALUES (1,''),(2,''),(5,''),(6,'');
/*!40000 ALTER TABLE `artista` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `cancion`
--

DROP TABLE IF EXISTS `cancion`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `cancion` (
  `id_cancion` bigint NOT NULL AUTO_INCREMENT,
  `archivo_audio` varchar(255) DEFAULT NULL,
  `duracion` varchar(255) DEFAULT NULL,
  `fecha_subida` datetime(6) DEFAULT NULL,
  `genero` varchar(255) DEFAULT NULL,
  `titulo` varchar(255) NOT NULL,
  `id_artista` bigint NOT NULL,
  PRIMARY KEY (`id_cancion`),
  KEY `FK7o3hjn3jt230ojsygc6drl5fs` (`id_artista`),
  CONSTRAINT `FK7o3hjn3jt230ojsygc6drl5fs` FOREIGN KEY (`id_artista`) REFERENCES `artista` (`id_usuario`)
) ENGINE=InnoDB AUTO_INCREMENT=10 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `cancion`
--

LOCK TABLES `cancion` WRITE;
/*!40000 ALTER TABLE `cancion` DISABLE KEYS */;
INSERT INTO `cancion` VALUES (1,'/uploads/1764365094825_b3dcfc3f-d5ff-48c2-b11c-842bb213e03c_Yankee_Doodle_-_U.S._Navy_Band.mp3','1:08','2025-11-28 21:24:54.846338','Militar','Yanki',1),(2,'/uploads/1764365130766_e21840df-d537-4803-a208-7616b4edd87d_The_Army_Song__aka_The_Army_Goes_Rolling_Along__-_The_U.S._Army_Band.mp3','0:38','2025-11-28 21:25:30.777556','Militar','Army Song',1),(3,'/uploads/1764365183419_1b4a0cd5-82f1-4113-8eb0-637acc8ad85b_Natural_-_Endless_Love.mp3','1:19','2025-11-28 21:26:23.433935','Naturaleza','Nature',1),(4,'/uploads/1764370033385_8c0e68c6-a910-4843-a94f-faca0663d53b_Mad_Science__Sting__-_MK2.mp3','0:14','2025-11-28 22:47:13.405297','Science','SonFor',1),(5,'/uploads/1764370201311_d8f4964a-4bbb-4120-9162-98dbf533b002_Baroque_Coffee_House__Sting__-_Doug_Maxwell_Media_Right_Productions.mp3','0:08','2025-11-28 22:50:01.322360','Pop','Brody',1),(6,'/uploads/1764370481892_d6216db0-7e01-4498-9d0c-4831d059dcec_Baila_Mi_Cumbia__Sting__-_Jimmy_Fontanez_Media_Right_Productions.mp3','0:11','2025-11-28 22:54:41.905177','Cumbia','Me llaman Bravo',2),(7,'/uploads/1764379353283_b5f9a456-1a32-48e8-8816-3723dd6a3971_A_Long_Cold__Sting__-_Riot.mp3','0:14','2025-11-29 01:22:33.302379','rewer','0Sondsw',1),(8,'/uploads/1764414267231_8de6a1c8-d4d3-4e03-b544-09251dfabf58_I_Don_t_Think_So_-_The_Soundlings.mp3','0:50','2025-11-29 11:04:27.249854','infantil','vaca lola',5),(9,'/uploads/1764531273616_af184f20-9916-4213-985f-25de447b3f49_Salute_To_A_New_Beginning_-_United_States_Army_Herald_Trumpets.mp3','0:44','2025-11-30 19:34:33.646804','Rap','Strapp',6);
/*!40000 ALTER TABLE `cancion` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `cancion_playlist`
--

DROP TABLE IF EXISTS `cancion_playlist`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `cancion_playlist` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `orden` int NOT NULL,
  `id_cancion` bigint NOT NULL,
  `id_playlist` bigint NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `UKcxyf53rb7w5vwu5egl77dp66c` (`id_cancion`,`id_playlist`),
  KEY `FK6t7bu825ebau9qrlynbaxwuf` (`id_playlist`),
  CONSTRAINT `FK6t7bu825ebau9qrlynbaxwuf` FOREIGN KEY (`id_playlist`) REFERENCES `playlist` (`id_playlist`),
  CONSTRAINT `FKjvd99uigje6rrrybm59lal8jb` FOREIGN KEY (`id_cancion`) REFERENCES `cancion` (`id_cancion`) ON DELETE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=12 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `cancion_playlist`
--

LOCK TABLES `cancion_playlist` WRITE;
/*!40000 ALTER TABLE `cancion_playlist` DISABLE KEYS */;
INSERT INTO `cancion_playlist` VALUES (1,1,1,2),(2,2,6,2),(3,3,4,2),(4,1,5,4),(5,2,3,4),(6,3,1,4),(7,4,6,4),(8,5,8,4),(9,1,1,1),(10,2,2,1),(11,3,5,1);
/*!40000 ALTER TABLE `cancion_playlist` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `playlist`
--

DROP TABLE IF EXISTS `playlist`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `playlist` (
  `id_playlist` bigint NOT NULL AUTO_INCREMENT,
  `descripcion` varchar(255) DEFAULT NULL,
  `fecha_creacion` datetime(6) DEFAULT NULL,
  `nombre` varchar(255) NOT NULL,
  `id_usuario` bigint DEFAULT NULL,
  PRIMARY KEY (`id_playlist`),
  KEY `FK661qwbrq7rr4xyj61s8ra86oe` (`id_usuario`),
  CONSTRAINT `FK661qwbrq7rr4xyj61s8ra86oe` FOREIGN KEY (`id_usuario`) REFERENCES `usuario` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `playlist`
--

LOCK TABLES `playlist` WRITE;
/*!40000 ALTER TABLE `playlist` DISABLE KEYS */;
INSERT INTO `playlist` VALUES (1,'Mi musica','2025-11-29 01:29:59.850967','MarcoBest',1),(2,'Literalmente lo mejor del artista marco','2025-11-29 01:33:50.979743','Lo mejor de Marco',3),(3,'322','2025-11-29 01:37:05.761459','lo mejor de hebb',3),(4,'infantiles','2025-11-29 10:57:29.791064','osititi',5);
/*!40000 ALTER TABLE `playlist` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `solicitud_verificacion`
--

DROP TABLE IF EXISTS `solicitud_verificacion`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `solicitud_verificacion` (
  `id_solicitud` bigint NOT NULL AUTO_INCREMENT,
  `estado` varchar(255) DEFAULT NULL,
  `fecha_solicitud` datetime(6) DEFAULT NULL,
  `mensaje_verificacion` varchar(255) NOT NULL,
  `id_usuario` bigint DEFAULT NULL,
  PRIMARY KEY (`id_solicitud`),
  KEY `FK7bbiebcffoc7qn4g64v1gxdvu` (`id_usuario`),
  CONSTRAINT `FK7bbiebcffoc7qn4g64v1gxdvu` FOREIGN KEY (`id_usuario`) REFERENCES `usuario` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=7 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `solicitud_verificacion`
--

LOCK TABLES `solicitud_verificacion` WRITE;
/*!40000 ALTER TABLE `solicitud_verificacion` DISABLE KEYS */;
INSERT INTO `solicitud_verificacion` VALUES (1,'APROBADA','2025-11-28 21:24:05.581517','Soy m',1),(2,'APROBADA','2025-11-28 22:51:54.917073','Soy Bravoncio',2),(3,'RECHAZADA','2025-11-29 01:08:08.766284','m',3),(4,'RECHAZADA','2025-11-29 01:19:05.448516','s',3),(5,'APROBADA','2025-11-29 11:01:02.076095','porque soy molona',5),(6,'APROBADA','2025-11-30 19:33:43.186368','yea',6);
/*!40000 ALTER TABLE `solicitud_verificacion` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `usuario`
--

DROP TABLE IF EXISTS `usuario`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `usuario` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `email` varchar(255) NOT NULL,
  `password` varchar(255) NOT NULL,
  `username` varchar(255) NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `UK5171l57faosmj8myawaucatdw` (`email`),
  UNIQUE KEY `UK863n1y3x0jalatoir4325ehal` (`username`)
) ENGINE=InnoDB AUTO_INCREMENT=7 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `usuario`
--

DROP TABLE IF EXISTS `usuario`;
/*!40000 ALTER TABLE `usuario` DISABLE KEYS */;
INSERT INTO `usuario` VALUES (1,'marcoherreraiborra@gmail.com','1234','Marco'),(2,'hugocar_13@hotmail.com','1234','Hebb'),(3,'m@m','1','McFloid'),(4,'p@p','1','Paco'),(5,'mavivepar49@gmail.com','0411','victoriavlzp'),(6,'music@m','1','Musico12');
/*!40000 ALTER TABLE `usuario` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2025-11-30 19:54:51

