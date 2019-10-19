package com.auth.Wrapper;

import com.jd.blockchain.crypto.*;

public class SM3Wrapper {
    private CryptoAlgorithm cryptoAlgorithm;
    private HashFunction hashFunction;
    public SM3Wrapper(){
        cryptoAlgorithm = Crypto.getAlgorithm("sm3");
        assert cryptoAlgorithm != null;
        hashFunction = Crypto.getHashFunction(cryptoAlgorithm);
    }

    public byte[] hash(byte[] data) {
        HashDigest hashDigest = hashFunction.hash(data);
        return hashDigest.getRawDigest();
    }
}
