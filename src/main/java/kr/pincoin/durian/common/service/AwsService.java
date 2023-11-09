package kr.pincoin.durian.common.service;

import kr.pincoin.durian.common.exception.ApiException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.tika.Tika;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.GetUrlRequest;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;

import java.io.IOException;
import java.io.InputStream;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
@Slf4j
public class AwsService {
    private final S3Client s3Client;

    @Value("${spring.cloud.aws.s3.bucket}")
    private String bucketName;

    public String uploadFile(String directory, MultipartFile multipartFile) {
        if (!validateImage(multipartFile)) {
            throw new ApiException(HttpStatus.BAD_REQUEST,
                                   "Invalid image file",
                                   List.of("Uploaded file is not image file."));
        }

        String fileName = renameImageFile(directory, multipartFile);

        try {
            PutObjectRequest putObjectRequest = PutObjectRequest.builder()
                    .bucket(bucketName)
                    .contentType(multipartFile.getContentType()) // object meta data
                    .contentLength(multipartFile.getSize()) // object meta data
                    .key(fileName)
                    .build();

            RequestBody requestBody = RequestBody.fromBytes(multipartFile.getBytes());

            s3Client.putObject(putObjectRequest, requestBody);
        } catch (IOException ex) {
            throw new ApiException(HttpStatus.BAD_REQUEST,
                                   "File upload fail",
                                   List.of("Failed to upload file"),
                                   ex);
        }

        GetUrlRequest getUrlRequest = GetUrlRequest.builder()
                .bucket(bucketName)
                .key(fileName)
                .build();

        return s3Client.utilities().getUrl(getUrlRequest).toString();
    }

    private String renameImageFile(String directory, MultipartFile multipartFile) {
        String filename = multipartFile.getOriginalFilename();

        if (filename == null) {
            throw new ApiException(HttpStatus.BAD_REQUEST,
                                   "Invalid filename",
                                   List.of("Failed to retrieve filename."));
        }

        if (filename.lastIndexOf('.') <= 0) {
            throw new ApiException(HttpStatus.BAD_REQUEST,
                                   "Invalid file extension",
                                   List.of("Filename must contain file extension."));
        }

        String renamed = String.format("%s/%s.%s",
                                       LocalDateTime.now().format(DateTimeFormatter.ISO_DATE), // 2023-01-01
                                       UUID.randomUUID(), // uuid4
                                       filename.substring(filename.lastIndexOf(".") + 1));

        return directory != null && !directory.isBlank() ? directory + "/" + renamed : renamed;
    }

    private boolean validateImage(MultipartFile multipartFile) {
        Tika tika = new Tika();

        if (multipartFile.isEmpty()) {
            throw new ApiException(HttpStatus.BAD_REQUEST,
                                   "Empty file",
                                   List.of("Empty file is invalid."));
        }

        try {
            InputStream inputStream = multipartFile.getInputStream();
            List<String> notValidTypeList = Arrays.asList("image/jpeg", "image/pjpeg", "image/png");

            String mimeType = tika.detect(inputStream);
            log.debug("MimeType : " + mimeType);

            return notValidTypeList.stream().anyMatch(notValidType -> notValidType.equalsIgnoreCase(mimeType));
        } catch (IOException ex) {
            throw new ApiException(HttpStatus.BAD_REQUEST,
                                   "File upload fail",
                                   List.of("Failed to upload file"),
                                   ex);
        }
    }
}
