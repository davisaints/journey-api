package journey.miles.api.controller;

import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import journey.miles.api.dto.TestimonialDTOConverter;
import journey.miles.api.dto.TestimonialDTOData;
import journey.miles.api.service.TestimonialService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;

@RestController
@CrossOrigin
@RequestMapping("testimonials")
public class TestimonialsController {
    @Autowired
    private TestimonialService testimonialService;

    @PostMapping
    @Transactional
    public ResponseEntity<TestimonialDTOData> postTestimonials(@Valid @ModelAttribute TestimonialDTOConverter testimonialDTOConverter) throws IOException {
            return new ResponseEntity<>(testimonialService.postTestimonial(testimonialDTOConverter), HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<List<TestimonialDTOData>> getAllTestimonials(@PageableDefault(size = 3, sort = {"createDate"}) Pageable pageable) {
        return ResponseEntity.ok(testimonialService.getAllTestimonials(pageable));
    }

    @GetMapping("{id}")
    public ResponseEntity<TestimonialDTOData> getTestimonialById(@PathVariable("id") Long id) {
        return new ResponseEntity<>(testimonialService.getTestimonialById(id),
                HttpStatus.OK);
    }

    @PutMapping
    @Transactional
    public ResponseEntity<TestimonialDTOData> putTestimonial(@ModelAttribute TestimonialDTOConverter testimonialDTOConverter,
                                                             Long id) throws IOException {
        return new ResponseEntity<>(testimonialService.putTestimonial(testimonialDTOConverter,
                id), HttpStatus.OK);
    }

    @DeleteMapping("{id}")
    @Transactional
    public ResponseEntity<String> deleteTestimonial(@PathVariable("id") Long id) {
        return new ResponseEntity<>(testimonialService.deleteTestimonial(id), HttpStatus.NO_CONTENT);
    }
}
