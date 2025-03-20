//package carrot.market.image.service;
//
//import carrot.market.common.properties.AwsS3Properties;
//import carrot.market.util.image.FileUtils;
//import com.amazonaws.services.s3.AmazonS3;
//import com.amazonaws.services.s3.model.CannedAccessControlList;
//import com.amazonaws.services.s3.model.ObjectMetadata;
//import com.amazonaws.services.s3.model.PutObjectRequest;
//import com.amazonaws.util.IOUtils;
//import lombok.RequiredArgsConstructor;
//import org.springframework.stereotype.Service;
//import org.springframework.web.multipart.MultipartFile;
//
//import java.io.ByteArrayInputStream;
//import java.io.IOException;
//
//@Service
//@RequiredArgsConstructor
//public class AwsS3Service implements StorageService {
//
//    private final AwsS3Properties properties;
//
//    private final AmazonS3 client;
//
//    public String upload(MultipartFile file, String filename) throws IOException {
//        return putObjectToS3Storage(client, FileUtils.getFilePath(file, filename), file);
//    }
//
//    /**
//     * 주어진 파일을 AWS S3에 업로드하고, 파일의 URL을 반환
//     */
//    private String putObjectToS3Storage(AmazonS3 client, String filepath, MultipartFile file) throws IOException {
//
//        // S3 버킷 이름
//        String bucket = properties.getBucket();
//
//        // S3에 업로드할 객체의 메타데이터를 관리하는 AWS S3 라이브러리
//        ObjectMetadata metadata = new ObjectMetadata();
//
//        // Image File의 바이트 배열을 읽어 입력 스트림으로 변환
//        ByteArrayInputStream stream = getByteArrayInputStream(file, metadata);
//
//        /**
//         * 1. S3 bucket name, filepath, InputStream, metadata 포함한 PutObjectRequest 객체를 생성
//         * 2. PublicRead 권한 설정으로 누구나 읽기 가능
//         * 3. putObject 메서드를 통해 S3에 해당 정보로 업로드 요청
//         */
//        client.putObject(new PutObjectRequest(bucket, filepath, stream, metadata)
//                .withCannedAcl(CannedAccessControlList.PublicRead));
//
//        // File URL 반환
//        return client.getUrl(bucket, filepath).toString();
//    }
//
//    /**
//     * MultipartFile의 file의 입력 스트림을 바이트 배열로 변환
//     * 파일의 바이트 길이를 metadate에 설정
//     * file의 byte 배열을 ByteArrayInputStream으로 변환
//     *
//     * ByteArrayInputStream으로 반환하는 이유
//     * ->  전체 바이트 배열을 미리 읽고, 그 길이를 메타데이터에 설정하기 위함
//     * ->  S3에 업로드할 때 파일 크기를 명시적으로 설정할 때 유용함
//     */
//    private ByteArrayInputStream getByteArrayInputStream(MultipartFile file, ObjectMetadata metadata) throws IOException {
//        byte[] bytes = IOUtils.toByteArray(file.getInputStream());
//        // 객체의 바이트 단위 크기를 설정 -> 파일의 바이트 배열을 미리 읽
//        metadata.setContentLength(bytes.length);
//        return new ByteArrayInputStream(bytes);
//    }
//
//}