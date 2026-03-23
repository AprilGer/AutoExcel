package org.shixuan.autoexcel.sample.web;

import java.io.ByteArrayOutputStream;
import java.nio.charset.StandardCharsets;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.MvcResult;

@SpringBootTest
@AutoConfigureMockMvc
class ExcelDemoControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @Test
    void shouldExportSingle() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/excel/export/single"))
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    void shouldExportTemplate() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/excel/export/template"))
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    void shouldExportChunk() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/excel/export/chunk").param("size", "100"))
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    void shouldRunAsyncExportFlow() throws Exception {
        MvcResult start = mockMvc.perform(MockMvcRequestBuilders.get("/excel/export/async/start").param("size", "20"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andReturn();
        String taskId = start.getResponse().getContentAsString(StandardCharsets.UTF_8);
        Thread.sleep(200L);
        mockMvc.perform(MockMvcRequestBuilders.get("/excel/export/async/" + taskId))
                .andExpect(MockMvcResultMatchers.status().is2xxSuccessful());
    }

    @Test
    void shouldImportAndWriteBackErrors() throws Exception {
        ByteArrayOutputStream output = new ByteArrayOutputStream();
        XSSFWorkbook workbook = new XSSFWorkbook();
        org.apache.poi.ss.usermodel.Sheet sheet = workbook.createSheet("Orders");
        org.apache.poi.ss.usermodel.Row row0 = sheet.createRow(0);
        row0.createCell(0).setCellValue("订单信息");
        row0.createCell(1).setCellValue("客户信息");
        row0.createCell(2).setCellValue("金额信息");
        org.apache.poi.ss.usermodel.Row row1 = sheet.createRow(1);
        row1.createCell(0).setCellValue("订单号");
        row1.createCell(1).setCellValue("客户名称");
        row1.createCell(2).setCellValue("金额");
        org.apache.poi.ss.usermodel.Row row2 = sheet.createRow(2);
        row2.createCell(0).setCellValue("ORD-T");
        row2.createCell(1).setCellValue("客户T");
        workbook.write(output);
        workbook.close();
        MockMultipartFile file = new MockMultipartFile("file", "bad.xlsx", MediaType.APPLICATION_OCTET_STREAM_VALUE, output.toByteArray());
        mockMvc.perform(MockMvcRequestBuilders.multipart("/excel/import/error-writeback").file(file))
                .andExpect(MockMvcResultMatchers.status().isOk());
    }
}
