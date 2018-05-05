package com.ice.utils;

import org.apache.commons.collections4.CollectionUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * @author HUANGP
 * @date 2018年2月27日
 * @des 泛型树创建
 * @param <T> 自定义节点属性字段实体
 */
@SuppressWarnings({ "unchecked", "rawtypes" })
public class TreeBuilder<T extends BaseNode> {

	public List<T> buildListToTree(List<T> dirs) {
		List<T> roots = findRoots(dirs);
		List<T> notRoots = (List<T>) CollectionUtils.subtract(dirs, roots);
		for (T root : roots) {
			root.setChild(findChildren(root, notRoots));
		}
		return roots;
	}

	private List<T> findRoots(List<T> allNodes) {
		List<T> results = new ArrayList<T>();
		for (T node : allNodes) {
			boolean isRoot = true;
			for (T comparedOne : allNodes) {
				if (node.getParentId() == comparedOne.getId()) {
					isRoot = false;
					break;
				}
			}
			if (isRoot) {
				node.setLevel(0);
				results.add(node);
				node.setRootId(node.getId());
			}
		}
		return results;
	}

	private List<T> findChildren(T root, List<T> allNodes) {
		List<T> children = new ArrayList<T>();

		for (T comparedOne : allNodes) {
			if (comparedOne.getParentId() == root.getId()) {
				comparedOne.setParent(root);
				comparedOne.setLevel(root.getLevel() + 1);
				children.add(comparedOne);
			}
		}
		List<T> notChildren = (List<T>) CollectionUtils.subtract(allNodes, children);
		for (T child : children) {
			List<T> tmpChildren = findChildren(child, notChildren);
			if (tmpChildren == null || tmpChildren.size() < 1) {
				child.setLeaf(true);
			} else {
				child.setLeaf(false);
			}
			child.setChild(tmpChildren);
		}
		return children;
	}

}