package com.yrj.test;

import org.activiti.engine.ManagementService;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.impl.persistence.entity.ProcessDefinitionEntity;
import org.activiti.engine.management.TablePage;
import org.activiti.engine.test.ActivitiRule;
import org.activiti.engine.test.Deployment;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.List;
import java.util.Map;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:activiti-context.xml"})
public class ManagementService_Test2 {

    @Autowired
    RuntimeService runtimeService;

    @Rule
    @Autowired
    public ActivitiRule activitiRule ;

    @Test
    @Deployment(resources= {"test07.bpmn"})
    public void test01(){

        ManagementService managementService = activitiRule.getManagementService();

//       获取指定表的数据
        TablePage tablePage = managementService.createTablePageQuery()
                .tableName(managementService.getTableName(ProcessDefinitionEntity.class)).listPage(1, 100);
//        ProcessDefinitionEntity流程定义对象
//        managementService.getTableName(ProcessDefinitionEntity.class)表示获取ProcessDefinition对象的表名（ACT_RE_PROCDEF）

        List<Map<String,Object>> rowsList = tablePage.getRows();
        for (Map<String,Object> row : rowsList){
            System.err.println("Map<String,Object>:"+row);
        }
        System.err.println("List<Map<String,Object>>.size:"+rowsList.size());
    }
}
