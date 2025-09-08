package com.dh.goyeon.auth.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.dh.goyeon.auth.CustomUserDetails;
import com.dh.goyeon.user.UserBean;
import com.dh.goyeon.user.impl.UserDAOMybatis;

@Service
public class CustomUserDetailsService implements UserDetailsService {

	@Autowired
	private UserDAOMybatis userDAO;
	
    
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
				
				//DB에서 조회
    	UserBean userData = userDAO.getUserDetails(username);
    	System.out.println("userData = " + userData); 
    	System.out.println("DB 비밀번호 = " + userData.getUpw());

        if (userData != null) {
						
						//UserDetails에 담아서 return하면 AutneticationManager가 검증 함
            return new CustomUserDetails(userData);
        }

        return null;
    }
    
    /*
	 * private final UserRepository userRepository;
	 * 
	 * public CustomUserDetailsService(UserRepository userRepository) {
	 * 
	 * this.userRepository = userRepository; }
	 * 
	 * @Override public UserDetails loadUserByUsername(String username) throws
	 * UsernameNotFoundException {
	 * 
	 * //DB에서 조회 UserEntity userData = userRepository.findByUsername(username);
	 * 
	 * if (userData != null) {
	 * 
	 * //UserDetails에 담아서 return하면 AutneticationManager가 검증 함 return new
	 * CustomUserDetails(userData); }
	 * 
	 * return null; }
	 */
}