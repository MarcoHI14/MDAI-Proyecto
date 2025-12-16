package com.example.MDAI_Proyecto;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.charset.StandardCharsets;
import java.io.InputStream;
import java.io.IOException;

@SpringBootApplication
public class MdaiProyectoApplication {

    private static final Logger logger = LoggerFactory.getLogger(MdaiProyectoApplication.class);

    public static void main(String[] args) {
        SpringApplication.run(MdaiProyectoApplication.class, args);
    }

    @Bean
    public CommandLineRunner checkDatabase(DataSource dataSource) {
        return args -> {
            String startMsg = "[MDAI] Comprobando conexión con la base de datos...";
            logger.info(startMsg);
            System.out.println(startMsg);

            try (Connection conn = dataSource.getConnection()) {
                if (conn != null && !conn.isClosed()) {
                    String url = conn.getMetaData() != null ? conn.getMetaData().getURL() : "(desconocida)";
                    String okMsg = "[MDAI] Conexión establecida con la base de datos. URL=" + url;
                    logger.info(okMsg);
                    System.out.println(okMsg);

                    // Listar todas las tablas básicas (BASE TABLE) del esquema actual
                    List<String> tablas = new ArrayList<>();
                    try (PreparedStatement ps = conn.prepareStatement(
                            "SELECT table_name FROM information_schema.tables WHERE table_schema = DATABASE() AND table_type = 'BASE TABLE'"
                    )) {
                        try (ResultSet rs = ps.executeQuery()) {
                            while (rs.next()) {
                                tablas.add(rs.getString(1));
                            }
                        }
                    } catch (SQLException e) {
                        String warn = "[MDAI] No se pudo listar las tablas en information_schema: " + e.getMessage();
                        logger.warn(warn);
                        System.out.println(warn);
                    }

                    if (tablas.isEmpty()) {
                        String emptyMsg = "[MDAI] No se encontraron tablas en la base de datos. Se considera vacía.";
                        logger.info(emptyMsg);
                        System.out.println(emptyMsg);

                        // Si no hay tablas, intentar poblar desde el dump
                        poblar(conn);

                    } else {
                        boolean anyPopulated = false;
                        System.out.println("[MDAI] Se han detectado " + tablas.size() + " tablas. Comprobando filas por tabla...");
                        logger.info("Tablas detectadas: {}", tablas.size());

                        for (String tabla : tablas) {
                            long count = -1L;
                            try (PreparedStatement ps2 = conn.prepareStatement("SELECT COUNT(*) FROM `" + tabla + "`")) {
                                try (ResultSet rs2 = ps2.executeQuery()) {
                                    if (rs2.next()) {
                                        count = rs2.getLong(1);
                                    }
                                }
                            } catch (SQLException ex) {
                                // Puede fallar en tablas con privilegios distintos o tipos especiales; lo registramos y seguimos
                                String dbg = "[MDAI] No se pudo obtener número de filas para tabla '" + tabla + "': " + ex.getMessage();
                                logger.debug(dbg);
                                System.out.println(dbg);
                                continue;
                            }

                            String tblMsg = "[MDAI] Tabla '" + tabla + "' -> " + count + " filas";
                            logger.info(tblMsg);
                            System.out.println(tblMsg);

                            if (count > 0) {
                                anyPopulated = true;
                            }
                        }

                        if (anyPopulated) {
                            String someMsg = "[MDAI] Al menos una tabla contiene datos.";
                            logger.info(someMsg);
                            System.out.println(someMsg);
                        } else {
                            String noneMsg = "[MDAI] Todas las tablas están vacías.";
                            logger.info(noneMsg);
                            System.out.println(noneMsg);

                            // Si todas están vacías, poblar desde el dump
                            poblar(conn);
                        }
                    }

                } else {
                    String err = "[MDAI] No se pudo establecer una conexión válida con la base de datos (conn null o cerrada).";
                    logger.error(err);
                    System.out.println(err);
                }
            } catch (SQLException ex) {
                String err = "[MDAI] Error al conectar con la base de datos: " + ex.getMessage();
                logger.error(err, ex);
                System.out.println(err);
            }
        };
    }

