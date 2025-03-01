package hu._ig.crm.crm4ig.service.impl;

import hu._ig.crm.crm4ig.constants.Constants;
import hu._ig.crm.crm4ig.domain.UserEntity;
import hu._ig.crm.crm4ig.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import static hu._ig.crm.crm4ig.constants.Constants.EMPTY_STRING;
import static hu._ig.crm.crm4ig.constants.Constants.USER_NOT_FOUND_FOR_EMAIL;

@Slf4j
@RequiredArgsConstructor
@Service
public class UserDetailsServiceImpl  implements UserDetailsService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        UserEntity userEntity = userRepository.findByEmail(username)
                .orElseThrow(() -> new UsernameNotFoundException(USER_NOT_FOUND_FOR_EMAIL + username));
        return new User(userEntity.getEmail(), userEntity.getPassword(),
                userEntity.getRoles().stream()
                        .map(role -> new SimpleGrantedAuthority(role.getRoleName()))
                        .toList());
    }

    public void saveUser(UserEntity user) {
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        userRepository.save(user);
    }
}
