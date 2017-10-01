// Licensed under the MIT license. See LICENSE file in the project root
// for full license information.

package net.dummydigit.qbranch.protocols;

import net.dummydigit.qbranch.BondDataType;

public class ContainerHeaderInfo {
    private BondDataType elementType;
    private long length;
    private int version;

    public ContainerHeaderInfo(BondDataType elementType, long containerLength, int version) {
        this.elementType = elementType;
        this.length = containerLength;
        this.version = version;
    }

    public BondDataType getElementType() {
        return elementType;
    }

    public long getElementCount() {
        return length;
    }

    public int getVersion() {
        return version;
    }
}
