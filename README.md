# Donkey Kong 

A remake of the classic **Donkey Kong** arcade game built with **Java + Bagel**.  
Players control Mario to dodge barrels, climb ladders, collect hammers, and reach the princess.

---

## 🎮 Gameplay

- **Controls**
   - `← / →`: Move left and right
   - `↑ / ↓`: Climb ladders
   - `Space`: Jump
   - Pick up a **Hammer** to enter invincible mode and smash barrels for bonus points.

- **Objective**
   - Avoid barrels and reach the top platform safely.
   - Use jumping and hammers strategically to score points.
   - Experience the nostalgic fun of the original arcade classic.

---

## 🛠️ Project Structure

- **Core Classes**
   - `Mario`: Player character logic (movement, jumping, climbing, hammer collection, scoring)
   - `Barrel`: Barrel generation and rolling behavior
   - `Donkey`: Enemy character responsible for throwing barrels
   - `Ladder`: Supports climbing mechanics
   - `Score`: Records and displays points
   - Other helper classes (platforms, object management, etc.)

- **Features**
   - Auto-generates platforms, ladders, and objects from config files
   - Uses **inheritance and polymorphism** for cleaner design
   - Implements collision detection, gravity, and landing logic

---

## 📦 Tech Stack

- **Language**: Java
- **Framework**: Bagel (Game Development Framework)
- **Tools**: Git, VS Code / IntelliJ

---

## 🚀 Getting Started

1. Clone the repo:
   ```bash
   git clone git@github.com:seekz39/Donkey-Kong.git
   cd Donkey-Kong
