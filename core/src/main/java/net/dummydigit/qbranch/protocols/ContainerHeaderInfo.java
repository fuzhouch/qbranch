// Licensed under the MIT license. See LICENSE file in the project root
// for full license information.

package net.dummydigit.qbranch.protocols;

import net.dummydigit.qbranch.BondDataType;

public class ContainerHeaderInfo {
    private BondDataType containerType;
    private long length;
    private int version;

    public ContainerHeaderInfo(BondDataType containerType, long containerLength, int version) {
        this.containerType = containerType;
        this.length = containerLength;
        this.version = version;
    }

    public BondDataType getContainerType() {
        return containerType;
    }

    public long getLength() {
        return length;
    }

    public int getVersion() {
        return version;
    }
}
