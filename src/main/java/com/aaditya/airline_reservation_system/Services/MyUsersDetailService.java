package com.aaditya.airline_reservation_system.Services;


import com.aaditya.airline_reservation_system.Entity.User;
import com.aaditya.airline_reservation_system.Repository.UserRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class MyUsersDetailService  implements UserDetailsService {
    private UserRepository userRepository;
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findByUsername(username);
        if(user == null) throw new UsernameNotFoundException("User Not Found 404.");
        return new UserPrincipal(user);
    }
}
