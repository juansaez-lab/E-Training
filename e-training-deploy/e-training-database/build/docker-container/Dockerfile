FROM mysql:5.6.32

ENV MYSQL_ROOT_PASSWORD=toor
ENV MYSQL_DATABASE=etime
ENV MYSQL_USER=aroot
ENV MYSQL_PASSWORD=password

COPY my.cnf /etc/mysql/
RUN chmod 0444 /etc/mysql/my.cnf

EXPOSE 3306