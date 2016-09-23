package com.ojass.sathelper.controller;

import com.ojass.sathelper.entity.Role;
import com.ojass.sathelper.entity.Subject;
import com.ojass.sathelper.entity.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.web.bind.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.validation.Valid;
import java.security.Principal;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Controller
@Transactional
public class UsersController {

    @PersistenceContext
    private EntityManager em;

    private static final Logger logger = LoggerFactory.getLogger(UsersController.class);

    @RequestMapping(value = "/users/{userId}", method = RequestMethod.GET)
    @ResponseBody
    public User get(@PathVariable Long userId) {
        logger.info("Fetching user with id: {}", userId);
        User user = em.find(User.class, userId);
        logger.info("Found user: {}", user);
        return user;
    }

    @RequestMapping(value = "/register/{roleName}", method = RequestMethod.POST)
    @ResponseBody
    public ResponseEntity<String> create(@RequestBody @Valid User user, @PathVariable String roleName) {
        logger.info("Creating new user: {}", user);
        if (findUserByUserName(user.getEmail()) != null) {

            Set<Role> s = new HashSet<Role>();
            Role role = (Role) em.createQuery("select r from Role r where r.name=:role").setParameter("role", roleName).getSingleResult();
            s.add(role);
            user.setRoles(s);
            em.persist(user);
        } else {
            return new ResponseEntity<String>("error", HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<String>(user.getId().toString(), HttpStatus.CREATED);
    }

    @RequestMapping(value = "/register/subjects", method = RequestMethod.GET)
    @ResponseBody
    public List<Subject> getSubejcts() {
        return em.createQuery("Select s from Subject s ").getResultList();
    }


    @RequestMapping(value = "/students/{subject}", method = RequestMethod.GET)
    @ResponseBody
    public List<User> listStudents(@PathVariable String subject) {
        logger.info("retreiving : ", subject);
        return em.createQuery("Select u from User u join u.subjects s join u.roles r where s.name=:subject and r.name='ROLE_STUDENT'")
                .setParameter("subject", subject).getResultList();
    }


    @RequestMapping(value = "/users/subject", method = RequestMethod.GET)
    @ResponseBody
    public Set<Subject> getUsersSubject(@AuthenticationPrincipal Principal principal) {
        //Ideally an inmemory cache would help
        User loggedInUser = findUserByUserName((String) ((UsernamePasswordAuthenticationToken) principal).getPrincipal());
        return loggedInUser.getSubjects();
    }

    @RequestMapping(value = "/users/login", method = RequestMethod.POST)
    @ResponseBody
    public ResponseEntity<String> login(@RequestBody User user) {
        try {
            User u = findUserByUserName(user.getEmail());

            if (u != null && u.getPassword().equals(user.getPassword())) {
                //TODO:This needs to be done in a token generator which may implement JWT.
                StringBuilder roles = new StringBuilder();
                for (Role r : u.getRoles()) {
                    roles.append(r.getName()).append("~");
                }
                String token = u.getEmail() + "=" + roles.toString();
                return new ResponseEntity<String>("Bearer " + token, HttpStatus.OK);
            }
        } catch (Exception ex) {
            logger.error("not found");
        }
        return new ResponseEntity<String>("Unauthorized", HttpStatus.UNAUTHORIZED);

    }


    /**
     * Retrieves a user from database
     *
     * @param userName
     * @return
     */
    private User findUserByUserName(String userName) {
        User u = null;
        try {
            logger.info("Fetching user with name: {}", userName);
            u = (User) em.createQuery("Select u from User u where u.email=:userName ")
                    .setParameter("userName", userName).getSingleResult();
        } catch (NoResultException ex) {
            logger.info("NO Result for name: ", userName);
        }
        return u;
    }

}
