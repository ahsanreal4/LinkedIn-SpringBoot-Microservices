package com.linkedin.auth_service.service.impl;

import com.linkedin.auth_service.config.JwtTokenProvider;
import com.linkedin.auth_service.constants.UserRoles;
import com.linkedin.auth_service.dto.LoginUserDto;
import com.linkedin.auth_service.dto.RegisterUserDto;
import com.linkedin.auth_service.dto.UserDto;
import com.linkedin.auth_service.entity.Role;
import com.linkedin.auth_service.entity.User;
import com.linkedin.auth_service.exception.ApiException;
import com.linkedin.auth_service.repository.RoleRepository;
import com.linkedin.auth_service.repository.UserRepository;
import com.linkedin.auth_service.service.AuthService;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Service
public class AuthServiceImpl implements AuthService {

    private final String USER_NAME_ALREADY_EXISTS = "username already exists";
    private final String EMAIL_ALREADY_EXISTS = "email already exists";
    private final String USER_DOES_NOT_EXIST = "user does not exist";
    private final String USER_ROLE_DOES_NOT_EXIST = "user role does not exist";
    private final String INVALID_CREDENTIALS = "Invalid email or password";


    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenProvider jwtTokenProvider;

    public AuthServiceImpl(UserRepository userRepository, RoleRepository roleRepository,
                           PasswordEncoder passwordEncoder,
                           JwtTokenProvider jwtTokenProvider){
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtTokenProvider = jwtTokenProvider;
    }

    @Override
    public UserDto createUser(RegisterUserDto registerUserDto) {
        Optional<User> user = userRepository.findByUsername(registerUserDto.getUsername());

        if (user.isPresent()) throw new ApiException(HttpStatus.BAD_REQUEST, USER_NAME_ALREADY_EXISTS);

        Optional<User> user2 = userRepository.findByEmail(registerUserDto.getEmail());

        if (user2.isPresent()) throw new ApiException(HttpStatus.BAD_REQUEST, EMAIL_ALREADY_EXISTS);

        User userToSave = new User();
        userToSave.setUsername(registerUserDto.getUsername());
        userToSave.setPassword(passwordEncoder.encode(registerUserDto.getPassword()));
        userToSave.setEmail(registerUserDto.getEmail());
        userToSave.setFirstName(registerUserDto.getFirstName());
        userToSave.setLastName(registerUserDto.getLastName());

        Set<Role> roles = new HashSet<>();
        Role userRole = roleRepository.findByName(UserRoles.ROLE_INDEX + UserRoles.USER);

        if (userRole == null) throw new ApiException(HttpStatus.INTERNAL_SERVER_ERROR, USER_ROLE_DOES_NOT_EXIST);

        roles.add(userRole);
        userToSave.setRoles(roles);

        User createdUser = userRepository.save(userToSave);

        UserDto userDto = new UserDto();
        userDto.setId(createdUser.getId());
        userDto.setEmail(createdUser.getEmail());
        userDto.setFirstName(createdUser.getFirstName());
        userDto.setLastName(createdUser.getLastName());
        userDto.setUsername(createdUser.getUsername());

        return userDto;
    }

    @Override
    public String login(LoginUserDto loginUserDto) {
        User user = userRepository.findByEmail(loginUserDto.getEmail())
                .orElseThrow(() -> new ApiException(HttpStatus.NOT_FOUND, USER_DOES_NOT_EXIST));

        if (!passwordEncoder.matches(loginUserDto.getPassword(), user.getPassword())) throw new ApiException(HttpStatus.BAD_REQUEST, INVALID_CREDENTIALS);

//        Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
//                loginUserDto.getEmail(),
//                loginUserDto.getPassword()
//        ));
//        SecurityContextHolder.getContext().setAuthentication(authentication);

        boolean isAdmin = false;

        Set<Role> roles = user.getRoles();
        if (!roles.isEmpty()) {
            List<Role> rolesList = roles.stream().toList();

            for (int i = 0; i < rolesList.size(); i++) {
                Role role = rolesList.get(i);

                if (role.getName().equalsIgnoreCase(UserRoles.ROLE_INDEX + UserRoles.ADMIN)) {
                    isAdmin = true;
                    break;
                }
            }
        }

        return jwtTokenProvider.generateToken(user.getEmail(), user.getId(), isAdmin);
    }
}
