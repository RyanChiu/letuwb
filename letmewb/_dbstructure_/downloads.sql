CREATE TABLE IF NOT EXISTS `downloads` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `srcid` int(11) NOT NULL COMMENT '0:letusee.apk,...',
  `ip` varchar(128) CHARACTER SET ascii COLLATE ascii_bin NOT NULL,
  `dltime` datetime NOT NULL,
  PRIMARY KEY (`id`)
)