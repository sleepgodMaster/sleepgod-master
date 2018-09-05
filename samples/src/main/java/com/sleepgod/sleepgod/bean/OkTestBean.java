package com.sleepgod.sleepgod.bean;

import java.util.List;

public class OkTestBean {


    /**
     * code : 200
     * msg : 成功
     * success : true
     * data : {"sysMessageList":[{"date":"2018-09-04 16:00:03","flag":"N","sign":19,"messageId":6816,"pushNum":2090,"consult":"N","title":"合同到期提醒","content":"【有巢公寓】亲爱的租客，您的合同还有30天即将到期，可登录有巢公寓APP进行查看，祝您生活幸福。"},{"date":"2018-09-04 16:00:00","flag":"N","sign":20,"messageId":6799,"pushNum":2089,"consult":"N","title":"合同到期提醒","content":"【有巢公寓】亲爱的租客，您的合同还有20天即将到期，可登录有巢公寓APP进行查看，祝您生活幸福。"},{"date":"2018-09-03 16:00:03","flag":"N","sign":19,"messageId":6770,"pushNum":2073,"consult":"N","title":"合同到期提醒","content":"【有巢公寓】亲爱的租客，您的合同还有30天即将到期，可登录有巢公寓APP进行查看，祝您生活幸福。"},{"date":"2018-08-30 16:00:02","flag":"N","sign":19,"messageId":6563,"pushNum":1955,"consult":"N","title":"合同到期提醒","content":"【有巢公寓】亲爱的租客，您的合同还有30天即将到期，可登录有巢公寓APP进行查看，祝您生活幸福。"},{"date":"2018-08-30 16:00:02","flag":"N","sign":19,"messageId":6574,"pushNum":1955,"consult":"N","title":"合同到期提醒","content":"【有巢公寓】亲爱的租客，您的合同还有30天即将到期，可登录有巢公寓APP进行查看，祝您生活幸福。"}],"totalPage":20}
     * attr1 : null
     * attr2 : null
     */

    private String code;
    private String msg;
    private boolean success;
    private DataBean data;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public DataBean getData() {
        return data;
    }

    public void setData(DataBean data) {
        this.data = data;
    }

    public static class DataBean {
        /**
         * sysMessageList : [{"date":"2018-09-04 16:00:03","flag":"N","sign":19,"messageId":6816,"pushNum":2090,"consult":"N","title":"合同到期提醒","content":"【有巢公寓】亲爱的租客，您的合同还有30天即将到期，可登录有巢公寓APP进行查看，祝您生活幸福。"},{"date":"2018-09-04 16:00:00","flag":"N","sign":20,"messageId":6799,"pushNum":2089,"consult":"N","title":"合同到期提醒","content":"【有巢公寓】亲爱的租客，您的合同还有20天即将到期，可登录有巢公寓APP进行查看，祝您生活幸福。"},{"date":"2018-09-03 16:00:03","flag":"N","sign":19,"messageId":6770,"pushNum":2073,"consult":"N","title":"合同到期提醒","content":"【有巢公寓】亲爱的租客，您的合同还有30天即将到期，可登录有巢公寓APP进行查看，祝您生活幸福。"},{"date":"2018-08-30 16:00:02","flag":"N","sign":19,"messageId":6563,"pushNum":1955,"consult":"N","title":"合同到期提醒","content":"【有巢公寓】亲爱的租客，您的合同还有30天即将到期，可登录有巢公寓APP进行查看，祝您生活幸福。"},{"date":"2018-08-30 16:00:02","flag":"N","sign":19,"messageId":6574,"pushNum":1955,"consult":"N","title":"合同到期提醒","content":"【有巢公寓】亲爱的租客，您的合同还有30天即将到期，可登录有巢公寓APP进行查看，祝您生活幸福。"}]
         * totalPage : 20
         */

        private int totalPage;
        private List<SysMessageListBean> sysMessageList;

        public int getTotalPage() {
            return totalPage;
        }

        public void setTotalPage(int totalPage) {
            this.totalPage = totalPage;
        }

        public List<SysMessageListBean> getSysMessageList() {
            return sysMessageList;
        }

        public void setSysMessageList(List<SysMessageListBean> sysMessageList) {
            this.sysMessageList = sysMessageList;
        }

        public static class SysMessageListBean {
            /**
             * date : 2018-09-04 16:00:03
             * flag : N
             * sign : 19
             * messageId : 6816
             * pushNum : 2090
             * consult : N
             * title : 合同到期提醒
             * content : 【有巢公寓】亲爱的租客，您的合同还有30天即将到期，可登录有巢公寓APP进行查看，祝您生活幸福。
             */

            private String date;
            private String flag;
            private int sign;
            private int messageId;
            private int pushNum;
            private String consult;
            private String title;
            private String content;

            public String getDate() {
                return date;
            }

            public void setDate(String date) {
                this.date = date;
            }

            public String getFlag() {
                return flag;
            }

            public void setFlag(String flag) {
                this.flag = flag;
            }

            public int getSign() {
                return sign;
            }

            public void setSign(int sign) {
                this.sign = sign;
            }

            public int getMessageId() {
                return messageId;
            }

            public void setMessageId(int messageId) {
                this.messageId = messageId;
            }

            public int getPushNum() {
                return pushNum;
            }

            public void setPushNum(int pushNum) {
                this.pushNum = pushNum;
            }

            public String getConsult() {
                return consult;
            }

            public void setConsult(String consult) {
                this.consult = consult;
            }

            public String getTitle() {
                return title;
            }

            public void setTitle(String title) {
                this.title = title;
            }

            public String getContent() {
                return content;
            }

            public void setContent(String content) {
                this.content = content;
            }
        }
    }
}
