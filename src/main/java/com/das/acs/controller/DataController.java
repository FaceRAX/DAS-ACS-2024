package com.das.acs.controller;

import com.das.acs.model.dto.DataExport;
import com.das.acs.service.DataService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/data")
public class DataController {
    @Autowired
    private DataService dataService;

    @GetMapping("/export")
    public ResponseEntity<DataExport> exportData() {
        return ResponseEntity.ok(dataService.exportAll());
    }

    @PostMapping("/import")
    @ResponseBody
    public ResponseEntity<Void> importData(@RequestBody DataExport data) {
        dataService.importAll(data);
        return ResponseEntity.ok().build();
    }
}
