package com.hzy.dao.impl;

import java.sql.ResultSet;
import java.sql.SQLException;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import com.hzy.dao.IUserDAO;
import com.hzy.domain.User;

@Repository
public class UserDAOImpl implements IUserDAO {

    private JdbcTemplate template;

    @Autowired
    private void setDataSource(DataSource dataSource){
        this.template = new JdbcTemplate(dataSource);
    }

    @Override
    public User getUserByUsername(String username) {
        try {
            return template.queryForObject("select * from user where username = ? ",new RowMapper<User>(){
                @Override
                public User mapRow(ResultSet rs, int rowNum) throws SQLException {
                    User user = new User();
                    user.setUsername(rs.getString("username"));
                    user.setId(rs.getLong("id"));
                    user.setPassword(rs.getString("password"));
                    return user;
                }
            }, username);
        }catch (Exception e){
            e.printStackTrace();
        }
        return null;


    }
}
