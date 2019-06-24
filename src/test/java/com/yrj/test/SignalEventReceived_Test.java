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
public class SignalEventReceived_Test {

    @Autowired
    RuntimeService runtimeService;

    @Rule
    @Autowired
    public ActivitiRule activitiRule ;

    @Test
    @Deployment(resources= {"test04.bpmn"})
    public void test01(){
        RuntimeService runtimeService = activitiRule.getRuntimeService();

        ProcessInstance myProcess_trigger = runtimeService.startProcessInstanceByKey("myProcess_signal");
        System.err.println("myProcess_trigger:"+myProcess_trigger.getName()+"--"+myProcess_trigger.getId()+"--"+myProcess_trigger);

//        signalEventSubscriptionName(信号名称)：查询该流程中监控 指定名称的 的流程执行对象
        Execution execution = runtimeService.createExecutionQuery().signalEventSubscriptionName("name").singleResult();
        System.err.println("execution::"+execution);

//        signalEventReceived(信号名称)：发送指定的信号
        runtimeService.signalEventReceived("name");
        Execution execution1 = runtimeService.createExecutionQuery().signalEventSubscriptionName("name").singleResult();
        System.err.println("execution::1"+execution);
    }

}
