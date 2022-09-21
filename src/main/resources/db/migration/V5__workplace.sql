
DROP TABLE IF EXISTS `workplace` ;

CREATE TABLE `workplace` (
`id` bigint(20) NOT NULL AUTO_INCREMENT,
`place_id` VARCHAR(255) NOT NULL,
`name` VARCHAR(255) NOT NULL,
`location` VARCHAR(255) NOT NULL,
`average_rating` FLOAT NULL,
 PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;