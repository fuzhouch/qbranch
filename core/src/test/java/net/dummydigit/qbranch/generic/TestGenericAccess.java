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
        TypeArg<GenericType<String, Byte>> tArg = GenericType.asQTypeArg(BuiltinTypeArg.WStringT, BuiltinTypeArg.Int8T);
        GenericType<String, Byte> obj = tArg.newInstance();
        Assert.assertEquals("", obj.fieldT1);
        Assert.assertEquals(new Byte((byte)0), obj.fieldT2);
        Assert.assertEquals(0, obj.mapT1T2.size());
        Assert.assertEquals(0, obj.setIntField.size());
        Assert.assertEquals(0, obj.vectorT1.size());
    }

    @Test
    public void testCreateBuiltinTypes() {
        Assert.assertEquals(0, BuiltinTypeArg.Int8T.newInstance().byteValue());
        Assert.assertEquals(0, BuiltinTypeArg.Int16T.newInstance().shortValue());
        Assert.assertEquals(0, BuiltinTypeArg.Int32T.newInstance().intValue());
        Assert.assertEquals(0, BuiltinTypeArg.Int64T.newInstance().longValue());
        Assert.assertEquals(0, BuiltinTypeArg.UInt8T.newInstance().getValue());
        Assert.assertEquals(0, BuiltinTypeArg.UInt16T.newInstance().getValue());
        Assert.assertEquals(0, BuiltinTypeArg.UInt32T.newInstance().getValue());
        Assert.assertEquals(BigInteger.valueOf(0), BuiltinTypeArg.UInt64T.newInstance().getValue());
        Assert.assertEquals("", BuiltinTypeArg.ByteStringT.newInstance().getValue());
        Assert.assertEquals("", BuiltinTypeArg.WStringT.newInstance());
        Assert.assertTrue(abs(0.0f - BuiltinTypeArg.FloatT.newInstance()) < 0.0001);
        Assert.assertTrue(abs(0.0 - BuiltinTypeArg.DoubleT.newInstance()) < 0.0001);
        Assert.assertNotEquals(null, BuiltinTypeArg.BlobT.newInstance());
    }
}
