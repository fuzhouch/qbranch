// Licensed under the MIT license. See LICENSE file in the project root
// for full license information.

package net.dummydigit.qbranch.protocols;

import net.dummydigit.qbranch.BondDataType;

public class KvpContainerHeaderInfo {
    private BondDataType keyType;
    private BondDataType valueType;
    private long kvpCount;
    private int version;

    public KvpContainerHeaderInfo(BondDataType keyType, BondDataType valueType, long kvpCount, int version) {
        this.keyType = keyType;
        this.valueType = valueType;
        this.kvpCount = kvpCount;
        this.version = version;
    }

    public BondDataType getKeyType() { return keyType; }

    public BondDataType getValueType() { return valueType; }

    public long getKvpCount() {
        return kvpCount;
    }

    public int getVersion() {
        return version;
    }
}