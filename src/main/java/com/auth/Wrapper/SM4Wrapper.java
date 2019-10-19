package com.auth.Wrapper;

import com.jd.blockchain.crypto.*;
import com.jd.blockchain.utils.io.BytesUtils;

public class SM4Wrapper {
    private CryptoAlgorithm cryptoAlgorithm;
    private SymmetricEncryptionFunction symmetricEncryptionFunction;
    public SM4Wrapper(){
        cryptoAlgorithm = Crypto.getAlgorithm("sm4");
        assert cryptoAlgorithm != null;
        symmetricEncryptionFunction = Crypto.getSymmetricEncryptionFunction(cryptoAlgorithm);
    }

    public byte[] encrypt(byte[] sm4key, byte[] plaintext) {
        SymmetricKey symmetricKey = new SymmetricKey(cryptoAlgorithm, sm4key);
        assert symmetricEncryptionFunction.supportSymmetricKey(symmetricKey.toBytes());

        Ciphertext ciphertext = symmetricEncryptionFunction.encrypt(symmetricKey, plaintext);
        return ciphertext.getRawCiphertext();
    }

    public byte[] decrypt(byte[] sm4key, byte[] ciphertext) {
        SymmetricKey symmetricKey = new SymmetricKey(cryptoAlgorithm, sm4key);
        assert symmetricEncryptionFunction.supportSymmetricKey(symmetricKey.toBytes());

        byte[] sm4CiphertextBytes = BytesUtils.concat(CryptoAlgorithm.getCodeBytes(cryptoAlgorithm), ciphertext);
        assert symmetricEncryptionFunction.supportCiphertext(sm4CiphertextBytes);

        Ciphertext resolvedCiphertext = symmetricEncryptionFunction.resolveCiphertext(sm4CiphertextBytes);
        return symmetricEncryptionFunction.decrypt(symmetricKey, resolvedCiphertext);
    }
}
