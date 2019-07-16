package com.ps.config;

import org.apache.http.client.fluent.Content;
import org.apache.http.client.fluent.Request;
import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.KeeperException;
import org.apache.zookeeper.ZooDefs;
import org.apache.zookeeper.ZooKeeper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

import javax.annotation.PostConstruct;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.List;
import java.util.Properties;


@Configuration
public class ReadFileConfig {

    @Value("${zookeeper.host}")
    private String host;

    @Value("${zookeeper.configRootName}")
    private String configRootName;


    /**
     * 将配置从文件中读出，并存入zookeeper中
     * @throws IOException
     * @throws KeeperException
     * @throws InterruptedException
     */
    @PostConstruct
    public void readFileConfig() throws IOException, KeeperException, InterruptedException {
        /*Content content = Request.Get("http://pud6t6jk0.bkt.clouddn.com/config.properties").execute().returnContent();

        Properties properties = new Properties();
        properties.load(content.asStream());*/

        Properties properties = new Properties();
        properties.load(new FileInputStream("D:/config/config.properties"));

        ZooKeeper zooKeeper = new ZooKeeper(host, 1000, null);
        // 判断configRootName存不存在
        if (zooKeeper.exists(configRootName, false) == null) {
            zooKeeper.create(configRootName, null, ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.PERSISTENT);
        }

        // 将配置添加到zookeeper中
        for (Object key : properties.keySet()) {
            String nodeName = configRootName + "/" + key;

            // 判断nodeName字节是否已经存在（不存在则创建。已存在则修改值）
            if (zooKeeper.exists(nodeName, false) == null) {
                zooKeeper.create(nodeName, properties.getProperty(key.toString()).getBytes(), ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.PERSISTENT);
                continue;
            }

            zooKeeper.setData(nodeName, properties.getProperty(key.toString()).getBytes(), -1);
        }

    }


    /**
     * 将配置写到配置文件中
     * @throws IOException
     * @throws KeeperException
     * @throws InterruptedException
     */
    @PostConstruct
    public void writeConfig() throws IOException, KeeperException, InterruptedException {
        // 读取zookeeper
        ZooKeeper zooKeeper = new ZooKeeper(host, 1000, null);

        List<String> childrens = zooKeeper.getChildren(configRootName, false);
        System.out.println(childrens);

        for (String key : childrens) {
            System.setProperty(key, new String(zooKeeper.getData(configRootName + "/" + key, false, null)));
        }
    }




}