package com.yt.lock1;

import org.apache.zookeeper.*;
import org.apache.zookeeper.data.Stat;

import java.util.Collections;
import java.util.List;
import java.util.concurrent.CountDownLatch;

public class WatchCallBack implements Watcher,AsyncCallback.Create2Callback,
        AsyncCallback.ChildrenCallback,AsyncCallback.StatCallback {
    ZooKeeper zk;
    String threadName;
    String pathName;
    CountDownLatch cd= new CountDownLatch(1);

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
        zk.create("/lock",threadName.getBytes(),
                ZooDefs.Ids.OPEN_ACL_UNSAFE,CreateMode.EPHEMERAL_SEQUENTIAL,this,"adc");
        try {
            cd.await();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public void unLock() {
        try {
            zk.delete(pathName,-1);
            System.out.println(pathName+"  .... over");
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (KeeperException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void process(WatchedEvent event) {
        switch (event.getType()) {
            case None:
                break;
            case NodeCreated:
                break;
            case NodeDeleted:
                zk.getChildren("/",false,this,"aa");
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
    public void processResult(int rc, String path, Object ctx, String name, Stat stat) {
        if (name!=null){
            pathName=name;
            zk.getChildren("/",false,this,"aa");
        }
    }
    @Override
    public void processResult(int rc, String path, Object ctx, List<String> children) {
        Collections.sort(children);
        int i = children.indexOf(pathName.substring(1));
        if (i==0){
            System.out.println(pathName+" i am first "+threadName);
            try {
                zk.setData("/",pathName.getBytes(),-1);
            } catch (KeeperException e) {
                e.printStackTrace();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            cd.countDown();
        }else {
            zk.exists("/"+children.get(i-1),this,this,"aaa");
        }
    }

    @Override
    public void processResult(int rc, String path, Object ctx, Stat stat) {

    }
}
