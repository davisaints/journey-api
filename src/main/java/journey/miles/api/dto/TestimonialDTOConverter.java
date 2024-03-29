package journey.miles.api.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;

public record TestimonialDTOConverter(
        @NotBlank(message = "The userName parameter is mandatory") String userName,
        @NotBlank(message = "The testimonial parameter is mandatory") @Size(max = 500, message = "The testimonial must " + "have a maximum of {max} characters")
        String testimonial,
        MultipartFile profilePicture,
        Long id,
        LocalDateTime createDate) {
}
    