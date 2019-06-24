package com.yrj.controller;

import com.yrj.service.TestService;
import org.activiti.engine.*;
import org.activiti.engine.identity.Group;
import org.activiti.engine.identity.User;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.Task;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletResponse;
import java.awt.image.BufferedImage;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;


//@RestController
public class TestController {
    @Autowired
    private RuntimeService runtimeService;

    @Autowired
    private TaskService taskService;

    @Autowired
    private IdentityService identityService;

    @Autowired
    private RepositoryService repositoryService;

    @Autowired
    private ProcessEngine processEngine;

    @Autowired
    private HistoryService historyService;


    @RequestMapping("/testA")
    public void test01(){

        //启动流程实例，字符串"myProcess_1"是BPMN模型文件里process元素的id,也就是流程图的ID
        ProcessInstance processInstance = runtimeService.startProcessInstanceByKey("myProcess_1");
        //流程实例启动后，流程会跳转到请假申请节点
        Task vacationApply = taskService.createTaskQuery().processInstanceId(processInstance.getId()).singleResult();
        //设置请假申请任务的执行人
//        taskService.setAssignee(vacationApply.getId(), admin.getId());

        //设置流程参数：请假天数和表单ID
        //流程引擎会根据请假天数days>3判断流程走向
        //formId是用来将流程数据和表单数据关联起来
        Map<String, Object> args = new HashMap<>();
        args.put("name", "yrj");
        args.put("commit", true);

        //完成请假申请任务
        taskService.complete(vacationApply.getId(), args);
    }

    @RequestMapping("/testB")
    public void test02(){
        //项目中每创建一个新用户，对应的要创建一个Activiti用户
        //两者的userId和userName一致
        User admin=identityService.newUser("1");
        admin.setLastName("admin");
        identityService.saveUser(admin);

        //项目中每创建一个角色，对应的要创建一个Activiti用户组
        Group adminGroup=identityService.newGroup("1");
        adminGroup.setName("admin");
        identityService.saveGroup(adminGroup);

        //用户与用户组关系绑定
        identityService.createMembership("1","1");

        //查出当前登录用户所在的用户组,给定UserID
        List<Group> groups = identityService.createGroupQuery()
                .groupMember("1").list();
        List<String> groupNames = groups.stream()
                .map(group -> group.getName()).collect(Collectors.toList());
        System.out.println(groupNames+"---------------------");

        //查询用户组的待审批请假流程列表
        List<Task> tasks = taskService.createTaskQuery()
                .processDefinitionKey("myProcess_1")
                .taskCandidateGroupIn(groupNames)
                .list();
        System.out.println(tasks.size()+"-------------------------");
        //根据流程实例ID查询请假申请表单数据
        List<String> processInstanceIds = tasks.stream()
                .map(task -> task.getProcessInstanceId())
                .collect(Collectors.toList());

        for (Task task : tasks) {
            System.out.println("任务id："+task.getId());
        }

        for (String id : processInstanceIds) {
            System.out.println(id);
        }

    }

    @Autowired
    TestService testService;

    //controller层代码
    @RequestMapping(value = "/image", method = RequestMethod.GET)
    public void image(HttpServletResponse response) {
        try {
            InputStream is = testService.getDiagram("90002");
            if (is == null){
                return;
            }

            response.setContentType("image/png");

            BufferedImage image = ImageIO.read(is);
            OutputStream out = response.getOutputStream();
            ImageIO.write(image, "png", out);

            is.close();
            out.close();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

}
