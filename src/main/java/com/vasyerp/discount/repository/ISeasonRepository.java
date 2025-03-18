package com.vasyerp.discount.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.vasyerp.discount.model.Season;

@Repository
public interface ISeasonRepository extends JpaRepository<Season, Long> {

}
