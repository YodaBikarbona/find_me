CREATE TABLE post_comments (

    id BIGSERIAL UNIQUE NOT NULL PRIMARY KEY,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    modified_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    comment VARCHAR(500) NOT NULL ,

    -- Relationships
    post_id BIGINT NOT NULL,
    profile_id BIGINT NOT NULL,

    -- Constraints
    CONSTRAINT fk_post_comment_post_id FOREIGN KEY (post_id) REFERENCES posts(id) ON DELETE CASCADE,
    CONSTRAINT fk_post_comment_profile_id FOREIGN KEY (profile_id) REFERENCES profiles(id) ON DELETE CASCADE

)
