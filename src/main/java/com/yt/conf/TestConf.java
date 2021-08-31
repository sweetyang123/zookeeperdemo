package com.yt.conf;

import org.apache.zookeeper.ZooKeeper;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class TestConf {
    ZooKeeper zk;
//    创建连接
    @Before
    public void getCon(){
        zk=ZKUtils.getZk();
    }
//    关闭连接
    @After
    public void close(){
        try {
            zk.close();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
//    获取数据，当节点存在，不存在时，在sever端进行删除，修改，新增数据时，回调保证数据一致（统一视图）
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
