package com.ps.controller;

import com.alibaba.fastjson.JSONObject;
import com.ps.domain.Message;
import com.ps.domain.User;
import com.ps.service.UserService;
import org.apache.http.client.fluent.Form;
import org.apache.http.client.fluent.Request;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.PostConstruct;
import java.io.*;
import java.net.URLDecoder;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * 用户controller
 *
 * @author zzz
 * @date 2019/07/08
 */
@RestController
public class UserController {

    /*spring:
  # 连接数据库
  datasource:
    url: jdbc:mysql://127.0.0.1:3306/petty_loan
    username: root
    password: 123456
    driver-class-name: com.mysql.jdbc.Driver
    type: com.alibaba.druid.pool.DruidDataSource
  servlet:
    multipart:
      max-file-size: 10485760
      max-request-size: 104857600*/

    private Logger log = LoggerFactory.getLogger(UserController.class);

    @Autowired
    private UserService userService;

    @Value("${spring.datasource.url}")
    private String url;

    @PostConstruct
    public void test() {
        System.out.println("url:"+ url);

        System.out.println(userService.isExistIphone("13957546280"));
    }


    /**
     * 注册
     * @param user  用户注册的信息（手机号，密码）
     * @param id   短信表的ID，用来获取验证码
     * @return
     */
    @PostMapping("/regist/{id}")
    public Message register(@RequestBody User user, @PathVariable Integer id) {
        Message message = new Message();

        userService.regist(user, id);

        message.setCode(200);
        message.setMsg("注册成功");

        return message;
    }


    /**
     * 登录
     * @param user
     * @return
     */
    @PostMapping("/login/{id}")
    public Message login(@RequestBody User user, @PathVariable Integer id) {
        Message message = new Message();

        User isExistIphone = userService.login(user, id);

        message.setCode(200);
        message.setMsg("登录成功");
        message.setData(isExistIphone);

        return message;
    }


    /**
     * 判断手机号是否存在（当输入框失去焦点时）
     * @param iphone
     * @return
     */
    @PostMapping("/isExistByIphone")
    public Message isExistByIphone(@RequestParam String iphone) {
        Message message = new Message();

        User existIphone = userService.isExistIphone(iphone);

        // 如果existIphone不为空，则已存在
        if (existIphone != null) {
            message.setCode(400);
            message.setMsg("该手机号已存在");
            return message;
        }

        // 否则 为空，则不存在
        message.setCode(200);
        message.setMsg("该手机号不存在");

        return message;
    }


    /**
     * 发送短信验证码
     * @param iphone
     * @param type  类型，发送短信验证码的类型
     * @return
     */
    @PostMapping("/sendShortCode")
    public Message sendShortMessage(@RequestParam String iphone, @RequestParam Integer type) {
        Message message = new Message();

        // 获取验证码随机数
        String code = userService.getShortCode();

        // 将验证码添加到数据库中
        Integer id = userService.addShortCode(code, type);

        // 发送验证码
        try {
            Request.Post("http://v.juhe.cn/sms/send")
                    .bodyForm(Form.form().add("mobile", iphone)
                                        .add("tpl_id", "168912")
                                        .add("tpl_value", "%23code%23%3d"+code)
                                        .add("key", "e0ffec79b9acbb6ff4d7c09df89a6fdf")
                    .build()).execute().returnContent();
        } catch (IOException e) {
            e.printStackTrace();
        }

        message.setCode(200);
        message.setMsg("成功");
        message.setData(id);

        return message;
    }


    /**
     * 根据用户ID查询用户信息（查询个人资料）
     * @param id    用户ID
     * @return
     */
    @PostMapping("/queryUserById/{id}")
    public Message<User> queryUserById(@PathVariable Integer id) {
        Message<User> message = new Message<>();

        User user = userService.queryUserById(id);

        message.setCode(200);
        message.setData(user);

        return message;
    }


    /**
     * 编辑个人资料
     * @return
     */
    @PutMapping("/updateUserById")
    public Message updateUserById(@RequestBody User user) {
        Message message = new Message();

        userService.updateUserInfoById(user);

        message.setCode(200);
        message.setMsg("成功");

        return message;
    }


    /**
     * 上传审核信息
     * @param id
     * @param identity_card_before_img
     * @param identity_card_after_img
     * @param bank_img
     * @return
     * @throws IOException
     */
    @PostMapping("/upload/{id}")
    public Message uploadAuditInformation(@PathVariable Integer id,
                                          @RequestParam MultipartFile identity_card_before_img,
                                          @RequestParam MultipartFile identity_card_after_img,
                                          @RequestParam MultipartFile bank_img) throws IOException {


        MultipartFile[] parts = {identity_card_before_img, identity_card_after_img, bank_img};

        userService.uploadAuditInformation(id, parts);

        return new Message(200, "成功");
    }



    /**
     * 资产评估
     * @param id
     * @return
     */
    @PostMapping("/valuation/{id}")
    public Message assetValuation(@PathVariable Integer id) {
        Message message = new Message();

        userService.assetValuation(id);

        // 获取当前时间
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

        message.setCode(200);
        message.setMsg("您的评估于("+df.format(new Date())+")已提交，一般1至2个工作日后查看结果");

        return message;
    }


    @GetMapping("/notice")
    public void notice(@RequestParam String idcard, @RequestParam String data) throws UnsupportedEncodingException {
        log.debug(data);

        JSONObject json = JSONObject.parseObject(URLDecoder.decode(data, "UTF-8"));
        Integer money = json.getInteger("money");

        // 修改用户表的额度
        userService.addMoney(money, URLDecoder.decode(idcard, "UTF-8"));

    }

}