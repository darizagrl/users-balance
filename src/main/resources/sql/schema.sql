CREATE TABLE IF NOT EXISTS users
(
    id         UUID PRIMARY KEY,
    name       VARCHAR(255) NOT NULL,
    created_at TIMESTAMP    NOT NULL,
    updated_at TIMESTAMP    NOT NULL
);

-- Parent table
CREATE TABLE IF NOT EXISTS balances
(
    id         UUID      NOT NULL,
    user_id    UUID      NOT NULL,
    balance    INT       NOT NULL,
    created_at TIMESTAMP NOT NULL,
    updated_at TIMESTAMP NOT NULL,
    CONSTRAINT balances_pk PRIMARY KEY (id, created_at),
    CONSTRAINT fk_user FOREIGN KEY (user_id) REFERENCES users (id)
) PARTITION BY RANGE (created_at);

-- Create partition tables
CREATE TABLE IF NOT EXISTS balances_2024_05 PARTITION OF balances
    FOR VALUES FROM ('2024-05-01') TO ('2024-06-01');

CREATE TABLE IF NOT EXISTS balances_2024_06 PARTITION OF balances
    FOR VALUES FROM ('2024-06-01') TO ('2024-07-01');

CREATE INDEX IF NOT EXISTS idx_balances_user_id ON balances (user_id);
