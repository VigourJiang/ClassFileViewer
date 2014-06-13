/*
 * AttributeLocalVariableTable.java    5:33 AM, August 5, 2007
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
 * The class for the {@code LocalVariableTable} attribute. The
 * {@code LocalVariableTable} attribute has the following format:
 * @author Amos Shi
 * @since JDK 6.0
 * @see <a
 *      href="http://www.freeinternals.org/mirror/java.sun.com/vmspec.2nded/ClassFile.doc.html#5956">
 *      VM Spec: The LocalVariableTable Attribute </a>
 */
public class AttributeLocalVariableTable extends AttributeLocalVariableTable_base {
	AttributeLocalVariableTable(final u2 nameIndex, final String type,
			final PosDataInputStream posDataInputStream) throws IOException,
			FileFormatException {
		super(nameIndex, type, posDataInputStream);
	}

}
