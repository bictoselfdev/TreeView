package com.example.treeview.treeView

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.treeview.R
import com.example.treeview.databinding.ItemNodeBinding

class TreeNodeAdapter(private var itemList: ArrayList<TreeNode>) : RecyclerView.Adapter<TreeNodeAdapter.RecyclerViewHolder?>() {
    private lateinit var binding: ItemNodeBinding

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerViewHolder {
        binding = ItemNodeBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return RecyclerViewHolder(binding)
    }

    override fun onBindViewHolder(holder: RecyclerViewHolder, position: Int) {
        val treeNode = itemList[position]
        holder.updateViewHolder(treeNode)
    }

    override fun getItemCount(): Int {
        return itemList.size
    }

    fun expand(treeNode: TreeNode) {
        if (treeNode.isLeaf()) return
        if (treeNode.isExpand()) return

        val startPosition = itemList.indexOf(treeNode) + 1
        val itemCount = addChildNodes(treeNode, startPosition)
        notifyItemRangeInserted(startPosition, itemCount)

        val position = itemList.indexOf(treeNode)
        notifyItemChanged(position)
    }

    fun expandAll() {
        val tempList = ArrayList<TreeNode>()
        tempList.addAll(itemList)

        for (treeNode in tempList) {
            if (treeNode.isLeaf()) continue
            if (treeNode.isExpand()) continue

            val startPosition = itemList.indexOf(treeNode) + 1
            val itemCount = addAllChildNodes(treeNode, startPosition)
            notifyItemRangeInserted(startPosition, itemCount)

            val position = itemList.indexOf(treeNode)
            notifyItemChanged(position)
        }
    }

    fun collapse(treeNode: TreeNode) {
        if (treeNode.isLeaf()) return
        if (!treeNode.isExpand()) return

        val startPosition = itemList.indexOf(treeNode) + 1
        val itemCount = removeChildNodes(treeNode, true)
        notifyItemRangeRemoved(startPosition, itemCount)

        val position = itemList.indexOf(treeNode)
        notifyItemChanged(position)
    }

    fun collapseAll() {
        val tempList = ArrayList<TreeNode>()
        tempList.addAll(itemList)

        for (treeNode in tempList) {
            if (treeNode.isLeaf()) continue
            if (!treeNode.isExpand()) continue

            val startPosition = itemList.indexOf(treeNode) + 1
            val itemCount = removeAllChildNodes(treeNode)
            notifyItemRangeRemoved(startPosition, itemCount)

            val position = itemList.indexOf(treeNode)
            notifyItemChanged(position)
        }
    }

    fun setSelect(treeNode: TreeNode, isChecked: Boolean) {

        treeNode.setCheck(isChecked)

        val startPosition = itemList.indexOf(treeNode) + 1
        val itemCount = selectChildNodes(treeNode, isChecked)
        notifyItemRangeChanged(startPosition, itemCount)

        if (!treeNode.isRoot()) {
            selectParentNodes(treeNode)
        }
    }

    fun getSelect(treeNode: TreeNode, isChecked: Boolean) {

        treeNode.setCheck(isChecked)

        val startPosition = itemList.indexOf(treeNode) + 1
        val itemCount = selectChildNodes(treeNode, isChecked)
        notifyItemRangeChanged(startPosition, itemCount)

        if (!treeNode.isRoot()) {
            selectParentNodes(treeNode)
        }
    }

    fun selectAll() {
        for ((index, treeNode) in itemList.withIndex()) {
            if (treeNode.isRoot()) {
                setSelect(treeNode, true)
                notifyItemChanged(index)
            }
        }
    }

    fun unselectAll() {
        for ((index, treeNode) in itemList.withIndex()) {
            if (treeNode.isRoot()) {
                setSelect(treeNode, false)
                notifyItemChanged(index)
            }
        }
    }

    fun getSelectedNodes(): ArrayList<TreeNode> {
        val selectedNodes = ArrayList<TreeNode>()

        itemList.forEach { treeNode ->
            if (treeNode.isRoot()) {
                if (treeNode.isCheck()) {
                    selectedNodes.add(treeNode)
                }
                selectedNodes.addAll(getSelectChildNodes(treeNode))
            }
        }

        return selectedNodes
    }

