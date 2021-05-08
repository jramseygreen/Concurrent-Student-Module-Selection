import java.util.ArrayList;
import java.util.Random;

/**
 * This class simulates a student thread
 * 
 * @author Joshua Green 956213
 *
 */
public class Student implements Runnable {
	private int studentNumber;
	public static int counter = 0;
	private int moduleLimit;
	private ArrayList<Module> enrolledModules = new ArrayList<>();

	/**
	 * Constructor for student - increments a counter to set the student number
	 * @param moduleLimit The module limit for student enrollment 
	 */
	public Student(int moduleLimit) {
		this.moduleLimit = moduleLimit;
		studentNumber = counter + 1;
		counter++;
	}

	/**
	 * Method demonstrating enrolling and transferring of students
	 */
	@Override
	public void run() {
		// set list of modules for students to attempt to enroll in / transfer
		ArrayList<Module> modules = Terminal.allModules;

		// enroll students randomly until they have filled their module limit
		Random r = new Random();
		while (enrolledModules.size() < moduleLimit) {
			int randomModule = r.nextInt(modules.size());
			try {
				modules.get(randomModule).enroll(this);
			} catch (Exception e) {
				exceptionHandler(e, modules.get(randomModule).getModuleTitle());
			}
		}
		
		// then transfer randomly to other modules
		System.out.println("Student " + studentNumber + " starts transferring now...");
		int module1;
		int module2;
		Module newModule = null;
		
		// only runs while at least one module has at least one space
		while (hasPermits(modules)) {
			module1 = r.nextInt(enrolledModules.size());
			module2 = r.nextInt(modules.size());
			try {
				newModule = modules.get(module2);
				enrolledModules.get(module1).transfer(this, newModule);
			} catch (Exception e) {
				exceptionHandler(e, newModule.getModuleTitle());
			}
		}
	}

	/**
	 * Checks the availability of permits in a given list of modules
	 * 
	 * @param modules An ArrayList of modules 
	 * @return true if there is at least one permit in at least one module
	 */
	private boolean hasPermits(ArrayList<Module> modules) {
		for (Module module : modules) {
			if (module.hasPermit()) {
				return true;
			}
		}
		return false;
	}

	
	/**
	 * This method deals with all exceptions and prints the correct message to the screen depending on why there was an error;
	 * Differentiating exceptions thrown by module
	 * @param e Exception to handle
	 * @param moduleTitle Title of the module which threw the exception
	 */
	private void exceptionHandler(Exception e, String moduleTitle) {
		switch (e.getMessage()) {
		case "Student fully enrolled":
			System.out.println("Student " + studentNumber + " Tried enrolling in " + moduleTitle + " But they already filled their module limit");
			break;
		case "Full":
			System.out.println("Student " + studentNumber + " tried enrolling in " + moduleTitle + " but there aren't any spaces");
			break;
		case "Module duplicate":
			System.out.println("Student " + studentNumber + " is already enrolled in " + moduleTitle);
			break;
		default:
			System.out.println("could not complete enroll!");
			e.printStackTrace();
			break;
		}
	}
	
	/**
	 * Gets module limit of a student
	 * @return Module limit 
	 */
	public int getModuleLimit() {
		return moduleLimit;
	}

	/**
	 * Gets a student's student number
	 * @return Student number
	 */
	public int getStudentNumber() {
		return studentNumber;
	}

	/**
	 * Gets an ArrayList of the students currently enrolled modules
	 * @return ArrayList of enrolled modules
	 */
	public ArrayList<Module> getEnrolledModules() {
		return enrolledModules;
	}
}