package com.example.demo.infruastructure.exception;

import com.example.demo.infruastructure.enums.BaseResultEnum;

public class BaseCustomException extends RuntimeException {
    private String code;
    private String msg;
    private String data;

    public BaseCustomException(String code, String msg) {
        super(code + msg);
        this.code = code;
        this.msg = msg;
    }

    public BaseCustomException(String code, String msg, String data) {
        super(code + msg);
        this.code = code;
        this.msg = msg;
        this.data = data;
    }

    public BaseCustomException(BaseResultEnum baseResultEnum) {
        super(baseResultEnum.toString());
        this.code = baseResultEnum.getCode();
        this.msg = baseResultEnum.getMsg();
    }

    public BaseCustomException(String msg) {
        super(BaseResultEnum.CUSTOMIZE_ERROR.getCode() + msg);
        this.code = BaseResultEnum.CUSTOMIZE_ERROR.getCode();
        this.msg = msg;
    }

    @Override
    public boolean equals(Object o) {
        if (o == this) {
            return true;
        } else if (!(o instanceof BaseCustomException)) {
            return false;
        } else {
            BaseCustomException other = (BaseCustomException) o;
            if (!other.canEqual(this)) {
                return false;
            } else if (!super.equals(o)) {
                return false;
            } else {
                label49:
                {
                    Object this$code = this.getCode();
                    Object other$code = other.getCode();
                    if (this$code == null) {
                        if (other$code == null) {
                            break label49;
                        }
                    } else if (this$code.equals(other$code)) {
                        break label49;
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
                }
                return this$data.equals(other$data);
            }
        }
    }

    protected boolean canEqual(Object other) {
        return other instanceof BaseCustomException;
    }

    @Override
    public int hashCode() {
        boolean PRIME = true;
        int result = super.hashCode();
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
        return "BaseCustomException(super=" + super.toString() + ", code=" + this.getCode() + ", msg=" + this.getMsg() + ", data=" + this.getData() + ")";
    }

    public String getCode() {
        return this.code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getMsg() {
        return this.msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public String getData() {
        return this.data;
    }

    public void setData(String data) {
        this.data = data;
    }
}

