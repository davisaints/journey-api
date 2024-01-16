package journey.miles.api.controller;

import jakarta.validation.Valid;
import journey.miles.api.constants.Constants;
import journey.miles.api.dto.TestimonialDTOConverter;
import journey.miles.api.exception.InvalidImageSizeException;
import journey.miles.api.exception.UnsupportedFileExtensionException;
import journey.miles.api.model.Testimonial;
import journey.miles.api.repository.TestimonialRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Base64;
import java.util.List;

@RestController
@RequestMapping("testimonials")
public class TestimonialsController {
    @Autowired(required = true)
    private TestimonialRepository repository;

    @PostMapping
    public ResponseEntity<String> postTestimonials(@Valid @ModelAttribute TestimonialDTOConverter testimonialDTOConverter) throws IOException {
        _validateImageSize(testimonialDTOConverter.profilePicture());
        _validateImageFormat(testimonialDTOConverter.profilePicture());

        try {
            String profilePictureEncoded =
                    Base64.getEncoder().encodeToString(testimonialDTOConverter.profilePicture().getBytes());

            Testimonial testimonial = new Testimonial(testimonialDTOConverter.userName(),
                    testimonialDTOConverter.testimonial(), profilePictureEncoded);

            repository.save(testimonial);

            return new ResponseEntity<>("Testimonial posted successfully", HttpStatus.OK);
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException(e.getMessage());
        } catch (DataIntegrityViolationException e) {
            String errorMessage = "Data truncation: " + e.getMessage();
            return new ResponseEntity<>(errorMessage, HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            throw new RuntimeException("An unexpected error occurred", e);
        }
    }

    @GetMapping
    public Page<Testimonial> getAllTestimonials(@PageableDefault(size = 5, sort = {"userName"}) Pageable pagination) {
        return repository.findAll(pagination);
    }

    @GetMapping("/get-by-user-name")
    public ResponseEntity<List<Testimonial>> getByUserName(@RequestParam(name = "userName") String userName) {
        return ResponseEntity.status(HttpStatus.OK).body(repository.findByUserName(userName));
    }
}
