FROM postgis/postgis:15-master
COPY *_structure.sql /
COPY container_scripts/* docker-entrypoint-initdb.d/
