package com.tool.patientmanangement.service;

import com.tool.patientmanangement.model.DoctorOperation;
import com.tool.patientmanangement.model.Role;
import com.tool.patientmanangement.model.User;
import com.tool.patientmanangement.repository.RoleRepository;
import com.tool.patientmanangement.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.*;

@Service
public class UserService implements UserDetailsService {

    private UserRepository userRepository;
    private RoleRepository roleRepository;
    private BCryptPasswordEncoder bCryptPasswordEncoder; //This will encrypt our users' passwords.

    @Autowired
    public UserService(UserRepository userRepository,
                       BCryptPasswordEncoder bCryptPasswordEncoder,
                       RoleRepository roleRepository) {
        this.userRepository = userRepository;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
        this.roleRepository = roleRepository;
    }

    public User getUserByUsername(String username) {
        return userRepository.findByUsername(username).get();
    }

    public User login(String username, String password) {
        Optional<User> byUsernameAndPassword = userRepository.findByUsernameAndPassword(username, password);
        return byUsernameAndPassword.orElse(null);
    }

    @Transactional
    public String register(User user) {
        Optional<User> userDetailsOptional = userRepository.findByUsername(user.getUsername());
        if (userDetailsOptional.isPresent()) {
            return null;
        }
        user.setPassword(bCryptPasswordEncoder.encode(user.getPassword()));
        List<Role> roleList = new ArrayList<>(user.getRoles());//Here we're converting the set of roles into a list
        Role userRole = roleRepository.findByRole(roleList.get(0).getRole());//From the list we just created, we getting the first role at index 0 and its name. 
        user.setRoles(new HashSet<>(Arrays.asList(userRole)));//Here we're converting back to a Hashset.

        user.setCreateDate(LocalDateTime.now());
        userRepository.save(user);// Here we're saving the complete infos of the user that's just registered.
        return "success";
    }


    @Override
    @Transactional
    public UserDetails loadUserByUsername(String userName) throws UsernameNotFoundException {
        User user = userRepository.findByUsername(userName).get();
        List<GrantedAuthority> authorities = getUserAuthority(user.getRoles());
        return buildUserForAuthentication(user, authorities);
    }

    private List<GrantedAuthority> getUserAuthority(Set<Role> userRoles) {
        Set<GrantedAuthority> roles = new HashSet<GrantedAuthority>();
        for (Role role : userRoles) {
            roles.add(new SimpleGrantedAuthority(role.getRole()));
        }
        List<GrantedAuthority> grantedAuthorities = new ArrayList<>(roles);
        return grantedAuthorities;
    }

    private UserDetails buildUserForAuthentication(User user, List<GrantedAuthority> authorities) {
        return new org.springframework.security.core.userdetails.User(user.getUsername(), user.getPassword(),
                true, true, true, true, authorities);
    }

    public List<User> getAllDoctors() {
        Role role = roleRepository.findByRole("Doctor");
        List<User> doctorList = userRepository.findByRoles(role);
        return doctorList;
    }

    public String deleteDoctor(DoctorOperation doctorOperation) {
        userRepository.deleteById(doctorOperation.getDoctorId());
        return "Doctor Deleted Successfully";
    }

    public String updateDoctor(DoctorOperation doctorOperation) {
        User user = userRepository.findById(doctorOperation.getDoctorId()).get();
        user.setDepartment(doctorOperation.getDepartment());
        userRepository.save(user);
        return "Doctor Updated Successfully";
    }
}
