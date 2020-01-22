package com.ijson.blog.dao.entity;

import lombok.Data;
import lombok.ToString;
import org.mongodb.morphia.annotations.Entity;
import org.mongodb.morphia.annotations.Id;

/**
 * desc:
 * version: 7.0.0
 * Created by cuiyongxu on 2020/1/23 12:17 AM
 */
@Data
@Entity(value = "Config", noClassnameStored = true)
@ToString(callSuper = true)
public class ConfigEntity {


    @Id
    private String id;

    private

}
