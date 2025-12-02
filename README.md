# MDAI-Proyecto

En este documento podrás encontrar la siguiente información:
- *Información General*
- *Requisitos de Uso (Funcionalidades)*
- *Diagrama E/R*
- *Guía de Instalación*
- *Ejecución de Tests*

### *INFORMACIÓN GENERAL*
TunedHive es una aplicación diseñada para compartir, escuchar y organizar música, en la que todo el mundo puede tanto crear como consumir contenido. 

En su creación hemos colaborado:
- **Marco Herrera Iborra**, estudiante de 4º de Ing. Informática en el CUMe
- **Juan José Galindo Cotano**, estudiante de 4º de Ing. Informática en el CUMe


### *REQUISITOS DE USO (FUNCIONALIDADES)*
En el siguiente enlace podrá consultar todas las funcionalidades y casos de uso de nuestra aplicación:
- *https://github.com/MarcoHI14/MDAI-Proyecto/blob/main/Requisitos_TunedHive.md*

### *DIAGRAMA E/R*
Puedes consultar el Diagrama E/R de nuestro proyecto usando los siguientes enlaces:
- *https://github.com/MarcoHI14/MDAI-Proyecto/blob/main/DERR.md*
- *https://github.com/MarcoHI14/MDAI-Proyecto/blob/main/DERR.jpeg*

### *GUÍA DE INSTALACIÓN*
Antes de poder usar TunedHive deberás cumplir los siguientes requisitos:
- *Tener Docker Desktop instalado*

Estos son los pasos que deberás seguir para estar al día con las novedades de TunedHive:
- Paso 1: Clonar la rama main del Repositorio usando tu IDE de confianza
- Paso 2: Abrir la terminal desde el directorio donde se encuentra el archivo docker-compose.yml.  
- Paso 3: Ejecutar el siguiente comando:
	`docker compose up -d`

Dicho comando creará el contenedor de Docker necesario para el funcionamiento de la app. Posteriormente, la base de datos y sus tablas se generarán automáticamente gracias al archivo application.properties, en el que se encuentra la configuración del proyecto.

<!-- Información añadida sobre el volcado SQL y puerto de la app -->

- Volcado SQL poblado: en el repositorio incluimos el archivo `mdai_db_dump.sql` en la raíz del proyecto y también en `src/main/resources/mdai_db_dump.sql`. Si necesitas compartir la base de datos con otra persona (por ejemplo tu profesor), simplemente pásale este archivo y que lo restaure en su servidor MySQL. También puedes exportar la base de datos desde el contenedor Docker usando `mysqldump`.

- Carpeta de samples: el proyecto incluye una carpeta de ejemplo con ~20 canciones de prueba en `src/main/resources/samples` (útil para pruebas locales).

- Puerto de la aplicación web: la app se sirve en el puerto 8084 por defecto; una vez levantada, accede en http://localhost:8084

### Población automática de la BD al iniciar la aplicación
La aplicación comprueba al arrancar si la base de datos está disponible y si las tablas están pobladas. Importante: si la aplicación se inicia por primera vez o si detecta que la base de datos no contiene tablas o registros (es decir, está vacía), ejecutará de forma automática el volcado SQL incluido en el proyecto para crear las tablas y poblar los datos necesarios.

En otras palabras, al iniciar por primera vez la aplicación (o después de limpiar la BD), no necesitas importar manualmente el fichero SQL: la propia app intentará hacerlo por ti. El volcado utilizado está embebido en el proyecto (`src/main/resources/mdai_db_dump.sql`), y la importación se registra en la salida de la aplicación con mensajes que comienzan por `[MDAI]`.

Comportamiento en resumen:
- Inicio por primera vez / BD vacía -> la app ejecuta el volcado automáticamente y registra cada paso en consola.
- Si la importación falla por permisos u otro error, la app dejará mensajes de diagnóstico en los logs y puedes importar manualmente usando `mdai_db_dump.sql`.

Si prefieres forzar una nueva población (por ejemplo para pruebas), elimina las tablas del esquema o borra la base de datos y vuelve a arrancar la aplicación; la comprobación detectará que está vacía y volverá a aplicar el volcado.

Comportamiento esperado al arrancar:
- Si la conexión con la BD es correcta verás en la salida de la app mensajes con prefijo `[MDAI]` indicando el estado de la conexión.
- Si la BD está vacía, verás mensajes como `"[MDAI] Todas las tablas están vacías."` y después `"[MDAI] Poblando la base de datos..."` y detalles de las sentencias ejecutadas.
- Si ocurre algún error durante la ejecución del volcado, se registrará en consola y el proceso intentará un fallback. Revisa los logs para diagnosticar el problema.

Cómo arrancar la app y ver los mensajes (dos opciones comunes):

- Con Docker Compose (desde la raíz del repo):

```powershell
docker compose up -d
# ver logs del servicio app (muestra los mensajes [MDAI])
docker compose logs --follow app
```

- Ejecutando la app localmente con Maven:

```powershell
# desde la raíz del proyecto
.\mvnw.cmd -DskipTests spring-boot:run
# la salida en la terminal mostrará los mensajes [MDAI]
```

Si la población automática falla, puedes poblar manualmente usando el archivo SQL incluido.

### Poblar la BD manualmente (desde fichero SQL)
Opciones para importar `mdai_db_dump.sql` en la máquina destino:

- Usando el cliente `mysql` (si tienes el servidor MySQL accesible desde tu máquina):

```powershell
mysql -u <usuario> -p<contraseña> mdai_db < mdai_db_dump.sql
```

- Desde el contenedor MySQL (si estás usando Docker):

```powershell
# copia el sql al contenedor (opcional)
docker cp mdai_db_dump.sql <mysql_container>:/tmp/mdai_db_dump.sql
# importar directamente desde host al contenedor
docker exec -i <mysql_container> sh -c 'mysql -u mdai_user -pmdai_pass mdai_db' < mdai_db_dump.sql
```

- Alternativa: si prefieres, el repositorio ya contiene `src/main/resources/mdai_db_dump.sql`; puedes abrir ese archivo y ejecutarlo con tu herramienta de administración de MySQL.

Notas importantes:
- Asegúrate de que el usuario que usas para importar tenga permisos suficientes (CREATE, INSERT, ALTER, DROP, etc.).
- Si tu entorno usa contraseñas o usuarios distintos, ajusta los comandos (`mdai_user` / `mdai_pass`) a los tuyos.

### Cómo confirmar que la BD está poblada
- Tras importar o tras el arranque automático, en los logs de la app verás líneas `"[MDAI] Tabla 'X' -> N filas"`. Verifica que haya filas en tablas clave como `usuario`, `cancion` y `playlist`.
- También puedes conectarte con un cliente MySQL y ejecutar `SHOW TABLES;` y `SELECT COUNT(*) FROM usuario;` para comprobar.

### *Cómo exportar / importar la BD (recomendado)*

- Exportar desde el contenedor (ejemplo):
  - `docker exec -i <nombre_contenedor_mysql> mysqldump -u mdai_user -pmdai_pass mdai_db > mdai_db_dump.sql`

- Importar en una máquina destino (ejemplo):
  - `mysql -u usuario -p contraseña mdai_db < mdai_db_dump.sql`

> Nota: Si usas Docker, puedes copiar el archivo al contenedor y luego importarlo dentro del contenedor.

### *EJECUCIÓN DE TESTS*
*DISCLAIMER: PARA EJECUTAR LOS TESTS Y PROBAR EL FUNCIONAMIENTO DE LA APP DEBERÁS TENER ABIERTO EL CONTENEDOR DE DOCKER CORRESPONDIENTE*
