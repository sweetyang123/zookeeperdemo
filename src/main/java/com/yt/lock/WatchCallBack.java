package com.yt.lock;

import org.apache.zookeeper.*;
import org.apache.zookeeper.data.Stat;

import java.util.Collections;
import java.util.List;
import java.util.concurrent.CountDownLatch;

public class WatchCallBack implements Watcher,AsyncCallback.Create2Callback,
        AsyncCallback.ChildrenCallback,AsyncCallback.StatCallback {
    ZooKeeper zk ;
    String threadName;
    String pathName;
    CountDownLatch cc = new CountDownLatch(1);

    public String getPathName() {
        return pathName;
    }

    public void setPathName(String pathName) {
        this.pathName = pathName;
    }


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
        try {
            zk.delete(pathName,-1);
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (KeeperException e) {
            e.printStackTrace();
        }
    }
    //当某个节点删除后，要保证后面的都能收到通知，则重新getChildren排序再watch
    @Override
    public void process(WatchedEvent event) {
        switch (event.getType()) {
            case None:
                break;
            case NodeCreated:
                break;
            case NodeDeleted:
                zk.getChildren("/",false,this,"aad");
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
//Create2Callback
    @Override
    public void processResult(int i, String s, Object o, String name, Stat stat) {
        if (name!=null){
            System.out.println(name);
            pathName=name;
            //获取所有的孩子，并不需要watch
            zk.getChildren("/",false,this,"aad");

        }
    }
//ChildrenCallback
    @Override
    public void processResult(int i, String s, Object o, List<String> children) {
        System.out.println(s);
        //孩子节点列表排序
        Collections.sort(children);
        //将pathName的/去掉
        int index = children.indexOf(pathName.substring(1));
        //获取当前孩子节点是不是第一个，是第一个就获取锁，
       if (index==0){
           System.out.println(pathName+"i am first "+threadName);
//           try {
//               zk.setData("/",threadName.getBytes(),-1);
//           } catch (KeeperException e) {
//               e.printStackTrace();
//           } catch (InterruptedException e) {
//               e.printStackTrace();
//           }
           cc.countDown();
       }else {
           // 否则就watch他的前一个孩子节点（这样第一个节点获取后就会回调让第二个节点获取锁）
            zk.exists("/"+children.get(index-1),this,this,"asdf");
       }
    }
    //StatCallback
    @Override
    public void processResult(int i, String s, Object o, Stat stat) {

    }
}
