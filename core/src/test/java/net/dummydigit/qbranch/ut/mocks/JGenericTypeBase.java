package net.dummydigit.qbranch.ut.mocks;

import net.dummydigit.qbranch.QBranchSerializable;
import net.dummydigit.qbranch.annotations.QBranchGeneratedCode;
import net.dummydigit.qbranch.generic.QTypeArg;
import net.dummydigit.qbranch.generic.StructT;

@QBranchGeneratedCode(compilerName = "mock", version = "version.mock")
public class JGenericTypeBase<T1, T2> implements QBranchSerializable {

    public JGenericTypeBase(JGenericTypeBaseT<T1, T2> qTypeArgs) {
        baseFieldT1 = qTypeArgs.getBaseFieldT1().newInstance();
        baseFieldT2 = qTypeArgs.getBaseFieldT2().newInstance();
    }

    private JGenericTypeBase() { }

    private T1 baseFieldT1;
    private T2 baseFieldT2;

    public static class JGenericTypeBaseT<T1, T2> extends StructT<JGenericTypeBase<T1, T2>> {

        public JGenericTypeBaseT(QTypeArg<T1> qTypeArgT1, QTypeArg<T2> qTypeArgT2) {
            this.baseFieldT1 = qTypeArgT1;
            this.baseFieldT2 = qTypeArgT2;
            this.refObj = new JGenericTypeBase<>(); // Light weight object. Just to get class
        }

        @Override
        public StructT<?> getBaseClassT () {
            return null;
        }

        @Override
        public JGenericTypeBase<T1, T2> newInstance() {
            return new JGenericTypeBase<>(this);
        }

        @Override
        @SuppressWarnings("unchecked")
        public Class<JGenericTypeBase<T1, T2>> getGenericType() {
            return (Class<JGenericTypeBase<T1, T2>>)refObj.getClass();
        }

        private QTypeArg<T1> baseFieldT1;
        private QTypeArg<T2> baseFieldT2;
        private JGenericTypeBase<T1, T2> refObj;

        public QTypeArg<T1> getBaseFieldT1() {
            return baseFieldT1;
        }

        public QTypeArg<T2> getBaseFieldT2() {
            return baseFieldT2;
        }
    }

    public static<T1, T2> JGenericTypeBaseT asQTypeArg(QTypeArg<T1> qTypeArgT1, QTypeArg<T2> qTypeArgT2) {
        return new JGenericTypeBaseT<>(qTypeArgT1, qTypeArgT2);
    }
}

