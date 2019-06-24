package com.yrj.test;

import org.activiti.engine.*;
import org.activiti.engine.history.*;
import org.activiti.engine.repository.Deployment;
import org.activiti.engine.repository.DeploymentBuilder;
import org.activiti.engine.repository.ProcessDefinition;
import org.activiti.engine.runtime.Execution;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.Task;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TestHistory {
    static ProcessEngine processEngine;
    public static void main(String[] args) {

        sunmitTaskFormData();

//        查询历史流程实例记录(act_hi_procinst表)
        List<HistoricProcessInstance>  historicProcessInstances = processEngine.getHistoryService() // 历史任务Service
                .createHistoricProcessInstanceQuery().list();
        for (HistoricProcessInstance processInstance : historicProcessInstances) {
            System.err.println(processInstance+"--"+processInstance.getId()+"--"+processInstance.getName());
        }
        System.err.println("List<HistoricProcessInstance>:"+historicProcessInstances.size());

//        历史活动记录查询(act_hi_actinst表)
        List<HistoricActivityInstance> historicActivityInstances = processEngine.getHistoryService().createHistoricActivityInstanceQuery().list();
        for (HistoricActivityInstance hai : historicActivityInstances) {
            System.err.println("任务ID:"+hai.getId());
            System.err.println("流程实例ID:"+hai.getProcessInstanceId());
            System.err.println("活动名称："+hai.getActivityName());
            System.err.println("办理人："+hai.getAssignee());
            System.err.println("开始时间："+hai.getStartTime());
            System.err.println("结束时间："+hai.getEndTime());
            System.err.println("===========================");
        }
        System.out.println("List<HistoricActivityInstance>:"+historicActivityInstances.size());

//        Task的历史记录(act_hi_taskinst表)
        List<HistoricTaskInstance> list = processEngine.getHistoryService().createHistoricTaskInstanceQuery().list();
        for (HistoricTaskInstance historicTaskInstance : list) {
            System.err.println(historicTaskInstance+"--"+historicTaskInstance.getName());
        }
        System.err.println("List<HistoricTaskInstance>:"+list.size()+"---------------------");

//        Task的Form表单的Data数据的历史记录，listPage(开始点，查询多少数据):查询指定数量的数据，从0开始
        List<HistoricDetail> historicDetails = processEngine.getHistoryService().createHistoricDetailQuery().listPage(0, 100);
        for (HistoricDetail historicDetail : historicDetails) {
            System.err.println(historicDetail+"--"+historicDetail.getTime());
        }
        System.err.println("List<HistoricDetail>:"+historicDetails.size()+"----------");

//        流程变量的历史记录(act_hi_varinst表)
        List<HistoricVariableInstance> historicVariableInstances = processEngine.getHistoryService()
                .createHistoricVariableInstanceQuery().listPage(0, 100);
        for (HistoricVariableInstance historicVariableInstance : historicVariableInstances) {
            System.err.println(historicVariableInstance+"--"+historicVariableInstance.getVariableName()+"--"
                    +historicVariableInstance.getVariableTypeName()+"--"+historicVariableInstance.getValue());
        }

        System.err.println("List<HistoricVariableInstance>:"+historicVariableInstances.size());

    }

//    提交表单数据
    private static void sunmitTaskFormData() {
        setProcessVariables();
        TaskService taskService = processEngine.getTaskService();
        Task task = taskService.createTaskQuery().singleResult();
        System.err.println(task.getId()+"--"+task.getName());

        Map<String,String> map=new HashMap<>();
        map.put("name","余仁杰");
        map.put("commit","false");

        processEngine.getFormService().submitTaskFormData(task.getId(),map);
    }

    //    设置流程变量
    private static void setProcessVariables() {
        ProcessInstance processInstance = getProcessInstance();
        RuntimeService runtimeService = processEngine.getRuntimeService();
        List<Execution> list = runtimeService.createExecutionQuery().list();

//        Execution:流程执行线路，或者执行环境
        for (Execution execution : list) {
            System.err.println(execution+"--"+execution.getId()+"--"+execution.getName());
        }

        System.err.println(list.size());

        runtimeService.setVariable(list.get(0).getId(),"name","jie");
    }

    //    创建流程实例
    private static ProcessInstance getProcessInstance() {
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

        Map<String,Object> map=new HashMap<>();
        map.put("name","yrj");
        map.put("commit",true);

//        运行流程服务（RuntimeService）
        RuntimeService runtimeService = processEngine.getRuntimeService();
//        运行流程实例（ProcessInstance）
        ProcessInstance processInstance = runtimeService.startProcessInstanceById(processDefinition.getId(),map);
        return  processInstance;
    }

//    启动流程
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
