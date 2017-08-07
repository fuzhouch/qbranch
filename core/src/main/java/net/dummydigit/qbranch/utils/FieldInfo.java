// Licensed under the MIT license. See LICENSE file in the project root
// for full license information.

package net.dummydigit.qbranch.utils;

import bond.BondDataType;

/**
 * Represent field information.
 */
public class FieldInfo {
    protected int m_fieldId = 0;
    protected BondDataType m_typeId = BondDataType.BT_STOP;

    public int getFieldId() {
        return m_fieldId;
    }

    public BondDataType getTypeId() {
        return m_typeId;
    }
}