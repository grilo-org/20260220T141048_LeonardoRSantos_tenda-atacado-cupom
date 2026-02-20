package br.com.tenda.atacado.cupom.application.controller;

import br.com.tenda.atacado.cupom.application.utils.TestUtils;
import br.com.tenda.atacado.cupom.infrastructure.config.AbstractIntegrationTest;
import br.com.tenda.atacado.cupom.infrastructure.config.security.WithMockUser;
import com.github.tomakehurst.wiremock.junit5.WireMockTest;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.UUID;

import static java.util.Objects.requireNonNull;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WireMockTest
@AutoConfigureMockMvc
@SpringBootTest
public class CouponControllerIT extends AbstractIntegrationTest {

    private static final String BASE_URL = "/v1/coupons";

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Nested
    @DisplayName("Create Coupon Controller Integration Tests")
    class CreateCouponControllerIT {

        @Test
        @DisplayName("should create coupon successfully when valid data is provided")
        @WithMockUser
        @Sql(scripts = {"/db/coupon/clean_coupons.sql"}, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
        void shouldCreateCouponSuccessfully() throws Exception {
            String requestBody = TestUtils.getContentFromInputStream(
                    requireNonNull(getClass().getClassLoader()
                            .getResourceAsStream("samples/inputs/coupon/request-to-save-valid-coupon.json"))
            );

            ResultActions response = mockMvc.perform(post(BASE_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(requestBody));

            response.andExpect(status().isCreated())
                    .andExpect(jsonPath("$.id").isNotEmpty())
                    .andExpect(jsonPath("$.code").value("ABC123"))
                    .andExpect(jsonPath("$.status").value("ACTIVE"))
                    .andExpect(jsonPath("$.published").value(false))
                    .andExpect(jsonPath("$.redeemed").value(false))
                    .andExpect(jsonPath("$.description").value("Iure saepe amet. Excepturi saepe inventore nam doloremque voluptatem a. Quaerat odio distinctio eos. Dolor debitis ex molestias nam quae hic suscipit odit nulla. Blanditiis ratione facilis nobis quam deserunt. Doloribus iste corrupti magni ipsum illo beatae consectetur."));
        }

        @Test
        @DisplayName("should return 400 bad request when invalid data is provided")
        @WithMockUser
        @Sql(scripts = {"/db/coupon/clean_coupons.sql"}, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
        void shouldReturnBadRequestWhenInvalidData() throws Exception {
            String requestBody = TestUtils.getContentFromInputStream(
                    requireNonNull(getClass().getClassLoader()
                            .getResourceAsStream("samples/inputs/coupon/request-to-save-invalid-coupon.json"))
            );

            mockMvc.perform(post(BASE_URL)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(requestBody))
                    .andExpect(status().isBadRequest());
        }
    }

    @Nested
    @DisplayName("Get Coupon Controller Integration Tests")
    class GetCouponControllerIT {

        @Test
        @DisplayName("should return coupon when valid id is provided")
        @WithMockUser
        @Sql(scripts = {"/db/coupon/insert_coupons_delete.sql"}, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
        @Sql(scripts = {"/db/coupon/clean_coupons.sql"}, executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
        void shouldReturnCouponById() throws Exception {
            String externalId = jdbcTemplate.queryForObject(
                    "SELECT external_id FROM coupon WHERE code = 'ABC123'",
                    String.class
            );

            mockMvc.perform(get(BASE_URL + "/" + externalId)
                            .contentType(MediaType.APPLICATION_JSON))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.id").value(externalId))
                    .andExpect(jsonPath("$.code").value("ABC123"))
                    .andExpect(jsonPath("$.status").value("ACTIVE"))
                    .andExpect(jsonPath("$.published").value(true))
                    .andExpect(jsonPath("$.redeemed").value(false));
        }

        @Test
        @DisplayName("should return 400 when coupon is not found")
        @WithMockUser
        void shouldReturnErrorWhenCouponNotFound() throws Exception {
            mockMvc.perform(get(BASE_URL + "/" + UUID.randomUUID())
                            .contentType(MediaType.APPLICATION_JSON))
                    .andExpect(status().isBadRequest());
        }
    }

    @Nested
    @DisplayName("Delete Coupon Controller Integration Tests")
    class DeleteCouponControllerIT {

        @Test
        @DisplayName("should soft delete coupon changing status to DELETED")
        @WithMockUser
        @Sql(scripts = {"/db/coupon/insert_coupons_delete.sql"}, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
        @Sql(scripts = {"/db/coupon/clean_coupons.sql"}, executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
        void shouldSoftDeleteCoupon() throws Exception {

            mockMvc.perform(MockMvcRequestBuilders.delete(BASE_URL + "/ABC123")
                            .contentType(MediaType.APPLICATION_JSON))
                    .andExpect(status().isOk());

            Integer deletedCount = jdbcTemplate.queryForObject(
                    "SELECT COUNT(*) FROM coupon WHERE code = 'ABC123' AND deleted_at IS NOT NULL AND coupon_status = 3",
                    Integer.class
            );

            Assertions.assertEquals(1, deletedCount.intValue());
        }

        @Test
        @DisplayName("should return 400 when trying to delete an already deleted coupon")
        @WithMockUser
        @Sql(scripts = {"/db/coupon/insert_coupons_delete.sql"}, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
        @Sql(scripts = {"/db/coupon/clean_coupons.sql"}, executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
        void shouldReturnErrorWhenDeletingAlreadyDeletedCoupon() throws Exception {
            mockMvc.perform(MockMvcRequestBuilders.delete(BASE_URL + "/ABC123")
                            .contentType(MediaType.APPLICATION_JSON))
                    .andExpect(status().isOk());

            mockMvc.perform(MockMvcRequestBuilders.delete(BASE_URL + "/ABC123")
                            .contentType(MediaType.APPLICATION_JSON))
                    .andExpect(status().isBadRequest());
        }
    }
}
