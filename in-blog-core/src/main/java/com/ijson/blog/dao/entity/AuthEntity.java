package com.ijson.blog.dao.entity;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.ijson.blog.model.AuthInfo;
import com.ijson.blog.model.Constant;
import com.ijson.blog.model.arg.AuthArg;
import com.ijson.mongo.support.model.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import org.apache.commons.collections.CollectionUtils;
import org.mongodb.morphia.annotations.Entity;
import org.mongodb.morphia.annotations.Id;
import org.mongodb.morphia.annotations.Property;

import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * desc:
 * version: 7.0.0
 * Created by cuiyongxu on 2020/1/25 11:39 PM
 */
@EqualsAndHashCode(callSuper = true)
@Data
@Entity(value = "Auth", noClassnameStored = true)
@ToString(callSuper = true)
public class AuthEntity extends BaseEntity {

    @Id
    private String id;

    @Property(Fields.ename)
    private String ename;

    @Property(Fields.fatherId)
    private String fatherId;

    @Property(Fields.cname)
    private String cname;

    @Property(Fields.path)
    private String path;

    @Property(Fields.icon)
    private String icon;

    @Property(Fields.menuType)
    private Constant.MenuType menuType;

    @Property(Fields.order)
    private Integer order;

    @Property(Fields.enable)
    private boolean enable;

    @Property(Fields.createdBy)
    private String createdBy;

    @Property(Fields.createTime)
    private long createTime;

    @Property(Fields.lastModifiedBy)
    private String lastModifiedBy;

    @Property(Fields.lastModifiedTime)
    private long lastModifiedTime;

    @Property(Fields.showMenu)
    private Boolean showMenu;

    public Boolean getShowMenu() {
        if (Objects.isNull(showMenu)) {
            return false;
        }
        return showMenu;
    }

    public interface Fields {
        String id = "_id";
        String cname = "cname";
        String ename = "ename";
        String path = "path";
        String icon = "icon";
        String menuType = "menuType";
        String order = "order";
        String enable = "enable";
        String fatherId = "fatherId";
        String createdBy = "createdBy";
        String createTime = "createTime";
        String lastModifiedBy = "lastModifiedBy";
        String lastModifiedTime = "lastModifiedTime";
        String showMenu = "showMenu";

    }


    public static List<AuthInfo> createAuthList(List<AuthEntity> dataList) {
        if (CollectionUtils.isEmpty(dataList)) {
            return Lists.newArrayList();
        }
        return dataList.stream().map(AuthEntity::createAuthInfo).collect(Collectors.toList());
    }


    public static AuthInfo createAuthInfo(AuthEntity entity) {
        return createAuthInfo(entity, false, false);
    }

    public static AuthInfo createAuthInfo(AuthEntity entity, boolean checked, boolean disabled) {
        AuthInfo info = new AuthInfo();
        info.setCname(entity.getCname());
        info.setEnable(entity.isEnable());
        info.setId(entity.getId());
        info.setEname(entity.getEname());
        info.setPath(entity.getPath());
        info.setFatherId(entity.getFatherId());
        info.setMenuType(entity.getMenuType());
        info.setOrder(entity.getOrder());
        info.setChecked(checked);
        info.setDisabled(disabled);
        info.setShowMenu(entity.getShowMenu());
        return info;
    }


    public static Map<AuthArg, List<AuthInfo>> getAuthMap(List<AuthInfo> allAuth, List<String> authIds, boolean disabled) {
        if (CollectionUtils.isEmpty(allAuth)) {
            return Maps.newHashMap();
        }
        List<AuthInfo> fatherEntity = allAuth.stream().filter(k -> {
            return "0".equals(k.getFatherId());
        }).sorted(Comparator.comparing(AuthInfo::getOrder)).collect(Collectors.toList());

        return fatherEntity.stream().collect(Collectors.toMap(key -> {
            AuthArg arg = new AuthArg(key.getId(), key.getEname(), key.getCname(), key.getPath(), authIds.contains(key.getId()), disabled, key.getIcon());
            arg.setOrder(key.getOrder());
            return arg;
        }, value -> {
            List<AuthInfo> vdata = allAuth.stream().filter(vs -> {
                return vs.getFatherId().equals(value.getId());
            }).collect(Collectors.toList()).stream().map(k -> {
                k.setChecked(authIds.contains(k.getId()));
                k.setDisabled(disabled);
                return k;
            }).sorted(Comparator.comparing(AuthInfo::getOrder)).collect(Collectors.toList());
            return vdata;
        }));
    }


}
