/*
 * Copyright (C), 2020, 安徽贝慕信息科技有限公司
 * FileName: AliSmsUtil
 * Author:   xao
 * Date:     2020/9/8 11:37
 * Description: 阿里云短信工具类
 * History:
 * <author>          <time>          <version>          <desc>
 * 作者姓名           修改时间           版本号              描述
 */
package com.bim.marvel.message.sms.enums;

/**
 * 短信
 * @author xao
 */
public enum SmsEnum {

    /**
     * Valid_Code_Sms_01
     */
    Valid_Code_Sms_01("1", "4位的验证码短信", SmsTypeEnum.SmsValidCode, "", "4"),
    Valid_Code_Sms_02("2", "6位的验证码短信", SmsTypeEnum.SmsValidCode, "", "6");

    /**
     * value
     */
    private String value;

    /**
     * desc
     */
    private String desc;

    /**
     * smsTypeEnum
     */
    private SmsTypeEnum smsTypeEnum;

    /**
     * templateParam
     */
    private String templateParam;

    /**
     * validCodeSize
     */
    private String validCodeSize;

    SmsEnum(String value, String desc, SmsTypeEnum smsTypeEnum, String templateParam, String validCodeSize) {
        this.value = value;
        this.desc = desc;
        this.smsTypeEnum = smsTypeEnum;
        this.validCodeSize = validCodeSize;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public SmsTypeEnum getSmsTypeEnum() {
        return smsTypeEnum;
    }

    public void setSmsTypeEnum(SmsTypeEnum smsTypeEnum) {
        this.smsTypeEnum = smsTypeEnum;
    }

    public String getTemplateParam() {
        return templateParam;
    }

    public void setTemplateParam(String templateParam) {
        this.templateParam = templateParam;
    }

    public String getValidCodeSize() {
        return validCodeSize;
    }

    public void setValidCodeSize(String validCodeSize) {
        this.validCodeSize = validCodeSize;
    }
}