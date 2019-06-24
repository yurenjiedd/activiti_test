package com.yrj.test;

import org.activiti.engine.*;
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

public class Test01 {
    public static void main(String[] args) {
//        创建基于内存模式的ProcessEngineConfiguration
        ProcessEngineConfiguration processEngineConf = ProcessEngineConfiguration
                .createStandaloneInMemProcessEngineConfiguration();

//        createProcessEngineConfigurationFromResourceDefault()：由XML文件创建基于内存模式的ProcessEngineConfiguration！
//

        ProcessEngine processEngine = processEngineConf.buildProcessEngine();
        String name = processEngine.getName();
        System.err.println("流程引擎名称：" + name + ",版本为：" + ProcessEngine.VERSION);

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
//        运行流程实例（ProcessInstance）
        ProcessInstance processInstance = runtimeService.startProcessInstanceById(processDefinition.getId());
        System.err.println(processInstance.getProcessDefinitionKey() + "---" + processInstance.getProcessDefinitionId());

//        流程任务服务（TaskService）
        TaskService taskService = processEngine.getTaskService();
//        当流程实例对象不为null且没有执行完毕时，不断获取Task任务
        while (processInstance != null && !processInstance.isEnded()) {
            List<Task> list = taskService.createTaskQuery().list();
            Scanner scanner = new Scanner(System.in);
            for (Task task : list) {
                System.err.println(task.getId() + "--" + task.getName());

                Map<String, Object> map = new HashMap<>();

//            form表单服务（FormService），设置流程变量
                FormService formService = processEngine.getFormService();
//            Task表单中所有的流程变量（TaskFormData）
                TaskFormData taskFormData = formService.getTaskFormData(task.getId());
                List<FormProperty> formProperties = taskFormData.getFormProperties();

                for (FormProperty property : formProperties) {
                    System.out.println(property.getName() + ":");
//                判断类型   StringFormType:String类型    DateFormType：Date类型    BooleanFormType:Boolean类型
                    if (StringFormType.class.isInstance(property.getType())) {
                        String s = scanner.nextLine();
                        map.put(property.getId(), s);
                    } else if (BooleanFormType.class.isInstance(property.getType())) {
                        String s = scanner.nextLine();
                        boolean b=false;
                        try {
                            b = Boolean.parseBoolean(s);
                        }catch (Exception e){
                            System.out.println(e);
                        }
                        map.put(property.getId(), b);
                    }
                    System.out.println("输入完毕-----------");
                }

//                完成此次任务，并设置下一步的流程变量
                taskService.complete(task.getId(), map);

//            taskService.complete(task.getId());

//                注意，流程改变后，原来的流程运行实例还是原来的状态，所以我们要获取最新的流程运行实例对象
                processInstance = processEngine.getRuntimeService().createProcessInstanceQuery()
                        .processInstanceId(processInstance.getId()).singleResult();

            }
        }
    }
}

