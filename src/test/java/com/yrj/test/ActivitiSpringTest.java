package com.yrj.test;

import org.activiti.engine.FormService;
import org.activiti.engine.ProcessEngine;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.TaskService;
import org.activiti.engine.form.FormProperty;
import org.activiti.engine.form.TaskFormData;
import org.activiti.engine.impl.DeploymentQueryImpl;
import org.activiti.engine.impl.form.BooleanFormType;
import org.activiti.engine.impl.form.StringFormType;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.Task;
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
import java.util.Scanner;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:activiti-context.xml"})
public class ActivitiSpringTest {

    @Rule
    @Autowired
    public ActivitiRule activitiRule;

    @Autowired
    RuntimeService runtimeService;

    @Autowired
    ProcessEngine processEngine;

    @Autowired
    TaskService taskService;

    @Test
    @Deployment(resources = {"test02.bpmn"})
    public void test01(){

        runtimeService.startProcessInstanceByKey("myProcess_holle");

        List<Task> list = taskService.createTaskQuery().list();

        FormService formService = processEngine.getFormService();

        for (Task task : list) {
            System.err.println(task.getId()+"--"+task.getName());
            TaskFormData taskFormData = formService.getTaskFormData(task.getId());
            List<FormProperty> formProperties = taskFormData.getFormProperties();
            for (FormProperty formProperty : formProperties) {
                System.err.println(formProperty.getValue());
            }
        }

        System.err.println("List<Task>.size():"+list.size());

    }
}
