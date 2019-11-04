package com.github.admarc.controller;

import com.github.admarc.security.AppUserDetailsService;
import com.github.admarc.domain.User;
import com.github.admarc.interactor.user.CreateUser;
import com.github.admarc.interactor.user.GetUser;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import javax.validation.Valid;
import java.security.Principal;
import java.util.Optional;

@RestController
@RequestMapping("/users")
public class UserController {
    private CreateUser createUserInteractor;
    private GetUser getUserInteractor;

    UserController(CreateUser createUserInteractor, GetUser getUserInteractor) {
        this.createUserInteractor = createUserInteractor;
        this.getUserInteractor = getUserInteractor;
    }

    @PreAuthorize("hasAuthority('STANDARD_USER')")
    @RequestMapping(value ="/{id}", method = RequestMethod.GET)
    public User list(
            @PathVariable Long id,
            Principal principal,
            Authentication authentication,
            @AuthenticationPrincipal AppUserDetailsService customUser
    ){
        Optional<User> user = getUserInteractor.execute(id);

        if (!user.isPresent()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }

        if(!user.get().getUsername().equals(principal.getName())) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN);
        }

        return user.get();
    }

    @RequestMapping(value ="", method = RequestMethod.POST)
    public User create(@Valid @RequestBody User user){
        return createUserInteractor.execute(user);
    }
}
