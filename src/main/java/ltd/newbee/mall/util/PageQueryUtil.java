package ltd.newbee.mall.util;

import org.apache.commons.lang3.StringUtils;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * 分页查询参数
 *
 * @author 13
 * @qq交流群 791509631
 * @email 2449207463@qq.com
 * @link https://github.com/newbee-ltd
 */
public class PageQueryUtil extends LinkedHashMap<String, Object> {
    // 当前页码
    private int page;
    // 每页条数
    private int limit;
    private String sidx;
    private String order;

    public PageQueryUtil(Map<String, Object> params) {
        this.putAll(params);

        // 分页参数
        this.page = Integer.parseInt(params.get("page").toString());
        this.limit = Integer.parseInt(params.get("limit").toString());
        this.put("start", (page - 1) * limit);
        this.put("page", page);
        this.put("limit", limit);
        this.sidx = (String) params.get("sidx");
        this.order = (String) params.get("order");
        if (StringUtils.isNotBlank(sidx) && StringUtils.isNotEmpty(order)) {
            this.put("sortField", this.sidx.replaceAll("[A-Z]", "_$0").toLowerCase());
            this.put("order", order);
        }
    }


    public int getPage() {
        return page;
    }

    public void setPage(int page) {
        this.page = page;
    }

    public int getLimit() {
        return limit;
    }

    public void setLimit(int limit) {
        this.limit = limit;
    }

    public String getSidx() {
        return sidx;
    }

    public void setSidx(String sidx) {
        this.sidx = sidx;
    }

    public String getOrder() {
        return order;
    }

    public void setOrder(String order) {
        this.order = order;
    }

    @Override
    public String toString() {
        return "PageQueryUtil{" +
                "page=" + page +
                ", limit=" + limit +
                ", sidx='" + sidx + '\'' +
                ", order='" + order + '\'' +
                "} " + super.toString();
    }
}
