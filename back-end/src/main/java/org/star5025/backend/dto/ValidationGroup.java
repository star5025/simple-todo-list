package org.star5025.backend.dto;

/**
 * 给DTO中的Validated注解加上先后循序
 * 顺序为1>2>3>4>5
 */
public class ValidationGroup {

    public interface Group1{}

    public interface Group2{}

    public interface Group3{}

    //这后面的可能用不到
    public interface Group4{}

    public interface Group5{}
}
