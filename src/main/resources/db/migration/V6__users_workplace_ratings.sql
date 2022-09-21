
DROP TABLE IF EXISTS `users_workplace_ratings`;

CREATE TABLE `users_workplace_ratings` (
`id` bigint(20) NOT NULL AUTO_INCREMENT,
`user_id` bigint(20) NOT NULL,
`workplace_id` bigint(20) NOT NULL,
`review` TEXT NOT NULL,
`rating` FLOAT NULL,
 PRIMARY KEY (`id`),
 FOREIGN KEY (`user_id`) REFERENCES `user` (`id`),
 FOREIGN KEY (`workplace_id`) REFERENCES  `workplace` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;