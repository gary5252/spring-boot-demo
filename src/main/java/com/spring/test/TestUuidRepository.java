package com.spring.test;

import org.springframework.data.jpa.repository.JpaRepository;

public interface TestUuidRepository extends JpaRepository<TestUuid, String> {

}
