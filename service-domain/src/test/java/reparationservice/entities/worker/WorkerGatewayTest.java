package reparationservice.entities.worker;

import static org.assertj.core.api.Assertions.*;

import org.junit.Before;
import org.junit.Test;

import reparationservice.entities.worker.Worker;
import reparationservice.entities.worker.WorkerDTO;
import reparationservice.entities.worker.WorkerGateway;
import reparationservice.persistenceimpls.inmemory.InMemoryConfigurator;

public class WorkerGatewayTest {
	private static final String WORKER_USER_NAME_1 = "UserName1";
	private static final String WORKER_USER_NAME_2 = "UserName2";
	private WorkerGateway workers;

	@Before
	public void setUp() {
		workers = InMemoryConfigurator.getWorkerGateway();
	}

	@Test
	public void returnNullWorkerWhenWorkerNotFound() {
		assertThat(workers.getWorkerByUserName(WORKER_USER_NAME_1))
				.isEqualTo(Worker.NULL);
	}
	
	@Test
	public void testAddWorker() {
		workers.addWorker(newWorkerDTO(WORKER_USER_NAME_1));
		workers.addWorker(newWorkerDTO(WORKER_USER_NAME_2));

		Worker worker1 = workers
				.getWorkerByUserName(WORKER_USER_NAME_1);
		assertThat(worker1.getUserName()).isEqualTo(WORKER_USER_NAME_1);

		Worker worker2 = workers
				.getWorkerByUserName(WORKER_USER_NAME_2);
		assertThat(worker2.getUserName()).isEqualTo(WORKER_USER_NAME_2);
	}

	private WorkerDTO newWorkerDTO(String userName) {
		return new WorkerDTO(userName);
	}
}
