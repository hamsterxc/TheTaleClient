package com.lonebytesoft.thetaleclient.util;

import java.util.ArrayList;

/**
 * An implementation of navigation backstack.
 * You can set a current item. If that item was not in history, it will be added there.
 * If an item to be set was already in stack, history will be reset to that item.
 * You can pop an item, a new stack head will be returned or null, if stack becomes empty.
 *
 * E.g.
 * set 1 -> (1)
 * set 2 -> (1, 2)
 * pop -> 1, (1)
 * set 3 -> (1, 3)
 * set 4 -> (1, 3, 4)
 * set 1 -> (1)
 * pop -> null, ()
 *
 * @author Hamster
 * @since 27.10.2014
 */
public class HistoryStack<T> {

    /**
     * Container for items
     */
    private final ArrayList<T> stack;

    /**
     * Index of current item in container
     */
    private int index = -1;

    public HistoryStack(final int capacity) {
        stack = new ArrayList<>(capacity);
    }

    public HistoryStack() {
        stack = new ArrayList<>();
    }

    /**
     * Set the current item
     * @param item item to set
     */
    public void set(final T item) {
        final int itemIndex = stack.indexOf(item);
        if(itemIndex == -1) {
            index = stack.size();
            stack.add(item);
        } else {
            index = itemIndex;
        }
    }

    /**
     * Pop an item from the container
     * @return popped item
     */
    public T pop() {
        if(index <= 0) {
            return null;
        } else {
            return stack.get(--index);
        }
    }

}
