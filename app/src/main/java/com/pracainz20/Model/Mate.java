package com.pracainz20.Model;

import java.util.Date;

/**
 * Created by Grzechu on 20.03.2018.
 */

public class Mate extends User {

    private Date createDt;

    public Date getCreateDt() {
        return createDt;
    }

    public void setCreateDt(Date createDt) {
        this.createDt = createDt;
    }
}
