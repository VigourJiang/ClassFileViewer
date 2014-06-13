/*
 * AttributeCode.java    5:09 AM, August 5, 2007
 *
 * Copyright  2007, FreeInternals.org. All rights reserved.
 * Use is subject to license terms.
 */
package org.freeinternals.format.classfile.attribute;

import java.io.IOException;

import org.freeinternals.format.FileFormatException;
import org.freeinternals.format.classfile.ClassComponent;
import org.freeinternals.format.classfile.PosDataInputStream;
import org.freeinternals.format.classfile.u1;
import org.freeinternals.format.classfile.u2;
import org.freeinternals.format.classfile.u4;
import org.freeinternals.format.classfile.attribute.AttributeCode.ExceptionTable;

/**
 * The class for the {@code Code} attribute. The {@code Code} attribute has the
 * following format:
 * 
 * <pre>
 * annotation {
 *   u2 type_index;
 *   u2 num_element_value_pairs;
 *   {  u2 element_name_index;
 *      element_value value;
 *   } element_value_pairs[num_element_value_pairs];
 * }
 * </pre>
 * 
 * 
 * @author Amos Shi
 * @since JDK 6.0
 * @see <a
 *      href="http://www.freeinternals.org/mirror/java.sun.com/vmspec.2nded/ClassFile.doc.html#1546">
 *      VM Spec: The Code Attribute </a>
 */
public class AttributeAnnotation_annotation extends ClassComponent {

	private transient final u2 type_index;
	private transient final u2 num_element_value_pairs;
	private transient ElementValuePair[] element_value_pairs;

	public int getTypeIndex() {
		return type_index.value;
	}

	public int getNumElementValuePairs() {
		return num_element_value_pairs.value;
	}

	public ElementValuePair getElementValue(int index) {
		return element_value_pairs[index];
	}

	AttributeAnnotation_annotation(final PosDataInputStream posDataInputStream)
			throws IOException, FileFormatException {
		this.startPos = posDataInputStream.getPos();

		int length = 0;
		this.type_index = new u2();
		this.type_index.value = posDataInputStream.readUnsignedShort();
		length += 2;

		this.num_element_value_pairs = new u2();
		this.num_element_value_pairs.value = posDataInputStream
				.readUnsignedShort();
		length += 2;

		if (this.num_element_value_pairs.value > 0) {
			this.element_value_pairs = new ElementValuePair[this.num_element_value_pairs.value];
			for (int i = 0; i < this.element_value_pairs.length; i++) {
				ElementValuePair pair = new ElementValuePair(posDataInputStream);
				this.element_value_pairs[i] = pair;
				length += pair.getLength();
			}
		}
		this.length = length;

	}

	public static class ElementValuePair extends ClassComponent {
		public ElementValuePair(final PosDataInputStream posDataInputStream)
				throws IOException, FileFormatException {
			startPos = posDataInputStream.getPos();

			element_name_index = new u2();
			element_name_index.value = posDataInputStream.readUnsignedShort();
			value = new ElementValue(posDataInputStream);

			length = 2 + value.getLength();
		}

		u2 element_name_index;
		ElementValue value;
	}

	/**
	 * <pre>
	 * element_value {
	 *   u1 tag;
	 *   union {
	 *     u2 const_value_index;
	 * 
	 *     {  u2 type_name_index;
	 *        u2 const_name_index;
	 *     } enum_const_value;
	 * 
	 *     u2 class_info_index;
	 * 
	 *     annotation annotation_value;
	 * 
	 *     {  u2 num_values;
	 *        element_value values[num_values];
	 *     } array_value;
	 *   } value;
	 * }
	 * </pre>
	 * 
	 * @author vigour
	 * 
	 */
	public static class ElementValue extends ClassComponent {

		public ElementValue(final PosDataInputStream posDataInputStream)
				throws IOException, FileFormatException {
			this.startPos = posDataInputStream.getPos();
			this.tag = new u1();
			this.tag.value = (short) posDataInputStream.readUnsignedByte();

			int tmpLength = 1;
			switch (getTagType()) {
			case StringField:
				const_value_index = new u2();
				const_value_index.value = posDataInputStream
						.readUnsignedShort();
				tmpLength += 2;
				break;
			case EnumConstantField:
				enum_const_value = new EnumConstValue(posDataInputStream);
				tmpLength += enum_const_value.getLength();
				break;
			case ClassField:
				class_info_index = new u2();
				class_info_index.value = posDataInputStream.readUnsignedShort();
				tmpLength += 2;
				break;
			case AnnotationField:
				annotation_value = new AttributeAnnotation_annotation(
						posDataInputStream);
				tmpLength += annotation_value.getLength();
				break;
			case ArrayField:
				array_value = new ArrayValue(posDataInputStream);
				tmpLength += array_value.getLength();
				break;
			default:
				break;
			}
			this.length = tmpLength;

		}

		enum ElementValue_TagType {
			PrimitiveField, StringField, EnumConstantField, ClassField, AnnotationField, ArrayField;
		}

		public ElementValue_TagType getTagType() throws FileFormatException {
			switch (tag.value) {
			case 'B':
			case 'C':
			case 'D':
			case 'F':
			case 'I':
			case 'J':
			case 'S':
			case 'Z':
			case 's':
				return ElementValue_TagType.StringField;
			case 'e':
				return ElementValue_TagType.EnumConstantField;
			case 'c':
				return ElementValue_TagType.ClassField;
			case '@':
				return ElementValue_TagType.AnnotationField;
			case '[':
				return ElementValue_TagType.ArrayField;
			default:
				throw new FileFormatException(this.getClass().getName()
						+ ": Illegal tag value");
			}
		}

		private u1 tag;

		private u2 const_value_index;
		private EnumConstValue enum_const_value;
		private u2 class_info_index;
		private AttributeAnnotation_annotation annotation_value;
		private ArrayValue array_value;
	}

	public static class EnumConstValue extends ClassComponent {
		public EnumConstValue(final PosDataInputStream posDataInputStream)
				throws IOException {
			this.startPos = posDataInputStream.getPos();
			this.length = 4;
			this.type_name_index = new u2();
			this.type_name_index.value = posDataInputStream.readUnsignedShort();
			this.const_name_index = new u2();
			this.const_name_index.value = posDataInputStream
					.readUnsignedShort();
		}

		private u2 type_name_index;
		private u2 const_name_index;

		public int getTypeNameIndex() {
			return type_name_index.value;
		}

		public int getConstNameIndex() {
			return const_name_index.value;
		}
	}

	public static class ArrayValue extends ClassComponent {
		public ArrayValue(final PosDataInputStream posDataInputStream)
				throws IOException, FileFormatException {
			this.startPos = posDataInputStream.getPos();
			this.num_values = new u2();
			this.num_values.value = posDataInputStream.readUnsignedShort();
			if (this.num_values.value > 0) {
				this.values = new ElementValue[this.num_values.value];
				for (int i = 0; i < this.num_values.value; i++) {
					this.values[i] = new ElementValue(posDataInputStream);
				}
			}
			this.length = posDataInputStream.getPos() - this.startPos;
		}

		private u2 num_values;
		private ElementValue values[];

		public int getNumValues() {
			return num_values.value;
		}

		public ElementValue getElementValue(int index) {
			return values[index];
		}
	}
}
