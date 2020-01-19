package com.ijson.blog.service.model.dtable;

import com.google.common.collect.Lists;
import com.ijson.blog.service.model.UserInfo;
import com.ijson.blog.util.Pageable;
import lombok.Data;

import java.util.List;
import java.util.Objects;

/**
 * desc: datatable 要求数据结构
 * version: 6.7
 * Created by cuiyongxu on 2019/9/6 11:16 PM
 */
@Data
public class UserDTable {
    //当前页
    private int sEcho;
    //总数
    private long iTotalRecords;
    //筛选后总数
    private long iTotalDisplayRecords;
    //返回的集合
    private List<UserInfo> aaData = Lists.newArrayList();

    private long recordsTotal;
    private long recordsFiltered;


    public static UserDTable create(List<UserInfo> posts, Long total, int index) {
        if (Objects.isNull(total)) {
            total = 0L;
        }
        Pageable pageable = new Pageable(total.intValue(), index);
        UserDTable data = new UserDTable();
        data.setITotalDisplayRecords(total);
        data.setITotalRecords(total);
        data.setRecordsFiltered(total);
        data.setRecordsTotal(total);
        data.setAaData(posts);
        data.setSEcho(pageable.getCurrentPage());
        return data;
    }


}

