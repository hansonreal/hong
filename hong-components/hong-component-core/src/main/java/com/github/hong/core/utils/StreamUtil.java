package com.github.hong.core.utils;

import lombok.extern.slf4j.Slf4j;
import org.springframework.util.FileCopyUtils;

import java.io.*;

/**
 * @className: StreamUtil
 * @description: 常用流操作方法
 * @author: 35533
 * @since : V1.0
 **/
@Slf4j
public class StreamUtil {
    /**
     * 流转字符串
     *
     * @param in 字节输入流
     * @return 字符串
     */
    public static String getString(InputStream in) {
        StringBuilder sb = new StringBuilder();
        try {
            BufferedReader reader = new BufferedReader(new InputStreamReader(in));
            String line;
            while ((line = reader.readLine()) != null) {
                sb.append(line);
            }
        } catch (IOException e) {
            log.error("get string failure", e);
            throw new RuntimeException(e);
        }
        return sb.toString();
    }

    /**
     * 输入流转字节数组
     *
     * @param in 输入流
     * @return 字节数组
     * @throws IOException IO异常
     */
    public static byte[] getBytes(InputStream in) throws IOException {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        FileCopyUtils.copy(in, outputStream);
        return outputStream.toByteArray();
    }


}
