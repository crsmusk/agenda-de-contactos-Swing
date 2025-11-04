-- Script de configuración de la base de datos para la Agenda de Contactos
-- MySQL version


CREATE TABLE contactos_auditoria (
    id_auditoria INT PRIMARY KEY AUTO_INCREMENT,
    contacto_id BIGINT,
    accion VARCHAR(20),
    nombre_anterior VARCHAR(255),
    nombre_nuevo VARCHAR(255),
    apellido_anterior VARCHAR(255),
    apellido_nuevo VARCHAR(255),
    correo_anterior VARCHAR(255),
    correo_nuevo VARCHAR(255),
    telefono_anterior VARCHAR(255),
    telefono_nuevo VARCHAR(255),
    fecha_modificacion DATETIME,
    usuario_modificacion VARCHAR(50)
);

-- Trigger para auditar actualizaciones
DELIMITER //
CREATE TRIGGER auditoriaActualizacion
AFTER UPDATE ON Contactos
FOR EACH ROW
BEGIN
    INSERT INTO contactos_auditoria (
        contacto_id,
        accion,
        nombre_anterior,
        nombre_nuevo,
        apellido_anterior,
        apellido_nuevo,
        correo_anterior,
        correo_nuevo,
        telefono_anterior,
        telefono_nuevo,
        fecha_modificacion,
        usuario_modificacion
    )
    VALUES (
        OLD.id,
        'UPDATE',
        OLD.nombre,
        NEW.nombre,
        OLD.apellido,
        NEW.apellido,
        OLD.correo,
        NEW.correo,
        OLD.numero_de_telefono,
        NEW.numero_de_telefono,
        NOW(),
        CURRENT_USER()
    );
END //
DELIMITER ;

-- Stored Procedure para guardar contacto
DELIMITER //
CREATE PROCEDURE guardarContacto(
    IN p_nombre VARCHAR(255),
    IN p_apellido VARCHAR(255),
    IN p_correo VARCHAR(255),
    IN p_numero_telefono VARCHAR(255),
    OUT p_id BIGINT
)
BEGIN
    INSERT INTO Contactos (nombre, apellido, correo, numero_de_telefono)
    VALUES (p_nombre, p_apellido, p_correo, p_numero_telefono);
    
    SET p_id = LAST_INSERT_ID();
END //
DELIMITER ;

-- Stored Procedure para obtener todos los contactos
DELIMITER //
CREATE PROCEDURE obtenerContactos()
BEGIN
    SELECT id, nombre, apellido, correo, numero_de_telefono
    FROM Contactos;
END //
DELIMITER ;

-- Stored Procedure para actualizar contacto
DELIMITER //
CREATE PROCEDURE actualizarContacto(
    IN p_id BIGINT,
    IN p_nombre VARCHAR(255),
    IN p_apellido VARCHAR(255),
    IN p_correo VARCHAR(255),
    IN p_numero_telefono VARCHAR(255)
)
BEGIN
    UPDATE Contactos
    SET nombre = p_nombre,
        apellido = p_apellido,
        correo = p_correo,
        numero_de_telefono = p_numero_telefono
    WHERE id = p_id;
    
    SELECT id, nombre, apellido, correo, numero_de_telefono
    FROM Contactos
    WHERE id = p_id;
END //
DELIMITER ;

-- Stored Procedure para eliminar contacto
DELIMITER //
CREATE PROCEDURE eliminarContacto(
    IN p_id BIGINT
)
BEGIN
    DELETE FROM Contactos WHERE id = p_id;
END //
DELIMITER ;