package com.campus.service;

import com.campus.dto.LoginDTO;
import com.campus.dto.RegisterDTO;
import com.campus.dto.UpdatePasswordDTO;
import com.campus.dto.UserUpdateDTO;
import com.campus.vo.UserVO;

public interface UserService {
    void register(RegisterDTO dto);
    String login(LoginDTO dto);
    UserVO info();
    void update(UserUpdateDTO dto);
    void updatePassword(UpdatePasswordDTO dto);
}
