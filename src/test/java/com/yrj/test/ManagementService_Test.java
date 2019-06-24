package com.yrj.test;

import org.activiti.engine.ManagementService;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.runtime.Job;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.test.ActivitiRule;
import org.activiti.engine.test.Deployment;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.List;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:activiti-context.xml"})
public class ManagementService_Test {

    @Autowired
    RuntimeService runtimeService;

    @Rule
    @Autowired
    public ActivitiRule activitiRule ;

    @Test
    @Deployment(resources= {"test07.bpmn"})
    public void test01(){

        ProcessInstance myProcess_form = activitiRule.getRuntimeService().startProcessInstanceByKey("myProcess_Form");
        ManagementService managementService = activitiRule.getManagementService();
//        普通任务
        /*List<Job> jobs = managementService.createJobQuery().list();
        for (Job job : jobs) {
            System.err.println("job::"+job);
        }
        System.err.println("jobs.size:"+jobs.size());*/

        List<Job> jobs = managementService.createTimerJobQuery().list();
        for (Job job : jobs) {
            System.err.println("job::"+job);
        }
        System.err.println("jobs.size:"+jobs.size());

/*//        执行失败的Job任务
        DeadLetterJobQuery deadLetterJobQuery = managementService.createDeadLetterJobQuery();
//        暂时挂起的Job任务
        SuspendedJobQuery suspendedJobQuery = managementService.createSuspendedJobQuery();*/
    }
}
