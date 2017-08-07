// Licensed under the MIT license. See LICENSE file in the project root
// for full license information.

package net.dummydigit.qbranch.utils;

import java.lang.reflect.Field;

import net.dummydigit.qbranch.types.ReflectionExtensions;
import net.dummydigit.qbranch.ut.mocks.StructWithGenericField;
import net.dummydigit.qbranch.generic.ObjectCreator;
import org.junit.Assert;
import org.junit.Test;

public class ReflectionUtilsTest {
    @Test
    public void testFieldGeneric() {
        StructWithGenericField<String> testObj = new StructWithGenericField<>(ObjectCreator.toJTypeArgsV(String.class));
        try {
            Field genericField = testObj.getClass().getDeclaredField("genericField");
            Field intField = testObj.getClass().getDeclaredField("intField");
            Field containerField = testObj.getClass().getDeclaredField("containerField");
            Assert.assertTrue(ReflectionExtensions.isGenericType(genericField));
            Assert.assertFalse(ReflectionExtensions.isGenericType(intField));
            Assert.assertFalse(ReflectionExtensions.isGenericType(containerField));
        } catch(NoSuchFieldException e) {
            Assert.assertFalse("TestFieldNotFound", true);
        }
    }
}
