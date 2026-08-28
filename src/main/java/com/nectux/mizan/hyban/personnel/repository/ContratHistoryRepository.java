package com.nectux.mizan.hyban.personnel.repository;

import com.nectux.mizan.hyban.personnel.entity.ContratHistory;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface ContratHistoryRepository extends CrudRepository<ContratHistory, Long> {

    List<ContratHistory> findByContratIdOrderByDateOperationDesc(Long contratId);

    long countByContratId(Long contratId);
}
