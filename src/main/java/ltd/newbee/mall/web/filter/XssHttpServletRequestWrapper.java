package ltd.newbee.mall.web.filter;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletRequestWrapper;
import org.apache.commons.text.StringEscapeUtils;

import java.io.IOException;

public class XssHttpServletRequestWrapper extends HttpServletRequestWrapper {
    private byte[] body;


    public XssHttpServletRequestWrapper(HttpServletRequest request) throws IOException {
        super(request);
    }

    @Override
    public String getQueryString() {
        return StringEscapeUtils.escapeHtml4(super.getQueryString());
    }

    @Override
    public String getParameter(String name) {
        return StringEscapeUtils.escapeHtml4(super.getParameter(name));
    }

    @Override
    public String[] getParameterValues(String name) {
        String[] values = super.getParameterValues(name);
        if (values != null) {
            int length = values.length;
            String[] escapeValues = new String[length];
            for (int i = 0; i < length; i++) {
                // 防xss攻击和过滤前后空格
                escapeValues[i] = StringEscapeUtils.escapeHtml4(values[i]).trim();
            }
            return escapeValues;
        }
        return super.getParameterValues(name);
    }

}
