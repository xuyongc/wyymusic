create
    DATABASE `vue-music-master`;

use
    `vue-music-master`;
-- auto-generated definition

drop table if exists user;

create table user
(
    userId       bigint auto_increment comment '用户ID' primary key,
    nickName     varchar(30)                        null comment '用户昵称',
    userAccount  varchar(256)                       not null comment '账户',
    avatarUrl    varchar(1024)                      null comment '头像',
    bgImg        varchar(1024)                      null comment '背景图片',
    gender       tinyint                            null comment '性别',
    userPassword varchar(256)                       not null comment '密码',
    phone        varchar(256)                       null comment '电话号码',
    email        varchar(256)                       null comment '邮箱',
    tag          text                               null comment '标签',
    userStatus   int      default 0                 not null comment '状态 0-正常 1-封号',
    createTime   datetime default CURRENT_TIMESTAMP not null comment '创建时间',
    updateTime   datetime default CURRENT_TIMESTAMP not null on update CURRENT_TIMESTAMP comment '修改时间',
    isDelete     tinyint  default 0                 not null comment '逻辑-1删除 -0存在',
    userRole     int      default 0                 not null comment '0-普通, 1- 作者 2-管理员'
) comment '用户表';

-- auto-generated definition

drop table if exists music;

create table music
(
    musicId             bigint auto_increment comment '音乐Id' primary key,
    musicTitle          varchar(256)                       not null comment '音乐标题',
    musicUserId         bigint                             not null comment '作者Id',
    showImgUrl          varchar(256)                       not null comment '封面地址',
    musicAddress        varchar(256)                       not null comment '作品地址',
    musicWord           text                               not null comment '歌词',
    musicLikeNumber     bigint   default 0                 not null comment '喜欢数',
    musicFavoriteNumber bigint   default 0                 not null comment '收藏数量',
    musicStatus         int      default 1                 not null comment '状态 0-正常 1-审核中 2-下架',
    createTime          datetime default CURRENT_TIMESTAMP not null comment '创建时间',
    updateTime          datetime default CURRENT_TIMESTAMP not null on update CURRENT_TIMESTAMP comment '修改时间',
    isDelete            tinyint  default 0                 not null comment '逻辑-1删除 -0存在'
) comment '歌曲表';



drop table if exists favorite;
-- auto-generated definition
create table favorite
(
    favoritesId bigint auto_increment primary key comment '表ID',
    userId      bigint                             not null comment '用户ID',
    musics      text                               not null comment '音乐Id',
    createTime  datetime default CURRENT_TIMESTAMP not null comment '创建时间',
    updateTime  datetime default CURRENT_TIMESTAMP null on update CURRENT_TIMESTAMP comment '修改时间',
    isDelete    tinyint  default 0                 not null comment '逻辑-1删除 -0存在'
) comment '收藏夹';

drop table if exists follow;

create table follow
(
    followId   bigint primary key auto_increment comment '表ID',
    userId     bigint                             not null comment '用户ID',
    fansId     bigint                             not null comment '粉丝Id',
    createTime datetime default CURRENT_TIMESTAMP not null comment '创建时间',
    updateTime datetime default CURRENT_TIMESTAMP null on update CURRENT_TIMESTAMP comment '修改时间',
    isDelete   tinyint  default 0                 not null comment '逻辑-1删除 -0存在'
) comment '关注表';

drop table if exists comment;

create table comment
(
    commentId  bigint primary key auto_increment comment '表ID',
    otherId    bigint comment '评论地点',
    userId     bigint                             not null comment '用户ID',
    content    varchar(150)                       not null null comment '评论内容',
    parentId   bigint                             null comment '上一级，只有两级',
    createTime datetime default CURRENT_TIMESTAMP null comment '创建时间',
    updateTime datetime default CURRENT_TIMESTAMP null on update CURRENT_TIMESTAMP comment '修改时间',
    isDelete   tinyint  default 0                 not null comment '逻辑-1删除 -0存在'
) comment '评论表';

drop table if exists musicList;

create table musicList
(
    musicListId         bigint primary key auto_increment comment '表ID',
    userId              bigint                             not null comment '作者Id',
    showImgUrl          varchar(256)                       not null comment '封面地址',
    musicListInfo       varchar(256)                       not null comment '歌单评价',
    musicListContentIds text                               not null null comment '歌曲内容地址',
    musicLikeNumber     bigint   default 0                 not null comment '喜欢数',
    musicFavoriteNumber bigint   default 0                 not null comment '收藏数量',
    createTime          datetime default CURRENT_TIMESTAMP null comment '创建时间',
    updateTime          datetime default CURRENT_TIMESTAMP null on update CURRENT_TIMESTAMP comment '修改时间',
    isDelete            tinyint  default 0                 not null comment '逻辑-1删除 -0存在'
) comment '歌单';

drop table if exists community;

create table community
(
    communityId bigint primary key auto_increment comment '表ID',
    userId      bigint                             not null comment '作者Id',
    imgList     text                               not null comment '图片地址',
    musicId     bigint                             null comment '歌曲推荐地址',
    textContent text                               not null comment '动态文字内容',
    createTime  datetime default CURRENT_TIMESTAMP null comment '创建时间',
    updateTime  datetime default CURRENT_TIMESTAMP null on update CURRENT_TIMESTAMP comment '修改时间',
    isDelete    tinyint  default 0                 not null comment '逻辑-1删除 -0存在'
) comment '动态表（社区表）';
