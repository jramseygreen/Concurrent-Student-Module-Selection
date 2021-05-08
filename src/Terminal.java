import java.util.ArrayList;

/**
 * This is the driver class
 * 
 * @author Joshua Green 956213
 *
 */
public class Terminal {	
	public static ArrayList<Module> allModules = new ArrayList<Module>();
	
	public static void main(String args[]) {
		int moduleLimit = 3; //change number of modules for all students
		Student student1 = new Student(moduleLimit);
		Student student2 = new Student(moduleLimit);
		Student student3 = new Student(moduleLimit);
		
		generateModule("History", "HI-002", "James", 1);
		generateModule("Engineering", "EN-101", "Bertie", 2);
		generateModule("Physics", "PY-113", "Leigh", 3);
		generateModule("Medicine", "MED-005", "Chris", 2);
		generateModule("Journalism", "JR-501", "Stan", 2);
		generateModule("Computer-Science", "CS-210", "Alma", 2);
		
		Thread T1 = new Thread(student1);
		Thread T2 = new Thread(student2);
		Thread T3 = new Thread(student3);
		T1.start();
		T2.start();
		T3.start();
		try {
			T1.join();
			T2.join();
			T3.join();
		} catch (InterruptedException e) {
			System.out.println("Thread interrupted!");
			e.printStackTrace();
		}
	}
	/**
	 * 
	 * @param moduleTitle title of the module
	 * @param moduleCode code of the module
	 * @param moduleCoordinator the lecturer coordinating the module
	 * @param capacity the module limit for student enrollment
	 */
	
	public static void generateModule(String moduleTitle, String moduleCode, String moduleCoordinator, int capacity) {
		allModules.add(new Module(moduleTitle, moduleCode, moduleCoordinator, capacity));
	}
}