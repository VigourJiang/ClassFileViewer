/*
 * ConstantNameAndTypeInfo.java    4:46 AM, August 5, 2007
 *
 * Copyright  2007, FreeInternals.org. All rights reserved.
 * Use is subject to license terms.
 */
package org.freeinternals.format.classfile.constpool;

import java.io.IOException;

import org.freeinternals.format.classfile.PosDataInputStream;
import org.freeinternals.format.classfile.u1;
import org.freeinternals.format.classfile.u2;

/**
 * The class for the {@code CONSTANT_NameAndType_info} structure in constant
 * pool. The {@code CONSTANT_NameAndType_info} structure has the following
 * format:
 * 
 * <pre>
 * CONSTANT_MethodHandle_info {
 *   u1 tag;
 *   u1 reference_kind;
 *   u2 reference_index;
 * }
 * </pre>
 * 
 * @author Amos Shi
 * @since JDK 6.0
 * @see <a
 *      href="http://www.freeinternals.org/mirror/java.sun.com/vmspec.2nded/ClassFile.doc.html#1327">
 *      VM Spec: The CONSTANT_NameAndType_info Structure </a>
 */
public class ConstantMethodHandleInfo extends AbstractCPInfo {

	private final u1 reference_kind;
	private final u2 reference_index;

	public ConstantMethodHandleInfo(final PosDataInputStream posDataInputStream)
			throws IOException {
		super();
		this.tag.value = AbstractCPInfo.CONSTANT_MethodHandle;

		this.startPos = posDataInputStream.getPos() - 1;
		this.length = 4;

		this.reference_kind = new u1();
		this.reference_kind.value = (short) posDataInputStream
				.readUnsignedByte();
		this.reference_index = new u2();
		this.reference_index.value = posDataInputStream.readUnsignedShort();
	}

	@Override
	public String getName() {
		return "MethodHandle";
	}

	@Override
	public String getDescription() {
		return String.format(
				"ConstantNameAndTypeInfo: Start Position: [%d], length: [%d], "
						+ "value: reference_kind=[%d], reference_index=[%d].",
				this.startPos, this.length, this.reference_kind.value,
				this.reference_index.value);
	}

	/**
	 * Get the value of {@code name_index}.
	 * 
	 * @return The value of {@code name_index}
	 */
	public int getReferenceKind() {
		return this.reference_kind.value;
	}

	/**
	 * Get the value of {@code descriptor_index}.
	 * 
	 * @return The value of {@code descriptor_index}
	 */
	public int getReferenceIndex() {
		return this.reference_index.value;
	}
}
