package journey.miles.api.service;

import journey.miles.api.dto.TestimonialDTOConverter;
import journey.miles.api.dto.TestimonialDTOData;
import journey.miles.api.model.testimonial.Testimonial;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.io.IOException;

public interface TestimonialService {
    Page<Testimonial> getAllTestimonials(Pageable pageable);

    TestimonialDTOData postTestimonial(TestimonialDTOConverter testimonialDTOConverter) throws IOException;

    TestimonialDTOData getTestimonialById(Long id);

    TestimonialDTOData putTestimonial(TestimonialDTOConverter testimonialDTOConverter,
                                      Long id) throws IOException;
    String deleteTestimonial(Long id);
}
