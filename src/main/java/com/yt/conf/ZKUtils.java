package com.yt.conf;

import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.ZooKeeper;

import java.io.IOException;

public class ZKUtils {
    private static ZooKeeper zk;
    private static String address="127.0.0.1:2187/testConf";
    private static DefaultWatch watch=new DefaultWatch();

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
