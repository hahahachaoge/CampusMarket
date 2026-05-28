package com.campus.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.campus.dto.LoginDTO;
import com.campus.dto.RegisterDTO;
import com.campus.dto.UpdatePasswordDTO;
import com.campus.dto.UserUpdateDTO;
import com.campus.entity.User;
import com.campus.vo.UserVO;

public interface UserService extends IService<User> {
    void register(RegisterDTO dto);
    String login(LoginDTO dto);
    UserVO info();
    void update(UserUpdateDTO dto);
    void updatePassword(UpdatePasswordDTO dto);
    User getByUsername(String username);
}
