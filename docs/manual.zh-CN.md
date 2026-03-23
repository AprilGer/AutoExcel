# 使用手册

## 1. 定义模型

- 类上使用 `@ExcelSheet`
- 字段上使用 `@ExcelColumn`
- 可选使用 `@ExcelComment`、`@ExcelStyle`、`@ExcelTemplate`

## 2. 注入组件

- Spring Boot 项目注入 `AutoExcel`

## 3. 导入

- `autoExcel.importFrom("poi", inputStream, Bean.class, Locale.SIMPLIFIED_CHINESE)`

## 4. 导出

- `autoExcel.exportTo("poi", outputStream, Bean.class, data, Locale.SIMPLIFIED_CHINESE)`

## 5. 异步导出

- `autoExcel.exportAsync(...)`

## 6. 扩展

- 自定义转换器：实现 `ValueConverter`
- 自定义校验器：实现 `ValueValidator`
- 自定义监听器：实现 `ExcelLifecycleListener`
- 自定义引擎：实现 `ExcelEngineAdapter`
