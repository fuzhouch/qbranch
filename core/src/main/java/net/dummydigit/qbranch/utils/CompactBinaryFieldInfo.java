// Licensed under the MIT license. See LICENSE file in the project root
// for full license information.

package net.dummydigit.qbranch.utils;

import bond.BondDataType;
import java.io.IOException;
import java.io.InputStream;
import net.dummydigit.qbranch.exceptions.EndOfStreamException;

/**
 * A helper class to extract field type Id from binary stream.
 */
public class CompactBinaryFieldInfo extends FieldInfo {
    public final int TWO_BYTE_FIELD_INFO_FLAG = 0x6; // b(110)
    public final int FOUR_BYTE_FIELD_INFO_FLAG = 0x7; // b(111)

    public CompactBinaryFieldInfo(InputStream input) throws IOException, EndOfStreamException {
        byte firstByte = (byte)input.read();
        int typeId = firstByte & 0x1F; // Get low 5 bits
        int flag = (firstByte >> 5) & 0x7; // Get high 3 bits
        byte[] moreFieldBytes = null;
        int actualBytesRead;
        switch (flag) {
            case TWO_BYTE_FIELD_INFO_FLAG:
                moreFieldBytes = new byte[1];
                break;
            case FOUR_BYTE_FIELD_INFO_FLAG:
                moreFieldBytes = new byte[3];
                break;
            default:
                m_fieldId = flag;
                break;
        }

        if (moreFieldBytes != null) {
            actualBytesRead = input.read(moreFieldBytes, 0, moreFieldBytes.length);
            if (actualBytesRead != moreFieldBytes.length) {
                throw new EndOfStreamException(moreFieldBytes.length, actualBytesRead);
            }
            m_fieldId = 0;
            for (int i = 0; i < moreFieldBytes.length; ++i) {
                m_fieldId = (m_fieldId << 8) + moreFieldBytes[i];
            }
        }
        m_typeId = BondDataType.values()[typeId];
    }
}
