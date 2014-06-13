/*
 * JTreeAttribute.java   August 15, 2007, 5:58 PM
 *
 * Copyright  2007, FreeInternals.org. All rights reserved.
 * Use is subject to license terms.
 */
package org.freeinternals.javaclassviewer.ui;

import javax.swing.tree.DefaultMutableTreeNode;

import org.freeinternals.format.FileFormatException;
import org.freeinternals.format.classfile.ClassFile;
import org.freeinternals.format.classfile.attribute.AttributeAnnotation_annotation;
import org.freeinternals.format.classfile.attribute.AttributeCode;
import org.freeinternals.format.classfile.attribute.AttributeConstantValue;
import org.freeinternals.format.classfile.attribute.AttributeDeprecated;
import org.freeinternals.format.classfile.attribute.AttributeExceptions;
import org.freeinternals.format.classfile.attribute.AttributeExtended;
import org.freeinternals.format.classfile.attribute.AttributeInfo;
import org.freeinternals.format.classfile.attribute.AttributeInnerClasses;
import org.freeinternals.format.classfile.attribute.AttributeLineNumberTable;
import org.freeinternals.format.classfile.attribute.AttributeLocalVariableTable;
import org.freeinternals.format.classfile.attribute.AttributeLocalVariableTable_base;
import org.freeinternals.format.classfile.attribute.AttributeRuntimeAnnotations;
import org.freeinternals.format.classfile.attribute.AttributeSignature;
import org.freeinternals.format.classfile.attribute.AttributeSourceFile;
import org.freeinternals.format.classfile.attribute.AttributeSynthetic;

/**
 * 
 * @author Amos Shi
 * @since JDK 6.0
 */
class JTreeAttribute {

	private JTreeAttribute() {
	}

	/**
	 * 负责生成class/field/method/Attribute的下属Attribute节点
	 * 
	 * @param rootNode
	 * @param attribute_info
	 * @param classFile
	 * @throws InvalidTreeNodeException
	 */
	public static void generateTreeNode(final DefaultMutableTreeNode rootNode,
			final AttributeInfo attribute_info, final ClassFile classFile)
			throws InvalidTreeNodeException {

		if (attribute_info == null) {
			return;
		}

		final int startPos = attribute_info.getStartPos();

		rootNode.add(new DefaultMutableTreeNode(new JTreeNodeClassComponent(
				startPos, 2, "attribute_name_index: "
						+ attribute_info.getNameIndex() + ", name="
						+ attribute_info.getName())));
		rootNode.add(new DefaultMutableTreeNode(new JTreeNodeClassComponent(
				startPos + 2, 4, "attribute_length: "
						+ attribute_info.getAttributeLength())));

		final String type = attribute_info.getName();
		if (AttributeInfo.TypeConstantValue.equals(type)) {
			generateTreeNode(rootNode, (AttributeConstantValue) attribute_info);
		} else if (AttributeInfo.TypeCode.equals(type)) {
			generateTreeNode(rootNode, (AttributeCode) attribute_info,
					classFile);
		} else if (AttributeInfo.TypeExceptions.equals(type)) {
			generateTreeNode(rootNode, (AttributeExceptions) attribute_info);
		} else if (AttributeInfo.TypeInnerClasses.equals(type)) {
			generateTreeNode(rootNode, (AttributeInnerClasses) attribute_info);
		} else if (AttributeInfo.TypeSynthetic.equals(type)) {
			generateTreeNode(rootNode, (AttributeSynthetic) attribute_info);
		} else if (AttributeInfo.TypeSourceFile.equals(type)) {
			generateTreeNode(rootNode, (AttributeSourceFile) attribute_info,
					classFile);
		} else if (AttributeInfo.TypeLineNumberTable.equals(type)) {
			generateTreeNode(rootNode,
					(AttributeLineNumberTable) attribute_info);
		} else if (AttributeInfo.TypeLocalVariableTable.equals(type)
				|| AttributeInfo.TypeLocalVariableTypeTable.equals(type)) {
			generateTreeNode(rootNode,
					(AttributeLocalVariableTable_base) attribute_info,
					classFile);
		} else if (AttributeInfo.TypeDeprecated.equals(type)) {
			generateTreeNode(rootNode, (AttributeDeprecated) attribute_info);
		} else if (AttributeInfo.TypeSignature.equals(type)) {
			generateTreeNode(rootNode, (AttributeSignature) attribute_info,
					classFile);
		} else if (AttributeInfo.TypeRuntimeInvisibleAnnotations.equals(type)
				|| AttributeInfo.TypeRuntimeVisibleAnnotations.equals(type)) {
			generateTreeNode(rootNode,
					(AttributeRuntimeAnnotations) attribute_info, classFile);
		} else // This is not a standard attribute type defined in the JVM Spec
				// 2nd Edition
		{
			generateTreeNode(rootNode, (AttributeExtended) attribute_info);
		}
	}

