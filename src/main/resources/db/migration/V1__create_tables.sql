CREATE TABLE users (
                       id         BIGINT AUTO_INCREMENT PRIMARY KEY,
                       name       VARCHAR(100) NOT NULL,
                       email      VARCHAR(150) NOT NULL UNIQUE,
                       password   VARCHAR(255) NOT NULL,
                       role       VARCHAR(20)  NOT NULL,
                       created_at DATETIME     NOT NULL DEFAULT CURRENT_TIMESTAMP,
                       updated_at DATETIME     NOT NULL DEFAULT CURRENT_TIMESTAMP
                           ON UPDATE CURRENT_TIMESTAMP
);

CREATE TABLE plans (
                       id            BIGINT         AUTO_INCREMENT PRIMARY KEY,
                       name          VARCHAR(100)   NOT NULL,
                       price         DECIMAL(10, 2) NOT NULL,
                       billing_cycle VARCHAR(20)    NOT NULL,
                       active        BOOLEAN        NOT NULL DEFAULT TRUE
);

CREATE TABLE subscriptions (
                               id                BIGINT      AUTO_INCREMENT PRIMARY KEY,
                               user_id           BIGINT      NOT NULL,
                               plan_id           BIGINT      NOT NULL,
                               status            VARCHAR(20) NOT NULL,
                               start_date        DATE        NOT NULL,
                               end_date          DATE,
                               next_billing_date DATE        NOT NULL,
                               created_at        DATETIME    NOT NULL DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE invoices (
                          id              BIGINT         AUTO_INCREMENT PRIMARY KEY,
                          subscription_id BIGINT         NOT NULL,
                          amount          DECIMAL(10, 2) NOT NULL,
                          due_date        DATE           NOT NULL,
                          issued_at       DATETIME       NOT NULL DEFAULT CURRENT_TIMESTAMP,
                          status          VARCHAR(20)    NOT NULL
);

CREATE TABLE payments (
                          id             BIGINT         AUTO_INCREMENT PRIMARY KEY,
                          invoice_id     BIGINT         NOT NULL,
                          amount         DECIMAL(10, 2) NOT NULL,
                          status         VARCHAR(20)    NOT NULL,
                          transaction_id VARCHAR(100)   UNIQUE,
                          payment_method VARCHAR(30),
                          failure_reason VARCHAR(255),
                          payment_date   DATETIME
);