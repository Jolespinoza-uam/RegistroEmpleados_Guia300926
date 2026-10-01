-- Base de datos: biblioteca_fx
CREATE TABLE IF NOT EXISTS empleado (
    id                 SERIAL PRIMARY KEY,
    nombres            VARCHAR(100)   NOT NULL,
    apellidos          VARCHAR(100)   NOT NULL,
    cedula             VARCHAR(20)    NOT NULL UNIQUE,
    correo             VARCHAR(120)   UNIQUE,
    telefono           VARCHAR(20),
    cargo              VARCHAR(80),
    departamento       VARCHAR(80)    NOT NULL,
    salario            NUMERIC(10,2)  NOT NULL CHECK (salario >= 0),
    fecha_contratacion DATE           NOT NULL,
    estado             VARCHAR(10)    NOT NULL CHECK (estado IN ('Activo','Inactivo'))
);