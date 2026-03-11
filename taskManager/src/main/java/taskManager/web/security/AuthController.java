package taskManager.web.security;

import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import taskManager.web.dto.request.RefreshTokenDto;
import taskManager.web.dto.request.UserCredentialDto;
import taskManager.web.dto.response.JwtAuthenticationDto;
import taskManager.web.security.jwt.JwtService;

@Controller
@RequestMapping("/auth")
@RequiredArgsConstructor
@Slf4j
public class AuthController {


    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;


    @PostMapping("/login")
    public ResponseEntity<JwtAuthenticationDto> login(@RequestBody UserCredentialDto userCredential
    ){
        log.info("From CONTROLLER called login");
        UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
                                                                userCredential.getEmail(), userCredential.getPassword());
        Authentication authentication = authenticationManager.authenticate(authToken);
        String email = ((UserDetails) authentication.getPrincipal()).getUsername(); 
        JwtAuthenticationDto jwtDto = jwtService.generateAuthToken(email);

        return ResponseEntity.ok(jwtDto);
    }


    @PostMapping("/refresh")
    public ResponseEntity<JwtAuthenticationDto> refresh(@RequestBody RefreshTokenDto refreshTokenDto
    ){
        log.info("From CONTROLLER called refresh");

        String refreshToken = refreshTokenDto.getRefreshToken();

        if (!jwtService.validateJwtToken(refreshToken)) {
            throw new IllegalArgumentException("Invalid refresh token");
        }

        String email = jwtService.getEmailFromToken(refreshToken);

        JwtAuthenticationDto jwtDto = jwtService.refreshBaseToken(email, refreshToken);

        return ResponseEntity.ok(jwtDto);

    }

}
