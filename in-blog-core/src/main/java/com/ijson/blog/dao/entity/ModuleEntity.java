package com.ijson.blog.dao.entity;

import com.google.common.collect.Lists;
import com.ijson.mongo.support.model.BaseEntity;
import lombok.Data;
import lombok.ToString;
import org.mongodb.morphia.annotations.*;
import com.ijson.mongo.generator.util.ObjectId;

import java.util.List;

@Data
@Entity(value = "Module", noClassnameStored = true)
@ToString(callSuper = true)
public class ModuleEntity extends BaseEntity{


	@Id
    private String id;

	@Property(Fields.name)
    private String name;

	@Property(Fields.detail)
    private String detail;

	@Property(Fields.turn)
    private boolean turn;

	@Property(Fields.rowNo)
    private int rowNo;


    @Property(Fields.lastModifiedBy)
    private String lastModifiedBy;

    @Property(Fields.deleted)
    private Boolean deleted;

    @Property(Fields.enable)
    private boolean enable;

    @Property(Fields.createdBy)
    private String createdBy;

    @Property(Fields.createTime)
    private long createTime;


    public interface Fields {
		String id = "_id";
		String name="name";
		String detail="detail";
		String turn="turn";
		String rowNo="rowNo";
        String createdBy = "createdBy";
        String createTime = "createTime";
        String enable = "enable";
        String deleted = "deleted";
        String lastModifiedBy ="lastModifiedBy";
    }
}

