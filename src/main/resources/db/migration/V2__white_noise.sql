
DROP TABLE IF EXISTS `white_noise`;

CREATE TABLE`white_noise` (
`id` bigint(20) NOT NULL AUTO_INCREMENT,
`picture` VARCHAR(255) DEFAULT NULL,
`listens` bigint(20) DEFAULT 0,
`length` bigint(20) DEFAULT 0,
`title`   VARCHAR(255) NOT NULL,
`audio_path` VARCHAR(512) NOT NULL,
PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8;

DROP TABLE IF EXISTS `user_white_noise`;

CREATE TABLE `user_white_noise` (
`id` bigint(20) NOT NULL AUTO_INCREMENT,
`white_noise_id` bigint(20) NOT NULL,
`user_id` bigint(20) NOT NULL,
`created` DATETIME NOT NULL,
PRIMARY KEY (`id`),
FOREIGN KEY (`user_id`) REFERENCES `user` (`id`),
FOREIGN KEY (`white_noise_id`) REFERENCES `white_noise` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;


INSERT INTO white_noise (`picture`, `length`, `title`, `audio_path`)
VALUES
("", 198, "Birds", "Birds.mp3"),
("", 122, "Forest", "Forest.mp3"),
("", 222, "Heavy Rain", "Heavy_Rain.mp3"),
("" , 86, "Rain", "Rain.mp3"),
("", 216, "Snow", "Snow.mp3");