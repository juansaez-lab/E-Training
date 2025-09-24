#!/bin/sh

echo "window._env_ = {
  API_URL: '${API_URL}',
  ENVIRONMENT: '${ENVIRONMENT}',
  TOP_IMAGE: '${TOP_IMAGE}'
};" > /usr/share/nginx/html/env-config.js

sed -i '/<\/head>/i <script src="env-config.js"></script>' /usr/share/nginx/html/index.html
