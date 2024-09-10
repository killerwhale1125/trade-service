package carrot.market.util.image;

import carrot.market.common.baseutil.BaseException;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.util.Arrays;
import java.util.Objects;
import java.util.UUID;

import static carrot.market.common.baseutil.BaseResponseStatus.UNSUPPORT_FILETYPE;

public class FileUtils {
    private static final String BASE_DIRECTORY = "image";

    public static String getRandomFilename() {
        return UUID.randomUUID().toString().replace("-", "");
    }

    public static String getFilePath(MultipartFile file, String filename) {
        /**
         * File 확장자명 ex ) jpeg, png 등
         */
        String extension = StringUtils.getFilenameExtension(Objects.requireNonNull(file.getOriginalFilename()));

        if(!isValidFileType(extension)) {
            throw new BaseException(UNSUPPORT_FILETYPE);
        }

        return  BASE_DIRECTORY + "/" + filename + "." + extension;
    }

    /**
     * File 확장자 유효성 검사
     */
    private static boolean isValidFileType(String extension) {
        return Arrays.stream(FileType.values())
                .anyMatch(type -> type.getExtension().equals(extension));
    }
}
