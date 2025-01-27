package com.traveloffersystem.persistence;

import com.traveloffersystem.dao.SiteDao;
import com.traveloffersystem.business.Site;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import javax.annotation.Resource;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;


// At this stage it's still a good idea to manually build the tables in the database, and this class is later used


@Repository
public class SiteDaoImpl implements SiteDao {

    @Resource
    private JdbcTemplate jdbcTemplate;

    @Override
    public void createTableIfNotExists() {
        String sql = "CREATE TABLE IF NOT EXISTS site ("
                + " site_id SERIAL PRIMARY KEY,"
                + " name VARCHAR(100),"
                + " type VARCHAR(50)"
                + ")";
        jdbcTemplate.execute(sql);
    }

    @Override
    public void insertSite(Site site) {
        String sql = "INSERT INTO site(name, type) VALUES(?, ?)";
        jdbcTemplate.update(sql, site.getName(), site.getType());
    }

    @Override
    public List<Site> findAll() {
        String sql = "SELECT site_id, name, type FROM site";
        return jdbcTemplate.query(sql, (rs, rowNum) -> mapSite(rs));
    }

    @Override
    public List<Site> findByType(String type) {
        String sql = "SELECT site_id, name, type FROM site WHERE type = ?";
        return jdbcTemplate.query(sql, new Object[]{type}, (rs, rowNum) -> mapSite(rs));
    }

    private Site mapSite(ResultSet rs) throws SQLException {
        Site s = new Site();
        s.setSiteId(rs.getInt("site_id"));
        s.setName(rs.getString("name"));
        s.setType(rs.getString("type"));
        return s;
    }
}
