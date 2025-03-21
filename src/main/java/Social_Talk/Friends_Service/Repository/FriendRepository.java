package Social_Talk.Friends_Service.Repository;

import Social_Talk.Friends_Service.Model.Friend;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface FriendRepository extends JpaRepository<Friend, Long> {
    Optional<Friend> findByUserIdAndFriendId(Long userId, Long friendId);

    List<Friend> findByUserId(Long userId);
    List<Friend> findByFriendId(Long friendId);


    boolean existsByUserIdAndFriendIdAndStatus(int userId, int friendId, Friend.FriendStatus status);

    boolean existsByFriendIdAndUserIdAndStatus(int friendId, int userId, Friend.FriendStatus status);


    List<Friend> findByUserIdAndStatus(Long userId, Friend.FriendStatus friendStatus);

    
    List<Friend> findByFriendIdAndStatus(Long friendId, Friend.FriendStatus status);

    @Query("SELECT f FROM Friend f " +
            "WHERE (f.userId = :userId OR f.friendId = :userId) " +
            "AND f.status = :status")
    List<Friend> findFriendsByUserIdAndStatus(Long userId, Friend.FriendStatus status);
}
