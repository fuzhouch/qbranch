// Licensed under the MIT license. See LICENSE file in the project root
// for full license information.

package net.dummydigit.qbranch.exceptions

class EndOfStreamException(expectedLen: Int, actualLen: Int):
        Exception("UnexpectedEndOfStream:expected=$expectedLen,actual=$actualLen") {
    val expectedLength = expectedLen
    val returnedLength = actualLen
}