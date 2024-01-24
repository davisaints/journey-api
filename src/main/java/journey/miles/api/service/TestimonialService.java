package journey.miles.api.service;

import journey.miles.api.dto.TestimonialDTOConverter;
import journey.miles.api.dto.TestimonialDTOData;
import journey.miles.api.model.Testimonial;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.io.IOException;
import java.util.List;

public interface TestimonialService {
    Page<Testimonial> getAllTestimonials(Pageable pageable);

    TestimonialDTOData postTestimonial(TestimonialDTOConverter testimonialDTOConverter) throws IOException;

    List<Testimonial> getTestimonialByUserName(String userName);

    TestimonialDTOData putTestimonial(TestimonialDTOConverter testimonialDTOConverter,
                                      Long id) throws IOException;
    String deleteTestimonial(Long id);
}
