-- -----------------------------------------------------
-- Table `occupation_type`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `occupation_type` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `name` VARCHAR(255) NOT NULL,
  PRIMARY KEY (`id`))
ENGINE = InnoDB;


--
-- Alter user table
--
ALTER TABLE `user` ADD COLUMN occupation_type_id bigint(20) AFTER `id`;


--
-- Insert data
--
INSERT INTO occupation_type (name)
VALUES
	("Accounting"),
	("Administration & Office Support"),
	("Advertising, Arts & Media"),
  ("Banking & Financial Services"),
  ("Call Centre & Customer Service"),
  ("CEO & General Management"),
  ("Community Services & Development"),
  ("Construction"),
  ("Consulting & Strategy"),
  ("Design & Architecture "),
  ("Education & Training"),
  ("Engineering"),
  ("Farming, Animals & Conservation"),
  ("Government & Defence"),
  ("Healthcare & Medical"),
  ("Hospitality & Tourism"),
  ("Human Resources & Recruitment"),
  ("Information & Communication Technology"),
  ("Insurance & Superannuation"),
  ("Legal"),
  ("Manufacturing, Transport & Logistics"),
  ("Marketing & Communications"),
  ("Mining, Resources & Energy"),
  ("Real Estate & Property"),
  ("Retail & Consumer Products"),
  ("Sales"),
  ("Science & Technology"),
  ("Self Employment"),
  ("Sport & Recreation"),
  ("Trades & Services"),
  ("Other");
