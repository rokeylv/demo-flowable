package com.example.activiti.controller;

import org.flowable.engine.*;
import org.flowable.engine.repository.Deployment;
import org.flowable.engine.repository.DeploymentBuilder;
import org.flowable.spring.boot.app.App;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.io.InputStream;
import java.time.LocalDateTime;
import java.time.ZoneOffset;

/**
 * @author Lv Jie
 * @version 1.0
 * @description TODO
 * @date 2020-07-11 18:11
 */
@RestController
@RequestMapping("/flow")
public class TestController {


    @Autowired
    private RepositoryService repositoryService;
    @Autowired
    private RuntimeService runtimeService;
    @Autowired
    private TaskService taskService;
    @Autowired
    private HistoryService historyService;

    public void deploy() throws IOException {
        // 定义的文件信息的流读取
        InputStream inputStream = App.class
                .getClassLoader().getResource("static\\三级会签.bpmn20.xml").openStream();
        // 流程定义的分类
        String category = "lvjie";
        // 构造DeploymentBuilder对象
        DeploymentBuilder deploymentBuilder = repositoryService
                .createDeployment().category(category)
                .addInputStream("三级会签.bpmn20", inputStream);
        // 部署
        Deployment deploy = deploymentBuilder.deploy();
        System.out.println(deploy);

    }

}
