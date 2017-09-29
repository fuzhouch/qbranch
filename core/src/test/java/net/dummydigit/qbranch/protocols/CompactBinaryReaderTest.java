// Licensed under the MIT license. See LICENSE file in the project root
// for full license information.

package net.dummydigit.qbranch.protocols;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileReader;
import java.math.BigInteger;
import javax.xml.bind.DatatypeConverter;

import kotlin.NotImplementedError;
import net.dummydigit.qbranch.Deserializer;
import net.dummydigit.qbranch.exceptions.UnsupportedVersionException;
import net.dummydigit.qbranch.ut.PrimitiveStruct;

import net.dummydigit.qbranch.ut.mocks.ContainerTypes;
import org.junit.Assert;
import org.junit.Test;

public class CompactBinaryReaderTest {

    private byte[] getEncodedTestPayload(String txtInSource) {
        ClassLoader loader = getClass().getClassLoader();
        String base64EncodedContent = null;
        try {
            File testIdlEncodedContent = new File(loader.getResource(txtInSource).getFile());
            BufferedReader b = new BufferedReader(new FileReader(testIdlEncodedContent));
            base64EncodedContent = b.readLine();
            b.close();
        } catch (java.io.IOException e) {
            Assert.assertTrue("FileInResourcesNotFound", false);
        }
        Assert.assertTrue(base64EncodedContent != null);
        Assert.assertTrue(base64EncodedContent.length() > 0);
        return DatatypeConverter.parseBase64Binary(base64EncodedContent);
    }

    @Test
    public void testReaderWorkflow() {
        byte[] data = getEncodedTestPayload("primitive_values.txt");

        ByteArrayInputStream inputBuffer = new ByteArrayInputStream(data);
        CompactBinaryReader reader = new CompactBinaryReader(inputBuffer);
        Deserializer<PrimitiveStruct> deserializer = new Deserializer<>(PrimitiveStruct.asQTypeArg());
        PrimitiveStruct primitiveStruct = deserializer.deserialize(reader);

        Assert.assertEquals(0x01, primitiveStruct.getInt8value());
        Assert.assertEquals(0x0102, primitiveStruct.getInt16value());
        Assert.assertEquals(0x01020304, primitiveStruct.getInt32value());
        Assert.assertEquals(0x0102030405060708L, primitiveStruct.getInt64value());

        Assert.assertEquals((short)0x01, primitiveStruct.getUint8value().getValue());
        Assert.assertEquals(0x0201, primitiveStruct.getUint16value().getValue());
        Assert.assertEquals((long)0x04030201, primitiveStruct.getUint32value().getValue());
        Assert.assertEquals(new BigInteger("0807060504030201", 16), primitiveStruct.getUint64value().getValue());
        Assert.assertEquals("teststring", primitiveStruct.getStringvalue().getValue());
        Assert.assertEquals("testwstring", primitiveStruct.getWstringvalue());

        Assert.assertEquals(123.456, primitiveStruct.getFloatvalue(), 0.00001);
        Assert.assertEquals(654.321, primitiveStruct.getDoublevalue(), 0.00001);
    }

    @Test(expected=UnsupportedVersionException.class)
    public void testThrowExceptionOnBadProtocolVersion() throws UnsupportedVersionException {
        byte[] data = DatatypeConverter.parseBase64Binary("TestData");
        ByteArrayInputStream inputBuffer = new ByteArrayInputStream(data);
        new CompactBinaryReader(inputBuffer, 2);
    }

    @Test
    public void testContainerType() {
        byte[] data = getEncodedTestPayload("container_values.txt");
        ByteArrayInputStream inputBuffer = new ByteArrayInputStream(data);
        CompactBinaryReader reader = new CompactBinaryReader(inputBuffer);
        Deserializer<ContainerTypes> deserializer = new Deserializer<>(ContainerTypes.asQTypeArg());
        ContainerTypes container = deserializer.deserialize(reader);
        Assert.assertEquals(1, container.vectorIntField.size());
        Assert.assertEquals(1, container.vectorIntField.get(0).size());
        Assert.assertEquals(10, (int)container.vectorIntField.get(0).get(0));
    }
}
