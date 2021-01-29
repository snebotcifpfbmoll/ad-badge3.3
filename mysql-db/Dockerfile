FROM mysql:8.0
COPY init.sql /docker-entrypoint-initdb.d/init-dbnerd.sql
HEALTHCHECK --interval=2s --retries=10 --timeout=10s CMD mysqladmin ping -h localhost
