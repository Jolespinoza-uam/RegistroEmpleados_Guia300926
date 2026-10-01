-- 1. Todos los empleados
SELECT * FROM empleado;
-- 2. Nombres, apellidos y cargo
SELECT nombres, apellidos, cargo FROM empleado;
-- 3. Empleados de un departamento
SELECT * FROM empleado WHERE departamento = 'Sistemas';
-- 4. Salario mayor a un valor
SELECT * FROM empleado WHERE salario > 1000;
-- 5. Ordenados por salario de mayor a menor
SELECT * FROM empleado ORDER BY salario DESC;
-- 6. Total de empleados
SELECT COUNT(*) AS total_empleados FROM empleado;
-- 7. Salario promedio
SELECT AVG(salario) AS salario_promedio FROM empleado;
-- 8. Suma de salarios
SELECT SUM(salario) AS total_salarios FROM empleado;
-- 9. Solo activos
SELECT * FROM empleado WHERE estado = 'Activo';
-- 10. Empleados por departamento
SELECT departamento, COUNT(*) AS cantidad FROM empleado GROUP BY departamento ORDER BY cantidad DESC;
