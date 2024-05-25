package com.findme.user.mapper;

import com.findme.user.dao.response.CreatedUserDao;
import com.findme.user.model.UserEntity;
import org.springframework.stereotype.Component;

@Component
public class UserMapper {

    public CreatedUserDao userEntityToNewUserDao(UserEntity userEntity) {
        return new CreatedUserDao(userEntity.getId(), userEntity.getEmail(), userEntity.getUsername(), userEntity.getPhoneNumber());
    }
}
