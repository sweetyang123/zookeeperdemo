package com.yt.lock;

import org.apache.zookeeper.ZooKeeper;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class TestLock {
    ZooKeeper zk;
    @Before
    public void getCon(){
        zk=ZKUtils.getZk();
    }
    @After
    public void close(){
        try {
            zk.close();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
    @Test
    public void testLock(){
        //十个线程竞争锁
        for (int i = 0; i <10 ; i++) {
            new Thread(){
                @Override
                public void run() {
                    //尝试获取锁
                    WatchCallBack wcb = new WatchCallBack();
                    String threadName = Thread.currentThread().getName();
                    wcb.setZk(zk);
                    wcb.setThreadName(threadName);
                    System.out.println("threadName："+threadName);
                    wcb.tryLock();
                    //业务
                   //释放锁
                    wcb.unLock();
                }
            }.start();
        }
    }
}
