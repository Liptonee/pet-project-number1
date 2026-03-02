package taskManager.web.security;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import taskManager.database.repositories.UserRepository;


@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService{

    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException{
        return userRepository.findByEmail(username).map(CustomUsersDetails::new)
                                                .orElseThrow(() -> new UsernameNotFoundException(username));
    }
}
