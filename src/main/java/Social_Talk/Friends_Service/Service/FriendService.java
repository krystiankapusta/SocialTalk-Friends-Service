package Social_Talk.Friends_Service.Service;

import Social_Talk.Friends_Service.Exception.FriendshipAlreadyExistsException;
import Social_Talk.Friends_Service.Exception.FriendshipNotFoundException;
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
            System.out.println("Friendship exists: " + existingFriendship.get());
            throw new FriendshipAlreadyExistsException("Friendship exists");
        }

        Friend friend = new Friend();
        friend.setUserId(userId);
        friend.setFriendId(friendId);
        friend.setStatus(Friend.FriendStatus.pending);

        friendRepository.save(friend);
    }

    @Transactional
    public void acceptFriendRequest(Long userId, Long friendId) {
        Optional<Friend> existingFriendship = friendRepository.findByUserIdAndFriendId(userId, friendId);

        if (!existingFriendship.isPresent() || existingFriendship.get().getStatus() == Friend.FriendStatus.accepted) {
            throw new FriendshipNotFoundException("No invitation to accept");
        }

        Friend friend = existingFriendship.get();
        friend.setStatus(Friend.FriendStatus.accepted);
        friendRepository.save(friend);
    }

    public List<Friend> getFriends(Long userId) {
        return friendRepository.findByUserId(userId);
    }

    @Transactional
    public void removeFriend(Long userId, Long friendId) {
        Optional<Friend> existingFriendship = friendRepository.findByUserIdAndFriendId(userId, friendId);

        if (!existingFriendship.isPresent()) {
            throw new FriendshipNotFoundException("Friendship not exist");
        }

        friendRepository.delete(existingFriendship.get());
    }
}
