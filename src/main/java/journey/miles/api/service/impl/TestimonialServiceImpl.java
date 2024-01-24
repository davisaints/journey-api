package journey.miles.api.service.impl;

import jakarta.persistence.EntityNotFoundException;
import journey.miles.api.dto.TestimonialDTOConverter;
import journey.miles.api.dto.TestimonialDTOData;
import journey.miles.api.model.Testimonial;
import journey.miles.api.repository.TestimonialRepository;
import journey.miles.api.service.TestimonialService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;

import static journey.miles.api.util.FileUtil.*;

@Service
public class TestimonialServiceImpl implements TestimonialService {
    @Autowired(required = true)
    private TestimonialRepository repository;

    @Override
    public TestimonialDTOData postTestimonial(TestimonialDTOConverter testimonialDTOConverter) throws IOException {
        _validateImageSize(testimonialDTOConverter.profilePicture());
        _validateImageFormat(testimonialDTOConverter.profilePicture());

        try {
            Testimonial testimonial = new Testimonial(testimonialDTOConverter.userName(),
                    testimonialDTOConverter.testimonial(),
                    _encodeBase64ToString(testimonialDTOConverter.profilePicture()),
                    testimonialDTOConverter.createDate());

            repository.save(testimonial);

            return new TestimonialDTOData(testimonial);
        } catch (IOException e) {
            throw new IOException("Error handling image data: " + e.getMessage(), e);
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Invalid input data: " + e.getMessage(), e);
        } catch (DataIntegrityViolationException e) {
            throw new DataIntegrityViolationException("Database integrity violation: " + e.getMessage(), e);
        } catch (Exception e) {
            throw new RuntimeException("An unexpected error occurred: " + e.getMessage(), e);
        }
    }

    @Override
    public Page<Testimonial> getAllTestimonials(Pageable pageable) {
        return repository.findAll(pageable);
    }


    @Override
    public List<Testimonial> getTestimonialByUserName(String userName) {
        return repository.findByUserName(userName);
    }

    @Override
    public TestimonialDTOData putTestimonial(TestimonialDTOConverter testimonialDTOConverter, Long id) throws IOException {
        Testimonial testimonial =
                repository.findById(testimonialDTOConverter.id()).orElseThrow(() -> new RuntimeException(
                        "Testimonial not found with the Id provided"));

        if (testimonialDTOConverter.profilePicture() != null) {
            _validateImageSize(testimonialDTOConverter.profilePicture());
            _validateImageFormat(testimonialDTOConverter.profilePicture());

            String profilePicture64 =
                    _encodeBase64ToString(testimonialDTOConverter.profilePicture());
            testimonial.setProfilePicture64(profilePicture64);
        }

        testimonial.setUserName(testimonialDTOConverter.userName());
        testimonial.setTestimonial(testimonialDTOConverter.testimonial());

        repository.save(testimonial);

        return new TestimonialDTOData(testimonial);
    }

        @Override
        public String deleteTestimonial(Long id) {
            repository.findById(id).orElseThrow();

            repository.deleteById(id);

            return null;
        }
}
