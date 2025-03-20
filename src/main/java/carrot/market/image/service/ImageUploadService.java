//package carrot.market.image.service;
//
//import carrot.market.image.entity.Image;
//import carrot.market.image.repository.ImageJdbcRepository;
//import carrot.market.post.entity.Post;
//import carrot.market.post.service.PostService;
//import carrot.market.util.image.FileUtils;
//import lombok.RequiredArgsConstructor;
//import org.springframework.stereotype.Service;
//import org.springframework.transaction.annotation.Transactional;
//import org.springframework.web.multipart.MultipartFile;
//
//import java.io.IOException;
//import java.util.List;
//import java.util.stream.Collectors;
//
//@Service
//@RequiredArgsConstructor
//public class ImageUploadService {
//
//    private final AwsS3Service awsS3Service;
//    private final PostService postService;
//    private final ImageJdbcRepository imageJdbcRepository;
//
//    @Transactional
//    public void upload(Long postId, List<MultipartFile> files) throws IOException {
//        Post post = postService.findPostById(postId);
//        upload(files, post);
//    }
//
//    /**
//     * 게시물에 이미지 파일 업로드
//     */
//    private void upload(List<MultipartFile> files, Post post) throws IOException {
//        List<Image> images = uploadImageToStorageServer(files, post);
//        imageJdbcRepository.saveAll(images);
//    }
//
//    /**
//     * AWS S3 이미지 업로드
//     */
//    private List<Image> uploadImageToStorageServer(List<MultipartFile> files, Post post){
//        return files.stream()
//                .map(file -> {
//                    try {
//                        // file 이름은 랜덤으로 생성
//                        String filename = FileUtils.getRandomFilename();
//                        /**
//                         * File path를 가져오기 위한 과정
//                         * 1. File 확장자명 유효성을 검사 하여 Filepath fullname 생성
//                         * 2. 생성한 fullname으로 AWS S3에 파일 업로드
//                         * 3. S3 bucket에 업로드한 해당 AWS client의 파일 fath를 가져옴
//                         */
//                        String filepath = awsS3Service.upload(file, filename);
//                        return Image.builder()
//                                .name(filename)
//                                .url(filepath)
//                                .post(post)
//                                .build();
//                    } catch (IOException e) {
//                        throw new RuntimeException("Failed to upload file", e);
//                    }
//                })
//                .collect(Collectors.toList());
//    }
//}