    // Método que aplica el volcado SQL mdai_db_dump.sql sobre la conexión
    private void poblar(Connection conn) {
        String info = "[MDAI] Poblando la base de datos con el volcado embebido...";
        logger.info(info);
        System.out.println(info);

        // Intentar leer el fichero mdai_db_dump.sql desde el directorio de trabajo
        String dump = null;
        try {
            Path p = Paths.get("mdai_db_dump.sql");
            if (Files.exists(p) && Files.isRegularFile(p)) {
                dump = Files.readString(p, StandardCharsets.UTF_8);
                logger.info("[MDAI] mdai_db_dump.sql leído desde: {}", p.toAbsolutePath());
                System.out.println("[MDAI] mdai_db_dump.sql leído desde: " + p.toAbsolutePath());
            } else {
                // Intentar cargar desde classpath (por si el archivo fue incluido como recurso)
                try (InputStream is = getClass().getClassLoader().getResourceAsStream("mdai_db_dump.sql")) {
                    if (is != null) {
                        dump = new String(is.readAllBytes(), StandardCharsets.UTF_8);
                        logger.info("[MDAI] mdai_db_dump.sql leído desde classpath.");
                        System.out.println("[MDAI] mdai_db_dump.sql leído desde classpath.");
                    }
                } catch (IOException e) {
                    logger.warn("[MDAI] Error al leer mdai_db_dump.sql desde classpath: {}", e.getMessage());
                }
            }
        } catch (IOException e) {
            logger.warn("[MDAI] No se pudo leer mdai_db_dump.sql en disco: {}", e.getMessage());
            System.out.println("[MDAI] No se pudo leer mdai_db_dump.sql en disco: " + e.getMessage());
        }

        if (dump == null || dump.trim().isEmpty()) {
            String warn = "[MDAI] mdai_db_dump.sql no encontrado o vacío; no se ejecutará la población desde archivo.";
            logger.warn(warn);
            System.out.println(warn);
            return; // No hay volcado para ejecutar
        }

         try {
             int executed = ejecutarScriptDesdeString(conn, dump);
             if (executed > 0) {
                 String ok = "[MDAI] Población automática completada. Sentencias ejecutadas: " + executed;
                 logger.info(ok);
                 System.out.println(ok);
             } else {
                 String warn = "[MDAI] No se ejecutó ninguna sentencia del volcado embebido. Comprueba el contenido.";
                 logger.warn(warn);
                 System.out.println(warn);
             }
         } catch (Exception ex) {
             String err = "[MDAI] Error al ejecutar el volcado embebido: " + ex.getMessage();
             logger.error(err, ex);
             System.out.println(err);
         }
     }

