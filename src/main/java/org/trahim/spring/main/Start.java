package org.trahim.spring.main;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.trahim.spring.dao.impls.MySqlDAO;
import org.trahim.spring.dao.objects.Mp3;

public class Start {
    public static void main(String[] args) {

        Mp3 mp3 = new Mp3();
        mp3.setAuthor("trahim");
        mp3.setName("spring dao 4");

        ApplicationContext context = new ClassPathXmlApplicationContext("context.xml");
        MySqlDAO mySqlDAO = (MySqlDAO) context.getBean("mySqlDAO");
//        mySqlDAO.insert(mp3);
//        mySqlDAO.delete(4);

//        System.out.println(mySqlDAO.getMp3ListByAuthor("trahim"));

//        System.out.println(mySqlDAO.getStat());
        //
        System.out.println(mySqlDAO.insert(mp3));
    }
}
