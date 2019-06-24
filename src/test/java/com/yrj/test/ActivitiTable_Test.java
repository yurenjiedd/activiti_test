package com.yrj.test;

import org.activiti.engine.ManagementService;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.impl.bpmn.helper.SignalThrowingEventListener;
import org.activiti.engine.impl.interceptor.Command;
import org.activiti.engine.impl.interceptor.CommandContext;
import org.activiti.engine.impl.persistence.entity.ByteArrayEntity;
import org.activiti.engine.impl.persistence.entity.ByteArrayEntityImpl;
import org.activiti.engine.test.ActivitiRule;
import org.activiti.engine.test.Deployment;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.Map;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:activiti-context.xml"})
public class ActivitiTable_Test {

    @Autowired
    RuntimeService runtimeService;

    @Rule
    @Autowired
    public ActivitiRule activitiRule ;

    /*查询所有的表*/
    @Test
    @Deployment(resources= {"test07.bpmn"})
    public void test01(){
        ManagementService managementService = activitiRule.getManagementService();
//        Key是表名，Value是表的记录个数
        Map<String, Long> tableCount = managementService.getTableCount();
        System.err.println("Map<String, Long>:"+tableCount);
    }

    /*删除所有的表*/
    @Test
    @Deployment(resources= {"test07.bpmn"})
    public void test02(){
        ManagementService managementService = activitiRule.getManagementService();
        managementService.executeCommand(new Command<Object>() {
            @Override
            public Object execute(CommandContext commandContext) {
//                删除所有的表结构
                commandContext.getDbSqlSession().dbSchemaDrop();
                return null;
            }
        });
    }

    /*部署bpmn文件*/
    @Test
    @Deployment(resources= {"test07.bpmn"})
    public void test03(){
        activitiRule.getRepositoryService().createDeployment().addClasspathResource("test07.bpmn").name("流程部署测试").deploy();
    }

    /*手动向act_ge_bytearray表中插入数据*/
    @Test
    @Deployment(resources= {"test07.bpmn"})
    public void test04(){
        ManagementService managementService = activitiRule.getManagementService();
        managementService.executeCommand(new Command<Object>() {
            @Override
            public Object execute(CommandContext commandContext) {
//                创建一条数据
                ByteArrayEntity entity=new ByteArrayEntityImpl();
                entity.setName("流程部署测试");
                entity.setBytes("这是一个测试".getBytes());
                commandContext.getByteArrayEntityManager().insert(entity);
                return null;
            }
        });

    }

}
