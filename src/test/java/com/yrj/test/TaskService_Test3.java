package com.yrj.test;

import org.activiti.engine.RuntimeService;
import org.activiti.engine.TaskService;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.IdentityLink;
import org.activiti.engine.task.Task;
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
public class TaskService_Test3 {

    @Autowired
    RuntimeService runtimeService;

    @Rule
    @Autowired
    public ActivitiRule activitiRule ;

    @Test
    @Deployment(resources= {"test06.bpmn"})
    public void test01(){

        ProcessInstance myProcess_task = runtimeService.startProcessInstanceByKey("myProcess_Task");

        TaskService taskService = activitiRule.getTaskService();


        List<Task> list = taskService.createTaskQuery().list();
        for (Task task : list) {
//            指定任务拥有人
            taskService.setOwner(task.getId(),"yrj");
//            指定任务的处理人
//            taskService.setAssignee(task.getId(),"yrj");
//            指定任务处理的候选人
            taskService.addCandidateUser(task.getId(),"yrj");
            taskService.addCandidateUser(task.getId(),"余仁杰");
            taskService.addCandidateUser(task.getId(),"Mr yrj");

            taskService.addCandidateGroup(task.getId(),"God");

        }

        //        根据任务处理候选人获取相关任务
        List<Task> taskList = activitiRule.getTaskService().createTaskQuery().taskCandidateUser("yrj").list();
        for (Task task : taskList) {
            List<IdentityLink> identityLinksForTask = taskService.getIdentityLinksForTask(task.getId());
            for (IdentityLink identityLink : identityLinksForTask) {
                System.err.println("identityLink::"+identityLink);
            }
        }
    }
}
