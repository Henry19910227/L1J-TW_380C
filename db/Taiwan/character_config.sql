/*
Navicat MySQL Data Transfer

Source Server         : Taiwan Server
Source Server Version : 50140
Source Host           : localhost:3366
Source Database       : l1jtw_db

Target Server Type    : MYSQL
Target Server Version : 50140
File Encoding         : 65001

Date: 2011-02-04 06:56:00
*/

SET FOREIGN_KEY_CHECKS=0;
-- ----------------------------
-- Table structure for character_config
-- ----------------------------
CREATE TABLE `character_config` (
  `object_id` int(10) NOT NULL DEFAULT '0',
  `length` int(10) unsigned NOT NULL DEFAULT '0',
  `data` blob,
  PRIMARY KEY (`object_id`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records 
-- ----------------------------
