#!/bin/sh

# Replace API URL in the built JS files
find /usr/share/nginx/html -type f -name '*.js' -exec sed -i 's,http://localhost:9192,'"$REACT_APP_API_BASE_URL"',g' {} +