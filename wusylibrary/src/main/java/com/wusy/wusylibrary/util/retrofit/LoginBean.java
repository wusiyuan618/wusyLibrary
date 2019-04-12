package com.wusy.wusylibrary.util.retrofit;
public class LoginBean {
    /**
     * result : LTgMDgwMzcMTANjgNjQNTA
     * str : 登录成功
     * param : {"isSupervise":"false","isSuper":"false","vlidateCode":"6be0a2ab663b78318da09997742ee14e","pname":"罗勇","orgPid":"8892851072426855058","roles":"100099","piconId":"3857694345683837630","pid":"2782811058791596581","userName":"luoy","layout":"2","orgCorpId":"407223454379623895","accountBindCode":"202","messagePageConfig":"0","depId":"7076616276001509306","dname":"产品部","isSuperviseManager":"false","accountBinded":"true","cid":"0"}
     */

    private String result;
    private String str;
    private ParamBean param;

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }

    public String getStr() {
        return str;
    }

    public void setStr(String str) {
        this.str = str;
    }

    public ParamBean getParam() {
        return param;
    }

    public void setParam(ParamBean param) {
        this.param = param;
    }

    public static class ParamBean {
        /**
         * isSupervise : false
         * isSuper : false
         * vlidateCode : 6be0a2ab663b78318da09997742ee14e
         * pname : 罗勇
         * orgPid : 8892851072426855058
         * roles : 100099
         * piconId : 3857694345683837630
         * pid : 2782811058791596581
         * userName : luoy
         * layout : 2
         * orgCorpId : 407223454379623895
         * accountBindCode : 202
         * messagePageConfig : 0
         * depId : 7076616276001509306
         * dname : 产品部
         * isSuperviseManager : false
         * accountBinded : true
         * cid : 0
         */

        private String isSupervise;
        private String isSuper;
        private String vlidateCode;
        private String pname;
        private String orgPid;
        private String roles;
        private String piconId;
        private String pid;
        private String userName;
        private String layout;
        private String orgCorpId;
        private String accountBindCode;
        private String messagePageConfig;
        private String depId;
        private String dname;
        private String isSuperviseManager;
        private String accountBinded;
        private String cid;

        public String getIsSupervise() {
            return isSupervise;
        }

        public void setIsSupervise(String isSupervise) {
            this.isSupervise = isSupervise;
        }

        public String getIsSuper() {
            return isSuper;
        }

        public void setIsSuper(String isSuper) {
            this.isSuper = isSuper;
        }

        public String getVlidateCode() {
            return vlidateCode;
        }

        public void setVlidateCode(String vlidateCode) {
            this.vlidateCode = vlidateCode;
        }

        public String getPname() {
            return pname;
        }

        public void setPname(String pname) {
            this.pname = pname;
        }

        public String getOrgPid() {
            return orgPid;
        }

        public void setOrgPid(String orgPid) {
            this.orgPid = orgPid;
        }

        public String getRoles() {
            return roles;
        }

        public void setRoles(String roles) {
            this.roles = roles;
        }

        public String getPiconId() {
            return piconId;
        }

        public void setPiconId(String piconId) {
            this.piconId = piconId;
        }

        public String getPid() {
            return pid;
        }

        public void setPid(String pid) {
            this.pid = pid;
        }

        public String getUserName() {
            return userName;
        }

        public void setUserName(String userName) {
            this.userName = userName;
        }

        public String getLayout() {
            return layout;
        }

        public void setLayout(String layout) {
            this.layout = layout;
        }

        public String getOrgCorpId() {
            return orgCorpId;
        }

        public void setOrgCorpId(String orgCorpId) {
            this.orgCorpId = orgCorpId;
        }

        public String getAccountBindCode() {
            return accountBindCode;
        }

        public void setAccountBindCode(String accountBindCode) {
            this.accountBindCode = accountBindCode;
        }

        public String getMessagePageConfig() {
            return messagePageConfig;
        }

        public void setMessagePageConfig(String messagePageConfig) {
            this.messagePageConfig = messagePageConfig;
        }

        public String getDepId() {
            return depId;
        }

        public void setDepId(String depId) {
            this.depId = depId;
        }

        public String getDname() {
            return dname;
        }

        public void setDname(String dname) {
            this.dname = dname;
        }

        public String getIsSuperviseManager() {
            return isSuperviseManager;
        }

        public void setIsSuperviseManager(String isSuperviseManager) {
            this.isSuperviseManager = isSuperviseManager;
        }

        public String getAccountBinded() {
            return accountBinded;
        }

        public void setAccountBinded(String accountBinded) {
            this.accountBinded = accountBinded;
        }

        public String getCid() {
            return cid;
        }

        public void setCid(String cid) {
            this.cid = cid;
        }

        @Override
        public String toString() {
            return "ParamBean{" +
                    "isSupervise='" + isSupervise + '\'' +
                    ", isSuper='" + isSuper + '\'' +
                    ", vlidateCode='" + vlidateCode + '\'' +
                    ", pname='" + pname + '\'' +
                    ", orgPid='" + orgPid + '\'' +
                    ", roles='" + roles + '\'' +
                    ", piconId='" + piconId + '\'' +
                    ", pid='" + pid + '\'' +
                    ", userName='" + userName + '\'' +
                    ", layout='" + layout + '\'' +
                    ", orgCorpId='" + orgCorpId + '\'' +
                    ", accountBindCode='" + accountBindCode + '\'' +
                    ", messagePageConfig='" + messagePageConfig + '\'' +
                    ", depId='" + depId + '\'' +
                    ", dname='" + dname + '\'' +
                    ", isSuperviseManager='" + isSuperviseManager + '\'' +
                    ", accountBinded='" + accountBinded + '\'' +
                    ", cid='" + cid + '\'' +
                    '}';
        }
    }

    @Override
    public String toString() {
        return "LoginBean{" +
                "result='" + result + '\'' +
                ", str='" + str + '\'' +
                ", param=" + param.toString() +
                '}';
    }
}