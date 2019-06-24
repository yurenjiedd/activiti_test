package com.yrj.test;

import org.activiti.engine.HistoryService;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.history.*;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.runtime.ProcessInstanceBuilder;
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

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:activiti-context.xml"})
public class HistoryService_Test {

    @Autowired
    RuntimeService runtimeService;

    @Rule
    @Autowired
    public ActivitiRule activitiRule ;

    @Test
    @Deployment(resources= {"test07.bpmn"})
    public void test01(){

        HistoryService historyService =activitiRule.getHistoryService();
        ProcessInstanceBuilder processInstanceBuilder =activitiRule.getRuntimeService().createProcessInstanceBuilder();
        Map<String,Object> map=new HashMap<>();
        map.put("start","开始节点执行");
        map.put("message","my test message" );
        map.put("name","yrj" );
        map.put("age","88" );

        Map<String,Object> transientMap=new HashMap<>();
        map.put("test","传入测试的变量");

        ProcessInstance processInstance = processInstanceBuilder.processDefinitionKey("myProcess_Form")
                .variables(map)//持久变量，会存入数据库表               
                .transientVariables(transientMap)//瞬时变量，不会存入数据库表               
                .start();
//        修改流程变量
        activitiRule.getRuntimeService().setVariable(processInstance.getId(), "name","余仁杰");

        Task task =activitiRule.getTaskService().createTaskQuery().processInstanceId(processInstance.getId()).singleResult();

        System.err.println("task=="+ToStringBuilder.reflectionToString(task));

        //完成任务
//        activitiRule.getTaskService().complete(task.getId(),map);

//        完成任务后才有历史数据，使用表单提交的方式
        Map<String,String> taskMap=new HashMap<>();
        taskMap.put("name","yrj" );
        taskMap.put("age","88" );
        activitiRule.getFormService().submitTaskFormData(task.getId(),taskMap);

//         HistoricProcessInstance 历史流程实例实体类
        List<HistoricProcessInstance> hpiList = historyService.createHistoricProcessInstanceQuery().list();
        for (HistoricProcessInstance historicProcessInstance : hpiList) {
            System.err.println("historicProcessInstance::"+ToStringBuilder.reflectionToString(historicProcessInstance));
        }

//        HistoricActivityInstance 单个活动节点执行的信息
        List<HistoricActivityInstance> haiList = historyService.createHistoricActivityInstanceQuery().list();
        for (HistoricActivityInstance historicActivityInstance : haiList) {
            System.err.println("historicActivityInstance::"+ToStringBuilder.reflectionToString(historicActivityInstance));
        }

//        HistoricDetail 历史流程活动任务详细信息
        List<HistoricDetail> hdList = historyService.createHistoricDetailQuery().list();
        for (HistoricDetail historicDetail : hdList) {
            System.err.println("historicDetail::"+ToStringBuilder.reflectionToString(historicDetail));
        }

//        HistoricTaskInstance 单个任务实例的信息
        List<HistoricTaskInstance> htiList = historyService.createHistoricTaskInstanceQuery().list();
        for (HistoricTaskInstance historicTaskInstance : htiList) {
            System.err.println("historicTaskInstance::"+ToStringBuilder.reflectionToString(historicTaskInstance));
        }

//        HistoricVariableInstance 历史变量实例的信息
        List<HistoricVariableInstance> hviList = historyService.createHistoricVariableInstanceQuery().list();
        for (HistoricVariableInstance historicVariableInstance : hviList) {
            System.err.println("historicVariableInstance::"+ToStringBuilder.reflectionToString(historicVariableInstance));
        }

        ProcessInstanceHistoryLog processInstanceHistoryLog = historyService.createProcessInstanceHistoryLogQuery(processInstance.getId())
                .includeActivities().includeComments()
                .includeFormProperties().includeTasks()
                .includeVariables().includeVariableUpdates()
                .singleResult();
//        HistoricData 历史数据
        List<HistoricData> historicDatas = processInstanceHistoryLog.getHistoricData();
        for (HistoricData historicData : historicDatas) {
            System.err.println("historicData::"+ToStringBuilder.reflectionToString(historicData));
        }


        //删除流程实例历史信息
        historyService.deleteHistoricProcessInstance(processInstance.getId());

    }

}
