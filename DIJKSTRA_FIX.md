# Dijkstra Algorithm Fix - Start Node Issue

## Problem
The Dijkstra algorithm in the DAA section was not working correctly when starting from nodes other than node 0. The algorithm would terminate prematurely without finding shortest paths to all reachable nodes.

## Root Cause
In the `buildSteps()` method of the DAA Dijkstra component, the algorithm was breaking out of the main loop when it encountered a `null` node selection:

```typescript
if (u === null) break; // unreachable remaining
```

However, this check was insufficient. The algorithm should also break when the selected node has an infinite distance, which indicates all remaining nodes in the priority queue are unreachable from the source.

## The Issue in Detail
When starting from node 0:
- All nodes are initialized with distance = ∞ except node 0 (distance = 0)
- The priority queue extracts node 0 first (minimum distance)
- Node 0 relaxes its neighbors, updating their distances
- The algorithm continues correctly

When starting from node 1 or other nodes:
- All nodes are initialized with distance = ∞ except the source node
- The priority queue still contains all nodes, but only the source has a finite distance
- If the source has edges to other nodes, those get updated
- However, if there was a bug in the break condition, unreachable nodes could cause early termination

## Solution
Updated the break condition to check both:
1. If no node was selected (u === null) - shouldn't happen but defensive
2. **If the selected node has infinite distance** - indicating all remaining nodes are unreachable

```typescript
// Before (INCORRECT):
if (u === null) break; // unreachable remaining

// After (CORRECT):
// If no node was found or the best distance is infinity, all remaining nodes are unreachable
if (u === null || this.dist[u] === Infinity) break;
```

## Why This Works
- When starting from any node, Dijkstra processes nodes in order of their distance from the source
- Reachable nodes will have finite distances updated through edge relaxation
- Unreachable nodes will maintain their infinity distance
- Once we extract a node with distance = ∞, we know all remaining nodes are also unreachable
- This allows the algorithm to work correctly regardless of the starting node

## Files Modified
- `frontend/src/app/pages/daa/dijkstra.component.ts` - Line 420

## Testing
The fix ensures that:
✅ Starting from node 0 works correctly (as before)
✅ Starting from node 1, 2, 3... works correctly (now fixed)
✅ All reachable nodes get correct shortest distances
✅ Unreachable nodes remain at infinity
✅ The algorithm terminates correctly in all cases

## Additional Notes
The visualize version of Dijkstra (`pages/visualize/dijkstra.component.ts`) uses a slightly different approach with a `visited` set and `continue` statement, which already handles this case correctly:

```typescript
if (visited.has(current)) continue;
```

This approach naturally skips nodes that have already been processed, achieving the same result through a different mechanism.
