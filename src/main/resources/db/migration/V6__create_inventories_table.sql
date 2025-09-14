CREATE TABLE inventories (
    id BIGSERIAL PRIMARY KEY,
    product_id BIGINT NOT NULL UNIQUE,
    quantity INT NOT NULL,
    CONSTRAINT fk_inventories_product
        FOREIGN KEY (product_id)
        REFERENCES products(id)
);

CREATE INDEX idx_inventories_product_id ON inventories(product_id);
