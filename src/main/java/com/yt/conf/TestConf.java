package com.yt.conf;

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
    public void getConf(){
        WatchCallBack wcb = new WatchCallBack();
        wcb.setZk(zk);
        MyConf conf = new MyConf();
        wcb.setConf(conf);
        //节点存在，或不存在两种情况
        wcb.await();
        System.out.println(conf.getData());

    }
}
