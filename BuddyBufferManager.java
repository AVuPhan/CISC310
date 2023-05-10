package Asg7;

import java.util.*;

public class BuddyBufferManager {
    private int maxBufferSize; // maximum buffer size
    private int[] bufferPool; // the actual memory model
    private int maxBuffers; // maximum number of buffers
    private ArrayList<Buffer> bufferList = new ArrayList<Buffer>(); // keep track of all the buffers
    private int memoryTightFlag = 0; // 0 for ok, 1 for tight (less than 2 spaces availible)

    // Initializes the buffer pool and linked lists
    public BuddyBufferManager() {
        // Initialize everything
        maxBufferSize = 512;
        maxBuffers = 10;
        bufferPool = new int[maxBufferSize];
    }

    // Returns an offset of a buffer of requested size (in words) or -1 if cannot provide a buffer of that size
    public int allocateBuffer(int requestedSize) {
        // Check if space available for more buffers
        if(bufferList.size() >= maxBuffers){
            return -2;
        }

        // Check if requested size is illegal
        if (requestedSize > 512 || requestedSize < 7){
            return -2; // illegal request
        }

        // Find the smallest buffer size that can satisfy the request
        int smallestSizeBufferPower = (int)Math.ceil(Math.log(requestedSize) / Math.log(2));
        int targetBufferSize = (int) Math.pow(2,smallestSizeBufferPower);

        //find available buffer. If no, then create new volume
        if(bufferList.isEmpty()){
            Buffer root = new Buffer(512,0,false,null);
            bufferList.add(root);
        }
        //Traverse through buffer list
        int i = 0;
        boolean available = false;
        for(; i < bufferList.size(); i++){
            // If there exists a buffer of correct size and is not already allocated, use it
            if(bufferList.get(i).getSize() == targetBufferSize && !bufferList.get(i).isAllocated()){
                available = true;
                break;
            }
        }
        // If the for loops above ends without finding pre-existing buffer w/ exact size, then find next available buffer w/ bigger size
        if(!available){
            int j = 0;
            boolean stillAvailable = false;
            for(; j < bufferList.size(); j++){
                if(bufferList.get(j).getSize() > targetBufferSize){
                    stillAvailable = true;
                    break;
                }
            }
            // If for loop above ends without finding a buffer w/ size bigger than target size, there is no space left
            if(!stillAvailable){
                return -1;
            }
            // Extract the target buffer & remove it from the list
            Buffer temp = bufferList.get(j);
            bufferList.remove(bufferList.get(j));
            // Slice the target buffer down until it is the correct size
            while(temp.getSize() != targetBufferSize){
                temp = sliceBuffer(temp);
            }
            //if it breaks out of while loop, then it broke down to correct size. Set its allocation & return it
            temp.setAllocation(true);
            return temp.getOffset();
        }
        else {
            // If for loop finds buffer with correct size, (available == true) set its allocation & return it
            bufferList.get(i).setAllocation(true);
        }
        return bufferList.get(i).getOffset();
    }//end allocate

    //This method takes in a buffer and splits it in two smaller buffers
    public Buffer sliceBuffer(Buffer b){
        int half = b.getSize()>>1;
        BuddyPair bp = new BuddyPair(half); // new buddy buffer pair
        bp.left.setOffset(b.getOffset()); // left buffer's offset is the same as original's
        bp.right.setOffset(b.getOffset() + half); // right buffer's offset is another half
        bp.left.setParent(b); // set left buffer's parent to the buffer that was passed in
        bp.right.setParent(b); // set right buffer's parent to the buffer that was passed in
        bufferList.add(bp.left); // add to list
        bufferList.add(bp.right);
        bufferList.remove(b); // remove parent from list
        return bp.left;
    }//end slice


