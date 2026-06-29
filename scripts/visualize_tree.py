from ete3 import Tree, TreeStyle, faces, TextFace
import sys
from pathlib import Path

def calculate_scale_factor(tree, img_width=1):
    """根据图像宽度和树的最大分支长度自动计算缩放因子"""
    max_branch_length = max(node.dist for node in tree.traverse() if node.dist is not None)
    # 假设我们想要最大分支长度占据图像宽度的大约100%
    scale_factor = (img_width * 1.0) / max_branch_length if max_branch_length > 0 else 1.0
    return scale_factor

def adjust_branch_lengths(tree, scale_factor=1.0):
    """调整树的分支长度，scale_factor用于缩放分支长度"""
    for node in tree.traverse():
        if node.dist is not None:
            node.dist *= scale_factor  # 调整分支长度
    return tree

def add_bootstrap_values(tree):
    """为树上的节点添加Bootstrap值（如果有）"""
    for node in tree.traverse():
        if hasattr(node, "support") and node.support:  # 检查是否有支持度信息
            support_face = TextFace(f"{node.support:.0f}%", fsize=8)
            node.add_face(support_face, column=0, position="branch-top")

def get_custom_tree_style():
    """获取自定义树风格"""
    ts = TreeStyle()
    ts.show_leaf_name = True  # 显示叶子节点名称
    ts.scale = 250  # 设置比例尺
    ts.branch_vertical_margin = 10  # 增加分支间的垂直间距，使图更清晰
    return ts

def visualize_tree(newick_file, output_image):
    tree = Tree(newick_file)

    # 计算缩放因子
    scale_factor = calculate_scale_factor(tree)
    
    # 调整分支长度
    adjusted_tree = adjust_branch_lengths(tree, scale_factor)
    
    # 添加Bootstrap值
    add_bootstrap_values(adjusted_tree)
    
    # 应用自定义样式
    ts = get_custom_tree_style()
    
    # 渲染图像
    adjusted_tree.render(output_image, w=1200, units='px', dpi=300, tree_style=ts)

if __name__ == "__main__":
    if len(sys.argv) != 3:
        print("Usage: python visualize_tree.py <newick_file_path> <output_image_path>")
        sys.exit(1)

    newick_file = sys.argv[1]
    output_image = sys.argv[2]

    # 检查文件是否存在
    if not Path(newick_file).is_file():
        print(f"Error: The file {newick_file} does not exist.")
        sys.exit(1)

    visualize_tree(newick_file, output_image)