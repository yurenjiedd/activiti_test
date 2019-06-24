package com.yrj.test;

import org.activiti.engine.RuntimeService;
import org.activiti.engine.TaskService;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.Task;
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
public class TaskService_Test {

    @Autowired
    RuntimeService runtimeService;

    @Rule
    @Autowired
    public ActivitiRule activitiRule ;

    @Test
    @Deployment(resources= {"test06.bpmn"})
    public void test01(){

        Map<String,Object> map=new HashMap<>();
        map.put("aaa","asdffghjkl");
        map.put("name","yrj");
        map.put("commit",false);
        map.put("commit1","false");
        map.put("commit2",false);
        ProcessInstance myProcess_task = runtimeService.startProcessInstanceByKey("myProcess_Task", map);

        TaskService taskService = activitiRule.getTaskService();
        List<Task> list = taskService.createTaskQuery().list();
        for (Task task : list) {

            System.err.println("task:="+task);
            System.err.println("task.getDescription():"+task.getDescription());

            taskService.setVariable(task.getId(),"test01","测试1");
            taskService.setVariable(task.getId(),"test02","测试2");

//            查询该任务节点中流程变量
            Map<String, Object> variables = taskService.getVariables(task.getId());
//            查询该任务节点中局部流程变量
            Map<String, Object> variablesLocal = taskService.getVariablesLocal(task.getId());
//            查询该流程中局部流程变量
            Map<String, Object> variables1 = runtimeService.getVariables(task.getExecutionId());

            System.err.println("variables::"+variables);
            System.err.println("variablesLocal::"+variablesLocal);
            System.err.println("variables1::"+variables1);

            taskService.complete(task.getId(),map);
        }

    }
}
