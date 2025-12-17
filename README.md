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

### *Integración con Ollama y Embeddings*

Esta sección describe cómo integrar un servicio local de Ollama para generar embeddings y usarlos en la aplicación (búsqueda semántica, recomendaciones, etc.). Se asume que la app usa MySQL por defecto; adapta los ejemplos si usas otra base de datos o un vector DB.

Resumen
- Ollama permite ejecutar modelos LLM localmente. Generar embeddings localmente evita enviar datos a servicios externos y facilita la privacidad y el control.
- Flujo común: generar embeddings para metadatos (títulos, letras, descripciones) al subir canciones → guardar el vector en la BD (o vector DB) → al buscar, generar embedding de la consulta y recuperar por similitud (coseno).

Requisitos
- Ollama instalado y un modelo que soporte embeddings (sustituye el nombre del modelo por el que tengas disponible).
- Puerto y host accesible desde la app (por ejemplo, localhost:11434). En este README usaremos variables de entorno para configurar la conexión.
- La app tiene acceso a la base de datos (MySQL) para guardar metadatos y vectores, o a un Vector DB si prefieres.

Instalación (opciones)
- Instalación nativa: sigue la guía oficial de Ollama (elige la opción para tu SO). Una vez instalado, levanta el servidor local según la documentación.

- Usando Docker (ejemplo genérico para PowerShell):

```powershell
# Ejemplo genérico: reemplaza <imagen_ollama> y puertos según tu imagen/versión
docker run --rm -p 11434:11434 --name ollama <imagen_ollama>:latest
```

Nota: la imagen/flag exacta depende de la distribución de Ollama que uses. Si instalaste Ollama nativo, probablemente no necesites Docker.

Endpoint local (plantilla)
- Suponemos un endpoint HTTP local para obtener embeddings. Usa estas variables:
  - OLLAMA_HOST (ej: localhost)
  - OLLAMA_PORT (ej: 11434)
  - OLLAMA_MODEL (ej: nombre_del_modelo)

- URL genérica de ejemplo para embeddings:
  - http://{OLLAMA_HOST}:{OLLAMA_PORT}/v1/embeddings

Ejemplo de petición (curl / PowerShell)
- Plantilla curl (ajusta la URL y cuerpo según tu servidor Ollama):

```bash
curl -X POST "http://localhost:11434/v1/embeddings" \
  -H "Content-Type: application/json" \
  -d '{"model":"<OLLAMA_MODEL>", "input": "Texto a convertir en embedding"}'
```

- Plantilla PowerShell con Invoke-RestMethod:

```powershell
$body = @{ model = '<OLLAMA_MODEL>'; input = 'Texto a convertir en embedding' } | ConvertTo-Json
Invoke-RestMethod -Method Post -Uri "http://localhost:11434/v1/embeddings" -ContentType 'application/json' -Body $body
```

Ejemplo en Java (HttpClient + Jackson - ilustrativo)
- Este snippet muestra cómo enviar la petición y obtener el vector; adapta importaciones y manejo de errores a tu código:

```java
// Ejemplo mínimo (Java 11+)
HttpClient client = HttpClient.newHttpClient();
String url = String.format("http://%s:%s/v1/embeddings", System.getenv("OLLAMA_HOST"), System.getenv("OLLAMA_PORT"));
ObjectMapper mapper = new ObjectMapper();
ObjectNode body = mapper.createObjectNode();
body.put("model", System.getenv("OLLAMA_MODEL"));
body.put("input", "Texto a convertir en embedding");
HttpRequest req = HttpRequest.newBuilder()
    .uri(URI.create(url))
    .header("Content-Type", "application/json")
    .POST(HttpRequest.BodyPublishers.ofString(body.toString()))
    .build();
HttpResponse<String> resp = client.send(req, HttpResponse.BodyHandlers.ofString());
// Parsear la respuesta (estructura puede variar según la API de tu servidor)
JsonNode root = mapper.readTree(resp.body());
// Ejemplo: suponemos root.embedding o root.data[0].embedding
JsonNode embeddingNode = root.path("data").path(0).path("embedding");
if (embeddingNode.isArray()) {
    List<Double> vector = new ArrayList<>();
    embeddingNode.forEach(n -> vector.add(n.asDouble()));
    // Guardar vector en BD o vector DB
}
```

Almacenamiento de embeddings (esquema sugerido)
- Esquema relacional simple (MySQL) — tabla `embedding`:
  - id (PK)
  - referencia_id (FK a `cancion` u otra entidad)
  - tipo (ej: "cancion_titulo", "letra")
  - vector (opcional: JSON o BLOB según MySQL; también puedes normalizar y guardar en tabla vectorial)
  - created_at

- Alternativa: usar una Vector DB (Milvus, Weaviate, Pinecone) y guardar allí los vectores, manteniendo referencias a filas de MySQL.

Flujo recomendado
1. Al subir una canción o actualizar metadatos -> generar embedding(s) para los campos relevantes.
2. Guardar embedding en la tabla `embedding` o en la Vector DB (con referencia a la canción).
3. Al realizar una búsqueda: generar embedding de la consulta, recuperar N candidatos por similitud (cálculo coseno), y después filtrar/ordenar con datos de la BD.

Cálculo de similitud
- Normalmente se usa similitud del coseno. Para dos vectores a y b:
  - cos_sim(a,b) = (a · b) / (||a|| * ||b||)
- En SQL se puede approximar calculando el producto punto y las normas, o delegar la operación a un vector DB para rendimiento.

Variables de entorno sugeridas
- OLLAMA_HOST=localhost
- OLLAMA_PORT=11434
- OLLAMA_MODEL=nombre_del_modelo
- EMBEDDING_DIM=768 (ajusta según el modelo)
- VECTOR_DB_URL=jdbc:mysql://localhost:3306/mdai_db (o URL a vector DB)

Ejemplo de application.properties (placeholders)

```
# Ollama
ollama.host=${OLLAMA_HOST:localhost}
ollama.port=${OLLAMA_PORT:11434}
ollama.model=${OLLAMA_MODEL:mi_modelo}

# Embeddings
embeddings.dim=${EMBEDDING_DIM:768}
embeddings.service.url=http://${OLLAMA_HOST:localhost}:${OLLAMA_PORT:11434}/v1/embeddings
```

Notas de rendimiento y seguridad
- Batch: solicita embeddings en lotes cuando proceses muchos textos para reducir latencia.
- Caché: almacena embeddings ya generados para evitar recomputarlos.
- Tamaño: modelos con embeddings grandes aumentan almacenamiento y coste de cálculo; ajusta EMBEDDING_DIM.
- Privacidad: si procesas datos sensibles, preferir ejecución local y restringir acceso al servicio Ollama.

Troubleshooting rápido
- Puerto en uso: si no puedes levantar Ollama, verifica el puerto (p.ej. 11434) y que no haya conflictos.
- Modelo no encontrado: asegúrate de que el modelo está instalado o disponible en Ollama; revisa la lista de modelos cargados en el servidor.
- Respuesta vacía o formato distinto: inspecciona la respuesta cruda (logs) y ajusta el parsing. La estructura JSON puede variar según la versión del servidor.

Recursos útiles
- Documentación oficial de Ollama (usa la fuente oficial para comandos e imágenes)
- Artículos sobre búsqueda vectorial y similitud coseno
- Bibliotecas para trabajo con vectores en Java (Ej: Apache Commons Math) y clientes para Vector DBs (Milvus, Weaviate)
