package journey.miles.api.controller;

import jakarta.transaction.Transactional;
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
@CrossOrigin
@RequestMapping("testimonials")
public class TestimonialsController {
    @Autowired(required = true)
    private TestimonialRepository repository;

    @PostMapping
    @Transactional
    public ResponseEntity<String> postTestimonials(@Valid @ModelAttribute TestimonialDTOConverter testimonialDTOConverter) throws IOException {
        _validateImageSize(testimonialDTOConverter.profilePicture());
        _validateImageFormat(testimonialDTOConverter.profilePicture());

        try {
            Testimonial testimonial = new Testimonial(testimonialDTOConverter.userName(),
                    testimonialDTOConverter.testimonial(),
                    _encodeBase64ToString(testimonialDTOConverter.profilePicture()),
                    testimonialDTOConverter.createDate());

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
    public Page<Testimonial> getAllTestimonials(@PageableDefault(size = 3, sort = {"createDate"}) Pageable pagination) {
        return repository.findAll(pagination);
    }

    @GetMapping("/get-by-user-name")
    public ResponseEntity<List<Testimonial>> getByUserName(@RequestParam(name = "userName") String userName) {
        return ResponseEntity.status(HttpStatus.OK).body(repository.findByUserName(userName));
    }

    @PutMapping
    @Transactional
    public ResponseEntity<String> putTestimonial(@ModelAttribute @Valid TestimonialDTOConverter testimonialDTOConverter,
                               long id) throws IOException {
        Testimonial testimonial =
                repository.findById(testimonialDTOConverter.id()).orElseThrow(() -> new RuntimeException("Testimonial not found with the Id provided"));
        _validateImageSize(testimonialDTOConverter.profilePicture());
        _validateImageFormat(testimonialDTOConverter.profilePicture());
        String profilePicture64 =
                _encodeBase64ToString(testimonialDTOConverter.profilePicture());

        testimonial.setProfilePicture64(profilePicture64);

        testimonial.setUserName(testimonialDTOConverter.userName());

        testimonial.setTestimonial(testimonialDTOConverter.testimonial());

        repository.save(testimonial);

        return new ResponseEntity<>("Testimonial updated successfully", HttpStatus.OK);
    }

    @DeleteMapping("{id}")
    @Transactional
    public ResponseEntity<String> deleteTestimonial(@PathVariable("id") long id) {
        repository.findById(id).orElseThrow();

        repository.deleteById(id);
        return new ResponseEntity<>("Testimonial deleted successfully", HttpStatus.OK);
    }

    private String _encodeBase64ToString(MultipartFile multipartFile) throws IOException {
        return Base64.getEncoder().encodeToString(multipartFile.getBytes());
    }

    private void _validateImageSize(MultipartFile profilePicture) {
        if (profilePicture.getSize() > 500000) {
            throw new InvalidImageSizeException("Image size exceeds the allowed limit. Please ensure that the image is smaller than 2 MB.");
        }
    }

    private void _validateImageFormat(MultipartFile profilePicture) {
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

    private boolean _containsExtension(String target) {
        for (String extension : Constants.imageExtensions) {
            if (extension.equalsIgnoreCase(target)) {
                return true;
            }
        }
        return false;
    }
}
