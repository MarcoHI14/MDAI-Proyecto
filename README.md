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
	*docker compose up -d*

Dicho comando creará el contenedor de Docker necesario para el funcionamiento de la app. Posteriormente, la base de datos y sus tablas se generarán automáticamente gracias al archivo application.properties, en el que se encuentra la configuración del proyecto.
Dicho comando creará el contenedor de Docker necesario para el funcionamiento de la app. 
Posteriormente, la base de datos y sus tablas se generarán automáticamente gracias al archivo application.properties, en el que se encuentra la configuración del proyecto.


### *EJECUCIÓN DE TESTS*
*DISCLAIMER: PARA EJECUTAR LOS TESTS Y PROBAR EL FUNCIONAMIENTO DE LA APP DEBERÁS TENER ABIERTO EL CONTENEDOR DE DOCKER CORRESPONDIENTE*
