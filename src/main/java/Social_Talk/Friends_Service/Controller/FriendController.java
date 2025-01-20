package Social_Talk.Friends_Service.Controller;

import Social_Talk.Friends_Service.Exception.FriendshipAlreadyExistsException;
import Social_Talk.Friends_Service.Exception.FriendshipNotFoundException;
import Social_Talk.Friends_Service.Exception.FriendshipPendingException;
import Social_Talk.Friends_Service.Model.Friend;
import Social_Talk.Friends_Service.Service.FriendService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/friends")
public class FriendController {

    @Autowired
    private FriendService friendService;

    @PostMapping("/add/{friendId}")
    public ResponseEntity<String> addFriend(@RequestParam Long userId, @PathVariable Long friendId) {
        try {
            friendService.addFriend(userId, friendId);
            return ResponseEntity.ok("Invitation has been sent.");
        } catch (FriendshipAlreadyExistsException ex) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body("This friendship is already accepted");
        } catch (FriendshipPendingException ex) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body("A friend request is already pending");
        } catch (IllegalStateException ex) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An unexpected error occurred");
        }
    }

    @PostMapping("/accept/{friendId}")
    public ResponseEntity<String> acceptFriendRequest(@RequestParam Long userId, @PathVariable Long friendId) {
        try {
            friendService.acceptFriendRequest(userId, friendId);
            return ResponseEntity.ok("Invitation has been accepted.");
        } catch (FriendshipNotFoundException ex) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ex.getMessage());
        }
    }

    @PostMapping("/reject/{friendId}")
    public ResponseEntity<String> rejectFriendRequest(@RequestParam Long userId, @PathVariable Long friendId) {
        try {
            friendService.rejectFriendRequest(userId, friendId);
            return ResponseEntity.ok("Invitation has been rejected.");
        } catch (FriendshipNotFoundException ex) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ex.getMessage());
        }
    }

    @GetMapping("/{userId}")
    public List<Friend> getFriend(@PathVariable Long userId) {
        return friendService.getFriend(userId);
    }

    @DeleteMapping("/remove/{friendId}")
    public ResponseEntity<String> removeFriend(@RequestParam Long userId, @PathVariable Long friendId) {
        try {
            friendService.removeFriend(userId, friendId);
            return ResponseEntity.ok("Friend has been deleted.");
        } catch (FriendshipNotFoundException ex) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ex.getMessage());
        }
    }

    @GetMapping("/accepted")
    public List<Friend> getFriends(@RequestParam Long userId) {
        return friendService.getAcceptedFriends(userId);
    }

    @GetMapping("/pending")
    public List<Friend> getPendingRequests(@RequestParam Long userId) {
        return friendService.getPendingFriendsReceived(userId);
    }

    @GetMapping("/isFriend")
    public ResponseEntity<Boolean> areFriends(
            @RequestParam int userId1,
            @RequestParam int userId2) {
        boolean areFriends = friendService.areUsersFriends(userId1, userId2);
        return ResponseEntity.ok(areFriends);
    }
}
