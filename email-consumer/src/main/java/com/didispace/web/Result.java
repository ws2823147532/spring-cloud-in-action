package com.didispace.web;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 邮件发送结果
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Result {

    /**
     * 200：成功
     * 100：失败
     */
    private Integer result;

    /**
     * result为100时存在错误描述
     */
    private String msg;

    /**
     * 实际返回的数据
     */
    private Object[] data;

    public static Result success(Object[] data, String msg) {
        return Result.builder().data(data).msg(msg).result(200).build();
    }

    public static Result failed(Object[] data, String msg) {
        return Result.builder().result(100).msg(msg).data(data).build();
    }
}
