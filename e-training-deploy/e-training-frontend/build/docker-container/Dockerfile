FROM alpine:3.20.3

RUN apk add --no-cache nginx
RUN apk add --no-cache bash
RUN apk add --no-cache dos2unix

COPY /dist/e-time /usr/share/nginx/html
COPY default.conf /etc/nginx/conf.d/default.conf
COPY nginx.conf /etc/nginx/nginx.conf
COPY configure.sh /usr/share/nginx/html

RUN chmod 644 /etc/nginx/nginx.conf /etc/nginx/conf.d/default.conf
RUN dos2unix /usr/share/nginx/html/configure.sh
RUN chmod +x /usr/share/nginx/html/configure.sh

CMD ["sh", "-c", "/usr/share/nginx/html/configure.sh && nginx -g 'daemon off;'"]
