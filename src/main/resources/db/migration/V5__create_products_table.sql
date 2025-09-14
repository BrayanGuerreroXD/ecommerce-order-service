CREATE TABLE public.products (
                                 id BIGSERIAL PRIMARY KEY,
                                 name VARCHAR(255) NOT NULL,
                                 price DOUBLE PRECISION NOT NULL,
                                 sku VARCHAR(255) NOT NULL UNIQUE
);

CREATE INDEX idx_products_sku ON public.products (sku);
