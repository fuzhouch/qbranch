// Licensed under the MIT license. See LICENSE file in the project root
// for full license information.

package net.dummydigit.qbranch.utils;

import net.dummydigit.qbranch.BondDataType;
import java.io.IOException;
import java.io.InputStream;
import net.dummydigit.qbranch.exceptions.EndOfStreamException;
import net.dummydigit.qbranch.UnsignedInt;

/**
 * A helper class to extract field type Id from binary stream.
 */
public class CompactBinaryFieldInfo extends FieldInfo {
    private static final int TWO_BYTE_FIELD_INFO_FLAG = 0x6; // b(110)
    private static final int FOUR_BYTE_FIELD_INFO_FLAG = 0x7; // b(111)

    public CompactBinaryFieldInfo(InputStream input) throws IOException, EndOfStreamException {
        byte firstByte = (byte)input.read();
        int _typeId = firstByte & 0x1F; // Get low 5 bits
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
                fieldId = flag;
                break;
        }

        if (moreFieldBytes != null) {
            actualBytesRead = input.read(moreFieldBytes, 0, moreFieldBytes.length);
            if (actualBytesRead != moreFieldBytes.length) {
                throw new EndOfStreamException(moreFieldBytes.length, actualBytesRead);
            }
            fieldId = 0;
            for (int eachFieldBytes : moreFieldBytes) {
                fieldId = (fieldId << 8) + eachFieldBytes;
            }
        }
        typeId = BondDataType.values()[_typeId];
    }

    public static ContainerHeaderInfo decodeContainerHeaderV1(InputStream stream) throws IOException {
        byte oneByte = (byte)stream.read();
        BondDataType containerType = BondDataType.values()[oneByte & 0x1F];
        UnsignedInt containerLength = VariableLength.decodeVarUInt32(stream);
        return new ContainerHeaderInfo(containerType, containerLength.getValue(), 1);
    }
}
