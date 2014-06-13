/*
 * AttributeLocalVariableTable.java    5:33 AM, August 5, 2007
 *
 * Copyright  2007, FreeInternals.org. All rights reserved.
 * Use is subject to license terms.
 */
package org.freeinternals.format.classfile.attribute;

import java.io.IOException;

import org.freeinternals.format.FileFormatException;
import org.freeinternals.format.classfile.ClassComponent;
import org.freeinternals.format.classfile.PosDataInputStream;
import org.freeinternals.format.classfile.u2;

/**
 * The class for the {@code LocalVariableTable} attribute. The
 * {@code LocalVariableTable} attribute has the following format:
 * 
 * <pre>
 *    LocalVariableTable_attribute {
 *        u2 attribute_name_index;
 *        u4 attribute_length;
 *        u2 local_variable_table_length;
 *        {   u2 start_pc;
 *            u2 length;
 *            u2 name_index;
 *            u2 descriptor_index;
 *            u2 index;
 *        } local_variable_table[local_variable_table_length];
 *    }
 *    LocalVariableTypeTable_attribute {
 *        u2 attribute_name_index;
 *        u4 attribute_length;
 *        u2 local_variable_type_table_length;
 *        {   u2 start_pc;
 *            u2 length;
 *            u2 name_index;
 *            u2 signature_index;
 *            u2 index;
 *        } local_variable_type_table[local_variable_type_table_length];
 *    }
 * </pre>
 * 
 * @author Amos Shi
 * @since JDK 6.0
 * @see <a
 *      href="http://www.freeinternals.org/mirror/java.sun.com/vmspec.2nded/ClassFile.doc.html#5956">
 *      VM Spec: The LocalVariableTable Attribute </a>
 */
public class AttributeLocalVariableTable_base extends AttributeInfo {

	protected transient final u2 local_variable_table_length;
	protected transient LocalVariableTableItem[] localVariableTable;

	AttributeLocalVariableTable_base(final u2 nameIndex, final String type,
			final PosDataInputStream posDataInputStream) throws IOException,
			FileFormatException {
		super(nameIndex, type, posDataInputStream);

		this.local_variable_table_length = new u2();
		this.local_variable_table_length.value = posDataInputStream
				.readUnsignedShort();
		if (this.local_variable_table_length.value > 0) {
			this.localVariableTable = new LocalVariableTableItem[this.local_variable_table_length.value];
			for (int i = 0; i < this.local_variable_table_length.value; i++) {
				this.localVariableTable[i] = new LocalVariableTableItem(
						posDataInputStream);
			}
		}

		super.checkSize(posDataInputStream.getPos());
	}

	/**
	 * Get the value of {@code local_variable_table_length}.
	 * 
	 * @return The value of {@code local_variable_table_length}
	 */
	public int getLocalVariableTalbeLength() {
		return this.local_variable_table_length.value;
	}

	/**
	 * Get the value of {@code local_variable_table}[{@code index}].
	 * 
	 * @param index
	 *            Index of the local variable table
	 * @return The value of {@code local_variable_table}[{@code index}]
	 */
	public LocalVariableTableItem getLocalVariableTable(final int index) {
		LocalVariableTableItem lvt = null;
		if (this.localVariableTable != null) {
			lvt = this.localVariableTable[index];
		}

		return lvt;
	}

	/**
	 * The {@code local_variable_table} structure in {@code LocalVariableTable}
	 * attribute.
	 * 
	 * @author Amos Shi
	 * @since JDK 6.0
	 * @see AttributeLocalVariableTable_base
	 */
	public final class LocalVariableTableItem extends ClassComponent {

		private transient final u2 start_pc;
		private transient final u2 length_lvt;
		private transient final u2 name_index;
		private transient final u2 descriptor_signature_index;
		private transient final u2 index;

		private LocalVariableTableItem(final PosDataInputStream posDataInputStream)
				throws IOException {
			this.startPos = posDataInputStream.getPos();
			super.length = 10;

			this.start_pc = new u2();
			this.start_pc.value = posDataInputStream.readUnsignedShort();
			this.length_lvt = new u2();
			this.length_lvt.value = posDataInputStream.readUnsignedShort();
			this.name_index = new u2();
			this.name_index.value = posDataInputStream.readUnsignedShort();
			this.descriptor_signature_index = new u2();
			this.descriptor_signature_index.value = posDataInputStream
					.readUnsignedShort();
			this.index = new u2();
			this.index.value = posDataInputStream.readUnsignedShort();
		}

		/**
		 * Get the value of {@code start_pc}.
		 * 
		 * @return The value of {@code start_pc}
		 */
		public int getStartPc() {
			return this.start_pc.value;
		}

		/**
		 * Get the value of code {@code length}.
		 * 
		 * @return The value of code {@code length}
		 */
		public int getCodeLength() {
			return this.length_lvt.value;
		}

		/**
		 * Get the value of {@code name_index}.
		 * 
		 * @return The value of {@code name_index}
		 */
		public int getNameIndex() {
			return this.name_index.value;
		}

		/**
		 * Get the value of {@code descriptor_signature_index}.
		 * 
		 * @return The value of {@code descriptor_signature_index}
		 */
		public int getDescriptorIndex() {
			return this.descriptor_signature_index.value;
		}

		public int getSignatureIndex() {
			return this.descriptor_signature_index.value;
		}

		/**
		 * Get the value of {@code index}.
		 * 
		 * @return The value of {@code index}
		 */
		public int getIndex() {
			return this.index.value;
		}
	}
}
