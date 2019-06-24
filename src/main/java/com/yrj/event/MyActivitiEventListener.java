package com.yrj.event;

import org.activiti.engine.delegate.event.ActivitiEvent;
import org.activiti.engine.delegate.event.ActivitiEventListener;
import org.activiti.engine.delegate.event.ActivitiEventType;

public class MyActivitiEventListener implements ActivitiEventListener {

//    事件的回调的方法
    @Override
    public void onEvent(ActivitiEvent event) {
        ActivitiEventType type = event.getType();
        if (ActivitiEventType.ACTIVITY_STARTED.equals(type)){
//            ActivitiEventType.ACTIVITY_STARTED流程开始事件类型
            System.err.println("流程开始。。。。。。。。。。。。。。。。");

        }else if(ActivitiEventType.ACTIVITY_COMPLETED.equals(type)){
            //            ActivitiEventType.ACTIVITY_COMPLETED流程开始事件类型
            System.err.println("流程结束。。。。。。。。。。。。。。。。");

        }else if (ActivitiEventType.PROCESS_STARTED.equals(type)){
            //            ActivitiEventType.PROCESS_STARTED流程实例开始事件类型
            System.err.println("流程实例开始。。。。。。。。。。。。。。。。");
        }else if (ActivitiEventType.CUSTOM.equals(type)){
            //            ActivitiEventType.CUSTOM自定义事件类型
            System.err.println("自定义事件类型。。。。。。。。。。。。。。。。");
        }
    }

    //该isFailOnException()方法确定onEvent(..)方法在调度事件时抛出异常时的行为。
    // 当false返回，异常被忽略。当true返回，异常不会被忽略
    @Override
    public boolean isFailOnException() {
        return false;
    }
}
