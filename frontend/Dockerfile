# syntax=docker/dockerfile:1

FROM node:20.17.0-alpine AS build
WORKDIR /build
COPY package.json package-lock.json ./
RUN npm ci
COPY . .
RUN npm run build -- --configuration production

FROM nginxinc/nginx-unprivileged:1.27-alpine AS runtime
COPY --from=build /build/dist/gabaritai-frontend/browser /usr/share/nginx/html
COPY nginx.conf /etc/nginx/conf.d/default.conf
EXPOSE 8080
HEALTHCHECK --interval=30s --timeout=5s --start-period=15s --retries=3 \
    CMD wget -qO- http://localhost:8080/healthz || exit 1
