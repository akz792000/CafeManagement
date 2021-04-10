package org.cafe.management.service.impl;

import lombok.RequiredArgsConstructor;
import org.cafe.management.core.UserDetailsImpl;
import org.cafe.management.domain.UserEntity;
import org.cafe.management.repository.UserRepository;
import org.cafe.management.service.UserService;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Arrays;

@RequiredArgsConstructor
@Service
public class UserServiceImpl implements UserService {

    private final UserRepository repository;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        UserEntity userEntity = repository.findByUsername(email);
        if (userEntity == null) {
            throw new UsernameNotFoundException("Invalid username or password.");
        }
        return new UserDetailsImpl(userEntity.getId(), userEntity.getUsername(), userEntity.getPassword(),
                Arrays.asList(new SimpleGrantedAuthority(userEntity.getRoleType().name())));
    }


}
