CREATE TABLE IF NOT EXISTS cached_article
(
    `id` bigint(20) NOT NULL AUTO_INCREMENT,
    `title` varchar(255) NOT NULL,
    `url` varchar(255) NOT NULL,
    `url_to_image` varchar(255) NOT NULL,
    PRIMARY KEY (`id`)
)
ENGINE InnoDB;

CREATE TABLE IF NOT EXISTS article_tag (
    `id` bigint(20) NOT NULL AUTO_INCREMENT,
    `tag_name` varchar(255) NOT NULL,
    PRIMARY KEY (`id`)
)
ENGINE InnoDB;

CREATE TABLE IF NOT EXISTS cached_article_tag_pair (
    `article_id` bigint(20) NOT NULL,
    `article_tag_id` bigint(20) NOT NULL,
    INDEX `fk_article_id_idx` (`article_id` ASC) VISIBLE,
    INDEX `fk_article_tag_id_idx` (`article_tag_id` ASC) VISIBLE,
    CONSTRAINT `fk_article_id` FOREIGN KEY (`article_id`)
        REFERENCES `cached_article` (`id`)
        ON DELETE NO ACTION
        ON UPDATE NO ACTION,
    CONSTRAINT `fk_article_tag_id` FOREIGN KEY (`article_tag_id`)
        REFERENCES `article_tag` (`id`)
        ON DELETE NO ACTION
        ON UPDATE NO ACTION
)
ENGINE InnoDB;