CREATE TABLE IF NOT EXISTS `clients` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `key` varchar(256) NOT NULL,
  `cretime` datetime NOT NULL,
  `time` datetime DEFAULT NULL,
  `times` int(11) NOT NULL,
  PRIMARY KEY (`id`),
  KEY `key` (`key`)
)