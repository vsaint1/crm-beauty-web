package br.com.crm.beauty.web.services.user;

import java.util.Date;
import java.util.Optional;

import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import br.com.crm.beauty.web.dtos.PasswordUpdateDto;
import br.com.crm.beauty.web.dtos.UserDto;
import br.com.crm.beauty.web.exceptions.NotFoundException;
import br.com.crm.beauty.web.models.User;
import br.com.crm.beauty.web.repositories.UserRepository;

@Service
public class UserService {

    private final UserRepository userRepository;

    private final ModelMapper modelMapper;

    public UserService(UserRepository userRepository, ModelMapper modelMapper) {
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

        userRepository.save(entity);

        user.setId(entity.getId());

        return user;

    }

    public void updatePassword(Long id, PasswordUpdateDto password) {
        var entity = getById(id);

        // TODO: send email etc
        // compare encrypted with spring security
        if (entity.getPassword().equals(password.oldPassword())) {

            if (!password.newPassword().equals(password.confirmPassword()))
                throw new RuntimeException("Passwords do not match.");

            entity.setPassword(password.newPassword());
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

}
