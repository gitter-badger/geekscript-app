package model;

import javax.persistence.Entity;
import javax.persistence.Id;

/**
 * Created by sriram on 16/02/14.
 */
@Entity
public class Session {


    private String id;

    @Id
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
