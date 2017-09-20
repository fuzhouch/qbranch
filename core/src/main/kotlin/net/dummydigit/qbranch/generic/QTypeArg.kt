// Licensed under the MIT license. See LICENSE file in the project root
// for full license information.

package net.dummydigit.qbranch.generic

interface QTypeArg<out T : Any> {
    fun newInstance() : T
}