CREATE TABLE users (

    id BIGSERIAL UNIQUE NOT NULL PRIMARY KEY,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    modified_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    email VARCHAR(128) UNIQUE NOT NULL,
    username VARCHAR(128) UNIQUE NOT NULL,
    phone_number VARCHAR(64) UNIQUE NOT NULL,
    activated BOOLEAN DEFAULT FALSE,
    banned BOOLEAN DEFAULT FALSE,

    -- Relationships
    security_id BIGINT NOT NULL,

    -- Constraints
    CONSTRAINT fk_security_id FOREIGN KEY (security_id) REFERENCES users_security(id) ON DELETE SET NULL

)
