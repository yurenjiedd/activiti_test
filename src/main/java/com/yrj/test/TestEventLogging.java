package com.yrj.test;

import com.yrj.event.MyActivitiEventListener;
import org.activiti.engine.*;
import org.activiti.engine.delegate.event.ActivitiEventType;
import org.activiti.engine.delegate.event.impl.ActivitiEventImpl;
import org.activiti.engine.event.EventLogEntry;
import org.activiti.engine.form.FormProperty;
import org.activiti.engine.form.TaskFormData;
import org.activiti.engine.impl.form.BooleanFormType;
import org.activiti.engine.impl.form.StringFormType;
import org.activiti.engine.repository.Deployment;
import org.activiti.engine.repository.DeploymentBuilder;
import org.activiti.engine.repository.ProcessDefinition;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.Task;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

public class TestEventLogging {
    static ProcessEngine processEngine;
    static ProcessInstance processInstance;
    public static void main(String[] args) {
        startAcitvit();


        List<EventLogEntry> eventLogEntrys = processEngine.getManagementService()
                .getEventLogEntriesByProcessInstanceId(processInstance.getProcessInstanceId());

        for (EventLogEntry eventLogEntry : eventLogEntrys) {
            System.err.println(eventLogEntry.getType()+"--"+new String(eventLogEntry.getData()));
        }
        System.err.println("List<EventLogEntry>:"+eventLogEntrys.size());
    }

    private static void startAcitvit() {
        processEngine = getProcessEngine();

        //        读取bpmn文件
        RepositoryService repositoryService = processEngine.getRepositoryService();
        DeploymentBuilder deployment = repositoryService.createDeployment();
        Deployment deploy = deployment.addClasspathResource("test01.bpmn").deploy();

//        获取流程定义对象（ProcessDefinition）
        String id = deploy.getId();
        ProcessDefinition processDefinition = repositoryService.createProcessDefinitionQuery()
                .deploymentId(id).singleResult();
        System.err.println("流程定义ID:" + processDefinition.getId() + ",流程定义名称:" + processDefinition.getName());

//        运行流程服务（RuntimeService）
        RuntimeService runtimeService = processEngine.getRuntimeService();

//        发布事件
        runtimeService.addEventListener(new MyActivitiEventListener());
//        监听事件
        runtimeService.dispatchEvent(new ActivitiEventImpl(ActivitiEventType.CUSTOM));

//        运行流程实例（ProcessInstance）
        processInstance = runtimeService.startProcessInstanceById(processDefinition.getId());
        System.err.println(processInstance.getProcessDefinitionKey() + "---" + processInstance.getProcessDefinitionId());


        startTake();


    }

    private static void startTake() {
        //        流程任务服务（TaskService）
        TaskService taskService = processEngine.getTaskService();
//        当流程实例对象不为null且没有执行完毕时，不断获取Task任务
        Task task = taskService.createTaskQuery().list().get(0);
        Map<String,Object> map=new HashMap<>();
        map.put("name","余仁杰-Entry-1");
        map.put("commit",true);
        taskService.complete(task.getId(),map);
    }

    private static ProcessEngine getProcessEngine() {
        //        由XML文件创建基于内存模式的ProcessEngineConfiguration！
        ProcessEngineConfiguration processEngineConf = ProcessEngineConfiguration
                .createProcessEngineConfigurationFromResourceDefault();

        System.err.println("DatabaseType:  "+processEngineConf.getDatabaseType());
        System.err.println("JdbcDriver:  "+processEngineConf.getJdbcDriver());
        System.err.println("JdbcUrl:  "+processEngineConf.getJdbcUrl());

        ProcessEngine processEngine = processEngineConf.buildProcessEngine();
        String name = processEngine.getName();
        System.err.println("流程引擎名称：" + name + ",版本为：" + ProcessEngine.VERSION);
        return processEngine;
    }
}
