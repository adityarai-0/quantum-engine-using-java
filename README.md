# Quantum Compute Engine

Welcome to the **Quantum Compute Engine** — a Java-based simulator that demonstrates how quantum circuits evolve through the application of quantum gates.

This project is designed for learning and experimenting with basic quantum concepts like superposition, entanglement, and quantum state measurement.

## ✨ Features

- Support for up to **5 qubits**.
- Interactive command-line interface for applying:
  - Hadamard Gate
  - Pauli-X, Y, Z Gates
  - Phase Gate
  - Identity Gate
  - Controlled-NOT (CNOT) Gate
- Simulates quantum state evolution.
- Displays final quantum state amplitudes.
- Simulates a quantum measurement.

## 🚀 How to Run

1. **Clone the repository:**

   ```bash
   git clone https://github.com/yourusername/quantum-compute-engine.git
   cd quantum-compute-engine
   ```

2. **Compile the code:**

   ```bash
   javac Main.java
   ```

3. **Run the simulator:**

   ```bash
   java Main
   ```

4. Follow the prompts to build your quantum circuit and simulate it!

## 🧠 Example Usage

```
Welcome to the Quantum Compute Engine!
Enter number of qubits (1 to 5 recommended): 2

Quantum Gate Menu:
1. Hadamard Gate
2. Pauli-X Gate
...
Choose an option (1-9): 1
Enter target qubit index (0 to 1): 0

... (apply more gates)

Choose an option (1-9): 8
Final quantum state amplitudes:
|00⟡: 0.707 + 0.0i
|01⟡: 0.0 + 0.0i
|10⟡: 0.707 + 0.0i
|11⟡: 0.0 + 0.0i

Measured output state: |10⟡
```

## 🧱 Project Structure

```
Main.java         - Entry point and CLI logic
Circuit.java      - Represents the quantum circuit
Gate.java         - Abstract class for all gates
Complex.java      - Complex number representation
Hadamard.java     - Implementation of Hadamard gate
...               - Other gate classes (PauliX, CNOT, etc.)
```

> ℹ️ All gate implementations are assumed to be present in separate `.java` files.

## 📚 Prerequisites

- Java 11 or higher
- Basic understanding of quantum mechanics helps!

## 🔧 Future Improvements

- GUI interface for easier circuit building
- Export circuit diagrams or states
- Support more advanced gates (T, SWAP, etc.)
- Quantum entanglement visualizations

## 📝 License

MIT License — feel free to fork and contribute!

---

Made with ☕ + 🧠 by [Your Name]

