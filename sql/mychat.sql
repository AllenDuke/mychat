/*
Navicat MySQL Data Transfer

Source Server         : localhost_3306
Source Server Version : 80015
Source Host           : localhost:3306
Source Database       : mychat

Target Server Type    : MYSQL
Target Server Version : 80015
File Encoding         : 65001

Date: 2020-07-29 14:13:43
*/

SET FOREIGN_KEY_CHECKS=0;

-- ----------------------------
-- Table structure for friend_request_message
-- ----------------------------
DROP TABLE IF EXISTS `friend_request_message`;
CREATE TABLE `friend_request_message` (
  `user_id` int(20) unsigned NOT NULL,
  `friend_id` int(20) unsigned NOT NULL,
  `content` varchar(255) DEFAULT NULL,
  `apply_time` datetime DEFAULT NULL,
  PRIMARY KEY (`user_id`,`friend_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of friend_request_message
-- ----------------------------

-- ----------------------------
-- Table structure for group
-- ----------------------------
DROP TABLE IF EXISTS `group`;
CREATE TABLE `group` (
  `id` int(20) unsigned NOT NULL AUTO_INCREMENT,
  `name` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL,
  `mem_num` int(20) DEFAULT NULL COMMENT '群成员数',
  `mem_online_num` int(20) DEFAULT NULL COMMENT '群成员在线人数',
  `notice` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL,
  `qrcode` varchar(255) CHARACTER SET utf8 COLLATE utf8_bin DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of group
-- ----------------------------

-- ----------------------------
-- Table structure for group_mem
-- ----------------------------
DROP TABLE IF EXISTS `group_mem`;
CREATE TABLE `group_mem` (
  `user_id` int(20) unsigned NOT NULL,
  `group_id` int(20) unsigned NOT NULL,
  `group_msg_id` int(20) DEFAULT NULL COMMENT '离线前在群中读到第几条消息',
  `notes` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL,
  PRIMARY KEY (`group_id`,`user_id`),
  UNIQUE KEY `mem_id` (`user_id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of group_mem
-- ----------------------------

-- ----------------------------
-- Table structure for group_message
-- ----------------------------
DROP TABLE IF EXISTS `group_message`;
CREATE TABLE `group_message` (
  `id` int(20) unsigned NOT NULL AUTO_INCREMENT,
  `group_id` int(20) unsigned NOT NULL,
  `sender_id` int(20) unsigned NOT NULL,
  `time` datetime DEFAULT CURRENT_TIMESTAMP,
  `content` varchar(1000) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL,
  `location` varchar(1000) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL COMMENT '文件路径或图片路径',
  PRIMARY KEY (`id`),
  KEY `send` (`sender_id`) USING BTREE,
  KEY `group` (`group_id`) USING BTREE,
  KEY `time` (`time`) USING BTREE,
  CONSTRAINT `group_id_groupmessage` FOREIGN KEY (`group_id`) REFERENCES `group` (`id`) ON DELETE CASCADE ON UPDATE RESTRICT,
  CONSTRAINT `sender_id_groupmessage` FOREIGN KEY (`sender_id`) REFERENCES `user` (`id`) ON DELETE CASCADE ON UPDATE RESTRICT
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of group_message
-- ----------------------------

-- ----------------------------
-- Table structure for p2p_message
-- ----------------------------
DROP TABLE IF EXISTS `p2p_message`;
CREATE TABLE `p2p_message` (
  `id` int(20) unsigned NOT NULL AUTO_INCREMENT,
  `send_id` int(20) unsigned NOT NULL,
  `receive_id` int(20) unsigned NOT NULL,
  `location` varchar(255) DEFAULT NULL COMMENT '图片路径或文件路径',
  `content` varchar(1000) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '文字',
  `time` datetime DEFAULT CURRENT_TIMESTAMP,
  `is_read` int(1) DEFAULT NULL COMMENT '1为已读，0为未读',
  PRIMARY KEY (`id`),
  KEY `send_id` (`send_id`) USING BTREE,
  KEY `receive_id` (`receive_id`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of p2p_message
-- ----------------------------

-- ----------------------------
-- Table structure for pyq
-- ----------------------------
DROP TABLE IF EXISTS `pyq`;
CREATE TABLE `pyq` (
  `id` int(20) unsigned NOT NULL AUTO_INCREMENT,
  `sharer_id` int(20) unsigned NOT NULL,
  `content` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL,
  `picture_location` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL,
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP,
  `type` int(1) DEFAULT NULL COMMENT '0为仅自己可见，1为所有人可见',
  PRIMARY KEY (`id`),
  KEY `builder` (`sharer_id`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=6 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of pyq
-- ----------------------------
INSERT INTO `pyq` VALUES ('1', '2', 'test', null, null, null);
INSERT INTO `pyq` VALUES ('2', '2', 'test', null, null, null);
INSERT INTO `pyq` VALUES ('3', '2', 'test', null, null, null);
INSERT INTO `pyq` VALUES ('4', '2', 'test', null, null, null);
INSERT INTO `pyq` VALUES ('5', '2', 'test', null, null, null);

-- ----------------------------
-- Table structure for pyq_comment
-- ----------------------------
DROP TABLE IF EXISTS `pyq_comment`;
CREATE TABLE `pyq_comment` (
  `id` int(20) unsigned NOT NULL AUTO_INCREMENT,
  `pyq_id` int(20) unsigned NOT NULL,
  `user_id` int(20) unsigned DEFAULT NULL,
  `content` varchar(255) DEFAULT NULL,
  `time` datetime DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `authorization` (`pyq_id`),
  KEY `reply` (`user_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of pyq_comment
-- ----------------------------

-- ----------------------------
-- Table structure for pyq_comment_reply
-- ----------------------------
DROP TABLE IF EXISTS `pyq_comment_reply`;
CREATE TABLE `pyq_comment_reply` (
  `id` int(20) unsigned NOT NULL AUTO_INCREMENT,
  `pyq_id` int(20) unsigned NOT NULL,
  `replier_id` int(20) unsigned DEFAULT NULL,
  `replieder_id` int(20) unsigned DEFAULT NULL,
  `content` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL,
  `time` datetime DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `pyq_fk` (`pyq_id`),
  KEY `replier_index` (`replier_id`),
  KEY `replieder_fk` (`replieder_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of pyq_comment_reply
-- ----------------------------

-- ----------------------------
-- Table structure for pyq_like
-- ----------------------------
DROP TABLE IF EXISTS `pyq_like`;
CREATE TABLE `pyq_like` (
  `id` int(20) unsigned NOT NULL AUTO_INCREMENT,
  `user_id` int(20) DEFAULT NULL,
  `pyq_id` int(20) DEFAULT NULL,
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of pyq_like
-- ----------------------------

-- ----------------------------
-- Table structure for user
-- ----------------------------
DROP TABLE IF EXISTS `user`;
CREATE TABLE `user` (
  `id` int(20) unsigned NOT NULL AUTO_INCREMENT,
  `name` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL,
  `chat_num` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL,
  `password` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL,
  `phone_num` bigint(20) unsigned DEFAULT NULL,
  `icon` binary(255) DEFAULT NULL,
  `gender` int(1) DEFAULT NULL COMMENT '0为男，1为女',
  `address` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL COMMENT '地区',
  `sign` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL COMMENT '个性签名',
  PRIMARY KEY (`id`),
  UNIQUE KEY `phone_num` (`phone_num`) USING BTREE,
  UNIQUE KEY `chat_num` (`chat_num`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of user
-- ----------------------------
INSERT INTO `user` VALUES ('2', 'Duke', 'Duke', '123456', null, null, null, null, null);
INSERT INTO `user` VALUES ('4', 'Allen', 'Allen', '123456', null, null, null, null, null);

-- ----------------------------
-- Table structure for user_collection
-- ----------------------------
DROP TABLE IF EXISTS `user_collection`;
CREATE TABLE `user_collection` (
  `id` int(20) unsigned NOT NULL AUTO_INCREMENT,
  `collector_id` int(20) unsigned NOT NULL,
  `content` varchar(255) DEFAULT NULL,
  `location` varchar(255) DEFAULT '' COMMENT '文件路径或图片',
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  KEY `collect` (`collector_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of user_collection
-- ----------------------------

-- ----------------------------
-- Table structure for user_friend
-- ----------------------------
DROP TABLE IF EXISTS `user_friend`;
CREATE TABLE `user_friend` (
  `user_id` int(20) unsigned NOT NULL,
  `friend_id` int(20) unsigned NOT NULL,
  `remark` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL COMMENT '备注',
  `label` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL COMMENT '标签',
  PRIMARY KEY (`user_id`,`friend_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of user_friend
-- ----------------------------
INSERT INTO `user_friend` VALUES ('2', '4', null, null);
INSERT INTO `user_friend` VALUES ('4', '2', null, null);
