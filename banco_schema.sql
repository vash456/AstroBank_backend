-- ============================================================
--  SISTEMA BANCARIO - Modelo de Base de Datos MySQL
--  Basado en el diagrama de clases UML
-- ============================================================

CREATE DATABASE IF NOT EXISTS banco_db CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
USE banco_db;

-- ============================================================
-- ENUMERACIONES como tablas de catálogo
-- ============================================================

CREATE TABLE tipo_movimiento (
    id         TINYINT UNSIGNED PRIMARY KEY AUTO_INCREMENT,
    nombre     VARCHAR(20) NOT NULL UNIQUE
    -- CONSIGNACION, RETIRO, TRANSFERENCIA_OUT,
    -- TRANSFERENCIA_IN, COMPRA_TC, PAGO_TC
);

INSERT INTO tipo_movimiento (nombre) VALUES
    ('CONSIGNACION'),
    ('RETIRO'),
    ('TRANSFERENCIA_OUT'),
    ('TRANSFERENCIA_IN'),
    ('COMPRA_TC'),
    ('PAGO_TC');

-- --------------------------------------------------------

CREATE TABLE estado_cuenta (
    id     TINYINT UNSIGNED PRIMARY KEY AUTO_INCREMENT,
    nombre VARCHAR(10) NOT NULL UNIQUE
    -- ACTIVA, INACTIVA, BLOQUEADA, CERRADA
);

INSERT INTO estado_cuenta (nombre) VALUES
    ('ACTIVA'),
    ('INACTIVA'),
    ('BLOQUEADA'),
    ('CERRADA');

-- ============================================================
-- CLIENTE  (implementa IAutenticable)
-- ============================================================

CREATE TABLE cliente (
    id                  INT UNSIGNED     PRIMARY KEY AUTO_INCREMENT,
    identificacion      VARCHAR(20)      NOT NULL UNIQUE,
    nombre_completo     VARCHAR(120)     NOT NULL,
    celular             VARCHAR(20)      NOT NULL,
    usuario             VARCHAR(50)      NOT NULL UNIQUE,
    contrasena_hash     VARCHAR(255)     NOT NULL,          -- almacenar hash, NUNCA texto plano
    intentos_fallidos   TINYINT UNSIGNED NOT NULL DEFAULT 0,
    bloqueado           BOOLEAN          NOT NULL DEFAULT FALSE,
    created_at          DATETIME         NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at          DATETIME         NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);

-- ============================================================
-- CUENTA  (abstract — discriminator pattern)
-- ============================================================

CREATE TABLE cuenta (
    id                INT UNSIGNED     PRIMARY KEY AUTO_INCREMENT,
    numero_cuenta     VARCHAR(20)      NOT NULL UNIQUE,
    saldo             DECIMAL(18,2)    NOT NULL DEFAULT 0.00,
    fecha_apertura    DATETIME         NOT NULL DEFAULT CURRENT_TIMESTAMP,
    estado_cuenta_id  TINYINT UNSIGNED NOT NULL DEFAULT 1,
    tipo_cuenta       ENUM('AHORROS','CORRIENTE','TARJETA_CREDITO') NOT NULL,
    cliente_id        INT UNSIGNED     NOT NULL,

    CONSTRAINT fk_cuenta_estado   FOREIGN KEY (estado_cuenta_id) REFERENCES estado_cuenta(id),
    CONSTRAINT fk_cuenta_cliente  FOREIGN KEY (cliente_id)       REFERENCES cliente(id)
);

-- ============================================================
-- CUENTA AHORROS  (extends Cuenta)
-- ============================================================

CREATE TABLE cuenta_ahorros (
    cuenta_id      INT UNSIGNED  PRIMARY KEY,
    tasa_interes   DECIMAL(7,4)  NOT NULL DEFAULT 0.00,   -- ej: 3.5000 → 3.5 %

    CONSTRAINT fk_ca_cuenta FOREIGN KEY (cuenta_id) REFERENCES cuenta(id) ON DELETE CASCADE
);

