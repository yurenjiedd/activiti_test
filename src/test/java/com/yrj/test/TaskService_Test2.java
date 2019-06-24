package com.yrj.test;

import org.activiti.engine.RuntimeService;
import org.activiti.engine.TaskService;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.Task;
import org.activiti.engine.task.TaskQuery;
import org.activiti.engine.test.ActivitiRule;
import org.activiti.engine.test.Deployment;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:activiti-context.xml"})
public class TaskService_Test2 {

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

        }
//        根据任务处理人获取相关任务
//        List<Task> taskList = activitiRule.getTaskService().createTaskQuery().taskAssignee("yrj").list();

//        根据任务拥有者获取相关任务
//        List<Task> taskList = activitiRule.getTaskService().createTaskQuery().taskOwner("yrj").list();

//        根据任务处理候选人获取相关任务
        List<Task> taskList = activitiRule.getTaskService().createTaskQuery().taskCandidateUser("yrj").list();

        for (Task task : taskList) {
            System.err.println("task.getAssignee():"+task.getAssignee());
            System.err.println("task.getOwner():"+task.getOwner());

//            完成签收这个时候就任务Task的Assignee属性就有了值
            taskService.claim(task.getId(),"Mr yrj");
        }
        System.err.println("List<Task>.size:"+taskList.size());
    }
}
