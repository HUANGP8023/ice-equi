package com.ice.utils;

import java.util.List;

import lombok.Getter;
import lombok.Setter;

/**
 * @author HUANGP
 * @date 2018年2月27日
 * @des 树形结构固定属性
 * @param <T> 自定义节点属性实体
 */
@Setter @Getter
public class BaseNode<T> implements java.io.Serializable {
	
    private static final long serialVersionUID = -2721191232926604726L;
   
    protected int id;
    protected int parentId;
    protected String nodeName;
    protected int level;
    protected int sort;
    protected boolean isLeaf;
    protected int rootId;
    protected T parent;
    protected List<T> child;
    
}