package ltd.newbee.mall.config;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import org.apache.commons.text.StringEscapeUtils;
import org.springframework.boot.jackson.JsonComponent;

import java.io.IOException;

/**
 * 添加全局的json反序列化设置
 */
@JsonComponent
public class GlobalJsonDeserializer {

    /**
     * 字符串反序列化器
     * 过滤特殊字符，解决 XSS 攻击
     */
    public static class StringDeserializer extends JsonDeserializer<String> {

        @Override
        public String deserialize(JsonParser jsonParser, DeserializationContext deserializationContext) throws IOException {
            // 防止xss攻击
            return StringEscapeUtils.escapeHtml4(jsonParser.getValueAsString());
        }
    }
}
