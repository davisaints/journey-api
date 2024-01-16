CREATE TABLE IF NOT EXISTS testimonials (
    id BIGINT NOT NULL AUTO_INCREMENT,
    user_name VARCHAR(100) NOT NULL,
    testimonial TEXT NOT NULL,
    profile_picture64 LONGTEXT,
    PRIMARY KEY (id)
);
