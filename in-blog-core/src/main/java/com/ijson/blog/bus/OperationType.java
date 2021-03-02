package com.ijson.blog.bus;

/**
 * desc: 操作类型
 * version: 7.0.0
 * Created by www.ijson.com on 2020/1/16 4:47 PM
 */
public enum OperationType {
    create_tag("创建文章时,创建tag"),
    update_tag("更新文章时,更新tag,tag-1"),
    delete_article_update_tag("删除文章时,更新tag,tag-1"),
    view_article("文章被查看,view+1"),
    unknown("未知类型");

    String desc;

    OperationType(String desc) {
        this.desc = desc;
    }

    public static OperationType toType(String operationType) {
        OperationType[] types = OperationType.values();
        for (OperationType type : types) {
            if (type.name().equals(operationType)) {
                return type;
            }
        }
        return unknown;
    }
}
