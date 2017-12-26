package com.wangweijun.structure.data.model;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.annotation.Id;

/**
 * Created by wangweijun1 on 2017/11/27.
 */

@Entity
public class Contributor {
    @Id(autoincrement = true)
    private Long id;

    public String login;
    public int contributions;
    public String avatar_url;

    @Generated(hash = 505775570)
    public Contributor() {
    }

    @Generated(hash = 1405543109)
    public Contributor(Long id, String login, int contributions, String avatar_url) {
        this.id = id;
        this.login = login;
        this.contributions = contributions;
        this.avatar_url = avatar_url;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public int getContributions() {
        return contributions;
    }

    public void setContributions(int contributions) {
        this.contributions = contributions;
    }

    public String getAvatar_url() {
        return avatar_url;
    }

    public void setAvatar_url(String avatar_url) {
        this.avatar_url = avatar_url;
    }

    @Override
    public String toString() {
        return "login:"+login+", contributions:"+contributions;
    }
}
