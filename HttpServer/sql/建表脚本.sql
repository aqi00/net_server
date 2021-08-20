

CREATE SCHEMA `demo` ;


-- drop table `demo`.`audio_info`;
-- ������Ƶ��Ϣ����ϲ�����ŵ���˵�飩
CREATE TABLE `demo`.`audio_info` (
  `id` bigint(11) NOT NULL AUTO_INCREMENT COMMENT '����ID',
  `artist` VARCHAR(32) NOT NULL COMMENT '����',
  `title` VARCHAR(128) NOT NULL COMMENT '����',
  `desc` VARCHAR(512) NOT NULL COMMENT '����',
  `cover` VARCHAR(256) NOT NULL COMMENT '��������',
  `audio` VARCHAR(256) NOT NULL COMMENT '��Ƶ����',
  `create_time` DATETIME NOT NULL,
  PRIMARY KEY (`id`))
COMMENT = '��Ƶ��Ϣ��';


-- drop table `demo`.`video_info`;
-- ��������Ƶ��Ϣ���¶����Ķ���Ƶ����
CREATE TABLE `demo`.`video_info` (
  `id` bigint(11) NOT NULL AUTO_INCREMENT COMMENT '����ID',
  `date` VARCHAR(10) NOT NULL COMMENT '����',
  `address` VARCHAR(256) NOT NULL COMMENT '�ص�',
  `label` VARCHAR(256) NOT NULL COMMENT '��ǩ',
  `desc` VARCHAR(512) NOT NULL COMMENT '����',
  `cover` VARCHAR(256) NOT NULL COMMENT '��������',
  `video` VARCHAR(256) NOT NULL COMMENT '��Ƶ����',
  `create_time` DATETIME NOT NULL,
  PRIMARY KEY (`id`))
COMMENT = '����Ƶ��Ϣ��';


-- drop table `demo`.`person_info`;
-- ����������Ա��Ϣ����΢�ŵĸ������ˣ�
CREATE TABLE `demo`.`person_info` (
  `id` bigint(11) NOT NULL AUTO_INCREMENT COMMENT '����ID',
  `name` VARCHAR(32) NOT NULL COMMENT '�ǳ�',
  `sex` int(2) NOT NULL COMMENT '�Ա�0 �У�1 Ů',
  `phone` VARCHAR(15) NOT NULL COMMENT '�ֻ���',
  `love` VARCHAR(32) NOT NULL COMMENT '����',
  `face` VARCHAR(256) NOT NULL COMMENT 'ͷ������',
  `info` VARCHAR(512) NOT NULL COMMENT '������Ϣ',
  `latitude` double NOT NULL COMMENT 'γ��',
  `longitude` double NOT NULL COMMENT '����',
  `address` VARCHAR(256) NOT NULL COMMENT '��ַ',
  `create_time` DATE NOT NULL,
  PRIMARY KEY (`id`))
COMMENT = '������Ա��Ϣ��';

