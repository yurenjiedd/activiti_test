package com.yrj.invoker;

import org.activiti.engine.ProcessEngineLifecycleListener;
import org.activiti.engine.debug.ExecutionTreeUtil;
import org.activiti.engine.impl.agenda.AbstractOperation;
import org.activiti.engine.impl.interceptor.DebugCommandInvoker;
import org.activiti.engine.logging.LogMDC;

public class MyMDCCommandInvoker extends DebugCommandInvoker {

    //继承了DebugCommandInvoker父类的内容,覆写其executeOperation方法
    @Override
    public void executeOperation(Runnable runnable) {

        //判断MDC是否生效（在后面讲还原LogMDC的设置）
        boolean mdcEnable = LogMDC.isMDCEnabled();

        //不管MDC是否生效，都将MDC设置为true
        LogMDC.setMDCEnabled(true);

        //判断可运行对象（runnable）是不是流程引擎支持的Operation
        if (runnable instanceof AbstractOperation) {
            //将runnable强制转换为AbstractOperation
            AbstractOperation operation = (AbstractOperation) runnable;
            //如果operation出现异常则执行
            if (operation.getExecution() != null) {
                LogMDC.putMDCExecution(operation.getExecution());
            }

        }

        super.executeOperation(runnable);

        //不影响其他地方，还原LogMDC设置
        LogMDC.clear();   //最后将它清理
        //mdcEnable不生效
        if( !mdcEnable ){
            //把状态再设置为不生效
            LogMDC.setMDCEnabled(false);
        }

    }

}
