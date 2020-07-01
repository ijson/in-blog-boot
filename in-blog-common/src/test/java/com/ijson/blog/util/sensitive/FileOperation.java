package com.ijson.blog.util.sensitive;

import lombok.extern.slf4j.Slf4j;
import org.junit.Test;

import java.io.File;

/**
 * desc:
 * version: 7.0.0
 * Created by cuiyongxu on 2020/6/30 11:18 下午
 */
@Slf4j
public class FileOperation {

    @Test
    public void fileInnerMethod() {
        File file = new File("/Users/cuiyongxu/demo");
        //判断目录是否存在
        if (!file.exists()) {
            //创建目录
            file.mkdir();
        }
        System.out.println("是否是文件:" + file.isFile());
        System.out.println("文件是否处于隐藏状态:" + file.isHidden());
        System.out.println("是不是目录:" + file.isDirectory());
        System.out.println("字节长度:" + file.length());
        System.out.println("是否可读:" + file.canRead());
        System.out.println("是否可写:" + file.canWrite());
        System.out.println("最后修改时间:" + file.lastModified());
        System.out.println("是否是可执行文件:" + file.canExecute());
        System.out.println("删除目录:" + file.delete());
    }
}
