package com.yrj.test;

import org.activiti.engine.RepositoryService;
import org.activiti.engine.repository.Deployment;
import org.activiti.engine.repository.DeploymentBuilder;
import org.activiti.engine.repository.DeploymentQuery;
import org.activiti.engine.repository.ProcessDefinition;
import org.activiti.engine.task.IdentityLink;
import org.activiti.engine.test.ActivitiRule;
import org.junit.Rule;
import org.junit.Test;

import java.util.List;

public class RepositoryService_Test2 {
    @Rule
    public ActivitiRule activitiRule = new ActivitiRule();   //默认流程配置文件

    @Test
    @org.activiti.engine.test.Deployment(resources= {"test02.bpmn"})
    public void testRepository(){
        RepositoryService repositoryService = activitiRule.getRepositoryService();

        ProcessDefinition processDefinition = repositoryService.createProcessDefinitionQuery().list().get(0);

        System.err.println("processDefinition = "+processDefinition+", " +
                        "version = [{}] "+processDefinition.getVersion()+
                        ", key = " +processDefinition.getKey()+
                        ", id = "+processDefinition.getId());

        repositoryService.addCandidateStarterUser(processDefinition.getId(),"user");   //按照用户的ID（user）来添加用户
        repositoryService.addCandidateStarterGroup(processDefinition.getId(),"groupM");    //添加用户组

//        IdentityLink:流程与用户身份关系
        List<IdentityLink> identityLinkList = repositoryService
                .getIdentityLinksForProcessDefinition(processDefinition.getId());    //通过流程定义获取我们设置的关系

        for (IdentityLink identityLink : identityLinkList){
            System.err.println("identityLink = "+identityLink);    //日志输出设置的关系
        }

        repositoryService.deleteCandidateStarterGroup(processDefinition.getId(),"groupM");    //删除用户组
        repositoryService.deleteCandidateStarterUser(processDefinition.getId(),"user");    //删除user用户

    }

}
