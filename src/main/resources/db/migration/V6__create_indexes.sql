-- SQL para crear índices
CREATE INDEX idx_user_id ON orders(user_id);
CREATE INDEX idx_product_id ON order_items(product_id);
-- SQL para crear tabla de productos
CREATE TABLE products (
  id BIGINT PRIMARY KEY,
  name VARCHAR(255),
  price DECIMAL(10,2)
);

