package io.bhpw3j.wallet;

import io.bhpw3j.protocol.core.methods.response.BhpGetBrc5Balances;
import io.bhpw3j.protocol.core.methods.response.BhpGetUnspents;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class Balances {

    private Map<String, AssetBalance> assetBalances;
    private Map<String, BigDecimal> tokenBalances;
    private Account account;

    public Balances(Account account) {
        this.account = account;
        this.assetBalances = new HashMap<>();
        this.tokenBalances = new HashMap<>();
    }

    /**
     * Overrides the asset balances with the provided information in the unspents.
     * @param unspents The unspents fetched from a RPC node.
     */
    public void updateAssetBalances(BhpGetUnspents.Unspents unspents) {
        if (!unspents.getAddress().equals(account.getAddress())) {
            throw new IllegalArgumentException("The provided unspents are not related to the " +
                    "account of this balance");
        }
        assetBalances = new HashMap<>(unspents.getBalances().size());
        unspents.getBalances().forEach(b -> {
            List<Utxo> utxos = b.getUnspentTransactions().stream()
                    .map(utxo -> new Utxo(b.getAssetHash(), utxo.getTxId(), utxo.getIndex(), utxo.getValue()))
                    .collect(Collectors.toList());
            assetBalances.put(b.getAssetHash(), new AssetBalance(utxos));
        });
    }

    public boolean hasAsset(String assetId) {
        return assetBalances.containsKey(assetId);
    }

    public AssetBalance getAssetBalance(String assetId) {
        return assetBalances.get(assetId);
    }

    /**
     * Overrides the token balances with the provided balances.
     * @param balances The BRC5 token balances fetched from a RPC node.
     */
    public void updateTokenBalances(BhpGetBrc5Balances.Balances balances) {
        if (!balances.getAddress().equals(account.getAddress())) {
            throw new IllegalArgumentException("The provided token balances are not related to the " +
                    "account of this balance");
        }
        this.tokenBalances = new HashMap<>(balances.getBalances().size());
        balances.getBalances().forEach(
                b -> this.tokenBalances.put(b.getAssetHash(), new BigDecimal(b.getAmount())));
    }

    public boolean hasToken(String tokenId) {
        return tokenBalances.containsKey(tokenId);
    }

    public BigDecimal getTokenBalance(String tokenId) {
        return tokenBalances.get(tokenId);
    }

    public static class AssetBalance {

        List<Utxo> utxos;
        BigDecimal amount;

        public AssetBalance(List<Utxo> utxos, BigDecimal amount) {
            this.utxos = utxos;
            this.amount = amount;
        }

        public AssetBalance(List<Utxo> utxos) {
            this.utxos = utxos;
            calculateAmountFormUtxos();
        }

        public List<Utxo> getUtxos() {
            return utxos;
        }

        public BigDecimal getAmount() {
            if (amount == null) {
                calculateAmountFormUtxos();
            }
            return amount;
        }

        public void calculateAmountFormUtxos() {
            amount = utxos.stream()
                    .map(Utxo::getValue)
                    .reduce(BigDecimal::add)
                    .orElse(BigDecimal.ZERO);
        }
    }
}
