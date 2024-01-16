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

}
