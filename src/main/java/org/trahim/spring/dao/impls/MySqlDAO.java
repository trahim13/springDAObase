package org.trahim.spring.dao.impls;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Component;
import org.trahim.spring.dao.interfaces.Mp3DAO;
import org.trahim.spring.dao.objects.Mp3;

import javax.sql.DataSource;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

@Component
public class MySqlDAO implements Mp3DAO {

    NamedParameterJdbcTemplate jdbcTemplate;

    @Autowired
    @Qualifier(value = "dataSource")
    private void setDataSource(DataSource dataSource) {
        this.jdbcTemplate = new NamedParameterJdbcTemplate(dataSource);
    }


    @Override
    public void insert(Mp3 mp3) {
        String sql = "insert into mp3 (name, author) values(:name, :author )";
        MapSqlParameterSource parameterSource = new MapSqlParameterSource();
        parameterSource.addValue("name", mp3.getName());
        parameterSource.addValue("author", mp3.getAuthor());
        jdbcTemplate.update(sql, parameterSource);
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
        String sql = "delete from mp3 where id=:id";
        MapSqlParameterSource parameterSource = new MapSqlParameterSource();
        parameterSource.addValue("id", id);
        jdbcTemplate.update(sql, parameterSource);
    }

    @Override
    public void delete(Mp3 mp3) {
        delete(mp3.getId());
    }

    @Override
    public Mp3 getMp3ById(long id) {
        String sql = "select * from mp3 where id=:id";
        MapSqlParameterSource parameterSource = new MapSqlParameterSource();
        parameterSource.addValue("id", id);
        Mp3 mp3 = jdbcTemplate.queryForObject(sql, parameterSource, new Mp3RowMapper());
        return null;
    }

    @Override
    public List<Mp3> getMp3ListByName(String name) {
        String sql = "select * from mp3 where upper (name) like :name";
        MapSqlParameterSource parameterSource = new MapSqlParameterSource();
        parameterSource.addValue("name", name);

        List<Mp3> mp3List = jdbcTemplate.query(sql, parameterSource, new Mp3RowMapper());
        return mp3List;
    }


    @Override
    public List<Mp3> getMp3ListByAuthor(String author) {
        String sql = "select * from mp3 where upper (author) like: author";
        MapSqlParameterSource parameterSource = new MapSqlParameterSource();
        parameterSource.addValue("author", author);
        List<Mp3> mp3List = jdbcTemplate.query(sql, parameterSource, new Mp3RowMapper());
        return mp3List;
    }

    @Override
    public int getMP3Count() {
        String sql = "select count(*) from mp3";
        return jdbcTemplate.getJdbcOperations().queryForObject(sql, Integer.class);
    }

    private static final class Mp3RowMapper implements RowMapper<Mp3> {
        @Override
        public Mp3 mapRow(ResultSet resultSet, int i) throws SQLException {
            Mp3 mp3 = new Mp3();
            mp3.setId(resultSet.getLong("id"));
            mp3.setName(resultSet.getString("name"));
            mp3.setAuthor(resultSet.getString("author"));
            return mp3;
        }
    }
}
