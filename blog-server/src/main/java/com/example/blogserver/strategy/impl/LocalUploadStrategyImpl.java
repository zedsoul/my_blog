package com.example.blogserver.strategy.impl;

import com.example.blogserver.exception.BizException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;


import java.io.*;

/**
 * 本地上传策略
 *
 */
@Service("localUploadStrategyImpl")
public class LocalUploadStrategyImpl extends AbstractUploadStrategyImpl {

    /**
     * 本地路径
     */

    private String localPath="111";

    /**
     * 访问url
     */

    private String localUrl="111                                                                                                                                                                                                                                                                     ";

    @Override
    public Boolean exists(String filePath) {
        return new File(localPath + filePath).exists();
    }

    @Override
    public void upload(String path, String fileName, InputStream inputStream) throws IOException {
        // 判断目录是否存在
        File directory = new File(localPath + path);
        if (!directory.exists()) {
            if (!directory.mkdirs()) {
                throw new BizException("创建目录失败");
            }
        }
        // 写入文件
        File file = new File(localPath + path + fileName);
        if (file.createNewFile()) {
            BufferedInputStream bis = new BufferedInputStream(inputStream);
            BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(file));
            byte[] bytes = new byte[1024];
            int length;
            while ((length = bis.read(bytes)) != -1) {
                bos.write(bytes, 0, length);
            }
            bos.flush();
            inputStream.close();
            bis.close();
            bos.close();
        }
    }

    @Override
    public String getFileAccessUrl(String filePath) {
        return localUrl + filePath;
    }

}
