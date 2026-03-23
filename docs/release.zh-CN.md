# Maven Central 发布说明

1. 在 `settings.xml` 配置 Sonatype 账号。
2. 配置 GPG 签名密钥。
3. 执行：

```bash
mvn clean deploy -Prelease
```

4. 在 Sonatype 平台关闭并发布 staging 仓库。
