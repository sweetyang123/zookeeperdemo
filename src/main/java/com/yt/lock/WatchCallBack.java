package com.yt.lock;

import org.apache.zookeeper.*;
import org.apache.zookeeper.data.Stat;

import java.util.List;
import java.util.concurrent.CountDownLatch;

public class WatchCallBack implements Watcher,AsyncCallback.Create2Callback,AsyncCallback.ChildrenCallback {
    ZooKeeper zk ;
    String threadName;
    CountDownLatch cc = new CountDownLatch(1);

    public String getThreadName() {
        return threadName;
    }

    public void setThreadName(String threadName) {
        this.threadName = threadName;
    }

    public ZooKeeper getZk() {
        return zk;
    }

    public void setZk(ZooKeeper zk) {
        this.zk = zk;
    }

    public void tryLock() {
        //EPHEMERAL_SEQUENTIAL创建临时序列队列
       zk.create("/lock",threadName.getBytes(),
               ZooDefs.Ids.OPEN_ACL_UNSAFE,CreateMode.EPHEMERAL_SEQUENTIAL,this,"aac");
        try {
            cc.await();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }


    public void unLock() {
    }

    @Override
    public void process(WatchedEvent watchedEvent) {

    }
//Create2Callback
    @Override
    public void processResult(int i, String s, Object o, String name, Stat stat) {
        if (name!=null){
            System.out.println(name);
            //获取所有的孩子，并不需要watch
            zk.getChildren(name,false,this,"aad");

        }
    }
//ChildrenCallback
    @Override
    public void processResult(int i, String s, Object o, List<String> list) {
        System.out.println(s);
        int index = list.indexOf(s);
        //获取当前孩子节点是不是第一个，是第一个就获取锁，
       if (index==0){
           cc.countDown();
       }else {

       }
        // 否则就watch他的前一个孩子节点（这样第一个节点获取后就会回调让第二个节点获取锁）
    }
}
