package com.yt.conf1;

import org.apache.zookeeper.AsyncCallback;
import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.ZooKeeper;
import org.apache.zookeeper.data.Stat;

import java.util.concurrent.CountDownLatch;

public class WatchCallBack implements Watcher,AsyncCallback.StatCallback,AsyncCallback.DataCallback {
    ZooKeeper zk;
    MyConf conf;
    CountDownLatch cc= new CountDownLatch(1);

    public ZooKeeper getZk() {
        return zk;
    }

    public void setZk(ZooKeeper zk) {
        this.zk = zk;
    }

    public MyConf getConf() {
        return conf;
    }

    public void setConf(MyConf conf) {
        this.conf = conf;
    }
    public  void await(){
        zk.exists("/AppConf",this,this,"aaa");
        try {
            cc.await();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
    @Override
    public void process(WatchedEvent event) {
        switch (event.getType()) {
            case None:
                break;
            case NodeCreated:
                zk.exists("/AppConf",this,this,"aaa");
                break;
            case NodeDeleted:
                conf.setConf("");
                cc=new CountDownLatch(1);
                break;
            case NodeDataChanged:
                break;
            case NodeChildrenChanged:
                break;
            case DataWatchRemoved:
                break;
            case ChildWatchRemoved:
                break;
            case PersistentWatchRemoved:
                break;
        }
    }

    @Override
    public void processResult(int i, String s, Object o, Stat stat) {
        if (stat!=null){
            zk.getData("/AppConf",this,this
            ,"sss");
        }
    }

    @Override
    public void processResult(int i, String s, Object o, byte[] data, Stat stat) {
        if (data!=null){
            conf.setConf(new String(data));
            cc.countDown();
        }
    }
}
