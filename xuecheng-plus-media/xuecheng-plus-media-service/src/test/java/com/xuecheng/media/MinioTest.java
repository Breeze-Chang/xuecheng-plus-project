package com.xuecheng.media;

import io.minio.*;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.io.IOUtils;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FilterInputStream;

/**
 * 测试minio的sdk
 */
public class MinioTest {

    MinioClient minioClient =
            MinioClient.builder()
                .endpoint("http://192.168.101.65:9000")
                    .credentials("minioadmin", "minioadmin")
                    .build();


    @Test
    public void test_upload() throws Exception {


        minioClient.uploadObject(
                UploadObjectArgs.builder()
                        .bucket("testbucket")
                        .object("test/1.mp4")
                        .filename("E:\\LenovoSoftstore\\PLAYERUNKNOWN'S BATTLEGROUNDS\\PLAYERUNKNOWN'S BATTLEGROUNDS 2024.11.14 - 22.30.46.01.mp4")
                        .build()
        );
    }

    @Test
    public void test_delete() throws Exception {
        minioClient.removeObject(
                RemoveObjectArgs.builder()
                        .bucket("testbucket")
                        .object("test/1.mp4")
                        .build()
        );
    }

    @Test
    public void test_getFile() throws Exception {
        FilterInputStream inputStream = minioClient.getObject(
                GetObjectArgs.builder()
                        .bucket("testbucket")
                        .object("test/1.mp4")
                        .build()
        );

        FileOutputStream outputStream=new FileOutputStream(new File("E:\\1a.mp4"));
        IOUtils.copy(inputStream, outputStream);

        String source_md5 = DigestUtils.md5Hex(inputStream);
        String local_md5 = DigestUtils.md5Hex(new FileInputStream(new File("E:\\1a.mp4")));
        if(source_md5.equals(local_md5)){
            System.out.println("下载成功");
        }
    }


}
