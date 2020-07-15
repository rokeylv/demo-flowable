package com.example.activiti;

import org.flowable.engine.HistoryService;
import org.flowable.engine.RepositoryService;
import org.flowable.engine.RuntimeService;
import org.flowable.engine.TaskService;
import org.flowable.engine.repository.Deployment;
import org.flowable.engine.repository.DeploymentBuilder;
import org.flowable.engine.repository.ProcessDefinition;
import org.flowable.engine.runtime.ProcessInstance;
import org.flowable.task.api.Task;
import org.flowable.task.api.TaskQuery;
import org.flowable.task.api.history.HistoricTaskInstance;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@SpringBootTest
class ApplicationTests {

    @Autowired
    private RepositoryService repositoryService;
    @Autowired
    private RuntimeService runtimeService;
    @Autowired
    private TaskService taskService;
    @Autowired
    private HistoryService historyService;

    @Test
    public void deploy() throws IOException {
        // 构造DeploymentBuilder对象
        DeploymentBuilder deploymentBuilder = repositoryService
                .createDeployment()
                .category("分类")// 流程定义的分类
                .key("deploy-key")//部署key
                .name("三级会签")//流程部署名称
                .tenantId("租户")//租户
                .addClasspathResource("static/三级会签.bpmn20.xml");
        // 部署
        Deployment deploy = deploymentBuilder.deploy();
        System.out.println(deploy);

    }
    @Test
    public void deleteDeploy() {
        String deployId = "753ebc13-c66a-11ea-9967-9cda3e8b0f61";
        repositoryService.deleteDeployment(deployId,true);//是否级联删除(已存在的流程实例)
        Deployment deployment = repositoryService
                .createDeploymentQuery()
                .deploymentId(deployId)
                .singleResult();
        System.out.println(deployment);


    }

    @Test
    public void definition() {
        String deployId = "753ebc13-c66a-11ea-9967-9cda3e8b0f61";
        ProcessDefinition definition = repositoryService
                .createProcessDefinitionQuery()
                .deploymentId(deployId)
                .singleResult();
        System.out.println(definition);

    }

    @Test
    public void processInstance() {
        String definitionId ="teamwork2:1:75a0b236-c66a-11ea-9967-9cda3e8b0f61";
        Map<String,Object> map = new HashMap<>();
        map.put("key1","value1");
        map.put("user1","xiao1");
        map.put("user2","xiao2");
        map.put("assigneeList", Arrays.asList("aaa","bbb","ccc"));
        ProcessInstance processInstance = runtimeService
                .startProcessInstanceById(definitionId,map);
        System.out.println(processInstance);

    }

    @Test
    public void deleteProcessInstance() {
        String processInstanceId = "";
        runtimeService.deleteProcessInstance(processInstanceId,"删除原因是:就要删除,咋地");
    }

    @Test
    public void getTaskListByUser() {
        String user = "xiao1";
        List<Task> taskList = taskService.createTaskQuery()
                .taskCandidateOrAssigned(user)
                .orderByTaskPriority()//必须组合asc(),desc()使用
                .desc()//排列顺序
                .list();
        taskList.forEach(t->{
            System.out.println("任务ID:"+t.getId());
            System.out.println("任务名称:"+t.getName());
            System.out.println("任务执行ID:"+t.getExecutionId());
            System.out.println("所属租户:"+t.getTenantId());
            System.out.println("任务创建时间:"+t.getCreateTime());
            System.out.println("任务被领取时间:"+t.getClaimTime());
            System.out.println("任务描述:"+t.getDescription());
        });
    }

    @Test
    public void doTask() {
        String taskId = "3ed8e5b2-c670-11ea-868c-9cda3e8b0f61";
        Map<String,Object> map = new HashMap<>();
//        map.put("reject",0);
        taskService.complete(taskId,map);
        Task task = taskService.createTaskQuery().taskId(taskId).singleResult();
        System.out.println(task);
    }

    @Test
    public void doMultiTask() {
        List<Task> taskList = taskService.createTaskQuery().orderByTaskCreateTime().desc().list();
        taskList.forEach(t->{
            System.out.println(">>>>>>>>>>>>>>>>任务信息<<<<<<<<<<<<<<<<<<<<<<<<<");
            System.out.println("任务ID:"+t.getId());
            System.out.println("任务名称:"+t.getName());
            System.out.println("任务执行ID:"+t.getExecutionId());
            System.out.println("所属租户:"+t.getTenantId());
            System.out.println("任务创建时间:"+t.getCreateTime());
            System.out.println("任务被领取时间:"+t.getClaimTime());
            System.out.println("任务描述:"+t.getDescription());
            System.out.println();
        });
        System.out.println("=======完成前=============");
        String taskId = taskList.get(0).getId();
        String processInstanceId = taskList.get(0).getProcessInstanceId();
        Map<String,Object> map = new HashMap<>();
        map.put("reject",0);
        taskService.complete(taskId,map);
        System.out.println("=======完成后=============");
        taskList = taskService.createTaskQuery().orderByTaskCreateTime().desc().list();
        taskList.forEach(t->{
            System.out.println(">>>>>>>>>>>>>>>>任务信息<<<<<<<<<<<<<<<<<<<<<<<<<");
            System.out.println("任务ID:"+t.getId());
            System.out.println("任务名称:"+t.getName());
            System.out.println("任务执行ID:"+t.getExecutionId());
            System.out.println("所属租户:"+t.getTenantId());
            System.out.println("任务创建时间:"+t.getCreateTime());
            System.out.println("任务被领取时间:"+t.getClaimTime());
            System.out.println("任务描述:"+t.getDescription());
            System.out.println();
        });

        System.out.println("==========当前流程信息===========");
        ProcessInstance processInstance = runtimeService.createProcessInstanceQuery().processInstanceId(processInstanceId).singleResult();
        System.out.println("当前流程:"+processInstance);
        if (processInstance!=null){
            System.out.println("流程实例名称:"+processInstance.getName());
            System.out.println("业务关联KEY:"+processInstance.getBusinessKey());
            System.out.println("流程实例是否挂起:"+processInstance.isSuspended());
            System.out.println("流程实例是否结束:"+processInstance.isEnded());
        }
    }


    @Test
    public void histToryTask() {
        String user = "曹操";
        List<HistoricTaskInstance> historicTaskInstances = historyService.createHistoricTaskInstanceQuery()
                .taskAssignee(user)
                .orderByTaskPriority()//必须组合asc(),desc()使用
                .desc()//排列顺序
                .list();
        historicTaskInstances.forEach(t->{
            System.out.println("任务ID:"+t.getId());
            System.out.println("任务名称:"+t.getName());
            System.out.println("任务执行ID:"+t.getExecutionId());
            System.out.println("所属租户:"+t.getTenantId());
            System.out.println("任务创建时间:"+t.getCreateTime());
            System.out.println("任务被领取时间:"+t.getClaimTime());
            System.out.println("任务被结束时间:"+t.getEndTime());
            System.out.println("任务持续时间长度(毫秒):"+t.getDurationInMillis());
            System.out.println("任务描述:"+t.getDescription());
        });
    }

}
