package com.ijson.blog.dao.entity;

import com.ijson.blog.dao.model.AccessType;
import com.ijson.mongo.support.model.BaseEntity;
import lombok.Data;
import lombok.ToString;
import org.mongodb.morphia.annotations.*;

/**
 * desc:
 * version: 6.7
 * Created by cuiyongxu on 2019/8/9 4:15 PM
 */
@Data
@Entity(value = "Count", noClassnameStored = true)
@ToString(callSuper = true)
@Indexes({
        @Index(name = "C_VIEWS",
                fields = {
                        @Field(value = CountEntity.Fields.views),
                })
})
public class CountEntity extends BaseEntity {

    @Id
    private String id;

    @Property(Fields.views)
    private long views;

    @Property(Fields.accessType)
    private String accessType;

    public static CountEntity create(PostEntity entity) {
        CountEntity countEntity = new CountEntity();
        countEntity.setId(entity.getId());
        countEntity.setAccessType(AccessType.blog.name());
        return countEntity;
    }


    public interface Fields {
        String id = "_id";
        String views = "views";
        String accessType = "accessType";
    }

}
