package com.ijson.blog.service.model;

import com.ijson.blog.dao.entity.UserEntity;
import com.ijson.blog.util.DateUtils;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;
import java.util.List;
import java.util.Objects;
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
    private String twitterName;
    private String twitterLink;
    private String facebookName;
    private String facebookLink;
    private String universityName;
    private String universityLink;
    private String professional;
    private String yearsOfWorking;
    private String weiboLink;
    private String wexinLink;
    private String indexName;
    private String avatar;
    private String roleCname;
    private boolean enable;
    private Long createTime;

    public static List<UserInfo> creaetUserList(List<UserEntity> userEntityList){

        return userEntityList.stream().map(k->{
            return create(k);
        }).collect(Collectors.toList());
    }


    public static UserInfo create(UserEntity userEntity) {
        UserInfo userInfo = new UserInfo();
        userInfo.setUserId(userEntity.getId());
        userInfo.setCname(userEntity.getCname());
        userInfo.setEmail(userEntity.getEmail());
        userInfo.setTwitterName(userEntity.getTwitterName());
        userInfo.setTwitterLink(userEntity.getTwitterLink());
        userInfo.setFacebookName(userEntity.getFacebookName());
        userInfo.setFacebookLink(userEntity.getFacebookLink());
        userInfo.setUniversityName(userEntity.getUniversityName());
        userInfo.setUniversityLink(userEntity.getUniversityLink());
        userInfo.setProfessional(userEntity.getProfessional());
        userInfo.setWeiboLink(userEntity.getWeiboLink());
        userInfo.setWexinLink(userEntity.getWechatLink());
        userInfo.setIndexName(userEntity.getIndexName());
        userInfo.setAvatar(userEntity.getAvatar());
        userInfo.setEname(userEntity.getEname());
        userInfo.setRoleCname(userEntity.getRoleCname());
        userInfo.setEnable(userEntity.isEnable());
        userInfo.setCreateTime(userEntity.getCreateTime());
        Long workStartTime = userEntity.getWorkStartTime();
        Long workEndTime = userEntity.getWorkEndTime();

        String workStart = "未知";
        String workEnd = "未知";

        if (Objects.nonNull(workStartTime)) {
            workStart = DateUtils.getInstance().format(new Date(workStartTime));
        }

        if (Objects.nonNull(workEndTime)) {
            if(workEndTime==-1){
                workEnd = "至今";
            }else {
                workEnd = DateUtils.getInstance().format(new Date(workEndTime));
            }
        }

        userInfo.setYearsOfWorking(workStart + "-" + workEnd);
        return userInfo;
    }
}
