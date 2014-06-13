/*
 * JTreeCPInfo.java    August 15, 2007, 4:12 PM
 *
 * Copyright  2007, FreeInternals.org. All rights reserved.
 * Use is subject to license terms.
 */
package org.freeinternals.javaclassviewer.ui;

import javax.swing.tree.DefaultMutableTreeNode;

import org.freeinternals.format.classfile.constpool.AbstractCPInfo;
import org.freeinternals.format.classfile.constpool.ConstantClassInfo;
import org.freeinternals.format.classfile.constpool.ConstantDoubleInfo;
import org.freeinternals.format.classfile.constpool.ConstantFieldrefInfo;
import org.freeinternals.format.classfile.constpool.ConstantFloatInfo;
import org.freeinternals.format.classfile.constpool.ConstantIntegerInfo;
import org.freeinternals.format.classfile.constpool.ConstantInterfaceMethodrefInfo;
import org.freeinternals.format.classfile.constpool.ConstantInvokeDynamicInfo;
import org.freeinternals.format.classfile.constpool.ConstantLongInfo;
import org.freeinternals.format.classfile.constpool.ConstantMethodHandleInfo;
import org.freeinternals.format.classfile.constpool.ConstantMethodTypeInfo;
import org.freeinternals.format.classfile.constpool.ConstantMethodrefInfo;
import org.freeinternals.format.classfile.constpool.ConstantNameAndTypeInfo;
import org.freeinternals.format.classfile.constpool.ConstantStringInfo;
import org.freeinternals.format.classfile.constpool.ConstantUtf8Info;

/**
 * 
 * @author Amos Shi
 * @since JDK 6.0
 */
final class JTreeCPInfo {

	private JTreeCPInfo() {
	}

	/**
	 * 负责生成const pool节点
	 * 
	 * @param rootNode
	 * @param cp_info
	 * @throws InvalidTreeNodeException
	 */
	public static void generateTreeNode(final DefaultMutableTreeNode rootNode,
			final AbstractCPInfo cp_info) throws InvalidTreeNodeException {
		if (cp_info == null) {
			return;
		}

		final short tag = cp_info.getTag();
		rootNode.add(new DefaultMutableTreeNode(new JTreeNodeClassComponent(
				cp_info.getStartPos(), 1, "tag: " + tag)));

		switch (tag) {
		case AbstractCPInfo.CONSTANT_Utf8:
			JTreeCPInfo.generateTreeNode(rootNode, (ConstantUtf8Info) cp_info);
			break;

		case AbstractCPInfo.CONSTANT_Integer:
			JTreeCPInfo.generateTreeNode(rootNode,
					(ConstantIntegerInfo) cp_info);
			break;

		case AbstractCPInfo.CONSTANT_Float:
			JTreeCPInfo.generateTreeNode(rootNode, (ConstantFloatInfo) cp_info);
			break;

		case AbstractCPInfo.CONSTANT_Long:
			JTreeCPInfo.generateTreeNode(rootNode, (ConstantLongInfo) cp_info);
			break;

		case AbstractCPInfo.CONSTANT_Double:
			JTreeCPInfo
					.generateTreeNode(rootNode, (ConstantDoubleInfo) cp_info);
			break;

		case AbstractCPInfo.CONSTANT_Class:
			JTreeCPInfo.generateTreeNode(rootNode, (ConstantClassInfo) cp_info);
			break;

		case AbstractCPInfo.CONSTANT_String:
			JTreeCPInfo
					.generateTreeNode(rootNode, (ConstantStringInfo) cp_info);
			break;

		case AbstractCPInfo.CONSTANT_Fieldref:
			JTreeCPInfo.generateTreeNode(rootNode,
					(ConstantFieldrefInfo) cp_info);
			break;

		case AbstractCPInfo.CONSTANT_Methodref:
			JTreeCPInfo.generateTreeNode(rootNode,
					(ConstantMethodrefInfo) cp_info);
			break;

		case AbstractCPInfo.CONSTANT_InterfaceMethodref:
			JTreeCPInfo.generateTreeNode(rootNode,
					(ConstantInterfaceMethodrefInfo) cp_info);
			break;

		case AbstractCPInfo.CONSTANT_NameAndType:
			JTreeCPInfo.generateTreeNode(rootNode,
					(ConstantNameAndTypeInfo) cp_info);
			break;

		case AbstractCPInfo.CONSTANT_MethodType:
			JTreeCPInfo.generateTreeNode(rootNode,
					(ConstantMethodTypeInfo) cp_info);
			break;

		case AbstractCPInfo.CONSTANT_MethodHandle:
			JTreeCPInfo.generateTreeNode(rootNode,
					(ConstantMethodHandleInfo) cp_info);
			break;

		case AbstractCPInfo.CONSTANT_InvokeDynamic:
			JTreeCPInfo.generateTreeNode(rootNode,
					(ConstantInvokeDynamicInfo) cp_info);
			break;

		default:
			// TODO: Add exception
			break;
		}
	}

