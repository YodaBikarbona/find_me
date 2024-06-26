CREATE TABLE following (

    id BIGSERIAL UNIQUE NOT NULL PRIMARY KEY,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    modified_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,

    -- Relationships
    profile_id BIGINT NOT NULL,
    following_id BIGINT NOT NULL,

    -- Constraints
    CONSTRAINT fk_following_profile_id FOREIGN KEY (profile_id) REFERENCES profiles(id) ON DELETE CASCADE,
    CONSTRAINT fk_following_following_id FOREIGN KEY (following_id) REFERENCES profiles(id) ON DELETE CASCADE

)
