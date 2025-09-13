-- SQL para agregar columna de versión
ALTER TABLE orders ADD COLUMN version INT DEFAULT 1;
ALTER TABLE products ADD COLUMN version INT DEFAULT 1;
ALTER TABLE inventory ADD COLUMN version INT DEFAULT 1;
ALTER TABLE order_items ADD COLUMN version INT DEFAULT 1;

