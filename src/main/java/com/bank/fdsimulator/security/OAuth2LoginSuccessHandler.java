package com.bank.fdsimulator.security;

import com.bank.fdsimulator.entity.Role;
import com.bank.fdsimulator.entity.User;
import com.bank.fdsimulator.service.UserService;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Map;

@Component
public class OAuth2LoginSuccessHandler extends SimpleUrlAuthenticationSuccessHandler {
    
    @Autowired
    private UserService userService;
    
    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
                                      Authentication authentication) throws IOException, ServletException {
        
        try {
            OAuth2User oAuth2User = (OAuth2User) authentication.getPrincipal();
            Map<String, Object> attributes = oAuth2User.getAttributes();
            
            String email = (String) attributes.get("email");
            String picture = (String) attributes.get("picture");
            String googleId = (String) attributes.get("sub");
            
            if (email == null || googleId == null) {
                response.sendRedirect("/login?error=oauth_failed");
                return;
            }
            
            User user = userService.findByGoogleId(googleId).orElse(null);
            
            if (user == null) {
                // Check if user exists with this email
                user = userService.findByEmail(email).orElse(null);
                
                if (user == null) {
                    // Create new user
                    user = new User();
                    user.setUsername(email.split("@")[0] + "_" + System.currentTimeMillis());
                    user.setEmail(email);
                    user.setGoogleId(googleId);
                    user.setProfilePicture(picture);
                    user.setRole(Role.CUSTOMER);
                    // For OAuth users, set a secure random password
                    user.setPassword("OAUTH_USER_" + java.util.UUID.randomUUID().toString());
                    user = userService.createUser(user, new org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder());
                } else {
                    // Update existing user with Google ID
                    user.setGoogleId(googleId);
                    if (picture != null) {
                        user.setProfilePicture(picture);
                    }
                    user = userService.updateUser(user);
                }
            }
            
            // Redirect based on user role
            String contextPath = request.getContextPath();
            if (user.getRole() == Role.ADMIN) {
                getRedirectStrategy().sendRedirect(request, response, contextPath + "/admin/dashboard");
            } else {
                getRedirectStrategy().sendRedirect(request, response, contextPath + "/customer/dashboard");
            }
        } catch (Exception e) {
            e.printStackTrace();
            response.sendRedirect("/login?error=oauth_failed");
        }
    }
}
