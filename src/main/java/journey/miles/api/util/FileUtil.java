package journey.miles.api.util;

import journey.miles.api.constants.Constants;
import journey.miles.api.exception.InvalidImageSizeException;
import journey.miles.api.exception.UnsupportedFileExtensionException;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Base64;

public class FileUtil {
    public static String _encodeBase64ToString(MultipartFile multipartFile) throws IOException {
        return Base64.getEncoder().encodeToString(multipartFile.getBytes());
    }

    public static void _validateImageSize(MultipartFile profilePicture) {
        if (profilePicture.getSize() > 500000) {
            throw new InvalidImageSizeException("Image size exceeds the allowed limit. Please ensure that the image is smaller than 2 MB.");
        }
    }

    public static void _validateImageFormat(MultipartFile profilePicture) {
        String originalFileName = profilePicture.getOriginalFilename();

        assert originalFileName != null;
        int lastDotIndex = originalFileName.lastIndexOf(".");

        if (lastDotIndex != -1 && lastDotIndex < originalFileName.length() - 1) {
            String extension = originalFileName.substring(lastDotIndex + 1).toLowerCase();

            if (!_containsExtension(extension)) {
                throw new UnsupportedFileExtensionException("Unsupported extension. Please upload an image with one of the following extensions: jpg, jpeg, png, webp, svg.");
            }
        }
    }

    public static boolean _containsExtension(String target) {
        for (String extension : Constants.imageExtensions) {
            if (extension.equalsIgnoreCase(target)) {
                return true;
            }
        }
        return false;
    }
}
