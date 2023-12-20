package com.example.blogserver.Utils;

import io.minio.*;
import io.minio.errors.MinioException;
import io.minio.http.Method;
import io.minio.messages.Bucket;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.List;
import java.util.concurrent.TimeUnit;


public  class MinioUtil {
    @Value("${minio.accessKey}")
    private String accessKey;

    @Value("${minio.secretKey}")
    private String secretKey;
    static String url = "";

   public static String upload(MultipartFile file,String email,String bucket) {
        try {
            // Create a minioClient with the MinIO server playground, its access key and secret key.
            MinioClient minioClient =
                    MinioClient.builder()
                            .endpoint("http://1.94.104.159:9800/")
                            .credentials("kSvtDwbqEzhurYDtVMnb", "Xh2VnDFgoAJdWTodQPH0J6o6GiJpqI4TaH5Qxqog")
                            .build();

            // Make 'avatar' bucket if not exist.
            boolean found = false;
            try {
                try {
                    found = minioClient.bucketExists(BucketExistsArgs.builder().bucket(bucket).build());
                } catch (InvalidKeyException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (NoSuchAlgorithmException e) {
                    e.printStackTrace();
                }
                if (!found) {
                    // Make a new bucket called 'avatar'.
                    try {
                        minioClient.makeBucket(MakeBucketArgs.builder().bucket(bucket).build());
                    } catch (InvalidKeyException e) {
                        e.printStackTrace();
                    } catch (IOException e) {
                        e.printStackTrace();
                    } catch (NoSuchAlgorithmException e) {
                        e.printStackTrace();
                    }
                } else {
                    System.out.println("Bucket :"+bucket+" already exists.");
                }

                // Upload the file to 'avatar' bucket.
                try {
                    // 创建一个临时文件，将上传的文件内容写入临时文件
                    File tempFile = File.createTempFile("temp", null);
                    file.transferTo(tempFile);

                    minioClient.uploadObject(
                            UploadObjectArgs.builder()
                                    .bucket(bucket)
                                    .object(email+".jpg")  // Set the object name to the filename.
                                    .filename(tempFile.getAbsolutePath())  // Set the filename to the absolute path of the file.
                                    .contentType("image/jpeg")
                                    .build());
                    tempFile.delete(); // 删除临时文件
                } catch (InvalidKeyException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (NoSuchAlgorithmException e) {
                    e.printStackTrace();
                }

                // Generate a presigned URL for the uploaded object.
                try {
                    url = minioClient.getPresignedObjectUrl(GetPresignedObjectUrlArgs.builder()
                            .method(Method.GET)
                            .bucket(bucket)
                            .object(email+".jpg")
                            .expiry(1, TimeUnit.DAYS)
                            .build());
                } catch (InvalidKeyException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (NoSuchAlgorithmException e) {
                    e.printStackTrace();
                }

                System.out.println("File uploaded successfully to MinIO. URL: " + url);
            } catch (MinioException e) {
                System.out.println("Error occurred: " + e);
                System.out.println("HTTP trace: " + e.httpTrace());
            }
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        }
        return url;
    }
}
