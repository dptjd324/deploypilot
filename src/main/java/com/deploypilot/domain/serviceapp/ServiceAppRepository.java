package com.deploypilot.domain.serviceapp;

import org.springframework.data.jpa.repository.JpaRepository;

public interface ServiceAppRepository extends JpaRepository<ServiceApp, Long> {

	boolean existsByName(String name);

	boolean existsByNameAndIdNot(String name, Long id);
}
