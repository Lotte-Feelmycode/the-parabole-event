package com.feelmycode.parabole.service;


import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.amazonaws.util.IOUtils;
import java.io.IOException;
import java.io.InputStream;
import java.util.UUID;
import javax.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Slf4j
@Service
@RequiredArgsConstructor
public class AwsS3Service {

    private AmazonS3 amazonS3;

    @Value("${cloud.aws.credentials.accessKey}")
    private String accessKey;
    @Value("${cloud.aws.credentials.secretKey}")
    private String secretKey;
    @Value("${cloud.aws.region.static}")
    private String region;
    @Value("${cloud.aws.s3.bucket}")
    private String bucket;

    @PostConstruct
    public void setS3Client() {
        AWSCredentials credentials = new BasicAWSCredentials(accessKey, secretKey);

        amazonS3 = AmazonS3ClientBuilder.standard()
            .withCredentials(new AWSStaticCredentialsProvider(credentials))
            .withRegion(region)
            .build();
    }

    public String upload(MultipartFile file) throws IOException {

        byte[] contentBytes = null;
        try {
            InputStream is = file.getInputStream();
            contentBytes = IOUtils.toByteArray(is);
        } catch (IOException e) {
            log.error("Failed while reading bytes from %s", e.getMessage());
        }

        Long contentLength = Long.valueOf(contentBytes.length);

        ObjectMetadata metadata = new ObjectMetadata();
        metadata.setContentLength(contentLength);

        String fileName = createFileName(file.getOriginalFilename());

        amazonS3.putObject(new PutObjectRequest(bucket, fileName, file.getInputStream(), metadata)
            .withCannedAcl(CannedAccessControlList.PublicRead));

        return amazonS3.getUrl(bucket, fileName).toString();
    }

    private String createFileName(String originalFileName) { //???????????? ???????????????
        return UUID.randomUUID().toString().concat(getFileExtension(originalFileName));
    }

    private String getFileExtension(String fileName) {
        try {
            return fileName.substring(fileName.lastIndexOf("."));
        } catch (StringIndexOutOfBoundsException e) {
            throw new IllegalArgumentException(String.format("????????? ????????? ?????? (%s) ?????????", fileName));
        }
    }

}
