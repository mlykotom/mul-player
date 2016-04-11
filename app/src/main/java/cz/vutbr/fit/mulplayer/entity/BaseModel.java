package cz.vutbr.fit.mulplayer.entity;

/**
 * @author mlyko
 * @since 08.04.2016
 */
public class BaseModel {
    protected long mId;

    public BaseModel(long id) {
        mId = id;
    }

    public long getId() {
        return mId;
    }

    public void setId(long id) {
        mId = id;
    }
}
