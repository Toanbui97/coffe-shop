package com.ioidigital.shop.auth.service;

import com.ioidigital.shop.auth.controller.model.UserCreateReq;
import com.ioidigital.shop.auth.persistence.User;
import com.ioidigital.shop.constant.Role;
import com.ioidigital.shop.constant.TokenType;
import com.ioidigital.shop.exception.AppErrorCodeMsg;
import com.ioidigital.shop.exception.BaseRuntimeException;
import com.ioidigital.shop.auth.controller.model.AuthenticationReq;
import com.ioidigital.shop.auth.controller.model.AuthenticationResp;
import com.ioidigital.shop.auth.persistence.UserRepository;
import com.ioidigital.shop.util.JwtUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class AuthenticationServiceImpl implements AuthenticationService {

    private final UserRepository userRepository;
    private final JwtUtil jwtUtil;
    private final PasswordEncoder passwordEncoder;

    @Override
    @Transactional
    public AuthenticationResp authenticateUser(AuthenticationReq authenticationReq) {
        log.info("authenticate() - authenticationReq = {}", authenticationReq);

        final var sessionId = UUID.randomUUID().toString();
        final var username = authenticationReq.getUsername();

        var user = userRepository.findByUsername(username)
                .orElseThrow(() -> {
                    log.error("authenticateUser() - User not found. username = {}", username);
                    return new BaseRuntimeException(AppErrorCodeMsg.AUTH_40001);
                });

        if (!passwordEncoder.matches(authenticationReq.getPassword(), user.getPassword())) {
            throw new BaseRuntimeException(AppErrorCodeMsg.AUTH_40001);
        }

        var accessToken = jwtUtil.generateToken(sessionId, TokenType.ACCESS, user);


        var authenticationRespBuilder = AuthenticationResp.builder()
                .accessToken(accessToken)
                .userId(user.getId())
                .fullName(user.getFullName())
                .username(user.getUsername())
                .role(user.getRole());

        return authenticationRespBuilder.build();
    }

    @Override
    @Transactional
    public Long createUser(UserCreateReq userCreateReq, Role role) {

        if (userRepository.findByUsername(userCreateReq.getUsername()).isPresent()) {
            log.error("registerUser() - Username already exists. username = {}", userCreateReq.getUsername());
            throw new BaseRuntimeException(AppErrorCodeMsg.AUTH_40005);
        }

        if (userRepository.findByPhoneNumber(userCreateReq.getPhoneNumber()).isPresent()) {
            log.error("registerUser - phone number already exists. phoneNumber = {}", userCreateReq.getPhoneNumber());
            throw new BaseRuntimeException(AppErrorCodeMsg.AUTH_40005);
        }

        var user = User.builder()
                .address(userCreateReq.getAddress())
                .username(userCreateReq.getUsername())
                .password(passwordEncoder.encode(userCreateReq.getPassword()))
                .role(role)
                .fullName(userCreateReq.getFullName())
                .phoneNumber(userCreateReq.getPhoneNumber())
                .build();

        userRepository.save(user);

        return user.getId();
    }



    public static void main(String[] args) {
        BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();

        String password = bCryptPasswordEncoder.encode("123@123a");
        System.out.println(password);
    }
}
