CREATE TABLE teachers (
                          id BIGSERIAL PRIMARY KEY,
                          first_name VARCHAR(255) NOT NULL,
                          last_name VARCHAR(255) NOT NULL,
                          department VARCHAR(100),
                          user_id BIGINT NOT NULL UNIQUE
);