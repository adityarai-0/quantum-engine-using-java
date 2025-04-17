// Main.java
import java.util.*;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        System.out.println("Welcome to the Quantum Compute Engine!\nThis simulator supports basic quantum gates and simulates how a quantum circuit evolves.");

        int numQubits = -1;
        while (numQubits <= 0 || numQubits > 5) {
            System.out.print("\nEnter number of qubits (1 to 5 recommended): ");
            try {
                numQubits = Integer.parseInt(scanner.nextLine());
                if (numQubits <= 0 || numQubits > 5) {
                    System.out.println("Please enter a valid number between 1 and 5.");
                }
            } catch (NumberFormatException e) {
                System.out.println("Invalid input. Please enter a numeric value.");
            }
        }

        Circuit circuit = new Circuit(numQubits);

        boolean running = true;
        while (running) {
            System.out.println("\nQuantum Gate Menu:");
            System.out.println("1. Hadamard Gate (Creates superposition)");
            System.out.println("2. Pauli-X Gate (Flips the qubit like NOT)");
            System.out.println("3. Pauli-Y Gate (Flips and adds phase)");
            System.out.println("4. Pauli-Z Gate (Adds phase to |1⟩)");
            System.out.println("5. Phase Gate (Rotates phase of |1⟩)");
            System.out.println("6. Identity Gate (Does nothing)");
            System.out.println("7. CNOT Gate (Controlled-NOT between 2 qubits)");
            System.out.println("8. Run Circuit and Measure");
            System.out.println("9. Exit");

            System.out.print("\nChoose an option (1-9): ");
            String input = scanner.nextLine();

            switch (input) {
                case "1" -> circuit.addGate(new Hadamard(promptQubit(scanner, numQubits)));
                case "2" -> circuit.addGate(new PauliX(promptQubit(scanner, numQubits)));
                case "3" -> circuit.addGate(new PauliY(promptQubit(scanner, numQubits)));
                case "4" -> circuit.addGate(new PauliZ(promptQubit(scanner, numQubits)));
                case "5" -> circuit.addGate(new PhaseGate(promptQubit(scanner, numQubits)));
                case "6" -> circuit.addGate(new IdentityGate(promptQubit(scanner, numQubits)));
                case "7" -> {
                    System.out.print("Enter control qubit index (0 to " + (numQubits - 1) + "): ");
                    int control = Integer.parseInt(scanner.nextLine());
                    System.out.print("Enter target qubit index (0 to " + (numQubits - 1) + "): ");
                    int target = Integer.parseInt(scanner.nextLine());
                    circuit.addGate(new ControlledNot(control, target));
                }
                case "8" -> {
                    Complex[] result = circuit.run();
                    System.out.println("\nFinal quantum state amplitudes:");
                    for (int i = 0; i < result.length; i++) {
                        System.out.printf("|%s⟩: %s\n", formatBinary(i, numQubits), result[i]);
                    }

                    System.out.println("\nSimulated measurement:");
                    System.out.println("Measured output state: |" + circuit.measure(result) + "⟩\n");
                }
                case "9" -> running = false;
                default -> System.out.println("Invalid choice. Please enter a number between 1 and 9.");
            }
        }
    }

    private static int promptQubit(Scanner scanner, int maxQubits) {
        while (true) {
            System.out.print("Enter target qubit index (0 to " + (maxQubits - 1) + "): ");
            try {
                int target = Integer.parseInt(scanner.nextLine());
                if (target >= 0 && target < maxQubits) return target;
                else System.out.println("Index out of range.");
            } catch (NumberFormatException e) {
                System.out.println("Invalid input. Please enter an integer.");
            }
        }
    }

    private static String formatBinary(int value, int length) {
        return String.format("%" + length + "s", Integer.toBinaryString(value)).replace(' ', '0');
    }
}


class Complex {
    double re, im;
    public Complex(double re, double im) {
        this.re = re;
        this.im = im;
    }
    public Complex add(Complex o) {
        return new Complex(this.re + o.re, this.im + o.im);
    }
    public Complex mul(Complex o) {
        return new Complex(this.re * o.re - this.im * o.im, this.re * o.im + this.im * o.re);
    }
    public String toString() {
        return String.format("%.2f%+.2fi", re, im);
    }
    public static Complex exp(double theta) {
        return new Complex(Math.cos(theta), Math.sin(theta));
    }
}

interface Gate {
    Complex[] apply(Complex[] state, int numQubits);
}

