# -------- BUILD + RUN --------
FROM node:20-alpine

WORKDIR /app

# Copier package.json
COPY package*.json ./

# Installer dépendances (IMPORTANT: pas production-only)
RUN npm install

# Copier le reste
COPY . .

# Build
RUN npm run build

# Exposer port Vite preview
EXPOSE 5173

# Lancer en mode preview
CMD ["npm", "run", "preview"]


# # Étape 1: Build
# FROM node:18-alpine AS builder

# WORKDIR /app

# # Copier les fichiers package.json et package-lock.json
# COPY package*.json ./
# COPY .env.docker .env

# # Installer les dépendances
# RUN npm ci --only=production

# # Copier le code source
# COPY . .

# # Build l'application
# RUN npm run build

# # Étape 2: Production avec Nginx
# FROM nginx:alpine

# # Copier les fichiers build depuis l'étape de build
# COPY --from=builder /app/dist /usr/share/nginx/html

# # Copier la configuration nginx
# COPY nginx.conf /etc/nginx/conf.d/default.conf

# # Exposer le port
# EXPOSE 80

# # Démarrer nginx
# CMD ["nginx", "-g", "daemon off;"]
