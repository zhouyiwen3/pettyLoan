package com.ps.service;

import com.ps.domain.User;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

/**
 * 用户service
 *
 * @author zzz
 * @date 2019/07/08
 */

public interface UserService {

    /**
     * 判断手机号是否存在
     * @param iphone
     * @return
     */
    User isExistIphone(String iphone);


    /**
     * 根据用户ID查询用户信息
     * @param id
     * @return
     */
    User queryUserById(Integer id);


    /**
     * 获取验证码
     * @return
     */
    String getShortCode();


    /**
     * 将验证码添加到数据库中
     * @param code
     * @param type
     * @return
     */
    Integer addShortCode(String code, Integer type);


    /**
     * 获取表中最大的id
     * @return
     */
    Integer queryMaxId();


    /**
     * 编辑个人资料
     * @param user
     */
    void updateUserInfoById(User user);


    /**
     * 将额度添加到用户表中
     * @param money
     * @param idcard
     */
    void addMoney(Integer money, String idcard);


    /**
     * 注册
     * @param user
     * @param id
     */
    void regist(User user, Integer id);


    /**
     * 登录
     * @param user
     * @param id
     * @return
     */
    User login(User user, Integer id);


    /**
     * 上传审核信息
     * @param id
     * @param parts
     */
    void uploadAuditInformation(Integer id, MultipartFile[] parts) throws IOException;


    /**
     * 资产评估
     * @param id
     */
    void assetValuation(Integer id);
}
