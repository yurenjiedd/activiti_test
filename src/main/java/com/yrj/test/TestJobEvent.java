package com.yrj.test;

import com.yrj.event.MyActivitiEventListener;
import org.activiti.engine.*;
import org.activiti.engine.delegate.event.ActivitiEventType;
import org.activiti.engine.delegate.event.impl.ActivitiEventImpl;
import org.activiti.engine.event.EventLogEntry;
import org.activiti.engine.repository.Deployment;
import org.activiti.engine.repository.DeploymentBuilder;
import org.activiti.engine.repository.ProcessDefinition;
import org.activiti.engine.runtime.Job;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.Task;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TestJobEvent {
    static ProcessEngine processEngine;
    static ProcessInstance processInstance;
    public static void main(String[] args) {
        startAcitvit();
    }

    private static void startAcitvit() {
        processEngine = getProcessEngine();

        //        读取bpmn文件
        RepositoryService repositoryService = processEngine.getRepositoryService();
        DeploymentBuilder deployment = repositoryService.createDeployment();
        Deployment deploy = deployment.addClasspathResource("job/my-process_job.bpmn").deploy();

//        获取流程定义对象（ProcessDefinition）
        String id = deploy.getId();
        ProcessDefinition processDefinition = repositoryService.createProcessDefinitionQuery()
                .deploymentId(id).singleResult();
        System.err.println("流程定义ID:" + processDefinition.getId() + ",流程定义名称:" + processDefinition.getName());

//        运行流程服务（RuntimeService）
        RuntimeService runtimeService = processEngine.getRuntimeService();

//        运行流程实例（ProcessInstance）
        processInstance = runtimeService.startProcessInstanceById(processDefinition.getId());
        System.err.println(processInstance.getProcessDefinitionKey() + "---" + processInstance.getProcessDefinitionId());


        startTake();


    }

    private static void startTake() {
        List<Job> jobList = processEngine.getManagementService().createTimerJobQuery().listPage(0, 100);
        for (Job job : jobList) {
            System.err.println("定时任务默认重试次数:"+job.getRetries());
        }
        System.err.println("jobList.size()"+jobList.size());

        System.err.println("---------end---------");

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