	private static void generateTreeNode(final DefaultMutableTreeNode rootNode,
			final AttributeConstantValue constantValue)
			throws InvalidTreeNodeException {
		rootNode.add(new DefaultMutableTreeNode(new JTreeNodeClassComponent(
				constantValue.getStartPos() + 6, 2, "constantvalue_index: "
						+ constantValue.getConstantValueIndex())));
	}

	private static void generateTreeNode(final DefaultMutableTreeNode rootNode,
			final AttributeRuntimeAnnotations code, final ClassFile classFile)
			throws InvalidTreeNodeException {
		final int startPos = code.getStartPos();
		final int attrCount = code.getNumberOfAnnotations();

		DefaultMutableTreeNode annotationArrayNode = new DefaultMutableTreeNode(
				new JTreeNodeClassComponent(startPos + 6, 2,
						"num_annotations: " + attrCount));
		rootNode.add(annotationArrayNode);

		if (attrCount > 0) {
			for (int i = 0; i < attrCount; i++) {
				AttributeAnnotation_annotation anno = code
						.getAnnotationsItem(i);
				DefaultMutableTreeNode curItem = new DefaultMutableTreeNode(
						new JTreeNodeClassComponent(anno.getStartPos(),
								anno.getLength(), "annotations[" + i + "]"));
				annotationArrayNode.add(curItem);

				int cpIndex = anno.getTypeIndex();
				String typeName = classFile.getCPDescription(cpIndex);
				curItem.add(new DefaultMutableTreeNode(
						new JTreeNodeClassComponent(anno.getStartPos(), 2,
								cpIndex + " " + typeName)));

			}
		}
	}

	private static void generateTreeNode(final DefaultMutableTreeNode rootNode,
			final AttributeCode code, final ClassFile classFile)
			throws InvalidTreeNodeException {
		int i;
		final int startPos = code.getStartPos();
		final int codeLength = code.getCodeLength();
		final int exceptionTableLength = code.getExceptionTableLength();
		DefaultMutableTreeNode treeNodeExceptionTable;
		DefaultMutableTreeNode treeNodeExceptionTableItem;
		final int attrCount = code.getAttributeCount();
		DefaultMutableTreeNode treeNodeAttribute;
		DefaultMutableTreeNode treeNodeAttributeItem;

		rootNode.add(new DefaultMutableTreeNode(new JTreeNodeClassComponent(
				startPos + 6, 2, "max_stack: " + code.getMaxStack())));
		rootNode.add(new DefaultMutableTreeNode(new JTreeNodeClassComponent(
				startPos + 8, 2, "max_locals: " + code.getMaxLocals())));
		rootNode.add(new DefaultMutableTreeNode(new JTreeNodeClassComponent(
				startPos + 10, 4, "code_length: " + code.getCodeLength())));
		rootNode.add(new DefaultMutableTreeNode(new JTreeNodeClassComponent(
				startPos + 14, codeLength, "code")));
		rootNode.add(new DefaultMutableTreeNode(new JTreeNodeClassComponent(
				startPos + 14 + codeLength, 2, "exception_table_length: "
						+ exceptionTableLength)));

		// Add exception table
		if (exceptionTableLength > 0) {
			final AttributeCode.ExceptionTable lastEt = code
					.getExceptionTable(exceptionTableLength - 1);
			treeNodeExceptionTable = new DefaultMutableTreeNode(
					new JTreeNodeClassComponent(startPos + 14 + codeLength + 1,
							lastEt.getStartPos() + lastEt.getLength()
									- (startPos + 14 + codeLength + 1),
							"exception_table"));

			AttributeCode.ExceptionTable et;
			for (i = 0; i < exceptionTableLength; i++) {
				et = code.getExceptionTable(i);

				treeNodeExceptionTableItem = new DefaultMutableTreeNode(
						new JTreeNodeClassComponent(et.getStartPc(),
								et.getLength(), String.format("[%d]", i)));
				JTreeAttribute.ExceptionTable.generateTreeNode(
						treeNodeExceptionTableItem, et);
				treeNodeExceptionTable.add(treeNodeExceptionTableItem);
				treeNodeExceptionTableItem = null;
			}

			rootNode.add(treeNodeExceptionTable);
		}

		// Add attributes
		rootNode.add(new DefaultMutableTreeNode(new JTreeNodeClassComponent(
				startPos + 14 + codeLength + 2 + exceptionTableLength * 8, 2,
				"attributes_count: " + attrCount)));
		if (attrCount > 0) {
			treeNodeAttribute = new DefaultMutableTreeNode(
					new JTreeNodeClassComponent(0, 0, "attributes"));

			for (i = 0; i < attrCount; i++) {
				AttributeInfo subAtrr = code.getAttribute(i);
				treeNodeAttributeItem = new DefaultMutableTreeNode(
						new JTreeNodeClassComponent(0, 0, String.format(
								"%d: %s", i, subAtrr.getName())));
				JTreeAttribute.generateTreeNode(treeNodeAttributeItem, subAtrr,
						classFile);
				treeNodeAttribute.add(treeNodeAttributeItem);
				treeNodeAttributeItem = null;
			}

			rootNode.add(treeNodeAttribute);
		}

	}

