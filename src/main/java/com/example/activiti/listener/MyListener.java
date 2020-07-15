package com.example.activiti.listener;

import com.example.activiti.util.SpringContextHolder;
import org.flowable.engine.RepositoryService;
import org.flowable.engine.RuntimeService;
import org.flowable.engine.delegate.TaskListener;
import org.flowable.engine.repository.ProcessDefinition;
import org.flowable.engine.runtime.ProcessInstance;
import org.flowable.task.service.delegate.DelegateTask;
import org.springframework.stereotype.Component;



@Component
public class MyListener implements TaskListener {


    @Override
    public void notify(DelegateTask delegateTask) {
//        String processInstanceId = delegateTask.getProcessInstanceId();
//       // delegateTask.setAssignee(assignee(processInstanceId));
//        System.out.println(delegateTask.getExecutionId());
//        System.out.println(delegateTask.getId());
//        System.out.println(delegateTask.getName());
        String result = (String) delegateTask.getVariable("result");
        //ExecutionListner类中设置的拒绝计数变量
        int rejectedCount = (int)delegateTask.getVariable("reject");
        if("reject".equals(result)){
            //拒绝
            delegateTask.setVariable("rejected", ++rejectedCount);
        }
    }


}
