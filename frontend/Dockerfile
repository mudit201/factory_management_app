# Build stage
FROM node:20-alpine AS build

WORKDIR /app

# Copy package.json and lock file
COPY package*.json ./

# Install dependencies
RUN npm install

# Copy the rest of the source code
COPY . .

# Build the production-ready static files
RUN npm run build

# Test stage (optional, if you want to run Jest tests in CI)
# RUN npm run test

# Production stage - serve built files with a lightweight server (nginx)
FROM nginx:alpine

# Copy the built static files from the build stage to nginx www directory
COPY --from=build /app/dist /usr/share/nginx/html
COPY docker/nginx.conf /etc/nginx/conf.d/default.conf
# Expose port 80
EXPOSE 80

# Start nginx server
CMD ["nginx", "-g", "daemon off;"]