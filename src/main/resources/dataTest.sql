DROP DATABASE IF EXISTS paymybuddytest;
CREATE DATABASE paymybuddytest;
USE paymybuddytest;

--
-- Table structure for table `user`
--

DROP TABLE IF EXISTS `user`;
CREATE TABLE `user` (
  `user_id` int NOT NULL AUTO_INCREMENT,
  `last_name` varchar(45) NOT NULL,
  `first_name` varchar(45) NOT NULL,
  `created_dt` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `email` varchar(255) NOT NULL,
  `solde` decimal(10,2) NOT NULL DEFAULT '0.00',
  `password` varchar(255) NOT NULL,
  PRIMARY KEY (`user_id`),
  UNIQUE KEY `email_UNIQUE` (`email`),
  UNIQUE KEY `user_id_UNIQUE` (`user_id`),
  UNIQUE KEY `UK_ob8kqyqqgmefl0aco34akdtpe` (`email`)
) ENGINE=InnoDB AUTO_INCREMENT=16 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;


INSERT INTO USER(last_name, first_name, email, solde, password) VALUES ('TestUserlast_name', 'TestUserfirst_name', 'TestUseremail@gmail.com', 2000, 'TestUserpassword');
INSERT INTO USER(last_name, first_name, email, solde, password) VALUES ('TestUserNeverUselast_name', 'TestUserNeverUsefirst_name', 'TestUserNeverUseemail@gmail.com', 100, 'TestUserNeverUsepassword');
INSERT INTO USER(last_name, first_name, email, solde, password) VALUES ('TestFriendUserlast_name', 'TestFriendUserfirst_name', 'TestFriendUseremail@gmail.com', 1000, 'TestFriendUserpassword');
INSERT INTO USER(last_name, first_name, email, solde, password) VALUES ('TestANewFriendUserlast_name', 'TestANewFriendUserfirst_name', 'TestANewFriendUseremail@gmail.com', 5000, 'TestANewFriendUserpassword');
commit;


--
-- Table structure for table `bank_account`
--

DROP TABLE IF EXISTS `bank_account`;
CREATE TABLE `bank_account` (
  `id` int NOT NULL AUTO_INCREMENT,
  `iban` varchar(255) NOT NULL,
  `description` varchar(255) DEFAULT NULL,
  `user_id` int DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `id_UNIQUE` (`id`),
  KEY `FK92iik4jwhk7q385jubl2bc2mm` (`user_id`),
  CONSTRAINT `FK92iik4jwhk7q385jubl2bc2mm` FOREIGN KEY (`user_id`) REFERENCES `user` (`user_id`)
) ENGINE=InnoDB AUTO_INCREMENT=16 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
INSERT INTO BANK_ACCOUNT(iban, description) VALUES('IBANUserTest', 'Principal Bank of User Test');
Commit;
UPDATE BANK_ACCOUNT SET USER_ID = (SELECT user_id from USER where email = 'TestUseremail@gmail.com') where iban = 'IBANUserTest';
Commit;
--
-- Table structure for table `connected_user`
--

DROP TABLE IF EXISTS `connected_user`;

CREATE TABLE `connected_user` (
  `friend_user_id` int NOT NULL,
  `user_id` int NOT NULL,
  PRIMARY KEY (`friend_user_id`,`user_id`),
  KEY `FK27d9gl9861yi1b5ahxe84o3fe` (`user_id`),
  CONSTRAINT `FK27d9gl9861yi1b5ahxe84o3fe` FOREIGN KEY (`user_id`) REFERENCES `user` (`user_id`),
  CONSTRAINT `FK2mrnebm3lk87wivmchcl61ove` FOREIGN KEY (`friend_user_id`) REFERENCES `user` (`user_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
INSERT INTO CONNECTED_USER select * from
(SELECT user_id FROM USER where email = 'TestFriendUseremail@gmail.com') a,
(SELECT user_id FROM USER where email = 'TestUseremail@gmail.com') b;
commit;
--
-- Table structure for table `transaction`
--

DROP TABLE IF EXISTS `transaction`;
CREATE TABLE `transaction` (
  `user_id_payer` int NOT NULL,
  `user_id_beneficiary` int NOT NULL,
  `description` varchar(255) DEFAULT NULL,
  `amount` decimal(10,2) NOT NULL,
  `created_dt` datetime DEFAULT CURRENT_TIMESTAMP,
  `id` int NOT NULL AUTO_INCREMENT,
  PRIMARY KEY (`id`),
  UNIQUE KEY `id_UNIQUE` (`id`),
  KEY `FKnt3e2jykkdh2wvw9fk9o7j5un` (`user_id_beneficiary`),
  KEY `FK862qq3dgydgo7pjjke78a7312` (`user_id_payer`),
  CONSTRAINT `FK862qq3dgydgo7pjjke78a7312` FOREIGN KEY (`user_id_payer`) REFERENCES `user` (`user_id`),
  CONSTRAINT `FKnt3e2jykkdh2wvw9fk9o7j5un` FOREIGN KEY (`user_id_beneficiary`) REFERENCES `user` (`user_id`)
) ENGINE=InnoDB AUTO_INCREMENT=203 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
INSERT INTO TRANSACTION(user_id_payer, user_id_beneficiary, description, amount) VALUES (
    (SELECT user_id from USER where email = 'TestFriendUseremail@gmail.com'),
    (SELECT user_id from USER where email = 'TestUseremail@gmail.com'),
    'TEST',
    100.00
)