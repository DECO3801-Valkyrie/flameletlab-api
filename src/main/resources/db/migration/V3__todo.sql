CREATE TABLE IF NOT EXISTS `todo` (
    `id` bigint(20) NOT NULL AUTO_INCREMENT,
    `name` VARCHAR(255) NULL,
    `done` TINYINT(1) DEFAULT 0,
    `created` DATETIME NOT NULL,
    `date_completed` DATETIME NULL,
    `user_id` bigint(20) NOT NULL,
    PRIMARY KEY (`id`),
    INDEX `fk_todo_user1_idx` (`user_id` ASC) VISIBLE,
    CONSTRAINT `fk_todo_user1`
    FOREIGN KEY (`user_id`)
    REFERENCES `user` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
    ENGINE = InnoDB;