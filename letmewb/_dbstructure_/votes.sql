CREATE TABLE IF NOT EXISTS `votes` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `weibouserid` int(11) NOT NULL COMMENT 'the same with id in weibo_users',
  `clientid` int(11) NOT NULL,
  `time` datetime NOT NULL COMMENT 'means the time the last vote happend',
  `type` tinyint(4) NOT NULL COMMENT '1 like, -1 dislike, and means type the last vote happened',
  `likes` int(11) NOT NULL DEFAULT '0',
  `dislikes` int(11) NOT NULL DEFAULT '0',
  PRIMARY KEY (`id`),
  UNIQUE KEY `pictureid` (`weibouserid`,`clientid`)
)