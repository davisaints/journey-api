package journey.miles.api.service.impl;

import jakarta.persistence.EntityNotFoundException;
import journey.miles.api.dto.TestimonialDTOConverter;
import journey.miles.api.dto.TestimonialDTOData;
import journey.miles.api.model.testimonial.Testimonial;
import journey.miles.api.repository.TestimonialRepository;
import journey.miles.api.service.TestimonialService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;
import java.util.NoSuchElementException;

import static journey.miles.api.util.FileUtil.*;

@Service
public class TestimonialServiceImpl implements TestimonialService {
    @Autowired
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
    public List<TestimonialDTOData> getAllTestimonials(Pageable pageable) {
        return repository.findAll(pageable).stream()
                .map(TestimonialDTOData::new)
                .toList();
    }

    @Override
    public TestimonialDTOData getTestimonialById(Long id) {
        Testimonial testimonial =
                repository.findById(id).orElseThrow(() -> new NoSuchElementException("Testimonial" +
                        " not found with the provided id"));

        return new TestimonialDTOData(testimonial);
    }

    @Override
    public TestimonialDTOData putTestimonial(TestimonialDTOConverter testimonialDTOConverter, Long id) throws IOException {
        Testimonial testimonial =
                repository.findById(testimonialDTOConverter.id()).orElseThrow(() -> new EntityNotFoundException(
                        "Testimonial not found with the Id provided"));

        if (testimonialDTOConverter.profilePicture() != null) {
            _validateImageSize(testimonialDTOConverter.profilePicture());
            _validateImageFormat(testimonialDTOConverter.profilePicture());

            String profilePicture64 =
                    _encodeBase64ToString(testimonialDTOConverter.profilePicture());
            testimonial.setProfilePicture64(profilePicture64);
        }

        if (testimonialDTOConverter.userName() != null) {
            testimonial.setUserName(testimonialDTOConverter.userName());
        }

        if (testimonialDTOConverter.testimonial() != null) {
            testimonial.setTestimonial(testimonialDTOConverter.testimonial());
        }

        repository.save(testimonial);

        return new TestimonialDTOData(testimonial);
    }

    @Override
    public String deleteTestimonial(Long id) {
        repository.findById(id).orElseThrow(() -> new NoSuchElementException("Testimonial" +
                " not found with the provided id"));
        repository.deleteById(id);

        return null;
    }
}
