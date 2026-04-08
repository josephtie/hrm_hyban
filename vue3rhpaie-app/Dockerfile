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
