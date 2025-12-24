# DAA Dijkstra Troubleshooting Guide

## Date: October 18, 2025

## Latest Changes Applied

### 1. Added ChangeDetectorRef ✅
**Problem**: Angular might not detect changes in nested setTimeout callbacks
**Solution**: Injected `ChangeDetectorRef` and call `detectChanges()` after state updates

```typescript
constructor(private cdr: ChangeDetectorRef) {}

// In applyStep():
this.activeNode = s.node;
this.cdr.detectChanges(); // Force UI update
```

### 2. Fixed Animation Delay Initialization ✅
**Problem**: `animationDelay` might not be set correctly on init
**Solution**: Set it explicitly in `ngOnInit()`

```typescript
ngOnInit(): void {
  this.generateGraph(6);
  this.reset();
  this.animationDelay = 2200 - (this.speed * 200);
}
```

### 3. Enhanced Start Method ✅
**Problem**: If completed, need to reset before replaying
**Solution**: Call `reset()` when currentStep >= steps.length

```typescript
if (this.currentStep >= this.steps.length) {
  this.currentStep = 0;
  this.reset(); // Rebuild steps
}
```

## How to Debug

### Step 1: Open Browser Console (F12)

### Step 2: Refresh Page
You should see:
```
🚀 ngOnInit called
Initial speed: 6 delay: 1000
✅ buildSteps complete! Total steps: [number]
```

**If you DON'T see this:**
- The component isn't loading
- Check the route is correct: `localhost:4201/daa/dijkstra`
- Check browser console for errors

### Step 3: Click Play Button
You should see:
```
🎬 Start called! Steps: [number] Current: 0
isPlaying before: false isPaused: false
isPlaying after: true
Starting animation...
🏃 Running animation step: 0 / [number]
📝 Applying step: visit 🎬 STARTING DIJKSTRA...
🎬 Initial visit node: 0
⏰ Setting timer for next step, delay: 1000
```

**If you DON'T see "Start called":**
- Play button isn't connected
- Check template: `(play)="start()"`
- Check skeleton component is working

**If you see "Start called" but nothing else:**
- Steps array is empty (check buildSteps)
- Animation isn't running (check runAnimation)

### Step 4: Watch for Animation Logs
Every ~1 second you should see:
```
🏃 Running animation step: [n] / [total]
📝 Applying step: [type] [description...]
```

**If logs stop:**
- Check for JavaScript errors
- Check if `isPaused` is true
- Check if `animationTimer` is being cleared

### Step 5: Check Visual Updates
While animation runs:
- Nodes should change color
- Edges should change color
- Distance labels should update

**If NO visual changes:**
- CSS classes aren't being applied
- Check inspector: element should have classes like `node-active`, `edge-active`
- Check CSS is loaded (Styles panel)

**If classes are applied but NO animations:**
- CSS animations aren't working
- Check for `!important` flags in CSS
- Check browser supports `filter: drop-shadow()`

## Common Issues & Solutions

### Issue 1: "Start called" but No Animation

**Symptoms:**
```
🎬 Start called! Steps: 50 Current: 0
Starting animation...
[nothing else]
```

**Causes:**
1. `runAnimation()` not called
2. `setTimeout` failing
3. `animationDelay` is undefined/NaN

**Solutions:**
```typescript
// Check animationDelay value
console.log('Animation delay:', this.animationDelay, typeof this.animationDelay);

// In runAnimation, add more logs
console.log('Setting timeout with delay:', this.animationDelay);
console.log('isPaused:', this.isPaused, 'isPlaying:', this.isPlaying);
```

### Issue 2: Animation Runs But No Visual Changes

**Symptoms:**
- Console logs show steps
- Nothing changes on screen

**Causes:**
1. Change detection not triggered
2. CSS classes not applied
3. CSS not loaded

**Solutions:**
```typescript
// Add after setting activeNode
console.log('activeNode set to:', this.activeNode);
console.log('visited array:', this.visited);

// Check in browser inspector
// Element should show: <circle class="node-active node-source">
```

### Issue 3: Classes Applied But No Animations

**Symptoms:**
- Element has `node-active` class
- No visual glow/pulse

**Causes:**
1. CSS not specific enough
2. Missing `!important` flags
3. Browser doesn't support CSS

**Solutions:**
1. Check CSS selector:
```css
/* Wrong */
.node-active { ... }

/* Right */
circle.node-active { ... !important; }
```

2. Check browser console for CSS warnings

3. Test browser support:
```javascript
// In console
const div = document.createElement('div');
div.style.filter = 'drop-shadow(0 0 10px red)';
console.log(div.style.filter); // Should show the value
```

### Issue 4: Animation Starts Then Stops

**Symptoms:**
- First few steps work
- Suddenly stops

