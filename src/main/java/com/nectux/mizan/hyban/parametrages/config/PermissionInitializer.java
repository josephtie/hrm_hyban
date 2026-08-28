package com.nectux.mizan.hyban.parametrages.config;

import com.nectux.mizan.hyban.parametrages.entity.Permission;
import com.nectux.mizan.hyban.parametrages.entity.Role;
import com.nectux.mizan.hyban.parametrages.entity.RoleName;
import com.nectux.mizan.hyban.parametrages.repository.PermissionRepository;
import com.nectux.mizan.hyban.parametrages.repository.RoleRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.util.*;

@Component
@Order(1)
public class PermissionInitializer implements CommandLineRunner {

    private static final Logger logger = LoggerFactory.getLogger(PermissionInitializer.class);

    @Autowired
    private PermissionRepository permissionRepository;

    @Autowired
    private RoleRepository roleRepository;

    private static final List<String> ALL_PERMISSIONS = Arrays.asList(
            "EMPLOYEE_READ", "EMPLOYEE_CREATE", "EMPLOYEE_UPDATE", "EMPLOYEE_DELETE", "EMPLOYEE_EXPORT",
            "CONTRACT_READ", "CONTRACT_CREATE", "CONTRACT_UPDATE", "CONTRACT_CLOSE", "CONTRACT_EXPORT",
            "PAYROLL_READ", "PAYROLL_CALCULATE", "PAYROLL_VALIDATE", "PAYROLL_CANCEL", "PAYROLL_EXPORT",
            "LEAVE_READ", "LEAVE_CREATE", "LEAVE_UPDATE", "LEAVE_VALIDATE", "LEAVE_CANCEL",
            "ABSENCE_READ", "ABSENCE_CREATE", "ABSENCE_UPDATE", "ABSENCE_VALIDATE",
            "REPORT_READ", "REPORT_EXPORT",
            "PARAMETER_READ", "PARAMETER_UPDATE",
            "USER_READ", "USER_CREATE", "USER_UPDATE", "USER_DISABLE",
            "ROLE_READ", "ROLE_CREATE", "ROLE_UPDATE", "ROLE_DELETE", "ROLE_ASSIGN",
            "PERMISSION_READ",
            "POINTAGE_READ", "POINTAGE_UPDATE",
            "SANCTION_READ", "SANCTION_CREATE", "SANCTION_UPDATE",
            "FORMATION_READ", "FORMATION_CREATE", "FORMATION_UPDATE",
            "CARRIERE_READ", "CARRIERE_UPDATE",
            "DASHBOARD_READ",
            "HS_READ", "HS_CREATE", "HS_UPDATE", "HS_SUBMIT", "HS_VALIDATE", "HS_REJECT", "HS_INTEGRATE_PAIE"
    );

