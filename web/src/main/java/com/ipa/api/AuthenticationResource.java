package com.ipa.api;


import com.ipa.model.Credentials;
import com.ipa.dto.UserDto;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpSession;

@RestController()
@RequestMapping("/api/session")
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class AuthenticationResource {
    private final @NonNull AuthenticationManager authenticationManager;

    @RequestMapping(method = RequestMethod.POST)
    public UserDto login(@RequestBody Credentials credentials, HttpSession httpSession) {
        Authentication authentication = new UsernamePasswordAuthenticationToken(credentials.getUsername(), credentials.getPassword());
        SecurityContextHolder.getContext().setAuthentication(authenticationManager.authenticate(authentication));

        return (UserDto) httpSession.getAttribute("user");
    }

    @RequestMapping(method = RequestMethod.GET)
    public UserDto session(HttpSession session) {
        return (UserDto) session.getAttribute("user");
    }

    @RequestMapping(method = RequestMethod.DELETE)
    public void logout(HttpSession session) {
        session.invalidate();
    }
}
