package jm.api.dao;


import jm.TokenRecovery;

public interface TokenRecoveryDAO {

    TokenRecovery getById(Long id);

    TokenRecovery getByHashEmail(String hash);

    void add(TokenRecovery tokenRecovery);

    void deleteById(Long id);
}
