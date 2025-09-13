-- SQL para crear tabla de ítems de orden
CREATE TABLE order_items (
  id BIGINT PRIMARY KEY,
  order_id BIGINT,
  product_id BIGINT,
  quantity INT
);

