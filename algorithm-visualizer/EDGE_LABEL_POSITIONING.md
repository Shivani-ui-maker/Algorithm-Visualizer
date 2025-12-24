# Edge Label Positioning Strategy

## Visual Layout

```
Node A ────────────────────> Node B
      ↑                  ↑
      |                  |
      35%               100%
      
      Label here (not at 50% midpoint!)
      ↓
     [7]  ← Offset perpendicular 24px
```

## Why 35% Works Better Than 50%

### Old Approach (Midpoint - 50%)
```
A ──────────[3]──────────> B
            ↓
C ──────────[5]──────────> D
            ↓
        All labels stack here!
        (Congestion at center)
```

### New Approach (35% + Perpendicular)
```
A ────[3]──────────────────> B
      ↑ 24px offset
      
C ──────────[5]────────────> D
            ↑ 24px offset
            
Labels spread out naturally!
```

## Perpendicular Offset Calculation

For an edge from (x1,y1) to (x2,y2):

1. **Calculate angle**: `θ = atan2(y2-y1, x2-x1)`
2. **Find position at 35%**: `P = (x1 + 0.35*Δx, y1 + 0.35*Δy)`
3. **Offset perpendicular**: 
   - `Px = P.x + (-sin(θ) × 24)`
   - `Py = P.y + (cos(θ) × 24)`

This rotates the offset vector 90° CCW (counter-clockwise), pushing labels to the "upper-left" side of each edge.

## Visual Features

### Background Rectangle
- **Size**: 24×16px (generous padding)
- **Fill**: `rgba(8,20,37,0.92)` - matches graph background
- **Stroke**: `rgba(251,191,36,0.3)` - subtle yellow border
- **Radius**: 4px for smooth corners

### Text Styling
- **Font**: 13px, weight 700 (bold)
- **Color**: Pure white `#ffffff`
- **Shadow**: `drop-shadow(0px 1px 2px rgba(0,0,0,0.8))`
- **Alignment**: Center-anchored on calculated position

## Result
✅ No overlap even with 10+ edges  
✅ Clear which weight belongs to which edge  
✅ Readable at any zoom level  
✅ Works consistently across randomize/resize  

## Edge Cases Handled
- **Parallel edges**: 35% position naturally separates them
- **Short edges**: 24px offset keeps label away from nodes
- **Long edges**: Label stays near source node for clarity
- **Crossing edges**: Perpendicular offset prevents overlap

## Testing Checklist
- [x] Randomize 5 times - labels remain clear
- [x] Test sizes 5-10 nodes - no overlaps
- [x] Bidirectional edges (A→B, B→A) - separated by offset
- [x] Dense subgraphs - labels spread by perpendicular calculation
