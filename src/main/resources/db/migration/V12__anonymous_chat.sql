CREATE TABLE IF NOT EXISTS `group_chat` (
    `id` bigint(20) NOT NULL AUTO_INCREMENT,
    `name` varchar(22) NOT NULL,
    `occupation_type_id` int NOT NULL,
    `total_users` int NOT NULL,
    `created` datetime NOT NULL,
    PRIMARY KEY (`id`),
    INDEX `fk_group_chat_occupation_type1_idx` (`occupation_type_id` ASC) VISIBLE,
    CONSTRAINT `fk_group_chat_occupation_type1`
        FOREIGN KEY (`occupation_type_id`) REFERENCES `occupation_type` (`id`)
                                        ON DELETE NO ACTION
                                        ON UPDATE NO ACTION
)
ENGINE = InnoDB;

CREATE TABLE IF NOT EXISTS `anonymous_group_chat_user` (
    `id` bigint(20) NOT NULL AUTO_INCREMENT,
    `user_id` int NOT NULL,
    `group_chat_id` int NOT NULL,
    `anonymous_name` varchar(255),
    `anonymous_image` varchar(255),
    PRIMARY KEY (`id`),
    INDEX `fk_anonymous_group_chat_user_user1_idx` (`user_id` ASC) VISIBLE,
    INDEX `fk_anonymous_group_chat_user_group_chat1_idx` (`group_chat_id` ASC) VISIBLE,
    CONSTRAINT `fk_anonymous_group_chat_user_user1`
        FOREIGN KEY (`user_id`) REFERENCES `user` (`id`)
                            ON DELETE NO ACTION
                            ON UPDATE NO ACTION,
    CONSTRAINT `fk_anonymous_group_chat_user_group_chat1`
        FOREIGN KEY (group_chat_id) REFERENCES `group_chat` (`id`)
                            ON DELETE NO ACTION
                            ON UPDATE NO ACTION
)
ENGINE = InnoDB;

CREATE TABLE IF NOT EXISTS `group_chat_message` (
    `id` bigint(20) NOT NULL AUTO_INCREMENT,
    `message` text,
    `created` datetime NOT NULL,
    `anonymous_group_chat_user_id` bigint(20) NOT NULL,
    `group_chat_id` bigint(20) NOT NULL,
    PRIMARY KEY (`id`),
    INDEX `fk_group_chat_message_anonymous_group_chat_user1_idx` (`anonymous_group_chat_user_id` ASC) VISIBLE,
    INDEX `fk_group_chat_message_group_chat1_idx` (`group_chat_id` ASC) VISIBLE,
    CONSTRAINT `fk_group_chat_message_anonymous_group_chat_user1`
        FOREIGN KEY (`anonymous_group_chat_user_id`) REFERENCES `anonymous_group_chat_user` (`id`)
                                                ON DELETE NO ACTION
                                                ON UPDATE NO ACTION,
    CONSTRAINT `fk_group_chat_message_group_chat1`
        FOREIGN KEY (`group_chat_id`) REFERENCES `group_chat` (`id`)
                                                ON DELETE NO ACTION
                                                ON UPDATE NO ACTION
)
ENGINE = InnoDB;

CREATE TABLE IF NOT EXISTS `group_chat_tag` (
    `group_chat_id` int NOT NULL,
    `chat_tag_id` int NOT NULL,
    INDEX `fk_group_chat_tag_group_chat1_idx` (`group_chat_id` ASC) VISIBLE,
    INDEX `fk_group_chat_tag_chat_tag1_idx` (`chat_tag_id` ASC) VISIBLE,
    CONSTRAINT `fk_group_chat_tag_group_chat1`
        FOREIGN KEY (`group_chat_id`) REFERENCES `group_chat` (`id`)
                                            ON DELETE NO ACTION
                                            ON UPDATE NO ACTION,
    CONSTRAINT `fk_group_chat_tag_chat_tag1`
        FOREIGN KEY (`chat_tag_id`) REFERENCES `chat_tag` (`id`)
                                            ON DELETE NO ACTION
                                            ON UPDATE NO ACTION
)
ENGINE = InnoDB;

CREATE TABLE IF NOT EXISTS `chat_tag` (
    `id` bigint(20) NOT NULL,
    `name` varchar(255) NOT NULL UNIQUE,
    PRIMARY KEY (`id`)
)
ENGINE = InnoDB;