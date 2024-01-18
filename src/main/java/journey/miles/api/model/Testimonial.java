        package journey.miles.api.model;

        import jakarta.persistence.*;
        import lombok.*;
        import java.time.LocalDateTime;

        @Table(name = "testimonials")
        @Entity(name = "Testimonial")
        @Getter
        @Setter
        @NoArgsConstructor
        @AllArgsConstructor
        @EqualsAndHashCode(of = "id")
        public class Testimonial {
            @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
            private Long id;
            private String userName;
            private String testimonial;
            @Column(columnDefinition = "TEXT")
            private String profilePicture64;
            private LocalDateTime createDate;
            public Testimonial(String userName, String testimonial, String profilePicture64, LocalDateTime createDate) {
                this.userName = userName;
                this.testimonial = testimonial;
                this.profilePicture64 = profilePicture64;
                this.createDate = LocalDateTime.now();
            }
        }
