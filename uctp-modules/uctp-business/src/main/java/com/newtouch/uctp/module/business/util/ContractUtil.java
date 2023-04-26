package com.newtouch.uctp.module.business.util;

import com.qiyuesuo.sdk.v2.SdkClient;
import com.qiyuesuo.sdk.v2.http.FileItem;
import com.qiyuesuo.sdk.v2.request.DocumentDownloadRequest;
import com.qiyuesuo.sdk.v2.utils.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.util.Map;

public class ContractUtil {
    private static final Logger log = LoggerFactory.getLogger(ContractUtil.class);
    /**
    * 通过契约锁下载文件接口，将文件存储到临时文件中并返回字节流，以便将文件上传值服务器操作
    * */
    public static byte[] ContractDown(Long documentId) throws FileNotFoundException {

        // 初始化sdkClient
        SdkClient sdkClient = new SdkClient("https://openapi.qiyuesuo.cn", "nVtv3tjV9s", "A0OOrtfkB3pqqeSsgBPglUSv7Mneoj");

        // 下载合同文档
        DocumentDownloadRequest request = new DocumentDownloadRequest(documentId);
        byte[] bytes =null;
        try {
            File tempFile =File.createTempFile("temp",".pdf");
            FileOutputStream fos = new FileOutputStream(tempFile);
            //下载文件到临时文件
            sdkClient.download(request, fos);
            IOUtils.safeClose(fos);
            bytes = fileConvertToByteArray(tempFile);
            log.info("文档转流成功");
            fos.close();
            //删除临时文件
            tempFile.delete();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return bytes;

    }

    /**
     * 将文件转为字节流操作
     * */
    public static byte[] fileConvertToByteArray(File file) {
        byte[] data = null;

        try {
            FileInputStream fis = new FileInputStream(file);
            ByteArrayOutputStream baos = new ByteArrayOutputStream();

            int len;
            byte[] buffer = new byte[1024];
            while ((len = fis.read(buffer)) != -1) {
                baos.write(buffer, 0, len);
            }

            data = baos.toByteArray();

            fis.close();
            baos.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return data;

    }
}
