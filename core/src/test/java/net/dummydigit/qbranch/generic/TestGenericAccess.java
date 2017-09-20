// Licensed under the MIT license. See LICENSE file in the project root
// for full license information.

package net.dummydigit.qbranch.generic;

import java.math.BigInteger;
import net.dummydigit.qbranch.ut.mocks.GenericType;
import org.junit.Assert;
import org.junit.Test;

import static java.lang.Math.abs;

public class TestGenericAccess {

    @Test
    public void testCreateTypedGeneric() {
        QTypeArg<GenericType<String, Byte>> tArg = GenericType.asQTypeArg(BuiltinQTypeArg.WStringT, BuiltinQTypeArg.Int8T);
        GenericType<String, Byte> obj = tArg.newInstance();
        Assert.assertEquals("", obj.fieldT1);
        Assert.assertEquals(new Byte((byte)0), obj.fieldT2);
        Assert.assertEquals(0, obj.mapT1T2.size());
        Assert.assertEquals(0, obj.setIntField.size());
        Assert.assertEquals(0, obj.vectorT1.size());
    }

    @Test
    public void testCreateBuiltinTypes() {
        Assert.assertEquals(0, BuiltinQTypeArg.Int8T.newInstance().byteValue());
        Assert.assertEquals(0, BuiltinQTypeArg.Int16T.newInstance().shortValue());
        Assert.assertEquals(0, BuiltinQTypeArg.Int32T.newInstance().intValue());
        Assert.assertEquals(0, BuiltinQTypeArg.Int64T.newInstance().longValue());
        Assert.assertEquals(0, BuiltinQTypeArg.UInt8T.newInstance().getValue());
        Assert.assertEquals(0, BuiltinQTypeArg.UInt16T.newInstance().getValue());
        Assert.assertEquals(0, BuiltinQTypeArg.UInt32T.newInstance().getValue());
        Assert.assertEquals(BigInteger.valueOf(0), BuiltinQTypeArg.UInt64T.newInstance().getValue());
        Assert.assertEquals("", BuiltinQTypeArg.ByteStringT.newInstance().getValue());
        Assert.assertEquals("", BuiltinQTypeArg.WStringT.newInstance());
        Assert.assertTrue(abs(0.0f - BuiltinQTypeArg.FloatT.newInstance()) < 0.0001);
        Assert.assertTrue(abs(0.0 - BuiltinQTypeArg.DoubleT.newInstance()) < 0.0001);
        Assert.assertNotEquals(null, BuiltinQTypeArg.BlobT.newInstance());
    }
}
