package com.yt;

import org.apache.zookeeper.AsyncCallback;
import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.ZooKeeper;
import org.apache.zookeeper.data.Stat;

import java.io.IOException;
import java.util.concurrent.CountDownLatch;

/**
 * Hello world!
 *
 */
public class App 
{
    public static void main( String[] args )
    {
        System.out.println( "Hello World!" );
        String address="";
        CountDownLatch cdl = new CountDownLatch(1);
        try {
            ZooKeeper zk = new ZooKeeper(address, 2000, new Watcher() {
                @Override
                public void process(WatchedEvent watchedEvent) {
                    switch (watchedEvent.getState()) {
                        case Unknown:
                            break;
                        case Disconnected:
                            break;
                        case NoSyncConnected:
                            break;
                        case SyncConnected:
                            System.out.println("connected....");
                            break;
                        case AuthFailed:
                            break;
                        case ConnectedReadOnly:
                            break;
                        case SaslAuthenticated:
                            break;
                        case Expired:
                            break;
                        case Closed:
                            break;
                    }
                }
            });
            zk.setData("/appconf", "olddata".getBytes(), 1, new AsyncCallback.StatCallback() {
                @Override
                public void processResult(int i, String s, Object o, Stat stat) {
                    System.out.println(s);
                }
            },"abc");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
