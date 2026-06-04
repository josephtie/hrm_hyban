#!/usr/bin/env bash
#
# Ouvre un tunnel SSH securise vers les bases PostgreSQL (HRM + Keycloak) du VPS.
#
# Permet de connecter un utilitaire DB local (DBeaver, pgAdmin, DataGrip...)
# aux bases qui tournent dans Docker sur le VPS, sans exposer PostgreSQL
# publiquement sur Internet.
#
# Ports HOTE publies dans docker-compose.portainer.yml :
#   - HRM      : 5438 -> 5432 (conteneur)
#   - Keycloak : 5439 -> 5432 (conteneur)
#
# Usage :
#   ./tunnel-db.sh
#   VPS_HOST=83.171.249.150 SSH_USER=root ./tunnel-db.sh

set -euo pipefail

VPS_HOST="${VPS_HOST:-83.171.249.150}"
SSH_USER="${SSH_USER:-root}"
SSH_PORT="${SSH_PORT:-22}"

# Ports locaux (sur votre PC)
LOCAL_HRM_PORT="${LOCAL_HRM_PORT:-5438}"
LOCAL_KEYCLOAK_PORT="${LOCAL_KEYCLOAK_PORT:-5439}"

# Ports HOTE publies sur le VPS
REMOTE_HRM_PORT="${REMOTE_HRM_PORT:-5438}"
REMOTE_KEYCLOAK_PORT="${REMOTE_KEYCLOAK_PORT:-5439}"

echo "Ouverture du tunnel SSH vers ${SSH_USER}@${VPS_HOST} ..."
echo
echo "  Base HRM      : localhost:${LOCAL_HRM_PORT}      -> VPS:${REMOTE_HRM_PORT}"
echo "  Base Keycloak : localhost:${LOCAL_KEYCLOAK_PORT} -> VPS:${REMOTE_KEYCLOAK_PORT}"
echo
echo "Laissez ce terminal OUVERT tant que vous utilisez la base (Ctrl+C pour couper)."
echo

ssh -N \
    -p "${SSH_PORT}" \
    -L "${LOCAL_HRM_PORT}:127.0.0.1:${REMOTE_HRM_PORT}" \
    -L "${LOCAL_KEYCLOAK_PORT}:127.0.0.1:${REMOTE_KEYCLOAK_PORT}" \
    "${SSH_USER}@${VPS_HOST}"
