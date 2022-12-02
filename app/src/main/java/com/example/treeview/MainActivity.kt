package com.example.treeview

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.example.treeview.databinding.ActivityMainBinding
import com.example.treeview.treeView.TreeNode
import com.example.treeview.treeView.TreeNodeAdapter

class MainActivity : AppCompatActivity() {

    lateinit var binding: ActivityMainBinding

    private var nodes = ArrayList<TreeNode>()
    private var adapter: TreeNodeAdapter? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)

        nodes.addAll(getSampleTreeNodes())
        adapter = TreeNodeAdapter(nodes)
        binding.rvTreeView.adapter = adapter

        binding.btnExpandAll.setOnClickListener {
            adapter?.expandAll()
        }

        binding.btnCollapseAll.setOnClickListener {
            adapter?.collapseAll()
        }

        binding.btnSelectAll.setOnClickListener {
            adapter?.selectAll()
        }

        binding.btnUnSelectAll.setOnClickListener {
            adapter?.unselectAll()
        }

        binding.btnSelectedNodes.setOnClickListener {
            val selectedNodes = adapter?.getSelectedNodes()
            selectedNodes?.forEach { treeNode ->
                System.err.println("treeNode $treeNode")
            }

            Toast.makeText(this, "selectedNodes count : ${selectedNodes?.size}", Toast.LENGTH_SHORT).show()
        }
    }

    private fun getSampleTreeNodes(): ArrayList<TreeNode> {

        val treeNode1 = TreeNode("Node_4")
            .addChild(TreeNode("Node_1"))
            .addChild(TreeNode("Node_2"))
            .addChild(TreeNode("Node_3"))
            .addChild(TreeNode("Node_4"))

        val treeNode2 = TreeNode("Node_3")
            .addChild(TreeNode("Node_1"))
            .addChild(TreeNode("Node_2"))
            .addChild(TreeNode("Node_3"))
            .addChild(treeNode1)

        val treeNode3 = TreeNode("Node_1")
            .addChild(TreeNode("Node_1"))
            .addChild(TreeNode("Node_2"))
            .addChild(TreeNode("Node_3"))

        val treeNode4 = TreeNode("Node_1")
            .addChild(TreeNode("Node_1"))

        val rootNode1 = TreeNode("Root_1")
            .addChild(TreeNode("Node_1"))
            .addChild(TreeNode("Node_2"))

        val rootNode2 = TreeNode("Root_2")
            .addChild(TreeNode("Node_1"))
            .addChild(TreeNode("Node_2"))
            .addChild(treeNode2)

        val rootNode3 = TreeNode("Root_3")

        val rootNode4 = TreeNode("Root_4")
            .addChild(treeNode3)
            .addChild(treeNode4)

        return arrayListOf(rootNode1, rootNode2, rootNode3, rootNode4)
    }
}