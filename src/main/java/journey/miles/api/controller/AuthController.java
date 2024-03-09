package journey.miles.api.controller;

import jakarta.validation.Valid;
import journey.miles.api.dto.UserDTOData;
import journey.miles.api.dto.JWTTokenDTOData;
import journey.miles.api.infra.security.TokenService;
import journey.miles.api.model.user.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("login")
public class AuthController {

    @Autowired
    private AuthenticationManager manager;

    @Autowired
    private TokenService tokenService;

    @PostMapping
    public ResponseEntity doLogin(@RequestBody @Valid UserDTOData userDTOData) {
        var authToken = new UsernamePasswordAuthenticationToken(userDTOData.login(), userDTOData.password());
        var auth = manager.authenticate(authToken);
        var jwtToken = tokenService.generateToken((User) auth.getPrincipal());

        return ResponseEntity.ok(new JWTTokenDTOData(jwtToken));
    }
}
