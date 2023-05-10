package Asg7;

// A buddy pair creates 2 buffers at the time of its creation.
// Its purpose is to set the buffers as each other's buddy.
public class BuddyPair {

    Buffer left;
    Buffer right;

    public BuddyPair(int LRBufferSizes){
        // Create left & right buffers
        left = new Buffer(LRBufferSizes,-1, false, null);
        right = new Buffer(LRBufferSizes, -1, false, left);
        left.setBuddy(right);
    }

}
