package com.yrj.test;

import org.activiti.engine.ManagementService;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.impl.interceptor.Command;
import org.activiti.engine.impl.interceptor.CommandContext;
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
public class ManagementService_Test4 {

    @Autowired
    RuntimeService runtimeService;

    @Rule
    @Autowired
    public ActivitiRule activitiRule ;

    @Test
    @Deployment(resources= {"test07.bpmn"})
    public void test01(){

        ManagementService managementService = activitiRule.getManagementService();
//        查询该流程的流程执行过程对象数据（act_ru_task表）
        String s = managementService.executeCommand(new Command<String>() {
            public String execute(CommandContext commandContext) {
                // 自定义命令实现
//                查询数据库操作
                return "测试自定义的命令进行查询。。";
            }
        });

        System.err.println("maps::"+s);

    }
}
