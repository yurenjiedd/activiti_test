package com.yrj.event;

import org.activiti.engine.delegate.event.ActivitiEvent;
import org.activiti.engine.delegate.event.ActivitiEventListener;
import org.activiti.engine.delegate.event.ActivitiEventType;

public class MyJobEventListener implements ActivitiEventListener {
    @Override
    public void onEvent(ActivitiEvent event) {
        ActivitiEventType eventType = event.getType();
        /*按名字判断进行监听*/
        String name = eventType.name();
        if(name.startsWith("TIMER") || name.startsWith("JOB")){
            System.err.println("JobEven:"+event.getProcessInstanceId()+"--"+event.getType());
        }
    }

    @Override
    public boolean isFailOnException() {
        return false;
    }
}
