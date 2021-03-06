/*
 * JTreeMethod.java    August 15, 2007, 6:03 PM
 *
 * Copyright 2007, FreeInternals.org. All rights reserved.
 * Use is subject to license terms.
 */
package org.freeinternals.javaclassviewer.ui;

import javax.swing.tree.DefaultMutableTreeNode;

import org.freeinternals.format.classfile.ClassFile;
import org.freeinternals.format.classfile.MethodInfo;
import org.freeinternals.format.classfile.attribute.AttributeInfo;

/**
 * 
 * @author Amos Shi
 * @since JDK 6.0
 */
final class JTreeMethod {

	private JTreeMethod() {
	}

	/**
	 * 负责生成左侧树中的方法节点，及其子节点
	 * 
	 * @param rootNode
	 * @param method_info
	 * @param classFile
	 * @throws InvalidTreeNodeException
	 */
	public static void generateTreeNode(final DefaultMutableTreeNode rootNode,
			final MethodInfo method_info, final ClassFile classFile)
			throws InvalidTreeNodeException {
		if (method_info == null) {
			return;
		}

		final int startPos = method_info.getStartPos();
		final int attributesCount = method_info.getAttributesCount();
		int cp_index;

		rootNode.add(new DefaultMutableTreeNode(new JTreeNodeClassComponent(
				startPos, 2, "access_flags: " + method_info.getAccessFlags()
						+ ", " + method_info.getModifiers())));
		cp_index = method_info.getNameIndex();
		rootNode.add(new DefaultMutableTreeNode(new JTreeNodeClassComponent(
				startPos + 2, 2, String.format("name_index: %d [%s]", cp_index,
						classFile.getCPDescription(cp_index)))));
		cp_index = method_info.getDescriptorIndex();
		rootNode.add(new DefaultMutableTreeNode(new JTreeNodeClassComponent(
				startPos + 4, 2, String.format("descriptor_index: %d [%s]",
						cp_index, classFile.getCPDescription(cp_index)))));
		rootNode.add(new DefaultMutableTreeNode(new JTreeNodeClassComponent(
				startPos + 6, 2, "attributes_count: " + attributesCount)));

		if (attributesCount > 0) {
			final AttributeInfo lastAttr = method_info
					.getAttribute(attributesCount - 1);
			final DefaultMutableTreeNode treeNodeAttr = new DefaultMutableTreeNode(
					new JTreeNodeClassComponent(startPos + 8,
							lastAttr.getStartPos() + lastAttr.getLength()
									- startPos - 8, "attributes"));

			DefaultMutableTreeNode treeNodeAttrItem;
			AttributeInfo attr;
			for (int i = 0; i < attributesCount; i++) {
				attr = method_info.getAttribute(i);
				treeNodeAttrItem = new DefaultMutableTreeNode(
						new JTreeNodeClassComponent(attr.getStartPos(),
								attr.getLength(), String.format("%d. %s",
										i + 1, attr.getName())));
				JTreeAttribute.generateTreeNode(treeNodeAttrItem, attr,
						classFile);
				treeNodeAttr.add(treeNodeAttrItem);
				treeNodeAttrItem = null;
			}
			rootNode.add(treeNodeAttr);
		}
	}
}
