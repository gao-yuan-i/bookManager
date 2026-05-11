package com.example.bookmanager.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.bookmanager.dto.LoginDTO;
import com.example.bookmanager.dto.RegisterDTO;
import com.example.bookmanager.dto.UserDTO;
import com.example.bookmanager.entity.User;
import com.example.bookmanager.enums.RoleEnum;
import com.example.bookmanager.exception.BusinessException;
import com.example.bookmanager.mapper.UserMapper;
import com.example.bookmanager.service.UserService;
import com.example.bookmanager.utils.JwtUtils;
import com.example.bookmanager.vo.UserVO;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

@Service
@RequiredArgsConstructor
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {

    private final BCryptPasswordEncoder passwordEncoder;
    private final JwtUtils jwtUtils;

    @Override
    @Transactional
    public void register(RegisterDTO registerDTO) {
        // 检查用户名是否已存在
        LambdaQueryWrapper<User> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(User::getUsername, registerDTO.getUsername());
        if (baseMapper.selectCount(wrapper) > 0) {
            throw new BusinessException("用户名已存在");
        }

        User user = new User();
        BeanUtils.copyProperties(registerDTO, user);
        user.setPassword(passwordEncoder.encode(registerDTO.getPassword()));
        user.setRole(RoleEnum.USER.getCode());
        user.setStatus(1);
        user.setMaxBorrow(5);
        user.setBorrowCount(0);
        baseMapper.insert(user);
    }

    @Override
    public String login(LoginDTO loginDTO) {
        LambdaQueryWrapper<User> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(User::getUsername, loginDTO.getUsername());
        User user = baseMapper.selectOne(wrapper);

        if (user == null) {
            throw new BusinessException("用户名或密码错误");
        }

        if (user.getStatus() == 0) {
            throw new BusinessException("账号已被禁用，请联系管理员");
        }

        if (!passwordEncoder.matches(loginDTO.getPassword(), user.getPassword())) {
            throw new BusinessException("用户名或密码错误");
        }

        return jwtUtils.generateToken(user.getId(), user.getUsername(), user.getRole());
    }

    @Override
    public UserVO getUserVOById(Long id) {
        User user = baseMapper.selectById(id);
        if (user == null) {
            throw new BusinessException("用户不存在");
        }
        return convertToVO(user);
    }

    @Override
    @Transactional
    public void updateMyInfo(Long userId, UserDTO userDTO) {
        User user = baseMapper.selectById(userId);
        if (user == null) {
            throw new BusinessException("用户不存在");
        }

        // 只允许修改部分字段
        if (StringUtils.hasText(userDTO.getRealName())) {
            user.setRealName(userDTO.getRealName());
        }
        if (StringUtils.hasText(userDTO.getPhone())) {
            user.setPhone(userDTO.getPhone());
        }
        if (StringUtils.hasText(userDTO.getEmail())) {
            user.setEmail(userDTO.getEmail());
        }
        baseMapper.updateById(user);
    }

    @Override
    @Transactional
    public void updatePassword(Long userId, String oldPassword, String newPassword) {
        User user = baseMapper.selectById(userId);
        if (user == null) {
            throw new BusinessException("用户不存在");
        }

        if (!passwordEncoder.matches(oldPassword, user.getPassword())) {
            throw new BusinessException("原密码错误");
        }

        user.setPassword(passwordEncoder.encode(newPassword));
        baseMapper.updateById(user);
    }

    @Override
    public Page<UserVO> pageUsers(Integer page, Integer size, String keyword) {
        LambdaQueryWrapper<User> wrapper = new LambdaQueryWrapper<>();
        if (StringUtils.hasText(keyword)) {
            wrapper.like(User::getUsername, keyword)
                    .or()
                    .like(User::getRealName, keyword)
                    .or()
                    .like(User::getPhone, keyword);
        }
        wrapper.orderByDesc(User::getCreateTime);

        Page<User> userPage = baseMapper.selectPage(new Page<>(page, size), wrapper);
        Page<UserVO> voPage = new Page<>(page, size, userPage.getTotal());
        voPage.setRecords(userPage.getRecords().stream().map(this::convertToVO).toList());
        return voPage;
    }

    @Override
    @Transactional
    public void addUser(UserDTO userDTO) {
        // 检查用户名是否已存在
        LambdaQueryWrapper<User> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(User::getUsername, userDTO.getUsername());
        if (baseMapper.selectCount(wrapper) > 0) {
            throw new BusinessException("用户名已存在");
        }

        User user = new User();
        BeanUtils.copyProperties(userDTO, user);
        if (StringUtils.hasText(userDTO.getPassword())) {
            user.setPassword(passwordEncoder.encode(userDTO.getPassword()));
        } else {
            user.setPassword(passwordEncoder.encode("123456")); // 默认密码
        }
        if (!StringUtils.hasText(user.getRole())) {
            user.setRole(RoleEnum.USER.getCode());
        }
        user.setStatus(1);
        user.setBorrowCount(0);
        if (user.getMaxBorrow() == null) {
            user.setMaxBorrow(5);
        }
        baseMapper.insert(user);
    }

    @Override
    @Transactional
    public void updateUser(Long id, UserDTO userDTO) {
        User user = baseMapper.selectById(id);
        if (user == null) {
            throw new BusinessException("用户不存在");
        }

        if (StringUtils.hasText(userDTO.getPassword())) {
            user.setPassword(passwordEncoder.encode(userDTO.getPassword()));
        }
        if (StringUtils.hasText(userDTO.getRealName())) {
            user.setRealName(userDTO.getRealName());
        }
        if (StringUtils.hasText(userDTO.getPhone())) {
            user.setPhone(userDTO.getPhone());
        }
        if (StringUtils.hasText(userDTO.getEmail())) {
            user.setEmail(userDTO.getEmail());
        }
        if (StringUtils.hasText(userDTO.getRole())) {
            user.setRole(userDTO.getRole());
        }
        if (userDTO.getMaxBorrow() != null) {
            user.setMaxBorrow(userDTO.getMaxBorrow());
        }
        baseMapper.updateById(user);
    }

    @Override
    @Transactional
    public void updateUserStatus(Long id, Integer status) {
        User user = baseMapper.selectById(id);
        if (user == null) {
            throw new BusinessException("用户不存在");
        }
        user.setStatus(status);
        baseMapper.updateById(user);
    }

    @Override
    @Transactional
    public void deleteUser(Long id) {
        User user = baseMapper.selectById(id);
        if (user == null) {
            throw new BusinessException("用户不存在");
        }
        // 检查是否有未归还的书籍
        if (user.getBorrowCount() > 0) {
            throw new BusinessException("该用户还有未归还的书籍，无法删除");
        }
        baseMapper.deleteById(id);
    }

    /**
     * User -> UserVO
     */
    private UserVO convertToVO(User user) {
        UserVO vo = new UserVO();
        BeanUtils.copyProperties(user, vo);
        return vo;
    }
}