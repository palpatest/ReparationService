package org.reparationservice.rest.controllers.standalone;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.io.IOException;
import org.junit.Before;
import org.junit.Test;
import org.reparationservice.rest.controllers.AddWorkerController;
import org.reparationservice.rest.requests.AddWorkerJsonRequest;
import org.reparationservice.rest.utils.TestUtil;
import org.reparationservice.rest.utils.doubles.InteractorFactoryStub;
import org.reparationservice.rest.utils.doubles.RequestBuilderStub;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import reparationservice.entities.worker.WorkerGateway;
import reparationservice.entities.worker.WorkerGatewaySpy;

public class AddWorkerCtrlTest {
  private static final String WORKER_USERNAME_1 = "username1";
  private static final String WORKER_USERNAME_2 = "username2";
  private static final MediaType JSON_HAL_CONTENT_TYPE = TestUtil.JSON_HAL_CONTENT_TYPE;
  private AddWorkerController addWorkerCtrl;
  private MockMvc mockMvc;
  private InteractorFactoryStub intFactoryStub;
  private RequestBuilderStub requestBuilderStub;
  private WorkerGateway workerGW;

  @Before
  public void setup() throws Exception {
    intFactoryStub = InteractorFactoryStub.newInstance();
    requestBuilderStub = new RequestBuilderStub();
    workerGW = new WorkerGatewaySpy();
    addWorkerCtrl = new AddWorkerController(intFactoryStub, workerGW, requestBuilderStub);
    mockMvc = MockMvcBuilders.standaloneSetup(addWorkerCtrl).build();
    sendWorkerPostRequestFor(WORKER_USERNAME_1);
  }

  @Test
  public void callToAddWorkerInteractorWithGivenGW() throws Exception {
    assertThat(intFactoryStub.wasMakeAddWorkerInteractorCalled()).isTrue();
    assertThat(intFactoryStub.getWorkerGateway()).isInstanceOf(workerGW.getClass());
  }

  @Test
  public void createdRequest() throws Exception {
    assertRequestFor(WORKER_USERNAME_1);
    sendWorkerPostRequestFor(WORKER_USERNAME_2);
    assertRequestFor(WORKER_USERNAME_2);
  }

  @Test
  public void executeWasCalled() throws Exception {
    assertThat(intFactoryStub.wasExecuteCalled()).isTrue();
  }

  private void assertRequestFor(String workerUsername) {
    assertThat(requestBuilderStub.wasAddWorkerRequestBuildCalled()).isTrue();
    assertThat(requestBuilderStub.withArgs(workerUsername)).isTrue();
  }

  private void sendWorkerPostRequestFor(String username) throws Exception, IOException {
    mockMvc.perform(
        post("/workers")
            .contentType(JSON_HAL_CONTENT_TYPE)
            .content(getJsonWorkerReq(username)))
        .andDo(print())
        .andExpect(status().isCreated());
  }

  private byte[] getJsonWorkerReq(String username) throws IOException {
    return TestUtil.object2JsonBytes(AddWorkerJsonRequest.newInstance(username));
  }
}
