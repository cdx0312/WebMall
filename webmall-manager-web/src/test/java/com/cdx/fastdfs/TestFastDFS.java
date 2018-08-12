package com.cdx.fastdfs;

import com.cdx.util.FastDFSClient;
import org.csource.fastdfs.*;
import org.junit.Test;

/**
 * 测试FastDFS
 */
public class TestFastDFS {
    /**
     * 测试上传图片
     * @throws Exception
     */
    @Test
    public void uploadFile() throws Exception {
        //1、向工程中添加jar包
        //2、创建一个配置文件，配置tracker服务器的地址
        //3、加载配置文件
        ClientGlobal.init("E:\\Java_Code\\taotao-shangcheng\\taotao-manager-web\\src\\main\\resources\\resource\\client.conf");
        //4、创建一个TrackerClient对象。
        TrackerClient trackerClient = new TrackerClient();
        //5、使用TrackerClient对象获得trackerserver对象。
        TrackerServer trackerServer = trackerClient.getConnection();
        //6、创建一个StorageServer的引用null就可以。
        StorageServer storageServer = null;
        //7、创建一个StorageClient对象。trackerserver、StorageServer两个参数。
        StorageClient storageClient = new StorageClient(trackerServer, storageServer);
        //8、使用StorageClient对象上传文件
        String[] strings = storageClient.upload_file("E:\\pic\\1a93f1dd-dfd6-49e3-92a0-dd533d13fc13.jpg","jpg", null);
        for (String string : strings) {
            System.out.println(string);
        }
    }

    /**
     * 测试FastDFSClient工具
     */
    @Test
    public void testFastDFSClient() throws Exception {
        FastDFSClient fastDFSClient = new FastDFSClient("E:\\Java_Code\\taotao-shangcheng\\taotao-manager-web\\src\\main\\resources\\resource\\client.conf");
        String strings = fastDFSClient.uploadFile("E:\\Chrome Download\\38311356464_2ed79d1a43_o.jpg");
        System.out.println(strings);
    }


}
