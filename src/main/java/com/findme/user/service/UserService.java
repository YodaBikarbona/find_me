package com.findme.user.service;

import com.findme.secutiry.model.SecurityEntity;
import com.findme.secutiry.repository.SecurityRepository;
import com.findme.user.dao.request.NewUserDao;
import com.findme.user.dao.response.CreatedUserDao;
import com.findme.user.mapper.UserMapper;
import com.findme.user.model.UserEntity;
import com.findme.user.repository.UserRepository;
import jakarta.transaction.Transactional;
import org.apache.coyote.BadRequestException;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final SecurityRepository securityRepository;
    private final UserMapper userMapper;

    public UserService(UserRepository userRepository, SecurityRepository securityRepository, UserMapper userMapper) {
        this.userRepository = userRepository;
        this.securityRepository = securityRepository;
        this.userMapper = userMapper;
    }

    @Transactional
    public CreatedUserDao createNewUser(NewUserDao newUserDao) throws BadRequestException {
        UserEntity user = userRepository.findByEmail(newUserDao.getEmail());
        if (user != null) {
            throw new BadRequestException("This user already exists!");
        }
        SecurityEntity security = new SecurityEntity(newUserDao.getPassword());
        securityRepository.save(security);
        user = new UserEntity(newUserDao.getEmail(), newUserDao.getUsername(), newUserDao.getPhoneNumber());
        user.setSecurity(security);
        userRepository.save(user);
        return userMapper.userEntityToNewUserDao(user);
    }
}
