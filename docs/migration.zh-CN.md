# 版本迁移说明

## 0.x 升级到 1.x

- 将手写 POI 映射替换为注解映射。
- 将自定义转换逻辑迁移到 `ValueConverter`。
- 将数据校验逻辑迁移到 `ValueValidator`。
- 通过 SPI 或 Spring Bean 注册引擎实现。
