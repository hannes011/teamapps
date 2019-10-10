package org.teamapps.ux.component.charting.tree;

import java.util.List;

public class TreeGraphNode<RECORD> extends BaseTreeGraphNode<RECORD> {

	private TreeGraphNode<RECORD> parent;
	private boolean expanded;
	private boolean hasLazyChildren = false;
	private List<BaseTreeGraphNode<RECORD>> sideListNodes;

	public TreeGraphNode<RECORD> getParent() {
		return parent;
	}

	public TreeGraphNode<RECORD> setParent(TreeGraphNode<RECORD> parent) {
		this.parent = parent;
		return this;
	}

	public boolean isExpanded() {
		return expanded;
	}

	public TreeGraphNode<RECORD> setExpanded(boolean expanded) {
		this.expanded = expanded;
		return this;
	}

	public boolean isHasLazyChildren() {
		return hasLazyChildren;
	}

	public TreeGraphNode<RECORD> setHasLazyChildren(boolean hasLazyChildren) {
		this.hasLazyChildren = hasLazyChildren;
		return this;
	}

	public List<BaseTreeGraphNode<RECORD>> getSideListNodes() {
		return sideListNodes;
	}

	public TreeGraphNode<RECORD> setSideListNodes(List<BaseTreeGraphNode<RECORD>> sideListNodes) {
		this.sideListNodes = sideListNodes;
		return this;
	}
}