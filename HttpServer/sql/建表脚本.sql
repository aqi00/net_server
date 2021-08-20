

CREATE SCHEMA `demo` ;


-- drop table `demo`.`audio_info`;
-- 创建音频信息表（仿喜马拉雅的听说书）
CREATE TABLE `demo`.`audio_info` (
  `id` bigint(11) NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `artist` VARCHAR(32) NOT NULL COMMENT '作者',
  `title` VARCHAR(128) NOT NULL COMMENT '标题',
  `desc` VARCHAR(512) NOT NULL COMMENT '描述',
  `cover` VARCHAR(256) NOT NULL COMMENT '封面链接',
  `audio` VARCHAR(256) NOT NULL COMMENT '音频链接',
  `create_time` DATETIME NOT NULL,
  PRIMARY KEY (`id`))
COMMENT = '音频信息表';


-- drop table `demo`.`video_info`;
-- 创建短视频信息表（仿抖音的短视频分享）
CREATE TABLE `demo`.`video_info` (
  `id` bigint(11) NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `date` VARCHAR(10) NOT NULL COMMENT '日期',
  `address` VARCHAR(256) NOT NULL COMMENT '地点',
  `label` VARCHAR(256) NOT NULL COMMENT '标签',
  `desc` VARCHAR(512) NOT NULL COMMENT '描述',
  `cover` VARCHAR(256) NOT NULL COMMENT '封面链接',
  `video` VARCHAR(256) NOT NULL COMMENT '视频链接',
  `create_time` DATETIME NOT NULL,
  PRIMARY KEY (`id`))
COMMENT = '短视频信息表';


-- drop table `demo`.`person_info`;
-- 创建附近人员信息表（仿微信的附近的人）
CREATE TABLE `demo`.`person_info` (
  `id` bigint(11) NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `name` VARCHAR(32) NOT NULL COMMENT '昵称',
  `sex` int(2) NOT NULL COMMENT '性别。0 男；1 女',
  `phone` VARCHAR(15) NOT NULL COMMENT '手机号',
  `love` VARCHAR(32) NOT NULL COMMENT '爱好',
  `face` VARCHAR(256) NOT NULL COMMENT '头像链接',
  `info` VARCHAR(512) NOT NULL COMMENT '发布信息',
  `latitude` double NOT NULL COMMENT '纬度',
  `longitude` double NOT NULL COMMENT '经度',
  `address` VARCHAR(256) NOT NULL COMMENT '地址',
  `create_time` DATE NOT NULL,
  PRIMARY KEY (`id`))
COMMENT = '附近人员信息表';

