CREATE TABLE IF NOT EXISTS `page_view` (
  `id` int NOT NULL AUTO_INCREMENT,
  `view_type` varchar(10) NOT NULL,
  `count` bigint NOT NULL DEFAULT 0,
  PRIMARY KEY (`id`)
);

INSERT IGNORE INTO `page_view` (`id`, `view_type`, `count`) VALUES (1, 'visitor', 5000);
INSERT IGNORE INTO `page_view` (`id`, `view_type`, `count`) VALUES (2, 'user', 3000);
