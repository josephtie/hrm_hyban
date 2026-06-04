<#
.SYNOPSIS
    Ouvre un tunnel SSH securise vers les bases PostgreSQL (HRM + Keycloak) du VPS.

.DESCRIPTION
    Permet de connecter un utilitaire DB local (DBeaver, pgAdmin, DataGrip...)
    aux bases qui tournent dans Docker sur le VPS, sans exposer PostgreSQL
    publiquement sur Internet.

    Les ports distants correspondent aux ports HOTE publies dans
    docker-compose.portainer.yml :
        - HRM      : 5438 -> 5432 (conteneur)
        - Keycloak : 5439 -> 5432 (conteneur)

.EXAMPLE
    .\tunnel-db.ps1
    .\tunnel-db.ps1 -SshUser root -VpsHost 83.171.249.150
#>

param(
    [string]$VpsHost      = "83.171.249.150",
    [string]$SshUser      = "root",
    [int]$SshPort         = 22,

    # Ports locaux utilises sur VOTRE PC
    [int]$LocalHrmPort      = 5438,
    [int]$LocalKeycloakPort = 5439,

    # Ports HOTE publies sur le VPS (voir docker-compose.portainer.yml)
    [int]$RemoteHrmPort      = 5438,
    [int]$RemoteKeycloakPort = 5439
)

Write-Host "Ouverture du tunnel SSH vers $SshUser@$VpsHost ..." -ForegroundColor Cyan
Write-Host ""
Write-Host "  Base HRM      : localhost:$LocalHrmPort      -> VPS:$RemoteHrmPort" -ForegroundColor Green
Write-Host "  Base Keycloak : localhost:$LocalKeycloakPort -> VPS:$RemoteKeycloakPort" -ForegroundColor Green
Write-Host ""
Write-Host "Laissez cette fenetre OUVERTE tant que vous utilisez la base." -ForegroundColor Yellow
Write-Host "Fermez-la (Ctrl+C) pour couper le tunnel." -ForegroundColor Yellow
Write-Host ""

ssh -N `
    -p $SshPort `
    -L "${LocalHrmPort}:127.0.0.1:${RemoteHrmPort}" `
    -L "${LocalKeycloakPort}:127.0.0.1:${RemoteKeycloakPort}" `
    "$SshUser@$VpsHost"
