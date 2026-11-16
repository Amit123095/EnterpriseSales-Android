CREATE TABLE users (
    id UUID PRIMARY KEY,
    full_name VARCHAR(120) NOT NULL,
    email VARCHAR(255) NOT NULL,
    password_hash VARCHAR(255) NOT NULL,
    user_role VARCHAR(30) NOT NULL DEFAULT 'SALES_REP',
    is_active BOOLEAN NOT NULL DEFAULT TRUE,
    created_at TIMESTAMPTZ NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMPTZ NOT NULL DEFAULT CURRENT_TIMESTAMP,

    CONSTRAINT chk_users_user_role
        CHECK (user_role IN ('ADMIN', 'MANAGER', 'SALES_REP'))
);

CREATE UNIQUE INDEX uk_users_email_lower
    ON users (LOWER(email));