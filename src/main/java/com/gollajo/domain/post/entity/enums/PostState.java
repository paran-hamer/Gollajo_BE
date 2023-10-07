package com.gollajo.domain.post.entity.enums;

public enum PostState {
    STATE_GENERATING(1),
    STATE_PROCEEDING(2),
    STATE_COMPLETE(3),
    STATE_EXPIRED_AND_NO_COMPLETE(4),
    STATE_EXPIRED_AND_COMPLETE(5)
    ;

    private final int stateCode;

    PostState(int stateCode){
        this.stateCode = stateCode;
    }

    public int stateCode(){
        return stateCode;
    }
}
