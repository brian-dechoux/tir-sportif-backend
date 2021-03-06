CREATE TABLE `userAccount` (
  `id` int  PRIMARY KEY AUTO_INCREMENT,
  `email` varchar(255) NOT NULL,
  `username` varchar(255) NOT NULL,
  `password` varchar(255) NOT NULL,
  `creationDate` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `enabled` boolean NOT NULL,
  `authorityId` int NOT NULL
);

CREATE TABLE `authority` (
  `id` int  PRIMARY KEY AUTO_INCREMENT,
  `label` varchar(255) NOT NULL
);

CREATE TABLE `country` (
  `id` int  PRIMARY KEY AUTO_INCREMENT,
  `code` varchar(255) NOT NULL,
  `name` varchar(255) NOT NULL
);

CREATE TABLE `address` (
  `id` int  PRIMARY KEY AUTO_INCREMENT,
  `street` varchar(255) NOT NULL,
  `number` varchar(255),
  `zip` varchar(255),
  `city` varchar(255) NOT NULL,
  `countryId` int NOT NULL
);

CREATE TABLE `club` (
  `id` int  PRIMARY KEY AUTO_INCREMENT,
  `name` varchar(255) NOT NULL,
  `addressId` int NOT NULL,
  `email` varchar(255)
);

CREATE TABLE `price` (
  `id` int  PRIMARY KEY AUTO_INCREMENT,
  `type` ENUM('LICENSE', 'CHALLENGE'),
  `forLicenseeOnly` boolean,
  `value` double,
  `clubId` int NOT NULL,
  `active` boolean
);

CREATE TABLE `shooter` (
  `id` int  PRIMARY KEY AUTO_INCREMENT,
  `lastname` varchar(255) NOT NULL,
  `firstname` varchar(255) NOT NULL,
  `birthDate` date,
  `clubId` int,
  `email` varchar(255),
  `categoryId` int NOT NULL
);

CREATE TABLE `licensee` (
  `id` int  PRIMARY KEY AUTO_INCREMENT,
  `badgeNumber` int NOT NULL,
  `lockerNumber` int,
  `subscriptionDate` date NOT NULL,
  `addressId` int,
  `shooterId` int NOT NULL
);

CREATE TABLE `category` (
  `id` int  PRIMARY KEY AUTO_INCREMENT,
  `label` varchar(255) NOT NULL,
  `code` varchar(255) NOT NULL,
  `gender` ENUM ('M', 'F'),
  `ageMin` int,
  `ageMax` int
);

CREATE TABLE `discipline` (
  `id` int  PRIMARY KEY AUTO_INCREMENT,
  `label` varchar(255) NOT NULL,
  `code` varchar(255) NOT NULL,
  `nbSeries` int NOT NULL,
  `nbShotsPerSerie` int NOT NULL,
  `isDecimalResult` boolean NOT NULL,
  `minPointsValue` double NOT NULL,
  `maxPointsValue` double NOT NULL
);

CREATE TABLE `categoriesDisciplinesParameters` (
  `id` int  PRIMARY KEY AUTO_INCREMENT,
  `categoryId` int NOT NULL,
  `disciplineId` int NOT NULL,
  `nbSeries` int NOT NULL,
  `nbShotsPerSerie` int NOT NULL
);

CREATE TABLE `challenge` (
  `id` int  PRIMARY KEY AUTO_INCREMENT,
  `name` varchar(255) NOT NULL,
  `startDate` datetime NOT NULL,
  `addressId` int NOT NULL,
  `organiserClubId` int NOT NULL
);

CREATE TABLE `challengeDisciplines` (
  `challengeId` int NOT NULL,
  `disciplineId` int NOT NULL,
  CONSTRAINT `id` PRIMARY KEY (challengeId, disciplineId)
);

CREATE TABLE `challengeCategories` (
  `challengeId` int NOT NULL,
  `categoryId` int NOT NULL,
  CONSTRAINT `id` PRIMARY KEY (challengeId, categoryId)
);

