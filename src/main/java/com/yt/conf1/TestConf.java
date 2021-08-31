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

    }
    @Test
    public void testConf(){

    }
}
