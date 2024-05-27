CREATE TABLE profiles (

    id BIGSERIAL UNIQUE NOT NULL PRIMARY KEY,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    modified_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    first_name VARCHAR(64) NOT NULL,
    last_name VARCHAR(64) NOT NULL,
    gender VARCHAR(10) NOT NULL,
    birthday DATE NOT NULL,
    about_me VARCHAR(500) NULL,

    -- Relationships
    user_id BIGINT NOT NULL,

    -- Constraints
    CONSTRAINT fk_profile_user_id FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE

)
