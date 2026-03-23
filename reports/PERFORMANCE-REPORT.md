# Performance Report

- Module: `autoexcel-benchmark`
- Command: `mvn -f autoexcel-benchmark/pom.xml exec:java "-Dexec.mainClass=org.shixuan.autoexcel.benchmark.BenchmarkRunner" "-Dexec.args=5000"`
- Metrics: native POI vs AutoExcel(POI) vs AutoExcel(EasyExcel adapter)
- Sample size: 5000 rows
- Result:
  - native-poi-ms = 814
  - autoexcel-poi-ms = 167
  - autoexcel-easyexcel-ms = 2
