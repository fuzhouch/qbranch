package net.dummydigit.qbranch.generic;

import net.dummydigit.qbranch.exceptions.UnsupportedBondTypeException;
import net.dummydigit.qbranch.ut.mocks.AllPrimitiveTypes;
import net.dummydigit.qbranch.ut.mocks.InvalidBondGenerated;
import net.dummydigit.qbranch.ut.mocks.StructWithGenericField;
import org.junit.Assert;
import org.junit.Test;

public class JObjectCreatorTest {
    @Test(expected=UnsupportedBondTypeException.class)
    public void testThrowExceptionOnNonBondGenerated() {
        ObjectCreatorAsConcreteType<InvalidBondGenerated> creator =
                ObjectCreator.mkCreator(InvalidBondGenerated.class);
    }

    @Test(expected=UnsupportedBondTypeException.class)
    public void testThrowExceptionWhenPassingGenericClassToNonGenericBuilder() {
        ObjectCreatorAsConcreteType<StructWithGenericField> creator =
                ObjectCreator.mkCreator(StructWithGenericField.class);
    }

    @Test(expected=UnsupportedBondTypeException.class)
    public void testThrowExceptionWhenPassingNonGenericClassToGenericBuilder() {
        ObjectCreatorAsConcreteType<AllPrimitiveTypes> creator =
                ObjectCreator.mkCreator(AllPrimitiveTypes.class,
                        ObjectCreator.toJTypeArgs(new Class[] { JObjectCreatorTest.class }));
    }

    @Test
    public void testCreateConcreteTypeCreators() {
        ObjectCreatorAsConcreteType<AllPrimitiveTypes> creator = ObjectCreator.mkCreator(AllPrimitiveTypes.class);
        AllPrimitiveTypes obj = creator.newInstance();
        Assert.assertEquals(1, obj.getFieldByte());
        Assert.assertEquals(2, obj.getFieldShort());
        Assert.assertEquals(3, obj.getFieldInt());
        Assert.assertEquals(4L, obj.getFieldLong());
    }

    @Test
    public void testCreateGenericTypeCreators() {
        ObjectCreatorAsConcreteType<StructWithGenericField> creator =
                ObjectCreator.mkCreator(StructWithGenericField.class,
                        ObjectCreator.toJTypeArgsV(AllPrimitiveTypes.class));
        @SuppressWarnings("unchecked")
        StructWithGenericField<AllPrimitiveTypes> genericObj = ObjectCreator.cast(creator.newInstance());

        AllPrimitiveTypes obj = genericObj.getGenericField();
        Assert.assertEquals(1, obj.getFieldByte());
        Assert.assertEquals(2, obj.getFieldShort());
        Assert.assertEquals(3, obj.getFieldInt());
        Assert.assertEquals(4L, obj.getFieldLong());
    }
}
