package com.github.admarc.steps;

import com.github.admarc.domain.Role;
import com.github.admarc.domain.User;
import com.github.admarc.repository.RoleRepository;
import com.github.admarc.repository.UserRepository;
import cucumber.api.java.After;
import cucumber.api.java.en.Given;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.*;

public class DatabaseStepDefinitions {
    private UserRepository userRepository;
    private RoleRepository roleRepository;
    private PasswordEncoder passwordEncoder;

    public HashMap<String, Role> roles = new HashMap<String, Role>();
    public HashMap<String, User> users = new HashMap<String, User>();

    DatabaseStepDefinitions(
            PasswordEncoder passwordEncoder,
            RoleRepository roleRepository,
            UserRepository userRepository
            ) {
        this.passwordEncoder = passwordEncoder;
        this.roleRepository = roleRepository;
        this.userRepository = userRepository;
    }

    @Given("roles exist:")
    public void roles_exist(io.cucumber.datatable.DataTable dataTable) {
        List<Map<String, String>> list = dataTable.asMaps(String.class, String.class);

        for(int i=0; i<list.size(); i++) {
            String name = list.get(i).get("name");
            Role role = new Role();
            role.setName(name);
            roleRepository.save(role);
            roles.put(name, role);
        }
    }


    @Given("users exist:")
    public void users_exist(io.cucumber.datatable.DataTable dataTable) {
        List<Map<String, String>> list = dataTable.asMaps(String.class, String.class);

        for(int i=0; i<list.size(); i++) {
            String username  = list.get(i).get("username");
            User user = new User(
                    list.get(i).get("email"),
                    username,
                    passwordEncoder.encode(list.get(i).get("password"))
            );

            String roleName = list.get(i).get("role");

            if(roleName != null && !roleName.isEmpty()) {
                user.setRoles(Arrays.asList(roles.get(roleName)));
            }

            userRepository.save(user);
            users.put(username, user);
        }
    }

    @After
    public void clearDatabase() {
        userRepository.deleteAll();
        roleRepository.deleteAll();
    }
}