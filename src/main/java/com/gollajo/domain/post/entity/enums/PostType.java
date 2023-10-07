package com.gollajo.domain.post.entity.enums;

public enum PostType {
    STRING_OPTION(1),
    IMAGE_OPTION(2),
    ;

    private final int typeCode;

    PostType(int typeCode){
        this.typeCode = typeCode;
    }

    private int typeCode(){
        return typeCode;
    }

}
