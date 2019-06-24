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
public class Trigger_Test {

    @Autowired
    RuntimeService runtimeService;

    @Rule
    @Autowired
    public ActivitiRule activitiRule ;

    @Test
    @Deployment(resources= {"test03.bpmn"})
    public void test01(){
        RuntimeService runtimeService = activitiRule.getRuntimeService();

        ProcessInstance processSignal = runtimeService.startProcessInstanceByKey("myProcess_signal");
        System.err.println("myProcess_trigger:"+processSignal.getName()+"--"+processSignal.getId()+"--"+processSignal);

//        执行指定的流程实例
//        Execution execution = runtimeService.createExecutionQuery().processInstanceId(myProcess_trigger.getId())
//                .onlyChildExecutions().singleResult();

//        activityId("trigger_test")：只选择包含带有给定id的活动的执行，也就是说必须是指定receiveTask的id
        Execution execution = runtimeService.createExecutionQuery().activityId("trigger_test").singleResult();
        System.err.println("Execution:"+execution);

        runtimeService.trigger(execution.getId());

        execution = runtimeService.createExecutionQuery().activityId("trigger_test").singleResult();
        System.err.println("Execution:"+execution);
    }
}
