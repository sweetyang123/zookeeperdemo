package com.yt.conf;

import org.apache.zookeeper.AsyncCallback;
import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.ZooKeeper;
import org.apache.zookeeper.data.Stat;

import java.util.concurrent.CountDownLatch;

public class WatchCallBack implements Watcher,AsyncCallback.StatCallback,AsyncCallback.DataCallback {
    private ZooKeeper zk;
    private MyConf conf;
    CountDownLatch cc = new CountDownLatch(1);

    public MyConf getConf() {
        return conf;
    }

    public void setConf(MyConf conf) {
        this.conf = conf;
    }

    public ZooKeeper getZk() {
        return zk;
    }

    public void setZk(ZooKeeper zk) {
        this.zk = zk;
    }
    public void await(){
        zk.exists("/AppConf",this,this,"abc");
        try {
            cc.await();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
    //watch 回调事件函数
    @Override
    public void process(WatchedEvent watchedEvent) {
        switch (watchedEvent.getType()) {
            case None:
                break;
            case NodeCreated:
                zk.getData("/AppConf",this,this,"ass");
                break;
            case NodeDeleted:
                //
                conf.setData("");
                cc= new CountDownLatch(1);
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
//StatCallback
    @Override
    public void processResult(int i, String s, Object o, Stat stat) {
        if (stat!=null){
            zk.getData("/AppConf",this,this,"ass");
        }
    }
//DataCallback
    @Override
    public void processResult(int i, String s, Object o, byte[] data, Stat stat) {
            if (data!=null){
                //拿到数据后释放锁
                conf.setData(new String(data));
                cc.countDown();
            }
    }
}
