package com.nectux.mizan.hyban.personnel.service;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.beans.factory.annotation.Value;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.UUID;

/**
 * Service pour la gestion des documents uploadés
 */
@Service
public class DocumentService {

    private static final Logger logger = LoggerFactory.getLogger(DocumentService.class);

    @Value("${app.upload.directory:uploads/documents}")
    private String uploadDirectory;

    @Value("${app.upload.max-size:10485760}") // 10MB par défaut
    private long maxFileSize;

    // Extensions autorisées
    private static final String[] ALLOWED_EXTENSIONS = {
        ".pdf", ".doc", ".docx", ".xls", ".xlsx", ".ppt", ".pptx", ".txt", ".jpg", ".jpeg", ".png", ".json"
    };

    /**
     * Sauvegarde un fichier uploadé
     * @param file Le fichier à sauvegarder
     * @return Le chemin relatif du fichier sauvegardé
     * @throws IOException En cas d'erreur lors de la sauvegarde
     */
    public String saveDocument(MultipartFile file) throws IOException {
        logger.info("Début de la sauvegarde du document: {}", file.getOriginalFilename());

        // Validation du fichier
        validateFile(file);

        // Création du répertoire si nécessaire
        Path uploadPath = Paths.get(uploadDirectory);
        if (!Files.exists(uploadPath)) {
            Files.createDirectories(uploadPath);
            logger.info("Répertoire de upload créé: {}", uploadPath);
        }

        // Génération d'un nom de fichier unique
        String originalFilename = file.getOriginalFilename();
        String extension = getFileExtension(originalFilename);
        String timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss"));
        String uniqueFilename = UUID.randomUUID().toString() + "_" + timestamp + extension;

        // Chemin complet du fichier
        Path filePath = uploadPath.resolve(uniqueFilename);

        // Sauvegarde du fichier
        Files.copy(file.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);

        String relativePath = uploadDirectory + "/" + uniqueFilename;
        logger.info("Document sauvegardé avec succès: {}", relativePath);

        return relativePath;
    }

    /**
     * Supprime un document
     * @param filePath Le chemin du fichier à supprimer
     * @return true si la suppression a réussi, false sinon
     */
    public boolean deleteDocument(String filePath) {
        try {
            Path path = Paths.get(filePath);
            if (Files.exists(path)) {
                Files.delete(path);
                logger.info("Document supprimé avec succès: {}", filePath);
                return true;
            } else {
                logger.warn("Tentative de suppression d'un fichier qui n'existe pas: {}", filePath);
                return false;
            }
        } catch (IOException e) {
            logger.error("Erreur lors de la suppression du document {}: {}", filePath, e.getMessage());
            return false;
        }
    }

    /**
     * Vérifie si un fichier existe
     * @param filePath Le chemin du fichier à vérifier
     * @return true si le fichier existe, false sinon
     */
    public boolean documentExists(String filePath) {
        return Files.exists(Paths.get(filePath));
    }

    /**
     * Valide un fichier uploadé
     * @param file Le fichier à valider
     * @throws IOException Si le fichier n'est pas valide
     */
    private void validateFile(MultipartFile file) throws IOException {
        if (file.isEmpty()) {
            throw new IOException("Le fichier est vide");
        }

        if (file.getSize() > maxFileSize) {
            throw new IOException("Le fichier dépasse la taille maximale autorisée de " + (maxFileSize / 1024 / 1024) + "MB");
        }

        String originalFilename = file.getOriginalFilename();
        if (originalFilename == null || originalFilename.trim().isEmpty()) {
            throw new IOException("Le nom du fichier ne peut pas être vide");
        }

        String extension = getFileExtension(originalFilename);
        if (!isAllowedExtension(extension)) {
            throw new IOException("Extension de fichier non autorisée: " + extension);
        }
    }

    /**
     * Récupère l'extension d'un fichier
     * @param filename Le nom du fichier
     * @return L'extension du fichier (avec le point)
     */
    private String getFileExtension(String filename) {
        if (filename == null || filename.lastIndexOf('.') == -1) {
            return "";
        }
        return filename.substring(filename.lastIndexOf('.')).toLowerCase();
    }

    /**
     * Vérifie si une extension est autorisée
     * @param extension L'extension à vérifier
     * @return true si l'extension est autorisée
     */
    private boolean isAllowedExtension(String extension) {
        for (String allowedExt : ALLOWED_EXTENSIONS) {
            if (allowedExt.equals(extension)) {
                return true;
            }
        }
        return false;
    }

    /**
     * Récupère la taille maximale autorisée pour les fichiers
     * @return La taille maximale en octets
     */
    public long getMaxFileSize() {
        return maxFileSize;
    }

    /**
     * Récupère le répertoire de upload
     * @return Le chemin du répertoire de upload
     */
    public String getUploadDirectory() {
        return uploadDirectory;
    }
}
