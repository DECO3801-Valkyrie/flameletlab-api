DROP TABLE IF EXISTS `white_noise`;

CREATE TABLE`white_noise` (
                            `id` bigint(20) NOT NULL AUTO_INCREMENT,
                            `picture` VARCHAR(255) DEFAULT NULL,
                            `listens` bigint(20) DEFAULT NULL,
                            `audio_path` VARCHAR(512) DEFAULT NULL,
                            PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8;

DROP TABLE IF EXISTS `users_white_noise`;

CREATE TABLE `users_white_noise` (
                               `white_noise_id` bigint(20) NOT NULL,
                               `user_id` bigint(20) NOT NULL,
                               `created` DATETIME NOT NULL,
                               PRIMARY KEY (`user_id`,`white_noise_id`),
                               FOREIGN KEY (`user_id`) REFERENCES `user` (`id`),
                               FOREIGN KEY (`white_noise_id`) REFERENCES `white_noise` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;