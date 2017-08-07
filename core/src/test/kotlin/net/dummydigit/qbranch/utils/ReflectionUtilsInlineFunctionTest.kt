// Licensed under the MIT license. See LICENSE file in the project root
// for full license information.

package net.dummydigit.qbranch.utils

import net.dummydigit.qbranch.types.extractGenericTypeArguments
import org.junit.Assert
import org.junit.Test
import java.util.*

class ReflectionUtilsInlineFunctionTest {
    @Test
    fun testExtractGenericTypeArguments() {
        val args = extractGenericTypeArguments<HashMap<String, ArrayList<String>>>()
        Assert.assertTrue(args.size == 2)
        Assert.assertEquals(args[0].typeName, String::class.java.name)
        Assert.assertEquals(args[1].typeName, "java.util.ArrayList<java.lang.String>")
    }

    @Test(expected=UnsupportedOperationException::class)
    fun testThrowExceptionOnNonGenericType() {
        val args = extractGenericTypeArguments<String>()
        Assert.assertEquals(args.size, 0)
    }
}