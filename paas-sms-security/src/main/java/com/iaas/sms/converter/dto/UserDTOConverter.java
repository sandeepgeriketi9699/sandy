/*
 * 
 */
package com.iaas.sms.converter.dto;

import com.iaas.sms.dto.UserDTO;
import com.iaas.sms.model.Authority;
import com.iaas.sms.model.User;

import org.springframework.core.convert.converter.Converter;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


// TODO: Auto-generated Javadoc
/**
 * The Class UserDTOConverter.
 */
public class UserDTOConverter implements Converter<UserDTO, User> {

    /**
     * Convert.
     *
     * @param dto the dto
     * @return the user
     */
    @Override
    public User convert(final UserDTO dto) {
        final User user = new User();
        
        user.setUsername(dto.getUsername());
        user.setPassword(dto.getPassword());
        user.setEmail(dto.getEmail());
        user.setPassword(dto.getPassword());
        user.setPasswordConfirm(dto.getPasswordConfirm());
        
        
      if(Authority.ROLE_USER.getAuthority().equals("ROLE_USER")) {
        user.setAuthorities(Arrays.asList(Authority.ROLE_USER));
      }else {
    	  user.setAuthorities(Arrays.asList(Authority.ROLE_ADMIN));
      }
        
        user.setAccountNonExpired(false);
        user.setCredentialsNonExpired(false);
        user.setEnabled(true);
		/*
		 * List<Authority> authorities = new ArrayList<>();
		 * authorities.add(Authority.ROLE_USER); user.setAuthorities(authorities);
		 */
        return user;
    }
}
