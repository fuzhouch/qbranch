// Licensed under the MIT license. See LICENSE file in the project root
// for full license information.

package net.dummydigit.qbranch.utils;

import net.dummydigit.qbranch.BondDataType;

/**
 * Represent field information.
 */
public class FieldInfo {
    int fieldId = 0;
    BondDataType typeId = BondDataType.BT_STOP;

    public int getFieldId() {
        return fieldId;
    }

    public BondDataType getTypeId() {
        return typeId;
    }
}