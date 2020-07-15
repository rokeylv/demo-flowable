package com.example.activiti.listener;

import org.flowable.engine.delegate.DelegateExecution;
import org.springframework.stereotype.Component;

import java.io.Serializable;

/**
 * @author Lv Jie
 * @version 1.0
 * @description TODO
 * @date 2020-07-15 12:26
 */
@Component("multiInstance")
public class MultiInstanceCompleteTask implements Serializable {


    /**
     * 评估结果判定条件
     * @param execution 分配执行实例
     */
    public boolean accessCondition(DelegateExecution execution){
        //已完成的实例数
        int completedInstance = (int)execution.getVariable("nrOfCompletedInstances");
        int sum = (int)execution.getVariable("nrOfInstances");
        //否决判断，一票否决
        if (execution.getVariable("reject") != null){
            int rejectCount = (int)execution.getVariable("reject");
            if(rejectCount > 0 ){
                //输出方向为拒绝
//                execution.setVariable("outcome", "reject");
                //一票否决其他实例没必要做，结束
                return true;
            }
        }
        //所有实例任务未全部做完则继续其他实例任务
        if(completedInstance != sum){
            return false;
        }else{
            //输出方向为赞同
//            execution.setVariable("outcome","reject");
            //所有都做完了没被否决，结束
            return true;
        }
    }
}
