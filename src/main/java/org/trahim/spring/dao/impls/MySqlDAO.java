package org.trahim.spring.dao.impls;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.core.namedparam.SqlParameterSourceUtils;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Component;
import org.trahim.spring.dao.interfaces.Mp3DAO;
import org.trahim.spring.dao.objects.Mp3;

import javax.sql.DataSource;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class MySqlDAO implements Mp3DAO {

    private NamedParameterJdbcTemplate jdbcTemplate;

    private SimpleJdbcInsert simpleJdbcInsert;



    @Autowired
    @Qualifier(value = "dataSource")
    private void setDataSource(DataSource dataSource) {
        this.jdbcTemplate = new NamedParameterJdbcTemplate(dataSource);
        this.simpleJdbcInsert = new SimpleJdbcInsert(dataSource).withTableName("mp3").usingColumns("name", "author");
    }


//    @Override
//    public long insert(Mp3 mp3) {
//        String sql = "insert into mp3 (name, author) values(:name, :author )";
//        KeyHolder keyHolder = new GeneratedKeyHolder();
//
//        MapSqlParameterSource parameterSource = new MapSqlParameterSource();
//        parameterSource.addValue("name", mp3.getName());
//        parameterSource.addValue("author", mp3.getAuthor());
//        jdbcTemplate.update(sql, parameterSource, keyHolder);
//
//        return keyHolder.getKey().longValue();
//    }


    @Override
    public long insert(Mp3 mp3) {
        MapSqlParameterSource parameterSource = new MapSqlParameterSource();
        parameterSource.addValue("author", mp3.getAuthor());
        parameterSource.addValue("name", mp3.getName());

        return simpleJdbcInsert.execute(parameterSource);
    }

    @Override
    public void insert(List<Mp3> list) {
        String sql = "insert into mp3 (author, name) values(:author, :name)";

        SqlParameterSource[] sqlParameterSources = SqlParameterSourceUtils.createBatch(list.toArray());
        jdbcTemplate.batchUpdate(sql, sqlParameterSources);

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
        parameterSource.addValue("name", "%"+name.toUpperCase()+"%");

        return jdbcTemplate.query(sql, parameterSource, new Mp3RowMapper());
    }


    @Override
    public List<Mp3> getMp3ListByAuthor(String author) {
        String sql = "select * from mp3 where upper (author) like :author";
        MapSqlParameterSource parameterSource = new MapSqlParameterSource();
        parameterSource.addValue("author", "%"+author.toUpperCase()+"%");

        return jdbcTemplate.query(sql, parameterSource, new Mp3RowMapper());
    }

    @Override
    public int getMP3Count() {
        String sql = "select count(*) from mp3";
        return jdbcTemplate.getJdbcOperations().queryForObject(sql, Integer.class);
    }


//    @Override
//    public Map<String, Long> getStat() {
//        String sql = "select author, count(*) as count from mp3 group by author";
//
//       return jdbcTemplate.query(sql, new ResultSetExtractor<Map<String, Long>>() {
//            @Override
//            public Map<String, Long> extractData(ResultSet resultSet) throws SQLException, DataAccessException {
//                Map<String, Long> map = new HashMap<>();
//                while (resultSet.next()) {
//                    String author = resultSet.getString("author");
//                    Long count = resultSet.getLong("count");
//                    map.put(author, count);
//                }
//                return map;
//            }
//        });
//    }


    @Override
    public Map<String, Long> getStat() {
        String sql = "select author, count(*) as count from mp3 group by author";

        return jdbcTemplate.query(sql, n -> {
                Map<String, Long> map = new HashMap<>();
                while (n.next()) {
                    String author = n.getString("author");
                    Long count = n.getLong("count");
                    map.put(author, count);
                }
                return map;
        });
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