	private static void generateTreeNode(final DefaultMutableTreeNode rootNode,
			final ConstantUtf8Info utf8Info) throws InvalidTreeNodeException {
		final int startPos = utf8Info.getStartPos();
		final int bytesLength = utf8Info.getBytesLength();

		rootNode.add(new DefaultMutableTreeNode(new JTreeNodeClassComponent(
				startPos + 1, 2, "length: " + bytesLength)));
		rootNode.add(new DefaultMutableTreeNode(new JTreeNodeClassComponent(
				startPos + 3, bytesLength, "bytes: " + utf8Info.getValue())));
	}

	private static void generateTreeNode(final DefaultMutableTreeNode rootNode,
			final ConstantIntegerInfo integerInfo)
			throws InvalidTreeNodeException {
		rootNode.add(new DefaultMutableTreeNode(new JTreeNodeClassComponent(
				integerInfo.getStartPos() + 1, 4, "bytes: "
						+ integerInfo.getValue())));
	}

	private static void generateTreeNode(final DefaultMutableTreeNode rootNode,
			final ConstantFloatInfo floatInfo) throws InvalidTreeNodeException {
		rootNode.add(new DefaultMutableTreeNode(new JTreeNodeClassComponent(
				floatInfo.getStartPos() + 1, 4, "bytes: "
						+ floatInfo.getValue())));
	}

	private static void generateTreeNode(final DefaultMutableTreeNode rootNode,
			final ConstantLongInfo longInfo) throws InvalidTreeNodeException {
		final int startPos = longInfo.getStartPos();

		rootNode.add(new DefaultMutableTreeNode(new JTreeNodeClassComponent(
				startPos + 1, 4, "high_bytes")));
		rootNode.add(new DefaultMutableTreeNode(new JTreeNodeClassComponent(
				startPos + 5, 4, "low_bytes")));
	}

	private static void generateTreeNode(final DefaultMutableTreeNode rootNode,
			final ConstantDoubleInfo doubleInfo)
			throws InvalidTreeNodeException {
		final int startPos = doubleInfo.getStartPos();

		rootNode.add(new DefaultMutableTreeNode(new JTreeNodeClassComponent(
				startPos + 1, 4, "high_bytes")));
		rootNode.add(new DefaultMutableTreeNode(new JTreeNodeClassComponent(
				startPos + 5, 4, "low_bytes")));
	}

	private static void generateTreeNode(final DefaultMutableTreeNode rootNode,
			final ConstantClassInfo classInfo) throws InvalidTreeNodeException {
		rootNode.add(new DefaultMutableTreeNode(new JTreeNodeClassComponent(
				classInfo.getStartPos() + 1, 2, "name_index: "
						+ classInfo.getNameIndex())));
	}

	private static void generateTreeNode(final DefaultMutableTreeNode rootNode,
			final ConstantStringInfo stringInfo)
			throws InvalidTreeNodeException {
		rootNode.add(new DefaultMutableTreeNode(new JTreeNodeClassComponent(
				stringInfo.getStartPos() + 1, 2, "string_index: "
						+ stringInfo.getStringIndex())));
	}

