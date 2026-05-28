package com.campus.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.campus.context.UserContext;
import com.campus.dto.UpdatePasswordDTO;
import com.campus.dto.UserUpdateDTO;
import com.campus.entity.User;
import com.campus.dto.LoginDTO;
import com.campus.dto.RegisterDTO;
import com.campus.mapper.UserMapper;
import com.campus.service.UserService;
import com.campus.utils.JwtUtils;
import com.campus.vo.UserVO;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
@Service
@RequiredArgsConstructor
public class UserServiceImpl extends ServiceImpl<UserMapper,User> implements UserService {
    private final UserMapper userMapper;
    private final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    @Override
    public void register(RegisterDTO dto){
        LambdaQueryWrapper<User> wrapper = new LambdaQueryWrapper<User>();
        wrapper.eq(User::getUsername,dto.getUsername());
        User existUser = userMapper.selectOne(wrapper);
        if(existUser != null){
            throw new RuntimeException("用户名已存在");
        }
        User user = new User();
        user.setUsername(dto.getUsername());
        user.setPassword(
                passwordEncoder.encode(dto.getPassword())
        );
        user.setNickname(dto.getNickname());
        userMapper.insert(user);
    }

    @Override
    public String login(LoginDTO dto){
        LambdaQueryWrapper<User> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(User::getUsername,dto.getUsername());
        User user = userMapper.selectOne(wrapper);
        if(user == null){
            throw new RuntimeException("用户不存在");
        }
        boolean matches = passwordEncoder.matches(
            dto.getPassword(),
            user.getPassword()
        );
        if(!matches){
            throw new RuntimeException("密码错误");
        }
        return JwtUtils.createToken(user.getId());
    }

    @Override
    public UserVO info(){
        //当前登陆用户
        Long userId = UserContext.getUserId();
        //查询用户
        User user = userMapper.selectById(userId);
        if(user == null){
            throw new RuntimeException("用户不存在");
        }
        UserVO vo = new UserVO();
        vo.setId(user.getId());
        vo.setUsername(user.getUsername());
        vo.setNickname(user.getNickname());
        vo.setAvatar(user.getAvatar());
        vo.setPhone(user.getPhone());
        vo.setEmail(user.getEmail());
        return vo;
    }

    @Override
    public void update(UserUpdateDTO dto){
        //当前登录用户
        Long userId = UserContext.getUserId();
        //查询用户
        User user = userMapper.selectById(userId);
        if(user == null){
            throw new RuntimeException("用户不存在");
        }
        //更新信息
        user.setNickname(dto.getNickname());
        user.setAvatar(dto.getAvatar());
        user.setPhone(dto.getPhone());
        user.setEmail(dto.getEmail());
        userMapper.updateById(user);
    }

    @Override
    public void updatePassword(
            UpdatePasswordDTO dto
    ){
        //当前登陆用户
        Long userId = UserContext.getUserId();
        //查询用户
        User user = userMapper.selectById(userId);
        if(user == null){
            throw new RuntimeException("用户不存在");
        }
        //校验旧密码
        boolean matches = passwordEncoder.matches(dto.getOldPassword(),user.getPassword());
        if(!matches){
            throw new RuntimeException("旧密码错误");
        }
        //新密码加密
        String newPassword = passwordEncoder.encode(dto.getNewPassword());
        user.setPassword(newPassword);
        userMapper.updateById(user);
    }

    @Override
    public User getByUsername(String username){
        return lambdaQuery().eq(User::getUsername,username).one();
    }
}
