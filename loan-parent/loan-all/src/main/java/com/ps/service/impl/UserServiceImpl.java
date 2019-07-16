package com.ps.service.impl;

import com.ps.domain.User;
import com.ps.exception.BusinessException;
import com.ps.mapper.UserMapper;
import com.ps.service.UserService;
import org.apache.http.client.fluent.Request;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;


/**
 * user实现类
 *
 * @author zzz
 * @date 2019/07/08
 */

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserMapper userMapper;

    @Value("${filePath.name}")
    private String filename;


    /**
     * 资产评估url
     */
    @Value("$(estimate_url)")
    private String estimateUrl;


    @Override
    public User isExistIphone(String iphone) {
        return userMapper.isExistIphone(iphone);
    }


    @Override
    public User queryUserById(Integer id) {
        return userMapper.queryUserById(id);
    }


    @Override
    public String getShortCode() {
        String[] beforeShuffle= new String[] { "1", "2", "3", "4", "5", "6", "7", "8", "9" };

        // 将数组转换为集合
        List list = Arrays.asList(beforeShuffle);

        //打乱集合顺序
        Collections.shuffle(list);

        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < list.size(); i++) {
            // 追加到sb中
            sb.append(list.get(i));
        }

        return sb.toString().substring(3, 7);
    }

    @Override
    public Integer addShortCode(String code, Integer type) {
        // 获取表中最大的id
        Integer id = queryMaxId()+1;

        // 添加验证码
        userMapper.addShortCode(code, id, type);

        return id;
    }


    @Override
    public void addMoney(Integer money, String idcard) {
        userMapper.addMoney(money, idcard);
    }


    @Override
    public void updateUserInfoById(User user) {
        userMapper.updateUserInfoById(user);
    }


    public Integer queryUserMaxId() {
        return userMapper.queryUserMaxId();
    }


    @Override
    public Integer queryMaxId() {
        return userMapper.queryMaxId();
    }


    @Override
    public void regist(User user, Integer id) {
        // 判断手机号是否存在
        User isExistIphone = userMapper.isExistIphone(user.getIphone());

        if(isExistIphone != null) {
            throw new BusinessException(401, "手机号已被注册");
        }

        // 根据id查询出验证码
        String code = userMapper.isTrueByCode(id);

        // 判断验证码是否正确（不正确）
        if (!code.equals(user.getCode())) {
            throw new BusinessException(400, "验证码错误");
        }

        // 验证码正确
        // 添加用户
        Integer userId = queryUserMaxId();

        user.setId(userId);

        userMapper.addUserInfo(user);

        // 将用户ID添加到短信表中
        userMapper.addShortUserId(id, userId);
    }


    @Override
    public User login(User user, Integer id) {

        // 判断手机号是否存在
        User isExistIphone = userMapper.isExistIphone(user.getIphone());

        // 如果isExistIphone 为空，则该手机号不存在
        if (isExistIphone == null) {
            throw new BusinessException(400, "该手机号不存在");
        }

        // 判断验证码是否正确
        String code = userMapper.isTrueByCode(id);
        if (!code.equals(user.getCode())) {
            throw new BusinessException(401, "验证码错误");
        }

        // 判断密码是否正确
        if (!isExistIphone.getPassword().equals(user.getPassword())) {
            throw new BusinessException(402, "密码错误");
        }

        return isExistIphone;
    }


    @Override
    public void uploadAuditInformation(Integer id, MultipartFile[] parts) throws IOException {
        User user = new User();
        user.setId(id);

        for(int i = 0; i < parts.length; i++) {
            MultipartFile part = parts[i];

            // 判断该文件夹存不存在，不存在则创建
            File file = new File(filename+id);
            if (!file.exists()) {
                file.mkdir();
            }

            // 将文件读出
            InputStream is = part.getInputStream();

            byte[] b = new byte[is.available()];
            is.read(b);

            // 将文件保存到本地磁盘
            FileOutputStream fos = null;

            if (i == 0) {
                fos = new FileOutputStream(file.getAbsolutePath()+"\\before.jpg");
                user.setIdentity_card_before_img("before.jpg");
            }

            if (i == 1) {
                fos = new FileOutputStream(file.getAbsolutePath()+"\\after.jpg");
                user.setIdentity_card_after_img("after.jpg");
            }

            if (i == 2) {
                fos = new FileOutputStream(file.getAbsolutePath()+"\\bank_img.jpg");
                user.setBank_img("bank_img.jpg");
            }

            fos.write(b);
        }

        // 将图片添加到用户表中
        userMapper.updateImg(user);
    }


    @Override
    public void assetValuation(Integer id) {
        // 判断用户是否完善个人信息
        User selectUser = userMapper.queryUserById(id);
        if (selectUser.getName() == null && selectUser.getIdentity_card() == null && selectUser.getBank_card() == null) {
            throw new BusinessException(401, "未完善个人资料");
        }

        // 判断用户是否审核
        if (selectUser.getAudit_status() == 3) {
            throw new BusinessException(402, "身份未审核");
        }

        // 判断用户身份是否正在审核
        if (selectUser.getAudit_status() == 0) {
            throw new BusinessException(402, "身份正在审核");
        }

        // 判断用户是否审核失败
        if (selectUser.getAudit_status() == 2) {
            throw new BusinessException(403, "身份审核失败，请重新编辑个人资料");
        }

        // 判断用户是否已经评估
        if (selectUser.getQuota() != null) {
            throw new BusinessException(404, "您已评估过金额");
        }

        try {
            Request.Get(estimateUrl+"?key=abcdef&idcard=" + selectUser.getIdentity_card() + "&notice_url=http://192.168.3.33:9091/notice")
                    .execute().returnContent();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
