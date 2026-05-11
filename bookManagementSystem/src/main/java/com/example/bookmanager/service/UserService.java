package com.example.bookmanager.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.example.bookmanager.dto.LoginDTO;
import com.example.bookmanager.dto.RegisterDTO;
import com.example.bookmanager.dto.UserDTO;
import com.example.bookmanager.entity.User;
import com.example.bookmanager.vo.UserVO;

public interface UserService extends IService<User> {

    /**
     * 用户注册
     */
    void register(RegisterDTO registerDTO);

    /**
     * 用户登录，返回JWT令牌
     */
    String login(LoginDTO loginDTO);

    /**
     * 根据ID获取用户VO
     */
    UserVO getUserVOById(Long id);

    /**
     * 修改个人信息
     */
    void updateMyInfo(Long userId, UserDTO userDTO);

    /**
     * 修改密码
     */
    void updatePassword(Long userId, String oldPassword, String newPassword);

    /**
     * 分页查询用户列表（管理员）
     */
    Page<UserVO> pageUsers(Integer page, Integer size, String keyword);

    /**
     * 管理员新增用户
     */
    void addUser(UserDTO userDTO);

    /**
     * 管理员修改用户
     */
    void updateUser(Long id, UserDTO userDTO);

    /**
     * 管理员启用/禁用用户
     */
    void updateUserStatus(Long id, Integer status);

    /**
     * 管理员删除用户
     */
    void deleteUser(Long id);
}