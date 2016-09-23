package com.ojass.sathelper.controller;

import com.ojass.sathelper.entity.Result;
import com.ojass.sathelper.entity.TestResult;
import com.ojass.sathelper.util.Mailer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.TransactionSynchronizationAdapter;
import org.springframework.transaction.support.TransactionSynchronizationManager;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.validation.Valid;

/**
 * Created by Chandana on 9/16/2016.
 */
@Controller
@Transactional
public class TestResultController {

    @PersistenceContext
    private EntityManager em;

    @Autowired
    private Mailer mailer;

    private static final Logger logger = LoggerFactory.getLogger(UsersController.class);

    @RequestMapping(value = "/testResults", method = RequestMethod.POST)
    @ResponseBody
    public void report(@RequestBody @Valid TestResult testResult) {
        for (Result result : testResult.getResults()) {
            //set Many to One part of the result after deserialization
            result.setTestResult(testResult);
        }
        final TestResult newResult = em.merge(testResult);

        //Send out mails only if the transaction is commited.
        TransactionSynchronizationManager.registerSynchronization(
                new TransactionSynchronizationAdapter() {
                    @Override
                    public void afterCommit() {
                        TestResult tResult = em.find(TestResult.class, newResult.getId());
                        for (Result result : tResult.getResults()) {
                            mailer.sendMails(result.getUser().getEmail(), "Score:" + result.getScore(), result.getSubject().getName());
                        }

                    }
                });
    }

}
