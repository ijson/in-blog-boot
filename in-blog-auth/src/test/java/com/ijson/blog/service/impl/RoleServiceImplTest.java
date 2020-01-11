package com.ijson.blog.service.impl;

import com.google.common.collect.Lists;
import com.ijson.blog.model.Permission;
import com.ijson.blog.model.PermissionType;
import com.ijson.blog.dao.entity.RoleEntity;
import com.ijson.blog.dao.entity.UserEntity;
import com.ijson.blog.model.AuthContext;
import com.ijson.blog.service.RoleService;
import com.ijson.blog.service.UserService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.List;

/**
 * desc:
 * version: 6.7
 * Created by cuiyongxu on 2019/12/14 9:28 PM
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "classpath:spring-mongo-datastore.xml")
public class RoleServiceImplTest {

    @Autowired
    private UserService userService;

    @Autowired
    private RoleService roleService;

    @Test
    public void create() {
        UserEntity entity = userService.findUserByEname("cuiyongxu", "", "");
        AuthContext context = new AuthContext(entity.getId(),
                entity.getEname(),
                entity.getCname(),
                entity.getEmail(),
                entity.getMobile(),
                entity.getAvatar());


        //"/upload",
        // "/edit/*"

        List<Permission> permission = Lists.newArrayList(
                /**
                 * 菜单
                 */
                Permission.create("admin_console_page", "首页", "/admin/console/page", PermissionType.menu,"root",true),
                Permission.create("admin_post_root", "文章", "/admin/post/root", PermissionType.menu,"root",false),
                Permission.create("admin_post_list_page", "文章列表", "/admin/post/list/page", PermissionType.menu,"admin_post_root",true),
                Permission.create("admin_post_add_page", "新建/编辑文章", "/admin/post/add/page", PermissionType.menu,"admin_post_root",true),
                Permission.create("admin_system_root", "系统设置", "/admin/system/root", PermissionType.menu,"root",false),
                Permission.create("admin_i_config_page", "我的设置", "/admin/i/config/page", PermissionType.menu,"admin_system_root",true),


                /**
                 * 连接重定向
                 */
                Permission.create("admin_edit_ename_shamId_page", "新建/编辑文章", "/admin/edit/{ename}/{shamId}/page", PermissionType.action,"",true),

                /**
                 * rest
                 */

                Permission.create("post_create", "博客创建", "/post/create", PermissionType.action,"",true),
                Permission.create("post_enable_ename_shamId", "启用/禁用", "/post/enable/{ename}/{shamId}", PermissionType.action,"",true),
                Permission.create("post_delete_ename_shamId", "删除博文", "/post/delete/{ename}/{shamId}", PermissionType.action,"",true),
                Permission.create("post_upload", "博文附件上传", "/post/upload", PermissionType.action,"",true),
                Permission.create("post_list", "rest 博文列表", "/post/list", PermissionType.action,"",true),
                Permission.create("user_edit_webset", "编辑网站信息", "/user/edit/webset", PermissionType.action,"",true),
                Permission.create("user_edit_base", "编辑基础信息", "/user/edit/base", PermissionType.action,"",true),
                Permission.create("user_edit_contact", "编辑用户常用联系方式", "/user/edit/contact", PermissionType.action,"",true),
                Permission.create("user_edit_password", "编辑用户密码", "/user/edit/password", PermissionType.action,"",true)

                );
        RoleEntity roleEntity = RoleEntity.create(context,
                "系统管理员",
                "system",
                "系统管理员权限",
                "0",
                permission,
                Lists.newArrayList());
        roleService.create(roleEntity);
    }
}
