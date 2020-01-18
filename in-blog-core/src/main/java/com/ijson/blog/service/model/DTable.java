package com.ijson.blog.service.model;

import com.ijson.blog.dao.entity.PostDraftEntity;
import com.ijson.blog.dao.entity.PostEntity;
import com.ijson.blog.util.Pageable;
import com.ijson.mongo.support.model.PageResult;
import lombok.Data;
import org.apache.commons.collections.CollectionUtils;

import java.util.List;
import java.util.Objects;

/**
 * desc: datatable 要求数据结构
 * version: 6.7
 * Created by cuiyongxu on 2019/9/6 11:16 PM
 */
@Data
public class DTable {
    //当前页
    private int sEcho;
    //总数
    private long iTotalRecords;
    //筛选后总数
    private long iTotalDisplayRecords;
    //返回的集合
    private List<Post> aaData;

    private long recordsTotal;
    private long recordsFiltered;


    public static DTable create(List<Post> posts, Long total, int index) {
        Pageable pageable = new Pageable(total.intValue(), index);
        DTable data = new DTable();
        data.setITotalDisplayRecords(total);
        data.setITotalRecords(total);
        data.setRecordsFiltered(total);
        data.setRecordsTotal(total);
        data.setAaData(posts);
        data.setSEcho(pageable.getCurrentPage());
        return data;
    }


}

