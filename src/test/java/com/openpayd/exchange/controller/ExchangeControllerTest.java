package com.openpayd.exchange.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.openpayd.exchange.dto.request.GetRateRequest;
import com.openpayd.exchange.gateway.jackson.ObjectMapperFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.restdocs.RestDocumentationContextProvider;
import org.springframework.restdocs.RestDocumentationExtension;
import org.springframework.restdocs.mockmvc.RestDocumentationResultHandler;
import org.springframework.restdocs.payload.JsonFieldType;
import org.springframework.restdocs.snippet.Attributes;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.util.HashMap;
import java.util.Map;

import static org.hamcrest.CoreMatchers.is;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.documentationConfiguration;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.preprocessRequest;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.preprocessResponse;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.prettyPrint;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.requestFields;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@ExtendWith({
        RestDocumentationExtension.class,
        SpringExtension.class
})
public class ExchangeControllerTest {
    private MockMvc mockMvc;

    private ObjectMapper mapper = ObjectMapperFactory.instance;

    @BeforeEach
    public void setUp(WebApplicationContext context,
                      RestDocumentationContextProvider restDocumentation) {

        mockMvc = MockMvcBuilders.webAppContextSetup(context)
                .apply(documentationConfiguration(restDocumentation)
                        .snippets()
                        .withEncoding("UTF-8"))
                .build();
    }

    @Test
    public void rate_missingSourceParameter() throws Exception {
        Map<String, Object> request = new HashMap<>();
        request.put("target", "TRY");

        MockHttpServletRequestBuilder builder = MockMvcRequestBuilders
                .post("/rate")
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsBytes(request));

        RestDocumentationResultHandler document = document("{class-name}/{method-name}",
                preprocessRequest(prettyPrint()),
                preprocessResponse(prettyPrint()),
                responseFields(
                        fieldWithPath("code")
                                .type(JsonFieldType.STRING)
                                .description("Error code to describe the cause of "),
                        fieldWithPath("message")
                                .type(JsonFieldType.STRING)
                                .description("Message to describe exception ")
                )
        );

        mockMvc.perform(builder)
                .andExpect(status().isBadRequest())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.code", is("INVALID_REQUEST")))
                .andDo(document);
    }

    @Test
    public void rate_missingTargetParameter() throws Exception {
        Map<String, Object> request = new HashMap<>();
        request.put("source", "TRY");

        MockHttpServletRequestBuilder builder = MockMvcRequestBuilders
                .post("/rate")
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsBytes(request));

        mockMvc.perform(builder)
                .andExpect(status().isBadRequest())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.code", is("INVALID_REQUEST")));
    }

    @Test
    public void rate_invalidCurrencyInSource() throws Exception {
        Map<String, Object> request = new HashMap<>();
        request.put("source", "T");
        request.put("target", "TRY");

        MockHttpServletRequestBuilder builder = MockMvcRequestBuilders
                .post("/rate")
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsBytes(request));

        RestDocumentationResultHandler document = document("{class-name}/{method-name}",
                preprocessRequest(prettyPrint()),
                preprocessResponse(prettyPrint()),
                responseFields(
                        fieldWithPath("code")
                                .type(JsonFieldType.STRING)
                                .description("Error code to describe the cause of "),
                        fieldWithPath("message")
                                .type(JsonFieldType.STRING)
                                .description("Message to describe exception ")
                )
        );

        mockMvc.perform(builder)
                .andExpect(status().isBadRequest())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.code", is("INVALID_CURRENCY_CODE")))
                .andDo(document);
    }

    @Test
    public void rate_invalidCurrencyInTarget() throws Exception {
        Map<String, Object> request = new HashMap<>();
        request.put("source", "TRY");
        request.put("target", "G");

        MockHttpServletRequestBuilder builder = MockMvcRequestBuilders
                .post("/rate")
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsBytes(request));

        mockMvc.perform(builder)
                .andExpect(status().isBadRequest())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.code", is("INVALID_CURRENCY_CODE")));
    }

    @Test
    public void rate_success() throws Exception {
        Map<String, Object> request = new HashMap<>();
        request.put("source", "TRY");
        request.put("target", "GBP");

        MockHttpServletRequestBuilder builder = MockMvcRequestBuilders
                .post("/rate")
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsBytes(request));

        ConstrainedFields fields = new ConstrainedFields(GetRateRequest.class);

        RestDocumentationResultHandler document = document("{class-name}/{method-name}",
                preprocessRequest(prettyPrint()),
                preprocessResponse(prettyPrint()),
                requestFields(
                        fields.withPath("source")
                                .type(JsonFieldType.STRING)
                                .description("Base `ISO 4217` currency code for rate"),

                        fields.withPath("target")
                                .type(JsonFieldType.STRING)
                                .description("Target `ISO 4217` currency code for rate")
                ),

                responseFields(
                        fieldWithPath("rate")
                                .type(JsonFieldType.NUMBER)
                                .description("Exchange rate between source and target currencies")
                )
        );

        mockMvc.perform(builder)
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.rate").isNumber())
                .andDo(document);
    }
}