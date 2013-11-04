use messagebox;

CREATE TABLE `message_meta` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `arrived` datetime DEFAULT NULL,
  `correlationId` varchar(255) DEFAULT NULL,
  `messageBodySize` bigint(20) NOT NULL,
  `serviceContract` varchar(255) NOT NULL,
  `sourceSystem` varchar(255) DEFAULT NULL,
  `status` varchar(255) NOT NULL,
  `targetOrganization` varchar(255) NOT NULL,
  `targetSystem` varchar(255) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=522 DEFAULT CHARSET=utf8;

CREATE TABLE `message_body` (
  `id` bigint(20) NOT NULL,
  `text` longtext NOT NULL,
  PRIMARY KEY (`id`),
  FOREIGN KEY (id) REFERENCES message_meta(id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

CREATE TABLE `statistic` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `canonicalDayTime` bigint(20) NOT NULL,
  `deliveryCount` int(11) NOT NULL,
  `maxWaitTimeMs` bigint(20) NOT NULL,
  `serviceContract` varchar(255) NOT NULL,
  `targetOrganization` varchar(255) NOT NULL,
  `targetSystem` varchar(255) NOT NULL,
  `totalWaitTimeMs` bigint(20) NOT NULL,
  `totalSize` bigint(20) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=63 DEFAULT CHARSET=utf8;
