package com.spring.domain;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import java.util.List;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.data.repository.query.Param;

public interface BuyRepository extends JpaRepository<BuyList, Long>{

}