	private static void generateTreeNode(final DefaultMutableTreeNode rootNode,
			final AttributeExceptions exceptions)
			throws InvalidTreeNodeException {
		int i;
		final int startPos = exceptions.getStartPos();
		final int numOfExceptions = exceptions.getNumberOfExceptions();
		DefaultMutableTreeNode treeNodeExceptions;

		rootNode.add(new DefaultMutableTreeNode(new JTreeNodeClassComponent(
				startPos + 6, 2, "number_of_exceptions: " + numOfExceptions)));
		if (numOfExceptions > 0) {
			treeNodeExceptions = new DefaultMutableTreeNode(
					new JTreeNodeClassComponent(startPos + 8,
							numOfExceptions * 2, "exceptions"));

			for (i = 0; i < numOfExceptions; i++) {
				treeNodeExceptions
						.add(new DefaultMutableTreeNode(
								new JTreeNodeClassComponent(
										startPos + 10 + i * 2,
										2,
										String.format(
												"exception_index_table[%d]: cp_index=%d",
												i,
												exceptions
														.getExceptionIndexTableItem(i)))));
			}

			rootNode.add(treeNodeExceptions);
		}
	}

	private static void generateTreeNode(final DefaultMutableTreeNode rootNode,
			final AttributeInnerClasses innerClasses)
			throws InvalidTreeNodeException {
		int i;
		final int startPos = innerClasses.getStartPos();
		final int numOfClasses = innerClasses.getNumberOfClasses();
		DefaultMutableTreeNode treeNodeInnerClass;
		DefaultMutableTreeNode treeNodeInnerClassItem;

		rootNode.add(new DefaultMutableTreeNode(new JTreeNodeClassComponent(
				startPos + 6, 2, "number_of_classes: " + numOfClasses)));
		if (numOfClasses > 0) {
			treeNodeInnerClass = new DefaultMutableTreeNode(
					new JTreeNodeClassComponent(startPos + 8, innerClasses
							.getClass(numOfClasses - 1).getStartPos()
							+ innerClasses.getClass(numOfClasses - 1)
									.getLength() - (startPos + 8), "classes"));

			AttributeInnerClasses.Class cls;
			for (i = 0; i < numOfClasses; i++) {
				cls = innerClasses.getClass(i);

				treeNodeInnerClassItem = new DefaultMutableTreeNode(
						new JTreeNodeClassComponent(cls.getStartPos(),
								cls.getLength(), String.format("class %d",
										i + 1)));
				Class.generateTreeNode(treeNodeInnerClassItem, cls);
				treeNodeInnerClass.add(treeNodeInnerClassItem);
				treeNodeInnerClassItem = null;
			}

			rootNode.add(treeNodeInnerClass);
		}
	}

	private static void generateTreeNode(final DefaultMutableTreeNode rootNode,
			final AttributeSynthetic synthetic) throws InvalidTreeNodeException {
		// Nothing to add
	}

	private static void generateTreeNode(final DefaultMutableTreeNode rootNode,
			final AttributeSourceFile sourceFile, final ClassFile classFile)
			throws InvalidTreeNodeException {
		int cp_index = sourceFile.getSourcefileIndex();
		rootNode.add(new DefaultMutableTreeNode(new JTreeNodeClassComponent(
				sourceFile.getStartPos() + 6, 2, String.format(
						"sourcefile_index: %d [%s]", cp_index,
						classFile.getCPDescription(cp_index)))));
	}

