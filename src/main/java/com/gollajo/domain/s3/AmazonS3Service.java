package com.gollajo.domain.s3;

import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.gollajo.domain.post.entity.ImageOption;
import com.gollajo.domain.post.repository.PostRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Date;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class AmazonS3Service {

    private final AmazonS3Client amazonS3Client;

    @Value("${cloud.aws.s3.bucket}")
    private String bucket;

    public List<String> uploadFiles(List<MultipartFile> images){
        List<String> imgUrls = new ArrayList<>();

        for (MultipartFile image : images) {
            try{
                String fileName = createFileName(image);
                ObjectMetadata metadata = new ObjectMetadata();
                metadata.setContentType(image.getContentType());
                metadata.setContentLength(image.getSize());

                amazonS3Client.putObject(bucket, fileName, image.getInputStream(), metadata);

                String imgUrl = amazonS3Client.getUrl(bucket, fileName).toString();
                imgUrls.add(imgUrl);

            } catch(IOException e){
                throw new RuntimeException("이미지 업로드 중 오류가 발생했습니다.");
            }
        }
        return imgUrls;
    }

    // 파일명 중복방지를 위한 함수
    private String createFileName(MultipartFile image){
        String originalFilename = image.getOriginalFilename();
        String fileExtension = originalFilename.substring(originalFilename.lastIndexOf("."));
        return "image_" + new Date().getTime() + "_"
                + UUID.randomUUID().toString().concat(fileExtension);
    }
    public void deleteImages(List<ImageOption> imgOptions){

        for(ImageOption imgOption:imgOptions){
            deleteImage(imgOption.getImageUrl());
        }
    }

    private void deleteImage(String imgUrl){
        String imgKey = extractS3KeyFromImgUrl(imgUrl);
        amazonS3Client.deleteObject(bucket, imgKey);
    }
    private String extractS3KeyFromImgUrl(String imgUrl){
        int lastSlashIndex = imgUrl.lastIndexOf("/");

        if (lastSlashIndex != -1 && (lastSlashIndex < imgUrl.length() - 1)) {
            String s3Key = imgUrl.substring(lastSlashIndex + 1);
            return URLDecoder.decode(s3Key, StandardCharsets.UTF_8);
        }else{
            throw new IllegalStateException("타당하지 않은 imageUrl 입니다.");
        }
    }
}
