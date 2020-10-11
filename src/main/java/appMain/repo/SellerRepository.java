package appMain.repo;

import appMain.entities.Seller;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.UUID;

public interface SellerRepository extends JpaRepository<Seller, UUID> {
    @Query("SELECT s FROM Seller s WHERE s.firstName = :firstName AND s.lastName = :lastName")
    Seller findSellerByFirstNameAndLastName(@Param("firstName") String firstName, @Param("lastName") String lastName);
}
