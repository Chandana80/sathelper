package com.ojass.sathelper.util;

import com.ojass.sathelper.entity.Role;
import com.ojass.sathelper.entity.Subject;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;

@Transactional
public class AppInit implements ApplicationListener<ContextRefreshedEvent> {

    @PersistenceContext
    private EntityManager em;

    @Override
    public void onApplicationEvent(final ContextRefreshedEvent event) {
        // Initialize with default subject and roles
        List<Subject> subjects = em.createQuery("select s from Subject s").getResultList();
        if (subjects == null || subjects.size() == 0) {
            //create subjects
            String[] names = new String[]{"English", "Math"};
            for (String name : names) {
                Subject s = new Subject();
                s.setName(name);
                em.persist(s);
            }

        }

        List<Role> roles = em.createQuery("select r from  Role r").getResultList();
        if (roles.size() == 0) {
            String[] names = new String[]{"ROLE_TEACHER", "ROLE_STUDENT"};
            for (String name : names) {
                Role s = new Role();
                s.setName(name);
                em.persist(s);
            }
        }


    }

}