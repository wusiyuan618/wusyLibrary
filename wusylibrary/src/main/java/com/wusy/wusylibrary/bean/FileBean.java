package com.wusy.wusylibrary.bean;

/**
 * @创建人 chaychan
 * @创建时间 2016/7/23  18:11
 * @描述 文件,可以是文档、apk、压缩包
 */
public class FileBean {
    /** 文件的路径*/
    private String path;
    /**文件图片资源的id，drawable或mipmap文件中已经存放doc、xml、xls等文件的图片*/
    private int iconId;
    private boolean isClick;

    public FileBean(String path, int iconId) {
        this.path = path;
        this.iconId = iconId;
    }

    @Override
    public String toString() {
        return "FileBean{" +
                "path='" + path + '\'' +
                ", iconId=" + iconId +
                ", isClick=" + isClick +
                '}';
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public int getIconId() {
        return iconId;
    }

    public void setIconId(int iconId) {
        this.iconId = iconId;
    }

    public boolean isClick() {
        return isClick;
    }

    public void setClick(boolean click) {
        isClick = click;
    }
}
