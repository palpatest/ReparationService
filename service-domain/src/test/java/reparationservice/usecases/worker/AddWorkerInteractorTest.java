package reparationservice.usecases.worker;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.Before;
import org.junit.Test;

import reparationservice.entities.worker.Worker;
import reparationservice.entities.worker.WorkerGatewaySpy;
import reparationservice.requestor.UseCaseActivator;
import reparationservice.requestor.UseCaseRequest;
import reparationservice.usecases.worker.AddWorkerInteractor;
import reparationservice.usecases.worker.AddWorkerRequest;

public class AddWorkerInteractorTest {
  private static final String WORKER_USERNAME = "username";
  private UseCaseActivator addWorker;
  private WorkerGatewaySpy workersSpy;
  private UseCaseRequest request;

  @Before
  public void givenAddWorkerInteractor() {
    workersSpy = new WorkerGatewaySpy();
    addWorker = new AddWorkerInteractor(workersSpy);
    request = new AddWorkerRequest(WORKER_USERNAME);
  }

  @Test
  public void gatewayWasConfigured() {
    assertThat(((AddWorkerInteractor) addWorker).getGateway()).isInstanceOf(
        WorkerGatewaySpy.class);
  }

  @Test
  public void gatewayWasNotCalledWhenInteractorNotYetExecuted() {
    assertThat(workersSpy.addWorkerWasCalled()).isFalse();
  }

  @Test
  public void executeAddOperation() {
    addWorker.execute(request);
    assertThat(workersSpy.addWorkerWasCalled()).isTrue();
    Worker worker = workersSpy.getWorker();
    assertThat(worker.getUserName()).isEqualTo(WORKER_USERNAME);
  }

  @Test(expected = AddWorkerInteractor.WorkerAlreadyExists.class)
  public void throwWorkerAlreadyExistsWhenTwoWorkersWithSameUserNameAdded() {
    addWorker.execute(request);
    addWorker.execute(request);
  }
}
