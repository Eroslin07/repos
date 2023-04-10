package com.newtouch.uctp.module.business.util;

import cn.hutool.core.thread.ThreadUtil;
import com.newtouch.uctp.framework.common.exception.ServiceException;
import org.apache.commons.io.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.util.Assert;

import javax.activation.MimetypesFileTypeMap;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.channels.Channels;
import java.nio.channels.FileChannel;
import java.nio.channels.WritableByteChannel;

public class DownLoadUtils {

    private static final Logger log = LoggerFactory.getLogger(DownLoadUtils.class);


    /**
     * 输出文件
     *
     * @param fileName 文件名称
     * @param file     文件对象
     * @param isDel    操作完是否删除文件, true 是，默认 true
     */
    public static void outFileByFile(String fileName, File file, Boolean isDel, HttpServletRequest request,
                                     HttpServletResponse response) {
        Assert.notNull(file, "文件不能为空");
        Assert.isTrue(file.exists(), "文件不存在");

        try {
            String type = request.getHeader("User-Agent").toLowerCase();
            String charsetCode = "UTF-8";
            /*if (type.indexOf("firefox") > 0 || type.indexOf("chrome") > 0) {
                fileName = new String(fileName.getBytes(charsetCode), "iso8859-1");
            } else {
                fileName = URLEncoder.encode(fileName, charsetCode);
            }*/
            fileName=URLEncoder.encode(fileName, charsetCode);
            // 设置响应的头部信息
            response.setHeader("content-disposition", "attachment;filename=\"" + fileName + "\"");
            // 设置响应内容的类型
            MimetypesFileTypeMap fileTypeMap = new MimetypesFileTypeMap();

            String mimeType = fileTypeMap.getContentType(file.getName());
            response.setContentType(mimeType + "; charset=" + charsetCode);
            response.setContentLength((int) file.length());
            // 设置响应内容的长度
            response.setHeader("Content-Length", String.valueOf(file.length()));
            // 通过文件管道下载
            WritableByteChannel writableByteChannel = Channels.newChannel(response.getOutputStream());
            FileChannel fileChannel = new FileInputStream(file.getAbsolutePath()).getChannel();
            fileChannel.transferTo(0, fileChannel.size(), writableByteChannel);
            log.info("输出文件成功");
            fileChannel.close();
            writableByteChannel.close();
        } catch (Exception e) {
            log.error("输出文件异常, 异常原因: ", e);
            throw new ServiceException(e.getMessage());
        } finally {
            // 这里使用的是 hutool线程工具 异步删除本地文件。这里默认为false不删除
            ThreadUtil.execAsync(() -> {
                log.info("输出文件，是否删除本地文件：【{}】", isDel);
                if (null != isDel) {
                    if (isDel) {
                        if (file.exists()) {
                            boolean isSuccessDel = file.delete();
                            log.info("输出文件，删除本地文件是否成功：【{}】", isSuccessDel);
                        }
                    }
                }
            });

        }

    }
    /**
     * 通过路径获取文件
     *
     * @param filePath 文件路径
     * @throws IOException {@link IOException}
     *
     */
    public static File getResourceFile(String filePath) throws  IOException {
        // 获取输入流

        InputStream inputStream=getInputStreamFromUrl(filePath);
        //这里是获取Resource下的文件
       // Resource resource = new ClassPathResource(filePath);
        //InputStream inputStream = resource.getInputStream();
        String outFilePath = (new File(".")).getCanonicalPath();
        // 文件名称
        String outFileName = String.format("/tmp-files/tmp-file-%s", System.currentTimeMillis());
        File file = new File(outFilePath.concat(outFileName));
        // 将inputStream 流复制输出到文件， 后续操作完文件，记得删除这个文件，避免时间久了产生太多没必要的文件， 也可以写个定时器 定时删除 过期文件
        FileUtils.copyInputStreamToFile(inputStream, file);
        inputStream.close();
        return file;

    }

    /**
     * 根据url下载文件流
     * @param urlStr
     * @return
     */
   private static InputStream getInputStreamFromUrl(String urlStr) {
        InputStream inputStream=null;
        try {
            //url解码
            URL url = new URL(java.net.URLDecoder.decode(urlStr, "UTF-8"));
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            //设置超时间为3秒
            conn.setConnectTimeout(3 * 1000);
            //防止屏蔽程序抓取而返回403错误
            conn.setRequestProperty("User-Agent", "Mozilla/4.0 (compatible; MSIE 5.0; Windows NT; DigExt)");
            //得到输入流
            inputStream = conn.getInputStream();
        } catch (IOException e) {

        }
        return inputStream;
    }

}
