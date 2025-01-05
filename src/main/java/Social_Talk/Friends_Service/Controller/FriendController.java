package Social_Talk.Friends_Service.Controller;

import Social_Talk.Friends_Service.Exception.FriendshipAlreadyExistsException;
import Social_Talk.Friends_Service.Exception.FriendshipNotFoundException;
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
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ex.getMessage());
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

    @GetMapping("/{userId}")
    public List<Friend> getFriends(@PathVariable Long userId) {
        return friendService.getFriends(userId);
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
}
