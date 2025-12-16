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
INSERT INTO `artista` VALUES (12,''),(13,''),(14,''),(15,''),(16,''),(17,''),(18,''),(19,'');
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
) ENGINE=InnoDB AUTO_INCREMENT=56 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `cancion`
--

LOCK TABLES `cancion` WRITE;
/*!40000 ALTER TABLE `cancion` DISABLE KEYS */;
INSERT INTO `cancion` VALUES (20,'/uploads/1765894189612_7bf191fb-0b0e-4575-a002-919ef3cbcd62_Anchors_Aweigh_-_Band_Only_-_The_U.S._Marine_Corps_Band.mp3','1:26','2025-12-16 14:09:49.625937','Militar','Anchors Aweigh',12),(21,'/uploads/1765894248977_f7dd3e5c-a001-4a00-bec2-ef28ab8ecbe8_Salute_To_A_New_Beginning_-_United_States_Army_Herald_Trumpets.mp3','0:44','2025-12-16 14:10:48.991193','Militar','Salute',12),(22,'/uploads/1765894264846_3c873eb8-35d9-407b-a9d6-7af78ad983c9_The_Army_Song__aka_The_Army_Goes_Rolling_Along__-_The_U.S._Army_Band.mp3','0:38','2025-12-16 14:11:04.857330','Militar','The Army',12),(23,'/uploads/1765894285173_a4756e3f-1894-4933-b57f-fd7403ede679_Yankee_Doodle_-_U.S._Navy_Band.mp3','1:08','2025-12-16 14:11:25.188552','Militar','Yankee Doodle',12),(24,'/uploads/1765894414183_7600670b-0f91-4929-b006-cf7843d96e05_I_Don_t_Think_So_-_The_Soundlings.mp3','0:50','2025-12-16 14:13:34.194074','Infantil','I Don\'t Think So',13),(25,'/uploads/1765894500426_0ceddb7e-ba71-4e1b-bfd2-efc60df8a8b0_A_Long_Cold__Sting__-_Riot.mp3','0:14','2025-12-16 14:15:00.439330','Infantil','A Long Cold',13),(26,'/uploads/1765894563444_f761877c-7ee0-41a8-bbe9-1b5b1d876c7d_Good_Starts_-_Jingle_Punks.mp3','1:09','2025-12-16 14:16:03.459780','Infantil','Good Starts',13),(27,'/uploads/1765894577953_182067e4-63f6-42eb-aaa7-30199501390d_Happy_Hour.mp3','1:20','2025-12-16 14:16:17.964723','Infantil','Happy Hour',13),(28,'/uploads/1765894615731_344006b5-d6ab-4016-a860-6cfc72c6bc4a_Upbeat_Kids.mp3','0:55','2025-12-16 14:16:55.744277','Infantil','Upbeat Kids',13),(29,'/uploads/1765906953827_7ccbd6a5-c4c1-4206-8416-5d6fc902180a_Adventure_Intro.mp3','1:36','2025-12-16 17:42:33.862193','Soundtrack','Adventure Intro',14),(30,'/uploads/1765906987194_d7d3913b-1782-45a4-95e8-87a382bf9bd8_Cinema.mp3','2:26','2025-12-16 17:43:07.210699','Soundtrack','Cinema',14),(31,'/uploads/1765907061979_e25738a3-a9a2-46f0-a13c-2779059b1365_Emotional.mp3','2:14','2025-12-16 17:44:21.994226','Soundtrack','Emotional',14),(32,'/uploads/1765907433320_441666cd-f542-4ea8-9bc9-0e390d926096_Dirty_Mac_-_Endless_Love.mp3','1:18','2025-12-16 17:50:33.342562','Rock','Dirty Mac',15),(33,'/uploads/1765907472225_bdda45d1-8481-46d3-b1bc-a85489fe1821_Leslie_s_Strut__Sting__-_John_Deley_and_the_41_Players.mp3','0:09','2025-12-16 17:51:12.249634','Rock','Leslie\'s Strut',15),(34,'/uploads/1765907507716_63749db0-1a14-4925-bc3d-626807bec9ab_O_Chanukah__Vocals__-_Jingle_Punks.mp3','1:19','2025-12-16 17:51:47.729954','Rock','O Chanukah',15),(35,'/uploads/1765907625108_8b4fcefa-d591-4bdd-a70e-d94567f75b21_Allemande__Sting__-_Wahneta_Meixsell.mp3','0:10','2025-12-16 17:53:45.120877','Clásico','Allemande',16),(36,'/uploads/1765907644402_ec94e2ed-5984-423c-854b-3a0547d5ee4d_Aura.mp3','2:50','2025-12-16 17:54:04.416984','Ambiental','Aura',16),(37,'/uploads/1765907671852_465d0c96-fe95-4b81-a866-eac62d103243_Baila_Mi_Cumbia__Sting__-_Jimmy_Fontanez_Media_Right_Productions.mp3','0:11','2025-12-16 17:54:31.864717','Cumbia','Baila Mi Cumbia',16),(38,'/uploads/1765907693088_1d9d023f-ba0a-468d-8d8b-eaefb0994754_Baroque_Coffee_House__Sting__-_Doug_Maxwell_Media_Right_Productions.mp3','0:08','2025-12-16 17:54:53.100315','Clásico','Baroque Cofee',16),(39,'/uploads/1765907713919_e78f68fd-8491-499c-aeec-58047e394e25_Bomber__Sting__-_Riot.mp3','0:13','2025-12-16 17:55:13.931866','Alternativo','Bomber',16),(40,'/uploads/1765907804970_92543d67-d1e3-42de-812c-6e4a5b71c7d4_Church_Bell_Celebration__Sting__-_Doug_Maxwell_Media_Right_Productions.mp3','0:11','2025-12-16 17:56:44.983136','Clásico','Church Vell Celebration',17),(41,'/uploads/1765907822354_772461a7-d5cd-46fb-88bc-9db061e8d49f_Deploy_FInal.mp3','0:59','2025-12-16 17:57:02.366346','Cumbia','Deploy Final',17),(42,'/uploads/1765907845090_120a1962-5c85-495e-9b06-c39774bb6161_Dunnes.mp3','1:37','2025-12-16 17:57:25.104266','Soundtrack','Dunnes',17),(43,'/uploads/1765907892682_3553d3dd-3dea-4d7f-9bc0-dfc2dfd38997_Mad_Science__Sting__-_MK2.mp3','0:14','2025-12-16 17:58:12.696100','Alternativo','Mad Science',17),(44,'/uploads/1765907938863_b2d7cc5f-9d0a-470c-a861-5339ae057459_Natural_-_Endless_Love.mp3','1:19','2025-12-16 17:58:58.880661','Clásico','Natural',17),(45,'/uploads/1765908230390_3112a7ce-76fa-431d-a388-2df2becd82dd_Race_Car_-_Rondo_Brothers.mp3','1:29','2025-12-16 18:03:50.404539','Alternativo','Race Car',18),(46,'/uploads/1765908275144_30330073-a303-484a-a1e5-9f875f41251d_RockBoot.mp3','0:59','2025-12-16 18:04:35.156993','Rock','RockBoot',18),(47,'/uploads/1765908296698_99229557-1df6-46c1-a2c7-f65af89effc6_Tape_Deck_-_Endless_Love.mp3','1:26','2025-12-16 18:04:56.711835','Alternativo','Tape Deck',18),(48,'/uploads/1765908476726_280e2747-7306-45ca-8e6a-ef1818d4945f_Cold.mp3','2:21','2025-12-16 18:07:56.741718','Soundtrack','Cold',18),(49,'/uploads/1765908533282_638e4246-1bfe-4ea6-a6b5-1fc054676bb8_On_My_Way_Home__Sting__-_The_126ers.mp3','0:20','2025-12-16 18:08:53.292447','Country','On My Way Home',18),(50,'/uploads/1765908733353_b973507f-01d8-433d-a765-769af4b71eba_Fairy_Tale.mp3','2:05','2025-12-16 18:12:13.366909','Soundtrack','Fairy Tale',19),(51,'/uploads/1765908930622_b90a8d04-ee52-4cc9-af06-6b6bb10bb3dc_Spanish_Ladies_-_US_Navy_Academy_Men_s_Glee_Club.mp3','2:57','2025-12-16 18:15:30.639231','Militar','Spanish Ladies',19),(52,'/uploads/1765909041777_e0782124-5c14-460a-a176-eab8fe4df851_Call_to_Statesmanship_-_United_States_Army_Herald_Trumpets.mp3','0:38','2025-12-16 18:17:21.792930','Militar','Call to Statesmanship',19),(53,'/uploads/1765909120387_048a479f-5cdc-454c-97d2-8e4b30218108_Dew_on_the_Roses_-_The_Mini_Vandals.mp3','1:35','2025-12-16 18:18:40.400639','Clásico','Dew on the Roses',19),(54,'/uploads/1765909206466_70352102-106b-412a-9057-a2423c3d81c0_The_Road_To_Mordor_-_Ezra_Lipp.mp3','2:14','2025-12-16 18:20:06.481827','Soundtrack','The Road To Mordor',19),(55,'/uploads/1765909247547_ebd3ca87-70dd-4a96-9b83-e963078edbec_Jah_Jah_Bangs_-_Quincas_Moreira.mp3','3:31','2025-12-16 18:20:47.564242','Cumbia','Jah Jah Bangs',19);
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
) ENGINE=InnoDB AUTO_INCREMENT=19 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `cancion_playlist`
--

