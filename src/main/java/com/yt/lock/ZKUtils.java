package com.yt.lock;

import com.yt.conf1.DefautWatcher;
import org.apache.zookeeper.ZooKeeper;

import java.util.concurrent.CountDownLatch;


public class ZKUtils {
    private static ZooKeeper zk;
    private static String address="127.0.0.1:2178/testLock";
    private static DefautWatcher watch;
    private static CountDownLatch cc=new CountDownLatch(1);

    public static ZooKeeper getZk() {
        try {
            zk=new ZooKeeper(address,1000,watch);
            watch.setCc(cc);
            cc.await();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return zk;
    }
}
