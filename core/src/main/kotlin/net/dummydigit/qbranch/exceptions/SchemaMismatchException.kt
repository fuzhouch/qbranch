// Licensed under the MIT license. See LICENSE file in the project root
// for full license information.

package net.dummydigit.qbranch.exceptions

import net.dummydigit.qbranch.BondDataType

class SchemaMismatchException(actualDataType : BondDataType, fieldName : String)
    : RuntimeException("SchemaMismatch:actualDataType=$actualDataType,fieldName=$fieldName")