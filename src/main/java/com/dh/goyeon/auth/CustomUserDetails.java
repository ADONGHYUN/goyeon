package com.dh.goyeon.auth;

import java.util.ArrayList;
import java.util.Collection;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.dh.goyeon.user.UserBean;



public class CustomUserDetails implements UserDetails {

    private final UserBean userbean;

    public CustomUserDetails(UserBean userbean) {

        this.userbean = userbean;
    }
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {

        Collection<GrantedAuthority> collection = new ArrayList<>();

        collection.add(new GrantedAuthority() {

            @Override
            public String getAuthority() {
            	return "임시 ROLE";
                //return userbean.getRole();
            }
        });

        return collection;
    }

    @Override
    public String getPassword() {
    	System.out.println("userbean.getUpw();"+ userbean.getUpw());
        return userbean.getUpw();
    }

    @Override
    public String getUsername() {
        return userbean.getUid();
    }
    
    public String getUgender() {
    	return userbean.getUgender();
    }

    @Override
    public boolean isAccountNonExpired() {

        return true;
    }

    @Override
    public boolean isAccountNonLocked() {

        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {

        return true;
    }

    @Override
    public boolean isEnabled() {

        return true;
    }
}