package com.github.admarc.steps;

import com.github.admarc.repository.TournamentRepository;
import cucumber.api.java.After;

import java.util.*;

public class DatabaseStepDefinitions {
    private TournamentRepository tournamentRepository;

    DatabaseStepDefinitions(TournamentRepository roleRepository) {
        this.tournamentRepository = tournamentRepository;
    }

    @After
    public void clearDatabase() {
        tournamentRepository.deleteAll();
    }
}