FROM nginx:alpine

# Copy UI files
COPY . /usr/share/nginx/html

# Replace default nginx config
COPY nginx.conf /etc/nginx/conf.d/default.conf
