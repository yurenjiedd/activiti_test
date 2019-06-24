package com.yrj.test;

import org.activiti.engine.RuntimeService;
import org.activiti.engine.runtime.Execution;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.test.ActivitiRule;
import org.activiti.engine.test.Deployment;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:activiti-context.xml"})
public class MessageEventReceived_Test {

    @Autowired
    RuntimeService runtimeService;

    @Rule
    @Autowired
    public ActivitiRule activitiRule ;

    @Test
    @Deployment(resources= {"test05.bpmn"})
    public void test01(){

        RuntimeService runtimeService = activitiRule.getRuntimeService();

        ProcessInstance myProcess_message = runtimeService.startProcessInstanceByKey("myProcess_message");
        System.err.println("myProcess_message:"+myProcess_message.getName()+"--"+myProcess_message.getId()+"--"+myProcess_message);

//        signalEventSubscriptionName(消息名称)：查询该流程中监控 指定名称的 的流程执行对象
        Execution execution = runtimeService.createExecutionQuery().messageEventSubscriptionName("age").singleResult();

        System.err.println("execution=="+execution);

//        messageEventReceived(消息名称,ExecutionID)：发布指定名称的消息
        runtimeService.messageEventReceived("age",execution.getId());
    }

}
