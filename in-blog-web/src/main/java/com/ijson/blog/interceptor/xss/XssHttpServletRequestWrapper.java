package com.ijson.blog.interceptor.xss;

import org.owasp.esapi.ESAPI;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;
import java.util.regex.Pattern;

import org.owasp.esapi.ESAPI;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;
import java.util.regex.Pattern;


public class XssHttpServletRequestWrapper extends HttpServletRequestWrapper {

    private final Pattern SCRIPT_ALL_PATTERN = Pattern.compile("<script>(.*?)</script>", Pattern.CASE_INSENSITIVE);
    private final Pattern SRC_ALL_PATTERN = Pattern.compile("src[\r\n]*=[\r\n]*'(.*?)'", Pattern.CASE_INSENSITIVE | Pattern.MULTILINE | Pattern.DOTALL);
    private final Pattern SRC_GAL_ALL_PATTERN = Pattern.compile("src[\r\n]*=[\r\n]*\"(.*?)\"", Pattern.CASE_INSENSITIVE | Pattern.MULTILINE | Pattern.DOTALL);
    private final Pattern SINGLE_SCRIPT_END_PATTERN = Pattern.compile("</script>", Pattern.CASE_INSENSITIVE);
    private final Pattern SINGLE_SCRIPT_BEGIN_PATTERN = Pattern.compile("<script(.*?)>", Pattern.CASE_INSENSITIVE | Pattern.MULTILINE | Pattern.DOTALL);
    private final Pattern EVAL_PATTERN_PATTERN = Pattern.compile("eval\\((.*?)\\)", Pattern.CASE_INSENSITIVE | Pattern.MULTILINE | Pattern.DOTALL);
    private final Pattern EXPRESSION_PATTERN = Pattern.compile("expression\\((.*?)\\)", Pattern.CASE_INSENSITIVE | Pattern.MULTILINE | Pattern.DOTALL);
    private final Pattern JAVASCRIPT_PATTERN = Pattern.compile("javascript:", Pattern.CASE_INSENSITIVE);
    private final Pattern VBSCRIPT_PATTERN = Pattern.compile("vbscript:", Pattern.CASE_INSENSITIVE);
    private final Pattern ΟNLΟAD_PATTERN = Pattern.compile("onload(.*?)=", Pattern.CASE_INSENSITIVE | Pattern.MULTILINE | Pattern.DOTALL);
    private final Pattern ONXX_PATTERN = Pattern.compile("on.*(.*?)=", Pattern.CASE_INSENSITIVE | Pattern.MULTILINE | Pattern.DOTALL);


    XssHttpServletRequestWrapper(HttpServletRequest servletRequest) {
        super(servletRequest);
    }


    @Override
    public String[] getParameterValues(String parameter) {
        String[] values = super.getParameterValues(parameter);
        if (values == null) {
            return null;
        }
        int count = values.length;
        String[] encodedValues = new String[count];
        for (int i = 0; i < count; i++) {
            encodedValues[i] = cleanXSS(values[i]);
        }
        return encodedValues;
    }

    @Override
    public String getParameter(String parameter) {
        String value = super.getParameter(parameter);
        if (value == null) {
            return null;
        }
        return cleanXSS(value);
    }

    @Override
    public String getHeader(String name) {
        String value = super.getHeader(name);
        if (value == null) {
            return null;
        }
        return cleanXSS(value);
    }


    private String cleanXSS(String value) {
        if (value != null) {
            // 推荐使用ESAPI库来避免脚本攻击
            value = ESAPI.encoder().canonicalize(value);

            // 避免空字符串
            value = value.replaceAll("", "");

            // 避免script 标签
            value = SCRIPT_ALL_PATTERN.matcher(value).replaceAll("");

            // 避免src形式的表达式
            value = SRC_ALL_PATTERN.matcher(value).replaceAll("");

            value = SRC_GAL_ALL_PATTERN.matcher(value).replaceAll("");

            // 删除单个的 </script> 标签
            value = SINGLE_SCRIPT_END_PATTERN.matcher(value).replaceAll("");

            // 删除单个的<script ...> 标签
            value = SINGLE_SCRIPT_BEGIN_PATTERN.matcher(value).replaceAll("");

            // 避免 eval(...) 形式表达式
            value = EVAL_PATTERN_PATTERN.matcher(value).replaceAll("");

            // 避免 expression(...) 表达式
            value = EXPRESSION_PATTERN.matcher(value).replaceAll("");

            // 避免 javascript: 表达式
            value = JAVASCRIPT_PATTERN.matcher(value).replaceAll("");

            // 避免 vbscript: 表达式
            value = VBSCRIPT_PATTERN.matcher(value).replaceAll("");

            // 避免 οnlοad= 表达式
            value = ΟNLΟAD_PATTERN.matcher(value).replaceAll("");

            // 避免 onXX= 表达式
            value = ONXX_PATTERN.matcher(value).replaceAll("");

        }
        return value;
    }


}