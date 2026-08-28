package com.nectux.mizan.hyban.paie.repository;

import com.nectux.mizan.hyban.paie.entity.RegleHeureSupplementaire;
import com.nectux.mizan.hyban.paie.enums.TypeHS;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface RegleHeureSupplementaireRepository extends JpaRepository<RegleHeureSupplementaire, Long> {

    Optional<RegleHeureSupplementaire> findByTypeHS(TypeHS typeHS);

    Optional<RegleHeureSupplementaire> findByCode(String code);

    List<RegleHeureSupplementaire> findByActiveTrue();
}
