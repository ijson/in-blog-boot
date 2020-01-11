package com.ijson.blog.util;

import lombok.Data;

import java.util.List;

@Data
public class Pageable {

    //成员变量

    //当前页
    private int currentPage;
    //总记录数
    private int countRecord;
    //总页数
    private int totalPage;
    // 当前页记录开始的位置The current page records the starting position
    private int currentPageStartPosition;
    // 每页显示的记录数
    public static final int PAGESIZE = 20;
    // 索引的sum值 代表的是 google页面中最大显示页数
    private int sumIndex = 6;
    // 开始的索引值
    private int startIndex;
    // 结束的索引值
    private int endIndex;
    //当前页信息
    private List allEntities;

    //构造器
    public Pageable() {
    }


    public static int getPageNumber(int total, int pageNumber, int limit) {
        return (total - 1) / limit + 1;
    }

    public Pageable(int countWebRecord, int currentWebPage) {
        // 计算当前页
        this.currentPage = currentWebPage;
        // 计算出当前页开始的位置
        this.currentPageStartPosition = (currentWebPage - 1) * PAGESIZE;
        // 计算总页数
        this.countRecord = countWebRecord;
        if (this.countRecord % this.PAGESIZE == 0) {
            this.totalPage = this.countRecord / this.PAGESIZE;
        } else {
            this.totalPage = this.countRecord / this.PAGESIZE + 1;
        }

        //计算开始和结束的索引值
        //当当前页小于等于四时开始的索引值等于一,而结束的索引值分两种情况
        if (this.currentPage <= 4) {
            this.startIndex = 1;
            if (this.endIndex > this.totalPage) {
                this.endIndex = this.totalPage;
            }
            this.endIndex = this.currentPage + 2;
        }
        // 当当前页大于四时开始的索引值和结束的索引值均分三种情况
        else if (this.currentPage > 4) {
            if (this.endIndex > this.totalPage && this.totalPage < this.sumIndex) {
                this.startIndex = 1;
                this.endIndex = this.totalPage;
            } else if (this.totalPage > this.sumIndex) {
                this.startIndex = this.totalPage - 5;
                this.endIndex = this.totalPage;
            } else {
                this.startIndex = this.currentPage - 3;
                this.endIndex = this.currentPage + 2;
            }
        }
    }

    public static void main(String[] args) {
        Pageable pageable = new Pageable(56, 1);
        System.out.println("当前页:" + pageable.getCurrentPage());
        System.out.println("数据总数据:" + pageable.getCountRecord());
        System.out.println("总页数:" + pageable.getTotalPage());
        System.out.println("当前页开始记录坐标:" + pageable.getCurrentPageStartPosition());
    }
}