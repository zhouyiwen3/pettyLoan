package com.ps.mapper;

import com.ps.domain.User;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;


/**
 * 用户mapper
 *
 * @author zzz
 * @date 2019/07/08
 */

@Mapper
@Repository
public interface UserMapper {

    /**
     * 判断手机号是否存在
     * @param iphone
     * @return
     */
    User isExistIphone(String iphone);


    /**
     * 查询验证吗
     * @param id
     * @return
     */
    String queryCodeByUserId(Integer id, Integer type);


    /**
     * 根据用户ID查询用户信息
     * @param id
     * @return
     */
    User queryUserById(Integer id);


    /**
     * 获取表中最大的id
     * @return
     */
    Integer queryMaxId();


    /**
     * 讲验证码添加到数据库中
     * @param code
     * @param id
     * @param type
     */
    void addShortCode(String code, Integer id, Integer type);


    /**
     * 根据id查询出验证码
     * @param id
     * @return
     */
    String isTrueByCode(Integer id);


    /**
     * 添加用户
     * @param user
     */
    void addUserInfo(User user);


    /**
     * 查找用户表最大的ID
     * @return
     */
    Integer queryUserMaxId();


    /**
     * 将用户ID添加到短信表中
     * @param id
     * @param userId
     */
    void addShortUserId(Integer id, Integer userId);


    /**
     * 编辑个人资料
     * @param user
     */
    void updateUserInfoById(User user);


    /**
     * 查询用户的身份证号
     * @param id
     * @return
     */
    String queryIdentityCard(Integer id);


    /**
     * 将图片添加到用户表中（根据用户id修改用户表中的身份证图片和银行卡图片）
     * @param user
     */
    void updateImg(User user);


    /**
     * 将额度添加到用户表中
     * @param money
     * @param idcard
     */
    void addMoney(Integer money, String idcard);
}