    private static final Map<String, String> PERMISSION_DESCRIPTIONS = new HashMap<>();
    static {
        PERMISSION_DESCRIPTIONS.put("EMPLOYEE_READ", "Consulter les employés");
        PERMISSION_DESCRIPTIONS.put("EMPLOYEE_CREATE", "Créer un employé");
        PERMISSION_DESCRIPTIONS.put("EMPLOYEE_UPDATE", "Modifier un employé");
        PERMISSION_DESCRIPTIONS.put("EMPLOYEE_DELETE", "Supprimer un employé");
        PERMISSION_DESCRIPTIONS.put("EMPLOYEE_EXPORT", "Exporter les employés");
        PERMISSION_DESCRIPTIONS.put("CONTRACT_READ", "Consulter les contrats");
        PERMISSION_DESCRIPTIONS.put("CONTRACT_CREATE", "Créer un contrat");
        PERMISSION_DESCRIPTIONS.put("CONTRACT_UPDATE", "Modifier un contrat");
        PERMISSION_DESCRIPTIONS.put("CONTRACT_CLOSE", "Clôturer un contrat");
        PERMISSION_DESCRIPTIONS.put("CONTRACT_EXPORT", "Exporter les contrats");
        PERMISSION_DESCRIPTIONS.put("PAYROLL_READ", "Consulter la paie");
        PERMISSION_DESCRIPTIONS.put("PAYROLL_CALCULATE", "Calculer la paie");
        PERMISSION_DESCRIPTIONS.put("PAYROLL_VALIDATE", "Valider la paie");
        PERMISSION_DESCRIPTIONS.put("PAYROLL_CANCEL", "Annuler la paie");
        PERMISSION_DESCRIPTIONS.put("PAYROLL_EXPORT", "Exporter la paie");
        PERMISSION_DESCRIPTIONS.put("LEAVE_READ", "Consulter les congés");
        PERMISSION_DESCRIPTIONS.put("LEAVE_CREATE", "Créer une demande de congé");
        PERMISSION_DESCRIPTIONS.put("LEAVE_UPDATE", "Modifier un congé");
        PERMISSION_DESCRIPTIONS.put("LEAVE_VALIDATE", "Valider un congé");
        PERMISSION_DESCRIPTIONS.put("LEAVE_CANCEL", "Annuler un congé");
        PERMISSION_DESCRIPTIONS.put("ABSENCE_READ", "Consulter les absences");
        PERMISSION_DESCRIPTIONS.put("ABSENCE_CREATE", "Créer une absence");
        PERMISSION_DESCRIPTIONS.put("ABSENCE_UPDATE", "Modifier une absence");
        PERMISSION_DESCRIPTIONS.put("ABSENCE_VALIDATE", "Valider une absence");
        PERMISSION_DESCRIPTIONS.put("REPORT_READ", "Consulter les rapports");
        PERMISSION_DESCRIPTIONS.put("REPORT_EXPORT", "Exporter les rapports");
        PERMISSION_DESCRIPTIONS.put("PARAMETER_READ", "Consulter les paramétrages");
        PERMISSION_DESCRIPTIONS.put("PARAMETER_UPDATE", "Modifier les paramétrages");
        PERMISSION_DESCRIPTIONS.put("USER_READ", "Consulter les utilisateurs");
        PERMISSION_DESCRIPTIONS.put("USER_CREATE", "Créer un utilisateur");
        PERMISSION_DESCRIPTIONS.put("USER_UPDATE", "Modifier un utilisateur");
        PERMISSION_DESCRIPTIONS.put("USER_DISABLE", "Désactiver un utilisateur");
        PERMISSION_DESCRIPTIONS.put("ROLE_READ", "Consulter les rôles");
        PERMISSION_DESCRIPTIONS.put("ROLE_CREATE", "Créer un rôle");
        PERMISSION_DESCRIPTIONS.put("ROLE_UPDATE", "Modifier un rôle");
        PERMISSION_DESCRIPTIONS.put("ROLE_DELETE", "Supprimer un rôle");
        PERMISSION_DESCRIPTIONS.put("ROLE_ASSIGN", "Assigner un rôle");
        PERMISSION_DESCRIPTIONS.put("PERMISSION_READ", "Consulter les permissions");
        PERMISSION_DESCRIPTIONS.put("POINTAGE_READ", "Consulter le pointage");
        PERMISSION_DESCRIPTIONS.put("POINTAGE_UPDATE", "Modifier le pointage");
        PERMISSION_DESCRIPTIONS.put("SANCTION_READ", "Consulter les sanctions");
        PERMISSION_DESCRIPTIONS.put("SANCTION_CREATE", "Créer une sanction");
        PERMISSION_DESCRIPTIONS.put("SANCTION_UPDATE", "Modifier une sanction");
        PERMISSION_DESCRIPTIONS.put("FORMATION_READ", "Consulter les formations");
        PERMISSION_DESCRIPTIONS.put("FORMATION_CREATE", "Créer une formation");
        PERMISSION_DESCRIPTIONS.put("FORMATION_UPDATE", "Modifier une formation");
        PERMISSION_DESCRIPTIONS.put("CARRIERE_READ", "Consulter la carrière");
        PERMISSION_DESCRIPTIONS.put("CARRIERE_UPDATE", "Modifier la carrière");
        PERMISSION_DESCRIPTIONS.put("DASHBOARD_READ", "Consulter le tableau de bord");
        PERMISSION_DESCRIPTIONS.put("HS_READ", "Consulter les heures supplémentaires");
        PERMISSION_DESCRIPTIONS.put("HS_CREATE", "Créer une heure supplémentaire");
        PERMISSION_DESCRIPTIONS.put("HS_UPDATE", "Modifier une heure supplémentaire");
        PERMISSION_DESCRIPTIONS.put("HS_SUBMIT", "Soumettre une heure supplémentaire");
        PERMISSION_DESCRIPTIONS.put("HS_VALIDATE", "Valider une heure supplémentaire");
        PERMISSION_DESCRIPTIONS.put("HS_REJECT", "Rejeter une heure supplémentaire");
        PERMISSION_DESCRIPTIONS.put("HS_INTEGRATE_PAIE", "Intégrer une heure supplémentaire à la paie");
    }

