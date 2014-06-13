/*
 * AttributeExceptions.java    5:18 AM, August 5, 2007
 *
 * Copyright  2007, FreeInternals.org. All rights reserved.
 * Use is subject to license terms.
 */
package org.freeinternals.format.classfile.attribute;

import java.io.IOException;

import org.freeinternals.format.FileFormatException;
import org.freeinternals.format.classfile.PosDataInputStream;
import org.freeinternals.format.classfile.u2;

/**
 * The class for the {@code Exceptions} attribute. The {@code Exceptions}
 * attribute has the following format:
 * 
 * <pre>
 * RuntimeVisibleAnnotations_attribute {
 *   u2 attribute_name_index;
 *   u4 attribute_length;
 *   u2 num_annotations;
 *   annotation annotations[num_annotations];
 * }
 * </pre>
 * 
 * @author Amos Shi
 * @since JDK 6.0
 * @see <a
 *      href="http://www.freeinternals.org/mirror/java.sun.com/vmspec.2nded/ClassFile.doc.html#3129">
 *      VM Spec: The Exceptions Attribute </a>
 */
public class AttributeRuntimeAnnotations extends AttributeInfo {

	private final u2 num_annotations;
	private final AttributeAnnotation_annotation annotations[];

	AttributeRuntimeAnnotations(final u2 nameIndex, final String type,
			final PosDataInputStream posDataInputStream) throws IOException,
			FileFormatException {
		super(nameIndex, type, posDataInputStream);

		this.num_annotations = new u2();
		this.num_annotations.value = posDataInputStream.readUnsignedShort();
		final int excpCount = this.num_annotations.value;
		if (excpCount > 0) {
			this.annotations = new AttributeAnnotation_annotation[excpCount];
			for (int i = 0; i < excpCount; i++) {
				this.annotations[i] = new AttributeAnnotation_annotation(
						posDataInputStream);
			}
		} else {
			annotations = null;
		}

		super.checkSize(posDataInputStream.getPos());
	}

	/**
	 * Get the value of {@code number_of_exceptions}.
	 * 
	 * @return The value of {@code number_of_exceptions}
	 */
	public int getNumberOfAnnotations() {
		return this.num_annotations.value;
	}

	/**
	 * Get the value of {@code exception_index_table}[{@code index}].
	 * 
	 * @param index
	 *            Index of the exception table
	 * @return The value of {@code exception_index_table}[{@code index}]
	 */
	public AttributeAnnotation_annotation getAnnotationsItem(final int index) {
		// TODO: Add the check: index < number_of_exceptions
		AttributeAnnotation_annotation ret;
		if (this.annotations == null) {
			ret = null;
		} else {
			ret = this.annotations[index];
		}

		return ret;
	}
}
