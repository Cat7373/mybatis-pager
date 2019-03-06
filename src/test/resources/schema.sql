-- ----------------------------
-- Table structure for pager_test
-- ----------------------------
DROP TABLE IF EXISTS `pager_test`;
CREATE TABLE `pager_test` (
  `id` int(11) NOT NULL,
  `name` char(20) NOT NULL COMMENT '名称',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of pager_test
-- ----------------------------
INSERT INTO `pager_test` (`id`, `name`) VALUES
  (1,  'node-1'),
  (2,  'node-2'),
  (3,  'node-3'),
  (4,  'node-4'),
  (5,  'node-5'),
  (6,  'node-6'),
  (7,  'node-7'),
  (8,  'node-8'),
  (9,  'node-9'),
  (10, 'node-10'),
  (11, 'node-11'),
  (12, 'node-12'),
  (13, 'node-13'),
  (14, 'node-14'),
  (15, 'node-15'),
  (16, 'node-16'),
  (17, 'node-17'),
  (18, 'node-18'),
  (19, 'node-19'),
  (20, 'node-20');
