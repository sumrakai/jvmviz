Содержимое файла `README.md`:

```markdown
# JVM Visualization Tool

A console utility for real-time monitoring of JVM threads, memory, and garbage collector with beautiful ASCII visualization.

## Features

- 📊 **Real-time JVM Monitoring** - Live statistics updated every 2 seconds
- 🧵 **Thread Statistics** - Current, daemon, peak, and total started threads
- 💾 **Memory Tracking** - Heap and non-heap memory usage with visual progress bars
- 🗑️ **Garbage Collector Metrics** - Collection counts, time, and average time per GC
- 🎨 **ASCII Visualization** - Beautiful console output with Unicode characters and progress bars
- ⚡ **High Performance** - Uses Java 25 virtual threads for efficient monitoring

## Screenshot

```
╔════════════════════════════════════════════════════════════╗
║              JVM Visualization Tool v1.0.0                 ║
╚════════════════════════════════════════════════════════════╝

┌─ Threads ─────────────────────────────────────────────────
│ Current Threads:     15                                   
│ Daemon Threads:      10                                   
│ Peak Threads:        20                                   
│ Total Started:       25                                   
└───────────────────────────────────────────────────────────

┌─ Heap Memory ─────────────────────────────────────────────
│ Used:    45 MB         │ Max:     256 MB        │
│
│ [█████████░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░] 17.6%
│
│ Committed: 128 MB                                         
└───────────────────────────────────────────────────────────

┌─ Garbage Collectors ──────────────────────────────────────
│ Total Collections:   15                                   
│ Total GC Time:       125.50 ms                            
│                                                           
│ G1 Young Generation:                                      
│   Collections:      10                                    
│   Time:             85.20 ms                              
│   Avg Time/GC:      8.52 ms                               
└───────────────────────────────────────────────────────────
```

## Requirements

- **JDK 25** (recommended for better performance and memory consumption)
- **Gradle 9.2.*

## Quick Start

### Using Gradle Wrapper (recommended)

```bash
./gradlew run
```

### Run with Load Generator (for testing)

```bash
./gradlew run --args="--load"
```

### Using installed Gradle

```bash
gradle run
```

### Build JAR

```bash
./gradlew build
java -jar build/libs/jvmviz-1.0.0.jar
```

## Project Structure

```
io.github.sumrakai.jvmviz
├── config/          # Spring configurations
│   └── AppConfig.java
├── collector/        # Statistics collection
│   ├── Collector.java          # Base interface
│   ├── ThreadCollector.java    # Thread statistics
│   ├── MemoryCollector.java     # Memory statistics
│   ├── GcCollector.java        # GC statistics
│   ├── ThreadInfo.java          # Thread data DTO
│   ├── MemoryInfo.java          # Memory data DTO
│   └── GcInfo.java              # GC data DTO
├── visualizer/       # Console output
│   ├── Visualizer.java          # Base interface
│   └── ConsoleVisualizer.java   # ASCII visualization
├── service/          # Business logic
│   └── MonitorService.java     # Main monitoring service
└── App.java          # Entry point
```

## Architecture

The project follows a clean architecture with clear separation of concerns:

- **Collectors** - Gather JVM statistics using JMX (Java Management Extensions)
- **Visualizers** - Format and display data in the console
- **Services** - Coordinate collectors and visualizers
- **Configuration** - Spring-based dependency injection

### Key Technologies

- **Spring Framework 6.2.7** - Dependency injection and configuration
- **JMX (Java Management Extensions)** - Access to JVM metrics
- **Java 25 Virtual Threads** - Efficient concurrent monitoring
- **SLF4J + Logback** - Logging framework

## Usage

1. **Start the application**:
   ```bash
   ./gradlew run
   ```

2. **Run with load generator** (for testing and demonstration):
   ```bash
   ./gradlew run --args="--load"
   ```
   This will start a load generator that creates threads, allocates memory, and triggers garbage collection to demonstrate the monitoring capabilities.

3. **Monitor your JVM**:
    - The console will update every 2 seconds
    - Press `Ctrl+C` to stop monitoring

4. **View statistics**:
    - Thread information (current, daemon, peak)
    - Memory usage (heap and non-heap)
    - Garbage collector metrics

## Development

### Running Tests

```bash
./gradlew test
```

### Building

```bash
./gradlew build
```

### Project Structure Explained

- **Collectors** implement the `Collector` interface and gather data from JMX beans
- **Visualizers** implement the `Visualizer` interface and format output
- **MonitorService** coordinates the collection and visualization cycle
- **Spring Configuration** wires all components together

## Current Status

✅ **Step 1**: Minimal starter with MonitorService  
✅ **Step 2**: Thread and Memory collectors  
✅ **Step 3**: Console visualizer with ASCII graphics  
✅ **Step 4**: Garbage Collector metrics  
✅ **Step 5**: Unit tests with JUnit 5  
✅ **Step 6**: Complete documentation

## Roadmap

Future enhancements may include:

- [ ] Historical data tracking (trends over time)
- [ ] Configurable update intervals
- [ ] Export to file (JSON, CSV)
- [ ] Memory pool details
- [ ] Thread dump analysis
- [ ] Customizable visualization themes
- [ ] Web interface option

## Contributing

Contributions are welcome! Please feel free to submit a Pull Request.

1. Fork the repository
2. Create your feature branch (`git checkout -b feature/AmazingFeature`)
3. Commit your changes (`git commit -m 'Add some AmazingFeature'`)
4. Push to the branch (`git push origin feature/AmazingFeature`)
5. Open a Pull Request

## License

This project is licensed under the MIT License - see the [LICENSE](LICENSE) file for details.

## Acknowledgments

- Built with Java 25 and Spring Framework 6
- Uses JMX for JVM monitoring
- Inspired by tools like `jstat`, `jmap`, and `jconsole`

## Author

**Alexandr Ishchenko**

- GitHub: [@sumrakai](https://github.com/sumrakai)

---

⭐ If you find this project useful, please consider giving it a star!
```