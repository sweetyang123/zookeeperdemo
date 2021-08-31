package com.yt.conf1;

import org.apache.zookeeper.ZooKeeper;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class TestConf {
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
    public void testConf(){
        WatchCallBack wb = new WatchCallBack();
        wb.setZk(zk);
        MyConf conf = new MyConf();
        wb.setConf(conf);
        wb.await();
    }
}
