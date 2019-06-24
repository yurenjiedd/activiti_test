package com.yrj.invoker;

import org.activiti.engine.impl.interceptor.AbstractCommandInterceptor;
import org.activiti.engine.impl.interceptor.Command;
import org.activiti.engine.impl.interceptor.CommandConfig;

public class MyCommandInterceptor extends AbstractCommandInterceptor{

    @Override
    public <T> T execute(CommandConfig config, Command<T> command) {

        System.err.println("---------"+command.getClass().getName()+"---------");

        // 然后让责任链中的下一请求处理者处理命令
        return next.execute(config,command);
    }
}
