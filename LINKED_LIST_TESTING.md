# Linked List Testing Checklist

## Quick Test Guide

### 1. Singly Linked List (Default)
- [ ] Insert at head: Should show green → arrow
- [ ] Insert at tail: Should show NULL at end
- [ ] Insert at index: Should maintain → chain
- [ ] Delete head: Should update HEAD pointer
- [ ] Delete tail: Should show NULL at new tail
- [ ] Search: Should traverse one direction
- [ ] Visual: Nodes 120px wide, 3rem gap

### 2. Circular Linked List
**Switch radio button to "Circular Linked"**
- [ ] Insert at head: Last node should show ↻
- [ ] Insert at tail: Should maintain circular connection
- [ ] Insert at index: ↻ indicator stays on last node
- [ ] Delete head: New head maintains circular link
- [ ] Delete tail: Previous node becomes new tail with ↻
- [ ] Delete last node: Should handle single-node circular list
- [ ] Visual: Orange rotating ↻ on last node, no NULL

### 3. Doubly Linked List
**Switch radio button to "Doubly Linked"**
- [ ] Insert at head: Should show ← prev pointer
- [ ] Insert at tail: Should show both ← and →
- [ ] Insert at index: Should maintain bidirectional links
- [ ] Delete head: New head should have NULL ← at start
- [ ] Delete tail: New tail should have NULL → at end
- [ ] Visual: Nodes 140px wide, blue ← arrows, green → arrows

## Edge Cases to Test

### Empty List Operations
- [ ] Insert head on empty list (all types)
- [ ] Insert tail on empty list (all types)
- [ ] Delete from empty list (should be disabled)
- [ ] Search on empty list

### Single Node Operations
- [ ] Singly: Delete only node
- [ ] Circular: Single node pointing to itself
- [ ] Doubly: Single node with NULL on both sides

### Type Switching
- [ ] Switch from singly to circular with nodes
- [ ] Switch from circular to doubly with nodes
- [ ] Switch from doubly to singly with nodes
- [ ] Radio buttons disabled during animation

### UI/UX Tests
- [ ] Radio buttons are visible and styled
- [ ] Type label "List Type:" is prominent
- [ ] Nodes have proper spacing (3rem gap)
- [ ] Visualization is centered on page
- [ ] Controls are below visualization
- [ ] Caption updates with list type name
- [ ] Animations work on all list types

## Visual Verification

### Node Appearance
```
Singly:  [Value] →
Circular: [Value] ↻ (last node)
Doubly:  ← [Value] →
```

### Spacing Check
- Radio buttons to visualization: ~2rem
- Between nodes: 3rem
- Nodes vertically centered
- No horizontal scrolling on desktop

### Color Check
- Radio button selected: Purple (#8b5cf6)
- Next arrows: Green (#10b981)
- Prev arrows: Blue (#3b82f6)
- Circular arrow: Orange (#f59e0b)
- NULL text: Red (#ef4444)

## Performance Tests

### No Infinite Loops
- [ ] Circular list traversal stops correctly
- [ ] getVisualNodes() doesn't hang
- [ ] getTail() returns correct node
- [ ] Search doesn't loop forever

### Animation Smoothness
- [ ] Node insertion animates smoothly
- [ ] Particle effects work
- [ ] No lag when switching types
- [ ] Radio buttons disable properly

## Caption Verification

Each operation should show list type in caption:
- ✓ "INSERT AT HEAD (SINGLY)"
- ✓ "DELETE AT TAIL (CIRCULAR)"
- ✓ "INSERT AT INDEX 2 (DOUBLY)"

## Browser Compatibility
- [ ] Chrome/Edge: All features work
- [ ] Firefox: All features work
- [ ] Safari: All features work
- [ ] Mobile: Touch interactions work

## Expected Behavior Summary

| Operation | Singly | Circular | Doubly |
|-----------|--------|----------|--------|
| Insert head | New head → old head | Last ↻ to new head | Old head.prev = new |
| Insert tail | Traverse to end | New tail ↻ to head | New tail.prev = old tail |
| Delete head | head = head.next | Tail ↻ to new head | New head.prev = null |
| Delete tail | Traverse, set NULL | Prev ↻ to head | New tail.next = null |

## Common Issues to Watch For

1. **Circular infinite loops**: Ensure visited Set prevents cycles
2. **Doubly prev pointer**: Must update on all insertions/deletions
3. **Radio button state**: Should disable during animations
4. **Layout overflow**: Nodes should wrap, not overflow
5. **Type switching**: Should preserve or clear data appropriately

## Success Criteria

✓ All three list types display correctly
✓ Radio buttons switch types seamlessly
✓ Visual indicators match list type (→, ←, ↻)
✓ No errors in browser console
✓ Layout is responsive and centered
✓ Animations work smoothly
✓ Captions include list type
✓ No infinite loops in circular list
✓ Prev pointers work in doubly list

## Quick Demo Steps

1. **Open Linked List page**
2. **Default (Singly)**: Insert 3-4 nodes, observe → arrows and NULL
3. **Switch to Circular**: Observe last node shows ↻
4. **Insert more nodes**: Verify ↻ stays on last node
5. **Switch to Doubly**: Observe ← arrows appear, nodes wider
6. **Delete operations**: Verify pointers update correctly
7. **Switch back to Singly**: Verify clean transition

## Screenshot Checklist

Take screenshots of:
- [ ] Empty list with all three radio options
- [ ] Singly linked list with 5 nodes
- [ ] Circular linked list showing ↻ indicator
- [ ] Doubly linked list showing ← and → arrows
- [ ] Radio button selector with different selections
- [ ] Mobile view showing wrapped nodes
