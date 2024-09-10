package carrot.market.image.controller;

import carrot.market.common.baseutil.BaseResponse;
import carrot.market.image.service.ImageUploadService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

import static carrot.market.common.HttpStatusResponseEntity.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/posts")
public class PostImageController {

    private final ImageUploadService imageUploadService;

    /**
     * 게시물 이미지 업로드 (회원만 업로드 가능)
     */
    @PostMapping("/{postId}/images")
    public BaseResponse<Void> uploadImages(@PathVariable Long postId,
                                                 @RequestParam("file") List<MultipartFile> files) throws IOException {
        imageUploadService.upload(postId, files);

        return new BaseResponse<>();
    }
}
