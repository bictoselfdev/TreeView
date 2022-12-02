package com.example.treeview.treeView

class TreeNode(private val name: String) {

    var parent: TreeNode? = null
    var children = ArrayList<TreeNode>()

    private var isCheck = false
    private var isExpand = false
    private var depth = -1

    fun getName(): String {
        return name
    }

    fun isRoot(): Boolean {
        return parent == null
    }

    fun isLeaf(): Boolean {
        return children.isEmpty()
    }

    fun isExpand(): Boolean {
        return isExpand
    }

    fun isCheck(): Boolean {
        return isCheck
    }

    fun setExpand(isExpanded: Boolean) {
        isExpand = isExpanded
    }

    fun setCheck(isChecked: Boolean) {
        isCheck = isChecked
    }

    fun getDepth(): Int {
        if (isRoot()) depth = 0
        else if (depth == -1) depth = parent!!.depth + 1

        return depth
    }

    fun addChild(child: TreeNode): TreeNode {
        children.add(child)
        child.parent = this

        return this
    }

    override fun toString(): String {
        return "Node : Name(${name}), isRoot(${isRoot()}), isLeaf(${isLeaf()}), isExpand(${isExpand()}), isCheck(${isCheck()}), Child size(${children.size})"
    }
}