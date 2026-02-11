# Use the official Node.js image as the base image
FROM docker.arvancloud.ir/node:latest AS build

# Set the working directory
WORKDIR /app

# Copy package.json and package-lock.json
COPY package.json package-lock.json ./

# Install dependencies
RUN npm install

# Copy the rest of the application code
COPY ../.. .

# Build the application
RUN npm run build

# Use a lightweight web server to serve the application
FROM docker.arvancloud.ir/nginx:alpine

ENV REACT_APP_API_BASE_URL=http://backend:9192

# Copy the built application from the build stage
COPY --from=build /app/dist /usr/share/nginx/html

COPY ./nginx.conf ./conf.d/default.conf

# Copy the built files
#COPY build /usr/share/nginx/html

# Expose the port that Nginx will run on
EXPOSE 80
#EXPOSE 5173

COPY ./env.sh /docker-entrypoint.d/
RUN chmod +x /docker-entrypoint.d/env.sh

# Start Nginx
CMD ["nginx", "-g", "daemon off;"]

#CMD ["npm", "run", "dev"]
