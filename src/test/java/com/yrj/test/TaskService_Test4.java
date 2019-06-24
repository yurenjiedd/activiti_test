package com.yrj.test;

import org.activiti.engine.RuntimeService;
import org.activiti.engine.TaskService;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.Attachment;
import org.activiti.engine.task.IdentityLink;
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
public class TaskService_Test4 {

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

            InputStream is=new FileInputStream("E:\\JavaeeProject\\my_activiti6\\src\\test\\resources\\1.jpg");
            Attachment attachment = taskService.createAttachment("类型：url", task.getId(),
                    myProcess_task.getId(), "给定attachment名称",
                    "附件描述", is);


            /*Attachment attachment = taskService.createAttachment("类型：url", task.getId(),
                    myProcess_task.getId(), "给定attachment名称",
                    "附件描述", "1.jpg");*/

            System.err.println("task.id:"+task.getId());
            taskService.complete(task.getId());
        }

        System.err.println("List<Task>:"+list.size());

        List<Attachment> taskAttachments = taskService.getTaskAttachments(list.get(0).getId());
        for (Attachment attachment : taskAttachments) {
            System.err.println("attachment::"+ToStringBuilder.reflectionToString(attachment));
        }
        System.err.println("List<Attachment>:"+taskAttachments.size());

    }
}