     // Ejecuta un script SQL representado por un string: limpia comentarios y divide por sentencias ';'
     private int ejecutarScriptDesdeString(Connection conn, String script) throws SQLException {
         if (script == null || script.trim().isEmpty()) {
             String msg = "[MDAI] El script proporcionado está vacío. No hay nada que ejecutar.";
             logger.warn(msg);
             System.out.println(msg);
             return 0;
         }

         // 1) Reemplazar bloques versionados /*!...*/ por su contenido
         String s = script.replaceAll("(?s)/\\*!\\d+\\s*(.*?)\\*/", "$1");
         // 2) Eliminar otros comentarios de bloque /* ... */
         s = s.replaceAll("(?s)/\\*.*?\\*/", " ");
         // 3) Eliminar comentarios de línea que comiencen por --
         s = s.replaceAll("(?m)^--.*$", "");
         // 4) Eliminar líneas DELIMITER
         s = s.replaceAll("(?m)^\\s*DELIMITER\\s+\\S+\\s*$", "");

         if (s == null || s.trim().isEmpty()) {
             String msg = "[MDAI] Tras limpiar el volcado no se detectaron sentencias SQL ejecutables. Abortando población.";
             logger.warn(msg);
             System.out.println(msg);
             return 0;
         }

         // 5) Ahora dividir por punto y coma. Evitamos splits dentro de literales simples.
         List<String> statements = new ArrayList<>();
         StringBuilder sb = new StringBuilder();
         boolean inSingleQuote = false;
         for (int i = 0; i < s.length(); i++) {
             char c = s.charAt(i);
             if (c == '\'') {
                 inSingleQuote = !inSingleQuote;
                 sb.append(c);
             } else if (c == ';' && !inSingleQuote) {
                 String stmt = sb.toString().trim();
                 if (!stmt.isEmpty()) statements.add(stmt);
                 sb.setLength(0);
             } else {
                 sb.append(c);
             }
         }
         String last = sb.toString().trim();
         if (!last.isEmpty()) statements.add(last);

         if (statements.isEmpty()) {
             String msg = "[MDAI] No se generaron sentencias SQL tras dividir el script. Abortando población.";
             logger.warn(msg);
             System.out.println(msg);
             return 0;
         }

         // Pre-procesado: eliminar DROP TABLE IF EXISTS `t` que aparecen después de un CREATE TABLE `t` y antes de un INSERT INTO `t`.
         java.util.regex.Pattern createPat = java.util.regex.Pattern.compile("(?i)CREATE\\s+TABLE\\s+`([^`]+)`");
         java.util.regex.Pattern dropPat = java.util.regex.Pattern.compile("(?i)DROP\\s+TABLE\\s+IF\\s+EXISTS\\s+`([^`]+)`");
         java.util.regex.Pattern insertPat = java.util.regex.Pattern.compile("(?i)INSERT\\s+INTO\\s+`([^`]+)`");

         java.util.Map<String, Integer> lastCreate = new java.util.HashMap<>();
         java.util.Map<String, java.util.List<Integer>> inserts = new java.util.HashMap<>();
         java.util.Map<Integer, String> drops = new java.util.HashMap<>();

         for (int i = 0; i < statements.size(); i++) {
             String st = statements.get(i);
             java.util.regex.Matcher mCreate = createPat.matcher(st);
             if (mCreate.find()) {
                 lastCreate.put(mCreate.group(1), i);
             }
             java.util.regex.Matcher mInsert = insertPat.matcher(st);
             if (mInsert.find()) {
                 inserts.computeIfAbsent(mInsert.group(1), k -> new java.util.ArrayList<>()).add(i);
             }
             java.util.regex.Matcher mDrop = dropPat.matcher(st);
             if (mDrop.find()) {
                 drops.put(i, mDrop.group(1));
             }
         }

         java.util.List<String> filtered = new java.util.ArrayList<>();
         for (int i = 0; i < statements.size(); i++) {
             String st = statements.get(i);
             if (drops.containsKey(i)) {
                 String table = drops.get(i);
                 Integer cIdx = lastCreate.get(table);
                 java.util.List<Integer> insIdxs = inserts.get(table);
                 boolean hasInsertAfter = false;
                 if (insIdxs != null) {
                     for (Integer idx : insIdxs) {
                         if (idx > i) { hasInsertAfter = true; break; }
                     }
                 }
                 if (cIdx != null && cIdx < i && hasInsertAfter) {
                     // Omitir este DROP porque hay un CREATE antes y un INSERT después (probable duplicado en el dump)
                     logger.info("[MDAI] Omitiendo DROP TABLE `{}` en script porque hay CREATE antes y INSERT después.", table);
                     System.out.println("[MDAI] Omitiendo DROP TABLE `" + table + "` en script porque hay CREATE antes y INSERT después.");
                     continue; // skip adding to filtered
                 }
             }
             filtered.add(st);
         }

         // Reasignar statements a la lista filtrada
         statements = filtered;

         // Recalcular mapa de CREATE en la lista filtrada (table -> index)
         java.util.Map<String, Integer> createIndex = new java.util.HashMap<>();
         for (int i = 0; i < statements.size(); i++) {
             java.util.regex.Matcher mCreate2 = createPat.matcher(statements.get(i));
             if (mCreate2.find()) {
                 createIndex.put(mCreate2.group(1), i);
             }
         }

         int executedCount = 0;
         // Ejecutar las sentencias en modo autocommit: no queremos que una sentencia fallida detenga todo el volcado
         boolean previousAutoCommit = conn.getAutoCommit();
         try (java.sql.Statement st = conn.createStatement()) {
             conn.setAutoCommit(true);
             for (int idx = 0; idx < statements.size(); idx++) {
                 String stmt = statements.get(idx);
                 String trimmed = stmt.trim();
                 if (trimmed.isEmpty()) continue;
                 logger.debug("Ejecutando sentencia SQL ({} chars)...", trimmed.length());
                 System.out.println("[MDAI] Ejecutando sentencia SQL (" + Math.min(trimmed.length(), 80) + " chars): " + (trimmed.length() > 80 ? trimmed.substring(0, 80) + "..." : trimmed));

                 // Evitar ejecutar ALTER/LOCK sobre tablas inexistentes: comprobar existencia antes
                 String lower = trimmed.toLowerCase();
                 boolean requiresTable = false;
                 String tableName = null;
                 if (lower.startsWith("alter table") || lower.startsWith("lock tables") ) {
                     requiresTable = true;
                     // Extraer el primer nombre de tabla entre backticks si existe
                     java.util.regex.Matcher m = java.util.regex.Pattern.compile("`([^`]+)`").matcher(trimmed);
                     if (m.find()) {
                         tableName = m.group(1);
                     }
                 }

                 if (requiresTable && tableName != null) {
                     // Comprobar existencia en information_schema
                     boolean exists = false;
                     try (PreparedStatement check = conn.prepareStatement("SELECT COUNT(*) FROM information_schema.tables WHERE table_schema = DATABASE() AND table_name = ?")) {
                         check.setString(1, tableName);
                         try (ResultSet rs = check.executeQuery()) {
                             if (rs.next() && rs.getInt(1) > 0) exists = true;
                         }
                     } catch (SQLException ex) {
                         logger.debug("No se pudo comprobar existencia de tabla {}: {}", tableName, ex.getMessage());
                     }

                     if (!exists) {
                         logger.info("[MDAI] Se omite sentencia que requiere la tabla '{}' porque no existe en la BD: {}", tableName, trimmed.length() > 80 ? trimmed.substring(0, 80) + "..." : trimmed);
                         System.out.println("[MDAI] Se omite sentencia que requiere la tabla '" + tableName + "' porque no existe en la BD.");
                         continue;
                     }
                 }

                 try {
                     st.execute(trimmed);
                     executedCount++;
                 } catch (SQLException ex) {
                     // Si es un INSERT y la tabla no existe, intentar ejecutar el CREATE correspondiente si aparece más adelante
                     boolean handled = false;
                     try {
                         if (trimmed.toLowerCase().startsWith("insert into")) {
                             java.util.regex.Matcher mIns = java.util.regex.Pattern.compile("(?i)INSERT\\s+INTO\\s+`([^`]+)`").matcher(trimmed);
                             if (mIns.find()) {
                                 String t = mIns.group(1);
                                 Integer createIdx = createIndex.get(t);
                                 if (createIdx != null && createIdx > idx) {
                                     String createStmt = statements.get(createIdx);
                                     try {
                                         System.out.println("[MDAI] INSERT falló por tabla inexistente; ejecutando CREATE TABLE posterior para '" + t + "' y reintentando INSERT.");
                                         logger.info("[MDAI] Ejecutando CREATE tardío para tabla {} (index {})", t, createIdx);
                                         st.execute(createStmt);
                                         executedCount++;
                                         // Ahora reintentar el INSERT
                                         st.execute(trimmed);
                                         executedCount++;
                                         handled = true;
                                         // Marcar el CREATE como ejecutado evitando re-ejecución: vaciar la entrada
                                         createIndex.remove(t);
                                     } catch (SQLException ex2) {
                                         String warn2 = "[MDAI] Falló al ejecutar CREATE tardío para tabla '" + t + "': " + ex2.getMessage();
                                         logger.warn(warn2, ex2);
                                         System.out.println(warn2);
                                     }
                                 }
                             }
                         }
                     } catch (Exception inner) {
                         logger.debug("Error manejando INSERT faltante: {}", inner.getMessage());
                     }

                     if (!handled) {
                         // No abortamos toda la importación; registramos y seguimos con la siguiente sentencia
                         String warn = "[MDAI] Error al ejecutar sentencia SQL (se omite): " + ex.getMessage();
                         logger.warn(warn, ex);
                         System.out.println(warn);
                     }
                     continue;
                 }
             }
         } finally {
             // restaurar modo autocommit original
             try {
                 conn.setAutoCommit(previousAutoCommit);
             } catch (SQLException e) {
                 logger.warn("No se pudo restaurar autocommit: {}", e.getMessage());
             }
         }
         return executedCount;
     }

 }
