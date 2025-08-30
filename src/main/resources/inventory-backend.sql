-- Create database schema for inventory management system

    -- Create ENUM types
    CREATE TYPE user_role AS ENUM ('ADMIN', 'MANAGER', 'STAFF');
    CREATE TYPE payment_status AS ENUM ('PAID', 'PARTIAL', 'UNPAID');

-- Create tables

-- WAREHOUSE table
CREATE TABLE IF NOT EXISTS warehouse (
    warehouse_id BIGSERIAL PRIMARY KEY,
    warehouse_name VARCHAR(255) NOT NULL,
    location VARCHAR(500),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- CATEGORY table
CREATE TABLE IF NOT EXISTS category (
    category_id BIGSERIAL PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- TYPE table
CREATE TABLE IF NOT EXISTS type (
    type_id BIGSERIAL PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- SUPPLIER table
CREATE TABLE IF NOT EXISTS supplier (
    supplier_id BIGSERIAL PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    contact_number VARCHAR(20),
    address VARCHAR(500),
    due_amount DECIMAL(10,2) DEFAULT 0.00,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE IF NOT EXISTS shipment(
    shipment_id BIGSERIAL PRIMARY KEY,
    supplier_id BIGINT NOT NULL,
    date DATE NOT NULL,
    purchase_amount NUMERIC(10, 2) NOT NULL,
    labor_cost NUMERIC(10, 2) DEFAULT 0.00,
    transport_cost NUMERIC(10, 2) DEFAULT 0.00,
    paid_amount NUMERIC(10, 2) DEFAULT 0.00,
    total_amount NUMERIC(10, 2) NOT NULL,
    due_amount NUMERIC(10, 2) NOT NULL,
    created_at TIMESTAMP WITHOUT TIME ZONE DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP WITHOUT TIME ZONE DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT fk_supplier
        FOREIGN KEY (supplier_id)
        REFERENCES supplier(supplier_id)
        ON DELETE RESTRICT
);

-- CUSTOMER table
CREATE TABLE IF NOT EXISTS customer (
    customer_id BIGSERIAL PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    contact_number VARCHAR(20),
    address VARCHAR(500),
    payment_status payment_status DEFAULT 'UNPAID',
    due_amount DECIMAL(10,2) DEFAULT 0.00,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- PRODUCT table
CREATE TABLE IF NOT EXISTS product (
    product_id BIGSERIAL PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    product_code VARCHAR(100) UNIQUE,
    type VARCHAR(255),
    stock DECIMAL(10,2) DEFAULT 0.00,
    buying_price DECIMAL(10,2),
    selling_price DECIMAL(10,2),
    unit VARCHAR(50),
    low_stock_threshold DECIMAL(10,2) DEFAULT 0.00,
    warehouse_id BIGINT REFERENCES warehouse(warehouse_id),
    category_id BIGINT REFERENCES category(category_id),
    supplier_id BIGINT REFERENCES supplier(supplier_id),
    type_id BIGINT REFERENCES type(type_id),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- PURCHASE table
CREATE TABLE IF NOT EXISTS purchase (
    purchase_id BIGSERIAL PRIMARY KEY,
    supplier_id BIGINT REFERENCES supplier(supplier_id),
    date TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    description TEXT,
    purchase_amount DECIMAL(10,2),
    labor_cost DECIMAL(10,2) DEFAULT 0.00,
    transport_cost DECIMAL(10,2) DEFAULT 0.00,
    paid_amount DECIMAL(10,2) DEFAULT 0.00,
    receipt_image BYTEA,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- PURCHASE_DETAIL table
CREATE TABLE IF NOT EXISTS purchase_detail (
    purchase_detail_id BIGSERIAL PRIMARY KEY,
    purchase_id BIGINT REFERENCES purchase(purchase_id) ON DELETE CASCADE,
    product_id BIGINT REFERENCES product(product_id),
    quantity DECIMAL(10,2) NOT NULL,
    unit_price DECIMAL(10,2) NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- SALE table
CREATE TABLE IF NOT EXISTS sale (
    sale_id BIGSERIAL PRIMARY KEY,
    customer_id BIGINT REFERENCES customer(customer_id),
    user_id BIGINT REFERENCES users(user_id),
    date DATE DEFAULT CURRENT_DATE,
    total_price DECIMAL(10,2),
    paid_amount DECIMAL(10,2) DEFAULT 0.00,
    labor_cost DECIMAL(10,2) DEFAULT 0.00,
    discount_amount DECIMAL(10,2) DEFAULT 0.00,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- SALES_ORDER table
CREATE TABLE IF NOT EXISTS sales_order (
    sales_order_id BIGSERIAL PRIMARY KEY,
    product_id BIGINT REFERENCES product(product_id),
    sale_id BIGINT REFERENCES sale(sale_id) ON DELETE CASCADE,
    quantity DECIMAL(10,2) NOT NULL,
    rate DECIMAL(10,2) NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- PAYMENT_LOG table
CREATE TABLE IF NOT EXISTS payment_log (
        payment_log_id BIGSERIAL PRIMARY KEY,
        customer_id BIGINT REFERENCES customer(customer_id) ON DELETE SET NULL,
        sale_id BIGINT REFERENCES sale(sale_id) ON DELETE SET NULL,
        amount NUMERIC(10, 2) NOT NULL,
        payment_method VARCHAR(255),
        note TEXT,
        payment_date DATE,
        created_at TIMESTAMP WITHOUT TIME ZONE DEFAULT NOW(),
        updated_at TIMESTAMP WITHOUT TIME ZONE DEFAULT NOW()
);

-- EXPENSE table
CREATE TABLE IF NOT EXISTS expense (
    expense_id BIGSERIAL PRIMARY KEY,
    user_id BIGINT REFERENCES users(user_id),
    category VARCHAR(255) NOT NULL,
    amount DECIMAL(10,2) NOT NULL,
    date TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Create indexes for better performance
CREATE INDEX IF NOT EXISTS idx_product_warehouse ON product(warehouse_id);
CREATE INDEX IF NOT EXISTS idx_product_category ON product(category_id);
CREATE INDEX IF NOT EXISTS idx_product_supplier ON product(supplier_id);
CREATE INDEX IF NOT EXISTS idx_product_type ON product(type_id);
CREATE INDEX IF NOT EXISTS idx_purchase_supplier ON purchase(supplier_id);
CREATE INDEX IF NOT EXISTS idx_shipment_supplier ON shipment(supplier_id);
CREATE INDEX IF NOT EXISTS idx_sale_customer ON sale(customer_id);
CREATE INDEX IF NOT EXISTS idx_sale_user ON sale(user_id);
CREATE INDEX IF NOT EXISTS idx_payment_log_sale ON payment_log(sale_id);
CREATE INDEX IF NOT EXISTS idx_expense_user ON expense(user_id);

-- Create triggers for updating updated_at timestamps
CREATE OR REPLACE FUNCTION update_updated_at_column()
RETURNS TRIGGER AS $$
BEGIN
    NEW.updated_at = CURRENT_TIMESTAMP;
    RETURN NEW;
END;
$$ language 'plpgsql';

-- Apply triggers to all tables
CREATE OR REPLACE TRIGGER update_warehouse_updated_at BEFORE UPDATE ON warehouse FOR EACH ROW EXECUTE FUNCTION update_updated_at_column();
CREATE OR REPLACE TRIGGER update_category_updated_at BEFORE UPDATE ON category FOR EACH ROW EXECUTE FUNCTION update_updated_at_column();
CREATE OR REPLACE TRIGGER update_type_updated_at BEFORE UPDATE ON type FOR EACH ROW EXECUTE FUNCTION update_updated_at_column();
CREATE OR REPLACE TRIGGER update_supplier_updated_at BEFORE UPDATE ON supplier FOR EACH ROW EXECUTE FUNCTION update_updated_at_column();
CREATE OR REPLACE TRIGGER update_users_updated_at BEFORE UPDATE ON users FOR EACH ROW EXECUTE FUNCTION update_updated_at_column();
CREATE OR REPLACE TRIGGER update_customer_updated_at BEFORE UPDATE ON customer FOR EACH ROW EXECUTE FUNCTION update_updated_at_column();
CREATE OR REPLACE TRIGGER update_product_updated_at BEFORE UPDATE ON product FOR EACH ROW EXECUTE FUNCTION update_updated_at_column();
CREATE OR REPLACE TRIGGER update_purchase_updated_at BEFORE UPDATE ON purchase FOR EACH ROW EXECUTE FUNCTION update_updated_at_column();
CREATE OR REPLACE TRIGGER update_purchase_detail_updated_at BEFORE UPDATE ON purchase_detail FOR EACH ROW EXECUTE FUNCTION update_updated_at_column();
CREATE OR REPLACE TRIGGER update_sale_updated_at BEFORE UPDATE ON sale FOR EACH ROW EXECUTE FUNCTION update_updated_at_column();
CREATE OR REPLACE TRIGGER update_sales_order_updated_at BEFORE UPDATE ON sales_order FOR EACH ROW EXECUTE FUNCTION update_updated_at_column();
CREATE OR REPLACE TRIGGER update_payment_log_updated_at BEFORE UPDATE ON payment_log FOR EACH ROW EXECUTE FUNCTION update_updated_at_column();
CREATE OR REPLACE TRIGGER update_expense_updated_at BEFORE UPDATE ON expense FOR EACH ROW EXECUTE FUNCTION update_updated_at_column();