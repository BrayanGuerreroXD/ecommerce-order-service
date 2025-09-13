CREATE TABLE users (
   id BIGSERIAL PRIMARY KEY,
   full_name VARCHAR(255),
   password VARCHAR(255),
   email VARCHAR(255) UNIQUE NOT NULL,
   role_id BIGINT NOT NULL,
   token VARCHAR(255),
   token_expiration TIMESTAMP,
   CONSTRAINT fk_role_id FOREIGN KEY (role_id) REFERENCES roles(id)
);