class Hadamard implements Gate {
    int target;
    public Hadamard(int target) { this.target = target; }
    public Complex[] apply(Complex[] state, int numQubits) {
        int size = state.length;
        Complex[] newState = new Complex[size];
        Arrays.fill(newState, new Complex(0, 0));

        for (int i = 0; i < size; i++) {
            int bit = (i >> target) & 1;
            int flipped = i ^ (1 << target);
            Complex coeff = new Complex(1 / Math.sqrt(2), 0);
            newState[i] = newState[i].add(state[i].mul(coeff));
            newState[flipped] = newState[flipped].add(state[i].mul(coeff));
        }
        return newState;
    }
}

class PauliX implements Gate {
    int target;
    public PauliX(int target) { this.target = target; }
    public Complex[] apply(Complex[] state, int numQubits) {
        Complex[] newState = new Complex[state.length];
        for (int i = 0; i < state.length; i++) newState[i] = new Complex(0, 0);
        for (int i = 0; i < state.length; i++) {
            int flipped = i ^ (1 << target);
            newState[flipped] = state[i];
        }
        return newState;
    }
}

class PauliY implements Gate {
    int target;
    public PauliY(int target) { this.target = target; }
    public Complex[] apply(Complex[] state, int numQubits) {
        Complex[] newState = new Complex[state.length];
        for (int i = 0; i < state.length; i++) newState[i] = new Complex(0, 0);
        for (int i = 0; i < state.length; i++) {
            int flipped = i ^ (1 << target);
            int sign = (((i >> target) & 1) == 0) ? 1 : -1;
            newState[flipped] = state[i].mul(new Complex(0, sign));
        }
        return newState;
    }
}

class PauliZ implements Gate {
    int target;
    public PauliZ(int target) { this.target = target; }
    public Complex[] apply(Complex[] state, int numQubits) {
        Complex[] newState = new Complex[state.length];
        for (int i = 0; i < state.length; i++) {
            int bit = (i >> target) & 1;
            newState[i] = (bit == 1) ? state[i].mul(new Complex(-1, 0)) : state[i];
        }
        return newState;
    }
}

class PhaseGate implements Gate {
    int target;
    public PhaseGate(int target) { this.target = target; }
    public Complex[] apply(Complex[] state, int numQubits) {
        Complex[] newState = new Complex[state.length];
        for (int i = 0; i < state.length; i++) {
            int bit = (i >> target) & 1;
            newState[i] = (bit == 1) ? state[i].mul(Complex.exp(Math.PI / 2)) : state[i];
        }
        return newState;
    }
}

class IdentityGate implements Gate {
    int target;
    public IdentityGate(int target) { this.target = target; }
    public Complex[] apply(Complex[] state, int numQubits) {
        return state.clone();
    }
}

class ControlledNot implements Gate {
    int control, target;
    public ControlledNot(int control, int target) {
        this.control = control;
        this.target = target;
    }
    public Complex[] apply(Complex[] state, int numQubits) {
        Complex[] newState = new Complex[state.length];
        for (int i = 0; i < state.length; i++) newState[i] = new Complex(0, 0);
        for (int i = 0; i < state.length; i++) {
            if (((i >> control) & 1) == 1) {
                int flipped = i ^ (1 << target);
                newState[flipped] = state[i];
            } else {
                newState[i] = state[i];
            }
        }
        return newState;
    }
}

class Circuit {
    int numQubits;
    List<Gate> gates = new ArrayList<>();

    public Circuit(int numQubits) {
        this.numQubits = numQubits;
    }

    public void addGate(Gate gate) {
        gates.add(gate);
    }

    public Complex[] run() {
        int size = 1 << numQubits;
        Complex[] state = new Complex[size];
        Arrays.fill(state, new Complex(0, 0));
        state[0] = new Complex(1, 0);
        for (Gate gate : gates) {
            state = gate.apply(state, numQubits);
        }
        return state;
    }

    public String measure(Complex[] state) {
        double[] probs = new double[state.length];
        double sum = 0;
        for (int i = 0; i < state.length; i++) {
            probs[i] = state[i].re * state[i].re + state[i].im * state[i].im;
            sum += probs[i];
        }
        double rand = Math.random() * sum;
        for (int i = 0; i < probs.length; i++) {
            rand -= probs[i];
            if (rand <= 0) return String.format("%" + numQubits + "s", Integer.toBinaryString(i)).replace(' ', '0');
        }
        return "0";
    }
}
