package com.ijson.blog.dao.entity;

import com.ijson.blog.dao.model.FileType;
import com.ijson.blog.model.AuthContext;
import com.ijson.blog.model.Constant;
import com.ijson.mongo.support.model.BaseEntity;
import lombok.Data;
import lombok.ToString;
import org.mongodb.morphia.annotations.*;

/**
 * desc:
 * version: 6.7
 * Created by cuiyongxu on 2019/9/1 12:28 AM
 */
@Data
@Entity(value = "FileUpload", noClassnameStored = true)
@ToString(callSuper = true)
@Indexes({
        @Index(name = "FU_MD5",
                fields = {
                        @Field(value = FileUploadEntity.Fields.md5),
                })
})
public class FileUploadEntity extends BaseEntity {
    @Id
    private String id;

    @Property(FileUploadEntity.Fields.url)
    private String url;

    @Property(FileUploadEntity.Fields.fileName)
    private String fileName;

    @Property(FileUploadEntity.Fields.newName)
    private String newName;

    @Property(FileUploadEntity.Fields.suffix)
    private String suffix;

    @Property(FileUploadEntity.Fields.fileType)
    private FileType fileType;

    @Property(FileUploadEntity.Fields.md5)
    private String md5;

    @Property(FileUploadEntity.Fields.lastModifiedBy)
    private String lastModifiedBy;

    @Property(FileUploadEntity.Fields.deleted)
    private Boolean deleted;

    @Property(FileUploadEntity.Fields.enable)
    private boolean enable;

    @Property(FileUploadEntity.Fields.createdBy)
    private String createdBy;

    @Property(FileUploadEntity.Fields.createTime)
    private long createTime;

    @Property(FileUploadEntity.Fields.lastModifiedTime)
    private long lastModifiedTime;


    public interface Fields {
        String id = "_id";
        String url = "url";
        String fileName = "fileName";
        String userId = "userId";
        String suffix = "suffix";
        String fileType = "fileType";
        String md5 = "md5";
        String newName = "newName";
        String createdBy = "createdBy";
        String createTime = "createTime";
        String enable = "enable";
        String deleted = "deleted";
        String lastModifiedBy = "lastModifiedBy";
        String lastModifiedTime = "lastModifiedTime";
    }


    public static FileUploadEntity create(FileType uploadType,
                                          String fileName,
                                          String newFileName,
                                          String md5,
                                          String suffixName,
                                          String ctx,
                                          AuthContext context) {
        FileUploadEntity fileUploadEntity = new FileUploadEntity();
        fileUploadEntity.setCreatedBy(context.getId());
        fileUploadEntity.setLastModifiedBy(context.getId());
        fileUploadEntity.setCreateTime(System.currentTimeMillis());
        fileUploadEntity.setLastModifiedTime(System.currentTimeMillis());
        fileUploadEntity.setDeleted(false);
        fileUploadEntity.setEnable(true);
        fileUploadEntity.setFileType(uploadType);
        fileUploadEntity.setFileName(fileName);
        fileUploadEntity.setNewName(newFileName);
        fileUploadEntity.setMd5(md5);
        fileUploadEntity.setSuffix(suffixName);
        fileUploadEntity.setUrl(ctx);
        return fileUploadEntity;
    }

}
