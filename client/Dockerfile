FROM node:18-alpine AS build

WORKDIR /app

COPY . .

RUN npm install
RUN npm run build --prod

FROM nginx:alpine

COPY --from=build /app/dist/astro-cluster-client /usr/share/nginx/html

# Copy the modified default.conf file
COPY ./nginx/default.conf /etc/nginx/conf.d/default.conf

EXPOSE 80

# Start Nginx
CMD ["nginx", "-g", "daemon off;"]













