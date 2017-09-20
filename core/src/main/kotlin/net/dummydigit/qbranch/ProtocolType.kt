// Licensed under the MIT license. See LICENSE file in the project root
// for full license information.

package net.dummydigit.qbranch

/**
 * A manually written protocol type enum to replace official Bond definition.
 */
internal enum class ProtocolType(val num : Int) {
    MARSHALED_PROTOCOL(0),
    COMPACT_PROTOCOL(16963)
}