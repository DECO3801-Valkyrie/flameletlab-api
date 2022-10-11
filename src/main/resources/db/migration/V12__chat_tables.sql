-- -----------------------------------------------------
-- Table `group_chat`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `group_chat` (
  `id` BIGINT(20) NOT NULL,
  `name` VARCHAR(255) NOT NULL,
  `occupation_type_id` INT NOT NULL,
  `total_users` INT NOT NULL,
  `created` DATETIME NOT NULL,
  PRIMARY KEY (`id`),
  INDEX `fk_group_chat_occupation_type1_idx` (`occupation_type_id` ASC) VISIBLE)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `chat_tag`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `chat_tag` (
  `id` BIGINT(20) NOT NULL,
  `name` VARCHAR(255) NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE INDEX `name_UNIQUE` (`name` ASC) VISIBLE)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `group_chat_tag`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `group_chat_tag` (
  `group_chat_id` INT NOT NULL,
  `chat_tag_id` INT NOT NULL,
  INDEX `fk_group_chat_tag_group_chat1_idx` (`group_chat_id` ASC) VISIBLE,
  INDEX `fk_group_chat_tag_chat_tag1_idx` (`chat_tag_id` ASC) VISIBLE)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `anonymous_group_chat_user`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `anonymous_group_chat_user` (
  `id` BIGINT(20) NOT NULL AUTO_INCREMENT,
  `user_id` INT NOT NULL,
  `group_chat_id` INT NOT NULL,
  `anonymous_name` VARCHAR(255) NULL,
  `anonymous_image` VARCHAR(255) NULL,
  INDEX `fk_anonymous_group_chat_user_user1_idx` (`user_id` ASC) VISIBLE,
  INDEX `fk_anonymous_group_chat_user_group_chat1_idx` (`group_chat_id` ASC) VISIBLE,
  PRIMARY KEY (`id`))
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `group_chat_message`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `group_chat_message` (
  `id` BIGINT(20) NOT NULL,
  `message` TEXT NULL,
  `created` DATETIME NOT NULL,
  `anonymous_group_chat_user_id` BIGINT(20) NOT NULL,
  `group_chat_id` BIGINT(20) NOT NULL,
  PRIMARY KEY (`id`),
  INDEX `fk_group_chat_message_anonymous_group_chat_user1_idx` (`anonymous_group_chat_user_id` ASC) VISIBLE,
  INDEX `fk_group_chat_message_group_chat1_idx` (`group_chat_id` ASC) VISIBLE)
ENGINE = InnoDB;
