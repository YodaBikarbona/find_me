CREATE TABLE post_images (

    id BIGSERIAL UNIQUE NOT NULL PRIMARY KEY,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    modified_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    url VARCHAR(255) NOT NULL,

    -- Relationships
    post_id BIGINT NOT NULL,

    -- Constraints
    CONSTRAINT fk_post_image_id FOREIGN KEY (post_id) REFERENCES posts(id) ON DELETE SET NULL

)
