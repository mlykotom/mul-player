package cz.vutbr.fit.mulplayer.model;

/**
 * @author mlyko
 * @since 08.04.2016
 */
public class BaseModel {
    protected String mId;

    public BaseModel(String mId) {
        this.mId = mId;
    }

    public String getId() {
        return mId;
    }

    public void setId(String id) {
        mId = id;
    }
}
