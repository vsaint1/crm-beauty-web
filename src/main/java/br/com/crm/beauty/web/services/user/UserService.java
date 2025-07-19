package br.com.crm.beauty.web.services.user;

import java.util.Date;
import java.util.Optional;

import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import br.com.crm.beauty.web.dtos.PasswordUpdateDto;
import br.com.crm.beauty.web.dtos.UserDto;
import br.com.crm.beauty.web.exceptions.NotFoundException;
import br.com.crm.beauty.web.models.User;
import br.com.crm.beauty.web.repositories.UserRepository;

@Service
public class UserService implements UserDetailsService {

    private final UserRepository userRepository;
    private final ModelMapper modelMapper;

    public UserService(UserRepository userRepository,  ModelMapper modelMapper) {
        this.userRepository = userRepository;
        this.modelMapper = modelMapper;
    }

    private UserDto toDTO(User user) {
        return modelMapper.map(user, UserDto.class);
    }

    private User toModel(UserDto user) {
        return modelMapper.map(user, User.class);
    }

    public UserDto add(UserDto user) {

        user.setCreatedAt(new Date());

        var entity = toModel(user);

        var encryptedPassword = new BCryptPasswordEncoder().encode(user.getPassword());

        entity.setPassword(encryptedPassword);
        userRepository.save(entity);

        user.setId(entity.getId());

        return user;

    }

    public void updatePassword(Long id, PasswordUpdateDto password) {
        var entity = getById(id);

        var bcrypt = new BCryptPasswordEncoder();

        // TODO: send email etc
        if (bcrypt.matches(password.oldPassword(), entity.getPassword())) {

            if (!password.newPassword().equals(password.confirmPassword()))
                throw new RuntimeException("Passwords do not match.");

            var encryptedPassword = new BCryptPasswordEncoder().encode(password.newPassword());
            entity.setPassword(encryptedPassword);
            entity = userRepository.save(entity);
            return;
        }

        throw new RuntimeException("The password is incorrect.");

    }

    public UserDto update(Long id, UserDto user) {

        var entity = getById(id);

        entity.setFirstName(user.getFirstName());
        entity.setLastName(user.getLastName());

        entity = userRepository.save(entity);

        var dto = new UserDto();
        dto = modelMapper.map(entity, UserDto.class);

        return dto;
    }

    private User getById(Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("User not found with id " + id));
    }

    public UserDto findById(Long id) {
        return userRepository.findById(id).map(this::toDTO)
                .orElseThrow(() -> new NotFoundException("User not found with id " + id));
    }

    public Optional<UserDto> findByEmail(String email) {
        return userRepository.findByEmail(email).map(this::toDTO);
    }

    public Page<UserDto> findAll(Pageable pageable) {
        return userRepository.findAll(pageable).map(this::toDTO);
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return userRepository.findByEmail(username)
                .orElseThrow(() -> new UsernameNotFoundException("Email or password is incorrect"));
    }

}
