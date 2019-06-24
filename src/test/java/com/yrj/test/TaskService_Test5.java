package com.yrj.test;

import org.activiti.engine.RuntimeService;
import org.activiti.engine.TaskService;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.Attachment;
import org.activiti.engine.task.Comment;
import org.activiti.engine.task.Event;
import org.activiti.engine.task.Task;
import org.activiti.engine.test.ActivitiRule;
import org.activiti.engine.test.Deployment;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:activiti-context.xml"})
public class TaskService_Test5 {

    @Autowired
    RuntimeService runtimeService;

    @Rule
    @Autowired
    public ActivitiRule activitiRule ;

    @Test
    @Deployment(resources= {"test06.bpmn"})
    public void test01() throws FileNotFoundException {

        Map<String,Object> map=new HashMap<>();
        map.put("aaa","asdffghjkl");
        ProcessInstance myProcess_task = runtimeService.startProcessInstanceByKey("myProcess_Task",map);

        TaskService taskService = activitiRule.getTaskService();


        List<Task> list = taskService.createTaskQuery().list();
        for (Task task : list) {
//            指定任务拥有人
//            taskService.setOwner(task.getId(),"yrj");
//            指定任务的处理人
            taskService.setAssignee(task.getId(),"yrj");

            taskService.addComment(task.getId(),myProcess_task.getId(),"呵呵呵呵。。。。");
            taskService.addComment(task.getId(),myProcess_task.getId(),"哈哈哈哈。。。。");

            taskService.complete(task.getId());
        }

        System.err.println("List<Task>:"+list.size());

//        获取指定任务的任务消息附件
        List<Comment> taskComments = taskService.getTaskComments(list.get(0).getId());
        for (Comment taskComment : taskComments) {
            System.err.println("taskComment::"+ToStringBuilder.reflectionToString(taskComment));
        }
        System.err.println("List<taskComment>:"+taskComments.size());

//        获取与给定任务相关的所有事件，Event也包含了Task中的Comment信息
        List<Event> taskEvents = taskService.getTaskEvents(list.get(0).getId());
        for (Event taskEvent : taskEvents) {
            System.err.println("taskEvent::"+ToStringBuilder.reflectionToString(taskEvent));
        }

    }
}