    private fun addChildNodes(parent: TreeNode, startPosition: Int): Int {

        var addChildCount = 0
        parent.children.forEach { child ->

            itemList.add(startPosition + addChildCount, child)
            addChildCount++

            if (!child.isLeaf() && child.isExpand()) {
                addChildCount += addChildNodes(child, startPosition + addChildCount)
            }
        }

        parent.setExpand(true)

        return addChildCount
    }

    private fun addAllChildNodes(parent: TreeNode, startPosition: Int): Int {

        var addChildCount = 0
        parent.children.forEach { child ->

            itemList.add(startPosition + addChildCount, child)
            addChildCount++

            if (!child.isLeaf()) {
                addChildCount += addAllChildNodes(child, startPosition + addChildCount)
            }
        }

        parent.setExpand(true)

        return addChildCount
    }

    private fun removeChildNodes(parent: TreeNode, isChangedExpand: Boolean): Int {

        var removeChildCount = 0
        parent.children.forEach { child ->

            itemList.remove(child)
            removeChildCount++

            if (!child.isLeaf() && child.isExpand()) {
                removeChildCount += removeChildNodes(child, false)
            }
        }

        if (isChangedExpand) parent.setExpand(false)

        return removeChildCount
    }

    private fun removeAllChildNodes(parent: TreeNode): Int {

        var removeChildCount = 0
        parent.children.forEach { child ->

            itemList.remove(child)
            removeChildCount++

            if (!child.isLeaf() && child.isExpand()) {
                removeChildCount += removeAllChildNodes(child)
            }
        }

        parent.setExpand(false)

        return removeChildCount
    }

    private fun selectChildNodes(parent: TreeNode, isChecked: Boolean): Int {

        var selectChildCount = 0
        parent.children.forEach { child ->

            child.setCheck(isChecked)

            if (parent.isExpand()) {
                selectChildCount++
            }

            if (!child.isLeaf()) {
                selectChildCount += selectChildNodes(child, isChecked)
            }
        }

        return selectChildCount
    }

    private fun selectParentNodes(treeNode: TreeNode) {

        var isUnCheckAll = true
        treeNode.parent?.let { parent ->
            for (child in parent.children) {
                if (child.isCheck()) {
                    isUnCheckAll = false
                    break
                }
            }

            if (isUnCheckAll) {
                parent.setCheck(false)
            } else {
                parent.setCheck(true)
            }

            val position = itemList.indexOf(parent)
            notifyItemChanged(position)

            selectParentNodes(parent)
        }
    }

    private fun getSelectChildNodes(parent: TreeNode): ArrayList<TreeNode> {

        val selectedNodes = ArrayList<TreeNode>()

        parent.children.forEach { child ->

            if (child.isCheck()) {
                selectedNodes.add(child)
            }

            if (!child.isLeaf()) {
                selectedNodes.addAll(getSelectChildNodes(child))
            }
        }

        return selectedNodes
    }

    inner class RecyclerViewHolder(private val binding: ItemNodeBinding) : RecyclerView.ViewHolder(binding.root) {
        fun updateViewHolder(treeNode: TreeNode) {

            setPaddingStart(treeNode)

            if (treeNode.isExpand()) {
                binding.ivExpand.setImageResource(R.drawable.ic_minus)
            } else {
                binding.ivExpand.setImageResource(R.drawable.ic_plus)
            }

            if (treeNode.isLeaf()) {
                binding.ivExpand.visibility = View.INVISIBLE
            } else {
                binding.ivExpand.visibility = View.VISIBLE
                binding.ivExpand.setOnClickListener {

                    if (treeNode.isExpand()) {
                        collapse(treeNode)
                    } else {
                        expand(treeNode)
                    }
                }
            }

            binding.cbName.text = treeNode.getName()
            binding.cbName.isChecked = treeNode.isCheck()
            binding.cbName.setOnClickListener {

                setSelect(treeNode, binding.cbName.isChecked)
            }
        }

        private fun setPaddingStart(node: TreeNode) {
            val depth = node.getDepth()
            itemView.run {
                setPadding(DEPTH_PADDING * depth, paddingTop, paddingRight, paddingBottom)
            }
        }
    }

    companion object {
        private const val DEPTH_PADDING = 60
    }
}