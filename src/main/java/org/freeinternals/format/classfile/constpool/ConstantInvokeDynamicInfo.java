/*
 * ConstantNameAndTypeInfo.java    4:46 AM, August 5, 2007
 *
 * Copyright  2007, FreeInternals.org. All rights reserved.
 * Use is subject to license terms.
 */
package org.freeinternals.format.classfile.constpool;

import java.io.IOException;

import org.freeinternals.format.classfile.PosDataInputStream;
import org.freeinternals.format.classfile.u2;

/**
 * The class for the {@code CONSTANT_NameAndType_info} structure in constant
 * pool. The {@code CONSTANT_NameAndType_info} structure has the following
 * format:
 * 
 * <pre>
 * CONSTANT_InvokeDynamic_info {
 *   u1 tag;
 *   u2 bootstrap_method_attr_index;
 *   u2 name_and_type_index;
 * }
 * </pre>
 * 
 * @author Amos Shi
 * @since JDK 6.0
 * @see <a
 *      href="http://www.freeinternals.org/mirror/java.sun.com/vmspec.2nded/ClassFile.doc.html#1327">
 *      VM Spec: The CONSTANT_NameAndType_info Structure </a>
 */
public class ConstantInvokeDynamicInfo extends AbstractCPInfo {

	private final u2 name_index;
	private final u2 descriptor_index;

	public ConstantInvokeDynamicInfo(final PosDataInputStream posDataInputStream)
			throws IOException {
		super();
		this.tag.value = AbstractCPInfo.CONSTANT_InvokeDynamic;

		this.startPos = posDataInputStream.getPos() - 1;
		this.length = 5;

		this.name_index = new u2();
		this.name_index.value = posDataInputStream.readUnsignedShort();
		this.descriptor_index = new u2();
		this.descriptor_index.value = posDataInputStream.readUnsignedShort();
	}

	@Override
	public String getName() {
		return "InvokeDynamic";
	}

	@Override
	public String getDescription() {
		return String
				.format("ConstantNameAndTypeInfo: Start Position: [%d], length: [%d], value: name_index=[%d], descriptor_index=[%d].",
						this.startPos, this.length, this.name_index.value,
						this.descriptor_index.value);
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
	 * Get the value of {@code descriptor_index}.
	 * 
	 * @return The value of {@code descriptor_index}
	 */
	public int getDescriptorIndex() {
		return this.descriptor_index.value;
	}
}
