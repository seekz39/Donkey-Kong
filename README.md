# DonkeyKong

---

## Assumptions

1. **Terminal Velocity**
    - All gravity‐affected entities (e.g. Mario, barrels, bananas, bullets, monkeys) have a fixed terminal velocity of **10 frame**.
    - Once their downward speed due to gravity reaches 10, it will not increase further.


2. **Platform‐Edge Collision Logic**
    - When a monkey reaches the edge of a platform, we detect the collision using the entity’s **left** or **right** bounding‐box edge—not its horizontal center.
    - As soon as the bounding‐box’s left `x` ≤ 0 (left screen edge) or its right `x` ≥ platform’s right boundary, the monkey immediately reverses direction.


3. **Single Hammer per Game**  
    - There is exactly one hammer collectible available in each play session. 
    - Once Mario picks it up, no other hammers will appear until the next game.

---