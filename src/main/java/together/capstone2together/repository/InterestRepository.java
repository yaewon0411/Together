package together.capstone2together.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import together.capstone2together.domain.Interest;

import java.util.List;

@Repository
public interface InterestRepository extends JpaRepository<Interest,Long> {

}
