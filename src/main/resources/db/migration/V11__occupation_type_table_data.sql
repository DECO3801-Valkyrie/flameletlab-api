-- -----------------------------------------------------
-- Table `occupation_type`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `occupation_type` (
  `id` INT NOT NULL AUTO_INCREMENT,
  `name` VARCHAR(255) NOT NULL,
  PRIMARY KEY (`id`))
ENGINE = InnoDB;


--
-- Alter user table
--
ALTER TABLE `user` ADD COLUMN occupation_type_id INT AFTER `id`;


--
-- Insert data
--
INSERT INTO occupation_type (name)
VALUES
	("Accounting"),
	("Administration & Office Support"),
	("Advertising, Arts & Media");