CREATE TABLE `participation` (
  `id` int  PRIMARY KEY AUTO_INCREMENT,
  `shooterId` int NOT NULL,
  `challengeId` int NOT NULL,
  `disciplineId` int NOT NULL,
  `useElectronicTarget` boolean,
  `outRank` boolean
);

CREATE TABLE `shotResult` (
  `serieNumber` int NOT NULL,
  `shotNumber` int NOT NULL,
  `points` double NOT NULL,
  `participationId` int NOT NULL,
  CONSTRAINT `id` PRIMARY KEY (serieNumber, shotNumber, participationId)
);

CREATE TABLE `bill` (
  `id` int  PRIMARY KEY AUTO_INCREMENT,
  `value` double NOT NULL,
  `paid` boolean NOT NULL,
  `creationDate` timestamp,
  `participationId` int,
  `licenseeId` int,
  `priceId` int NOT NULL,
  `paidDate` timestamp
);

ALTER TABLE `user` ADD FOREIGN KEY (`authorityId`) REFERENCES `authority` (`id`);

ALTER TABLE `address` ADD FOREIGN KEY (`countryId`) REFERENCES `country` (`id`);

ALTER TABLE `club` ADD FOREIGN KEY (`addressId`) REFERENCES `address` (`id`);

ALTER TABLE `price` ADD FOREIGN KEY (`clubId`) REFERENCES `club` (`id`);

ALTER TABLE `shooter` ADD FOREIGN KEY (`clubId`) REFERENCES `club` (`id`);
ALTER TABLE `shooter` ADD FOREIGN KEY (`categoryId`) REFERENCES `category` (`id`);

ALTER TABLE `licensee` ADD FOREIGN KEY (`shooterId`) REFERENCES `shooter` (`id`);
ALTER TABLE `licensee` ADD FOREIGN KEY (`addressId`) REFERENCES `address` (`id`);

ALTER TABLE `challenge` ADD FOREIGN KEY (`organiserClubId`) REFERENCES `club` (`id`);

ALTER TABLE `categoriesDisciplinesParameters` ADD FOREIGN KEY (`categoryId`) REFERENCES `category` (`id`);
ALTER TABLE `categoriesDisciplinesParameters` ADD FOREIGN KEY (`disciplineId`) REFERENCES `discipline` (`id`);

ALTER TABLE `challengeDisciplines` ADD FOREIGN KEY (`challengeId`) REFERENCES `challenge` (`id`);
ALTER TABLE `challengeDisciplines` ADD FOREIGN KEY (`disciplineId`) REFERENCES `discipline` (`id`);

ALTER TABLE `challengeCategories` ADD FOREIGN KEY (`challengeId`) REFERENCES `challenge` (`id`);
ALTER TABLE `challengeCategories` ADD FOREIGN KEY (`categoryId`) REFERENCES `category` (`id`);

ALTER TABLE `participation` ADD FOREIGN KEY (`shooterId`) REFERENCES `shooter` (`id`);
ALTER TABLE `participation` ADD FOREIGN KEY (`challengeId`) REFERENCES `challenge` (`id`);
ALTER TABLE `participation` ADD FOREIGN KEY (`disciplineId`) REFERENCES `discipline` (`id`);

ALTER TABLE `bill` ADD FOREIGN KEY (`participationId`) REFERENCES `participation` (`id`);
ALTER TABLE `bill` ADD FOREIGN KEY (`licenseeId`) REFERENCES `licensee` (`id`);
ALTER TABLE `bill` ADD FOREIGN KEY (`priceId`) REFERENCES `price` (`id`);

ALTER TABLE `discipline` ADD CONSTRAINT `minPointsValueLighterThanMaxPointsValueIndex`
CHECK (minPointsValue <= maxPointsValue);

CREATE UNIQUE INDEX `participationDisciplineOnlyOneRankedUniqueIndex`
ON `participation` (challengeId, shooterId, disciplineId, (CASE WHEN outRank = 0 THEN outRank END));

CREATE UNIQUE INDEX `shotResultUniqueIndex`
ON `shotResult` (`serieNumber`, `shotNumber`, `participationId`);
