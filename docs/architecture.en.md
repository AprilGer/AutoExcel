# Architecture

```text
annotation -> core -> adapter SPI -> poi/easyexcel/jxls
                     -> converter/validator/event/exception/util
spring-boot-starter -> auto configuration -> sample-web
benchmark -> performance comparison
```

- Annotation metadata is scanned by `MetadataScanner`.
- `AnnotationMappingEngine` maps rows and beans.
- `ExcelEngineAdapter` is SPI for pluggable engines.
- Starter auto-registers adapters and `AutoExcel`.
