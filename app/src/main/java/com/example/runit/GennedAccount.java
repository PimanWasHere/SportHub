package com.example.runit;

import com.hedera.hashgraph.sdk.BadMnemonicException;
import com.hedera.hashgraph.sdk.Mnemonic;
import com.hedera.hashgraph.sdk.PrivateKey;
import com.hedera.hashgraph.sdk.PublicKey;


public class GennedAccount {

    Mnemonic mnemonic;
    PrivateKey newPrivKey;
    PublicKey newPublicKey;


    public GennedAccount() throws BadMnemonicException {
        System.out.println("ok.. creating  new keys ..");
        mnemonic = Mnemonic.generate24();
        // Generate a Ed25519 private, public key pair
        newPrivKey = mnemonic.toPrivateKey();
        newPublicKey = newPrivKey.getPublicKey();
    }
}