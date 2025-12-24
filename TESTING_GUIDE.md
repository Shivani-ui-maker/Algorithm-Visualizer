# Visual Testing Guide - Graph Algorithms

## Quick Test Steps

### 1. Start the Application
```bash
# Terminal 1 - Backend
cd algorithm-visualizer/backend
./mvnw spring-boot:run

# Terminal 2 - Frontend
cd algorithm-visualizer/frontend
npm start
```

### 2. Test BFS Visualization
1. Navigate to: `http://localhost:4200/visualize/bfs`
2. **Expected**: 
   - Graph with 6 nodes in circular layout
   - Start node (Node 0) has blue border
   - All edges visible in gray

3. Click "Step" button repeatedly:
   - **First step**: Node 0 turns **YELLOW** with glow
   - **Queue updates**: Shows [0] then neighbors
   - **Edge highlight**: Edge from current to neighbor turns **YELLOW**
   - **Node visited**: After processing, node turns **GREEN**
   - **Result array**: Green boxes show traversal order: [0, 1, 2, ...]

4. Click "Randomize":
   - **Expected**: New graph with all edges visible
   - **Check**: No missing connections between nodes

5. Change start node to 3:
   - **Expected**: Clean reset, Node 3 has blue border
   - **Check**: No leftover colors from previous run

### 3. Test DFS Visualization
1. Navigate to: `http://localhost:4200/visualize/dfs`
2. **Expected**: Same visual quality as BFS
3. Repeat steps from BFS test
4. **Difference**: Stack visualization instead of queue

### 4. Visual Checklist

#### Node Colors ✅
- [ ] Dark gray = Unvisited
- [ ] **Bright yellow with glow** = Currently visiting
- [ ] **Solid green** = Visited/processed
- [ ] Blue border = Start node
- [ ] Pink dashed border = In queue/stack

#### Edge Colors ✅
- [ ] Gray (thin) = Unvisited
- [ ] **Yellow (thick)** = Actively traversing
- [ ] **Green (medium)** = Visited path

#### Animations ✅
- [ ] Visiting nodes pulse (scale up/down)
- [ ] Active edges pulse (opacity)
- [ ] Queued nodes have animated dashed border

#### Result Display ✅
- [ ] Green boxes show each visited node
- [ ] Text shows: `res[] = [0, 1, 2, 3, 4, 5]`
- [ ] Updates in real-time with each step

### 5. Edge Visibility Test

**Critical Test**: Check if all edges are rendered

1. Click "Randomize" 5 times
2. For each graph:
   - Count edges in the visualization
   - Check if any nodes are isolated (no connections)
   - Verify edges go in multiple directions

**Expected**: Every node should have at least one visible edge

### 6. Common Issues to Watch For

#### ❌ Problem: "Edges disappear during animation"
**Check**: Are edges turning green after traversal?
**Expected**: Yes, visited edges should be green and visible

#### ❌ Problem: "Node colors unclear"
**Check**: Is the yellow bright enough?
**Expected**: Yellow nodes should have glow effect and be clearly distinguishable

#### ❌ Problem: "Can't see path from Node A to Node B"
**Check**: Look for yellow edge during step animation
**Expected**: Yellow edge should flash when traversing that path

#### ❌ Problem: "Bar chart appears"
**Check**: What URL are you visiting?
**Fix**: Use `/visualize/bfs` or `/visualize/dfs` (not `/visualize?algo=bfs`)

### 7. Screenshot Comparison

#### Good Visualization:
```
✅ Yellow node with glow effect
✅ Yellow edge connecting to next node
✅ Green visited nodes clearly visible
✅ Result array showing [0, 1, 2, ...]
✅ Queue/Stack panel synchronized
```

#### Bad Visualization:
```
❌ Nodes same color (no clear state)
❌ Edges invisible or gray during traversal
❌ No result array display
❌ Bar chart interface instead of graph
```

### 8. Performance Test

1. Change graph size to 15 nodes
2. Click "Play" (auto-step)
3. **Expected**: Smooth animation, no lag
4. **Check**: All colors transition properly

### 9. Browser Test

Test in multiple browsers:
- [ ] Chrome/Edge (Chromium)
- [ ] Firefox
- [ ] Safari (if available)

**Expected**: Same visual quality in all browsers

### 10. Mobile Test (Optional)

1. Open on mobile device or use DevTools responsive mode
2. **Expected**: Graph scales to fit screen
3. **Check**: Controls are accessible

---

## Quick Reference: What to Look For

| Feature | What You Should See |
|---------|---------------------|
| **Starting state** | Blue border on start node, all other nodes dark |
| **During visit** | Current node **bright yellow with glow** |
| **After visit** | Node turns **solid green** |
| **Edge traversal** | Edge flashes **yellow** when moving to neighbor |
| **Completed path** | Edges turn **green** after traversal |
| **Result array** | Green boxes with numbers, text format below |
| **Queue/Stack** | Pink dashed borders on waiting nodes |

## Success Criteria

✅ **PASS** if:
- Clear yellow→green node transitions
- Edges visible throughout animation
- Result array updates in real-time
- No bar charts on graph algorithm pages
- All edges visible in randomized graphs

❌ **FAIL** if:
- Node colors unclear or flickering
- Edges disappear during animation
- Result array missing or not updating
- Bar chart interface appears
- Isolated nodes with no visible edges

---

**Last Updated**: After applying all visualization fixes
**Status**: Ready for testing
