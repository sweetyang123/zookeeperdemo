package com.yt.lock1;

import com.yt.lock.ZKUtils;
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
        for (int i = 0; i <10 ; i++) {
            new Thread(){
                @Override
                public void run() {
                    WatchCallBack wc = new WatchCallBack();
                    wc.setZk(zk);
                    wc.setThreadName(Thread.currentThread().getName());
                    wc.tryLock();
                    try {
                        Thread.sleep(100);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    wc.unLock();
                }
            }.start();
        }

    }
}