**Causes:**
1. JavaScript error in step
2. Timer cleared unexpectedly
3. isPaused set to true

**Solutions:**
```typescript
// Check for errors in console
// Add try-catch in applyStep
try {
  // ... step logic
} catch (error) {
  console.error('Error in applyStep:', error);
}
```

## Verification Checklist

Run through this checklist step by step:

### Initialization
- [ ] `ngOnInit` log appears
- [ ] `buildSteps` log shows step count > 0
- [ ] `animationDelay` is set (check log)
- [ ] Source node is visible and colored

### Play Button Click
- [ ] "Start called" log appears
- [ ] `isPlaying` changes to true
- [ ] "Starting animation" log appears
- [ ] "Running animation step" log appears

### Animation Loop
- [ ] Logs appear every ~1 second
- [ ] Step counter increments (0, 1, 2, ...)
- [ ] Different step types shown (visit, extract, edge-relax-check, etc.)
- [ ] Timer log shows delay value

### Visual Updates
- [ ] Source node has blue color
- [ ] Active node has golden color
- [ ] Visited nodes turn green
- [ ] Edges change color (golden/green)
- [ ] Distance labels update in table
- [ ] TV caption text changes

### CSS Application
- [ ] Inspect source node: has `node-source` class
- [ ] Inspect active node: has `node-active` class
- [ ] Inspect active edge: has `edge-active` class
- [ ] CSS rules are NOT crossed out in Styles panel
- [ ] Animations panel shows running animations

### Completion
- [ ] Animation reaches end (step === total)
- [ ] "Animation complete" log appears
- [ ] `isPlaying` changes to false
- [ ] Final state is stable (no flickering)

## If Still Not Working

### Nuclear Option 1: Hard Reset Browser
1. Open DevTools (F12)
2. Right-click refresh button
3. Select "Empty Cache and Hard Reload"
4. Wait for page to reload
5. Try again

### Nuclear Option 2: Check Angular Zones
```typescript
// In constructor, add:
import { NgZone } from '@angular/core';
constructor(private cdr: ChangeDetectorRef, private ngZone: NgZone) {}

// In runAnimation, wrap timeout:
this.ngZone.run(() => {
  this.animationTimer = setTimeout(() => {
    if (!this.isPaused && this.isPlaying) {
      this.runAnimation();
    }
  }, this.animationDelay);
});
```

### Nuclear Option 3: Simplify Animation
Test with minimal animation:
```typescript
start() {
  console.log('START');
  let count = 0;
  const interval = setInterval(() => {
    console.log('Step:', count++);
    if (count >= 10) {
      clearInterval(interval);
      console.log('DONE');
    }
  }, 1000);
}
```

If this works, problem is in animation logic.
If this doesn't work, problem is deeper (Angular/browser issue).

## Expected Console Output (Full Run)

```
🚀 ngOnInit called
Initial speed: 6 delay: 1000
✅ buildSteps complete! Total steps: 50

[User clicks Play]

🎬 Start called! Steps: 50 Current: 0
isPlaying before: false isPaused: false
isPlaying after: true
Starting animation...
🏃 Running animation step: 0 / 50
📝 Applying step: visit 🎬 STARTING DIJKSTRA: Node A is our source...
🎬 Initial visit node: 0
⏰ Setting timer for next step, delay: 1000
🏃 Running animation step: 1 / 50
📝 Applying step: extract 📤 EXTRACT MIN: Selected Node A...
🔵 Extract node: 0
⏰ Setting timer for next step, delay: 1000
🏃 Running animation step: 2 / 50
📝 Applying step: edge-relax-check 🔍 CHECKING: Edge A → B...
🔍 Checking edge: 0 -> 1
⏰ Setting timer for next step, delay: 1000
🏃 Running animation step: 3 / 50
📝 Applying step: edge-relax ✨ RELAXATION: YES! Found shorter...
✨ Relax edge: 0 -> 1 new dist: 5
⏰ Setting timer for next step, delay: 1000
[... continues for all steps ...]
🏃 Running animation step: 49 / 50
📝 Applying step: complete 🎉 DIJKSTRA COMPLETE!...
⏰ Setting timer for next step, delay: 1000
🏃 Running animation step: 50 / 50
✅ Animation complete!
```

## Browser Compatibility Check

Test in different browsers:
- Chrome/Edge: Should work perfectly
- Firefox: Should work perfectly
- Safari: May have issues with `drop-shadow`

## Files Modified
- `e:\Algorithm visualizer\algorithm-visualizer\frontend\src\app\pages\daa\dijkstra.component.ts`

## Changes Made
1. Added `ChangeDetectorRef` injection
2. Call `detectChanges()` after state updates
3. Initialize `animationDelay` in `ngOnInit`
4. Enhanced logging in `start()` method
5. Call `reset()` if animation already completed
