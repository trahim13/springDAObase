package org.trahim.spring.dao.impls;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import org.trahim.spring.dao.interfaces.Mp3DAO;
import org.trahim.spring.dao.objects.Mp3;

import javax.sql.DataSource;
import java.util.List;

@Component
public class MySqlDAO implements Mp3DAO {

    JdbcTemplate jdbcTemplate;

    @Autowired
    @Qualifier(value = "dataSource")
    private void setDataSource(DataSource dataSource) {
        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }


    @Override
    public void insert(Mp3 mp3) {
        String sql = "insert into mp3 (name, author) values(?,?)";
        jdbcTemplate.update(sql, mp3.getName(), mp3.getAuthor());
    }

    @Override
    public void insert(List<Mp3> list) {
        for (Mp3 mp3 :
                list) {
            insert(mp3);
        }
    }

    @Override
    public void delete(long id) {
        String sql = "delete from mp3 where id=?";
        jdbcTemplate.update(sql, id);
    }

    @Override
    public void delete(Mp3 mp3) {
        delete(mp3.getId());
    }

    @Override
    public Mp3 getMp3ById(long id) {
        return null;
    }

    @Override
    public List<Mp3> getMp3ListByName(String name) {
        return null;
    }

    @Override
    public List<Mp3> getMp3ListByAuthor(String author) {
        return null;
    }
}
