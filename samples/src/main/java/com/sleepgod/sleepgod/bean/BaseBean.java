package com.sleepgod.sleepgod.bean;

/**
 * Created by cool on 2018/6/21.
 */
public class BaseBean<T> {
    public int ERRORCODE;
    public T RESULT;

    public boolean isSuccess(){
        if(ERRORCODE == 0){
            return true;
        }
        return false;
    }

    @Override
    public String toString() {
        return "BaseBean{" +
                "ERRORCODE=" + ERRORCODE +
                ", RESULT=" + RESULT +
                '}';
    }
}
