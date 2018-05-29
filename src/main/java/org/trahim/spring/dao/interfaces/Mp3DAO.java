package org.trahim.spring.dao.interfaces;

import org.trahim.spring.dao.objects.Mp3;

import java.util.List;
import java.util.Map;

public interface Mp3DAO {
    long insert(Mp3 mp3);

    void insert(List<Mp3> list);

    void delete(Mp3 mp3);

    void delete(long id);

    Mp3 getMp3ById(long id);

    List<Mp3> getMp3ListByName(String name);

    List<Mp3> getMp3ListByAuthor(String author);

    int getMP3Count();

    Map<String, Long> getStat();
}
