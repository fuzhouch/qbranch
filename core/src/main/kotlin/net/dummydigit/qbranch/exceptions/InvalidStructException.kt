// Licensed under the MIT license. See LICENSE file in the project root
// for full license information.

package net.dummydigit.qbranch.exceptions

class InvalidStructException(fieldName : String, hint : String) : RuntimeException("err=$hint,field=$fieldName")