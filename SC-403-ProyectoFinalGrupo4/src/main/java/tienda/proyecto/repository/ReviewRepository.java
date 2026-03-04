package tienda.proyecto.repository;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import tienda.proyecto.domain.Review;

public interface ReviewRepository extends JpaRepository<Review, Integer> {

    public List<Review> findByActivoTrue();
}
