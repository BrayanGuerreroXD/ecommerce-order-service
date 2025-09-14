CREATE TABLE order_logs (
    id BIGSERIAL PRIMARY KEY,
    order_id BIGINT NOT NULL,
    details JSONB,
    created_at TIMESTAMP NOT NULL,
    CONSTRAINT fk_order_logs_order FOREIGN KEY (order_id)
        REFERENCES orders(id)
);