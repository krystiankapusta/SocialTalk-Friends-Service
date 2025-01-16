package Social_Talk.Friends_Service.Service;

import Social_Talk.Friends_Service.Exception.FriendShipRejectException;
import Social_Talk.Friends_Service.Exception.FriendshipAlreadyExistsException;
import Social_Talk.Friends_Service.Exception.FriendshipNotFoundException;
import Social_Talk.Friends_Service.Exception.FriendshipPendingException;
import Social_Talk.Friends_Service.Model.Friend;
import Social_Talk.Friends_Service.Repository.FriendRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;


@Service
public class FriendService {

    @Autowired
    private FriendRepository friendRepository;

    @Transactional
    public void addFriend(Long userId, Long friendId) {
        Optional<Friend> existingFriendship = friendRepository.findByUserIdAndFriendId(userId, friendId);

        if (existingFriendship.isPresent()) {
            Friend friend = existingFriendship.get();
            if (friend.getStatus() == Friend.FriendStatus.accepted) {
                throw new FriendshipAlreadyExistsException("Friendship already accepted");
            }
            if (friend.getStatus() == Friend.FriendStatus.pending) {
                throw new FriendshipPendingException("Friend request already pending");
            }

            friend.setStatus(Friend.FriendStatus.pending);
            friendRepository.save(friend);
            return;
        }

        Friend friend = new Friend();
        friend.setUserId(userId);
        friend.setFriendId(friendId);
        friend.setStatus(Friend.FriendStatus.pending);

        friendRepository.save(friend);
    }

    @Transactional
    public void acceptFriendRequest(Long userId, Long friendId) {
        Friend friend = friendRepository.findByUserIdAndFriendId(userId, friendId)
                .orElseThrow(() -> new FriendshipNotFoundException("No invitation to accept"));

        if (friend.getStatus() == Friend.FriendStatus.accepted) {
            throw new IllegalStateException("Friendship already accepted");
        }

        friend.setStatus(Friend.FriendStatus.accepted);
        friendRepository.save(friend);
    }

    @Transactional
    public void rejectFriendRequest(Long userId, Long friendId) {
        Friend friend = friendRepository.findByUserIdAndFriendId(userId, friendId)
                .orElseThrow(() -> new FriendshipNotFoundException("No invitation to reject"));

        if (friend.getStatus() == Friend.FriendStatus.rejected) {
            throw new FriendShipRejectException("Invitation already rejected");
        }

        friend.setStatus(Friend.FriendStatus.rejected);
        friendRepository.save(friend);
    }

    public List<Friend> getFriend(Long userId) {
        return friendRepository.findByUserId(userId);
    }

    public List<Friend> getAcceptedFriends(Long userId) {
        return friendRepository.findByUserIdAndStatus(userId, Friend.FriendStatus.accepted);
    }

    public List<Friend> getPendingFriends(Long userId) {
        return friendRepository.findByUserIdAndStatus(userId, Friend.FriendStatus.pending);
    }

    @Transactional
    public void removeFriend(Long userId, Long friendId) {
        Friend friend = friendRepository.findByUserIdAndFriendId(userId, friendId)
                .orElseThrow(() -> new FriendshipNotFoundException("Friendship does not exist"));

        friendRepository.delete(friend);
    }

    public boolean areUsersFriends(int userId1, int userId2) {
        return friendRepository.existsByUserIdAndFriendIdAndStatus(userId1, userId2, Friend.FriendStatus.accepted) ||
                friendRepository.existsByUserIdAndFriendIdAndStatus(userId2, userId1, Friend.FriendStatus.accepted);
    }

}
