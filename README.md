# DBNerd-1 & CI/CD
## DBNerd-1
Crea una aplicación restful que procese toda combinación de File Creator (rest, webscrapping, importación de datos por fichero) a través de un microservicio con una base de datos relacional.
## CI/CD
Genera una infraestructura de unit testing para tu servicio o implementa una característica de CI/CD a tu elección (jenkins, travis, bamboo, teamcity)
## MySQL y configuración
Se puede utilizar el archivo `mysql-db/Dockerfile` para generar una imagen de docker con MySQL y el schema preparado. Se deben especificar la variables de entorno:
- MYSQL_ROOT_PASSWORD
- MYSQL_DATABASE
- MYSQL_USER
- MYSQL_PASSWORD

Además se debe especificar unas variables para poder acceder a la base de datos en el archivo `application.properties`:
- BNERD_MYSQL_USER
- BNERD_MYSQL_PASSWORD
- DBNERD_MYSQL_URL
## Docker Compose
Hay una serie de archivos de Docker Compose que tienen diferentes propositos:
- `docker-compose.dev.yml`: Iniciar un contenedor de MySQL con el schema generado.
- `docker-compose.test.yml`: Realizar unit testing. Solo se utiliza en el Workflow de GitHub.
- `docker-compose.prod.yml`: Iniciar base de datos y webapp. Solo se utiliza en el servidor.
