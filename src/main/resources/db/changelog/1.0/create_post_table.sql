CREATE TABLE posts (

    id BIGSERIAL UNIQUE NOT NULL PRIMARY KEY,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    modified_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    views BIGINT NOT NULL,
    description VARCHAR(500) NOT NULL,
    longitude DOUBLE PRECISION NOT NULL,
    latitude DOUBLE PRECISION NOT NULL,

    -- Relationships
    profile_id BIGINT NOT NULL,

    -- Constraints
    CONSTRAINT fk_post_profile_id FOREIGN KEY (profile_id) REFERENCES profiles(id) ON DELETE SET NULL,
    CONSTRAINT check_longitude_range CHECK (longitude >= -180 AND longitude <= 180),
    CONSTRAINT check_latitude_range CHECK (latitude >= -90 AND latitude <= 90)

)
