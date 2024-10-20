package com.example.common;

import com.example.common.enums.ResultCodeEnum;
import lombok.Data;

@Data
public class Result {
    /** 状态码 */
    private String code;
    /** 返回消息 */
    private String msg;
    /** 返回数据 */
    private Object data;

    private Result(Object data) {
        this.data = data;
    }

    public Result() {
    }

    /**
     * 返回成功时不包含数据的响应对象
     * @return Result对象
     */
    public static Result success() {
        Result result = new Result();
        result.setCode(ResultCodeEnum.SUCCESS.code);
        result.setMsg(ResultCodeEnum.SUCCESS.msg);
        return result;
    }

    /**
     * 返回成功时包含数据的响应对象
     * @param data 数据
     * @return Result对象
     */
    public static Result success(Object data) {
        Result result = new Result (data);
        result.setCode(ResultCodeEnum.SUCCESS.code);
        result.setMsg(ResultCodeEnum.SUCCESS.msg);
        return result;
    }

    /**
     * 返回失败时不包含数据的响应对象
     * @return Result对象
     */
    public static Result error() {
        Result result = new Result();
        result.setCode(ResultCodeEnum.SYSTEM_ERROR.code);
        result.setMsg(ResultCodeEnum.SYSTEM_ERROR.msg);
        return result;
    }

    public static Result error(String msg) {
        Result result = new Result();
        result.setCode(ResultCodeEnum.SYSTEM_ERROR.code);
        result.setMsg(msg);
        return result;
    }
    /**
     * 返回失败时自定义状态码和响应消息的对象
     * @param code 状态码
     * @param msg 响应消息
     * @return Result 对象
     */
    public static Result error(String code, String msg) {
        Result result = new Result();
        result.setCode(code);
        result.setMsg(msg);
        return result;
    }

    /**
     * 返回失败时包含数据的响应对象
     * @param resultCodeEnum 状态码枚举
     * @return Result对象
     */
    public static Result error(ResultCodeEnum resultCodeEnum) {
        Result result = new Result();
        result.setCode(resultCodeEnum.code);
        result.setMsg(resultCodeEnum.msg);
        return result;
    }
}