	private static void generateTreeNode(final DefaultMutableTreeNode rootNode,
			final AttributeSignature signature, final ClassFile classFile)
			throws InvalidTreeNodeException {
		int cp_index = signature.getSignatureIndex();
		rootNode.add(new DefaultMutableTreeNode(new JTreeNodeClassComponent(
				signature.getStartPos() + 6, 2, String.format(
						"signature_index: %d [%s]", cp_index,
						classFile.getCPDescription(cp_index)))));
	}

	private static void generateTreeNode(final DefaultMutableTreeNode rootNode,
			final AttributeLineNumberTable lineNumberTable)
			throws InvalidTreeNodeException {
		final int startPos = lineNumberTable.getStartPos();
		final int length = lineNumberTable.getLineNumberTableLength();

		rootNode.add(new DefaultMutableTreeNode(new JTreeNodeClassComponent(
				startPos + 6, 2, "line_number_table_length: " + length)));

		if (length > 0) {
			final DefaultMutableTreeNode treeNodeLnt = new DefaultMutableTreeNode(
					new JTreeNodeClassComponent(startPos + 8, length * 4,
							"line_number_table"));

			DefaultMutableTreeNode treeNodeLntItem;
			AttributeLineNumberTable.LineNumberTable lnt;
			for (int i = 0; i < length; i++) {
				lnt = lineNumberTable.getLineNumberTable(i);

				treeNodeLntItem = new DefaultMutableTreeNode(
						new JTreeNodeClassComponent(lnt.getStartPos(),
								lnt.getLength(), String.format("[row %d]", i)));
				LineNumberTable.generateTreeNode(treeNodeLntItem, lnt);
				treeNodeLnt.add(treeNodeLntItem);
				treeNodeLntItem = null;
			}

			rootNode.add(treeNodeLnt);
		}
	}

	private static void generateTreeNode(final DefaultMutableTreeNode rootNode,
			final AttributeLocalVariableTable_base localVariableTable,
			final ClassFile classFile) throws InvalidTreeNodeException {
		final int startPos = localVariableTable.getStartPos();
		final int length = localVariableTable.getLocalVariableTalbeLength();

		final String lengthField;
		final String tableField;
		final boolean isTypeTable;
		if (AttributeInfo.TypeLocalVariableTable.equals(localVariableTable
				.getName())) {
			isTypeTable = false;
			lengthField = "local_variable_table_length";
			tableField = "local_variable_table";
		} else {
			isTypeTable = true;
			lengthField = "local_variable_type_table_length";
			tableField = "local_variable_type_table";
		}
		rootNode.add(new DefaultMutableTreeNode(new JTreeNodeClassComponent(
				startPos + 6, 2, lengthField + ": " + length)));

		if (length > 0) {
			final DefaultMutableTreeNode treeNodeLvt = new DefaultMutableTreeNode(
					new JTreeNodeClassComponent(startPos + 8, length * 4,
							tableField));

			DefaultMutableTreeNode treeNodeLvtItem;
			AttributeLocalVariableTable.LocalVariableTableItem lvt;
			for (int i = 0; i < length; i++) {
				lvt = localVariableTable.getLocalVariableTable(i);

				treeNodeLvtItem = new DefaultMutableTreeNode(
						new JTreeNodeClassComponent(lvt.getStartPos(),
								lvt.getLength(), String.format("[%05d]", i)));
				LocalVariableTable.generateTreeNode(treeNodeLvtItem, lvt,
						classFile, isTypeTable);
				treeNodeLvt.add(treeNodeLvtItem);
				treeNodeLvtItem = null;
			}

			rootNode.add(treeNodeLvt);
		}
	}

	private static void generateTreeNode(final DefaultMutableTreeNode rootNode,
			final AttributeDeprecated deprecated)
			throws InvalidTreeNodeException {
		// Nothing to add
	}

	private static void generateTreeNode(final DefaultMutableTreeNode rootNode,
			final AttributeExtended unknown) throws InvalidTreeNodeException {
		rootNode.add(new DefaultMutableTreeNode(new JTreeNodeClassComponent(
				unknown.getStartPos() + 6, unknown.getAttributeLength(),
				"raw data")));
	}

	private static final class ExceptionTable {

		ExceptionTable() {
		}

