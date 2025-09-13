-- SQL para crear tabla de órdenes
CREATE TABLE orders (
  id BIGINT PRIMARY KEY,
  user_id BIGINT,
  status VARCHAR(50)
);

