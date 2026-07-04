# Super Architect

Super Architect is a technical Minecraft Fabric mod focused on advanced block-based storage systems, networking, and dynamic UI management. It offers a robust, multi-block storage network that seamlessly links physical storage drives to a central terminal monitor for an optimized player experience.

## Features

### 💽 Disk Drives
- **Tiered Storage Capacities**: Includes distinct Disk Items of varying sizes (128, 256, 512, 1k, 2k, 4k, 8k) to fit storage needs of any scale.
- **Data Component Integration**: Fully utilizes modern Minecraft 1.21 Data Components to safely encode, store, and transfer items within Disks without the risk of NBT overflow.
- **Dynamic Tooltips**: Hover over a disk to instantly see its maximum capacity and current item usage right in your inventory!

### 🗄️ Disk Racks
- **Multi-Slot Expansion**: The physical heart of the network. Each Disk Rack Block holds up to 4 Disk Drives.
- **Connected Textures & Models**: Disk racks smartly detect neighboring racks (Up, Down, Left, Right). Through dynamic block states and Datagen multipart models, connected faces visually seamlessly merge, removing ugly chunk borders.
- **Live Visuals**: Custom Block Entity rendering supports live Bay States.

### 🖥️ Monitor Terminal
The Monitor acts as the brain and interface for your storage facility, featuring a stunning custom UI and powerful tools:
- **Contiguous Networking**: Placing a Monitor connects it to all contiguous Disk Racks via an automated Breadth-First-Search algorithm (up to 128 blocks), unifying all your items into one central access point.
- **Real-Time Free Space Metrics**: Instantly view the available and maximum space of your entire facility right on the UI. Network stats are smartly formatted (e.g., 1k, 5M).
- **Search Bar Filtering**: Features a live text-input widget. The moment you type a query, a custom server payload flawlessly filters your inventory display in real-time, zero client-side desync guaranteed.
- **Smart User Sorting**: Includes an interactive Sort Button (Qty ▼, Qty ▲, A-Z). Built with intelligent UUID-persistence, the Monitor remembers your preferred sorting mode every time you open it!
- **Fluid I/O**: Support for quick Shift-Clicking from player inventory natively cascades items into the first available Drive with space, and clicking items in the grid smartly extracts them back.
- **Custom VoxelShapes**: Realistic flat collision hitboxes perfectly map to the direction the Monitor is facing.

## Development & Setup
This project runs on **Minecraft 1.21 (Fabric)**.
Build the project using standard Gradle commands:
```bash
# Generate translation and model files
./gradlew runDatagen

# Compile the mod
./gradlew build
```

## Credits
Created by HxN Games.