	private static void generateTreeNode(final DefaultMutableTreeNode rootNode,
			final ConstantFieldrefInfo fieldrefInfo)
			throws InvalidTreeNodeException {
		final int startPos = fieldrefInfo.getStartPos();

		rootNode.add(new DefaultMutableTreeNode(
				new JTreeNodeClassComponent(startPos + 1, 2, "class_index: "
						+ fieldrefInfo.getClassIndex())));
		rootNode.add(new DefaultMutableTreeNode(new JTreeNodeClassComponent(
				startPos + 3, 2, "name_and_type_index: "
						+ fieldrefInfo.getNameAndTypeIndex())));
	}

	private static void generateTreeNode(final DefaultMutableTreeNode rootNode,
			final ConstantMethodrefInfo methodrefInfo)
			throws InvalidTreeNodeException {
		final int startPos = methodrefInfo.getStartPos();

		rootNode.add(new DefaultMutableTreeNode(new JTreeNodeClassComponent(
				startPos + 1, 2, "class_index: "
						+ methodrefInfo.getClassIndex())));
		rootNode.add(new DefaultMutableTreeNode(new JTreeNodeClassComponent(
				startPos + 3, 2, "name_and_type_index: "
						+ methodrefInfo.getNameAndTypeIndex())));
	}

	private static void generateTreeNode(final DefaultMutableTreeNode rootNode,
			final ConstantInterfaceMethodrefInfo interfaceMethodrefInfo)
			throws InvalidTreeNodeException {
		final int startPos = interfaceMethodrefInfo.getStartPos();

		rootNode.add(new DefaultMutableTreeNode(new JTreeNodeClassComponent(
				startPos + 1, 2, "class_index: "
						+ interfaceMethodrefInfo.getClassIndex())));
		rootNode.add(new DefaultMutableTreeNode(new JTreeNodeClassComponent(
				startPos + 3, 2, "name_and_type_index: "
						+ interfaceMethodrefInfo.getNameAndTypeIndex())));
	}

	private static void generateTreeNode(final DefaultMutableTreeNode rootNode,
			final ConstantNameAndTypeInfo nameAndTypeInfo)
			throws InvalidTreeNodeException {
		final int startPos = nameAndTypeInfo.getStartPos();

		rootNode.add(new DefaultMutableTreeNode(new JTreeNodeClassComponent(
				startPos + 1, 2, "name_index: "
						+ nameAndTypeInfo.getNameIndex())));
		rootNode.add(new DefaultMutableTreeNode(new JTreeNodeClassComponent(
				startPos + 3, 2, "descriptor_index: "
						+ nameAndTypeInfo.getDescriptorIndex())));
	}

	private static void generateTreeNode(final DefaultMutableTreeNode rootNode,
			final ConstantMethodTypeInfo methodTypeInfo)
			throws InvalidTreeNodeException {
		final int startPos = methodTypeInfo.getStartPos();

		rootNode.add(new DefaultMutableTreeNode(new JTreeNodeClassComponent(
				startPos + 1, 2, "descriptor_index: "
						+ methodTypeInfo.getDescriptorIndex())));
	}

	private static void generateTreeNode(final DefaultMutableTreeNode rootNode,
			final ConstantMethodHandleInfo methodHandleInfo)
			throws InvalidTreeNodeException {
		final int startPos = methodHandleInfo.getStartPos();

		rootNode.add(new DefaultMutableTreeNode(new JTreeNodeClassComponent(
				startPos + 1, 1, "reference_kind: "
						+ methodHandleInfo.getReferenceKind())));
		rootNode.add(new DefaultMutableTreeNode(new JTreeNodeClassComponent(
				startPos + 2, 2, "reference_index: "
						+ methodHandleInfo.getReferenceIndex())));
	}

	private static void generateTreeNode(final DefaultMutableTreeNode rootNode,
			final ConstantInvokeDynamicInfo invokeDynamicInfo)
			throws InvalidTreeNodeException {
		final int startPos = invokeDynamicInfo.getStartPos();

		rootNode.add(new DefaultMutableTreeNode(new JTreeNodeClassComponent(
				startPos + 1, 2, "name_index: "
						+ invokeDynamicInfo.getNameIndex())));
		rootNode.add(new DefaultMutableTreeNode(new JTreeNodeClassComponent(
				startPos + 3, 2, "descriptor_index: "
						+ invokeDynamicInfo.getDescriptorIndex())));
	}
}