LOCK TABLES `cancion_playlist` WRITE;
/*!40000 ALTER TABLE `cancion_playlist` DISABLE KEYS */;
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
) ENGINE=InnoDB AUTO_INCREMENT=8 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `playlist`
--

LOCK TABLES `playlist` WRITE;
/*!40000 ALTER TABLE `playlist` DISABLE KEYS */;
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
) ENGINE=InnoDB AUTO_INCREMENT=18 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `solicitud_verificacion`
--

LOCK TABLES `solicitud_verificacion` WRITE;
/*!40000 ALTER TABLE `solicitud_verificacion` DISABLE KEYS */;
INSERT INTO `solicitud_verificacion` VALUES (10,'APROBADA','2025-12-16 14:06:19.209994','Soy el capitán ladrido, quiero subir mis himnos militares.',12),(11,'APROBADA','2025-12-16 14:12:45.827632','Hola! SOy una artista emergente, me fliparía poder compartir mis creaciones!',13),(12,'APROBADA','2025-12-16 17:39:39.189764','Hola, soy un apasionado del cine y de la música.',14),(13,'APROBADA','2025-12-16 17:48:40.778678','Me gusta el rock y quiero compartirlo.',15),(14,'APROBADA','2025-12-16 17:53:01.994919','Quiero subir música variada.',16),(15,'APROBADA','2025-12-16 17:56:05.317229','Me gusta crear y compartir.',17),(16,'APROBADA','2025-12-16 18:03:04.319723','Me encanta crear musica.',18),(17,'APROBADA','2025-12-16 18:11:33.459458','Soy doro, compositor emergente.',19);
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
) ENGINE=InnoDB AUTO_INCREMENT=20 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `usuario`
--

LOCK TABLES `usuario` WRITE;
/*!40000 ALTER TABLE `usuario` DISABLE KEYS */;
INSERT INTO `usuario` VALUES (12,'capi@gamil.com','1','Capitán Ladrido'),(13,'boni@gmail.com','1','Funny Bonnie'),(14,'cine@gmail.com','1','CineArte'),(15,'boy@gmail.com','1','RockBoy'),(16,'rand@gmail.com','1','Randy Artist'),(17,'bil@gamail.com','1','Billy Hated'),(18,'musi@gmail.com','1','Music Lover'),(19,'doro@gmail.com','1','Doro');
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

-- Dump completed on 2025-12-16 19:18:38
