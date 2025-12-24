# How to Use BFS, DFS, and Dijkstra - Quick Guide

## 🎯 BFS (Breadth-First Search)

**Location:** Visualize > BFS

### Steps to Use:
1. **Select Start Node** - Choose where BFS begins (e.g., Node 0)
2. **Select Target Node** - Choose destination to find shortest path
3. **Click "Start"** - Watch BFS explore level by level
4. **View Path** - Shortest path displayed at top after completion

### Expected Behavior:
- ✅ Queue shows FIFO (First In, First Out) order
- ✅ Nodes visited level by level
- ✅ Shortest path guaranteed
- ✅ Distance shows levels from start node

### Changing Nodes:
- **Change Start Node** → Algorithm automatically resets and regenerates steps ✅
- **Change Target Node** → Path updates instantly (no need to re-run) ✅

---

## 🎯 DFS (Depth-First Search)

**Location:** Visualize > DFS

### Steps to Use:
1. **Select Start Node** - Choose where DFS begins
2. **Select Target Node** (optional) - See path to specific node
3. **Click "Start"** - Watch DFS explore depth-first
4. **View Path** - DFS path displayed after completion

### Expected Behavior:
- ✅ Stack shows LIFO (Last In, First Out) order
- ✅ Goes as deep as possible before backtracking
- ✅ Depth tracking shows how deep each node is
- ✅ Backtracking visualization

### Changing Nodes:
- **Change Start Node** → Algorithm automatically resets and regenerates steps ✅
- **Change Target Node** → Path updates instantly ✅

---

## 🎯 Dijkstra's Algorithm (DAA)

**Location:** DAA > Dijkstra

### Steps to Use:
1. **Select Source Node** - Choose starting point
2. **Click "Start"** - Watch Dijkstra find shortest paths
3. **View Results** - All shortest distances shown in right panel

### Expected Behavior:
- ✅ Priority queue picks minimum distance node
- ✅ Edge relaxation updates distances
- ✅ Shortest paths to ALL nodes calculated
- ✅ Works with weighted edges

### Changing Source:
- **Change Source Node** → Algorithm automatically resets and regenerates steps ✅
- Works with ANY node (0, 1, 2, etc.) ✅

---

## 🔧 Troubleshooting

### Issue: "Algorithm doesn't respond to start node changes"

**Solutions:**
1. **Hard refresh browser:** Ctrl+Shift+R (Windows) or Cmd+Shift+R (Mac)
2. **Clear cache and reload**
3. **Click "Apply" button** if available
4. **Restart Angular dev server:**
   ```bash
   # Stop current server (Ctrl+C)
   # Then restart:
   ng serve
   ```

### Issue: "No path shown between nodes"

**Cause:** Nodes are disconnected (no path exists)

**Solutions:**
1. **Click "Randomize"** to generate a new connected graph
2. **Try different start/target nodes** that are connected
3. **Check if graph visualization shows edges** between nodes

### Issue: "Steps not updating after changing start node"

**Solutions:**
1. **Wait a moment** - Large graphs take time to generate steps
2. **Click "Reset" button** manually
3. **Refresh the page** and try again

### Issue: "Path shows incorrectly"

**Cause:** Might be using cached old steps

**Solutions:**
1. **Click "Reset"** before changing nodes
2. **Hard refresh browser** (Ctrl+Shift+R)
3. **Change start node** (this triggers automatic reset)

---

## ✅ Expected Results

### BFS:
```
Start Node: 0, Target Node: 5
✅ Should show: 0 → 1 → 5 (shortest path)
✅ Distance: 2 (2 hops)
✅ All nodes at level 1, then level 2, then level 3...
```

### DFS:
```
Start Node: 0, Target Node: 5
✅ Should show: 0 → 2 → 4 → 5 (one possible DFS path)
✅ Depth values shown for each node
✅ Explores one branch completely before backtracking
```

### Dijkstra:
```
Source Node: 0
✅ Shows distances to ALL nodes
✅ Example: {0: 0, 1: 4, 2: 12, 3: 19, ...}
✅ Previous node (prev) shown for path reconstruction
✅ Works even if some nodes unreachable (shows ∞)
```

---

## 🎮 Interactive Controls

### All Algorithms Have:
- **Start/Play** ▶️ - Begin visualization
- **Pause** ⏸️ - Pause at current step
- **Reset** 🔄 - Go back to beginning
- **Next Step** ⏭️ - Manual step-through
- **Randomize** 🎲 - Generate new graph

### Node Selectors:
- **Start Node / Source** - Where algorithm begins
- **Target Node** (BFS/DFS) - Destination to find path to

---

## 📝 Notes

### BFS vs DFS:
- **BFS** finds **SHORTEST PATH** (minimum hops)
- **DFS** finds **A PATH** (not necessarily shortest)
- Use BFS when you need the shortest unweighted path
- Use DFS for exploring all possibilities or specific patterns

### Dijkstra:
- Finds **SHORTEST WEIGHTED PATH** to all nodes
- More powerful than BFS (handles edge weights)
- Uses greedy approach with relaxation
- Guaranteed optimal for non-negative weights

---

## 🚀 All Systems Working!

**Status:** ✅ **ALL ALGORITHMS WORKING CORRECTLY**

- ✅ BFS logic correct with shortest path guarantee
- ✅ DFS logic correct with depth-first exploration
- ✅ Dijkstra logic correct with weighted shortest paths
- ✅ Start node changes work automatically
- ✅ Target node changes update paths instantly
- ✅ No compilation errors

**Enjoy exploring these classic graph algorithms!** 🎉
