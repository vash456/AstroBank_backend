# AstroBank Backend

Backend Java para una aplicación bancaria con arquitectura limpia/hexagonal y persistencia MySQL.

## Descripción

AstroBank Backend es un proyecto de consola que gestiona clientes, cuentas, transacciones y sesiones de usuario.
Se diseñó con separación de responsabilidades entre dominio, casos de uso, persistencia y vista.

## Arquitectura

- `domain`: entidades del modelo de negocio (`Cliente`, `Cuenta`, `Movimiento`, `TarjetaCredito`, etc.)
- `services.input`: puertos de entrada / API de los casos de uso
- `services.outputport`: puertos de salida para persistencia
- `persistence.repository`: adaptadores MySQL que implementan los puertos de persistencia
- `persistence.mapper`: mapeo de filas de base de datos a objetos de dominio
- `userinterface` + `view`: interfaz de consola y lógica de presentación
- `config.Config`: ensamblaje y wiring manual de dependencias

## Patrones utilizados

- Arquitectura limpia / hexagonal
- Puertos y adaptadores
- Inversión de dependencias (DI manual)
- Repository / Data Mapper
- Singleton para la conexión a la base de datos

## Funcionalidades

- Registro y autenticación de clientes
- Gestión de cuentas de ahorro, corriente y tarjeta de crédito
- Consultas de saldo
- Consignaciones y retiros
- Transferencias entre cuentas
- Compras y pagos con tarjeta de crédito
- Consulta de movimientos bancarios

## Tecnologías

- Java 21
- Maven
- MySQL
- JDBC

## Estructura importante

- `src/main/java/astrobankapp/Main.java`: punto de entrada
- `src/main/java/astrobankapp/config/Config.java`: configuración de dependencias
- `src/main/java/astrobankapp/userinterface/MenuApp.java`: menú de consola
- `src/main/java/astrobankapp/persistence/database/DataBaseConnectionMySql.java`: conexión MySQL singleton
- `banco_schema.sql`: script de creación de la base de datos

## Uso

1. Crear la base de datos `astro_bank` en MySQL.
2. Importar el script `banco_schema.sql`.
3. Ajustar `src/main/java/astrobankapp/persistence/database/DataBaseConnectionMySql.java` si es necesario.
4. Compilar con Maven:

```bash
mvn compile
```

5. Ejecutar el proyecto desde el IDE apuntando a la clase `astrobankapp.Main`.

## Notas

- La conexión a la base de datos está configurada de forma estática en `DataBaseConnectionMySql`.
- La aplicación usa una interfaz de texto para interactuar con el usuario.

