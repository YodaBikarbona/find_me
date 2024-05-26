package com.findme.user.service;

import com.findme.secutiry.model.SecurityEntity;
import com.findme.secutiry.repository.SecurityRepository;
import com.findme.user.dao.request.LoginDao;
import com.findme.user.dao.request.NewUserDao;
import com.findme.user.dao.response.CreatedUserDao;
import com.findme.user.dao.response.ResponseLoginDao;
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
    public CreatedUserDao registerUser(NewUserDao newUserDao) throws BadRequestException {
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

    @Transactional
    public ResponseLoginDao loginUser(LoginDao loginDao) throws BadRequestException {
        UserEntity user = userRepository.findByUsername(loginDao.getUsername());
        if (user == null) {
            throw new BadRequestException("Incorrect user or password!");
        }
        if (!user.getSecurity().getPassword().equals(user.getSecurity().encryptPassword(loginDao.getPassword()))) {
            throw new BadRequestException("Incorrect user or password!");
        }
        user.getSecurity().generateTokenSecret();
        securityRepository.save(user.getSecurity());
        return userMapper.userEntityToResponseLoginDao(user);
    }
}
