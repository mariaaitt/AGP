package com.traveloffersystem.dao;

import com.traveloffersystem.business.Site;
import java.util.List;

public interface SiteDao {
    void createTableIfNotExists();
    void insertSite(Site site);
    List<Site> findAll();
    List<Site> findByType(String type);
    // ...更多操作
}
