package com.sunft.action;

import javax.jms.MapMessage;

/**
 * 业务逻辑处理类
 * Created by sunft on 2018/6/30.
 */
public class MessageTask implements Runnable {

    private MapMessage message;

    public MessageTask(MapMessage message) {
        this.message = message;
    }

    @Override
    public void run() {
        try {
            Thread.sleep(500);
            System.out.println("当前线程:" + Thread.currentThread().getName()
                    + "处理任务:" + this.message.getString("id"));
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}
