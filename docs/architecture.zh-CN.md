# 架构图

```text
annotation -> core -> adapter SPI -> poi/easyexcel/jxls
                     -> converter/validator/event/exception/util
spring-boot-starter -> 自动配置 -> sample-web
benchmark -> 性能对比
```

- `MetadataScanner` 负责解析注解模型。
- `AnnotationMappingEngine` 负责对象映射和校验。
- `ExcelEngineAdapter` 提供可插拔引擎扩展点。
- Starter 自动装配 `AutoExcel` 与默认适配器。