    private static final Map<RoleName, List<String>> ROLE_PERMISSION_MATRIX = new EnumMap<>(RoleName.class);
    static {
        ROLE_PERMISSION_MATRIX.put(RoleName.ADMIN, Arrays.asList(
            "EMPLOYEE_READ", "EMPLOYEE_CREATE", "EMPLOYEE_UPDATE", "EMPLOYEE_DELETE", "EMPLOYEE_EXPORT",
            "CONTRACT_READ", "CONTRACT_CREATE", "CONTRACT_UPDATE", "CONTRACT_CLOSE", "CONTRACT_EXPORT",
            "PAYROLL_READ", "PAYROLL_CALCULATE", "PAYROLL_VALIDATE", "PAYROLL_CANCEL", "PAYROLL_EXPORT",
            "LEAVE_READ", "LEAVE_CREATE", "LEAVE_UPDATE", "LEAVE_VALIDATE", "LEAVE_CANCEL",
            "ABSENCE_READ", "ABSENCE_CREATE", "ABSENCE_UPDATE", "ABSENCE_VALIDATE",
            "REPORT_READ", "REPORT_EXPORT",
            "PARAMETER_READ", "PARAMETER_UPDATE",
            "USER_READ", "USER_CREATE", "USER_UPDATE", "USER_DISABLE",
            "ROLE_READ", "ROLE_CREATE", "ROLE_UPDATE", "ROLE_DELETE", "ROLE_ASSIGN",
            "PERMISSION_READ",
            "POINTAGE_READ", "POINTAGE_UPDATE",
            "SANCTION_READ", "SANCTION_CREATE", "SANCTION_UPDATE",
            "FORMATION_READ", "FORMATION_CREATE", "FORMATION_UPDATE",
            "CARRIERE_READ", "CARRIERE_UPDATE",
            "DASHBOARD_READ",
            "HS_READ", "HS_CREATE", "HS_UPDATE", "HS_SUBMIT", "HS_VALIDATE", "HS_REJECT", "HS_INTEGRATE_PAIE"
        ));

        ROLE_PERMISSION_MATRIX.put(RoleName.RH, Arrays.asList(
            "EMPLOYEE_READ", "EMPLOYEE_CREATE", "EMPLOYEE_UPDATE", "EMPLOYEE_DELETE", "EMPLOYEE_EXPORT",
            "CONTRACT_READ", "CONTRACT_CREATE", "CONTRACT_UPDATE", "CONTRACT_CLOSE",
            "PAYROLL_READ", "PAYROLL_EXPORT",
            "LEAVE_READ", "LEAVE_CREATE", "LEAVE_UPDATE", "LEAVE_VALIDATE",
            "ABSENCE_READ", "ABSENCE_CREATE", "ABSENCE_UPDATE", "ABSENCE_VALIDATE",
            "REPORT_READ", "REPORT_EXPORT",
            "PARAMETER_READ",
            "POINTAGE_READ",
            "SANCTION_READ", "SANCTION_CREATE", "SANCTION_UPDATE",
            "FORMATION_READ", "FORMATION_CREATE", "FORMATION_UPDATE",
            "CARRIERE_READ", "CARRIERE_UPDATE",
            "DASHBOARD_READ",
            "HS_READ", "HS_CREATE", "HS_UPDATE", "HS_SUBMIT"
        ));

        ROLE_PERMISSION_MATRIX.put(RoleName.DAF, Arrays.asList(
            "EMPLOYEE_READ", "EMPLOYEE_EXPORT",
            "CONTRACT_READ", "CONTRACT_EXPORT",
            "PAYROLL_READ", "PAYROLL_CALCULATE", "PAYROLL_VALIDATE", "PAYROLL_CANCEL", "PAYROLL_EXPORT",
            "LEAVE_READ",
            "ABSENCE_READ",
            "REPORT_READ", "REPORT_EXPORT",
            "PARAMETER_READ",
            "DASHBOARD_READ",
            "HS_READ", "HS_VALIDATE", "HS_REJECT", "HS_INTEGRATE_PAIE"
        ));

        ROLE_PERMISSION_MATRIX.put(RoleName.PTGE, Arrays.asList(
            "EMPLOYEE_READ",
            "POINTAGE_READ", "POINTAGE_UPDATE",
            "PARAMETER_READ",
            "DASHBOARD_READ"
        ));

        ROLE_PERMISSION_MATRIX.put(RoleName.USER, Arrays.asList(
            "EMPLOYEE_READ",
            "LEAVE_READ", "LEAVE_CREATE",
            "DASHBOARD_READ"
        ));
    }

    @Override
    public void run(String... args) throws Exception {
        logger.info(">>> Initialisation des permissions RHPAIE...");

        Map<String, Permission> permissionMap = new HashMap<>();

        for (String code : ALL_PERMISSIONS) {
            Permission perm = permissionRepository.findByCode(code).orElse(null);
            if (perm == null) {
                perm = new Permission(code, PERMISSION_DESCRIPTIONS.getOrDefault(code, code));
                perm = permissionRepository.save(perm);
                logger.info("  Permission créée: {}", code);
            }
            permissionMap.put(code, perm);
        }

        for (Map.Entry<RoleName, List<String>> entry : ROLE_PERMISSION_MATRIX.entrySet()) {
            RoleName roleName = entry.getKey();
            Role role = roleRepository.findByName(roleName).orElse(null);
            if (role == null) {
                logger.warn("  Rôle non trouvé en DB: {} — création...", roleName);
                role = new Role(roleName);
                role = roleRepository.save(role);
            }

            Set<Permission> perms = new HashSet<>();
            for (String code : entry.getValue()) {
                Permission p = permissionMap.get(code);
                if (p != null) {
                    perms.add(p);
                }
            }

            if (!perms.equals(role.getPermissions())) {
                role.setPermissions(perms);
                roleRepository.save(role);
                logger.info("  Rôle {} : {} permissions associées", roleName, perms.size());
            }
        }

        logger.info(">>> Initialisation des permissions terminée ({} permissions, {} rôles)",
                permissionRepository.count(), roleRepository.count());
    }
}
