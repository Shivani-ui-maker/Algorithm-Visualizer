# TODO List for Algorithm Visualizer Web App

## Phase 1: Authentication and User Flow
- [x] Implement user authentication (login, register, onboarding) with JWT
- [x] Redirect user to home page after successful login/register and guest option with just a name
- [x] Store user session and token securely

## Phase 2: Home Page
- [x] Design home page with two medium interactive cards: DAA and DSA
- [x] Cards should fit in viewport without scrolling
- [x] Display algorithm categories as dropdown buttons grouped by DAA and DSA
- [x] Clicking a category filters algorithms accordingly
- [x] Redesign with clean, modern interface (black-yellow theme)

## Phase 3: Algorithm Visualization Page
- [ ] Create visualization page with interactive "LED TV" style display for animations for every algorithms
- [ ] Add controls: play, pause, next step, previous step, speed control
- [ ] Display step-by-step descriptions alongside visualization
- [ ] Show time complexities and real-world use cases
- [ ] Show multi-language code explanations (Java, Python, C++, )
- [ ] On completing visualization, prompt user with related quiz popup (3-5 MCQs)
- [ ] Provide immediate feedback and store quiz score in DB
- [ ] Mark algorithm as completed after quiz completion

## Phase 4: Exercises Page
- [ ] Create exercises page with mixed questions from all algorithms (MCQs, True/False, Fill-in-the-blank)
- [ ] Support easy, medium, hard levels for questions
- [ ] Provide instant feedback on answers
- [ ] Track progress and update leaderboard accordingly

## Phase 5: Code Editor
- [ ] Integrate Monaco Editor for code editing (Java, Python, C++, )
- [ ] Provide real-world story-type coding problems
- [ ] Run code via backend API (Spring Boot → Judge0 API)
- [ ] Show real-time feedback with line-specific error highlighting
- [ ] Track code attempts and success/failure stats
- [ ] Support Java, Python, C++ languages specifically

## Phase 6: Progress and Leaderboard
- [ ] Implement progress page showing algorithms attempted, quiz scores, code attempts with charts
- [ ] Implement leaderboard with user rankings, scores, streaks, badges
- [ ] Support real-time updates for progress and leaderboard

## Phase 7: FAQs Page
- [ ] Display interview-style questions
- [ ] Allow timed text box answers
- [ ] Provide immediate evaluation based on keywords
- [ ] Store answers and update progress

## Phase 8: Settings and Profile
- [ ] Create settings page with theme switcher (dark: black/yellow(default), light: crimson red/white)
- [ ] Allow user profile viewing and editing
- [ ] Provide contact form

## Phase 9: UI/UX and Theming
- [ ] Apply black/yellow theme for dark mode (default)
- [ ] Apply crimson red/white theme for light mode
- [ ] Ensure color-blind friendly highlights
- [ ] Add smooth animations using Angular animations and GSAP
- [ ] Ensure responsive design and accessibility

## Phase 10: Testing and Deployment
- [ ] Test all features and buttons for correct functionality
- [ ] Fix bugs and optimize performance
- [ ] Prepare deployment scripts and documentation

---

This plan will be implemented step-by-step. Please confirm to start with Phase 1: Authentication and User Flow.
