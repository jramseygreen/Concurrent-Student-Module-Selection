import java.util.concurrent.Semaphore;

/**
 * This class simulates a module
 * @author Joshua Green 956213
 *
 */
public class Module {
	private String moduleCoordinator;
	private Semaphore semaphore;
	private String moduleTitle;
	private String moduleCode;
	
	/**
	 * Constructor creates a semaphore with the number of permits being equal to the module's module limit
	 * @param moduleTitle Title of the module
	 * @param moduleCode The code of the module
	 * @param moduleCoordinator The lecturer coordinating the module
	 * @param moduleLimit The limit of students who can take this module
	 */
	public Module(String moduleTitle, String moduleCode, String moduleCoordinator, int moduleLimit) {
		semaphore = new Semaphore(moduleLimit);
		this.moduleTitle = moduleTitle;
		this.moduleCode = moduleCode;
		this.moduleCoordinator = moduleCoordinator;
	}

	/**
	 * This is a synchronized method which attempts to enroll the student to this module
	 * @param student The student to be enrolled
	 * @throws Exception Exception detailing why the enroll couldn't be completed
	 * @throws InterruptedException Exception when the thread is interrupted
	 */
	public synchronized void enroll(Student student) throws Exception, InterruptedException {

		// if student has filled their module limit
		if (student.getEnrolledModules().size() == student.getModuleLimit()) {
			throw new Exception("Student fully enrolled");
		} else
			
		// If they are already enrolled here
		if (student.getEnrolledModules().contains(this)) {
			throw new Exception("Module duplicate");
		} else
			
		// try acquire permit from here and add it to student's enrolled modules list
		if (semaphore.tryAcquire()) {
			student.getEnrolledModules().add(this);
			System.out.println("	Student " + student.getStudentNumber() + " enrolled in " + getModuleTitle());
		} else {
			
			// Otherwise the module is full
			throw new Exception("Full");
		}
	}

	/**
	 * This is a synchronized method which attempts to transfer the student from this module to another one
	 * @param student Student to transfer
	 * @param newModule Module to attempt to transfer to
	 * @throws Exception Exception detailing why the transfer couldn't be completed
	 * @throws InterruptedException Exception when the thread is interrupted
	 */
	public synchronized void transfer(Student student, Module newModule) throws Exception, InterruptedException {

		// check if the student is already enrolled in the proposed new module
		if (student.getEnrolledModules().contains(newModule)) {
			throw new Exception("Module duplicate");
		} else
			
		// try acquire permit from new module.
		if (newModule.getSemaphore().tryAcquire()) {
			
			// un-enrolls student from this module
			semaphore.release();
			student.getEnrolledModules().remove(this);
			System.out.println("	Student " + student.getStudentNumber() + " Left " + getModuleTitle());

			// enrolls in new module
			student.getEnrolledModules().add(newModule);
			System.out.println("	Student " + student.getStudentNumber() + " transferred to " + newModule.getModuleTitle());
		} else {
			
			// Otherwise the module is full
			throw new Exception("Full");
		}

	}

	/**
	 * Gets the title of the module
	 * @return Module title
	 */
	public String getModuleTitle() {
		return moduleTitle;
	}

	/**
	 * Gets the semaphore object holding the number of available spots in this module
	 * @return Semaphore object
	 */
	public Semaphore getSemaphore() {
		return semaphore;
	}

	/**
	 * Gets the lecturer who coordinates the module
	 * @return module coordinator
	 */
	public String getModuleCoordinator() {
		return moduleCoordinator;
	}

	/**
	 * Gets the code of the module
	 * @return Module code
	 */
	public String getModuleCode() {
		return moduleCode;
	}

	/**
	 * Checks if the semaphore has an available permit
	 * @return true if there is a permit available
	 */
	public boolean hasPermit() {
		boolean status = semaphore.availablePermits() > 0 ? true : false;
		return status;
	}

}