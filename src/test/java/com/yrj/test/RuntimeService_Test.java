package com.yrj.test;

import org.activiti.engine.RepositoryService;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.repository.ProcessDefinition;
import org.activiti.engine.runtime.Execution;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.runtime.ProcessInstanceBuilder;
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
public class RuntimeService_Test {

    @Autowired
    RuntimeService runtimeService;

    @Rule
    @Autowired
    public ActivitiRule activitiRule ;

    @Test
    @Deployment(resources= {"test02.bpmn"})
    public void test(){

        RepositoryService repositoryService = activitiRule.getRepositoryService();

        ProcessDefinition processDefinition = repositoryService.createProcessDefinitionQuery().list().get(0);
        System.err.println("processDefinition = "+processDefinition+", " +
                "version = [{}] "+processDefinition.getVersion()+
                ", key = " +processDefinition.getKey()+
                ", id = "+processDefinition.getId());

        // 根据流程定义id启动流程实例
//        ProcessInstance processInstance = runtimeService.startProcessInstanceById(processDefinition.getId());

        // 根据流程定义key启动流程实例(bpmn流程图的id)
//        ProcessInstance processInstance = runtimeService.startProcessInstanceByKey("myProcess_holle");

        // 启动流程实例时带上参数
        Map<String,Object> variables = new HashMap<String, Object>();
        variables.put("name","余仁杰");
        variables.put("age","88");
        ProcessInstance processInstance = runtimeService.startProcessInstanceByKey("myProcess_holle",variables);

        // 根据ProcessInstanceBuilder启动流程实例
//        ProcessInstanceBuilder processInstanceBuilder = runtimeService.createProcessInstanceBuilder();
//        processInstanceBuilder.businessKey("myProcess_holle")
//                .processDefinitionKey(processDefinition.getKey())
////                .variables(variables)
//                .start();

        // 流程启动后获取变量
        Map<String,Object> variables1 = runtimeService.getVariables(processInstance.getId());
        System.err.println(variables1+"=========");

        // 查询流程实例对象
        ProcessInstance processInstance1 = runtimeService.createProcessInstanceQuery()
                .processInstanceId(processInstance.getId())
                .singleResult();
        System.err.println("ProcessInstance:"+processInstance1.getId()+"-=-"+processInstance1.getName());

        // 执行对象查询
        List<Execution> executionList = runtimeService.createExecutionQuery()
                .listPage(0,100);

        for (Execution execution : executionList) {
            System.err.println("execution:"+execution.getId()+"-=-"+execution.getName()+"--"+execution);
        }
        System.err.println(executionList.size()+"------------");
    }
}
