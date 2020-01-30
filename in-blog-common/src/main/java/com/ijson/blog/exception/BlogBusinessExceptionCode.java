package com.ijson.blog.exception;

/**
 * desc:
 * version: 6.7
 * Created by cuiyongxu on 2019/7/28 9:17 PM
 */
public enum BlogBusinessExceptionCode {

    SYSTEM_ERROR(500_000_001, "系统发生未知错误,该异常已周知管理员,请尝试正常访问网站"),
    REQUEST_WAY_ERROR(500_000_002, "请求方式异常,请检查"),


    USER_NOT_FOUND(200_000_001, "用户不存在或密码不正确,请检查"),
    PASSWORD_NOT_FOUND(200_000_002, "密码验证失败,请检查"),
    INVALID_CURRENT_PASSWORD(200_000_003, "密码不正确,请检查"),
    INFORMATION_IS_INCOMPLETE(200_000_004, "信息不完整,请检查"),
    THE_PASSWORD_CANNOT_BE_EMPTY(200_000_005, "密码不能为空,请检查"),
    USER_ALREADY_EXISTS(200_000_006, "用户已存在,请检查"),
    TITLE_NOT_SET(200_000_007, "未填写文章标题"),
    CONTEXT_NOT_SET(200_000_008, "未填写文章内容"),
    BLOG_NOT_FOUND(200_000_009, "文章未找到"),
    YOU_ALREADY_SUPPORTED_IT(200_000_010, "您已经支持过了"),
    POST_UPDATE_ID_NOT_FOUND(200_000_011, "数据更新必填参数为空"),
    COMMENT_CREATION_FAILED(200_000_012, "评论创建失败"),
    USER_INFORMATION_ACQUISITION_FAILED(200_000_013, "用户信息获取失败,请重新登录"),
    CAPTCHA_ERROR_OR_NOT_PRESENT(200_000_014, "验证码错误或不存在"),
    USER_ENAME_CANNOT_BE_EMPTY(200_000_015, "用户名不能为空"),
    USER_CNAME_CANNOT_BE_EMPTY(200_000_016, "昵称不能为空"),
    USER_EMAIL_CANNOT_BE_EMPTY(200_000_017, "邮箱不能为空"),
    USER_PAASWORD_CANNOT_BE_EMPTY(200_000_018, "密码不能为空"),
    SENSITIVE_TEXT_EXISTS_PLEASE_CHECK_AND_RESUBMIT(200_000_019, "存在敏感文字,请检查后重新提交"),
    TAG_NOT_FOUND(200_000_020, "标签未找到"),
    COMMENTS_SHOULD_NOT_EXCEED_300_WORDS(200_000_021, "评论内容不能超过300字"),
    INCORRECT_ACCOUNT_FORMAT(200_000_022, "账号格式不正确,请字母开头5-16位,允许字母数字下划线"),
    NICKNAME_FORMAT_INCORRECT(200_000_023, "昵称格式不正确,允许中文,英文,数字"),
    INCORRECT_MAILBOX_FORMAT(200_000_024, "邮箱格式不正确"),
    USER_NAMES_MUST_NOT_EXCEED_30_DIGITS(200_000_025, "用户名长度不能超过30位"),
    NICKNAME_MUST_NOT_EXCEED_20_DIGITS(200_000_026, "昵称长度不能超过20位"),
    USER_DOES_NOT_EXIST_OR_HAS_BEEN_DELETED(200_000_027, "用户不存在或已删除"),
    ADMINISTRATOR_ACCOUNTS_ARE_NOT_ALLOWED_TO_BE_DISABLED_OR_DELETED(200_000_028, "管理员账号不允许禁用或删除"),
    ENABLED_STATE_CANNOT_BE_DELETED(200_000_029, "启用状态无法删除"),
    ACCOUNT_DEACTIVATED_OR_DELETED(200_000_030, "账号已停用或已删除"),
    FRIENDSHIP_LINK_DOES_NOT_EXIST_OR_HAS_BEEN_DELETED(200_000_031, "友情链接不存在或已删除"),
    PERMISSIONS_DOES_NOT_EXIST_OR_HAS_BEEN_DELETED(200_000_032, "权限不存在或者已删除"),
    ROLE_DOES_NOT_EXIST_OR_HAS_BEEN_DELETED(200_000_033, "角色不存在或者已删除"),
    PLEASE_MODIFY_THE_WEBSITE_USER_REGISTRATION_ROLE_TO_DISABLE_OR_REMOVE(200_000_034, "请修改网站设置中注册用户角色后禁用或删除"),
    LABEL_CANNOT_BE_EMPTY(200_000_035, "标签不能为空"),
    ROLE_CANNOT_BE_EMPTY(200_000_036, "角色不能为空"),
    WEBSITE_NAME_CANNOT_BE_EMPTY(200_000_037, "网站名称不能为空"),
    REG_ROLE_CANNOT_BE_EMPTY(200_000_038, "注册角色不能为空"),
    BLOGROLL_DESC_CANNOT_BE_EMPTY(200_000_039, "友情链接描述不能为空"),
    BLOGROLL_LINK_CANNOT_BE_EMPTY(200_000_040, "友情链接地址不能为空"),
    AUTH_ORDER_CANNOT_BE_EMPTY(200_000_041, "权限排列顺序不能为空"),
    AUTH_ENAME_CANNOT_BE_EMPTY(200_000_042, "权限英文标识不能为空"),
    AUTH_CNAME_CANNOT_BE_EMPTY(200_000_043, "权限中文标识不能为空"),
    AUTH_LINK_CANNOT_BE_EMPTY(200_000_044, "权限地址不能为空"),
    AUTH_ENAME_ALREADY_EXIST(200_000_045, "权限英文标识已存在"),
    ROLE_ENAME_ALREADY_EXIST(200_000_046, "角色英文标识已存在"),
    ROLE_ENAME_CANNOT_BE_EMPTY(200_000_047, "角色英文标识不能为空"),
    ROLE_AUTH_CANNOT_BE_EMPTY(200_000_048, "角色未选择权限"),
    ROLE_CNAME_CANNOT_BE_EMPTY(200_000_049, "角色中文标识不能为空"),
    REQUEST_PARAMETERS_ARE_INCOMPLETE(200_000_050, "请求参数不完整"),
    NO_RIGHT_TO_DO_THIS(200_000_051, "无权执行此操作"),
    DRAFT_NOT_FOUND(200_000_052, "草稿未找到"),
    TAGS_DOES_NOT_EXIST_OR_HAS_BEEN_DELETED(200_000_053, "标签不存在或已删除"),
    THE_CODE_CANNOT_BE_EMPTY(200_000_054, "代码不能为空"),
    THE_DESCRIPTION_CANNOT_BE_EMPTY(200_000_055, "描述不能为空"),
    THE_HEADER_IS_EMPTY_OR_DOES_NOT_EXIST(200_000_056, "header为空或不存在");

    private int code;
    private String message;

    BlogBusinessExceptionCode(int code, String message) {
        this.code = code;
        this.message = message;
    }

    public int getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }

    public static BlogBusinessExceptionCode valueOf(int errorCode) {
        BlogBusinessExceptionCode[] codes = BlogBusinessExceptionCode.values();
        for (BlogBusinessExceptionCode code : codes) {
            if (code.getCode() == errorCode) {
                return code;
            }
        }

        return null;
    }
}
