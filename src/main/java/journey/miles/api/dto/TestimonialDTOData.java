package journey.miles.api.dto;

import journey.miles.api.model.testimonial.Testimonial;
import java.time.LocalDateTime;

public record TestimonialDTOData(Long id, String userName, String testimonial,
                                 String profilePicture64,
                                 LocalDateTime createDate) {

    public TestimonialDTOData(Testimonial testimonial) {
        this(testimonial.getId(), testimonial.getUserName(), testimonial.getTestimonial(),
                testimonial.getProfilePicture64(),
                testimonial.getCreateDate());
    }
}
