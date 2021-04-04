package com.ijson.blog.remote.codec;

import com.alibaba.fastjson.JSONObject;
import com.ijson.blog.remote.model.BaseResult;
import com.ijson.blog.remote.model.GetQQOpenToken;
import com.ijson.blog.remote.model.GetQQTokenByCode;
import com.ijson.rest.proxy.codec.AbstractRestCodeC;
import com.ijson.rest.proxy.exception.RestProxyBusinessException;
import com.ijson.rest.proxy.util.JsonUtil;
import lombok.extern.slf4j.Slf4j;

import java.io.UnsupportedEncodingException;
import java.util.List;
import java.util.Map;

/**
 * desc:
 * version: 7.0.0
 * Created by cuiyongxu on 2020/2/8 7:51 PM
 */
@Slf4j
public class QQConnectCodeC extends AbstractRestCodeC {


    @Override
    public <T> byte[] encodeArg(T obj) {
        return JsonUtil.toJson(obj).getBytes();
    }

    @Override
    public <T> T decodeResult(int statusCode, Map<String, List<String>> headers, byte[] bytes, Class<T> clazz) {
        super.decodeResult(statusCode, headers, bytes, clazz);
        T ret = null;
        try {
            String json = new String(bytes, "UTF-8");
            log.info("encodeArg Result : {}", json);

            if (clazz == GetQQOpenToken.Result.class) {
                json = GetQQOpenToken.Result.format(json);
            }

            if (clazz == GetQQTokenByCode.Result.class) {
                json = GetQQTokenByCode.Result.format(json);
            }

            if (isJson(json)) {
                ret = JsonUtil.fromJson(json, clazz);
            }
        } catch (UnsupportedEncodingException e) {
            log.error("decodeResult", e);
        }
        validateResult(ret);
        return ret;
    }

    private boolean isJson(String jsonString) {
        try {
            JSONObject.parse(jsonString);
        } catch (Exception e) {
            return false;
        }
        return true;
    }

    private void validateResult(Object ret) {
        if (BaseResult.class.isInstance(ret)) {
            BaseResult result = (BaseResult) ret;
            if (result.getCode() != 0) {
                throw new RestProxyBusinessException(result.getCode(), "获取数据异常");
            }
        }
    }


}