-- ============================================================
-- CUENTA CORRIENTE  (extends Cuenta)
-- ============================================================

CREATE TABLE cuenta_corriente (
    cuenta_id              INT UNSIGNED  PRIMARY KEY,
    porcentaje_sobregiro   DECIMAL(7,4)  NOT NULL DEFAULT 0.00,
    limite_sobregiro       DECIMAL(18,2) NOT NULL DEFAULT 0.00,

    CONSTRAINT fk_cc_cuenta FOREIGN KEY (cuenta_id) REFERENCES cuenta(id) ON DELETE CASCADE
);

-- ============================================================
-- TARJETA DE CRÉDITO  (extends Cuenta + ITransferible)
-- ============================================================

CREATE TABLE tarjeta_credito (
    cuenta_id       INT UNSIGNED  PRIMARY KEY,
    cupo            DECIMAL(18,2) NOT NULL DEFAULT 0.00,
    deuda           DECIMAL(18,2) NOT NULL DEFAULT 0.00,
    numero_cuotas   INT UNSIGNED  NOT NULL DEFAULT 1,

    CONSTRAINT fk_tc_cuenta FOREIGN KEY (cuenta_id) REFERENCES cuenta(id) ON DELETE CASCADE
);

-- ============================================================
-- MOVIMIENTO  (registra → Cuenta)
-- ============================================================

CREATE TABLE movimiento (
    id                   INT UNSIGNED     PRIMARY KEY AUTO_INCREMENT,
    cuenta_id            INT UNSIGNED     NOT NULL,
    tipo_movimiento_id   TINYINT UNSIGNED NOT NULL,
    valor                DECIMAL(18,2)    NOT NULL,
    saldo_posterior      DECIMAL(18,2)    NOT NULL,
    descripcion          VARCHAR(255)     NOT NULL DEFAULT '',
    fecha_hora           DATETIME         NOT NULL DEFAULT CURRENT_TIMESTAMP,
    -- referencia opcional a cuenta destino en transferencias
    cuenta_destino_id    INT UNSIGNED     NULL,

    CONSTRAINT fk_mov_cuenta        FOREIGN KEY (cuenta_id)          REFERENCES cuenta(id),
    CONSTRAINT fk_mov_tipo          FOREIGN KEY (tipo_movimiento_id) REFERENCES tipo_movimiento(id),
    CONSTRAINT fk_mov_destino       FOREIGN KEY (cuenta_destino_id)  REFERENCES cuenta(id)
);

-- ============================================================
-- SESIÓN / AUDITORÍA DE AUTENTICACIÓN
-- ============================================================

CREATE TABLE sesion (
    id           INT UNSIGNED PRIMARY KEY AUTO_INCREMENT,
    cliente_id   INT UNSIGNED NOT NULL,
    token        VARCHAR(255) NOT NULL UNIQUE,
    ip_address   VARCHAR(45)  NOT NULL,
    activa       BOOLEAN      NOT NULL DEFAULT TRUE,
    creada_en    DATETIME     NOT NULL DEFAULT CURRENT_TIMESTAMP,
    cerrada_en   DATETIME     NULL,

    CONSTRAINT fk_sesion_cliente FOREIGN KEY (cliente_id) REFERENCES cliente(id)
);

-- ============================================================
-- ÍNDICES adicionales para rendimiento
-- ============================================================

CREATE INDEX idx_movimiento_cuenta    ON movimiento (cuenta_id, fecha_hora DESC);
CREATE INDEX idx_movimiento_destino   ON movimiento (cuenta_destino_id);
CREATE INDEX idx_cuenta_cliente       ON cuenta     (cliente_id);
CREATE INDEX idx_sesion_cliente       ON sesion     (cliente_id);
CREATE INDEX idx_sesion_token         ON sesion     (token);
