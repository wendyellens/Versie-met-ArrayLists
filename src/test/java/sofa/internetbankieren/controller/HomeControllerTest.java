package sofa.internetbankieren.controller;
/**
 * @author Wichert aangemaakt op 14-12
 */


import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(HomeController.class)
class HomeControllerTest {

    @Autowired
    private MockMvc mockMvc;

    // even wat probeersels

    @Test
    void startHandler() {
        try {
            MockHttpServletRequestBuilder postRequest =
                    MockMvcRequestBuilders.get("/");
            ResultActions response = mockMvc.perform(postRequest);
            response.andDo(print()).
                    andExpect(status().isOk());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    void homeHandler() {
        try {
            MockHttpServletRequestBuilder postRequest =
                    MockMvcRequestBuilders.get("/home");
            ResultActions response = mockMvc.perform(postRequest);
            response.andDo(print()).
                    andExpect(status().isOk());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}