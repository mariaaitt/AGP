package com.traveloffersystem.business;

/**
 * Site 实体类，用于表示景点（或旅游目的地）的基本信息
 */
public class Site {
    private int siteId;
    private String name;
    private String type;
    // 如果 description 很长，就不存到表里，而是用文本文件+Lucene

    public Site() {
    }

    public Site(int siteId, String name, String type) {
        this.siteId = siteId;
        this.name = name;
        this.type = type;
    }

    public int getSiteId() {
        return siteId;
    }

    public void setSiteId(int siteId) {
        this.siteId = siteId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
