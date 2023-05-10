package Asg7;

// This is the Buffer object. A buffer consists of a memory size, offset from start of memory, a flag designating its status,
// a reference to its buddy, and a reference to the pair's parent
public class Buffer {

    private int size; // power of 2 up to 512
    private int offset; // Dist from 0, if -1 it is not allocated yet
    private boolean isAllocated; // Defaulted to false. Become rue if its being used for memory
    Buffer buddy; // point to buddy buffer
    Buffer parent; //point to its parent

    // Constructor for buffer
    public Buffer(int size, int offset, boolean isAllocated, Buffer buddy){
        this.size = size;
        this.offset = offset;
        this.isAllocated = isAllocated;
        this.buddy = buddy;
    }

    // Getter & setter for a buffer's size
    public int getSize(){
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }

    // Getter & setter for a buffer's offset
    public int getOffset(){
        return offset;
    }

    public void setOffset(int offset){
        this.offset = offset;
    }

    // Getter & setter for a buffer's allocation status
    public boolean isAllocated() {
        return isAllocated;
    }

    public void setAllocation(boolean allocated) {
        isAllocated = allocated;
    }

    // Getter & setter for a buffer's buddy
    public Buffer getBuddy() {
        return buddy;
    }

    public void setBuddy(Buffer buddy) {
        this.buddy = buddy;
    }

    // Getter & setter for a buffer's parent
    public Buffer getParent() {
        return parent;
    }

    public void setParent(Buffer parent) {
        this.parent = parent;
    }

    // toString used for debugging
    public String toString(){
        return "size: " + size + "\nOffset: " + offset + "\nIs it allocated?: " + isAllocated;
    }

}//end class
