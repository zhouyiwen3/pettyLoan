//package com.ps.config;
//
//import org.apache.zookeeper.KeeperException;
//import org.apache.zookeeper.ZooKeeper;
//import org.springframework.core.env.PropertiesPropertySource;
//import org.springframework.core.env.PropertySource;
//import org.springframework.core.io.support.EncodedResource;
//import org.springframework.core.io.support.PropertySourceFactory;
//import org.springframework.core.io.support.ResourcePropertySource;
//
//import java.io.IOException;
//import java.util.List;
//import java.util.Properties;
//
///**
// * @author zzz
// * @date 2019/7/15 15:21
// */
//public class ZkPropertyFactory implements PropertySourceFactory {
//
//    @Override
//    public PropertySource<?> createPropertySource(String name, EncodedResource resource) throws IOException {
//        // 读取zookeeper的配置
//        ResourcePropertySource resourcePropertySource = new ResourcePropertySource(resource);
//        String host = resourcePropertySource.getProperty("zookeeper.host").toString();
//        String configRootName = resourcePropertySource.getProperty("zookeeper.configRootName").toString();
//
//        // 读取zookeeper
//        ZooKeeper zooKeeper = new ZooKeeper(host, 1000, null);
//
//        Properties properties = new Properties();
//        try {
//            List<String> childrens = zooKeeper.getChildren(configRootName, false);
//
//
//            for (String key : childrens) {
//                properties.setProperty(key, new String(zooKeeper.getData(configRootName+"/"+key, false, null)));
//            }
//        } catch (KeeperException e) {
//            e.printStackTrace();
//        } catch (InterruptedException e) {
//            e.printStackTrace();
//        }
//
//        return new PropertiesPropertySource(this.toString(), properties);
//    }
//}
