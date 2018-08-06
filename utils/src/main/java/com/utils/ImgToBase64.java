package com.utils;

import java.io.InputStream;
import java.util.Base64;
import java.io.ByteArrayOutputStream;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

import javax.imageio.ImageIO;
import sun.misc.BASE64Encoder;

import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;

/**
 * Created by cynthia on 2018/7/31.
 */
public class ImgToBase64 {
    /**
     * 网络图片转换 base64编码
     * @param path
     * @return
     */
    public static String resourceToBase64(String path) {
        try {
            Resource resource = new ClassPathResource(path);
            InputStream fis = resource.getInputStream();
            ByteArrayOutputStream bos = new ByteArrayOutputStream(1000);
            byte[] b = new byte[1000];
            int n;
            while ((n = fis.read(b)) != -1) {
                bos.write(b, 0, n);
            }
            fis.close();
            bos.close();

            return encryptToBase64(bos.toByteArray());
        }
        catch(Exception ex) {
            return null;
        }


    }

    private static String encryptToBase64(byte[] b) {
        try {
            return Base64.getEncoder().encodeToString(b);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * @return
     * @Description: 根据图片地址转换为base64编码字符串
     * @Author:
     * @CreateTime:
     */
    public static String localImage2Base64(String imgFile) {
        InputStream inputStream = null;
        byte[] data = null;
        try {
            inputStream = new FileInputStream(imgFile);
            data = new byte[inputStream.available()];
            inputStream.read(data);
            inputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        // 加密
        BASE64Encoder encoder = new BASE64Encoder();
        return data != null ? encoder.encode(data) : "";
    }

    /**
     * 远程读取image转换为Base64字符串
     *
     * @param imgUrl
     * @return
     */
    public static String netImage2Base64(String imgUrl) {
        URL url = null;
        InputStream is = null;
        ByteArrayOutputStream outStream = null;
        HttpURLConnection httpUrl = null;
        try {
            url = new URL(imgUrl);

            httpUrl = (HttpURLConnection) url.openConnection();
            httpUrl.connect();
            httpUrl.getInputStream();
            is = httpUrl.getInputStream();

            outStream = new ByteArrayOutputStream();
            //创建一个Buffer字符串
            byte[] buffer = new byte[1024];
            //每次读取的字符串长度，如果为-1，代表全部读取完毕
            int len = 0;
            //使用一个输入流从buffer里把数据读取出来
            while ((len = is.read(buffer)) != -1) {
                //用输出流往buffer里写入数据，中间参数代表从哪个位置开始读，len代表读取的长度
                outStream.write(buffer, 0, len);
            }
            // 对字节数组Base64编码
            return new BASE64Encoder().encode(outStream.toByteArray());
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (is != null) {
                try {
                    is.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (outStream != null) {
                try {
                    outStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (httpUrl != null) {
                httpUrl.disconnect();
            }
        }
        return imgUrl;
    }

    /**
     * 根据输入流转换成 base64
     * @param inputStream
     * @return
     */
    public static String localImage2Base64(InputStream inputStream) {

        byte[] data = null;
        try {
            data = new byte[inputStream.available()];
            inputStream.read(data);
            inputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        // 加密
        BASE64Encoder encoder = new BASE64Encoder();
        return data != null ? encoder.encode(data) : "";
    }


    /**
     * @param source     源图片路径
     * @param formatName 将要转换的图片格式
     * @param fileName   文件名
     * @param tempPath   文件路径
     */
    private static void convertPic(String source, String formatName, String fileName, String tempPath) {
        try {
            File f = new File(source);
            f.canRead();
            BufferedImage src = ImageIO.read(f);
            ImageIO.write(src, formatName, new File(tempPath + fileName));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * @param urlString 源路径
     * @param filename  文件名
     * @param savePath  文件路径
     * @throws Exception
     */
    private static void download(String urlString, String filename, String savePath) throws Exception {
        // 构造URL
        URL url = new URL(urlString);
        // 打开连接
        URLConnection con = url.openConnection();
        //设置请求超时为5s
        con.setConnectTimeout(500 * 1000);
        // 输入流
        InputStream is = con.getInputStream();

        // 1K的数据缓冲
        byte[] bs = new byte[1024];
        // 读取到的数据长度
        int len;
        // 输出的文件流
        File sf = new File(savePath);
        if (!sf.exists()) {
            sf.mkdirs();
        }
        FileOutputStream os;
        if (sf.getPath().contains("/")) {
            os = new FileOutputStream(sf.getPath() + "/" + filename);
        } else {
            os = new FileOutputStream(sf.getPath() + "\\" + filename);
        }
        // 开始读取
        while ((len = is.read(bs)) != -1) {
            os.write(bs, 0, len);
        }
        // 完毕，关闭所有链接
        os.close();
        is.close();
    }

    /**
     *图片格式转换
     * @param source 图片路径
     * @param formatName 需要转换的格式
     * @param fileName 新图片名
     * @param tempPath 新图片路径
     * @throws Exception
     */
    public static void convert(String source, String formatName, String fileName, String tempPath) throws Exception {
        try {
            if (source.startsWith("http")) {
                try {
                    download(source, fileName.substring(0, fileName.lastIndexOf(".")) + source.substring(source.lastIndexOf(".")), tempPath);
                    source = tempPath + source.substring(source.lastIndexOf("/") + 1);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                if (!source.endsWith("." + formatName)) {
                    convertPic(source, formatName, fileName, tempPath);
                }
            } else {
                convertPic(source, formatName, fileName, tempPath);

            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public static void main(String[] args) {
        String ss = resourceToBase64("/Users/trthi/Documents/px/纪念日类项目/mm/cat44.jpg");
        System.out.println(ss);
    }
}
