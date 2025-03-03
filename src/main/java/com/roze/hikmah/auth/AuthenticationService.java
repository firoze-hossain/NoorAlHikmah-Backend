package com.roze.hikmah.auth;

import com.roze.hikmah.config.JwtService;
import jakarta.mail.MessagingException;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.security.SecureRandom;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AuthenticationService {
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;
    private final TokenRepository tokenRepository;
    private final EmailService emailService;
    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;
    @Value("${application.mailing.frontend.activation-url}")
    private String activationUrl;

//    public String register(RegistrationRequest request, HttpServletRequest servletRequest) throws MessagingException {
//        Optional<User> existingUser = userRepository.findByEmail(request.getEmail());
//
//        if (existingUser.isPresent()) {
//            User user = existingUser.get();
//            if (!user.isEnabled()) { // Email already sent, but not activated
//                return "Email already sent. Please check your inbox.";
//            } else {
//                throw new IllegalStateException("User already registered and activated.");
//            }
//        }
//
//        var userRole = roleRepository.findByName("USER")
//                .orElseThrow(() -> new IllegalStateException("ROLE USER was not initiated"));
//        var user = User.builder()
//                .firstName(request.getFirstName())
//                .lastName(request.getLastName())
//                .ipAddress(servletRequest.getRemoteAddr())
//                .email(request.getEmail())
//                .password(passwordEncoder.encode(request.getPassword()))
//                .accountLocked(false)
//                .enabled(false)
//                .roles(List.of(userRole))
//                .build();
//
//        userRepository.save(user);
//        sendValidationEmail(user);
//        return "Registration successful. Please check your email for activation.";
//    }
public RegistrationResponse register(RegistrationRequest request, HttpServletRequest servletRequest) throws MessagingException {
    Optional<User> existingUser = userRepository.findByEmail(request.getEmail());

    if (existingUser.isPresent()) {
        User user = existingUser.get();
        if (!user.isEnabled()) {
            return new RegistrationResponse(user.getEmail(), "Email already sent to " + user.getEmail() + ". Please check your inbox.");
        } else {
            return new RegistrationResponse(user.getEmail(), "User already registered. Please login.");
        }
    }

    var userRole = roleRepository.findByName("USER")
            .orElseThrow(() -> new IllegalStateException("ROLE USER was not initiated"));
    var user = User.builder()
            .firstName(request.getFirstName())
            .lastName(request.getLastName())
            .ipAddress(servletRequest.getRemoteAddr())
            .email(request.getEmail())
            .password(passwordEncoder.encode(request.getPassword()))
            .accountLocked(false)
            .enabled(false)
            .roles(List.of(userRole))
            .build();
    userRepository.save(user);
    sendValidationEmail(user);

    return new RegistrationResponse(user.getEmail(), "Registration successful. Please check your email.");
}


    private void sendValidationEmail(User user) throws MessagingException {
        var newToken = generateAndSaveActivationToken(user);

        emailService.sendMail(
                user.getEmail(),
                user.fullName(),
                EmailTemplateName.ACTIVATE_ACCOUNT,
                activationUrl,
                newToken,
                "Account activation"
        );
    }

    private String generateAndSaveActivationToken(User user) {
        String generatedToken = generateActivationCode(6);
        Token token = Token.builder()
                .token(generatedToken)
                .createdAt(LocalDateTime.now())
                .expiresAt(LocalDateTime.now().plusMinutes(15))
                .user(user)
                .build();
        tokenRepository.save(token);
        return generatedToken;
    }

    private String generateActivationCode(int length) {
        String characters = "0123456789";
        StringBuilder codeBuilder = new StringBuilder();
        SecureRandom secureRandom = new SecureRandom();
        for (int i = 0; i < length; i++) {
            int randomIndex = secureRandom.nextInt(characters.length());
            codeBuilder.append(characters.charAt(randomIndex));
        }
        return codeBuilder.toString();
    }

    public AuthenticationResponse authenticate(AuthenticationRequest authenticationRequest) {
        var auth = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        authenticationRequest.getEmail(),
                        authenticationRequest.getPassword()
                )
        );
        Map claims = new HashMap<String, Object>();
        User user = (User) auth.getPrincipal();
        claims.put("fullName", user.fullName());
        String token = jwtService.generateToken(claims, user);

        return AuthenticationResponse.builder()
                .token(token)
                .build();

    }

    @Transactional
    public void activateAccount(String token) throws MessagingException {
        Token savedToken = tokenRepository.findByToken(token).orElseThrow(() -> new RuntimeException("Invalid Token"));
        if (LocalDateTime.now().isAfter(savedToken.getExpiresAt())) {
            sendValidationEmail(savedToken.getUser());
            throw new RuntimeException("Activation token has expired. A new token has been sent");
        }
        User user = userRepository.findById(savedToken.getUser().getId()).orElseThrow(() -> new RuntimeException("User not found"));
        user.setEnabled(true);
        userRepository.save(user);
        savedToken.setValidatedAt(LocalDateTime.now());
        tokenRepository.save(savedToken);
    }
}