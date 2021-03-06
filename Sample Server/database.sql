-- MySQL Script generated by MySQL Workbench
-- Пт 24 мар 2017 06:53:27
-- Model: New Model    Version: 1.0
-- MySQL Workbench Forward Engineering

SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0;
SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0;
SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='TRADITIONAL,ALLOW_INVALID_DATES';

-- -----------------------------------------------------
-- Schema sample_server
-- -----------------------------------------------------
-- Схема для размещения данных сервера.
DROP SCHEMA IF EXISTS `sample_server` ;

-- -----------------------------------------------------
-- Schema sample_server
--
-- Схема для размещения данных сервера.
-- -----------------------------------------------------
CREATE SCHEMA IF NOT EXISTS `sample_server` DEFAULT CHARACTER SET utf8 ;
USE `sample_server` ;

-- -----------------------------------------------------
-- Table `sample_server`.`account`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `sample_server`.`account` ;

CREATE TABLE IF NOT EXISTS `sample_server`.`account` (
  `id` INT NOT NULL AUTO_INCREMENT COMMENT 'The uniq ID of an account.',
  `name` VARCHAR(255) NOT NULL COMMENT 'The user name.',
  `password` VARCHAR(45) NOT NULL COMMENT 'The paasword.',
  PRIMARY KEY (`id`),
  UNIQUE INDEX `name_UNIQUE` (`name` ASC))
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8
COMMENT = 'The accounts table,';


-- -----------------------------------------------------
-- Table `sample_server`.`player_vehicle`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `sample_server`.`player_vehicle` ;

CREATE TABLE IF NOT EXISTS `sample_server`.`player_vehicle` (
  `id` INT NOT NULL AUTO_INCREMENT COMMENT 'The uniq IF of a player vehicle.',
  `player_id` INT NULL,
  `template_id` INT NOT NULL,
  PRIMARY KEY (`id`),
  INDEX `fk_plsyer_vehicle_player1_idx` (`player_id` ASC),
  CONSTRAINT `fk_plsyer_vehicle_player1`
    FOREIGN KEY (`player_id`)
    REFERENCES `sample_server`.`player` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `sample_server`.`player`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `sample_server`.`player` ;

CREATE TABLE IF NOT EXISTS `sample_server`.`player` (
  `id` INT NOT NULL AUTO_INCREMENT,
  `account_id` INT NULL,
  `current_vehicle_id` INT NULL,
  PRIMARY KEY (`id`),
  INDEX `fk_player_account_idx` (`account_id` ASC),
  INDEX `fk_player_player_vehicle1_idx` (`current_vehicle_id` ASC),
  CONSTRAINT `fk_player_account`
    FOREIGN KEY (`account_id`)
    REFERENCES `sample_server`.`account` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_player_player_vehicle1`
    FOREIGN KEY (`current_vehicle_id`)
    REFERENCES `sample_server`.`player_vehicle` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8
COMMENT = 'The players table.';


SET SQL_MODE=@OLD_SQL_MODE;
SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS;
SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS;
