package spring.repositories;

import org.springframework.data.repository.CrudRepository;
import spring.entities.User;
import spring.entities.BitcoinWallet;

import java.util.Set;

public interface WalletRepository extends CrudRepository<BitcoinWallet, Long> {
    Set<BitcoinWallet> getWalletByOwner(User owner);
}