    // This method releases a buffer at the specified address & combines unallocated memory into bigger chunks
    public void deallocateBuffer(int address) {
        // Search via address for the buffer that we want to remove
        int i = 0;
        for(; i < bufferList.size(); i++){
            if(bufferList.get(i).getOffset() == address){ // found the buffer
                break;
            }
        }
        // Set its allocation to false
        bufferList.get(i).setAllocation(false);

        // Go through buffer list again
        for(int j = 0; j < bufferList.size(); j++){
            // If it has a buddy that is also not currently allocating memory, then coalesce
            try{
                // keep combining buffers until it does not have an availible buddy
                while(bufferList.get(i).getBuddy().equals(bufferList.get(j)) && !bufferList.get(j).isAllocated()){
                    bufferList.add(coalesce(bufferList.get(i), bufferList.get(j)));
                }
            } catch (IndexOutOfBoundsException e){
                // If this error is thrown then the buffer list has reduced enough where the original for loop
                // will be out of bounds. Catch & break out of the for loop.
                break;
            }

        }

        // Check of the memory is tight
        int total = 0;
        // Traverse through the list. If a buffer in the list is allocated, increment the counter
        for(int k = 0; k < bufferList.size(); k++){
            if(bufferList.get(k).isAllocated()){
                total++;
            }
        }
        // Check if there are 2 or fewer available slots left.
        if(total >= 7){
            memoryTightFlag = 1;
        }
        else{
            memoryTightFlag = 0;
        }
    }//end deallocate

    // This method takes 2 unallocated buffers and replaces them with their parent
    public Buffer coalesce(Buffer a, Buffer b){
        // remove a and b from list. return its parent
        Buffer parent = a.getParent();
        if(a.getOffset() < b.getOffset()){
            parent.setOffset(a.getOffset());
        }else {
            parent.setOffset(b.getOffset());
        }
        parent.setAllocation(false);
        bufferList.remove(a);
        bufferList.remove(b);
        return parent;
    }

    // This method prints the memory tight flag
    public String getManagerStatus(){
        String ret = "Status:\n";
        int total = 0;
        for(int k = 0; k < bufferList.size(); k++){
            if(bufferList.get(k).isAllocated()){
                total++;
            }
        }
        if(total >= 7){
            memoryTightFlag = 1;
        }
        else{
            memoryTightFlag = 0;
        }
        if(memoryTightFlag == 0){
            return ret + "Ok\n";
        }
        return ret + "Tight\n";
    }

    // This method prints out how many buffers of various sizes are available.
    public String debug(){
        int five12Ctr = 0;
        int two56Ctr = 0;
        int one28Ctr = 0;
        int sixty4Ctr = 0;
        int thirty2Ctr = 0;
        int sixteenCtr = 0;
        int eightCtr = 0;
        String ret = "Free Buffer Count:\n";
        for(int i = 0; i < bufferList.size(); i++){
            if(!bufferList.get(i).isAllocated()){
                if(bufferList.get(i).isAllocated() && bufferList.get(i).getSize() == 512){
                    five12Ctr++;
                }else if(bufferList.get(i).getSize() == 256){
                    two56Ctr++;
                }else if(bufferList.get(i).getSize() == 128){
                    one28Ctr++;
                }else if(bufferList.get(i).getSize() == 64){
                    sixty4Ctr++;
                }else if(bufferList.get(i).getSize() == 32){
                    thirty2Ctr++;
                }else if(bufferList.get(i).getSize() == 16){
                    sixteenCtr++;
                }else{
                    eightCtr++;
                }
            }
        }
        ret = ret + five12Ctr + " 512 size buffers\n";
        ret = ret + two56Ctr + " 256 size buffers\n";
        ret = ret + one28Ctr + " 128 size buffers\n";
        ret = ret + sixty4Ctr + " 64 size buffers\n";
        ret = ret + thirty2Ctr + " 32 size buffers\n";
        ret = ret + sixteenCtr + " 16 size buffers\n";
        ret = ret + eightCtr + " 8 size buffers\n";
        return ret;
    }

}//end class