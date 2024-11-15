package com.example.demo.infruastructure.common;


import cn.hutool.json.JSONObject;
import com.example.demo.infruastructure.enums.BaseResultEnum;
import com.example.demo.infruastructure.exception.BaseCustomException;
import io.swagger.annotations.ApiModelProperty;

public class BaseResult<T> {
    @ApiModelProperty("返回码")
    private String code;
    @ApiModelProperty("描述")
    private String msg;
    @ApiModelProperty("业务结果对象")
    private T data;

    public BaseResult() {
    }

    public BaseResult(String code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    public BaseResult(String code, String msg, T data) {
        this.code = code;
        this.msg = msg;
        this.data = data;
    }

    public BaseResult(BaseResultEnum baseResultEnum) {
        this.code = baseResultEnum.getCode();
        this.msg = baseResultEnum.getMsg();
    }

    public BaseResult(BaseCustomException e) {
        this.code = e.getCode();
        this.msg = e.getMsg();
    }

    public void setCodeAndMsg(BaseCustomException e) {
        this.code = e.getCode();
        this.msg = e.getMsg();
    }

    public void setCodeAndMsgAndData(BaseCustomException e, Class<T> clazz) {
        this.code = e.getCode();
        this.msg = e.getMsg();
        if (e.getData() != null) {
            JSONObject jsonObject2 = new JSONObject(e.getData());
            this.data = jsonObject2.toBean(clazz);
        }

    }

    public void setCodeAndMsg(BaseResultEnum baseResultEnum) {
        this.code = baseResultEnum.getCode();
        this.msg = baseResultEnum.getMsg();
    }

    public void setCodeAndMsg(String code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    public void setCodeAndMsg(String code, String msg, T data) {
        this.code = code;
        this.msg = msg;
        this.data = data;
    }

    public void setSuccess(T data) {
        this.code = BaseResultEnum.SUCCESS.getCode();
        this.msg = BaseResultEnum.SUCCESS.getMsg();
        this.data = data;
    }

    public void setSuccess() {
        this.code = BaseResultEnum.SUCCESS.getCode();
        this.msg = BaseResultEnum.SUCCESS.getMsg();
    }

    public String getCode() {
        return this.code;
    }

    public String getMsg() {
        return this.msg;
    }

    public T getData() {
        return this.data;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public void setData(T data) {
        this.data = data;
    }

    @Override
    public boolean equals(Object o) {
        if (o == this) {
            return true;
        } else if (!(o instanceof BaseResult)) {
            return false;
        } else {
            BaseResult<?> other = (BaseResult) o;
            if (!other.canEqual(this)) {
                return false;
            } else {
                label47:
                {
                    Object this$code = this.getCode();
                    Object other$code = other.getCode();
                    if (this$code == null) {
                        if (other$code == null) {
                            break label47;
                        }
                    } else if (this$code.equals(other$code)) {
                        break label47;
                    }

                    return false;
                }

                Object this$msg = this.getMsg();
                Object other$msg = other.getMsg();
                if (this$msg == null) {
                    if (other$msg != null) {
                        return false;
                    }
                } else if (!this$msg.equals(other$msg)) {
                    return false;
                }

                Object this$data = this.getData();
                Object other$data = other.getData();
                if (this$data == null) {
                    return other$data == null;
                } else return this$data.equals(other$data);
            }
        }
    }

    protected boolean canEqual(Object other) {
        return other instanceof BaseResult;
    }

    @Override
    public int hashCode() {
        boolean PRIME = true;
        int result = 1;
        Object $code = this.getCode();
        result = result * 59 + ($code == null ? 43 : $code.hashCode());
        Object $msg = this.getMsg();
        result = result * 59 + ($msg == null ? 43 : $msg.hashCode());
        Object $data = this.getData();
        result = result * 59 + ($data == null ? 43 : $data.hashCode());
        return result;
    }

    @Override
    public String toString() {
        return "BaseResult(code=" + this.getCode() + ", msg=" + this.getMsg() + ", data=" + this.getData() + ")";
    }
}
