package com.ijson.blog.service.model.info;

import com.ijson.blog.dao.entity.UserEntity;
import lombok.Getter;
import lombok.Setter;

import java.util.List;
import java.util.stream.Collectors;


/**
 * desc:
 * version: 6.7
 * Created by cuiyongxu on 2019/8/13 1:47 AM
 */

@Getter
@Setter
public class UserInfo {
    private String userId;
    private String cname;
    private String ename;
    private String email;
    private String twitter;
    private String facebook;
    private String school;
    private String schoolLink;
    private String profession;
    private String beginJobTime;
    private String endJobTime;
    private String weibo;
    private String wechat;
    private String avatar;
    private String roleId;
    private String roleCname;
    private boolean enable;
    private Long createTime;
    private String mobile;
    private String qq;

    public static List<UserInfo> creaetUserList(List<UserEntity> userEntityList) {

        return userEntityList.stream().map(k -> {
            return create(k);
        }).collect(Collectors.toList());
    }


    public static UserInfo create(UserEntity userEntity) {
        UserInfo userInfo = new UserInfo();
        userInfo.setUserId(userEntity.getId());
        userInfo.setCname(userEntity.getCname());
        userInfo.setEmail(userEntity.getEmail());
        userInfo.setTwitter(userEntity.getTwitter());
        userInfo.setFacebook(userEntity.getFacebook());
        userInfo.setSchool(userEntity.getSchool());
        userInfo.setSchoolLink(userEntity.getSchoolLink());
        userInfo.setProfession(userEntity.getProfession());
        userInfo.setWechat(userEntity.getWechat());
        userInfo.setWeibo(userEntity.getWeibo());
        userInfo.setAvatar(userEntity.getAvatar());
        userInfo.setEname(userEntity.getEname());
        userInfo.setRoleCname(userEntity.getRoleCname());
        userInfo.setEnable(userEntity.isEnable());
        userInfo.setCreateTime(userEntity.getCreateTime());
        userInfo.setBeginJobTime(userEntity.getBeginJobTime());
        userInfo.setEndJobTime(userEntity.getEndJobTime());
        userInfo.setMobile(userEntity.getMobile());
        userInfo.setQq(userEntity.getQq());
        userInfo.setRoleId(userEntity.getRoleId());
        return userInfo;
    }
}
