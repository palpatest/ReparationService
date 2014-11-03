package org.reparationservice.rest.controllers.standalone;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.io.IOException;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.reparationservice.rest.controllers.AddWorkerController;
import org.reparationservice.rest.requestor.InteractorFactoryImpl;
import org.reparationservice.rest.requests.AddWorkerJsonRequest;
import org.reparationservice.rest.utils.TestUtil;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import de.bechte.junit.runners.context.HierarchicalContextRunner;
import doubles.InteractorFactoryStub;
import reparationservice.Configurator;
import reparationservice.gateways.WorkerGateway;

@RunWith(HierarchicalContextRunner.class)
public class AddWorkerCtrlTest {
  private static final String WORKER_USERNAME_1 = "username1";
  private static final String WORKER_USERNAME_2 = "username2";
  private static final MediaType JSON_HAL_CONTENT_TYPE = TestUtil.JSON_HAL_CONTENT_TYPE;
  private AddWorkerController addWorkerCtrl;
  private MockMvc mockMvc;
  private WorkerGateway workerGW;

  public class Unit {
    private InteractorFactoryStub intActivatorStub;

    @Before
    public void setup() throws Exception {
      intActivatorStub = InteractorFactoryStub.newInstance();
      addWorkerCtrl = new AddWorkerController(intActivatorStub);
      mockMvc = MockMvcBuilders.standaloneSetup(addWorkerCtrl).build();
      sendWorkerPostRequestFor(WORKER_USERNAME_1);
    }

    @Test
    public void callToAddWorkerInteractor() throws Exception {
      assertThat(intActivatorStub.getCalledInteractorName()).isEqualTo(
          InteractorFactoryImpl.ADD_WORKER_INTERACTOR);
    }
    
    @Test
    public void executeWasCalled() throws Exception {
      assertThat(intActivatorStub.wasExecuteCalled()).isTrue();
    }
  }

  public class Integration {
    @Before
    public void setup() {
      workerGW = Configurator.getWorkerGateway();
      addWorkerCtrl = new AddWorkerController(new InteractorFactoryImpl(workerGW));
      mockMvc = MockMvcBuilders.standaloneSetup(addWorkerCtrl).build();
    }

    @Test
    public void successfulWorkerCreation() throws Exception {
      sendWorkerPostRequestFor(WORKER_USERNAME_1);
      sendWorkerPostRequestFor(WORKER_USERNAME_2);

      assertThat(workerGW.getWorkerByUserName(WORKER_USERNAME_1).getUserName())
          .isEqualTo(WORKER_USERNAME_1);
      assertThat(workerGW.getWorkerByUserName(WORKER_USERNAME_2).getUserName())
          .isEqualTo(WORKER_USERNAME_2);
    }
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