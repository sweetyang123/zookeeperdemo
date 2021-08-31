package com.yt.conf1;

import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.ZooKeeper;


public class ZKUtils {
    private static ZooKeeper zk;
    private static String address;
    private static DefautWatcher watch;

    public static ZooKeeper getZk() {
        try {
            zk=new ZooKeeper(address,1000,watch);
            watch.cc.await();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return zk;
    }
}
