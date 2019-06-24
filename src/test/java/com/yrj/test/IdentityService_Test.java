package com.yrj.test;

import org.activiti.engine.IdentityService;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.identity.Group;
import org.activiti.engine.identity.User;
import org.activiti.engine.identity.UserQuery;
import org.activiti.engine.test.ActivitiRule;
import org.activiti.engine.test.Deployment;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.List;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:activiti-context.xml"})
public class IdentityService_Test {

    @Autowired
    RuntimeService runtimeService;

    @Rule
    @Autowired
    public ActivitiRule activitiRule ;

    @Test
    @Deployment(resources= {"test06.bpmn"})
    public void test01(){
        IdentityService identityService =activitiRule.getIdentityService();

/*
//        创建用户
        User user1 = identityService.newUser("user1");
        user1.setEmail("user1@qq.com");
        identityService.saveUser(user1);
        User user2 = identityService.newUser("user2");
        user2.setEmail("user2@qq.com");
        identityService.saveUser(user2);

//        创建组
        Group group1 = identityService.newGroup("group1");
        identityService.saveGroup(group1);
        Group group2 = identityService.newGroup("group2");
        identityService.saveGroup(group2);

//        用户与用户组绑定
        identityService.createMembership("user1","group1");
        identityService.createMembership("user1","group2");
        identityService.createMembership("user2","group1");

//        获取指定组的所有用户
        List<User> userList = identityService.createUserQuery().memberOfGroup("group1").list();
        for (User user : userList) {
            System.err.println("user::"+ ToStringBuilder.reflectionToString(user));
        }

//        获取指定组的所有用户
        List<Group> groupList = identityService.createGroupQuery().groupMember("user1").list();
        for (Group group : groupList) {
            System.err.println("group::"+ ToStringBuilder.reflectionToString(group));
        }
*/

//        修改用户名
        List<User> list = identityService.createUserQuery().list();

//        identityService.deleteMembership(user11.getId(),"group1");
//        identityService.deleteMembership(user11.getId(),"group2");
        for (User user11 : list) {

            if (user11.getId().equals("user1")) {
                //        修改用户名
                user11.setLastName("yrj");
//                        修改ID
//                user11.setId("yyy");
                identityService.saveUser(user11);
            }

        }

//        重新查询
        List<User> users= identityService.createUserQuery().memberOfGroup("group1").list();
        for (User user : users) {
            System.err.println("user:::"+ ToStringBuilder.reflectionToString(user));
        }

    }

}
