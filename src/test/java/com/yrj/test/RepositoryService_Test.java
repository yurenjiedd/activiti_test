package com.yrj.test;

import org.activiti.engine.RepositoryService;
import org.activiti.engine.repository.Deployment;
import org.activiti.engine.repository.DeploymentBuilder;
import org.activiti.engine.repository.DeploymentQuery;
import org.activiti.engine.repository.ProcessDefinition;
import org.activiti.engine.test.ActivitiRule;
import org.junit.Rule;
import org.junit.Test;

import java.util.List;

public class RepositoryService_Test {
    @Rule
    public ActivitiRule activitiRule = new ActivitiRule();   //默认流程配置文件

    @Test
    public void testRepository(){
        RepositoryService repositoryService = activitiRule.getRepositoryService();  //获取RepositoryService对象

        DeploymentBuilder deploymentBuilder = repositoryService.createDeployment();   //流程部署对象
        deploymentBuilder.name("测试部署资源")
                .addClasspathResource("test01.bpmn")
                .addClasspathResource("test02.bpmn");    //部署两个流程定义配置文件

        Deployment deploy = deploymentBuilder.deploy();   //将部署对象与两个定义文件部署到数据库中
        System.err.println("deploy = "+deploy);     //输出这个部署对象

//        DeploymentBuilder deploymentBuilder2 = repositoryService.createDeployment();   //流程部署对象
//        deploymentBuilder2.name("测试部署资源2")
//                .addClasspathResource("test01.bpmn")
//                .addClasspathResource("test02.bpmn");    //部署两个流程定义配置文件
//        deploymentBuilder2.deploy();    //第二次部署

        DeploymentQuery deploymentQuery = repositoryService.createDeploymentQuery();   //通过这个类查询
        Deployment deployment = deploymentQuery.deploymentId(deploy.getId()).singleResult();//根据deploy的ID查询出一个独立的对象
        System.err.println("deployment = "+deployment);     //上下 deploy与deployment 输出的部署对象相同

        List<ProcessDefinition> definitions = repositoryService.createProcessDefinitionQuery()
                .deploymentId(deployment.getId()).listPage(0, 100);//使用流程定义的查询对象,使用listPage因为有两个资源文件

        for (ProcessDefinition processDefinition : definitions){
            System.err.println("processDefinition = "+processDefinition+", " +
                            "version = [{}] "+processDefinition.getVersion()+
                            ", key = " +processDefinition.getKey()+
                            ", id = "+processDefinition.getId());
        }

//        挂起流程定义对象
        repositoryService.suspendProcessDefinitionById(definitions.get(0).getId());

        try{
            System.err.println("启动开始-------------");
            activitiRule.getRuntimeService().startProcessInstanceById(definitions.get(0).getId());
            System.err.println("启动结束-------------");
        }catch (Exception e){
            System.err.println("启动失败-------------");
        }

//        重新开启流程定义对象
        repositoryService.activateProcessDefinitionById(definitions.get(0).getId());

        System.err.println("启动开始-------------");
        activitiRule.getRuntimeService().startProcessInstanceById(definitions.get(0).getId());
        System.err.println("启动结束-------------");
    }

}
