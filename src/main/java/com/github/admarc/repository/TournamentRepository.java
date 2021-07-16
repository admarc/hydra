package com.github.admarc.repository;

import com.github.admarc.domain.Tournament;
import org.springframework.data.repository.CrudRepository;

public interface TournamentRepository extends CrudRepository<Tournament, Long> {
}
