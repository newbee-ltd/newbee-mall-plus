package ltd.newbee.mall.util;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;

/**
 * 操作json的封装方法
 * use:jackson
 */
public class JsonUtil {
    /**
     * 将json字符串转换为java对象
     *
     * @param objClass obj对象所属class类
     * @param jsonStr  json字符串
     * @param <T>      泛型
     * @return java对象
     */
    public static <T> T jsonToObj(Class<T> objClass, String jsonStr) throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        return mapper.readValue(jsonStr, objClass);
    }

    /**
     * 将obj对象转换为json字符串
     *
     * @param obj java对象
     * @return 字符串
     */
    public static String objToJson(Object obj) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        return mapper.writeValueAsString(obj);
    }
}
