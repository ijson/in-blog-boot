package com.ijson.blog.util;

import com.google.common.collect.Lists;

import java.util.List;

/**
 * desc:
 * version: 7.0.0
 * Created by cuiyongxu on 2020/2/29 1:50 PM
 */
public class INFileUtil {

    private static final List<String> imageFileType = Lists.newArrayList("webp", "bmp", "pcx", "tif", "gif", "jpg", "jpeg", "tga", "exif", "fpx", "svg", "psd", "cdr", "pcd", "dxf", "ufo", "eps", "ai", "png", "hdri", "raw", "wmf", "flic", "emf", "ico");

    public static boolean isImage(String suffix) {
        return imageFileType.contains(suffix.replace(".", ""));
    }


}
