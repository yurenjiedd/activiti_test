package com.yrj.test;

import org.activiti.engine.FormService;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.form.FormProperty;
import org.activiti.engine.form.StartFormData;
import org.activiti.engine.form.TaskFormData;
import org.activiti.engine.repository.ProcessDefinition;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.Task;
import org.activiti.engine.test.ActivitiRule;
import org.activiti.engine.test.Deployment;
import org.apache.commons.lang3.builder.ToStringBuilder;
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
public class FormService_Test {

    @Autowired
    RuntimeService runtimeService;

    @Rule
    @Autowired
    public ActivitiRule activitiRule ;

    @Test
    @Deployment(resources= {"test07.bpmn"})
    public void test01(){
        FormService formService = activitiRule.getFormService();
        List<ProcessDefinition> pdlist = activitiRule.getRepositoryService().createProcessDefinitionQuery().list();
        for (ProcessDefinition processDefinition : pdlist) {

//            -------------------------- 开 始 节 点--------------------------
            // startFormKey 对应流程文件中的startEvent中的属性activiti:formKey
            String startFormKey = formService.getStartFormKey(processDefinition.getId());

            System.err.println("startFormKey--" + startFormKey+"----------"+processDefinition.getId());

            //获取开始表单中的数据 对应的是startEvent标签下的扩展属性标签extensionElements下的activiti:formProperty标签的属性
            //每一个formProperty对应下面的一个FormProperty
            StartFormData startFormData = formService.getStartFormData(processDefinition.getId());
            List<FormProperty> formProperties = startFormData.getFormProperties();
            for (FormProperty formProperty : formProperties) {
                System.err.println("formProperty--" + ToStringBuilder.reflectionToString(formProperty));
            }
            System.err.println("List<FormProperty>.size--" + formProperties.size());

        }

        //启动方式一，用RuntimeService启动

//      ProcessInstance processInstance = rule.getRuntimeService().startProcessInstanceByKey("myProcess");
        //启动方式二,用表单提交的方式启动
        Map<String,String> map=new HashMap<>();
        map.put("start","开始节点执行");
        map.put("message","my test message" );
        map.put("name","yrj" );
        map.put("age","88" );
        ProcessInstance processInstance = formService.submitStartFormData("myProcess_Form:1:512504", map);
        Task task =activitiRule.getTaskService().createTaskQuery().processInstanceId(processInstance.getId()).singleResult();

//        查询该Task任务的表单数据
        TaskFormData taskFormData = formService.getTaskFormData(task.getId());
        List<FormProperty> formProperties = taskFormData.getFormProperties();
        for (FormProperty formProperty : formProperties) {
            System.err.println("formProperty--"+ToStringBuilder.reflectionToString(formProperty));
        }
    }

}
