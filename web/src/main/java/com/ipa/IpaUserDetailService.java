package com.ipa;

import com.ipa.database.postgre.dao.PostgreTransactions;
import com.ipa.database.postgre.dao.services.UserService;
import com.ipa.dto.UserDto;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpSession;
import java.util.Set;
import java.util.stream.Collectors;

@Service("userDetailsService")
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class IpaUserDetailService implements UserDetailsService {

    private final @NonNull UserService userService;
    private final @NonNull HttpSession httpSession;
    private final @NonNull ModelMapper modelMapper;

    @Transactional(value = PostgreTransactions.JPA, readOnly = true)
    @Override
    public UserDetails loadUserByUsername(final String username)
            throws UsernameNotFoundException {

        com.ipa.database.postgre.dao.entities.User user = userService.findByUserNameOrEmail(username)
                .orElseThrow(() -> new UsernameNotFoundException(String.format("user with name %s is not found", username)));

        Set<GrantedAuthority> auths = user.getUserRole().stream()
                .map(ur -> new SimpleGrantedAuthority(ur.getRole().name()))
                .collect(Collectors.toSet());

        saveInSession(user);

        return new User(user.getUsername(), user.getPassword(),
                user.isEnabled(), true, true, true, auths);

    }

    private void saveInSession(com.ipa.database.postgre.dao.entities.User user) {
        UserDto userDto = modelMapper.map(user, UserDto.class);
        userDto.setToken(httpSession.getId());
        userDto.setAuthenticated(true);
        httpSession.setAttribute("user", userDto);
    }
}