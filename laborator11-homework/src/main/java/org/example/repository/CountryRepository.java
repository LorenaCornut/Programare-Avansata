package org.example.repository;

import org.example.entity.Country;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import java.util.List;

public interface CountryRepository extends JpaRepository<Country, Long> {
}