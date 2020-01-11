package com.ijson.blog.service.model;

import com.ijson.blog.util.Pageable;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * desc:
 * version: 6.7
 * Created by cuiyongxu on 2019/8/25 2:12 AM
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ReplyResult {
    private List<Reply> reply;
    private Pageable pageable;
}
