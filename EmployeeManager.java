import java.io.*;

class User {
    public String username, password;

    public User(String u, String p) {
        username = u;
        password = p;
    }

    public boolean authenticate(String u, String p) {
        return username.equals(u) && password.equals(p);
    }
}

class Employee {
    public int id;
    public String name;
    public float salary;

    public Employee(int id, String name, float salary) {
        this.id = id;
        this.name = name;
        this.salary = salary;
    }

    public void display() {
        System.out.println("\n-----------------------------");
        System.out.println("ID     : " + id);
        System.out.println("Name   : " + name);
        System.out.println("Salary : " + salary);
        System.out.println("-----------------------------");
    }
}

class Node {
    public Employee emp;
    public Node next;

    public Node(Employee e) {
        emp = e;
        next = null;
    }
}

class Company {
    private Node head;

    public Company() {
        head = null;
    }

    public void addEmployee(Employee e) {
        Node newNode = new Node(e);
        newNode.next = head;
        head = newNode;
    }

    public void showAllEmployees() {
        if (head == null) {
            System.out.println("No employees to display.");
            return;
        }

        Node current = head;
        while (current != null) {
            current.emp.display();
            current = current.next;
        }
    }

    public void searchById(int id) {
        Node current = head;
        while (current != null) {
            if (current.emp.id == id) {
                System.out.println("Employee found:");
                current.emp.display();
                return;
            }
            current = current.next;
        }
        System.out.println("Employee not found.");
    }

    public void deleteById(int id) {
        if (head == null) {
            System.out.println("No employees to delete.");
            return;
        }

        if (head.emp.id == id) {
            head.emp.display();
            head = head.next;
            System.out.println("Employee deleted.");
            return;
        }

        Node current = head;
        while (current.next != null) {
            if (current.next.emp.id == id) {
                current.next.emp.display();
                current.next = current.next.next;
                System.out.println("Employee deleted.");
                return;
            }
            current = current.next;
        }

        System.out.println("Employee not found.");
    }

    public void editEmployee(int id, BufferedReader br) throws IOException {
        Node current = head;
        while (current != null) {
            if (current.emp.id == id) {
                System.out.print("Change name? (Y/N): ");
                if (br.readLine().trim().equalsIgnoreCase("Y")) {
                    System.out.print("Enter new name: ");
                    current.emp.name = br.readLine();
                }

                System.out.print("Change salary? (Y/N): ");
                if (br.readLine().trim().equalsIgnoreCase("Y")) {
                    System.out.print("Enter new salary: ");
                    current.emp.salary = Float.parseFloat(br.readLine());
                }

                System.out.println("Employee updated.");
                return;
            }
            current = current.next;
        }

        System.out.println("Employee not found.");
    }

    public void saveToCSV(String filename) {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(filename))) {
            Node current = head;
            while (current != null) {
                Employee e = current.emp;
                bw.write(e.id + "," + e.name + "," + e.salary);
                bw.newLine();
                current = current.next;
            }
            System.out.println("Data saved to " + filename);
        } catch (IOException e) {
            System.out.println("Error saving file: " + e.getMessage());
        }
    }

    public void loadFromCSV(String filename) {
        try (BufferedReader br = new BufferedReader(new FileReader(filename))) {
            String line;
            System.out.println("\n***** Employees from File *****");
            while ((line = br.readLine()) != null) {
                String[] data = line.split(",");
                if (data.length == 3) {
                    System.out.println("ID: " + data[0] + ", Name: " + data[1] + ", Salary: " + data[2]);
                }
            }
        } catch (IOException e) {
            System.out.println("Error reading file: " + e.getMessage());
        }
    }
}

public class EmployeeManager {
    public static void main(String[] args) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        Company company = new Company();
        User admin = new User("admin", "admin123");
        int choice;

        // Login
        System.out.println("****** Login ******");
        boolean loggedIn = false;
        for (int i = 0; i < 3; i++) {
            System.out.print("Username: ");
            String user = br.readLine();
            System.out.print("Password: ");
            String pass = br.readLine();

            if (admin.authenticate(user, pass)) {
                System.out.println("Login successful!");
                loggedIn = true;
                break;
            } else {
                System.out.println("Wrong credentials.");
            }
        }

        if (!loggedIn) {
            System.out.println("Too many attempts. Exiting.");
            return;
        }

        // Menu
        do {
            System.out.println("\n===== Employee Management Menu =====");
            System.out.println("1. Add Employees");
            System.out.println("2. Show All Employees");
            System.out.println("3. Search by ID");
            System.out.println("4. Delete by ID");
            System.out.println("5. Edit Employee");
            System.out.println("6. Save to CSV");
            System.out.println("7. Load from CSV");
            System.out.println("8. Exit");
            System.out.print("Choice: ");

            try {
                choice = Integer.parseInt(br.readLine());

                switch (choice) {
                    case 1:
                        System.out.print("How many employees to add? ");
                        int n = Integer.parseInt(br.readLine());
                        for (int i = 0; i < n; i++) {
                            System.out.print("Enter ID: ");
                            int id = Integer.parseInt(br.readLine());
                            System.out.print("Enter Name: ");
                            String name = br.readLine();
                            System.out.print("Enter Salary: ");
                            float sal = Float.parseFloat(br.readLine());
                            company.addEmployee(new Employee(id, name, sal));
                        }
                        break;
                    case 2:
                        company.showAllEmployees();
                        break;
                    case 3:
                        System.out.print("Enter ID to search: ");
                        int searchId = Integer.parseInt(br.readLine());
                        company.searchById(searchId);
                        break;
                    case 4:
                        System.out.print("Enter ID to delete: ");
                        int delId = Integer.parseInt(br.readLine());
                        company.deleteById(delId);
                        break;
                    case 5:
                        System.out.print("Enter ID to edit: ");
                        int editId = Integer.parseInt(br.readLine());
                        company.editEmployee(editId, br);
                        break;
                    case 6:
                        System.out.print("Enter CSV filename to save: ");
                        String saveFile = br.readLine();
                        company.saveToCSV(saveFile);
                        break;
                    case 7:
                        System.out.print("Enter CSV filename to load: ");
                        String loadFile = br.readLine();
                        company.loadFromCSV(loadFile);
                        break;
                    case 8:
                        System.out.println("Exiting program. Goodbye!");
                        break;
                    default:
                        System.out.println("Invalid choice!");
                }

            } catch (NumberFormatException e) {
                System.out.println("Please enter a valid number.");
                choice = 0; // reset to avoid exit
            }

        } while (choice != 8);
    }
}