		private static void generateTreeNode(
				final DefaultMutableTreeNode rootNode,
				final AttributeCode.ExceptionTable et)
				throws InvalidTreeNodeException {
			if (et == null) {
				return;
			}

			final int startPos = et.getStartPos();

			rootNode.add(new DefaultMutableTreeNode(
					new JTreeNodeClassComponent(startPos, 2, "start_pc: "
							+ et.getStartPc())));
			rootNode.add(new DefaultMutableTreeNode(
					new JTreeNodeClassComponent(startPos + 2, 2, "end_pc: "
							+ et.getEndPc())));
			rootNode.add(new DefaultMutableTreeNode(
					new JTreeNodeClassComponent(startPos + 4, 2, "handler_pc: "
							+ et.getHandlerPc())));
			rootNode.add(new DefaultMutableTreeNode(
					new JTreeNodeClassComponent(startPos + 6, 2, "catch_type: "
							+ et.getCatchType())));
		}
	}

	private static final class Class {

		Class() {
		}

		private static void generateTreeNode(
				final DefaultMutableTreeNode rootNode,
				final AttributeInnerClasses.Class cls)
				throws InvalidTreeNodeException {
			final int startPos = cls.getStartPos();

			rootNode.add(new DefaultMutableTreeNode(
					new JTreeNodeClassComponent(startPos, 2,
							"inner_class_info_index: "
									+ cls.getInnerClassInfoIndex())));
			rootNode.add(new DefaultMutableTreeNode(
					new JTreeNodeClassComponent(startPos + 2, 2,
							"outer_class_info_index: "
									+ cls.getOuterClassInfoIndex())));
			rootNode.add(new DefaultMutableTreeNode(
					new JTreeNodeClassComponent(startPos + 4, 2,
							"inner_name_index: " + cls.getInnerNameIndex())));
			rootNode.add(new DefaultMutableTreeNode(
					new JTreeNodeClassComponent(startPos + 6, 2,
							"inner_class_access_flags: "
									+ cls.getInnerClassAccessFlags())));
		}
	}

	private static final class LineNumberTable {

		LineNumberTable() {
		}

		private static void generateTreeNode(
				final DefaultMutableTreeNode rootNode,
				final AttributeLineNumberTable.LineNumberTable lnt)
				throws InvalidTreeNodeException {
			if (lnt == null) {
				return;
			}

			final int startPos = lnt.getStartPos();

			rootNode.add(new DefaultMutableTreeNode(
					new JTreeNodeClassComponent(startPos, 2, "start_pc: "
							+ lnt.getStartPc())));
			rootNode.add(new DefaultMutableTreeNode(
					new JTreeNodeClassComponent(startPos + 2, 2,
							"line_number: " + lnt.getLineNumber())));
		}
	}

	private static final class LocalVariableTable {

		LocalVariableTable() {
		}

		private static void generateTreeNode(
				final DefaultMutableTreeNode rootNode,
				final AttributeLocalVariableTable.LocalVariableTableItem lvt,
				final ClassFile classFile, boolean isTypeTable)
				throws InvalidTreeNodeException {
			if (lvt == null) {
				return;
			}

			final int startPos = lvt.getStartPos();
			int cp_index;

			rootNode.add(new DefaultMutableTreeNode(
					new JTreeNodeClassComponent(startPos, 2, "start_pc: "
							+ lvt.getStartPc())));
			rootNode.add(new DefaultMutableTreeNode(
					new JTreeNodeClassComponent(startPos + 2, 2, "length: "
							+ lvt.getLength())));
			cp_index = lvt.getNameIndex();
			rootNode.add(new DefaultMutableTreeNode(
					new JTreeNodeClassComponent(startPos + 4, 2, String.format(
							"name_index: %d [%s]", cp_index,
							classFile.getCPDescription(cp_index)))));

			String descriptor_signature = "";
			if (!isTypeTable) {
				cp_index = lvt.getDescriptorIndex();
				descriptor_signature = "descriptor_index";
			} else {
				cp_index = lvt.getDescriptorIndex();
				descriptor_signature = "signature_index";
			}
			rootNode.add(new DefaultMutableTreeNode(
					new JTreeNodeClassComponent(startPos + 6, 2, String.format(
							descriptor_signature + ": %d [%s]", cp_index,
							classFile.getCPDescription(cp_index)))));

			rootNode.add(new DefaultMutableTreeNode(
					new JTreeNodeClassComponent(startPos + 8, 2, "index: "
							+ lvt.getIndex())));
		}
	}
}
