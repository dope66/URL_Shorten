package com.project.UrlJrr.repository;

import com.project.UrlJrr.entity.ManagementItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ManagementItemRepository extends JpaRepository<ManagementItem,Long> {


}
