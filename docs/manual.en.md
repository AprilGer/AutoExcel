# User Manual

## 1. Define model

- Annotate class with `@ExcelSheet`
- Annotate fields with `@ExcelColumn`
- Optionally use `@ExcelComment`, `@ExcelStyle`, `@ExcelTemplate`

## 2. Inject component

- Inject `AutoExcel` in Spring Boot application

## 3. Import

- `autoExcel.importFrom("poi", inputStream, Bean.class, Locale.CHINA)`

## 4. Export

- `autoExcel.exportTo("poi", outputStream, Bean.class, data, Locale.CHINA)`

## 5. Async export

- `autoExcel.exportAsync(...)`

## 6. Extend

- Custom converter: implement `ValueConverter`
- Custom validator: implement `ValueValidator`
- Custom listener: implement `ExcelLifecycleListener`
- Custom engine: implement `ExcelEngineAdapter`
